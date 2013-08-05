import java.lang.System
import scalax.file.Path


object Ebookman {
  def main(args: Array[String]) {
    val ebroot = "/Users/eiji/work/ebook"
    val scaned = ebroot + "/scaned/comic/"
    val archive = ebroot + "/archive/comic/"

    //val zipper = new Zipper
    //val book = "MASTER KEATON 01"
    //zipper.exec(scaned + book, archive + book + ".zip", true)
    
    val opts = (new OptionProcessor).analyze(args.toList)
    if (opts.contains("help")) {
      printHelp
      System.exit(1)
    }

    val device  = Device.LIST(opts("device"))
    val quality = Quality.LIST(opts("newness") + opts("booktype"))
    val zip     = Path(opts("indir") + "/" + opts("zipfile") + ".zip", '/')

    println("DEVICE:" + device.name)
    println("QUALITY:" + quality.booktype)
    println("SOURCE:" + zip.path)

    val outf    = opts.getOrElse("outfile", opts("zipfile")) + "." + System.currentTimeMillis

    val creator = new PdfCreator(opts("outdir"), opts("workdir"))
    val outs = creator.convert(zip, outf, device, quality)
    println("OUTF:" + outs.path)
  }

  def printHelp {
    printf("""|Usage: Ebookman <options> <inputfile(zip)>
              |  -h : help. print this document
              |  -c : comic book
              |  -t : text book
              |  -n : newness (new/old/veryold)
              |  -d : device name (ex. kindle3, nexus7)
              |  -i : directory for input file
              |  -o : directory for output file
              |  -f : output file name
              |""".stripMargin)
              
  }
}
