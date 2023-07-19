package io.skilgal.http4sjokes

import cats.effect.Sync
import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

object Http4sjokesRoutes {

  def jokeRoutes[F[_]: Sync](J: Jokes[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F] { case GET -> Root / "jokes" =>
      for {
        joke <- J.get
        resp <- Ok(joke)
      } yield resp
    }
  }
}
