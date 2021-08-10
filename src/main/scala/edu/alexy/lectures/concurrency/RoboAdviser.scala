package edu.alexy.lectures.concurrency

import edu.alexy.lectures.concurrency.model.Company
import edu.alexy.lectures.concurrency.util.{Db, DbError, WebApi, WebApiError}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object RoboAdviser {
  // Task 1.
  // Return 'AAPL' revenue from `Db.getCompanyLastFinancials`. Possible error should be returned as a ServiceError.
  def getAAPLRevenue: Future[Double] = ???

  // Task 2.
  // Implement a fallback strategy for 'Db.getAllTickers'.
  // 'Db.getAllTickers' should be called no more then 'retries' times.
  // Possible error should be returned as a ServiceError.
  def getAllTickersRetryable(retries: Int = 10): Future[Seq[String]] = ???

  // Task 3.
  // Implement a fallback strategy for 'Db.getCompanyLastFinancials'.
  // 'Db.getCompanyLastFinancials' should be called no more then 'retries' times.
  // Possible error should be returned as a ServiceError.
  def getCompanyRetryable(ticker: String, retries: Int = 10): Future[Option[Company]] = ???

  // Task 4.
  // Implement a fallback strategy 'WebApi.getPrice'.
  // 'WebApi.getPrice' should be called no more then 'retries' times.
  // Possible error should be returned as a ServiceError.
  def getPriceRetryable(ticker: String, retries: Int = 10): Future[Double] = ???

  // Task 5.
  // Using retryable functions return all tickers with their real time prices.
  def getAllTickersPrices: Future[Seq[(String, Double)]] = ???

  // Task 6.
  // Using `getCompanyRetryable` and `getPriceRetryable` functions return a company with its real time stock price.
  def getCompanyFinancialsWithPrice(ticker: String): Future[(Company, Double)] = ???

  // Task 7.
  // Implement a function that returns a list of chip ('Company.isCheap') companies
  // with their real time stock prices using 'getAllTickersRetryable' and
  // 'getCompanyFinancialsWithPrice' functions.
  def buyList: Future[Seq[(Company, Double)]] = ???

  // Task 8.
  // Implement a function that returns a list of expensive ('Company.isExpensive') companies
  // with their real time stock prices using 'getAllTickersRetryable', 'getCompanyRetryable',
  // 'getPriceRetryable' and zipping.
  def sellList: Future[Seq[(Company, Double)]] = ???

}
