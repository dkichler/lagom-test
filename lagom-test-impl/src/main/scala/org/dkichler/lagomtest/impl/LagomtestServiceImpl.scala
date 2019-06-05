package org.dkichler.lagomtest.impl

import org.dkichler.lagomtest.api
import org.dkichler.lagomtest.api.LagomtestService
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.broker.TopicProducer
import com.lightbend.lagom.scaladsl.persistence.{EventStreamElement, PersistentEntityRegistry}

/**
  * Implementation of the LagomtestService.
  */
class LagomtestServiceImpl(persistentEntityRegistry: PersistentEntityRegistry) extends LagomtestService {

  override def hello(id: String) = ServiceCall { _ =>
    // Look up the lagom-test entity for the given ID.
    val ref = persistentEntityRegistry.refFor[LagomtestEntity](id)

    // Ask the entity the Hello command.
    ref.ask(Hello(id))
  }

  override def useGreeting(id: String) = ServiceCall { request =>
    // Look up the lagom-test entity for the given ID.
    val ref = persistentEntityRegistry.refFor[LagomtestEntity](id)

    // Tell the entity to use the greeting message specified.
    ref.ask(UseGreetingMessage(request.message))
  }


  override def greetingsTopic(): Topic[api.GreetingMessageChanged] =
    TopicProducer.singleStreamWithOffset {
      fromOffset =>
        persistentEntityRegistry.eventStream(LagomtestEvent.Tag, fromOffset)
          .map(ev => (convertEvent(ev), ev.offset))
    }

  private def convertEvent(helloEvent: EventStreamElement[LagomtestEvent]): api.GreetingMessageChanged = {
    helloEvent.event match {
      case GreetingMessageChanged(msg) => api.GreetingMessageChanged(helloEvent.entityId, msg)
    }
  }
}
