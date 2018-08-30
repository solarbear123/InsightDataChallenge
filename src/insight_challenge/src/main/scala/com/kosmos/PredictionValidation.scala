package com.kosmos

import java.io.PrintWriter

import scala.io.Source

/** Computes simple moving averages of abs(predicted - actual)
  * Writes results to disk
  * Scalable to large input files
  *
  */
object PredictionValidation {

  def main(args : Array[String]): Unit = {
    if (args.size != 4) {
      println("Arguments: window.txt actual.txt predicted.txt comparison.txt")
      System.exit(1)
    }

    val windowFile = args(0)
    val actualsFile = args(1)
    val predictionsFile = args(2)
    val comparisonFile = args(3)

    val wBuf = Source.fromFile(windowFile)
    val window = wBuf.getLines.toArray.apply(0).toInt  // 4
    wBuf.close()

    val batchSizeInHours = 5000
    val actualsReader = BatchDataReader(actualsFile, batchSizeInHours)
    val predictionsReader = BatchDataReader(predictionsFile, batchSizeInHours)

    val ma = MovingAverager(window)

    val pw = new PrintWriter(new java.io.File(comparisonFile))

    var i = 0

    while (actualsReader.hasNext && predictionsReader.hasNext) {
      val actuals = actualsReader.nextBatch
      val predictions = predictionsReader.nextBatch
      val diff = LossFunction.absDiff_V2(actuals, predictions)
      val averages = ma.getSimpleAverages_V2(diff)


      val output = for (a <- averages) yield {
        if (a < 0) {
          i += 1
          s"$i|${i + window - 1}|NA\n"
        } else {
          i += 1
          f"$i|${i + window - 1}|$a%.2f\n"
        }
      }

      pw.write(output.mkString)
    }

    pw.close()
    actualsReader.close()
    predictionsReader.close()
  }
}

