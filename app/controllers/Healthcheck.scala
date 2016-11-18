package controllers

import javax.inject.Singleton

import com.gilt.akk.cluster.api.test.v0.models.{Healthcheck => HealthCheckModel}
import com.gilt.akk.cluster.api.test.v0.models.json._
import play.api.mvc._
import play.api.libs.json._


@Singleton
class Healthcheck extends Controller {

  def get() = Action{ implicit request =>
    Ok(Json.toJson(HealthCheckModel("Akka cluster test")))
  }
}
