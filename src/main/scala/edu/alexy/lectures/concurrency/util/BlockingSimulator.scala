package edu.alexy.lectures.concurrency.util

import java.io.IOException
import java.sql.SQLTimeoutException
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Random

object BlockingSimulator {
  def simulate[T](block: => T, error: Throwable, sleepTime: Long, errorsPercentage: Int): Future[T] = {
    def isError = Random.nextInt(100) < errorsPercentage
    Future {
      Thread.sleep(sleepTime)
      if (isError) throw error else block
    }
  }

  def webApiCall[T](block: => T, sleepTime: Long = 100, errorsPercentage: Int = 20): Future[T] =
    simulate(block, new IOException, sleepTime, errorsPercentage)

  def dbCall[T](block: => T, sleepTime: Long = 100, errorsPercentage: Int = 20): Future[T] =
    simulate(block, new SQLTimeoutException, sleepTime, errorsPercentage)
}
