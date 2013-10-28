#!/usr/bin/ruby
#
#

require 'csv'

def init
  if ARGV.size != 4
    STDERR.puts "Usage: converter.rb <sbtdir> <inputdir> <outputdir>  <booklist file>"
    exit(1)
  end
  @INDIR = ARGV[1]
  @OUTDIR = ARGV[2]
  @OPT = " -i #{@INDIR} -o #{@OUTDIR} "
  @LISTF = ARGV[3]
end

def convert(zip, device, opt)
  zipf = @INDIR + "/#{zip}.zip"
  return if !File.exist?(zipf)
  mobi = @OUTDIR + "/#{device}/#{zip}.mobi"
  if File.exist?(mobi)
    return if File.ctime(mobi) >= File.ctime(zipf)
  end
  Dir.chdir(ARGV[0])
  system "sbt 'run #{@OPT} #{opt} #{zip}'"
  Dir.chdir(@OUTDIR + "/#{device}")
  Dir.glob("#{zip}.*").each do |epub|
    system "mkbook.rb #{epub}"
    File.rename(epub + ".mobi", zip + ".mobi")
  end
end

def update(info)
  return if info[0] =~ /^##/
  opt  = if info[1] != nil then " -s #{info[1]} " else "" end
  opt += if info[2] != nil then " -a #{info[2]} " else "" end
  opt += if info[3] != nil then " -#{info[3]} " else "" end
  opt += if info[4] != nil then " -#{info[4]} " else "" end
  opt += case info[5]
         when "o" then "-n old "
         when "v" then "-n veryold "
         else "-n new "
         end
  info[6..-1].each do |d|
    convert(info[0], d, opt + "-d #{d} ")
  end
end

def main
  init
  CSV.foreach(@LISTF) do |row|
    update(row)
  end
end

main
