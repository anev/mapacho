package ru.litota

import dispatch._
import com.ning.http.client.Response
import scala.collection.JavaConversions._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration._

/**
 * Created with IntelliJ IDEA.
 * User: anev
 * Date: 09/10/14
 * Time: 11:05
 * To change this template use File | Settings | File Templates.
 */
object Mapacho {


  implicit class MapachoMeStr(x: String) {
    def param(k: String, v: String): Req = Req(url = x, entries = Seq(Param(k, v)))

    def body(b: String): Req = Req(url = x, entries = Seq(StringBody(b)))
  }

  implicit class MapachoMeReq(req: Req) {

    private def makeRequest(f: Resp => Unit, method: String) {

      val fut: Future[Response] = Http(url(req.url).setContentType("application/json", "utf-8").setMethod(method))

      val r = Await.result(fut, 5000 millis);

      val m: java.util.Map[String, java.util.List[String]] = r.getHeaders

      val h = for {
        e <- m
      } yield (e._1 -> e._2.head)

      f(Resp(r.getStatusCode, h.toMap, r.getResponseBody("UTF-8")))
    }

    def GET(f: Resp => Unit): Unit = makeRequest(f, "GET")

    def POST(f: Resp => Unit): Unit = makeRequest(f, "POST")

    def param(k: String, v: String): Req = req.copy(entries = req.entries :+ Param(k, v))

    def body(b: String): Req = req.copy(entries = req.entries :+ StringBody(b))
  }

}

case class Req(url: String, entries: Seq[ReqEntry] = Seq.empty)

case class Resp(httpCode: Int, headers: Map[String, String], body: String) {

}

trait ReqEntry

trait Body extends ReqEntry

case class StringBody(value: String) extends Body

case class Param(k: String, v: String) extends ReqEntry

case class Header(n: String, v: String) extends ReqEntry