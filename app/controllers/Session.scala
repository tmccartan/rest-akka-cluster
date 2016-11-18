package controllers


import javax.inject.Singleton

import com.google.inject.Inject
import play.api.mvc._
import play.api.libs.json._
import com.gilt.akk.cluster.api.test.v0.models.json._
import service.SessionService
import scala.concurrent.ExecutionContext.Implicits.global
@Singleton
class Session @Inject()(sessionService: SessionService) {

  def get() = Action.async {
    implicit request => {
      sessionService.get().map(
        session => Results.Ok(Json.toJson(session))
      )
    }
  }
}
