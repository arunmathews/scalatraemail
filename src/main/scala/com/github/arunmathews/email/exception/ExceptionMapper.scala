package com.github.arunmathews.email.exception

/**
 *
 */
object ExceptionMapper {
   def mapExceptions[T](thunk: => T): T = {
     try {
       //Parentheses not necessary but adding it for clarity
       (thunk)
     }
     catch {
       case e: Throwable => mapDefaultExceptions(e)
     }
   }

   private def mapDefaultExceptions(t: Throwable) = t match {
     case e: EmailServiceException => throw e
     case e: RuntimeException => throw new EmailServiceException(e)
     case e: Error => throw e
     case e: Throwable => throw new EmailServiceException(e)
   }

 }
