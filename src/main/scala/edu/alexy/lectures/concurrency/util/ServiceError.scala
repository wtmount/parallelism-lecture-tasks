package edu.alexy.lectures.concurrency.util

sealed trait ServiceError extends Throwable
case object DbError extends ServiceError
case object WebApiError extends ServiceError