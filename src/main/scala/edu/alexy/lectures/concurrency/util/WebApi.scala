package edu.alexy.lectures.concurrency.util

import scala.concurrent.Future
import scala.util.Random

object WebApi {
  def getPrice(ticker: String): Future[Double] = BlockingSimulator.webApiCall {
    ticker match {
      case "AAPL" => 80 + Random.nextInt(100)
      case "FB" => 250 + Random.nextInt(300)
      case "GOOG" => 2500 + Random.nextInt(4000)
    }
  }
}
