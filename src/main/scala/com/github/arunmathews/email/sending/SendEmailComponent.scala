package com.github.arunmathews.email.sending

import com.github.arunmathews.email.businessobject.BusinessObjects.{CreateEmailOutput, Email}
import dispatch._

trait SendEmailComponent
{
  type E <: EmailSender

  def emailSender: E

  trait EmailSender {
    def sendEmail(email: Email, emailAddress: String): Future[CreateEmailOutput]
  }
}
