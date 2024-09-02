package me.binwang.somment.lib

import org.scalajs.dom.document

object HTMLDecoder {

  def decode(html: String): String = {
    val e= document.createElement("div")
    e.innerHTML = html
    e.childNodes.headOption.map(_.nodeValue).getOrElse("")
  }

}
