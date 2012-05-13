

object EbmCL extends App {
  val device  = Device.KINDLE3
  val zip     = "~/tmp/sample-book.zip"
  val outf    = "~/tmp/sample-book.pdf"
  val workdir = "~/tmp"

  val creator = new PdfCreator(device, workdir)
  creator.create(zip, outf)

}
