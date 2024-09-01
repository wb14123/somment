package me.binwang.somment.fetcher

import cats.effect.IO
import me.binwang.somment.model.Comment
import sttp.client4.WebSocketBackend
import sttp.client4.impl.cats.FetchCatsBackend

trait CommentFetcher {

  protected val sttpBackend: WebSocketBackend[IO] = FetchCatsBackend[IO]()

  def canHandle(url: String): Boolean
  def getComments(url: String): IO[Seq[Comment]]
}
