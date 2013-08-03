

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

  // Kindle3
  val K3TN   = Device("kindle3", 560, 735, 167, "0%,100%,0.3", GRAYSCALE)
  val K3CN   = Device("kindle3", 560, 735, 167, "10%,95%", GRAYSCALE)
  val K3TO   = Device("kindle3", 560, 735, 167, "0%,90%,0.3", GRAYSCALE)
  val K3CO   = Device("kindle3", 560, 735, 167, "10%,85%", GRAYSCALE)
  // Kindle Paperwhite
  val KWTN   = Device("kindle_pw", 658, 905, 212, "0%,100%,0.3", GRAYSCALE)
  val KWCN   = Device("kindle_pw", 658, 905, 212, "15%,95%", GRAYSCALE)
  val KWTO   = Device("kindle_pw", 658, 905, 212, "0%,90%,0.3", GRAYSCALE)
  val KWCO   = Device("kindle_pw", 658, 905, 212, "15%,85%", GRAYSCALE)
  // Nexus7
  val N7N    = Device("nexus7", 800, 1205, 216, "0%,100%", FULLCOLOR)
  val N7O    = Device("nexus7", 800, 1205, 216, "0%,90%", FULLCOLOR)
  // Others
  val READERT1  = Device("reader_t1", 584, 754, 167, "15%,95%", GRAYSCALE)
  val IPAD      = Device("ipad", 768, 1008, 163, null, FULLCOLOR)

  val presets = Array(KWTN, KWCN, KWTO, KWCO, N7N, N7O, READERT1, IPAD)
}

