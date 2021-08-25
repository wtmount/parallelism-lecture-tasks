package edu.alexy.lectures.concurrency

import edu.alexy.lectures.concurrency.model.Company
import edu.alexy.lectures.concurrency.util.{Db, DbError, WebApi, WebApiError}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

object RoboAdviser {
  // Task 1.
  // Return 'AAPL' revenue from `Db.getCompanyLastFinancials`. Possible error should be returned as a ServiceError.
  def getAAPLRevenue: Future[Double] = {
    Db.getCompanyLastFinancials("AAPL").transform {
      case Success(Some(company)) => Success(company.revenue)
      case _ => Failure(DbError)
    }
  }

  // Task 2.
  // Implement a fallback strategy for 'Db.getAllTickers'.
  // 'Db.getAllTickers' should be called no more then 'retries' times.
  // Possible error should be returned as a ServiceError.
  def getAllTickersRetryable(retries: Int = 10): Future[Seq[String]] = {
    if (retries <= 0) Future.failed(DbError)
    else Db.getAllTickers.fallbackTo(getAllTickersRetryable(retries - 1))
  }

  // Task 3.
  // Implement a fallback strategy for 'Db.getCompanyLastFinancials'.
  // 'Db.getCompanyLastFinancials' should be called no more then 'retries' times.
  // Possible error should be returned as a ServiceError.
  def getCompanyRetryable(ticker: String, retries: Int = 10): Future[Option[Company]] = {
    if (retries <= 0) Future.failed(DbError)
    else Db.getCompanyLastFinancials(ticker).fallbackTo(getCompanyRetryable(ticker, retries - 1))
  }

  // Task 4.
  // Implement a fallback strategy 'WebApi.getPrice'.
  // 'WebApi.getPrice' should be called no more then 'retries' times.
  // Possible error should be returned as a ServiceError.
  def getPriceRetryable(ticker: String, retries: Int = 10): Future[Double] = {
    if (retries <= 0) Future.failed(WebApiError)
    else WebApi.getPrice(ticker).fallbackTo(getPriceRetryable(ticker, retries - 1))
  }

  // Task 5.
  // Using retryable functions return all tickers with their real time prices.
  def getAllTickersPrices: Future[Seq[(String, Double)]] = {
    for {
      tickers <- getAllTickersRetryable()
      prices <- Future.traverse(tickers) {
        ticker => getPriceRetryable(ticker)
      }
    } yield tickers zip prices
  }

  // Task 6.
  // Using `getCompanyRetryable` and `getPriceRetryable` functions return a company with its real time stock price.
  def getCompanyFinancialsWithPrice(ticker: String): Future[(Company, Double)] = {
    val company = getCompanyRetryable(ticker)
    val price = getPriceRetryable(ticker)
    for {
      c <- company.map {
        case Some(x) => x
      }
      p <- price
    } yield (c, p)
  }

  // Task 7.
  // Implement a function that returns a list of chip ('Company.isCheap') companies
  // with their real time stock prices using 'getAllTickersRetryable' and
  // 'getCompanyFinancialsWithPrice' functions.
  def buyList: Future[Seq[(Company, Double)]] = {
    for {
      tickers <- getAllTickersRetryable()
      companyPrice <- Future.traverse(tickers) {
        ticker => getCompanyFinancialsWithPrice(ticker)
      }
    } yield companyPrice.filter { case (company, price) => company.isCheap(price) }
  }

  // Task 8.
  // Implement a function that returns a list of expensive ('Company.isExpensive') companies
  // with their real time stock prices using 'getAllTickersRetryable', 'getCompanyRetryable',
  // 'getPriceRetryable' and zipping.
  def sellList: Future[Seq[(Company, Double)]] = {
    for {
      tickers <- getAllTickersRetryable()
      companyPrice <- Future.traverse(tickers) {
        ticker => getCompanyRetryable(ticker) zip getPriceRetryable(ticker)
      }
    } yield companyPrice.filter {
        case (Some(company), price) => company.isExpensive(price)
        case _ => false
    }.map(cp => (cp._1.get, cp._2))
  }
}
