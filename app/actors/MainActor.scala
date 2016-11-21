package actors

import java.util.UUID

import akka.actor.{Props, ActorRef, Actor}
import akka.util.Timeout
import com.gilt.akk.cluster.api.test.v0.models.{Address, PaymentMethod}
import akka.pattern.ask
import akka.pattern.pipe
import com.google.inject.Inject
import service.{AddressService, PaymentMethodService}
import scala.concurrent.duration._

import scala.concurrent.ExecutionContext.Implicits.global

object MainActorMessages {

  case class ResolveSession(uUID: UUID)
  case class GetPaymentMethods(uUID: UUID)
  case class GetShippingAddresses(uUID: UUID)
}

class MainActor @Inject()(paymentMethodService: PaymentMethodService, addressService: AddressService) extends Actor {

  val sessionActors =  Map[UUID, ActorRef]()

  implicit val timeout: Timeout = 5.seconds
  import MainActorMessages._


  override def receive: Receive = {

    case ResolveSession(uuid:UUID) =>

      withActor(uuid) match {
        case Some(actor: ActorRef) => actor ! ResolveSession
        case None =>
          val sessionActor = Init.instance.system.actorOf(Props(new SessionActor(paymentMethodService, addressService)), "sessionActor")
          sessionActor ! ResolveSession
      }


    case GetPaymentMethods(uuid:UUID) =>
          withActor(uuid) match {
            case Some(actor) => {
              println("asking existing actor")
              ask(actor, GetPaymentMethods).mapTo[Seq[PaymentMethod]] pipeTo sender
            }
            case None =>
              val sessionActor = Init.instance.system.actorOf(Props(new SessionActor(paymentMethodService, addressService)), "sessionActor")
              println("asking new actor")

              ask(sessionActor, GetPaymentMethods).mapTo[Seq[PaymentMethod]] pipeTo sender

          }

    case GetShippingAddresses(uuid: UUID) =>
      withActor(uuid) match {
        case Some(actor) => ask(actor, GetPaymentMethods).mapTo[Seq[Address]] pipeTo sender
        case None =>
          val sessionActor = Init.instance.system.actorOf(Props(new SessionActor(paymentMethodService, addressService)), "sessionActor")

          ask(sessionActor, GetPaymentMethods).mapTo[Seq[PaymentMethod]] pipeTo sender

      }
  }


  def withActor(uuid: UUID) : Option[ActorRef] = {
    sessionActors.find(_._1 == uuid).map(_._2)
  }
}
