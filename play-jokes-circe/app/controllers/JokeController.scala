package controllers

import play.api.http.ContentTypes
import play.api.libs.ws.WSClient
import play.api.mvc._

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}
import CirceUtils._
import io.circe._
import io.circe.syntax._

@Singleton
class JokeController @Inject() (
    val client: WSClient,
    val controllerComponents: ControllerComponents
)(implicit ex: ExecutionContext)
    extends BaseController {

  def jokes(): Action[AnyContent] = Action.async { _ =>
    val url = "https://icanhazdadjoke.com/"

    for {
      wsResponse <- client.url(url).addHttpHeaders(ACCEPT -> ContentTypes.JSON).get()
      parsingResult = wsResponse.body[Json].as[Joke]
      joke <- parsingResult.fold(
        failure => Future.failed(new IllegalArgumentException(failure.message)),
        joke => Future.successful(joke)
      )
    } yield Ok(joke.asJson.noSpaces)
  }
}
