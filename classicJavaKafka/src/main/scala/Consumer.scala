package classickafka

import org.apache.kafka.clients.consumer._
import java.util.Properties

import com.ovoenergy.kafka.serialization.core._
import com.ovoenergy.kafka.serialization.circe._

import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.parser._
import io.circe.java8.time._
import io.buildo.enumero.CirceSupport

import Utils._

import scala.collection.JavaConverters._

object ConsumerTest extends App with CirceSupport {

  val props = new Properties()
  props.put("bootstrap.servers", "192.168.99.100:9092")
  props.put("group.id", "myConsumerTest")
  // If true the consumer's offset will be periodically committed in the background.
  props.put("enable.auto.commit", "true");
  // The frequency in milliseconds that the consumer offsets are auto-committed to Kafka if enable.auto.commit is set to true
  props.put("auto.commit.interval.ms", "1000");
  // earliest: automatically reset the offset to the earliest offset
  // latest: automatically reset the offset to the latest offset
  // For attaching new services to kafka 'latest' is probably the best choice. We use 'earliest' just for the purpose of this POC
  props.put("auto.offset.reset", "earliest")

  val consumer = new KafkaConsumer(props, nullDeserializer[Unit], circeJsonDeserializer[MessageTest])
  consumer.subscribe(List(topic).asJava)

  while(true) {
    val records = consumer.poll(100)
    for (record <- records.asScala) {
      val r: MessageTest = record.value
      println(s"Retrieved and decode case class $r")
    }
    // If we don't want autocommit
    // consumer.commitSync()
  }
}