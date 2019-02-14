import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.{SparkConf, SparkContext}
import java.io._
import scala.collection.mutable.ListBuffer

object KMeansScala {

  def main(args: Array[String]): Unit = {

    val maxCluster = 20;
    val maxIteration = 100;

    val conf:SparkConf = new SparkConf().setAppName("KMeansScala").setMaster("local")
    val sc:SparkContext = new SparkContext(conf)

    // Load data
    val data = sc.textFile("src/main/resources/mnist_test.csv")

    val lines = data
      .map(l => Vectors.dense(l.split(",").map(_.toDouble))).cache()

    var centroids = new ListBuffer[Double]()

    for(n <- 1 to maxCluster){
      val clusters = KMeans.train(lines, n, maxIteration);
      centroids += clusters.trainingCost;
    }

    //After we discovered our right number of centroids we save our model
    val clusters = KMeans.train(lines, 10, maxIteration);

    printToFile(new File("target/clusters")) { p => p.println(clusters.clusterCenters.mkString("\n")) }
    //printToFile(new File("target/clusters")) { p => p.println(centroids.toString()) }
    clusters.save(sc, "target/model")

  }

  def printToFile(f: java.io.File)(op: java.io.PrintWriter => Unit) {
    val p = new java.io.PrintWriter(f)
    try { op(p) } finally { p.close() }
  }
}
