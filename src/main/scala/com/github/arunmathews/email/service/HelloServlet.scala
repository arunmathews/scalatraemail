package com.github.arunmathews.email.service

import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json._

class HelloServlet extends ScalatraEmailStack {

   get("/") {
       <html>
         <body>
           <h1>Hello, world!</h1>
           Say <a href="hello-scalate">hello to Scalate</a>.
         </body>
       </html>
     }

 }
