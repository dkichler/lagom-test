package org.dkichler.lagomtest.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import org.dkichler.lagomtest.api.LagomtestService
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.lightbend.lagom.scaladsl.playjson.JsonSerializerRegistry
import com.softwaremill.macwire._

class LagomtestLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new LagomtestApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new LagomtestApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[LagomtestService])
}

abstract class LagomtestApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with CassandraPersistenceComponents
    with LagomKafkaComponents
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer: LagomServer = serverFor[LagomtestService](wire[LagomtestServiceImpl])

  // Register the JSON serializer registry
  override lazy val jsonSerializerRegistry: JsonSerializerRegistry = LagomtestSerializerRegistry

  // Register the lagom-test persistent entity
  persistentEntityRegistry.register(wire[LagomtestEntity])
}
