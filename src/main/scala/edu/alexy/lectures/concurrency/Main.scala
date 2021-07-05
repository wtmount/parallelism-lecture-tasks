package edu.alexy.lectures.concurrency

import scala.concurrent.ExecutionContext.Implicits.global


object Main extends App {

  while (true) {
    RoboAdviser.getAllTickersPrices.foreach { tickers =>
      print("Real time quotes: ")
      tickers.foreach { case (ticker, price) =>
        print(s"[$ticker] " + price + "$\t")
      }
      println
    }

    RoboAdviser.buyList.foreach { buys =>
      buys.foreach { case (company, price) =>
        println(s"===> BUY :  ${company.metricsFormatted(price)}")
      }
    }

    RoboAdviser.sellList.foreach { sells =>
      sells.foreach { case (company, price) =>
        println(s"===> SELL :  ${company.metricsFormatted(price)}")
      }
    }

    Thread.sleep(1000)
  }
}
