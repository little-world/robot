package main

import akka.actor._
import monitor._
import robot._



object Main extends App {
   val actorSystem = ActorSystem("akka_robot")
  
  val robot1 = actorSystem.actorOf(Props(new robot.RobotActor), name = "robot")
  val monitorActor = actorSystem.actorOf(Props(new Monitor), "monitor")
  
  monitorActor ! Start
  robot1 ! Start
}