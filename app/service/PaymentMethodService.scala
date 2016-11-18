package service

import java.util.UUID

import com.gilt.akk.cluster.api.test.v0.models.PaymentMethod

import scala.concurrent.Future


class PaymentMethodService {

  def getAll() : Future[Seq[PaymentMethod]] = {
    //simulate remote call
    Thread.sleep(1000)
    Future.successful(Seq.fill(3)(PaymentMethod(UUID.randomUUID(), "test")))
  }
}
