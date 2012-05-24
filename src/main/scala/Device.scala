

case class Device(name: String, w: Int, h: Int, col: String = "gray") {
  val aspect = h.toDouble / w.toDouble
  def scale(x:Int, y:Int): (Int, Int) = {
    val mag = y.toDouble / x.toDouble
    mag match {
      case mag if aspect > mag => (w, (y * (w / x.toDouble)).toInt)
      case _ => ((x * (h / y.toDouble)).toInt, h)
    }
  }
}


object Device {
  val FULLCOLOR = "full"
  val GRAYSCALE = "gray"

  val KINDLE3 = Device("kindle3", 560, 734, GRAYSCALE)
  val READERT1 = Device("reader_t1", 584, 754, GRAYSCALE)
  val IPAD = Device("ipad", 768, 1008, FULLCOLOR)

  val presets = Array(KINDLE3, READERT1, IPAD)
}

