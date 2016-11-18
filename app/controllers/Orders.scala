package controllers

import java.util.UUID
import javax.inject.Singleton

import play.api.mvc.Action

@Singleton
class Orders extends BaseController {

  def get(uuid:String) = Action {
    Ok
  }

}
