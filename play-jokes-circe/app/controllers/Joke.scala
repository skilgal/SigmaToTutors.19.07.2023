package controllers

import io.circe._
import io.circe.generic.semiauto._

final case class Joke(joke: String) extends AnyVal
object Joke {
//  implicit val encoder: Encoder[Joke] = deriveEncoder
//  implicit val decoder: Decoder[Joke] = deriveDecoder
  implicit val codec: Codec[Joke] = deriveCodec
}
