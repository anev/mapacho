package ru.evseev.mapacho

import com.ning.http.client.Response
import dispatch.{url, Http, Future}
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


  trait ReqOperations {

    private def makeRequest(f: Resp => Unit, method: String) {

      val fut: Future[Response] = {

        var u = dispatch.url(getReq.url).setContentType("application/json", "utf-8").setMethod(method)

        if (method.eq("POST")) {
          u = u.addHeader("Content-length", "0")
        }

        for (r: ReqEntry <- getReq.entries) {
          u = r match {
            case sb: StringBody => u.setBody(sb.value).addHeader("Content-length", String.valueOf(sb.value.length))
            case p: Param => u.addParameter(p.k, p.v)
            case h: Header => u.addHeader(h.k, h.v)
          }
        }

        Http(u)
      }

      val r = Await.result(fut, 5000 millis);

      val m: java.util.Map[String, java.util.List[String]] = r.getHeaders

      val h = for {
        e <- m
      } yield (e._1 -> e._2.head)

      f(Resp(r.getStatusCode, h.toMap, r.getResponseBody("UTF-8")))
    }

    def GET(f: Resp => Unit): Unit = makeRequest(f, "GET")

    def POST(f: Resp => Unit): Unit = makeRequest(f, "POST")

    def param(k: String, v: String): Req = getReq.copy(entries = getReq.entries :+ Param(k, v))

    def p(k: String, v: String) = param(k, v)

    def header(k: String, v: String): Req = getReq.copy(entries = getReq.entries :+ Header(k, v))

    def h(k: String, v: String) = header(k, v)

    def body(b: String): Req = getReq.copy(entries = getReq.entries :+ StringBody(b))

    def getReq: Req
  }

  implicit class MapachoMeStr(x: String) extends ReqOperations {

    override def getReq = Req(url = x)
  }

  implicit class MapachoMeReq(req: Req) extends ReqOperations {

    override def getReq = req
  }

}

case class Req(url: String, entries: Seq[ReqEntry] = Seq.empty)

case class Resp(httpCode: Int, headers: Map[String, String], body: String) {

}

sealed trait ReqEntry

trait Body extends ReqEntry

case class StringBody(value: String) extends Body

case class Param(k: String, v: String) extends ReqEntry

case class Header(k: String, v: String) extends ReqEntry