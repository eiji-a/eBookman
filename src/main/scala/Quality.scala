

case class Quality(booktype: String, newness: Int, rate: Int, format: String) { 

}

object Quality {
  val NEW = 0
  val OLD = 10
  val VERYOLD = 20

  val FMTJPG = "jpg"
  val FMTPNG = "png"

  val NEWCOMIC = Quality("newcomic", NEW, 60, FMTJPG)
  val NEWTEXT  = Quality("newtext",  NEW, 60, FMTPNG)
  val OLDCOMIC = Quality("oldcomic", OLD, 60, FMTJPG)
  val OLDTEXT  = Quality("oldtext",  OLD, 60, FMTPNG)

  val LIST = Map(
    NEWCOMIC.booktype -> NEWCOMIC,
    NEWTEXT.booktype  -> NEWTEXT,
    OLDCOMIC.booktype -> OLDCOMIC,
    OLDTEXT.booktype  -> OLDTEXT
  )

}
