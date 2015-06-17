package robot

import akka.actor._
import robot.sensors.Eye

case class LineFollower(left: Eye, right: Eye)
case class EdgeRepeller(left: Eye, right: Eye)
case class WalkAround(left: Eye, right: Eye)

class Process extends Actor with ActorLogging {

  def receive = { 
    case LineFollower(l, r) => sender ! lineFollower(l, r)
    case EdgeRepeller(l, r) => sender ! edgeRepeller(l, r)
    case WalkAround(l, r) => sender ! walkAround(l, r)
    case _ =>
  }

  def lineFollower(left: Eye, right: Eye) = {
    // crossing left to right and right to left
    Step(right.average / 2, left.average / 2)
  }

   val l = new Learn(Array(1, -1, -1, 1, 1, 1))
  def edgeRepeller(left: Eye, right: Eye) = {
    //log.info(s"edge: ${l.a(0)(0)}  ${l.a(0)(1)}  ${l.a(1)(0)}  ${l.a(1)(1)}  ${l.a(0)(2)}  ${l.a(1)(2)}"); 
    Step(l.learn(0)(0) * left.average + l.learn(0)(1) * right.average + l.learn(0)(2), l.learn(1)(0) * left.average + l.learn(1)(1) * right.average + l.learn(1)(2));
  }

  val lw = new Learn(Array(1, 2, -1, 3, 0, 0))
  def walkAround(left: Eye, right: Eye) = {
     //log.info(s"edge: ${lw.a(0)(0).toInt}  ${lw.a(0)(1).toInt}  ${lw.a(1)(0).toInt}  ${lw.a(1)(1).toInt}  ${lw.a(0)(2).toInt}  ${lw.a(1)(2).toInt}"); 
    Step(lw.learn(0)(0) * left.average + lw.learn(0)(1) * right.average + lw.learn(0)(2), lw.learn(1)(0) * left.average + lw.learn(1)(1) * right.average + lw.learn(1)(2));
  }
}

class Learn(start: Array[Int]) {
  val a = Array.ofDim[Double](2, 3)
    a(0)(0) = start(0)
    a(0)(1) = start(2)
    a(0)(2) = start(4)
    a(1)(0) = start(1)
    a(1)(1) = start(3)
    a(1)(2) = start(5)

  def learn = {
    a(0)(0) += Math.random()  - 0.51;
    a(0)(1) += Math.random()  - 0.51;
    a(0)(2) += Math.random()  - 0.51;
    a(1)(0) += Math.random()  - 0.49;
    a(1)(1) += Math.random()  - 0.49;
    a(1)(2) += Math.random()  - 0.49;
    a
  }

}