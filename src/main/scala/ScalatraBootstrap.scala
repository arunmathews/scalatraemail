import java.io.File

import com.github.arunmathews.email.controller.{MandrillEmailComponentConcrete, MailGunEmailComponentConcrete}
import com.github.arunmathews.email.service.{HelloServlet, EmailServlet}
import com.typesafe.config.ConfigFactory
import org.scalatra._
import javax.servlet.ServletContext
import com.github.kxbmap.configs._

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    val myCfg =  ConfigFactory.parseFile(new File("/Volumes/Unix/personal/Programming/Scala/Scalatra/scalatraemail/override.conf"))
    val conf = ConfigFactory.load(myCfg)
    
    context.mount(new HelloServlet, "/ping/*")
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
        context.mount(new EmailServlet(mandrillEmailComponent), "/emails/*")
      case (_, _) => throw new RuntimeException("Components to start servlet not available")
    }
  }
}
