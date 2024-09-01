package me.binwang.somment.model

object Errors {

  abstract class SommentException(code: Int, msg: String) extends Exception(msg)

  case class FetchingException(url: String, reason: String)
    extends SommentException(10001, s"Error fetching $url: $reason")
  
}
