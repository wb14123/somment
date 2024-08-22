package me.binwang.somment

import me.binwang.somment.fetcher.RedditFetcher
import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel

import cats.effect.unsafe.implicits.global

@JSExportTopLevel("Somment", moduleID = "somment")
class Somment(url: String, id: String) extends js.Object {

  def create(): Unit = {
    val fetcher = new RedditFetcher()
    fetcher.getComments(url).map { comments =>
      dom.document.getElementById(id).innerHTML = comments.mkString
    }.unsafeRunAndForget()
  }

}
