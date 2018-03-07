package reactivekafka

import java.time.LocalDateTime

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.scaladsl.Source

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

object ProducerTest extends App with CirceSupport {

  val system = ActorSystem("kafkaExample")

  implicit val ec = system.dispatcher
  implicit val materializer = ActorMaterializer.create(system)
  

  val producerSettings = ProducerSettings(system, nullSerializer[Unit], circeJsonSerializer[MessageTest])
  // val producerSettings = ProducerSettings(config, nullSerializer[Unit], circeJsonSerializer[MessageTest])
  val kafkaProducer = producerSettings.createKafkaProducer()

  val m1 = MessageTest(
    1,
    "TestingString",
    RandomEnum.First,
    LocalDateTime.now,
    3.5,
    Map("Ciao" -> 5, "Salve" -> 7)
  )

  val m2 = m1.copy(myId = 18, myEnum = RandomEnum.Second, myDateTime = LocalDateTime.now.minusDays(3))
  val m3 = m1.copy(myId = 3, myEnum = RandomEnum.Third, myDateTime = LocalDateTime.now.minusMonths(4))

  val messages: List[MessageTest] = List(m1, m2, m3)

  val done = Source(messages)
    .map { new ProducerRecord[Unit, MessageTest](topic, _) }
    .runWith(Producer.plainSink(producerSettings, kafkaProducer))

  // For POC purposes
  done.onComplete {
    case Failure(e) =>
      system.log.error(e, e.getMessage)
      system.terminate()
    case Success(_) => system.terminate()
  }

}
