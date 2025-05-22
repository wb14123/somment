package me.binwang.somment

import me.binwang.somment.fetcher.{HackerNewsFetcher, RedditFetcher}
import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel
import cats.effect.unsafe.implicits.global
import me.binwang.somment.render.CommentRender
import org.scalajs.dom.Element
import scalatags.JsDom.all.*
import scalatags.JsDom.tags2.progress

@JSExportTopLevel("Somment", moduleID = "somment")
class Somment(url: String, elem: Element) extends js.Object {
  
  private val fetchers = Seq(
    new RedditFetcher(),
    new HackerNewsFetcher(),
  )

  def create(): Unit = {
    val progressBar = progress(id := "comment-progress").render
    elem.replaceWith(progressBar)
    fetchers.find(_.canHandle(url)).map { fetcher =>
      fetcher.getComments(url).map { comments =>
        progressBar.replaceWith(CommentRender(comments))
      }.handleError{ err =>
        progressBar.replaceWith(div(id := "comment-error", s"Error to load comment: ${err.getMessage}").render)
      }.unsafeRunAndForget()
    }.getOrElse(
      progressBar.replaceWith(div(cls := "comment", "No comment to load").render)
    )
  }

}
