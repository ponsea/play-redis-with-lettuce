import com.google.inject.{AbstractModule, TypeLiteral}
import io.lettuce.core.RedisClient
import io.lettuce.core.api.StatefulRedisConnection

class Module extends AbstractModule {
  override def configure() = {
    bind(classOf[RedisClient])
      .toProvider(classOf[RedisClientProvider])
    bind(new TypeLiteral[StatefulRedisConnection[String, String]] {})
      .toProvider(classOf[StatefulRedisConnectionProvider])
  }
}
