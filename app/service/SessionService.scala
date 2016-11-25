package service

import java.util.UUID

import actors.MainActorMessages.ResolveSession
import com.gilt.akk.cluster.api.test.v0.models.{Item, Session}
import akka.actor.ActorRef

import scala.concurrent.{ExecutionContext, Future}

class SessionService(mainActor: ActorRef) {

  def get()(implicit ex: ExecutionContext) : Future [Session] = {

    val guid = UUID.randomUUID()
    //build the urls for the slow resources so they can be loaded later on
    val paymentLink = s"/session/$guid/payment_methods"
    val shippingLink = s"/session/$guid/addresses"
    val orderLink = s"/session/$guid/order"
    val userLink = s"/session/$guid/user"
    val items = Seq.fill(10)(Item(UUID.randomUUID(), ""))

    mainActor ! ResolveSession(guid)

    Future.successful(Session(guid, userLink, shippingLink, paymentLink, items, orderLink))

  }
}
