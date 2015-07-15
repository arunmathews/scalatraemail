package com.github.arunmathews.email.controller

import com.github.arunmathews.email.creation.FreeMarkerCreateEmailComponent
import com.github.arunmathews.email.sending.{MailGunSendEmailComponent, MandrillSendEmailComponent}

/**
 *
 */
class MailGunEmailComponentConcrete(val apiKey: String, val sender: String)
  extends EmailComponent
  with FreeMarkerCreateEmailComponent
  with MailGunSendEmailComponent {
}
