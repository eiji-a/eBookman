import scala.sys.process._

class ImageConverter(workdir: String, device: Device) {
  val SIPS = "sips "
  val SIPSF = SIPS + "-s format "
  val CONVERT = "convert "
  val SIZECMD = SIPS + "-g pixelWidth -g pixelHeight "

  val IMGHDR = "img-"
  val IMGEXT = ".gif"
  val TMP1 = workdir + "/tmp01.bmp"
  val TMP2 = workdir + "/tmp02.bmp"


  def size(file: String): (Int, Int) = {
    val sz = Process(SIZECMD + file) !!;
    val pat = """pixelWidth:\s+(\d+)\s+pixelHeight:\s+(\d+)""".r
    val pat(x, y) = pat.findFirstIn(sz).get
    device.scale(x.toInt, y.toInt)
  }

  def exec(pair: (String, Int)) = {
    println(SIPSF + "bmp '" + pair._1 + "' --out " + TMP1 + " > /dev/null")
    val sz = size(pair._1)
    println(CONVERT + "-geometry " + sz._1 + "x" + sz._2 + "! " + TMP1 + " " + TMP2)
    println(CONVERT + "-gravity east -splice " + (device.w - 2 - sz._1) + "x0 - background white " + TMP2 + " " + TMP1 + " > /dev/null")
    println(CONVERT + "-border 1x1 " + TMP1 + " " + TMP2)
    val col = if (device.col == Device.FULLCOLOR) "" else "-type GrayScale"
    println(CONVERT + "+dither " + col + " " + TMP2 + " " + TMP1 + " > /dev/null")
    val dstf = "/" + IMGHDR + pair._2 + IMGEXT
    println(SIPSF + "gif " + TMP1 + " --out " + workdir + dstf + " > /dev/null")
    println(pair._1 + " -> " + dstf + " (" + sz._1 + "," + sz._2 + ")")

  }
}
