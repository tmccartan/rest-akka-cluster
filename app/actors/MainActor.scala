package actors

import java.util.UUID

import actors.MainActorMessages.{ResolveSession, GetShippingAddresses, GetPaymentMethods}
import akka.actor.{ActorSystem, Props, ActorRef, Actor}
import akka.util.Timeout
import com.gilt.akk.cluster.api.test.v0.models.{Address, PaymentMethod}
import akka.pattern.ask
import com.google.inject.Inject
import service.{AddressService, PaymentMethodService}
import scala.concurrent.duration._
object MainActorMessages {

  case class ResolveSession(uUID: UUID)
  case class GetPaymentMethods(uUID: UUID)
  case class GetShippingAddresses(uUID: UUID)
}

class MainActor @Inject()(init: Init, paymentMethodService: PaymentMethodService, addressService: AddressService) extends Actor {

  val sessionActors =  Map[UUID, ActorRef]()

  implicit val timeout: Timeout = 5.seconds

  override def receive: Receive = {

    case ResolveSession(uuid:UUID) =>

      withActor(uuid) match {
        case Some(actor: ActorRef) => actor ! ResolveSession
        case None =>
          val sessionActor = init.system.actorOf(Props(new SessionActor(paymentMethodService, addressService)), "sessionActor")
          sessionActor ! ResolveSession
      }


    case GetPaymentMethods(uuid:UUID) =>
          withActor(uuid) match {
            case Some(actor) => ask(actor, GetPaymentMethods).mapTo[Seq[PaymentMethod]]
            case None =>
              val sessionActor = init.system.actorOf(Props(new SessionActor(paymentMethodService, addressService)), "sessionActor")

              ask(sessionActor, GetPaymentMethods).mapTo[Seq[PaymentMethod]]

          }

    case GetShippingAddresses(uuid: UUID) =>
      withActor(uuid) match {
        case Some(actor) => ask(actor, GetPaymentMethods).mapTo[Seq[Address]]
        case None =>
          val sessionActor = init.system.actorOf(Props(new SessionActor(paymentMethodService, addressService)), "sessionActor")

          ask(sessionActor, GetPaymentMethods).mapTo[Seq[PaymentMethod]]

      }
  }

  def withActor(uuid: UUID) : Option[ActorRef] = {
    sessionActors.find(_._1 == uuid).map(_._2)
  }
}
