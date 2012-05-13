

class ImageConverter(workdir: String) {
  val SIPS = "sips -s format "
  val CONVERT = "convert "

  val IMGHDR = "img-"
  val IMGEXT = ".gif"
  val TMP1 = workdir + "/tmp01.bmp"
  val TMP2 = workdir + "/tmp02.bmp"

  def exec(pair: (String, Int)) = {
    val dstf = IMGHDR + pair._2 + IMGEXT
    printf("SRC: %s -> DST: %s\n", pair._1, dstf)
    println(SIPS + "bmp '" + pair._1 + "' --out " + TMP1 + " > /dev/null")
    val xs = 100
    val ys = 200
    println(CONVERT + "-geometry " + xs + "x" + ys + "! " + TMP1 + " " + TMP2)
  }
}
