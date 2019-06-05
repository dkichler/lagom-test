package org.dkichler.lagomteststream.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import org.dkichler.lagomteststream.api.LagomtestStreamService
import org.dkichler.lagomtest.api.LagomtestService
import com.softwaremill.macwire._

class LagomtestStreamLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new LagomtestStreamApplication(context) {
      override def serviceLocator: NoServiceLocator.type = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new LagomtestStreamApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[LagomtestStreamService])
}

abstract class LagomtestStreamApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer: LagomServer = serverFor[LagomtestStreamService](wire[LagomtestStreamServiceImpl])

  // Bind the LagomtestService client
  lazy val lagomtestService: LagomtestService = serviceClient.implement[LagomtestService]
}
