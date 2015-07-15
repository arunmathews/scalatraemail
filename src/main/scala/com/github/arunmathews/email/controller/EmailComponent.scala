package com.github.arunmathews.email.controller

import com.github.arunmathews.email.businessobject.BusinessObjects.{CreateEmailOutput, CreateEmailInput}
import com.github.arunmathews.email.creation.CreateEmailComponent
import com.github.arunmathews.email.sending.SendEmailComponent

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Business logic for creating email and sending email
 */
trait EmailComponent { self: CreateEmailComponent with SendEmailComponent =>

  def createAndSendEmail(cei: CreateEmailInput): Future[CreateEmailOutput] = {
    for {
      email <- emailCreator.createEmail(cei)
      sendEmailOutput <- emailSender.sendEmail(email, cei.person.emailAddress)
    } yield sendEmailOutput
  }
}
