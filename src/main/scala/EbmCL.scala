

object EbmCL extends App {
  val device  = Device.IPAD
  val workdir = "/Users/eiji/tmp"
  val zip     = workdir + "/sample-book.zip"
  val outf    = workdir + "/sample-book.pdf"

  val creator = new PdfCreator(device, workdir)
  creator.create(zip, outf)

}
