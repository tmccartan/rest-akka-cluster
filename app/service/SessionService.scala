package service

import java.util.UUID

import actors.{Init, ResolveSession, MainActor}
import com.gilt.akk.cluster.api.test.v0.models.{Item, Session}
import com.google.inject.Inject
import akka.actor.{ActorRef, Actor}

import scala.concurrent.Future

class SessionService  @Inject ()(val init: Init){

  def get() : Future [Session] = {

    val guid = UUID.randomUUID()
    val paymentLink = s"/session/$guid/payments_methods"
    val shippingLink = s"/session/$guid/shipping_methods"
    val orderLink = s"/session/$guid/order"
    val userLink = s"/session/$guid/user"
    val items = Seq.fill(10)(Item(UUID.randomUUID(), ""))

    init.mainActor ! ResolveSession(guid)

    Future.successful(Session(guid, userLink, shippingLink, paymentLink,items, orderLink))

  }
}
