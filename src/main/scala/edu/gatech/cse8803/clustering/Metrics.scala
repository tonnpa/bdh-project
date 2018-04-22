/**
 * @author Hang Su <hangsu@gatech.edu>.
 */

package edu.gatech.cse8803.clustering

import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD

object Metrics {
  /**
   * Given input RDD with tuples of assigned cluster id by clustering,
   * and corresponding real class. Calculate the purity of clustering.
   * Purity is defined as
   *             \fract{1}{N}\sum_K max_j |w_k \cap c_j|
   * where N is the number of samples, K is number of clusters and j
   * is index of class. w_k denotes the set of samples in k-th cluster
   * and c_j denotes set of samples of class j.
   * @param clusterAssignmentAndLabel RDD in the tuple format
   *                                  (assigned_cluster_id, class)
   * @return purity
   */
  def purity(clusterAssignmentAndLabel: RDD[(Int, Int)]): Double = {
    clusterAssignmentAndLabel.cache()
    val clusters = clusterAssignmentAndLabel.map(a => a._1).distinct.toArray
    val classes = clusterAssignmentAndLabel.map(a => a._2).distinct.toArray

    val intersectSizes = clusters.map(c => {
      val assignedToK = clusterAssignmentAndLabel.filter(a => a._1 == c)
      val grouped = assignedToK.groupBy(a => a._2)
      val classCounts = grouped.mapValues(l => l.size).toArray
      val maxCount = classCounts.reduce((c1, c2) => if (c1._2 > c2._2) c1 else c2)
      maxCount._2
    })

    val purity = intersectSizes.reduce(_ + _) / clusterAssignmentAndLabel.count.toDouble
    purity
  }
}
