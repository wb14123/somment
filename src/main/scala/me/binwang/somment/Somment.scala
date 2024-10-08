package me.binwang.somment

import me.binwang.somment.fetcher.{HackerNewsFetcher, RedditFetcher}
import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel
import cats.effect.unsafe.implicits.global
import me.binwang.somment.render.CommentRender
import org.scalajs.dom.Element

@JSExportTopLevel("Somment", moduleID = "somment")
class Somment(url: String, elem: Element) extends js.Object {
  
  private val fetchers = Seq(
    new RedditFetcher(),
    new HackerNewsFetcher(),
  )
  
  def create(): Unit = {
    fetchers.find(_.canHandle(url)).foreach { fetcher =>
      fetcher.getComments(url).map { comments =>
        elem.replaceWith(CommentRender(comments))
      }.unsafeRunAndForget()     
    }
  }

}
