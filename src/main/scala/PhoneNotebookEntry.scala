import slick.jdbc.PostgresProfile.api._

case class PhoneNotebookEntry(id: Option[Long], name: String, phonenumber: String) {

  def addToDB(database: Database): Unit = {
    val action = PhoneNotebookDatabase.phoneNotebookData returning PhoneNotebookDatabase.phoneNotebookData.map(_.id) += PhoneNotebookEntry(id, name, phonenumber)
    database.run(action)
  }
}