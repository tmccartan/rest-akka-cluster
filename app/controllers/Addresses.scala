package controllers

import java.util.UUID
import javax.inject.Singleton

import actors.{ShippingAddressMessage, AkkaConfig, ShippingAddressActor}
import akka.actor.{ActorSystem, Props, ActorRef}
import akka.util.Timeout

import scala.concurrent.duration._

import com.gilt.akk.cluster.api.test.v0.models.Address
import com.gilt.akk.cluster.api.test.v0.models.json._
import com.google.inject.Inject
import play.api.mvc._
import play.api.libs.json._
import service.AddressService
import akka.pattern.ask
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class Addresses @Inject () (val addressService: AddressService) extends BaseController {

  implicit val timeout = Timeout(10.seconds)
  val addressActorRef = system.actorOf(Props(new ShippingAddressActor(addressService)))

  def get(uuid: String) = Action.async { request =>
    val res = ask(addressActorRef, ShippingAddressMessage).mapTo[Seq[Address]]

    res.map(address => {
        Results.Ok(Json.toJson(address))
    })
  }
}
