package mapGenerator

import utilities.Vector2D

import java.util

/*
  Map generator for battle simulation.
*/

object MapGenerator{
  class Cell(val squareSide: Int, val x: Int, val y: Int,
             val sparseForestIsoValue: Int,
             val altitude: Double,
             val denseForestIsoValue: Int,
             val riverIsoValue: Int) {

    val isRiver: Boolean = if(MapConst.inTerrainIsoValue.contains(riverIsoValue)) true else false
    val isDenseForest: Boolean = if(!isRiver && MapConst.inTerrainIsoValue.contains(denseForestIsoValue)) true else false
    val isSparseForest: Boolean = if(!isDenseForest && !isRiver && MapConst.inTerrainIsoValue.contains(sparseForestIsoValue)) true else false

    val compass: Compass = Compass(squareSide)

    override def toString: String = s"$altitude"

    def getPolygons(terrain: String): (Vector[Int], Vector[Int]) = {
      val xPix = y*squareSide
      val yPix = x*squareSide
      var isoValue = 0

      if(terrain == "forest")
        isoValue = sparseForestIsoValue
      else if(terrain == "river")
        isoValue = riverIsoValue

      isoValue match {
        case 1 => (Vector(xPix, xPix + compass.xWEST, xPix + compass.xSOUTH),
          Vector(yPix + squareSide, yPix + compass.yWEST, yPix + compass.ySOUTH))
        case 2 => (Vector(xPix + squareSide, xPix + compass.xSOUTH, xPix + compass.xEAST),
          Vector(yPix + squareSide, yPix + compass.ySOUTH, yPix + compass.yEAST))
        case 3 => (Vector(xPix + compass.xWEST, xPix + compass.xEAST, xPix + squareSide, xPix),
          Vector(yPix + compass.yWEST, yPix + compass.yEAST, yPix + squareSide, yPix + squareSide))
        case 4 => (Vector(xPix + squareSide, xPix + compass.xNORTH, xPix + compass.xEAST),
          Vector(yPix, yPix + compass.yNORTH, yPix + compass.yEAST))
        case 5 => (Vector(xPix + squareSide, xPix + compass.xEAST, xPix + compass.xSOUTH, xPix, xPix + compass.xWEST, xPix + compass.xNORTH),
          Vector(yPix, yPix + compass.yEAST, yPix + compass.ySOUTH, yPix + squareSide, yPix + compass.yWEST, yPix + compass.yNORTH))
        case 6 => (Vector(xPix + squareSide, xPix + squareSide, xPix + compass.xSOUTH, xPix + compass.xNORTH),
          Vector(yPix, yPix + squareSide, yPix + compass.ySOUTH, yPix + compass.yNORTH))
        case 7 => (Vector(xPix + compass.xWEST, xPix + compass.xNORTH, xPix + squareSide, xPix + squareSide, xPix),
          Vector(yPix + compass.yWEST, yPix + compass.yNORTH, yPix, yPix + squareSide, yPix + squareSide))
        case 8 => (Vector(xPix, xPix + compass.xNORTH, xPix + compass.xWEST), Vector(yPix, yPix + compass.yNORTH, yPix + compass.yWEST))
        case 9 => (Vector(xPix + compass.xNORTH, xPix + compass.xSOUTH, xPix, xPix),
          Vector(yPix + compass.yNORTH, yPix + compass.ySOUTH, yPix + squareSide, yPix))
        case 10 => (Vector(xPix, xPix + compass.xNORTH, xPix + compass.xEAST, xPix + squareSide, xPix + compass.xSOUTH, xPix + compass.xWEST),
          Vector(yPix, yPix + compass.yNORTH, yPix + compass.yEAST, yPix + squareSide, yPix + compass.ySOUTH, yPix + compass.yWEST))
        case 11 => (Vector(xPix + compass.xNORTH, xPix + compass.xEAST, xPix + squareSide, xPix, xPix),
          Vector(yPix + compass.yNORTH, yPix + compass.yEAST, yPix + squareSide, yPix + squareSide, yPix))
        case 12 => (Vector(xPix, xPix + squareSide, xPix + compass.xEAST, xPix + compass.xWEST),
          Vector(yPix, yPix, yPix + compass.yEAST, yPix + compass.yWEST))
        case 13 => (Vector(xPix + compass.xEAST, xPix + compass.xSOUTH, xPix, xPix, xPix + squareSide),
          Vector(yPix + compass.yEAST, yPix + compass.ySOUTH, yPix + squareSide, yPix, yPix))
        case 14 => (Vector(xPix + compass.xSOUTH, xPix + compass.xWEST, xPix, xPix + squareSide, xPix + squareSide),
          Vector(yPix + compass.ySOUTH, yPix + compass.yWEST, yPix, yPix, yPix + squareSide))
        case 15 => (Vector(xPix, xPix + squareSide, xPix + squareSide, xPix),
          Vector(yPix, yPix, yPix + squareSide, yPix + squareSide))
        case _ => null
      }
    }
  }

  object Cell {
    def apply(squareSide: Int, x: Int, y: Int,
              sparseForestIsoValue: Int, altitude: Double, denseForestIsoValue: Int, riverIsoValue: Int): Cell =
      new Cell(squareSide, x, y, sparseForestIsoValue, altitude, denseForestIsoValue, riverIsoValue)
  }

  object MapConst {
    val inTerrainIsoValue: Array[Int] = Array(3, 5, 6, 7, 9, 10, 11, 12, 13, 14 ,15)

    val forestFrequency = 0.02
    val terrainFrequency = 0.02
    val sparseForestThresh = 0.3
    val denseForestThresh: Double = sparseForestThresh + 0.1

    val obstaclesFrequency = 0.15
    val obstaclesThresh = 0.2
  }

  class Map(val height: Int, val width: Int) extends Terrain {
    import mapGUI.MapGUI

    val squareSide:Int = Math.min(800/width, 800/height)
    val windowWidth:Int = squareSide * width
    val windowHeight:Int = squareSide * height

    //Cell(squareSide)
    val biomPerlin: PerlinNoise = PerlinNoise(128)
    val terrainPerlin: PerlinNoise = PerlinNoise(128)

    //point 1 is black point 0 is white
    val forestPoints: Seq[Vector[Double]] = Vector.tabulate(height+1, width+1)((x, y) => biomPerlin.noise(x*MapConst.forestFrequency, y*MapConst.forestFrequency))

    val riverGenerator: RiverGenerator = RiverGenerator(height+1, width+1, MapConst.obstaclesFrequency, MapConst.obstaclesThresh)

    val riverPoints: Seq[Vector[Int]] = riverGenerator.getRiverPoints(getRandomVecLeftUp(height, width), getRandomVecRightDown(height, width))

    val map: Seq[Vector[Cell]] = Vector.tabulate(height, width)((y, x) =>
      Cell(squareSide, y, x,
        calculateIsoValue(y, x, MapConst.sparseForestThresh, "forest"),
        getAltitude(terrainPerlin.noise(y*MapConst.terrainFrequency, x*MapConst.terrainFrequency)),
        calculateIsoValue(y, x, MapConst.denseForestThresh, "forest"),
        calculateIsoValue(y, x, 0.5, "river")))

    val GUI = new MapGUI(windowWidth, windowHeight, squareSide)

    addPolygons("forest")
    addPolygons("river")
    addRectangles()
    GUI.drawMap()

    override def toString: String =
      map.map(row => row.mkString(" ")).mkString("\n")

    private def calculateIsoValue(x: Int, y: Int, thresh: Double, terrain: String): Int = {
      var a = 0
      var b = 0
      var c = 0
      var d = 0

      if(terrain == "forest") {
        a = if (forestPoints(x)(y) >= thresh) 1 else 0
        b = if (forestPoints(x)(y + 1) >= thresh) 1 else 0
        c = if (forestPoints(x + 1)(y + 1) >= thresh) 1 else 0
        d = if (forestPoints(x + 1)(y) >= thresh) 1 else 0
      }
      else {
        a = if (riverPoints(x)(y) >= thresh) 1 else 0
        b = if (riverPoints(x)(y + 1) >= thresh) 1 else 0
        c = if (riverPoints(x + 1)(y + 1) >= thresh) 1 else 0
        d = if (riverPoints(x + 1)(y) >= thresh) 1 else 0
      }
      getIsoValue(a, b, c, d)
    }

    private def addPolygons(terrain: String):Unit = {
      map.foreach(row => row.foreach(cell => {
        val polygons = cell.getPolygons(terrain)

        polygons match {
          case (xs: Vector[Int], ys: Vector[Int]) =>
            val xsJava = new util.Vector[Integer]()
            val ysJava = new util.Vector[Integer]()

            xs.foreach(x => xsJava.add(x))
            ys.foreach(y => ysJava.add(y))

            GUI.addPolygon(xsJava, ysJava, terrain)
          case _ =>
        }
      }))
    }

    private def addRectangles():Unit = {
      map.foreach(row => row.foreach(cell => {
        val xPix = cell.y * squareSide
        val yPix = cell.x * squareSide

        GUI.addRectangle(xPix, yPix, squareSide, squareSide, cell.altitude)
      }))
    }

    import utilities.TerrainType._
    override def getTerrainType(position: Vector2D): TerrainType = {
      if(map(position.x)(position.y).isRiver)
        River
      else if(map(position.x)(position.y).isSparseForest)
        SparseForest
      else if(map(position.x)(position.y).isSparseForest)
        DenseForest
      else
        Meadow
    }

    override def canSee(agentPosition: Vector2D, place: Vector2D): Boolean = {
      val visionLine: Array[(Int, Int)] = getVisionLine(agentPosition.x, place.x, agentPosition.y, place.y)

      var visibility: Double = 0
      visionLine.foreach{
        case (x: Int, y: Int) =>
          if (map(x)(y).isDenseForest) visibility -= 2
          else if (map(x)(y).isSparseForest) visibility -= 1
        case _ =>
      }

      if(map(agentPosition.x)(agentPosition.y).isSparseForest && (!map(place.x)(place.y).isSparseForest && !map(place.x)(place.y).isDenseForest)) {
        //agent is in sparse forest and place is outside of forest
        if(visibility >= -5) true else false
      }
      else if(!map(agentPosition.x)(agentPosition.y).isSparseForest && !map(agentPosition.x)(agentPosition.y).isDenseForest && (map(place.x)(place.y).isSparseForest || map(place.x)(place.y).isDenseForest)) {
        //agent is outside of the forest and place is in the forest
        if(visibility >= -1) true else false
      }
      else {
        //every other possibility
        if(visibility >= -10) true else false
      }
    }

    override def elevation(a: Vector2D, b: Vector2D): Double = {
      val tgTheta: Double = (map(a.x)(a.y).altitude - map(b.x)(b.y).altitude)/a.getDistance(b)
      if(tgTheta <= 1 && tgTheta >= -1)
        Math.exp(tgTheta)
      else if(tgTheta > 1)
        Math.E
      else
        1/Math.E
    }

    override def paintMap(xs: Array[Int], ys: Array[Int], health: Array[Double], typeA: Array[Int], morale: Array[Double]): Unit = {
      GUI.paintMap(xs, ys, health, typeA, morale)
    }
  }

  object Map {
    def apply(height: Int, width: Int) = new Map(height, width)
  }

  //get random bit value
  val getRandomBit: () => Int = () =>  {
    //should exist only one
    val rand = scala.util.Random
    if(rand.nextInt(100) > 50) 0 else 1
  }

  //calculate square isovalue from 16 possibilities
  val getIsoValue: (Int, Int, Int, Int) => Int = (a, b, c, d) => a*8 + b*4 + c*2 + d

  //convert noise (in range [-1,1]) to altitude
  val getAltitude: Double => Double = (noise: Double) => {
    (noise+1)/2
  }

  val getRandomVecLeftUp: (Int, Int) => Vector2D = (height: Int, width: Int) => {
    val rand = scala.util.Random
    if(rand.nextInt(2) == 0) {
      //Up border
      Vector2D((0.25*width).asInstanceOf[Int] + rand.nextInt((0.5*width).asInstanceOf[Int]),0)
    }
    else {
      //Left border
      Vector2D(0, (0.25*height).asInstanceOf[Int] + rand.nextInt((0.5*height).asInstanceOf[Int]))
    }
  }

  val getRandomVecRightDown: (Int, Int) => Vector2D = (height: Int, width: Int) => {
    val rand = scala.util.Random
    if(rand.nextInt(2) == 0) {
      //Down border
      Vector2D((0.25*width).asInstanceOf[Int] + rand.nextInt((0.5*width).asInstanceOf[Int]),height)
    }
    else {
      //Right border
      Vector2D(width, (0.25*height).asInstanceOf[Int] + rand.nextInt((0.5*height).asInstanceOf[Int]))
    }
  }

  val getVisionLine: (Int, Int, Int, Int) => Array[(Int, Int)] = (xAgent, xPlace, yAgent, yPlace) => {
    var kx: Int = 0
    var ky: Int = 0
    var dx: Int = 0
    var dy: Int = 0
    var err: Double = .0
    var x1: Int = xAgent
    val x2: Int = xPlace
    var y1: Int = yAgent
    val y2: Int = yPlace
    var line: Array[(Int, Int)] = Array((x1, y1))
    kx = if (x1 <= x2) 1 else -1
    ky = if (y1 <= y2) 1 else -1
    dx = Math.abs(x1 - x2)
    dy = Math.abs(y1 - y2)

    if(dx >= dy) {
      err = dx.toDouble / 2
      for(_ <- 0 until dx - 1) {
        x1 += kx
        err -= dy
        if (err < 0) {
          y1 += ky
          err += dx
        }
        line = line :+ (x1, y1)
      }
    }
    else {
      err = dy.toDouble / 2
      for (_ <- 0 until dy - 1) {
        y1 += ky
        err -= dx
        if (err < 0) {
          x1 += kx
          err += dy
        }
        line = line :+ (x1, y1)
      }
    }
    line = line :+ (x2, y2)
    line
  }
}
