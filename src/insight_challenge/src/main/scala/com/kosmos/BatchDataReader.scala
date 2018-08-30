package com.kosmos

import scala.collection.breakOut
import scala.io.Source

/** Reads one batche of data at a time instead of the entire file
  * Advantageous for scaling to large data files
  *
  * @param filePath input file (actual.txt or predicted.txt)
  */
class BatchDataReader private (val filePath: String, val numHours: Int) {
  private val buf = Source.fromFile(filePath)
  private var restIter = buf.getLines()
  var hour = 0

  /**
    *
    * @param numHours controls batch size
    * @return Key-value pairs of (hour|stock, price)
    */
  def nextBatch: Map[String, Double] = {
    hour += numHours
    val (thisBatch, rest) = restIter.span(s => s.split("\\|")(0).toInt <= hour)
    restIter = rest
    val lines = thisBatch.toList
    val data : Map[String, Double] = lines.map(s => {
      val f = s.split("\\|"); (f(0)+ "|"+ f(1), f(2).toDouble)
    })(breakOut)

    data
  }

  def hasNext : Boolean = restIter.hasNext

  def close(): Unit = buf.close()
}

object BatchDataReader {
  def apply(filePath: String, batchSize: Int)
      = new BatchDataReader(filePath, batchSize)
}
