package com.github.arunmathews.email

import com.github.arunmathews.email.service.{RootServlet, EmailServlet}
import org.scalatest.concurrent.ScalaFutures
import org.scalatra.test.specs2._

class RootServletSpec extends ScalatraSpec { def is =
  "GET / on RootServlet"                     ^
    "should return status 404"                  ! root404^
                                                end

  addServlet(classOf[RootServlet], "/*")

  def root404 = get("/") {
    status must_== 404
  }
}
