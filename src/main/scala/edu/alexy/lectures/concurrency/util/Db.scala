package edu.alexy.lectures.concurrency.util

import edu.alexy.lectures.concurrency.model.Company

import scala.concurrent.Future

object Db {
  val companiesFinancials = Seq(
    Company("FB", 2400000000L, 94399000000L, 0.22, 33741000000L, 64220000000L, 12140000000L),
    Company("AAPL", 16690000000L, 89580000000L, 0.5363, 76311000000L, 38470000000L, 134740000000L),
    Company("GOOG", 323580000, 196682000000L, 0.344, 51363000000L, 136694000000L, 28250000000L)
  )

  def getAllTickers: Future[Seq[String]] = BlockingSimulator.dbCall {
    companiesFinancials.map(_.ticker)
  }

  def getCompanyLastFinancials(ticker: String): Future[Option[Company]] = BlockingSimulator.dbCall {
    companiesFinancials.find(_.ticker == ticker.toUpperCase)
  }
}
