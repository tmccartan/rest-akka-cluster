package actors

import java.util.UUID

import akka.actor.Actor
import akka.event.Logging
import akka.pattern.pipe
import com.google.inject.Inject
import service.PaymentMethodService

import scala.concurrent.ExecutionContext.Implicits.global

class PaymentMethodActor @Inject() (paymentMethodService: PaymentMethodService) extends Actor {

  val log = Logging(context.system, this)

  override def receive : Receive = {
    case MainActorMessages.GetPaymentMethods =>
      log.info("received payment method message")
      paymentMethodService.getAll() pipeTo sender

    case _ =>
      log.info("what was that")
  }
}
