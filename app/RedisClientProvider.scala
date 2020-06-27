import io.lettuce.core.RedisClient
import io.lettuce.core.api.StatefulRedisConnection
import javax.inject.{ Inject, Provider, Singleton }
import play.api.Configuration
import play.api.inject.ApplicationLifecycle

import scala.jdk.FutureConverters._

@Singleton
class RedisClientProvider @Inject()(
  config: Configuration,
  lifecycle: ApplicationLifecycle
) extends Provider[RedisClient] {
  private val redisClient =
    RedisClient.create(config.get[String]("lettuce.redis-uri"))

  lifecycle.addStopHook { () =>
    redisClient.shutdownAsync().asScala
  }

  override val get: RedisClient = redisClient
}

@Singleton
class StatefulRedisConnectionProvider @Inject()(
  redisClient: RedisClient,
  lifecycle: ApplicationLifecycle
) extends Provider[StatefulRedisConnection[String, String]] {
  private val connection = redisClient.connect()

  lifecycle.addStopHook { () =>
    connection.closeAsync().asScala
  }

  override val get: StatefulRedisConnection[String, String] = connection
}
