package actors

import java.util.UUID

import akka.actor.Actor
import com.gilt.akk.cluster.api.test.v0.models.{Address, PaymentMethod}
import service.{AddressService, PaymentMethodService}

import scala.concurrent.{ExecutionContext, Future}

object SessionActorMessages {
  case class ResolveSession(uUID: UUID)
  case class PaymentMethods(uUID: UUID)
  case class AddressMethods(uUID: UUID)
}


class SessionActor (paymentMethodService: PaymentMethodService, shippingAddressService: AddressService) extends Actor {

  var paymentMethods: Option[Future[Seq[PaymentMethod]]] = None
  var shippingAddresses: Option[Future[Seq[Address]]] = None
  import SessionActorMessages._
  implicit val ec: ExecutionContext = context.dispatcher

  override def receive : Receive = {

    case ResolveSession(uuid) =>
      paymentMethods = Some(paymentMethodService.getAll())
      shippingAddresses = Some(shippingAddressService.getAll())

    case PaymentMethods(uuid) =>
      paymentMethods match {
        case Some(resolvedPaymentMethods) =>
          println("Served payment methods from actor result")
           sender ! resolvedPaymentMethods

        case None =>
          println("Getting payment methods")
          sender ! paymentMethodService.getAll()
      }

    case AddressMethods(uuid) =>
      shippingAddresses match {
        case Some(resolvedShippingAddresses) =>
          println("Served addresses from actor result")
          sender ! resolvedShippingAddresses
        case None =>
          println("Getting addresses")
          sender ! shippingAddressService.getAll()
      }

  }
}
