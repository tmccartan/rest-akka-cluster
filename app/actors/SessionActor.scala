package actors

import java.util.UUID

import akka.actor.Actor
import akka.pattern.pipe
import com.gilt.akk.cluster.api.test.v0.models.{Address, PaymentMethod}
import com.google.inject.Inject
import service.{AddressService, PaymentMethodService}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

case class ResolveSession(uUID: UUID)
case class PaymentMethods(uUID: UUID)
case class AddressMethods(uUID: UUID)

class SessionActor @Inject() (paymentMethodService: PaymentMethodService, shippingAddressService: AddressService) extends Actor {

  var paymentMethods: Option[Future[Seq[PaymentMethod]]] = None
  var shippingAddresses: Option[Future[Seq[Address]]] = None

  override def receive : Receive = {

    case ResolveSession(uuid) =>
      paymentMethods = Some(paymentMethodService.getAll())
      shippingAddresses = Some(shippingAddressService.getAll())

    case PaymentMethods(uuid) =>
      paymentMethods match {
        case Some(resolvedPaymentMethods) =>
          println("Served payment methods from actor result")
          resolvedPaymentMethods pipeTo sender

        case None =>
          println("Getting payment methods")
          paymentMethodService.getAll() pipeTo sender

      }

    case AddressMethods(uuid) =>
      shippingAddresses match {
        case Some(resolvedShippingAddresses) =>
          println("Served addresses from actor result")
          resolvedShippingAddresses pipeTo sender
        case None =>
          println("Getting addresses")
          shippingAddressService.getAll() pipeTo sender
      }

  }
}
