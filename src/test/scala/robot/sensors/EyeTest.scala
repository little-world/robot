package robot.sensors

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.Success

import org.scalatest.BeforeAndAfter
import org.scalatest.FunSuite
import org.scalatest.Matchers
import org.scalatest.mock.MockitoSugar

import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import robot._

class EyeTests extends FunSuite with BeforeAndAfter with MockitoSugar with Matchers {

  implicit val actorSystem = ActorSystem("akka_robot_test")

  test("eye watch") {
    val eye = new Eye(200, 200)

    val sum = eye.retina.foldLeft(0)((x, t) => t.sum + x)

    assert(sum == 16 * 16 + 9 * 16) // all 1's + 16 10's
  }

  test("eye average") {
    val eye = new Eye(200, 200)

    val avg = eye.average

    assert(avg > 1 && avg < 2)
  }

  test("eye histogram") {
    val eye = new Eye(200, 200)

    val h = eye.histogram

    assert(h(0) == 0)
    assert(h(5) == 0)
    assert(h(7) == 0)
    assert(h(10) == 16)
  }

  test("robot is on the line: both eyes recognize the line with ask") {
    import akka.testkit.TestActorRef
    implicit val timeout = Timeout(5 seconds)

    val actorRef = TestActorRef[EyesActor]
    val actor = actorRef.underlyingActor

    val position = BodyPosition(202, 200, 0, 14, 20)
    val future = actorRef ? Look(position)

    val Success(result: Recognize) = future.value.get
    result.left.histogram(10) should be(16)
    result.right.histogram(10) should be(16)

  }

  test("robot is on the line: both eyes recognize the line with probe") {
    import akka.testkit.TestProbe
    import akka.testkit.TestActorRef

    implicit val timeout = Timeout(5 seconds)
    val system = ActorSystem("akka_robot")
    val testProbe = new TestProbe(system)
    val actorRef = TestActorRef[EyesActor]

    val position = BodyPosition(202, 200, 0, 14, 20)
    actorRef.tell(Look(position), testProbe.ref)
    
    val result = testProbe.expectMsgType[Recognize]
    result.left.histogram(10) should be(16)
    result.right.histogram(10) should be(16)

  }
}