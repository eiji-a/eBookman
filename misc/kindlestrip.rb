#!/usr/bin/ruby
#
# kindlestrip
#

class StripError < StandardError
end

class SectionStripper
  def initialize(datain)
    if datain[0x3c, 8] != 'BOOKMOBI'
      raise StripError.new("invalid file format")
    end
    @num_sections = datain[76, 2].unpack("n")[0]
    puts "NUMS: #{@num_sections}"
    offset0 = datain[78, 4].unpack("N")[0]
    offset1 = datain[86, 4].unpack("N")[0]
    puts "OFF: #{offset0}, #{offset1}"
    mobiheader = datain[offset0, offset1]
    arr = mobiheader[0xe0, 8].unpack("NN")
    srcs_secnum = arr[0]
    srcs_cnt = arr[1]
    if srcs_secnum == 0xffffffff || srcs_cnt == 0
      raise StripError.new("File doesn't contain the sources section.")
    end
    puts "Found SRCS section number #{srcs_secnum}, and count #{srcs_cnt}"

    nxt = srcs_secnum + srcs_cnt
    arr1 = datain[(78 + srcs_secnum * 8), 8].unpack("NN")
    arr2 = datain[(78 + nxt * 8), 8].unpack("NN")
    srcs_offset = arr1[0]
    srcs_length = arr2[0] - srcs_offset
    if datain[srcs_offset, 4] != "SRCS"
      raise StripError.new("SRCS section num does not point to SRCS.")
    end
    printf "    beginning at offset %0x and ending at offset %0x\n", srcs_offset, srcs_length

    #-----
    @data_file = datain[0..67] + [(@num_sections - srcs_cnt) * 2 + 1].pack("N")
    @data_file += datain[72, 4]
    @data_file += [@num_sections - srcs_cnt].pack("n")
    delta = -8 * srcs_cnt
    srcs_secnum.times do |i|
      arr = datain[(78 + i * 8), 8].unpack("NN")
      offset = arr[0] + delta
      @data_file += [offset, arr[1]].pack("NN")
    end
    delta = delta - srcs_length
    (srcs_secnum + srcs_cnt).upto(@num_sections - 1) do |i|
      arr = datain[(78 + i * 8), 8].unpack("NN")
      offset = arr[0] + delta
      flgval = 2 * (i - srcs_cnt)
      @data_file += [offset, flgval].pack("NN")
    end

    #-----
    arr = @data_file[78, 8].unpack("NN")
    @data_file += "\0" * (arr[0] - @data_file.length)
    @data_file += datain[offset0..(srcs_offset - 1)]

    @data_file += datain[(srcs_offset + srcs_length)..-1]

    @stripped_data_header = datain[srcs_offset, 16]
    hd = @stripped_data_header.unpack("nnnn")
    @stripped_data = datain[(srcs_offset + 16), srcs_length]
    @num_section = @num_sections - srcs_cnt

    arr1 = @data_file[78, 8].unpack("NN")
    arr2 = @data_file[86, 8].unpack("NN")
    mobiheader = @data_file[arr1[0]..(arr2[0] - 1)]
    mobiheader = mobiheader[0..(0xe0 - 1)] + [0xffffffff, 0].pack("NN") + mobiheader[0xe8..-1]

    mobiheader = updateEXTH121(srcs_secnum, srcs_cnt, mobiheader)
    @data_file = @data_file[0..(arr1[0] - 1)] + mobiheader + @data_file[arr2[0]..-1]
    
    puts "done"

  end

  def loadSection(section)
  end

  def patch(off, nw)
  end

  def strip(off, len)
  end

  def patchSection(section, nw, in_off = 0)
  end

  def updateEXTH121(srcs_secnum, srcs_cnt, mobiheader)
    m0 = mobiheader
    mobi_length = mobiheader[0x14, 4].unpack("N")[0]
    exth_flag   = mobiheader[0x80, 4].unpack("N")[0]
    exth = 'NONE'
    begin
      if exth_flag & 0x40
        exth = mobiheader[(16 + mobi_length)..-1]
        if (exth.length >= 4) && (exth[0..3] == 'EXTH')
          nitems = exth[8, 4].unpack("N")[0]
          pos = 12
          nitems.times do |i|
            arr = exth[pos, 8].unpack("NN")
            if arr[0] == 121
              boundaryptr = exth[(pos + 8), arr[1]].unpack("N")[0]
              if srcs_secnum <= boundaryptr
                boundaryptr -= srcs_cnt
                prefix = mobiheader[0..(16 + mobi_length + pos + 8 - 1)]
                suffix = mobiheader[(16 + mobi_length + pos + 8 + 4)..-1]
                nval = [boundaryptr].pack("N")
                mobiheader = prefix + nval + suffix
              end
            end
            pos += arr[1]
          end
        end
      end
    rescue StandardError => e
      puts e
    end
    mobiheader
  end

  def getResult
    @data_file
  end

  def getStrippedData
    @stripped_data
  end

  def getHeader
    @stripped_data_header
  end

end

def main
  # Unbuffered?
  puts "Kindlestrip v 0.1 Written 2013 by Eiji"
  if ARGV.length < 2 || ARGV.length > 3
    puts "Strips the Source record from Mobipocket ebooks"
    puts "For ebooks generated using KindleGen 1.1 and later that add the source"
    puts "Usage:"
    puts "    kindlestrip <infile> <outfile> [<strippeddatafile>]"
    exit(1)
  end
  infile = ARGV[0]
  outfile = ARGV[1]
  data_file = open(infile, 'rb').read
  begin
    strippedFile = SectionStripper.new(data_file)
    open(outfile, 'wb').write(strippedFile.getResult)
    printf "Header Bytes: %0x\n", strippedFile.getHeader
    if ARGV.length == 3
      open(ARGV[2], 'wb').write(strippedFile.getStripedData)
    end
  rescue StandardError => e
    STDERR.puts "Error: " + e.message
    exit(1)
  end

end

main
