package org.dkichler.lagomteststream.impl

import com.lightbend.lagom.scaladsl.api.ServiceCall
import org.dkichler.lagomteststream.api.LagomtestStreamService
import org.dkichler.lagomtest.api.LagomtestService

import scala.concurrent.Future

/**
  * Implementation of the LagomtestStreamService.
  */
class LagomtestStreamServiceImpl(lagomtestService: LagomtestService) extends LagomtestStreamService {
  def stream = ServiceCall { hellos =>
    Future.successful(hellos.mapAsync(8)(lagomtestService.hello(_).invoke()))
  }
}
