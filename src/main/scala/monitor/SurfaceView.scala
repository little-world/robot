package monitor

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

import reality.Surface;

object SurfaceView {

  val BORDER = 50;
  val rect = new Rectangle2D.Double()

  val rop = new RescaleOp(1, 0, null)

  val borderColor = Color.black;
  val surfaceColor = Color.lightGray;
  val lineColor = Color.red;
  val sourceBasketColor = Color.green;
  val targerBasketColor = Color.blue;

  rect.x = 0;
  rect.y = 0;
  rect.height = Surface.SIZE;
  rect.width = Surface.SIZE;

  val bufferedImage = new BufferedImage(Surface.SIZE + BORDER * 2, Surface.SIZE + BORDER * 2, BufferedImage.TYPE_INT_RGB);

  for (x <- 0 to Surface.SIZE - 1)
    for (y <- 0 to Surface.SIZE - 1) {

      val rgb = Surface.get(x, y) match {
        case 0 => borderColor.getRGB
        case 10 => lineColor.getRGB
        case 5 => sourceBasketColor.getRGB
        case 7 => targerBasketColor.getRGB
        case _ => surfaceColor.getRGB
      }
      bufferedImage.setRGB(x + BORDER, y + BORDER, rgb);
    }

  def draw(g: Graphics2D) {

    g.drawImage(bufferedImage, rop, 0, 0);
  }

}
