import scala.sys.process._
import scalax.file.Path

class PdfCreator(workdir: String) {

  val WORK1 = Path(workdir + "/ebin", '/')
  val WORK2 = Path(workdir + "/ebout", '/')
  val EXTPDF = ".pdf"

  def resetOutdir {
    WORK2.deleteRecursively()
    WORK2.createDirectory()
  }

  def cleanDirs {
    WORK1.deleteRecursively()
    WORK2.deleteRecursively()
  }

  val exec = (cmd: String) => cmd !!
  val unzipSource = (zipf: Path) => {
    exec("unzip -d " + WORK1.path + " " + zipf.path)
    val dirs = WORK1.children().filter(_.isDirectory).toList
    dirs.map(_.children().filter(f => f.name.endsWith(".jpg"))).flatten.sort(_ < _)
  }
  val combineImages = (converted: List[String]) => (outf: String) => {
    exec("convert " + converted.mkString(" ") + " " + outf)
    Path(outf, '/')
  }

  def createPdf(device: Device, outf: String, images: List[Path]): (Device, Path) = {
    resetOutdir
    val conv = new ImageConverter(device, WORK1, WORK2)
    val converted = images.zipWithIndex.map(conv.proc)
    println("done.")
    (device, combineImages(converted)(outf + "." + device.name + EXTPDF))
  }

  def convert(zipf: Path, outf: String, devices: List[Device]): List[(Device, Path)] = {
    val unzipped = unzipSource(zipf)
    val pdfs = devices.map(createPdf(_, outf, unzipped))
    cleanDirs
    pdfs
  }
}
