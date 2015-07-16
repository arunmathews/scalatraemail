package com.github.arunmathews.email

import com.github.arunmathews.email.service.EmailServlet
import org.scalatest.concurrent.ScalaFutures
import org.scalatra.test.specs2._

// For more on Specs2, see http://etorreborre.github.com/specs2/guide/org.specs2.guide.QuickStart.html
class EmailServletSpec extends ScalatraSpec with ScalaFutures { def is =
  "GET / on EmailServlet"                     ^
    "should return status 200"                  ! root200^
                                                end

  addServlet(classOf[EmailServlet], "/*")

  def root200 = get("/") {
    status must_== 200
  }
}
