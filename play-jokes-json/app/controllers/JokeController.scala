package controllers

import play.api.http.ContentTypes
import play.api.libs.json.Json
import play.api.libs.ws.{WSClient, WSResponse}
import play.api.mvc._

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class JokeController @Inject() (
    val client: WSClient,
    val controllerComponents: ControllerComponents
)(implicit ex: ExecutionContext)
    extends BaseController {

  //  Request => Response
  //  trait EssentialAction extends (RequestHeader => Accumulator[ByteString, Result])

  def jokes(): Action[AnyContent] = Action.async { _ =>
    val url = "https://icanhazdadjoke.com/"

    val clientResponse: Future[WSResponse] =
      client.url(url).addHttpHeaders(ACCEPT -> ContentTypes.JSON).get()

    clientResponse
//    .map(wsResponse => wsResponse.json.as[Joke])
      .map(_.json.as[Joke]) // Transformation from Json to Joke
      .map(joke => Ok(Json.toJson(joke))) // Transformation from Joke to Json
  }
}
