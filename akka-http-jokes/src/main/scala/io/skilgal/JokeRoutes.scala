package io.skilgal

import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import io.skilgal.JokeRegistry._

import scala.concurrent.Future

class JokeRoutes(userRegistry: ActorRef[JokeRegistry.Command])(implicit val system: ActorSystem[_]) {

  //#user-routes-class
  import JsonFormats._
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  //#import-json-formats

  // If ask takes more time than this to complete the request is failed
  private implicit val timeout: Timeout = Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))

  def getJokes(): Future[Joke] = userRegistry.ask(GetJokes)

  val userRoutes: Route =
    pathPrefix("jokes") {
      concat(
        //#users-get-delete
        pathEnd {
          concat(
            get { complete(getJokes()) }
          )
        }
      )
    }
}
