package controllers

import io.circe.Json
import play.api.libs.ws.BodyReadable

object CirceUtils {

  implicit def circeJsonBodyReadable: BodyReadable[Json] = BodyReadable { response =>
    io.circe.parser.parse(response.bodyAsBytes.utf8String) match {
      case Left(decodingFailure) => throw decodingFailure
      case Right(json)           => json
    }
  }

}
