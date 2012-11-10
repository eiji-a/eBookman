
object Ebookman {
  def main(args: Array[String]) {
    val ebroot = "/Users/eiji/work/ebook"
    val scaned = ebroot + "/scaned/comic/"
    val archive = ebroot + "/archive/comic/"

    val zipper = new Zipper
    //println("FILE: " + args(0))
    val book = "MASTER KEATON 01"
    zipper.exec(scaned + book, archive + book + ".zip", true)


    val device  = Device.NEXUS7
    val input   = "/Users/eiji/tmp/ebinput"
    val output  = "/Users/eiji/tmp/eboutput"
    val workdir = "/Users/eiji/tmp"
    val zip     = workdir + "/sample-book.zip"
    val outf    = workdir + "/sample-book.pdf"

    val creator = new PdfCreator(device, input, output, workdir)
    creator.create(zip, outf)

  }
}

