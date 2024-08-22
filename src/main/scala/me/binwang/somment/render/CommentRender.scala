package me.binwang.somment.render

import me.binwang.somment.model.Comment
import org.scalajs.dom.Node
import scalatags.JsDom.all.*

import java.time.{LocalTime, ZoneId}
import scala.scalajs.js.Date

object CommentRender {

  private val indentColors = Seq(
    "red",
    "orange",
    "yellow",
    "green",
    "blue",
    "indigo",
    "purple",
    "rebeccapurple",
  )

  def apply(comments: Seq[Comment]): Node = {
    renderComments(comments, 0).render
  }

  private def renderComments(comments: Seq[Comment], depth: Int): Frag = {
    if (comments.isEmpty) {
      ""
    } else {
      val doms = comments.map { comment =>
        renderComment(comment, depth)
      }
      div(
        cls := "comments",
        doms
      )
    }
  }

  private def renderComment(comment: Comment, depth: Int): Frag = {
    val childrenClass = if (depth > 0) "comment-child" else ""
    val indentColor = if (depth > 0) Some(indentColors((depth-1) % indentColors.size)) else None
    div(
      cls := s"comment $childrenClass",
      div(
        cls := "comment-self",
        indentColor.map(c => borderLeftColor := c),
        div(
          cls := "comment-author-line",
          div(cls := "comment-author", comment.author),
          comment.upvotes.map { upvotes =>
            val score = upvotes - comment.downvotes.getOrElse(0L)
            div(cls := "comment-points", s"$score points")
          },
          comment.createdAt.map(t => div(cls := "comment-time",
            new Date(t.toEpochMilli).toLocaleString()))
        ),
        div(
          cls := "comment-content",
          comment.text,
        ),
      ),
      hr(cls := "comment-separator"),
      renderComments(comment.children, depth + 1),
    )
  }


}
