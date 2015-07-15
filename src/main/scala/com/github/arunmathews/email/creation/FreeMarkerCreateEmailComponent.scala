package com.github.arunmathews.email.creation

import java.io.StringWriter

import com.github.arunmathews.email.businessobject.BusinessObjects.{Email, CreateEmailInput}
import com.github.arunmathews.email.businessobject.BusinessObjects.EmailType._
import com.github.arunmathews.email.exception.ExceptionMapper._
import freemarker.template.{TemplateExceptionHandler, Configuration}

import scala.concurrent.{Promise, Future}
import scala.util.{Success, Failure, Try}
import scala.collection.JavaConverters._

/**
 *
 */
trait FreeMarkerCreateEmailComponent extends CreateEmailComponent {
  override type T = FreeMarkerEmailCreator

  override val emailCreator: FreeMarkerEmailCreator = new FreeMarkerEmailCreator

  class FreeMarkerEmailCreator extends EmailCreator {
    val version = Configuration.VERSION_2_3_23
    val templatePath = "email/template"
    private val config = new Configuration(version)
    config.setClassForTemplateLoading(classOf[FreeMarkerCreateEmailComponent], "/")
    config.setDefaultEncoding("UTF-8")
    config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER)

    val templateMap = Map(Birthday -> "happyBirthdayEmail.ftl", Holidays -> "happyHolidaysEmail.ftl")
    val subjectMap = Map(Birthday -> "Happy Birthday", Holidays -> "Happy Holidays")

    override def createEmail(cei: CreateEmailInput): Future[Email] = {
      val t = templateMap.get(cei.emailType).fold(Failure(new IllegalArgumentException(s"Template not setup for email type ${cei.emailType}")): Try[Email])((templateName) => {
        val person = cei.person
        try {
          Success(createEmail(cei.emailType, person.firstName, person.lastName, templateName))
        }
        catch {
          case e: Exception => Failure(e)
        }
      })

      Promise().complete(t).future
    }

    private def createEmail(emailType: EmailType, firstName: String, lastName: String, templateName: String): Email = mapExceptions {
      val template = config.getTemplate(s"$templatePath/$templateName")
      val data = Map("firstName" -> firstName, "lastName" -> lastName).asJava
      val output = new StringWriter
      template.process(data, output)

      Email(subjectMap.get(emailType).get, output.toString)

    }
  }
}
