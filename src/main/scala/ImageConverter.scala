import scala.sys.process._
import java.io.File
import scalax.file.Path

class ImageConverter(device: Device, win: Path, wout: Path) {

  val workin = win
  val workout = wout

  //val SIPS = "sips "
  //val SIPSF = SIPS + "-s format "
  val CONVERT = "convert "
  //val SIZECMD = "sips -g pixelWidth -g pixelHeight "

  val IMGHDR = "img-"
  val EXTGIF = ".gif"
  val EXTPNG = ".png"
  val EXTJPG = ".jpg"
  val TMP1 = "tmp01"
  val TMP2 = "tmp02"

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

  def tempPath(file: String)(ext: String) = workout / Path(file + ext)
  def temp1 = tempPath(TMP1)_
  def temp2 = tempPath(TMP2)_

  def convert(opt: String, src: Path, dst: Path) = {
    exec(CONVERT + opt + " " + src.path + " " + dst.path)
    dst
  }

  def fsize(file: Path) = {
    file.size match {
      case Some(size) => size
      case _ => 0
    }
  }

  def proc(pair: (Path, Int)) = {
    // DEVICE DEPENDENCY
    convert(if (device.cont == null) "" else " -normalize ", pair._1, temp1(EXTJPG))

    // RESIZE AND COLORING AND SELECT FORMAT
    val sz2 = (device.w - FRAMEWIDTH) + "x" + (device.h - FRAMEWIDTH)
    val opt2 = " -resize " + sz2 + " -extent " + sz2 + " -border 1x1 -unsharp 0" +
    (if (device.col == Device.GRAYSCALE) " -type Grayscale" else "") +
    (if (device.cont == null) "" else " -level " + device.cont)
    val fgif = convert(opt2, temp1(EXTJPG), temp2(EXTGIF))
    val fjpg = convert(opt2, temp1(EXTJPG), temp2(EXTJPG))
    val ext = if (fsize(fgif) < fsize(fjpg)) {
      convert("", temp2(EXTGIF), temp2(EXTPNG))
      EXTPNG
    } else {
      EXTJPG
    }

    // DENSITY
    val opt3 = " -units PixelsPerInch -density " + device.dpi
    val dstf = this.workout / Path(IMGHDR + "%03d".format(pair._2) + ext)
    convert(opt3, temp2(ext), dstf)

    print(".")
    dstf.path
  }

}
