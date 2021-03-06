package com.github.arunmathews.email.sending

import com.github.arunmathews.email.businessobject.BusinessObjects.{CreateEmailOutput, Email}
import dispatch.Future

import scala.concurrent.Promise
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

/**
 *
 */
trait FailoverSendEmailComponent extends SendEmailComponent {
  override type E = FailoverEmailSender

  def firstComponent: SendEmailComponent

  def secondComponent: SendEmailComponent

  override val emailSender = new FailoverEmailSender(firstComponent.emailSender, secondComponent.emailSender)

  class FailoverEmailSender(firstSender: SendEmailComponent#E, secondSender: SendEmailComponent#E) extends EmailSender {

    override def sendEmail(email: Email, emailAddress: String): Future[CreateEmailOutput] = {
      val prom = Promise[CreateEmailOutput]()
      firstSender.sendEmail(email, emailAddress).onComplete {
        case Success(ceo) => prom.complete(Success(ceo))
        case Failure(_) => secondSender.sendEmail(email, emailAddress).onComplete {
          case Success(ceo) => prom.complete(Success(ceo))
          case Failure(e) => prom.failure(e)
        }
      }

      prom.future
    }
  }

}