package com.github.arunmathews.email.controller

import com.github.arunmathews.email.creation.FreeMarkerCreateEmailComponent
import com.github.arunmathews.email.sending.{FailoverSendEmailComponent, SendEmailComponent}

/**
 *
 */
class FailoverEmailComponentConcrete(val firstComponent: SendEmailComponent, val secondComponent: SendEmailComponent)
  extends EmailComponent
  with FreeMarkerCreateEmailComponent
  with FailoverSendEmailComponent {
}
