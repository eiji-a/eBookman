//
// Ebookman
//

import java.lang.System
import scalax.file.Path

object Ebookman {

  val help =
    """|Usage: Ebookman <options> <inputfile(zip)>
       |  -h          : help. print this document
       |  -c          : comic book
       |  -t          : text book
       |  -n <type>   : newness (type=new/old/veryold)
       |  -d <device> : device name (ex. kindle3, nexus7)
       |  -i <dir>    : directory for input file
       |  -o <dir>    : directory for output file
       |  -f <file>   : output file name
       |""".stripMargin

  val ebroot = "/Users/eiji/work/ebook"
  val scaned = ebroot + "/scaned/comic/"
  val archive = ebroot + "/archive/comic/"

  def main(args: Array[String]) {

    //val zipper = new Zipper
    //val book = "MASTER KEATON 01"
    //zipper.exec(scaned + book, archive + book + ".zip", true)
    
    val opts = OptionProcessor.analyze(args.toList)
    if (opts.contains("help")) {
      println(help)
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

    val creator = new PdfCreator(opts("outdir"), opts("workdir"))
    val outs = creator.convert(zip, outf, device, quality)
    println("OUTF:" + outs + " finished successfully")
  }

}
