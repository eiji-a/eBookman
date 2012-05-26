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
    val pat = """\S+\s+pixelWidth:\s+(\d+)\s+pixelHeight:\s+(\d+)\s+""".r
    val pat(x, y) = sz
    //println("X=" + x + ", Y=" + y)
    device.scale(x.toInt, y.toInt)
  }

  def exec(pair: (String, Int)) = {
    // FORMAT (JPG->BMP)
    var cmd = SIPSF + "bmp " + pair._1 + " --out " + TMP1 // + " > /dev/null"
    //println(cmd)
    Process(cmd) !!;

    // RESIZE
    val sz = size(pair._1)
    cmd = CONVERT + "-geometry " + sz._1 + "x" + sz._2 + "! " + TMP1 + " " + TMP2
    //println(cmd)
    Process(cmd) !!;

    // SPACE FILLING
    cmd = CONVERT + "-gravity east -splice " + (device.w - 2 - sz._1) + "x0 -background white " + TMP2 + " " + TMP1
    //println(cmd)
    Process(cmd) !!;

    // BORDERING
    cmd = CONVERT + "-border 1x1 " + TMP1 + " " + TMP2
    //println(cmd)
    Process(cmd) !!;

    // COLORING
    val col = if (device.col == Device.FULLCOLOR) "" else "-type GrayScale"
    cmd = CONVERT + "+dither " + col + " " + TMP2 + " " + TMP1
    //println(cmd)
    Process(cmd) !!;

    // FORMAT (BMP->GIF)
    val dstf = "/" + IMGHDR + pair._2 + IMGEXT
    cmd = SIPSF + "gif " + TMP1 + " --out " + workdir + dstf
    //println(cmd)
    Process(cmd) !!;

    println(pair._1 + " -> " + dstf + " (" + sz._1 + "," + sz._2 + ")")

  }
}
