package com.github.arunmathews.email

import com.github.arunmathews.email.businessobject.BusinessObjects.{CreateEmailOutput, Email}
import com.github.arunmathews.email.exception.EmailServiceException
import com.github.arunmathews.email.sending.SendEmailComponent
import dispatch.Future

import scala.concurrent.Promise

/**
 *
 */
class SucceedingSendEmailComponent extends SendEmailComponent {
  override type E = SucceedingEmailSender

  override val emailSender = new SucceedingEmailSender

  class SucceedingEmailSender extends EmailSender {
    override def sendEmail(email: Email, emailAddress: String): Future[CreateEmailOutput] = {
      val prom = Promise[CreateEmailOutput]()
      prom.success(CreateEmailOutput(None, None, result = true))

      prom.future
    }

  }
}
