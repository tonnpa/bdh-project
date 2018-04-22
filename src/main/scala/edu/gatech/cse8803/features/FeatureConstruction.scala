package edu.gatech.cse8803.features

import org.apache.spark.ml.feature.StopWordsRemover
import org.apache.spark.ml.feature.Tokenizer
import org.apache.spark.ml.feature.{CountVectorizer, CountVectorizerModel}
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.rdd.RDD
import org.apache.spark.SparkContext
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.Row

object FeatureConstruction {

  def construct(sc: SparkContext, note_df: DataFrame, onyx: Array[String]): RDD[(Long, (String, Vector))] = {
    val tokenizer = new Tokenizer().setInputCol("TEXT").setOutputCol("WORDS")
    val tokenized_df = tokenizer.transform(note_df)

    val remover = new StopWordsRemover().setInputCol("WORDS").setOutputCol("FILTERED").setStopWords(onyx)
    val filtered_df = remover.transform(tokenized_df)

    val cvModel: CountVectorizerModel = new CountVectorizer().
      setInputCol("FILTERED").setOutputCol("FEATURES").setVocabSize(100000).setMinDF(2).setMinTF(5).
      fit(filtered_df)

    val feature_df = cvModel.transform(filtered_df)
    val feature_rdd = feature_df.select("SUBJECT_ID", "FEATURES").map{
      case Row(subjectId: String, features: Vector) => (subjectId, features)
    }.zipWithIndex.map(_.swap)

    feature_rdd
  }

  def getCorpus(feature_rdd: RDD[(Long, (String, Vector))]): RDD[(Long, Vector)] = {
    val corpus = feature_rdd.map{
      case (idx, (subjectId, features)) => (idx, features)
    }

    corpus
  }
}


