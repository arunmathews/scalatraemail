package com.github.arunmathews.email.controller

import com.github.arunmathews.email.creation.FreeMarkerCreateEmailComponent
import com.github.arunmathews.email.sending.{MandrillSendEmailComponent, MailGunSendEmailComponent}

/**
 *
 */
class MandrillEmailComponentConcrete(val apiKey: String, val senderEmail: String)
  extends EmailComponent
  with FreeMarkerCreateEmailComponent
  with MandrillSendEmailComponent {
}
