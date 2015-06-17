package robot.sensors

import akka.actor._
import robot._
import reality._
import javax.media.jai.Histogram


class EyesActor extends Actor with ActorLogging {
  def receive = {
    case Look(position) => look(position)
    case _ =>
  }

  def look(position: BodyPosition) = {
    val eyes = new Eyes(position)
    sender ! Recognize(eyes.left, eyes.right)
  }
}

class Eyes (bodyPosition: BodyPosition) {
   val left = new Eye(leftEyePosition(bodyPosition)._1, leftEyePosition(bodyPosition)._2)
   val right = new Eye(rightEyePosition(bodyPosition)._1, rightEyePosition(bodyPosition)._2)
  
   def rightEyePosition(bodyPosition: BodyPosition) = {
    val x = bodyPosition.x + 0.5 * bodyPosition.width * Math.cos(bodyPosition.phi) - 0.5 * bodyPosition.height * Math.sin(bodyPosition.phi)
    val y = bodyPosition.y + 0.5 * bodyPosition.height * Math.cos(bodyPosition.phi) + 0.5 * bodyPosition.width * Math.sin(bodyPosition.phi)
    (x, y)
  }

  def leftEyePosition(bodyPosition: BodyPosition) = {
    val x = bodyPosition.x - 0.5 * bodyPosition.width * Math.cos(bodyPosition.phi) - 0.5 * bodyPosition.height * Math.sin(bodyPosition.phi);
    val y = bodyPosition.y + 0.5 * bodyPosition.height * Math.cos(bodyPosition.phi) - 0.5 * bodyPosition.width * Math.sin(bodyPosition.phi);
    (x, y)
  }
}

class Eye(val x: Double, val y: Double) {
  val radius = 16;
  val surface = Surface

  val retina = Array.ofDim[Int](radius, radius)
  for (i <- 0 to radius - 1)
    for (k <- 0 to radius - 1)
      retina(i)(k) = Surface.get(x.toInt - 8 + i, y.toInt - 8 + k);

  def histogram: Array[Int] = {
    // 11 parameter for the eye
    // 0, 5, 7, 10 are defined
    val temp = Array.ofDim[Int](11)

    for (i <- 0 to radius - 1)
      for (k <- 0 to radius - 1)
        temp(Math.round(retina(i)(k))) += 1;

    temp
  }

  def average = retina.foldLeft(0)((x, t) => t.sum + x) / (radius * radius).toDouble // force double division
}