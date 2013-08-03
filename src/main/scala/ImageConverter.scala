
import scala.sys.process._
import scalax.file.Path

class ImageConverter(device: Device, win: Path, wout: Path) {

  val workin = win
  val workout = wout

  val CONVERT = "convert "
  val IMGHDR = "img-"
  val EXTGIF = ".gif"
  val EXTPNG = ".png"
  val EXTJPG = ".jpg"
  val TMP1 = "tmp01"
  val TMP2 = "tmp02"
  val FRAMEWIDTH = 2

  val exec = (cmd: String) => cmd !!
  val tempPath = (file: String) => (ext: String) => workout / Path(file + ext)
  val temp1 = tempPath(TMP1)
  val temp2 = tempPath(TMP2)
  val convert = (opt: String) => (src: Path) => (dst: Path) => {
    exec(CONVERT + opt + " " + src.path + " " + dst.path)
    dst
  }
  val fsize = (file: Path) => { 
    file.size match {
      case Some(size) => size
      case _ => 0
    }
  }

  def proc(pair: (Path, Int)) = {
    // DEVICE DEPENDENCY
    //convert(if (device.cont == null) "" else " -contrast-stretch 3% ")(pair._1)(temp1(EXTJPG))
    //convert(if (device.cont == null) "" else " -normalize ")(pair._1)(temp1(EXTJPG))
    convert(if (device.cont == null) "" else "")(pair._1)(temp1(EXTJPG))

    // RESIZE AND COLORING AND SELECT FORMAT
    val sz2 = (device.w - FRAMEWIDTH) + "x" + (device.h - FRAMEWIDTH)
    val opt2 = " -resize " + sz2 + " -extent " + sz2 + " -border 1x1 -unsharp 0x1" +
    //val opt2 = " -resize " + sz2 + " -extent " + sz2 + " -border 1x1 " +
    //(if (device.col == Device.GRAYSCALE) " -type Grayscale" else "") +
    (if (device.cont == null) "" else " -level " + device.cont)
    val sz_gif = fsize(convert(opt2)(temp1(EXTJPG))(temp2(EXTGIF)))
    val sz_jpg = fsize(convert(opt2)(temp1(EXTJPG))(temp2(EXTJPG)))
    val ext = if (sz_gif < sz_jpg) {
      convert("")(temp2(EXTGIF))(temp2(EXTPNG))
      EXTPNG
    } else {
      EXTJPG
    }

    // DENSITY
    val opt3 = " -units PixelsPerInch -density " + device.dpi
    val dstf = this.workout / Path(IMGHDR + "%03d".format(pair._2) + ext)
    convert(opt3)(temp2(ext))(dstf)

    print(".")
    dstf.path
  }

}
