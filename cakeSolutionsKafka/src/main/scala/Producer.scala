package cakekafka

import java.time.LocalDateTime

import cakesolutions.kafka.KafkaProducer
import cakesolutions.kafka.KafkaProducer.Conf
import cakesolutions.kafka.KafkaProducerRecord

import com.ovoenergy.kafka.serialization.core._
import com.ovoenergy.kafka.serialization.circe._

import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.java8.time._
import io.buildo.enumero.CirceSupport
import com.typesafe.config.ConfigFactory

import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

import Utils._

object ProducerTest extends App with CirceSupport {

  val producerConf = ConfigFactory.load().getConfig("kafka-producer")
  val producer = KafkaProducer(Conf(producerConf, nullSerializer[Unit], circeJsonSerializer[MessageTest]))

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

  messages.foreach { message =>
    val r = KafkaProducerRecord[Unit, MessageTest](topic, message)
    producer.send(r).map { metadata =>
      println(metadata.toString)
    }
  }
  
  producer.close
}