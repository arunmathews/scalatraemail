package com.github.arunmathews.email.service

class RootServlet extends ScalatraEmailStack {
  notFound {
    contentType = null
    resourceNotFound()
  }
}
