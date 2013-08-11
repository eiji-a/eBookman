
object OptionProcessor {

  val DEFAULT = Map(
    "newness" -> "new",
    "booktype" -> "comic",
    "device" -> "kindle_pw",
    "workdir" -> "/tmp",
    "indir" -> ".",
    "outdir" -> ".",
    "zipfile" -> "bookfile"
  )

  def analyze(args: List[String]): Map[String, String] = {
    args match {
      case x :: xs if x == "-h"       => analyze(xs) + ("help" -> "yes")
      case x :: xs if x == "-c"       => analyze(xs) + ("booktype" -> "comic")
      case x :: xs if x == "-t"       => analyze(xs) + ("booktype" -> "text")
      case x :: x2 :: xs if x == "-n" => analyze(xs) + ("newness" -> newness(x2))
      case x :: x2 :: xs if x == "-d" => analyze(xs) + ("device" -> x2)
      case x :: x2 :: xs if x == "-i" => analyze(xs) + ("indir" -> x2)
      case x :: x2 :: xs if x == "-o" => analyze(xs) + ("outdir" -> x2)
      case x :: x2 :: xs if x == "-f" => analyze(xs) + ("outfile" -> x2)
      case x :: Nil                   => DEFAULT + ("zipfile" -> x)
      case Nil                        => DEFAULT
    }
  }

  def newness(opt: String): String = {
    opt match {
      case "old" => opt
      case "veryold" => opt
      case _ => "new"
    }
  }

}
