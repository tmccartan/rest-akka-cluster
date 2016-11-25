package controllers

import play.api.mvc._
import service.AddressService

class Addresses (addressService: AddressService) extends BaseController {

  def get(uuid: String) = Action { request =>
    Ok
  }
}
