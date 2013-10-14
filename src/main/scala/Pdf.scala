//
// Pdf
//

import scala.sys.process._

class Pdf(outdir: String, outname: String) extends Generator(outdir, outname) {

  val EXTPDF = ".pdf"

  val exec = (cmd: String) => cmd !!

  def init() = {}


  def generate(imagefiles: List[String], srcdir: String, opt: List[String]) = {
    // opt0 = extent of imagefile
    val outimages = imagefiles.map(srcdir + "/" + _ + opt(0)).mkString(" ")
    val outf = outdir + "/" + outname + EXTPDF
    exec("convert " + outimages + " " + outf)
  }
}
