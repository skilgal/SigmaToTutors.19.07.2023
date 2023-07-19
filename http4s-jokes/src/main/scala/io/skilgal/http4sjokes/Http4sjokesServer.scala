package io.skilgal.http4sjokes

import cats.effect.Async
import com.comcast.ip4s._
import fs2.io.net.Network
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import org.http4s.server.middleware.Logger

object Http4sjokesServer {

  def run[F[_]: Async: Network]: F[Nothing] = {
    for {
      client <- EmberClientBuilder.default[F].build
      jokeAlg = Jokes.impl[F](client)
      httpApp = (Http4sjokesRoutes.jokeRoutes[F](jokeAlg)).orNotFound

      // With Middlewares in place
      finalHttpApp = Logger.httpApp(true, true)(httpApp)

      _ <- EmberServerBuilder
        .default[F]
        .withHost(ipv4"0.0.0.0")
        .withPort(port"9000")
        .withHttpApp(finalHttpApp)
        .build
    } yield ()
  }.useForever
}
