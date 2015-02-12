//
// Device
//

case class Device(name: String, w: Int, h: Int, dpi: Int, scr: Screen, dir: String) {

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

  // direction
  val LEFT  = "ltr"
  val RIGHT = "rtl"
  val BOTH  = ""

  // Kindle3
  //val KINDLE3   = Device("kindle3",   560,  735, 167, Screen.EINK16)
  val KINDLE3   = Device("kindle3",   600,  800, 167, Screen.EINK16, LEFT)
  //val KINDLE_PW = Device("kindle_pw", 658,  905, 212, Screen.EINK16)
  val KINDLE_PW = Device("kindle_pw", 758, 1024, 212, Screen.EINK16, BOTH)
  val KINDLE7   = Device("kindle7",   600,  800, 167, Screen.EINK16, LEFT)
  val NEXUS7    = Device("nexus7",    800, 1205, 216, Screen.LCD256, BOTH)
  val IPADMINI  = Device("ipad_mini", 768, 1008, 163, Screen.LCD256, BOTH)
  val READERT1  = Device("reader_t1", 584,  754, 167, Screen.EINK16, LEFT)

  val presets = Array(KINDLE3, KINDLE_PW, NEXUS7, IPADMINI, READERT1)
  val LIST = Map(
    KINDLE3.name   -> KINDLE3,
    KINDLE7.name   -> KINDLE7,
    KINDLE_PW.name -> KINDLE_PW,
    NEXUS7.name    -> NEXUS7,
    IPADMINI.name  -> IPADMINI,
    READERT1.name  -> READERT1
  )

}

