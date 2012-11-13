import scala.sys.process._
import java.io.File


class PdfCreator(workdir: String = "/tmp") {

  val WORK1 = workdir + "/ebin"
  val WORK2 = workdir + "/ebout"
  val EXTPDF = ".pdf"

  def resetOutdir {
    Process("rm -rf " + WORK2) !!;
    new File(WORK2).mkdir
  }

  def cleanDirs {
    Process("rm -rf " + WORK1 + " " + WORK2) !!;
  }

  def unzipSource(zipf: File): List[File] = {
    Process("unzip -d " + WORK1 + " " + zipf.getAbsolutePath) !!;
    val dirs = new File(WORK1).listFiles.toList.filter(_.isDirectory)
    dirs.map(_.listFiles.toList.filter(_.getName.endsWith(".jpg"))).flatten
  }

  def combineImages(converted: List[String], outf: String): File = {
    Process("convert " + converted.mkString(" ") + " " + outf) !!;
    println("OUTF:" + outf)
    new File(outf)
  }

  def createPdf(device: Device, outf: String, images: List[File]): (Device, File) = {
    resetOutdir
    val conv = new ImageConverter(device, WORK1, WORK2)
    val converted = (images zip (1 to images.size toList)).map(conv.proc)
    (device, combineImages(converted, outf + "." + device.name + EXTPDF))
  }

  def convert(zipf: File, outf: String, devices: List[Device]): List[(Device, File)] = {
    val unzipped = unzipSource(zipf)
    val pdfs = devices.map(createPdf(_, outf, unzipped))
    cleanDirs
    pdfs
  }
}
