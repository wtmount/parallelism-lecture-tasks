package edu.alexy.lectures.concurrency.model

case class Company(ticker: String,
                   stocksQtty: Long,
                   revenue: Long,
                   growth: Double,
                   netIncome: Long,
                   cash: Long,
                   debt: Long) {

  def capital(stockPrice: Double): Double = stockPrice * stocksQtty
  def enterpriseValue(stockPrice: Double): Double = capital(stockPrice) - cash + debt

  def evToRevenue(stockPrice: Double): Double = enterpriseValue(stockPrice) / revenue
  def evToForwardRevenue(stockPrice: Double): Double = evToRevenue(stockPrice) / (1 + growth)
  def priceToEarnings(stockPrice: Double): Double = capital(stockPrice) / netIncome

  def isCheap(stockPrice: Double, marginOfSafety: Double = 0.2): Boolean = {
    (netIncome > 0 && priceToEarnings(stockPrice) <= 30 * (1 - marginOfSafety)) ||
      (netIncome <= 0 && evToForwardRevenue(stockPrice) <= 10 * (1 - marginOfSafety))
  }

  def isExpensive(stockPrice: Double, margin: Double = 0.1): Boolean = {
    (netIncome > 0 && priceToEarnings(stockPrice) > 30 * (1 + margin)) ||
      (netIncome <= 0 && evToForwardRevenue(stockPrice) > 10 * (1 + margin))
  }

  def metricsFormatted(stockPrice: Double): String =
    s"[$ticker] " + stockPrice + "$ " +
      s"CAP = ${capital(stockPrice)} EV = ${enterpriseValue(stockPrice)} EV/R = ${evToRevenue(stockPrice)} " +
      s"EV/FR = ${evToForwardRevenue(stockPrice)} P/E = ${priceToEarnings(stockPrice)}"
}
