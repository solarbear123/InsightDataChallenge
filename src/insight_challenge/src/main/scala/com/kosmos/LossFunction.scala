package com.kosmos

object LossFunction {

  /**
    *
    * @param a actual prices by (hour|stock)
    * @param b predicted prices by (hour|stock)
    * @return  ordered (in increasing hour) sequence of collections
    *          of abs(predicted-actual). stock names not included.
    */
  def absDiff(a: Map[String, Double], b: Map[String, Double]): Seq[Seq[Double]] = {
    val innerJoin = for ((k, v) <- b if a.contains(k)) yield (k, math.abs(a(k) - v))

    innerJoin.toSeq.map(x => (x._1.split("\\|")(0).toInt, x._2))
      .groupBy(_._1)
      .mapValues(x => for (e <- x) yield e._2)
      .toSeq
      .sortBy(_._1)
      .map(_._2)  // Seq[Seq(difference))]
  }

  def absDiff_V2(a: Map[String, Double], b: Map[String, Double]): Seq[Seq[Double]] = {
    val leftJoin = for ((k, v) <- a)
      yield (k, if (b.contains(k)) math.abs(a(k) - b(k)) else -1.0)

    leftJoin.toSeq.map(x => (x._1.split("\\|")(0).toInt, x._2))
      .groupBy(_._1)
      .mapValues(x => for (e <- x) yield e._2)
      .toSeq
      .sortBy(_._1)
      .map(_._2)  // Seq[Seq(difference))]. difference = -1.0 if prediction is missing
  }
}