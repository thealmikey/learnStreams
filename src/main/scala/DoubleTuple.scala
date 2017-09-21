import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, FlowShape}
import akka.{Done, NotUsed}
import akka.stream.scaladsl._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object DoubleTuple extends App{

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()


  val printSink:Sink[(Int,String),Future[Done]] = Sink.foreach(println(_))
  val intSource:Source[Int,NotUsed] = Source(1 to 20)

  val doubleTupleFlow = Flow.fromGraph(GraphDSL.create(){
    implicit b =>
      import GraphDSL.Implicits._

      val broadcast = b.add(Broadcast[Int](2))
      val zip = b.add(Zip[Int,String]())

      broadcast.out(0) ~> zip.in0
      broadcast.out(1).map{ a =>
        val b = a*2
        s"i got double so i gat $b"
      }~>zip.in1

        FlowShape(broadcast.in,zip.out)
  })

  doubleTupleFlow.runWith(intSource, printSink)
}