#!/usr/bin/ruby
#
#

require 'fileutils'

def init
  if ARGV.length == 0
    STDERR.puts "Usage: mkbook.rb <epub dir> [<epub dir>] ..."
    exit(1)
  end
  dlist = Array.new
  ARGV.each do |d|
    if FileTest.directory?(d) || FileTest.exist?("#{d}.epub")
      dlist << d
    else
      raise StandardError.new("#{d} isn't a directory")
    end
  end
  dlist
end

def create_epub(d)
  if !FileTest.exist?("mimetype")
    raise StandardError.new("mimetype doesn't exist in #{d}")
  end
  system "zip -0 -X ../#{d}.epub mimetype"
  system "zip -r ../#{d}.epub * -x mimetype"
end

def epub2mobi(d)
  system "kindlegen #{d}.epub -o #{d}.mobi.0"
  system "kindlestrip.rb #{d}.mobi.0 #{d}.mobi"
end

def delete_data(d)
  File.delete("#{d}.mobi.0") if File.exist?("#{d}.mobi.0")
  #File.delete("#{d}.epub") if File.exist?("#{d}.epub")
  #FileUtils.rm_r("#{d}") if File.exist?("#{d}")
end

def main
  dlist = init
  dlist.each do |d|
    begin
      if !FileTest.exist?("#{d}.epub")
        Dir.chdir(d)
        create_epub(d)
        Dir.chdir("..")
      end
      epub2mobi(d)
      delete_data(d)
    rescue StandardError => e
      puts e
    end
  end

end

main


