package utils

import scala.annotation.tailrec

object Utils {

  def merge[A, B](seq: Seq[(A, B)]): Map[A, Seq[B]] = {
    @tailrec
    def helper(rest: Seq[(A, B)], result: Map[A, Seq[B]]): Map[A, Seq[B]] = rest match {
      case Seq() => result
      case head +: tail =>
        val newResult =
          if (result.contains(head._1)) {
            result + (head._1 -> (result(head._1) :+ head._2))
          } else {
            result + (head._1 -> Seq[B](head._2))
          }
        helper(tail, newResult)
    }
    helper(seq, Map.empty[A, Seq[B]])
  }


}
