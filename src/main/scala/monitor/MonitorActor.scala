package monitor

import akka.actor._
import robot._
import javax.swing._
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Color
import scala.collection.mutable.ListBuffer

import scala.collection.mutable.Map

case class ShowMonitor()

case class UpdateScene(Position: BodyPosition)

class Monitor extends Actor with ActorLogging {

  def receive = {
    case position: BodyPosition => addRobot(position, sender)
    case Start => mainFrame.setVisible(true)
    case _ =>
  }

  val map = Map[String, BodyPosition]()

  def addRobot(position: BodyPosition, robotBodyRef: ActorRef) {
    val name = robotBodyRef.path.parent.name
    map.update(name, position) // the values are overridden on the same key, so the map contains as many values as there are robots

    canvas.repaint()

  }

  val canvas = new Canvas(this);

  val mainFrame = new JFrame {
    setSize(700, 700)
    setTitle("akka robot")
    setBackground(Color.WHITE)
    setLocation(100, 100)
    getContentPane.add(canvas)
    setUndecorated(true)
  }

}

class Canvas(monitor: Monitor) extends JComponent {

  override def paintComponent(g: Graphics) {
    val g2d = g.asInstanceOf[Graphics2D]

    SurfaceView.draw(g2d)

    for (pos <- monitor.map.values)
      RobotView.draw(g2d, pos)
  }

}