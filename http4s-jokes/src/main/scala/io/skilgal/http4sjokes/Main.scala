package io.skilgal.http4sjokes

import cats.effect.{IO, IOApp}

object Main extends IOApp.Simple {
  val run = Http4sjokesServer.run[IO]
}
