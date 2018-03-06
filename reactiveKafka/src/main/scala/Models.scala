package reactivekafka

import io.buildo.enumero.annotations.enum
import java.time.LocalDateTime


case class MessageTest(
  myId: Int,
  myString: String,
  myEnum: RandomEnum,
  myDateTime: LocalDateTime,
  myDouble: Double,
  myFeature: Map[String, Int]
)

@enum trait RandomEnum {
  object First
  object Second
  object Third
}

object Utils {
  def topic: String = "messageTestTopic"
}