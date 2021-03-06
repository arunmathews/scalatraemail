import java.io.File

import com.github.arunmathews.email.controller._
import com.github.arunmathews.email.exception.EmailServiceException
import com.github.arunmathews.email.service.{RootServlet, EmailServlet}
import com.typesafe.config.ConfigFactory
import org.scalatra._
import javax.servlet.ServletContext
import com.github.kxbmap.configs._

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    val fileLocation = "/Volumes/Unix/personal/Programming/Scala/Scalatra/scalatraemail/override.conf"
    val myCfg =  ConfigFactory.parseFile(new File(fileLocation))
    val conf = ConfigFactory.load(myCfg)

    val maybeMandrillApiKey = conf.opt[String]("mandrillApiKey")
    val maybeMandrillSender = conf.opt[String]("mandrillSender")
    val maybeMailGunApiKey= conf.opt[String]("mailGunApiKey")
    val maybeMailGunSender = conf.opt[String]("mailGunSender")
    val maybeMandrillEmailComponent = for {
      mandrillApiKey <- maybeMandrillApiKey
      mandrillSender <- maybeMandrillSender
    } yield new MandrillEmailComponentConcrete(mandrillApiKey, mandrillSender)
    val maybeMailGunEmailComponent = for {
      mailGunApiKey <- maybeMailGunApiKey
      mailGunSender <- maybeMailGunSender
    } yield new MailGunEmailComponentConcrete(mailGunApiKey, mailGunSender)

    (maybeMandrillEmailComponent, maybeMailGunEmailComponent) match {
      case (Some(mandrillEmailComponent), Some(mailGunEmailComponent)) =>
        val backupEmailComponent = new FailoverEmailComponentConcrete(mandrillEmailComponent, mailGunEmailComponent)
        //TODO: Reject all other urls
        context.mount(new EmailServlet(backupEmailComponent), "/emails/*")
        context.mount(new RootServlet(), "/*")
      case (_, _) => throw new EmailServiceException("Components to start servlet not available")
    }
  }
}
