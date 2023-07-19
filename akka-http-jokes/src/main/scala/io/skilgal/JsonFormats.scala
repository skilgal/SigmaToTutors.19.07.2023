package io.skilgal

import io.skilgal.JokeRegistry.ActionPerformed
import spray.json.RootJsonFormat

//#json-formats
import spray.json.DefaultJsonProtocol

object JsonFormats {
  // import the default encoders for primitive types (Int, String, Lists etc)
  import DefaultJsonProtocol._

  implicit val jokeJsonFormat: RootJsonFormat[Joke] = jsonFormat1(Joke.apply)
  implicit val actionPerformedJsonFormat: RootJsonFormat[ActionPerformed] = jsonFormat1(ActionPerformed.apply)
}
//#json-formats
