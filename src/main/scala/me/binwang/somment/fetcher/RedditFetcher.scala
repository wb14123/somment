package me.binwang.somment.fetcher

import cats.effect.IO
import me.binwang.somment.lib.HTMLDecoder
import me.binwang.somment.model.Comment
import sttp.client4.*
import sttp.client4.upicklejson.default.asJson

import java.net.URI
import java.time.Instant


class RedditFetcher extends CommentFetcher {

  override def canHandle(url: String): Boolean = {
    val uri = new URI(url)
    val validSchemas = Seq("https")
    val validHosts = Seq(
      "old.reddit.com",
      "www.reddit.com",
      "reddit.com"
    )
    validSchemas.contains(uri.getScheme) && validHosts.contains(uri.getHost)
  }

  override def getComments(url: String): IO[Seq[Comment]] = {
    val apiUrl = url.replaceFirst("(https?://)(www\\.)?reddit\\.com", "https://api.reddit.com")
    basicRequest
      .get(uri"$apiUrl.json?sort=confidence")
      .response(asJson[ujson.Arr])
      .send(sttpBackend)
      .flatMap { _.body match {
        case Left(e) => IO.raiseError(e)
        case Right(result) => IO.pure(parseRedditJson(result))
      }}
  }

  private def parseRedditJson(json: ujson.Arr): Seq[Comment] = {
    val redditComments = json(1).obj
    parseRedditComments(redditComments)
  }

  private def parseRedditComments(redditComments: ujson.Obj): Seq[Comment] = {
    redditComments("data")("children").arr.toSeq.map(_("data")).map { v =>
      val redditComment = v.obj
      val createdAt = redditComment.get("created_utc").flatMap(_.numOpt).map(t => Instant.ofEpochSecond(t.toLong))
      val text = redditComment.get("body").flatMap(_.strOpt).getOrElse("")
      val replies = redditComment.get("replies").flatMap(_.objOpt)
      val children = if (replies.isDefined && replies.get.nonEmpty) {
        parseRedditComments(replies.get)
      } else Seq()
      Comment(
        id = redditComment("id").str,
        author = redditComment.get("author").flatMap(_.strOpt).getOrElse("[Unknown user]"),
        createdAt = createdAt,
        updatedAt = createdAt,
        upvotes = redditComment.get("ups").flatMap(_.numOpt).map(_.toLong),
        downvotes = redditComment.get("downs").flatMap(_.numOpt).map(_.toLong),
        text = text,
        html = HTMLDecoder.decode(redditComment.get("body_html").flatMap(_.strOpt).getOrElse(text)),
        link = redditComment.get("permalink").flatMap(_.strOpt).map(l => new URI(s"https://old.reddit.com$l")),
        replyToID = redditComment.get("parent_id").flatMap(_.strOpt).flatMap(s => if (s.isEmpty) None else Some(s)),
        children = children,
        childrenCount = children.size,
      )
    }
  }
}

