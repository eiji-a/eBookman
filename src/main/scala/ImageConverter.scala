import scala.sys.process._
import java.io.File

class ImageConverter(device: Device, workin: String, workout: String) {

  //val SIPS = "sips "
  //val SIPSF = SIPS + "-s format "
  val CONVERT = "convert "
  //val SIZECMD = "sips -g pixelWidth -g pixelHeight "

  val IMGHDR = "img-"
  val EXTGIF = ".gif"
  val EXTPNG = ".png"
  val EXTJPG = ".jpg"
  val TMP1 = workout + "/tmp01"
  val TMP2 = workout + "/tmp02"

  val FRAMEWIDTH = 2

  def exec(cmd: String): String = {
    Process(cmd) !!;
  }

  /*
  def size(file: String): (Int, Int) = {
    val sz = exec(SIZECMD + file)
    val pat = """\S+\s+pixelWidth:\s+(\d+)\s+pixelHeight:\s+(\d+)\s+""".r
    val pat(x, y) = sz
    device.scale(x.toInt, y.toInt, FRAMEWIDTH)
  }
  */

  def clean: Unit = {
    // workdir の tmpファイルを消す
  }

  def convert(opt: String, src: String, dst: String) = {
    exec(CONVERT + opt + " " + src + " " + dst)
    dst
  }

  def proc(pair: (File, Int)): String = {
    // DEVICE DEPENDENCY
    val srcname = pair._1.getAbsolutePath
    convert(if (device.cont == null) "" else " -normalize ", srcname, TMP1 + EXTJPG)

    // RESIZE AND COLORING AND SELECT FORMAT
    val sz2 = (device.w - FRAMEWIDTH) + "x" + (device.h - FRAMEWIDTH)
    val opt2 = " -resize " + sz2 + " -extent " + sz2 + " -border 1x1 -unsharp 0" +
    (if (device.col == Device.GRAYSCALE) " -type Grayscale" else "") +
    (if (device.cont == null) "" else " -level " + device.cont)
    val fgif = new File(convert(opt2, TMP1 + EXTJPG, TMP2 + EXTGIF))
    val fjpg = new File(convert(opt2, TMP1 + EXTJPG, TMP2 + EXTJPG))
    var ext = EXTJPG
    if (fgif.length() < fjpg.length()) {
      ext = EXTPNG
      convert("", TMP2 + EXTGIF, TMP2 + EXTPNG)
    }

    // DENSITY
    val dstf = workout + "/" + IMGHDR + "%03d".format(pair._2) + ext
    val opt3 = " -units PixelsPerInch -density " + device.dpi
    convert(opt3, TMP2 + ext, dstf)

    println(pair._1 + " -> " + dstf)
    dstf
  }

}
