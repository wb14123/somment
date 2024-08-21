package me.binwang.somment.model

import java.time.Instant

case class Comment(
    id: String,
    author: String,
    createdAt: Instant,
    updatedAt: Instant,
    upvotes: Option[Long],
    downvotes: Option[Long],
    text: String,
    html: String,
)
