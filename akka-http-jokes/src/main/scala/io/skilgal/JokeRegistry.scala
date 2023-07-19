package io.skilgal

import akka.actor.typed._
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.unmarshalling.Unmarshal
import io.skilgal.JsonFormats._
import spray.json._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

final case class Joke(joke: String)

object JokeRegistry {

  sealed trait Command
  final case class GetJokes(replyTo: ActorRef[Joke]) extends Command

  final case class ActionPerformed(description: String)

  def apply(): Behavior[Command] = registry(Set.empty)

  private def registry(users: Set[Joke]): Behavior[Command] =
    Behaviors.receiveMessage { case GetJokes(replyTo) =>
      implicit val system = ActorSystem(Behaviors.empty, "SingleRequest")

      def parseResponse(response: HttpResponse): Future[Joke] = {
        Unmarshal(response.entity).to[String].map { string =>
          string.parseJson.convertTo[Joke]
        }
      }

      val responseFuture: Future[HttpResponse] =
        Http().singleRequest(
          HttpRequest(
            uri = "https://icanhazdadjoke.com/",
            headers = List(RawHeader("Accept", "application/json"))
          )
        )

      responseFuture
        .flatMap(parseResponse)
        .onComplete {
          case Success(joke) => replyTo ! joke
          case Failure(_)    => sys.error("something wrong")
        }

      Behaviors.same
    }
}
//#user-registry-actor
