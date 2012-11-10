import scala.sys.process._
import java.io.File

class Zipper {
  val firstp0 = "obj.jpg"
  val firstp1 = "obj_000.jpg"

  def exec(scaned: String, archive: String, remain: Boolean = false) = {
    val first = new File(scaned + "/" + firstp0)
    if (first.exists == true) {
      first.renameTo(new File(scaned + "/" + firstp1))
    }
    val scn = new File(scaned)
    scn.renameTo(new File("/tmp/tmp1"))
    println("zip -rj /tmp/tmp1.zip /tmp/tmp1")
    val tmp = new File("/tmp/tmp1.zip")
    tmp.renameTo(new File(archive))

    // delete original
    if (!remain) {
      Process("rm -rf " + scaned)
    }
  }
}
