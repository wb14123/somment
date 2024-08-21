package me.binwang.somment

import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel

@JSExportTopLevel("Somment", moduleID = "somment")
class Somment(url: String, id: String) extends js.Object {

  def create(): Unit = {
    dom.document.getElementById(id).innerHTML = url
  }

}
