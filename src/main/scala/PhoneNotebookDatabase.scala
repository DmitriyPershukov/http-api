
import java.sql.DriverManager

import akka.actor.FSM.Failure
import com.typesafe.config.ConfigFactory
import akka.actor.Status.Success
import slick.driver
import slick.jdbc.GetResult
import slick.jdbc.PostgresProfile.api._

import scala.util.{Failure, Success}
import slick.lifted.{ForeignKeyQuery, ProvenShape}

import scala.concurrent._
import ExecutionContext.Implicits.global

object PhoneNotebookDatabase {
  val dataBase = Database.forURL("jdbc:postgresql://localhost/my-db?user=postgres&password=postgres", driver = "org.postgresql.Driver")
  //val con = ConfigFactory.load()
  //val dataBase = Database.forConfig("myb")

  implicit val getSupplierResult = GetResult(r => PhoneNotebookEntry(r.nextLongOption(), r.nextString, r.nextString))
  val phoneNotebookData = TableQuery[PhoneNotebookEntries]
  val phoneNotebookSetup = DBIO.seq(
    phoneNotebookData.schema.create
  )
  val setupFuture = dataBase.run(phoneNotebookSetup)

  def addToDB(phoneNotebookEntry: PhoneNotebookEntry) =
  {
    val action = PhoneNotebookDatabase.phoneNotebookData returning PhoneNotebookDatabase.phoneNotebookData.map(_.id) += phoneNotebookEntry
    dataBase.run(action).map(id => phoneNotebookEntry.copy(id = Some(id)))
  }
  def findByNumber(numberSubString: String)= {
    dataBase.run(sql"""select * from phoneNotebookData where PHONE LIKE '%$numberSubString%'""".as[PhoneNotebookEntry])
  }

  def get(): Future[Seq[PhoneNotebookEntry]]= {
    val f = for (notebook <- PhoneNotebookDatabase.phoneNotebookData)
      yield notebook
    PhoneNotebookDatabase.dataBase.run(f.result)

  }
  def findByName(subString: String) = {
    dataBase.run(sql"""select * from phoneNotebookData where PHONE LIKE '%$subString%'""".as[PhoneNotebookEntry])
  }
  def updateById(id: Long, phoneNotebookEntry: PhoneNotebookEntry) =
    {
    val querrY = for (noteBookEntry <- phoneNotebookData if noteBookEntry.id === id)
      yield (noteBookEntry.name, noteBookEntry.phoneNumber)
    dataBase.run(querrY.update(phoneNotebookEntry.name, phoneNotebookEntry.phonenumber))
  }
  def delete(id: Long) =
  {
    dataBase.run(phoneNotebookData.filter(_.id=== id).delete) map { _ > 0}
  }
  class PhoneNotebookEntries(tag: Tag) extends Table[PhoneNotebookEntry](tag, "PHONE_DATA_ENTRIES")
  {
    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
    def name = column[String]("NAME")
    def phoneNumber = column[String]("PHONE")
    def * = (id.?, name, phoneNumber) <> (PhoneNotebookEntry.tupled, PhoneNotebookEntry.unapply)
  }
}
