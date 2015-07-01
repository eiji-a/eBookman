
object OptionProcessor {

  val HELPMSG =
    """|Usage: Ebookman <options> <inputfile(zip)>
       |  -h          : help. print this document
       |  -c          : comic book
       |  -t          : text book
       |  -r          : direction right
       |  -l          : direction left
       |  -n <type>   : newness (type=new/old/veryold)
       |  -d <device> : device name (ex. kindle3, nexus7)
       |  -i <dir>    : directory for input file
       |  -o <dir>    : directory for output file
       |  -f <file>   : output file name
       |  -s <title>  : subject/title of book
       |  -a <author> : author of book
       |""".stripMargin

  val DEFAULT = Map(
    "author" -> "unknown",
    "booktype" -> "comic",
    "device" -> "kindle_pw",
    "direction" -> Device.RIGHT,
    "indir" -> ".",
    "newness" -> "new",
    "outdir" -> ".",
    "workdir" -> "/tmp",
    "zipfile" -> "bookfile"
  )

  def analyze(args: List[String]): Map[String, String] = {
    args match {
      case x :: xs if x == "-h"       => analyze(xs) + ("help" -> "yes")
      case x :: xs if x == "-c"       => analyze(xs) + ("booktype" -> "comic")
      case x :: xs if x == "-t"       => analyze(xs) + ("booktype" -> "text")
      case x :: xs if x == "-r"       => analyze(xs) + ("direction" -> Device.RIGHT)
      case x :: xs if x == "-l"       => analyze(xs) + ("direction" -> Device.LEFT)
      case x :: x2 :: xs if x == "-n" => analyze(xs) + ("newness" -> newness(x2))
      case x :: x2 :: xs if x == "-d" => analyze(xs) + ("device" -> x2)
      case x :: x2 :: xs if x == "-i" => analyze(xs) + ("indir" -> x2)
      case x :: x2 :: xs if x == "-o" => analyze(xs) + ("outdir" -> x2)
      case x :: x2 :: xs if x == "-f" => analyze(xs) + ("outfile" -> x2)
      case x :: x2 :: xs if x == "-s" => analyze(xs) + ("subject" -> x2)
      case x :: x2 :: xs if x == "-a" => analyze(xs) + ("author" -> x2)
      case x :: x2 :: Nil             => DEFAULT + ("zipfile" -> x)
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
