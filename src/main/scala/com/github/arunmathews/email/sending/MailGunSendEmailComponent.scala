package com.github.arunmathews.email.sending

import com.github.arunmathews.email.businessobject.BusinessObjects.{CreateEmailOutput, Email}
import dispatch._
import org.json4s._
import org.json4s.jackson.Serialization._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Promise
import scala.util.{Failure, Success, Try}

/**
 *
 */
trait MailGunSendEmailComponent extends SendEmailComponent {
  override type E = MailGunEmailSender

  def apiKey: String

  def sender: String

  override val emailSender = new MailGunEmailSender(apiKey, sender)

  class MailGunEmailSender(val apiKey: String, val sender: String) extends EmailSender {
    val root = host("api.mailgun.net").secure / "v3" / "mg.arunmathews.com"
    val sendEmailPath = (root / "messages").as_!("api", apiKey)
    val sendEmailForm = sendEmailPath.setContentType("application/x-www-form-urlencoded", "UTF-8")

    override def sendEmail(email: Email, emailAddress: String): Future[CreateEmailOutput] = {
      val prom = Promise[CreateEmailOutput]()
      sendEmailWithBody(email, emailAddress).onComplete {
        case Success(content) => prom.complete(Try(CreateEmailOutput(None, Option(content), result = true)))
        case Failure(e) => prom.failure(e)
      }
      prom.future
    }

    def sendEmailWithBody(email: Email, emailAddress: String): Future[String] = {
      val sendEmailWithBody = sendEmailPath << Map("from" -> sender,
        "to" -> emailAddress,
        "subject" -> email.subject,
        "html" -> email.body)
      Http(sendEmailWithBody OK as.String)
    }
  }
}
