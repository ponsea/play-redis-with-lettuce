package controllers

import io.lettuce.core.api.StatefulRedisConnection
import javax.inject._
import play.api.mvc._

import scala.concurrent.ExecutionContext

@Singleton
class RedisController @Inject()(
  cc: ControllerComponents,
  redisConnection: StatefulRedisConnection[String, String]

)(implicit ec: ExecutionContext)
    extends AbstractController(cc) {

  def redis = Action.async {
    import scala.jdk.FutureConverters._
    val asyncCommands = redisConnection.async()
    for {
      _       <- asyncCommands.set("key", "Hello, Redis!").asScala
      message <- asyncCommands.get("key").asScala
    } yield Ok(message)
  }
}
