package com.github.arunmathews.app

import org.scalatra._
import scalate.ScalateSupport

class EmailServlet extends ScalatraemailStack {

  get("/") {
    <html>
      <body>
        <h1>Hello, world!</h1>
        Say <a href="hello-scalate">hello to Scalate</a>.
      </body>
    </html>
  }

}
