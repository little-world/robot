package reality

import robot._

object Surface {
  val SIZE = 600;
  val matrix = Array.ofDim[Int](SIZE,SIZE)
  
  // fill the matrix with allowed state 
  // 0 is not allowed by definition 
  for (x <- 0 to SIZE-1 ; y <- 0 to SIZE-1) 
       matrix(x)(y) = 1
  
  // define source basket
  for (x <- 100 to 105; y <- 300 to 305)
      matrix(x)(y) = 5;

  // define source basket
  for (x <- 500 to 505; y <- 300 to 305)
      matrix(x)(y) = 7;

  // create a line on the surface
  for (y <- 50 to 550)
    matrix(202)(y) = 10;

  
  def get(pos: Tuple2[Int, Int]): Int = {
    get(pos._1, pos._2)
  }


  def get(x: Int, y: Int) = {
    if (x >= SIZE-1 || y >= SIZE-1) 0
    else if (x < 0 || y < 0) 0
    else matrix(x)(y)
  }

}


