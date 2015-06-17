package robot

import scala.language.postfixOps

import actuators.FeetActor
import actuators.Foot
import akka.actor._
import akka.actor.ActorLogging
import akka.actor.ActorSelection.toScala
import akka.actor.Props
import akka.actor.actorRef2Scala
import scala.language.postfixOps
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent._
import scala.concurrent.duration._
import reality.Surface
import scala.concurrent.ExecutionContext.Implicits.global

import main._

case class BodyPosition(x: Double, y: Double, phi: Double, width: Int, height: Int)
case class Step(left: Double, right: Double)

class RobotBody extends Actor with ActorLogging {
  val feetActor = context.actorOf(Props(new FeetActor), "FeetActor")
  def receive = {
    case Locate => sender ! currentPosition
    case Step(left, right) => calculateNewBodyPosition(left, right)
    case _ =>
  }

  // -------------------
  // body state
  val width = 15
  val height = 20
  val aura = Math.sqrt(width * width + height * height) / 2;
  var currentPosition = BodyPosition(280, 300, 0, width, height)
  // -------------------

  def calculateNewBodyPosition(left: Double, right: Double) {
    // calculate new temp currentPosition
    // check on surface
    // update currentPosition: x, y, phi

    val leftFoot = new Foot
    val rightFoot = new Foot

    // calculate new position from step
    val phiDeltaLeft = left * leftFoot.radius / width;
    val phiDeltaRight = right * rightFoot.radius / width;

    val tempXR = currentPosition.x + 0.5 * width * (Math.cos(phiDeltaRight + currentPosition.phi) - Math.cos(currentPosition.phi));
    val tempYR = currentPosition.y + 0.5 * width * (Math.sin(phiDeltaRight + currentPosition.phi) - Math.sin(currentPosition.phi));

    val tempXL = tempXR - 0.5 * width * (Math.cos(-phiDeltaLeft + currentPosition.phi) - Math.cos(currentPosition.phi));
    val tempYL = tempYR - 0.5 * width * (Math.sin(-phiDeltaLeft + currentPosition.phi) - Math.sin(currentPosition.phi));

    val tempPhi = currentPosition.phi + phiDeltaRight - phiDeltaLeft;

    //log.info (s"$left $right : $tempXL $tempYL")
    
    if (allowedPosition(tempXL, tempYL)) {
      currentPosition = BodyPosition(tempXL, tempYL, tempPhi, width, height)
      feetActor ! Step(left, right)
      Main.monitorActor ! currentPosition
     
    }

  }

  def allowedPosition(x: Double, y: Double): Boolean = {
    // not outside the surface
    x > aura && y > aura && x < Surface.SIZE - aura & y < Surface.SIZE - aura
  }

  def allowedRobotPosition(otherRobotPosition: BodyPosition) = {
    // cannot be on the seem position as an other robot
    val d = Math.sqrt((currentPosition.x - otherRobotPosition.x) * (currentPosition.x - otherRobotPosition.x) + (currentPosition.y - otherRobotPosition.y) * (currentPosition.y - otherRobotPosition.y));
    d > 2 * aura;

  }

}