package robot.actuators

import akka.actor._

import robot._

import reality._


class FeetActor extends Actor {
 
  def receive = {
    case step: Step => doStep(step)
    case _ =>
  }

  def doStep(step: Step) = {
       Reality.leftFoot.step = step.left
       Reality.rightFoot.step = step.right
  }
}

// emulates a step motor
class Foot {
  val radius = 10;
  var _step: Double = 0;

  //getter
  def step = {
    val temp = _step;
    _step = 0;
    temp;
  }

  // setter 
  def step_=(value: Double): Unit = _step += value
}

