import akka.actor.ActorSystem

import akka.stream.scaladsl._
import com.typesafe.config._
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{MessageEntity, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import spray.json.RootJsonFormat
import scala.io.StdIn
import spray.json.DefaultJsonProtocol._

import scala.concurrent.ExecutionContext.Implicits.global

Inimport scala.concurrent._
import scala.util.{Failure, Success}

object webService extends App {

  import JsonFormats._
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

  implicit val actorSystem = ActorSystem()
  val routes: Route = concat(

    pathPrefix("phone" / LongNumber) {
      { id =>
        concat(
          post {
            entity(as[PhoneNotebookEntry]) { phoneNotebookEntry =>
              PhoneNotebookDatabase.updateById(id, phoneNotebookEntry)
              complete((StatusCodes.Created))
            }
          },
          delete {
            PhoneNotebookDatabase.delete(id)
            complete(StatusCodes.OK)
          }
        )
      }
    },
    pathPrefix("phones") {
      concat(
        pathEnd {
          get {
            onComplete(PhoneNotebookDatabase.get()) {
              case Failure(exo) => complete(StatusCodes.InternalServerError, s"${exo.getMessage}")
              case Success(outp) => complete(Marshal(outp).to[MessageEntity])
            }
          }
        },
        (path("searchByName") & parameter("nameSubstring")) { parameter =>
          onComplete(PhoneNotebookDatabase.findByName(parameter)) {
            case Success(value) => complete(Marshal(value).to[MessageEntity])
            case Failure(xe) =>complete(StatusCodes.InternalServerError)
          }
        },
        path("createNewPhone") {
          post {
            entity(as[PhoneNotebookEntry]) { phoneNotebookEntry =>
              PhoneNotebookDatabase.addToDB(phoneNotebookEntry)
              complete((StatusCodes.Created))
            }
          }
        },
        (path("searchByNumber") & parameter("numberSubstring")) { parameter =>
          onComplete(PhoneNotebookDatabase.findByName(parameter)) {
            case Success(out) => complete(Marshal(out).to[MessageEntity])
            case Failure(e) => complete(StatusCodes.InternalServerError, "" + e.getMessage)
          }
        }
      )
    }
  )
  val bindingFuture = Http().bindAndHandle(routes, "localhost", 8080)
  StdIn.readLine()
}
