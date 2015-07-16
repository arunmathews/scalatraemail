package com.github.arunmathews.email

import com.github.arunmathews.email.controller.FailoverEmailComponentConcrete
import com.github.arunmathews.email.service.{EmailServlet, RootServlet}
import org.scalatest.concurrent.ScalaFutures
import org.scalatra.test.specs2._

// For more on Specs2, see http://etorreborre.github.com/specs2/guide/org.specs2.guide.QuickStart.html
class EmailServletSpec extends ScalatraSpec with ScalaFutures { def is =
  "GET / on EmailServlet"                     ^
    "should return status 404"                  ! root405^
    "POST / on EmailServlet"                  ^
    "should return status 200"                  ! root200^
  end

  addServlet(new EmailServlet(
    new FailoverEmailComponentConcrete(
      new FailingSendEmailComponent(),
      new SucceedingSendEmailComponent())), "/*")

  def root405 = get("/") {
    status must_== 405
  }

  val rbody = """{"person": {"firstName":"xyz","lastName":"xyz", "emailAddress":"xyz@xyz.com"}, "emailType": "Birthday"}"""
  def root200 = post("/", headers = Map("Accept" -> "application/json", "Content-Type" -> "application/json"), body = rbody) {
    status must_== 200
    body must_== """{"result":true}"""
  }
}
