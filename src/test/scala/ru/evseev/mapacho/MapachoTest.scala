package ru.litota

import org.scalatest._
import ru.litota.Mapacho._
import org.scalatest.matchers.{MatchResult, Matcher}

/**
 * Created with IntelliJ IDEA.
 * User: anev
 * Date: 09/10/14
 * Time: 11:08
 * To change this template use File | Settings | File Templates.
 */
class MapachoTest extends FlatSpec with Matchers {


  "POST requests" should "be easy" in {

    "https://account.bkfonbet.com/api/ps/list" body("{}") POST {
      resp: Resp =>

        resp.httpCode should be(200)

        resp.body should include("result")
    }

  }

}
