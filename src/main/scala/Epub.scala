//
// Epub
//


import scalax.file.Path

class Epub(outdir: String, outname: String) extends Generator(outdir, outname) {

  val OUTPATH = Path(outdir + "/" + outname, '/')
  val EXTJPG = ".jpg"
  val EXTPNG = ".png"
  val TYPEJPG = "image/jpeg"
  val TYPEPNG = "image/png"

  def createMimetype() = {
    val mtype = OUTPATH / Path("mimetype")
    mtype.write("application/epub+zip")
  }

  def createToc() = {
    val toc = OUTPATH / Path("toc.ncx")
    toc.write("""<?xml version="1.0" encoding="UTF-8"?>""")
    toc.append("\n")
    toc.append("""<!DOCTYPE ncx PUBLIC "-//NISO//DTD ncx 2005-1//EN" "http://www.daisy.org/z3986/2005/ncx-2005-1.dtd">""")
    toc.append("\n")
    val ncx =
<ncx xmlns="http://www.daisy.org/z3986/2005/ncx/" version="2005-1" xml:lang="jp">
  <head>
    <meta name="dtb:uid" content="630358fe-4dd5-46e8-9241-4904dab61eeb"/>
    <meta name="dtb:depth" content="1"/>
    <meta name="dtb:generator" content="Txt2ePub"/>
    <meta name="dtb:totalPageCount" content="0"/>
    <meta name="dtb:maxPageNumber" content="0"/>
  </head>
  <docTitle><text>Table of Contents</text></docTitle>
  <navMap>
  </navMap>
</ncx>
    toc.append(ncx.toString())
  }

  def createMeta() = {
    val meta = OUTPATH / Path("META-INF")
    meta.createDirectory()
    val container = meta / Path("container.xml")
    container.write("""<?xml version="1.0"?>""")
    container.append("\n")
    val ctn =
<container version="1.0" xmlns="urn:oasis:names:tc:opendocument:xmlns:container">
  <rootfiles>
    <rootfile full-path="metadata.opf" media-type="application/oebps-package+xml"/>
  </rootfiles>
</container>
    container.append(ctn.toString())
  }


  def initContent(): Path = {
    val cont = OUTPATH / Path("content")
    cont.createDirectory()
    val resource = cont / Path("resources")
    resource.createDirectory()
    val css = """
body {
  text-align: left;
  text-indent: 0em;
  margin-top: 0;
  margin-bottom: 0;
  margin-left: 0;
  margin-right: 0;
  Padding-top: 0;
  Padding-bottom: 0;
  Padding-left: 0;
  Padding-right: 0;
}
html {
  writing-mode: tb-rl;
  direction: rtl;
  -moz-writing-mode: vertical-rl;
  -webkit-writing-mode: vertical-rl;
  -o-writing-mode: vertical-rl;
  -ms-writing-mode: vertical-rl;
  -epub-writing-mode: vertical-rl;
}
p {
  text-align: left;
  text-indent: 0em;
  margin-top: 0;
  margin-bottom: 0;
  margin-left: 0;
  margin-right: 0;
  Padding-top: 0;
  Padding-bottom: 0;
  Padding-left: 0;
  Padding-right: 0;
}
div {
  text-align: left;
  text-indent: 0em;
  margin-top: 0;
  margin-bottom: 0;
  margin-left: 0;
  margin-right: 0;
  Padding-top: 0;
  Padding-bottom: 0;
  Padding-left: 0;
  Padding-right: 0;
}
    """
    val cssf = resource / Path("index_0.css")
    cssf.write(css)
    cont
  }

  def init() = {
    OUTPATH.createDirectory()
    createMimetype()
    createToc()
    createMeta()
    initContent()
  }

  def copyImage(tmpdir: String, imagefile: String, imgext: String) = {
    val imgpath = Path(tmpdir + "/" + imagefile + imgext, '/')
    imgpath.copyTo(OUTPATH / Path("content/resources/" + imagefile + imgext, '/'))
  }

  def createHtml(imagefile: String, imgext: String, size: (String, String)) = {
    val htmlpath = OUTPATH / Path("content/" + imagefile + ".xhtml", '/')
    val html = """
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:xlink="http://www.w3.org/1999/xlink">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>###IMGFILE###</title>
<link href="resources/index_0.css" type="text/css" charset="UTF-8" rel="stylesheet" />
</head>
<body>
<div>
<p><img src="resources/###IMGFILE###" width="###SIZEW###" height="###SIZEH###" />
</p>
</div>
</body>
</html>
    """
    val html0 = html.replaceAll("###IMGFILE###", imagefile + imgext)
    val html1 = html0.replaceAll("###SIZEW###", size._1)
    val html2 = html1.replaceAll("###SIZEH###", size._2)

    htmlpath.write(html2)
  }

  def createContent(tmpdir: String, imagefiles: List[String], imgext: String, size: (String, String)) = {
    imagefiles.map(copyImage(tmpdir, _, imgext))
    imagefiles.map(createHtml(_, imgext, size))
  }

  def manifest(imgfile: String, imgext: String): String = {
    val fmt = imgext match {
      case EXTJPG => TYPEJPG
      case EXTPNG => TYPEPNG
      case _      => TYPEJPG
    }
    val manif1 =
<item id={imgfile + "-1"} href={"content/resources/" + imgfile + imgext} media-type={fmt}/>
    val manif2 =
<item id={imgfile + "-2"} href={"content/" + imgfile + ".xhtml"} media-type="application/xhtml+xml"/>

    List(manif1, manif2).map(_.toString()).mkString("\n")
  }

  def itemref(imgfile: String): String = {
    val item =
<itemref idref={imgfile + "-2"}/>

    item.toString()
  }

  def generate(imagefiles: List[String], srcdir: String, opt: List[String]) = {
    val imgext = opt(0)
    val title  = opt(1)
    val author = opt(2)
    val sizex  = opt(3)
    val sizey  = opt(4)
    val opf = OUTPATH / Path("metadata.opf")
    opf.write("""<?xml version="1.0"  encoding="UTF-8"?>""")
    opf.append("\n")
    opf.append("""<package xmlns="http://www.idpf.org/2007/opf" version="2.0" unique-identifier="calibre_id">""")
    opf.append("\n")
    val opfxml =
<metadata xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:opf="http://www.idpf.org/2007/opf" xmlns:Txt2ePub="http://no722.cocolog-nifty.com/blog/metadata">
<dc:title>{title}</dc:title>
<dc:creator opf:role="aut" opf:file-as="Unknown">{author}</dc:creator>
<dc:contributor opf:role="bkp" opf:file-as="eBookman">eBookman (0.1)</dc:contributor>
<dc:identifier opf:scheme="eBookman" id="eBookman_id">630358fe-4dd5-46e8-9241-4904dab61eeb</dc:identifier>
<dc:date>2013-09-29T14:50:27+09:00</dc:date>
<dc:language>ja</dc:language>
<meta content="true" name="fixed-layout" />
<meta content="comic" name="book-type" />
<meta name="Txt2ePub:series_index" content="1"/>
<meta name="cover" content="img-0000-1" />
<dc:identifier opf:scheme="ISBN"></dc:identifier>
</metadata>

    opf.append(opfxml.toString())
    opf.append("<manifest>\n")
    opf.append(imagefiles.map(manifest(_, imgext)).mkString("\n"))
    opf.append("\n</manifest>\n")
    opf.append("""<spine page-progression-direction="ltr">""" + "\n")
    opf.append(imagefiles.map(itemref(_)).mkString("\n"))
    opf.append("\n</spine>\n")
    opf.append("</package>\n")      
    

    createContent(srcdir, imagefiles, imgext, (sizex, sizey))
  }


}
