package edu.gatech.cse8803.clustering

import org.apache.spark.mllib.clustering.{LDA, DistributedLDAModel}
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.rdd.RDD

object LDA {

  def run(corpus: RDD[(Long, Vector)]): RDD[(Long, Vector)] = {
    val ldaModel: DistributedLDAModel = new LDA().setK(50).setMaxIterations(10).
      run(corpus).asInstanceOf[DistributedLDAModel]
    val topicDistributions = ldaModel.toLocal.topicDistributions(corpus)

    topicDistributions
  }

}
