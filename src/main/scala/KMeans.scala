import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.{SparkConf, SparkContext}
import java.io._

object KMeansScala {

  def main(args: Array[String]): Unit = {

    val conf:SparkConf = new SparkConf().setAppName("KMeansScala").setMaster("local")
    val sc:SparkContext = new SparkContext(conf)

    // Load data
    val data = sc.textFile("src/main/resources/mnist_test.csv")

    var lines = data
      .map(l => Vectors.dense(l.split(",").map(_.toDouble))).cache()

    var clusters = KMeans.train(lines, 10, 100)

    printToFile(new File("target/clusters.txt")) { p =>
      clusters.clusterCenters.foreach(p.println)
    }

    println(clusters.clusterCenters.mkString("\n"))
  }

  def printToFile(f: java.io.File)(op: java.io.PrintWriter => Unit) {
    val p = new java.io.PrintWriter(f)
    try { op(p) } finally { p.close() }
  }
}
