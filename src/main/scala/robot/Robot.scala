package robot

import scala.language.postfixOps


import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success
import scala.util.Failure

import sensors._
import monitor._

case object Start
case object Tick

case object Locate

class RobotActor extends Actor with ActorLogging {
  val bodyActor = context.actorOf(Props(new RobotBody), "body")
  val mind = context.actorOf(Props(new RobotMind(bodyActor)), "mind")
 

  def receive = {
    case Start => heartbeat
    case Tick =>  mind ! Tick
    case _ =>
  }


  def heartbeat = {
    // send the tick message every XX ms (25ms = 40 frames/sec)
    val cancellable =
      context.system.scheduler.schedule(0 milliseconds,
        100 milliseconds,
        self,
        Tick)
  }

}