//
// Generator
//

abstract class Generator(outdir: String, outname: String) {
  def init()
  def generate(pagefiles: List[String], srcdir: String, opt: List[String])
}
