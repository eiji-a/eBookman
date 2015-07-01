//
// Ebookman
//

import java.lang.System
import scalax.file.Path

object Ebookman {

  /*
  val ebroot = "/Users/eiji/work/ebook"
  val scaned = ebroot + "/scaned/comic/"
  val archive = ebroot + "/archive/comic/"
  */

  def main(args: Array[String]) {

    //val zipper = new Zipper
    //val book = "MASTER KEATON 01"
    //zipper.exec(scaned + book, archive + book + ".zip", true)
    
    val opts = OptionProcessor.analyze(args.toList)
    if (opts.contains("help")) {
      println(OptionProcessor.HELPMSG)
      System.exit(1)
    }

    val device  = Device.LIST(opts("device"))
    val quality = Quality.LIST(opts("newness") + opts("booktype"))
    val zip     = Path(opts("indir") + "/" + opts("zipfile") + ".zip", '/')
    val outf    = opts.getOrElse("outfile", opts("zipfile")) + "." +
                  System.currentTimeMillis

    println("DEVICE:" + device.name)
    println("QUALITY:" + quality.booktype)
    println("SOURCE:" + zip.path)

    val creator = new EbookCreator(opts("outdir"), opts("workdir"))
    val outs = creator.convert(zip, outf, device, quality, opts)
    println("OUTF:" + outs + " finished successfully")
  }

}
