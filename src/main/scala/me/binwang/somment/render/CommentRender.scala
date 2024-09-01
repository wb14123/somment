package me.binwang.somment.render

import me.binwang.somment.model.Comment
import org.scalajs.dom
import org.scalajs.dom.{HTMLElement, Node}
import scalatags.JsDom.all.*

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

  private def renderComments(comments: Seq[Comment], depth: Int, parentID: Option[String] = None): Frag = {
    if (comments.isEmpty) {
      ""
    } else {
      val doms = comments.map(renderComment(_, depth))
      val childCls = parentID.map(showChildrenClass).getOrElse("")
      div(
        cls := s"comments $childCls",
        doms
      )
    }
  }

  private def renderComment(comment: Comment, depth: Int): Frag = {
    val childrenClass = if (depth > 0) "comment-child" else ""
    val indentColor = if (depth > 0) Some(indentColors((depth-1) % indentColors.size)) else None
    val nullHref = href := "javascript:void(0);"
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
            new Date(t.toEpochMilli).toLocaleString())),

          a(cls := s"comment-op ${hideChildrenClass(comment.id)}",
            nullHref, display := "none",
            onclick := { () => openChildren(comment.id) },
            s"[${comment.childrenCount} more]")
        ),
        div(cls := s"comment-content ${showChildrenClass(comment.id)}", comment.text),
        div(
          cls := s"comment-ops ${showChildrenClass(comment.id)}",
          comment.link.map(l => a(cls := "comment-op", href := l.toString, target := "_blank", "open")),
          a(cls := "comment-op", nullHref, onclick := { () => closeChildren(comment.id) }, "hide"),
        )
      ),
      hr(cls := "comment-separator"),
      renderComments(comment.children, depth + 1, Some(comment.id)),
    )
  }

  private def showChildrenClass(id: String) = {
    s"comment-show-children-$id"
  }

  private def hideChildrenClass(id: String) = {
    s"comment-hide-children-$id"
  }

  private def closeChildren(id: String): Unit = {
    dom.document.querySelectorAll(s".${showChildrenClass(id)}").toSeq.foreach {
      _.asInstanceOf[HTMLElement].style.display = "none"
    }
    dom.document.querySelectorAll(s".${hideChildrenClass(id)}").toSeq.foreach {
      _.asInstanceOf[HTMLElement].style.display = "flex"
    }
  }

  private def openChildren(id: String): Unit = {
    dom.document.querySelectorAll(s".${showChildrenClass(id)}").toSeq.foreach {
      _.asInstanceOf[HTMLElement].style.display = "flex"
    }
    dom.document.querySelectorAll(s".${hideChildrenClass(id)}").toSeq.foreach {
      _.asInstanceOf[HTMLElement].style.display = "none"
    }
  }


}
