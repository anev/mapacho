Mapacho
------------

Simple DSL to test REST services

usage example

```scala
"POST requests with custom headers" should "be easy" in {

  "http://headers.jsontest.com/" h("qwe", "asd") body("") POST {
    resp: Resp =>

      resp.httpCode should be(200)
      resp.body should include("headers.jsontest.com")
      resp.body should include("\"qwe\": \"asd\"")
      resp.json \ "qwe" should be(JsString("asd"))
      resp.headers("Content-Type") should be("application/json; charset=ISO-8859-1")
  }
}
```
