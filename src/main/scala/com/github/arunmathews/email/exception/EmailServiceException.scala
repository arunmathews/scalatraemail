package com.github.arunmathews.email.exception

class EmailServiceException(msg: String, cause: Throwable) extends RuntimeException(msg, cause) {
  def this() = this(null, null)
  def this(msg: String) = this(msg, null)
  def this(cause: Throwable) = this(cause.getMessage, cause)
}
