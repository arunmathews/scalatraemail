package com.github.arunmathews.email.sending

import com.github.arunmathews.email.businessobject.BusinessObjects.{CreateEmailOutput, Email}

import scala.concurrent.Promise
import scala.util.{Success, Try, Failure}
import dispatch._
import org.json4s._
import scala.concurrent.ExecutionContext.Implicits.global
import org.json4s.jackson.Serialization._
/**
 *
 */
trait MandrillSendEmailComponent extends SendEmailComponent {
  override type E = MandrillEmailSender

  def apiKey: String

  def senderEmail: String

  override val emailSender = new MandrillEmailSender(apiKey, senderEmail)

  class MandrillEmailSender(val apiKey: String, val sender: String) extends EmailSender {
    val root = host("mandrillapp.com").secure / "api" / "1.0"
    val sendEmailPath = root / "messages" / "send.json"
    val sendEmailJson = sendEmailPath.setContentType("application/json", "UTF-8")
    implicit val jsonFormats = DefaultFormats

    override def sendEmail(email: Email, emailAddress: String): Future[CreateEmailOutput] = {
      val recipient = Recipient(emailAddress)
      val message = Message(email.body, sender, email.subject, List(recipient))
      val mandrillRequest = MandrillSendMessageRequest(apiKey, message)
      val prom = Promise[CreateEmailOutput]()
      sendEmailWithBody(mandrillRequest).onComplete {
        case Success(jval) =>
          val resp = jval.camelizeKeys.extract[MandrillSendMessageResponse]
          prom.complete(Try(CreateEmailOutput(Option(resp._id), Option(resp.status), result = true)))
        case Failure(e) => prom.failure(e)
      }

      prom.future
    }

    def sendEmailWithBody(request: MandrillSendMessageRequest): Future[JValue] = {
      val sendEmailWithBody = sendEmailJson << write(Extraction.decompose(request).underscoreKeys)
      Http(sendEmailWithBody OK as.json4s.Json)
    }
  }

}

case class Recipient(email: String)
case class Message(html: String, fromEmail: String, subject: String, to: List[Recipient])
case class MandrillSendMessageRequest(key: String, message: Message)
case class MandrillSendMessageResponse(email: String, status: String, _id: String, rejectReason: String)
