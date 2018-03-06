package reactivekafka

import java.time.LocalDateTime

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.kafka.{ Subscriptions, ConsumerSettings }
import akka.kafka.scaladsl.Consumer
import akka.stream.scaladsl.Source
import akka.stream.scaladsl.Sink
// import akka.actor.ActorRef

import org.apache.kafka.clients.producer.ProducerRecord

import com.ovoenergy.kafka.serialization.core._
import com.ovoenergy.kafka.serialization.circe._

import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.java8.time._
import io.buildo.enumero.CirceSupport

import scala.concurrent.Future
import scala.util.{Failure, Success}
import akka.Done

import Utils._

import scala.collection.JavaConverters._

object ConsumerTest extends App with CirceSupport {

  val system = ActorSystem("kafkaExample")
  implicit val ec = system.dispatcher
  implicit val materializer = ActorMaterializer.create(system)

  val consumerSettings = ConsumerSettings(system, nullDeserializer[Unit], circeJsonDeserializer[MessageTest])
  
  // val consumer: ActorRef = system.actorOf(KafkaConsumerActor.props(consumerSettings))
  val done = Consumer.plainSource(consumerSettings, Subscriptions.topics(topic)).mapAsync(1) { msg =>
      println(s"Retrieved record ${msg.value}")
      // Hack!
      Future.successful(msg)
    }
    .runWith(Sink.ignore)

  done.onComplete {
    case Failure(e) =>
      system.log.error(e, e.getMessage)
      system.terminate()
    case Success(_) => system.terminate()
  }
}