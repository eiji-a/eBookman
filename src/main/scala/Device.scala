

case class Device(name: String, w: Int, h: Int, dpi: Int,
  cont: String, col: String = Device.GRAYSCALE) {

  val aspect = h.toDouble / w.toDouble

  def scale(x:Int, y:Int, frame: Int = 0): (Int, Int) = {
    val mag = y.toDouble / x.toDouble
    mag match {
      case mag if aspect > mag => (w - frame, y)
      case _ => (x, h - frame)
    }
  }
}


object Device {
  val FULLCOLOR = "full"
  val GRAYSCALE = "gray"

  val SHARP   = true
  val UNSHARP = !SHARP

  val KINDLE3  = Device("kindle3", 560, 734, 167, "15%,95%", GRAYSCALE)
  val READERT1 = Device("reader_t1", 584, 754, 167, "15%,95%", GRAYSCALE)
  val IPAD     = Device("ipad", 768, 1008, 163, null, FULLCOLOR)
  val NEXUS7   = Device("nexus7", 800, 1172, 216, null, FULLCOLOR)

  val presets = Array(KINDLE3, READERT1, IPAD, NEXUS7)
}

