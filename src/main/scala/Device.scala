

case class Device(name: String, w: Int, h: Int, col: String = "gray") {
  val aspect = h.toDouble / w.toDouble
}


object Device {
  val KINDLE3 = Device("kindle3", 560, 734, "gray")
  val READERT1 = Device("reader_t1", 584, 754, "gray")
  val IPAD = Device("ipad", 768, 1008, "full")

  val presets = Array(KINDLE3, READERT1, IPAD)
}

