package actors

import java.util.UUID

import akka.actor.{Props, ActorRef, Actor}
import akka.util.Timeout
import com.google.inject.Inject
import service.{AddressService, PaymentMethodService}
import scala.concurrent.duration._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}

object MainActorMessages {
  case class FindActor(uUID: UUID)
  case class ResolveSession(uUID: UUID)
  case class GetPaymentMethods(uUID: UUID)
  case class GetShippingAddresses(uUID: UUID)
}

class MainActor @Inject()(paymentMethodService: PaymentMethodService, addressService: AddressService) extends Actor {

  val sessionActors =  Map[UUID, ActorRef]()

  implicit val timeout: Timeout = 5.seconds
  import MainActorMessages._

  override def receive: Receive = {

    case FindActor(uuid) =>
      // need to keep reference of the sender since it could change
      val caller = context.sender
      withActor(uuid) andThen {
        case Success(actor) => caller ! actor
        case Failure(t) => caller ! new RuntimeException("No actor found")
      }

    case ResolveSession(uuid:UUID) =>
      withActor(uuid) map {
        case (actor: ActorRef) => actor ! ResolveSession
          actor ! ResolveSession
      } recover {
        case _ =>
          println("create new  actor and ask")
          val actor = createActor(uuid)
          actor ! SessionActorMessages.ResolveSession(uuid)
      }

  }


  def createActor(uUID: UUID): ActorRef ={
    context.system.actorOf(Props(new SessionActor(paymentMethodService, addressService)), s"actor.$uUID")
  }
  def withActor(uuid: UUID ) = {
    context.system.actorSelection("/user/actor." + uuid).resolveOne()
  }
}
