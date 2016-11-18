package actors

import java.util.UUID

import akka.actor.Actor
import akka.actor.Actor.Receive
import akka.cluster.Cluster
import akka.event.Logging
import akka.pattern.pipe
import com.google.inject.Inject
import service.AddressService


import scala.concurrent.ExecutionContext.Implicits.global


case class ShippingAddressMessage (guid: UUID)

class ShippingAddressActor @Inject()(addressService: AddressService) extends Actor {

  val cluster = Cluster(context.system)
  val log = Logging(context.system, this)

  override def receive: Receive = {
    case MainActorMessages.GetShippingAddresses(guid:UUID) =>
      log.info("received ShippingAddress message")
      addressService.getAll() pipeTo sender
  }
}
