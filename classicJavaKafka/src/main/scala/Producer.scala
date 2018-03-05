package classickafka

import org.apache.kafka.clients.producer._
import java.util.Properties
import java.time.LocalDateTime

import com.ovoenergy.kafka.serialization.core._
import com.ovoenergy.kafka.serialization.circe._

import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.java8.time._
import io.buildo.enumero.CirceSupport

import Utils._

object ProducerTest extends App with CirceSupport {

  val props = new Properties()
  props.put("bootstrap.servers", "192.168.99.100:9092")
  // 0 -> the producer will not wait for any acknowledgment from the server at all
  // 1 (default) -> the leader will respond without awaiting full acknowledgement from all followers
  // all -> the leader will wait for the full set of in-sync replicas to acknowledge the record.
  //     This guarantees that the record will not be lost as long as at least one in-sync replica remains alive
  props.put("acks", "1")
  // If > 0 then client will resend any record whose send fails with a potentially transient error.
  //     It can change records ordering
  props.put("retries", "3")
  // If = 0, batch will be disabled
  props.put("batch.size", "16384")
  props.put("client.id", "myProducerTest")

  val producer = new KafkaProducer[Unit, MessageTest](props, nullSerializer[Unit], circeJsonSerializer[MessageTest])

  val m1 = MessageTest(
    1,
    "TestingString",
    RandomEnum.First,
    LocalDateTime.now,
    3.5,
    Map("Ciao" -> 5, "Salve" -> 7)
  )

  val m2 = m1.copy(myId = 2, myEnum = RandomEnum.Second, myDateTime = LocalDateTime.now.minusDays(3))
  val m3 = m1.copy(myId = 3, myEnum = RandomEnum.Third, myDateTime = LocalDateTime.now.minusMonths(4))

  val l: List[MessageTest] = List(m1, m2, m3)
  val records = l.map(new ProducerRecord[Unit, MessageTest](topic, _))

  for(record <- records) {
    println(s"Sending $record")
    val res = producer.send(record)
    println(res.get)
  }

  producer.close()
}