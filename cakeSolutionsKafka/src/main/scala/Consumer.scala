package cakekafka

import cakesolutions.kafka.KafkaConsumer
import cakesolutions.kafka.KafkaConsumer.Conf

import com.ovoenergy.kafka.serialization.core._
import com.ovoenergy.kafka.serialization.circe._

import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.parser._
import io.circe.java8.time._
import io.buildo.enumero.CirceSupport
import com.typesafe.config.ConfigFactory

import Utils._

import scala.collection.JavaConverters._

object ConsumerTest extends App with CirceSupport {

  val consumerConf = ConfigFactory.load().getConfig("kafka-consumer")
  val consumer = KafkaConsumer(Conf(consumerConf, nullDeserializer[Unit], circeJsonDeserializer[MessageTest]))

  consumer.subscribe(List(topic).asJava)

  while(true) {
    val records = consumer.poll(100)
    for (record <- records.asScala) {
      val r: MessageTest = record.value
      println(s"Retrieved and decode case class $r")
    }
  }
}