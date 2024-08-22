package me.binwang.somment.fetcher

import cats.effect.IO
import me.binwang.somment.model.Comment

trait CommentFetcher {
  def canHandle(url: String): Boolean
  def getComments(url: String): IO[Seq[Comment]]
}
