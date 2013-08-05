
import scala.sys.process._
import scalax.file.Path

class ImageConverter(device: Device, quality: Quality, win: Path, wout: Path) {

  val workin = win
  val workout = wout

  val CONVERT = "convert "
  val MOGRIFY = "mogrify "
  val IMGHDR = "img-"
  val EXTGIF = ".gif"
  val EXTPNG = ".png"
  val EXTJPG = ".jpg"
  val FRAMEWIDTH = 2

  val exec = (cmd: String) => cmd !!
  val tempPath = (file: String) => (ext: String) => workout / Path(file + ext)
  val convert = (opt: String) => (src: Path) => (dst: Path) => {
    exec(CONVERT + opt + " " + src.path + " " + dst.path)
    dst
  }
  val mogrify = (opt: String) => (src: String) => {
    exec(MOGRIFY + opt + " " + src)
    opt
  }
  val fsize = (file: Path) => { 
    file.size match {
      case Some(size) => size
      case _ => 0
    }
  }
  val rename = (pair: (Path, Int)) => (ext: String) => {
    val dstf = this.workout / Path(IMGHDR + "%03d".format(pair._2) + ext)
    pair._1.copyTo(dstf).path
  }

  def execute(outfiles: String) = {
    val sz = (device.w - FRAMEWIDTH) + "x" + (device.h - FRAMEWIDTH)
    val opt =
      " -level " + ImageConverter.LEVEL((device.scr, quality)) +
       " -resize " + sz + " -extent " + sz +
      " -border 1x1 -bordercolor #000 " +
      " -unsharp 0x1" +
      " -quality " + quality.rate +
      " -units PixelsPerInch -density " + device.dpi
    mogrify(opt)(outfiles)
    outfiles
  }

}

object ImageConverter {
  val LEVEL = Map(
    (Screen.EINK16, Quality.NEWCOMIC) -> "10%,95%",
    (Screen.EINK16, Quality.NEWTEXT)  -> "0%,100%,0.4",
    (Screen.EINK16, Quality.OLDCOMIC) -> "10%,85%",
    (Screen.EINK16, Quality.OLDTEXT)  -> "0%,90%,0.4",

    (Screen.LCD256, Quality.NEWCOMIC) -> "0%,100%",
    (Screen.LCD256, Quality.NEWTEXT)  -> "0%,100%",
    (Screen.LCD256, Quality.OLDCOMIC) -> "0%,90%",
    (Screen.LCD256, Quality.OLDTEXT)  -> "0%,90%"
  )
}
