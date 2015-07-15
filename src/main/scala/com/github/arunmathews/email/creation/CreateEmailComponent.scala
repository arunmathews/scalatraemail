package com.github.arunmathews.email.creation

import com.github.arunmathews.email.businessobject.BusinessObjects.{Email, CreateEmailInput}

import scala.concurrent.Future
import scala.util.Try

/**
 * Component that creates an email from input
 */
trait CreateEmailComponent {
  type T <: EmailCreator

  def emailCreator: T

  trait EmailCreator {
    def createEmail(cei: CreateEmailInput): Future[Email]
  }
}
