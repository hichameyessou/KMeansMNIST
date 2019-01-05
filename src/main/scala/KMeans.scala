import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.{SparkConf, SparkContext}

object KMeansScala {

  def main(args: Array[String]): Unit = {

    val conf:SparkConf = new SparkConf().setAppName("KMeansScala").setMaster("local")
    val sc:SparkContext = new SparkContext(conf)


    // Load data
    val data = sc.textFile("src/main/resources/mnist_test.csv")

    var lines = data
      .map(l => Vectors.dense(l.split(",").map(_.toDouble))).cache()

    var clusters = KMeans.train(lines, 10, 40)

    clusters.save(sc,"target/KMeansModel")
    println(clusters.clusterCenters.mkString("\n"))
  }

}
