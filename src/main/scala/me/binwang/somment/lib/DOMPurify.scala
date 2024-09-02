package me.binwang.somment.lib

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal

@js.native
@JSGlobal("DOMPurify")
object DOMPurify extends js.Any {
  def sanitize(html: String): String = js.native
}
