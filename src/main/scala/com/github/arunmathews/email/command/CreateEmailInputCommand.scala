package com.github.arunmathews.email.command

import com.github.arunmathews.email.businessobject.BusinessObjects.EmailType
import com.github.arunmathews.email.businessobject.BusinessObjects.EmailType
import com.github.arunmathews.email.businessobject.BusinessObjects.EmailType.EmailType
import org.scalatra.commands._
import org.scalatra.validation.Validators.{PredicateValidator, Validator}
import org.json4s.{DefaultFormats, Formats}
import org.json4s.ext.EnumNameSerializer
/**
 *
 */

class CreateEmailInputCommand extends JsonCommand {
  protected implicit val jsonFormats = DefaultFormats + new EnumNameSerializer(EmailType)

  val firstName: Field[String] = asString("person.firstName").notBlank
  val lastName: Field[String] = asString("person.lastName").notBlank
  val emailAddress: Field[String] = asString("person.emailAddress").validEmail
  val emailType: Field[String] = asString("emailType").enumValue(EmailType)
}
