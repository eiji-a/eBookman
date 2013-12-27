
import scala.sys.process._
import scalax.file.Path

class ImageConverter(device: Device, quality: Quality, win: Path, wout: Path, btype: String) {

  val workin = win
  val workout = wout

  val CONVERT = "convert "
  val IDENTIFY = """identify -format %w,%h, """
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
    val imgfile = IMGHDR + "%04d".format(pair._2)
    val dstf = this.workout / Path(imgfile + ext)
    pair._1.copyTo(dstf)
    imgfile
  }

  def shiftv(x: String, y: String) = {
    val sft = (device.w - device.h * x.toInt / y.toInt) / 2
    if (sft < 0) 0 else sft
  }

  def rate(reso: List[String]): List[Int] = {
    reso match {
      case x :: y :: xs => shiftv(x, y) :: rate(xs)
      case default => Nil
    }
  }

  def calcShift(outfiles: String) = {
    val dims = Process(IDENTIFY + outfiles) !!

    val margs = rate(dims.split(",").toList)
    val min = margs.min
    val avg = margs.sum / margs.size
    if (min * 2 > avg) avg else min * 2
  }

  def execute(outfiles: String) = {
    val shift = calcShift(outfiles)
    val sz = (device.w - FRAMEWIDTH) + "x" + (device.h - FRAMEWIDTH)
    //val sz = device.w + "x" + device.h
    val opt =
      " -level " + ImageConverter.LEVEL((device.scr, quality)) +
       " -resize " + sz + " -extent " + sz + "-" + shift +
      " -unsharp 0x1" +
      " -quality " + ImageConverter.QUALITY((device.scr, quality)) +
      " -border 1x1 -bordercolor #000 " +
      " -units PixelsPerInch -density " + device.dpi +
      (if (btype == "text") {""} else {""})
      //(if (btype == "text") {" -format png "} else {""})
    mogrify(opt)(outfiles)
    outfiles
  }

  def createPdf(outimages: List[String], outf: String) = {
    exec("convert " + outimages.mkString(" ") + " " + outf)
  }

  def createEpub(outfiles: List[String], outdir: String) = {
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

  val QUALITY = Map(
    (Screen.EINK16, Quality.NEWCOMIC) -> "30",
    (Screen.EINK16, Quality.NEWTEXT)  -> "60",
    (Screen.EINK16, Quality.OLDCOMIC) -> "30",
    (Screen.EINK16, Quality.OLDTEXT)  -> "60",

    (Screen.LCD256, Quality.NEWCOMIC) -> "60",
    (Screen.LCD256, Quality.NEWTEXT)  -> "90",
    (Screen.LCD256, Quality.OLDCOMIC) -> "60",
    (Screen.LCD256, Quality.OLDTEXT)  -> "90"
  )
}
