package com.github.arunmathews.email.service

import com.github.arunmathews.email.businessobject.BusinessObjects.{CreateEmailOutput, EmailType, CreateEmailInput, Person}
import com.github.arunmathews.email.command.CreateEmailInputCommand
import com.github.arunmathews.email.controller.EmailComponent
import org.json4s
import org.json4s.JsonAST.JValue
import org.json4s.ext.EnumNameSerializer
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.FutureSupport
import org.scalatra.commands.JacksonJsonParsing
import org.scalatra.json._
import org.json4s._
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Promise, ExecutionContext, Future}
import scala.util.{Failure, Success}

class EmailServlet(val emailComponent: EmailComponent)
  extends ScalatraEmailStack
  with JacksonJsonParsing
  with JacksonJsonSupport
  with FutureSupport {
  val logger =  LoggerFactory.getLogger(getClass)

  protected implicit lazy val jsonFormats: Formats = DefaultFormats + new EnumNameSerializer(EmailType)

  before() {
    contentType = formats("json")
  }

  get("/") {
    Person("a", "b", "c")
  }

  protected override def transformRequestBody(body: JValue): JValue = body.camelizeKeys

  protected override def transformResponseBody(body: JValue): JValue = body.underscoreKeys

  post("/") {
      val cmd = command[CreateEmailInputCommand]
      if (cmd.isValid) {
        val person = Person(cmd.firstName.value.get, cmd.lastName.value.get, cmd.emailAddress.value.get)
        val cei = CreateEmailInput(person, EmailType.withName(cmd.emailType.value.get))
        val promise = Promise[CreateEmailOutput]()
        emailComponent.createAndSendEmail(cei).onComplete {
          case r => promise.complete(r)
        }

        promise.future
      }
      else {
        val ves = for {
          errorBinding <- cmd.errors
          error = errorBinding.error
          ve <- error
          es = s"field - ${ve.field.get.name}. error - ${ve.message}"
        } yield es
        halt(400, s"invalid input: $ves")
      }
  }

  override def render(value: json4s.JValue)(implicit formats: Formats): json4s.JValue = ???

  override protected implicit def executor: ExecutionContext = global
}
