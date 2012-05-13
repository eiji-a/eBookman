


class PdfCreator(device: Device, workdir: String) {

  def unzip(zip: String) : List[String] = {
    List("/tmp/samp1.jpg", "/tmp/samp2.jpg", "/tmp/samp3.jpg",
    "/tmp/samp4.jpg", "/tmp/samp5.jpg")
  }

  def create(zipf: String, outf: String): Unit = {
    println("DEVICE: " + device.name)
    println("OUTF: " + outf)
    val conv = new ImageConverter(workdir)
    val srcimgs = unzip(zipf)
    val srcpair = (srcimgs zip (1 to srcimgs.size toList)).map(conv.exec)
  }
}
