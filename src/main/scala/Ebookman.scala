import java.io.File
import java.lang.System
import scalax.file.Path


object Ebookman {
  def main(args: Array[String]) {
    val ebroot = "/Users/eiji/work/ebook"
    val scaned = ebroot + "/scaned/comic/"
    val archive = ebroot + "/archive/comic/"

    val zipper = new Zipper
    //println("FILE: " + args(0))
    val book = "MASTER KEATON 01"
    zipper.exec(scaned + book, archive + book + ".zip", true)


    val devices = List(Device.NEXUS7, Device.KINDLE3)
    val workdir = "/Users/eiji/tmp"
    val zip     = Path(workdir + "/sample-book.zip", '/')
    val outf    = workdir + "/Ebookman" + System.currentTimeMillis

    val creator = new PdfCreator(workdir)
    val outs = creator.convert(zip, outf, devices)
    outs.foreach(s => println("OUTF:" + s._2.path))
  }
}

