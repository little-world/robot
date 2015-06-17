package monitor

import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.AffineTransform
import java.awt.geom.Area
import java.awt.geom.Ellipse2D
import java.awt.geom.Rectangle2D
import robot._
import robot.sensors._
import reality.Reality

object RobotView {
  val instanceCount = 1
  val instance = 1;

  val PUPILSIZE = 4;
  val FOOTSIZE = 4;

  val bodyColor = Color.red;
  val eyeColor = new Color(210, 210, 210);
  val eyeColorBorder = new Color(140, 140, 140);
  val pupilColor = Color.black;
  val footColor = new Color(255, 200, 200);

  val rect = new Rectangle2D.Double();
  val eyeRadius = (new Eye(0, 0)).radius
  val leftEye = new Rectangle2D.Double(0, 0, eyeRadius * 2, eyeRadius * 2);
  val rightEye = new Rectangle2D.Double(0, 0, eyeRadius * 2, eyeRadius * 2);

  val leftEyePupil = new Ellipse2D.Double(0, 0, PUPILSIZE, PUPILSIZE);
  val rightEyePupil = new Ellipse2D.Double(0, 0, PUPILSIZE, PUPILSIZE);

  val leftFoot = new Rectangle2D.Double(0, 0, FOOTSIZE, 0);
  val rightFoot = new Rectangle2D.Double(0, 0, FOOTSIZE, 0);

  def draw(g: Graphics2D, position: BodyPosition) {

    rect.height = position.height
    rect.width = position.width
    rect.x = position.x - position.width / 2 + SurfaceView.BORDER;
    rect.y = position.y - position.height / 2 + SurfaceView.BORDER;

    leftFoot.x = rect.x - FOOTSIZE - 1;

    leftFoot.y = rect.y + position.height / 2 - Reality.leftFoot.step * 15;
    leftFoot.height = Reality.leftFoot.step * 30;

    rightFoot.x = rect.x + position.width + 1;

    rightFoot.y = rect.y + position.height / 2 - Reality.rightFoot.step * 15;
    rightFoot.height = Reality.rightFoot.step * 30;

    val eyes = new Eyes(position)

    leftEye.x = eyes.left.x + SurfaceView.BORDER - eyes.left.radius;
    leftEye.y = eyes.left.y + SurfaceView.BORDER - eyes.left.radius;

    leftEyePupil.x = eyes.left.x + SurfaceView.BORDER - PUPILSIZE / 2;
    leftEyePupil.y = eyes.left.y + SurfaceView.BORDER - PUPILSIZE / 2;

    rightEye.x = eyes.right.x + SurfaceView.BORDER - eyes.right.radius;
    rightEye.y = eyes.right.y + SurfaceView.BORDER - eyes.right.radius;

    rightEyePupil.x = eyes.right.x + SurfaceView.BORDER - PUPILSIZE / 2;
    rightEyePupil.y = eyes.right.y + SurfaceView.BORDER - PUPILSIZE / 2;

    
    
    val bb = new Area(rect);
    bb.add(new Area(leftFoot));
    bb.add(new Area(rightFoot));

    val leftArea = new Area(leftEye);
    val rightArea = new Area(rightEye);

    val at = new AffineTransform();

    at.rotate(position.phi, position.x + SurfaceView.BORDER, position.y + SurfaceView.BORDER);

    g.setColor(eyeColor);
    g.fill(leftArea);
    g.fill(rightArea);

    g.setColor(eyeColorBorder);
    g.draw(leftArea);
    g.draw(rightArea);

    g.setColor(bodyColor);
    g.fill(bb.createTransformedArea(at));

    g.setColor(pupilColor);
    g.fill(new Area(leftEyePupil));
    g.fill(new Area(rightEyePupil));

    // g.setColor(bodyColor);
    // g.draw(rect);

  }

}
