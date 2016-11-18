package actors

import java.util.UUID

import akka.actor.Actor
import com.gilt.akk.cluster.api.test.v0.models.{Address, PaymentMethod}
import com.google.inject.Inject
import service.{AddressService, PaymentMethodService}

import scala.concurrent.Future


case class ResolveSession(uUID: UUID)
case class PaymentMethods(uUID: UUID)
case class AddressMethods(uUID: UUID)

class SessionActor @Inject() (paymentMethodService: PaymentMethodService, shippingAddressService: AddressService) extends Actor {


  var paymentMethods: Option[Future[Seq[PaymentMethod]]] = None
  var shippingAddresses: Option[Future[Seq[Address]]] = None

  override def receive : Receive = {

    case ResolveSession(uuid) => {
      paymentMethods = Some(paymentMethodService.getAll())
      shippingAddresses = Some(shippingAddressService.getAll())
    }
    case PaymentMethods(uuid) => {
      paymentMethods match {
        case Some(resolvedPaymentMethods) => resolvedPaymentMethods
        case None => paymentMethodService.getAll()
      }
    }
    case AddressMethods(uuid) => {
      shippingAddresses match {
        case Some(resolvedShippingAddresses) => resolvedShippingAddresses
        case None => shippingAddressService.getAll()
      }
    }
  }
}
