


class PdfCreator(device: Device, workdir: String) {

  def unzip(zip: String) : List[String] = {
    val dir = "/Users/eiji/work/"
    List(dir + "samp1.jpg", dir + "samp2.jpg", dir + "samp3.jpg",
      dir + "samp4.jpg", dir + "samp5.jpg")
  }

  def create(zipf: String, outf: String): Unit = {
    println("DEVICE: " + device.name)
    println("OUTF: " + outf)
    val conv = new ImageConverter(workdir, device)
    val srcimgs = unzip(zipf)
    val srcpair = (srcimgs zip (1 to srcimgs.size toList)).map(conv.exec)
  }
}
