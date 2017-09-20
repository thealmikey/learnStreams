import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape}
import akka.stream.scaladsl._

import scala.concurrent.Future

object Main extends App{
  implicit val system = ActorSystem("mike")
  implicit val materializer = ActorMaterializer()

  case class Student(first:String,age:Int)
  var theStudentList:List[Student] = List(Student("mike",25),Student("joe",17),Student("ngene",21),Student("mercy",26))
  val theStudentsSource:Source[Student,NotUsed] = Source(theStudentList)

  val namePrint:Sink[Student,Future[Done]] = Sink.foreach{  a =>  println(s"hey my name is ${a.first}") }
  val agePrint:Sink[Student,Future[Done]] = Sink.foreach{  a =>  println(s"hey i am ${a.age} years old") }

  val g = RunnableGraph.fromGraph(GraphDSL.create(){
    implicit b =>
      import GraphDSL.Implicits._
      val bcast = b.add(Broadcast[Student](2))
      theStudentsSource ~> bcast
      bcast.out(0) ~>namePrint
      bcast.out(1) ~> agePrint
      ClosedShape
  })

  g.run()
}
