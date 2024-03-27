package robot

import scala.language.postfixOps
import akka.actor._
import akka.event.Logging
import sensors._

import akka.pattern.{ ask, pipe }
import akka.util.Timeout
import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
/*
 * RobotMind is responsible for the recognition of things in the reality
 * The mind distributes the heartbeat in Do messsages tot the sensors
 * From the sensors the mind recieves messages to be recognized
 * 
 */

case class Look(position: BodyPosition)
case class Recognize(left: Eye, right: Eye)

class RobotMind(body: ActorRef) extends Actor with ActorLogging {
  val eyes = context.actorOf(Props(new EyesActor), "eyes")
  val process = context.actorOf(Props(new Process), "process")

  def receive = {
    case Tick => body ! Locate
    case position: BodyPosition =>  eyes ! Look(position)
    case Recognize(left, right) => decide(left, right)
    case step: Step => body ! step
    case _ =>
  }

  def decide(left: Eye, right: Eye) {
    if (left.histogram(10) > 0 || right.histogram(10) > 0) // line detected
      process ! LineFollower(left, right)
    else if (left.histogram(0) > 0 || right.histogram(0) > 0) // edge detected
      process ! EdgeRepeller(left, right)
    else // no line or edge
      process ! WalkAround(left, right)
  }
}