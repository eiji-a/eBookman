import java.io.File


class PdfCreator(device: Device, dirin: String, dirout: String,
  workdir: String = "/tmp") {

  def unzip(zip: String) : Array[java.io.File] = {
    val dir = "/Users/eiji/work/"
    List(dir + "samp1.jpg", dir + "samp2.jpg", dir + "samp3.jpg",
      dir + "samp4.jpg", dir + "samp5.jpg", dir + "samp6.jpg",
      dir + "samp7.jpg", dir + "samp8.jpg", dir + "samp9.jpg",
      dir + "samp10.jpg", dir + "samp11.jpg", dir + "samp12.jpg",
      dir + "samp13.jpg", dir + "samp14.jpg", dir + "samp15.jpg",
      dir + "samp16.jpg")
    new File(dirin).listFiles
  }

  def create(zipf: String, outf: String): Unit = {
    println("DEVICE: " + device.name)
    println("OUTF: " + outf)
    val conv = new ImageConverter(device, dirin, dirout, workdir)
    val srcimgs = unzip(zipf)
    val srcpair = (srcimgs zip (1 to srcimgs.size toList)).map(conv.proc)
    srcpair.foreach(println)
    conv.clean
  }
}
