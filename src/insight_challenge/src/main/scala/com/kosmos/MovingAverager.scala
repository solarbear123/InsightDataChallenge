package com.kosmos

/**
  *
  * @param window size of moving average window
  */
class MovingAverager private(val window:Int) {
  private var leftOverChunk: Seq[Seq[Double]] = Seq.empty[Seq[Double]]

  /**
    *
    * @param diffByHour collections of absolute differences ordered in increasing hour
    * @return moving averages
    */
  def getSimpleAverages(diffByHour: Seq[Seq[Double]]) : List[Double] = {
    val chunks: List[Seq[Seq[Double]]] = (leftOverChunk ++ diffByHour).sliding(window).toList
    leftOverChunk = chunks.last.tail
    chunks.map(c => c.flatten.sum/c.flatten.size)
  }

  def getSimpleAverages_V2(diffByHour: Seq[Seq[Double]]) : List[Double] = {
    val chunks: List[Seq[Seq[Double]]] = (leftOverChunk ++ diffByHour).sliding(window).toList
    leftOverChunk = chunks.last.tail
    chunks.map(c =>
      {
        val n = c.flatten.count(x => x > -1)
        if (n > 0)
          c.flatten.filter(x => x > -1).sum/n
        else
          -1.0  // no predicted values in this window at all
      })
  }
}

object MovingAverager {
  def apply(width: Int) = new MovingAverager(width)
}