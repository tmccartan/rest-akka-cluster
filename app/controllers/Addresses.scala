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

  def get(uuid: String) = Action { request =>
    Ok
  }
}
