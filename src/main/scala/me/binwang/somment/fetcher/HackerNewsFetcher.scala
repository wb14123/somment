package me.binwang.somment.fetcher
import cats.effect.IO
import me.binwang.somment.model.Comment
import me.binwang.somment.model.Errors.FetchingException
import org.scalajs.dom.{DOMParser, Document, Element, MIMEType, console}
import sttp.client4.*

import java.net.URI
import java.time.{LocalDateTime, ZoneOffset}
import scala.collection.mutable
import scala.util.Try

class HackerNewsFetcher extends CommentFetcher {

  override def canHandle(url: String): Boolean = {

    val uri = new URI(url)
    val validSchemas = Seq("https")
    val validHosts = Seq("news.ycombinator.com")
    validSchemas.contains(uri.getScheme) && validHosts.contains(uri.getHost)  }

  override def getComments(url: String): IO[Seq[Comment]] = {
    basicRequest
      .get(uri"https://http-proxy.rssbrain.com/?link=$url")
      .response(asString)
      .send(sttpBackend)
      .flatMap {
        _.body match {
          case Left(e) => IO.raiseError(FetchingException(url, e))
          case Right(result) => IO.pure(parseComments(result))
        }
      }
  }

  private def parseChildrenComments(elements: Seq[Element], start: Int, indent: Int, parentID: Option[String]): Seq[Comment] = {
    var curIndent = indent
    val result: mutable.Buffer[Comment] = mutable.Buffer()
    for
      i <- start to elements.length
      if curIndent < indent
    do
      val elem = elements(i)
      curIndent = elem.querySelector("td[indent]").getAttribute("indent").toInt
      val createdAt = Try(LocalDateTime
        .parse(elem.querySelector(".age").getAttribute("title"))
        .toInstant(ZoneOffset.UTC)
      ).toOption
      val id = elem.getAttribute("id")
      val contentElem = elem.querySelector(".commtext")
      result.append(Comment(
        id = id,
        author = elem.querySelector(".hnuser").innerText,
        createdAt = createdAt,
        updatedAt = createdAt,
        upvotes = None,
        downvotes = None,
        text = contentElem.innerText,
        html = contentElem.innerHTML,
        link = Some(new URI(s"https://news.ycombinator.com/item?id=$id")),
        replyToID = parentID,
        children = Seq(),
        childrenCount = 0,
      ))
    result.toSeq
  }

  private def findChildren(id: Option[String], comments: Seq[Comment]): Seq[Comment] = {
    comments.filter(_.replyToID.equals(id)).map { c =>
      val children = findChildren(Some(c.id), comments)
      c.copy(children = children, childrenCount = children.map(_.childrenCount + 1).sum)
    }
  }

  private def parseComments(html: String): Seq[Comment] = {

    val parser = new DOMParser()
    val parsedHTML = parser.parseFromString(html, MIMEType.`text/html`)

    val lastComments = mutable.Map[Int, String]()

    val comments = parsedHTML.querySelectorAll(".comtr:not(.noshow):not(.coll)").toSeq.map { elem =>
      val indent = elem.querySelector("td[indent]").getAttribute("indent").toInt
      val parent = if (indent > 0) {
        lastComments.get(indent - 1)
      } else None
      val createdAt = Try(LocalDateTime
        .parse(elem.querySelector(".age").getAttribute("title"))
        .toInstant(ZoneOffset.UTC)
      ).toOption
      val id = elem.getAttribute("id")
      val contentElem = elem.querySelector(".commtext")
      lastComments.put(indent, id)
      Comment(
        id = id,
        author = elem.querySelector(".hnuser").innerText,
        createdAt = createdAt,
        updatedAt = createdAt,
        upvotes = None,
        downvotes = None,
        text = contentElem.innerText,
        html = contentElem.innerHTML,
        link = Some(new URI(s"https://news.ycombinator.com/item?id=$id")),
        replyToID = parent,
        children = Seq(),
        childrenCount = 0,
      )
    }
    findChildren(None, comments)
  }
}
