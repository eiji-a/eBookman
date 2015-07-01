

case class Screen(kind: String, tone: Int, color: Boolean) { 

}

object Screen {
  val COLOR = true
  val GRAY  = !COLOR

  // E Ink 16 tone
  val EINK16 = Screen("EInk",  16, GRAY)
  val EINK2  = Screen("EInk2", 16, GRAY)
  val LCD256 = Screen("LCD",  256, COLOR)

}
