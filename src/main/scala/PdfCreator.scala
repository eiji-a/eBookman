import scala.sys.process._
import scalax.file.Path

class PdfCreator(outdir: String, workdir: String) {

  val WORK1 = Path(workdir + "/ebin", '/')
  val WORK2 = Path(workdir + "/ebout", '/')
  val EXTPDF = ".pdf"
  val EXTIMG = ".jpg"

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
    dirs.map(_.children().filter(f => f.name.endsWith(EXTIMG))).flatten.sort(_ < _)
  }
  val combineImages = (outfiles: List[String]) => (outf: String) => {
    exec("convert " + outfiles.mkString(" ") + " " + outf)
    Path(outf, '/')
  }

  def createPdf(device: Device, quality: Quality, outf: String, images: List[Path]): Path = {
    resetOutdir
    val conv = new ImageConverter(device, quality, WORK1, WORK2)
    val outfiles = images.zipWithIndex.map(conv.rename(_)(EXTIMG))
    println("rename: done.")
    conv.execute(outfiles.mkString(" "))
    println("convert: done.")
    val fname = outdir + "/" + device.name + "/" + outf + EXTPDF
    combineImages(outfiles)(fname)
  }

  def convert(zipf: Path, outf: String, device: Device, quality: Quality): Path = {
    cleanDirs
    val unzipped = unzipSource(zipf)
    val pdf = createPdf(device, quality, outf, unzipped)
    cleanDirs
    println("all: done.")
    pdf
  }
}
