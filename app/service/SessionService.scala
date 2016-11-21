package service

import java.util.UUID

import actors.{Init, ResolveSession, MainActor}
import com.gilt.akk.cluster.api.test.v0.models.{Item, Session}
import com.google.inject.Inject
import akka.actor.{ActorRef, Actor}

import scala.concurrent.Future

class SessionService  {

  def get() : Future [Session] = {

    val guid = UUID.randomUUID()
    //build the urls for the slow resources so they can be loaded later on
    val paymentLink = s"/session/$guid/payment_methods"
    val shippingLink = s"/session/$guid/addresses"
    val orderLink = s"/session/$guid/order"
    val userLink = s"/session/$guid/user"
    val items = Seq.fill(10)(Item(UUID.randomUUID(), ""))

    Init.instance.mainActor ! ResolveSession(guid)

    Future.successful(Session(guid, userLink, shippingLink, paymentLink, items, orderLink))

  }
}
