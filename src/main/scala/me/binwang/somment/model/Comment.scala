package me.binwang.somment.model

import java.net.URI
import java.time.Instant

case class Comment(
  id: String,
  author: String,
  createdAt: Option[Instant],
  updatedAt: Option[Instant],
  upvotes: Option[Long],
  downvotes: Option[Long],
  text: String,
  html: String,
  link: Option[URI],
  replyToID: Option[String],
  children: Seq[Comment],
  childrenCount: Long,
)
