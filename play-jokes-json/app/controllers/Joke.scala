package controllers

import play.api.libs.json._

final case class Joke(joke: String) extends AnyVal
object Joke {

//  implicit val reader: Reads[Joke] = Json.reads
//  implicit val writes: Writes[Joke] = Json.writes
  implicit val format: OFormat[Joke] = Json.format
}
