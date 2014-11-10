package ru.evseev.mapacho

import org.scalatest._
import ru.evseev.mapacho.Mapacho._
import org.scalatest.matchers.{MatchResult, Matcher}

/**
 * Created with IntelliJ IDEA.
 * User: anev
 * Date: 09/10/14
 * Time: 11:08
 * To change this template use File | Settings | File Templates.
 */
class MapachoTest extends FlatSpec with Matchers {

  "GET requests" should "be easy" in {

    "http://headers.jsontest.com/" GET {
      resp: Resp =>

        resp.httpCode should be(200)
        resp.body should include("headers.jsontest.com")
        resp.body should include("Dispatch/0.11.1-SNAPSHOT")
    }
  }

  "GET requests with custom headers" should "be easy" in {

    "http://headers.jsontest.com/" header("h1", "v1") GET {
      resp: Resp =>

        resp.httpCode should be(200)
        resp.body should include("headers.jsontest.com")
        resp.body should include("\"h1\": \"v1\",")
    }
  }

  "POST requests with custom headers" should "be easy" in {

    "http://headers.jsontest.com/" h("qwe", "asd") POST {
      resp: Resp =>

        resp.httpCode should be(200)
        resp.body should include("headers.jsontest.com")
        resp.body should include("\"qwe\": \"asd\"")
    }
  }

}
