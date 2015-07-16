package com.github.arunmathews.email

import com.github.arunmathews.email.businessobject.BusinessObjects.{CreateEmailOutput, Email}
import com.github.arunmathews.email.exception.EmailServiceException
import com.github.arunmathews.email.sending.SendEmailComponent
import dispatch.Future

import scala.concurrent.Promise

/**
 *
 */
class FailingSendEmailComponent extends SendEmailComponent {
  override type E = FailingEmailSender

  override val emailSender = new FailingEmailSender
  class FailingEmailSender extends EmailSender {
    override def sendEmail(email: Email, emailAddress: String): Future[CreateEmailOutput] = {
      val prom = Promise[CreateEmailOutput]()
      prom.failure(new EmailServiceException("Always failing future"))

      prom.future
    }

  }
}
