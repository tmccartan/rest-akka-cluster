package controllers


import javax.inject.Singleton
import actors.MainActorMessages.GetPaymentMethods
import actors.Init
import akka.util.Timeout
import com.gilt.akk.cluster.api.test.v0.models.PaymentMethod
import scala.concurrent.duration._

import com.google.inject.Inject
import play.api.libs.json._
import com.gilt.akk.cluster.api.test.v0.models.json._

import play.api.mvc._
import akka.pattern.ask
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class PaymentMethods extends BaseController {

  implicit val timeout = Timeout(10.seconds)

  def get(uuid: String) = Action.async { request =>
    val res = ask(Init.instance.mainActor, GetPaymentMethods).mapTo[Seq[PaymentMethod]]

    res.map(paymentMethods => {
      Results.Ok(Json.toJson(paymentMethods))
    })
  }
}
