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

  def createPdf(images: List[Path], conv: ImageConverter, pdf: String) = {
    val outfiles = images.zipWithIndex.map(conv.rename(_)(EXTIMG))
    conv.execute(outfiles.mkString(" "))
    conv.combine(outfiles, pdf)
  }

  def convert(zipf: Path, outf: String, device: Device, quality: Quality) = {
    cleanDirs
    resetOutdir
    val conv = new ImageConverter(device, quality, WORK1, WORK2)
    val pdf = outdir + "/" + device.name + "/" + outf + EXTPDF
    createPdf(unzipSource(zipf), conv, pdf)
    cleanDirs
    pdf
  }
}
