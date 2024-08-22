package me.binwang.somment.render

import me.binwang.somment.model.Comment
import org.scalajs.dom.Node
import scalatags.JsDom.all.*

object CommentRender {

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
    div(
      cls := s"comment $childrenClass",
      div(
        cls := "comment-author",
        comment.author,
      ),
      div(
        cls := "comment-content",
        comment.text,
      ),
      renderComments(comment.children, depth + 1),
    )
  }


}
