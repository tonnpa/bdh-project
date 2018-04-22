package edu.gatech.cse8803.main

import edu.gatech.cse8803.clustering.LDA
import edu.gatech.cse8803.features.FeatureConstruction
import edu.gatech.cse8803.ioutils.CSVUtils
import edu.gatech.cse8803.model.Note
import java.text.SimpleDateFormat
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}
import scala.io.Source

object Main {
  def main(args: Array[String]) {
    import org.apache.log4j.Logger
    import org.apache.log4j.Level

    Logger.getLogger("org").setLevel(Level.ERROR)
    Logger.getLogger("akka").setLevel(Level.ERROR)

    val sc = createContext
    val sqlContext = new SQLContext(sc)

    val note_df = loadRawNotes(sqlContext)
    val onyx_stop_words = loadOnyxStopWords()

    // Feature Construction
    val features = FeatureConstruction.construct(sc, note_df, onyx_stop_words)
    val corpus = FeatureConstruction.getCorpus(features)

    // LDA Clustering
    val topicDistributions = LDA.run(corpus)

    val topics = features.join(topicDistributions).map{
      case (idx, ((subjectId, features), topic)) => (subjectId, topic)
    }

    // Save to Disk
    topics.map{
      case (subjectId, topic) => subjectId.toString + ", " + topic.toString
    }.saveAsTextFile("data/text_features")

    sc.stop 
  }

  def loadRawNotes(sqlContext: SQLContext): DataFrame = {
    val dateFormat = new SimpleDateFormat("yyyy-MM-dd")
    CSVUtils.loadCSVAsTable(sqlContext, "data/M3_NOTEEVENTS.csv", "notes")

    val note_df = sqlContext.sql("SELECT SUBJECT_ID, CHARTDATE, CATEGORY, TEXT FROM notes")
    note_df
  }

  def loadOnyxStopWords(): Array[String] = {
    val onyx = Source.fromFile("data/onyx_stop_words.txt").getLines().toArray[String]
    onyx
  }

  def createContext(appName: String, masterUrl: String): SparkContext = {
    val conf = new SparkConf().setAppName(appName).setMaster(masterUrl)
    new SparkContext(conf)
  }

  def createContext(appName: String): SparkContext = createContext(appName, "local")

  def createContext: SparkContext = createContext("CSE 8803 Project", "local")
}
