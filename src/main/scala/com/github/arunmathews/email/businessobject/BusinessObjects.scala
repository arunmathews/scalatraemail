package com.github.arunmathews.email.businessobject

import com.github.arunmathews.email.businessobject.BusinessObjects.EmailType.EmailType

/**
 *
 */
object BusinessObjects {
  object EmailType extends Enumeration {
    type EmailType = Value
    val Birthday, Holidays = Value
  }

  case class Person(firstName: String, lastName: String, emailAddress: String)

  case class CreateEmailInput(person: Person, emailType: EmailType)

  case class Email(subject: String, body: String)

  case class CreateEmailOutput(id: Option[String], message: Option[String], result: Boolean)
}
