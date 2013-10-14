import scala.sys.process._
import scalax.file.Path

class EbookCreator(outdir: String, workdir: String) {

  val WORK1 = Path(workdir + "/ebin", '/')
  val WORK2 = Path(workdir + "/ebout", '/')
  val EXTPDF = ".pdf"
  val EXTIMG = ".jpg"
  val EXTJPG = ".jpg"
  val EXTPNG = ".png"

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

  def convertImages(images: List[Path], conv: ImageConverter) : List[String] = {
    val outfiles = images.zipWithIndex.map(conv.rename(_)(EXTIMG))
    conv.execute(outfiles.map(WORK2.path + "/" + _ + EXTIMG).mkString(" "))
    outfiles
  }

  def convert(zipf: Path, outf: String, device: Device, quality: Quality, opt: Map[String, String]) = {
    cleanDirs
    resetOutdir
    val conv = new ImageConverter(device, quality, WORK1, WORK2, opt("booktype"))
    val outfiles = convertImages(unzipSource(zipf), conv)
    val fname = outf
    //val gene = new Pdf(outdir + "/" + device.name, fname)
    val gene = new Epub(outdir + "/" + device.name, fname)
    gene.init()
    //val ext = if (opt("booktype") == "text") {EXTPNG} else {EXTJPG}
    val ext = if (opt("booktype") == "text") {EXTJPG} else {EXTJPG}
    val subject = opt.getOrElse("subject", opt("zipfile")).replaceAll("_", " ")
    val author  = opt.getOrElse("author", "unknown").replaceAll("_", " ")

    gene.generate(outfiles, WORK2.path, List(ext, subject, author, device.w.toString(), device.h.toString()))
    cleanDirs
    fname
  }
}
