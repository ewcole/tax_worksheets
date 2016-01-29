// http://www.openscope.net/2010/02/09/transforming-xml-into-ms-excel-xml/
// https://msdn.microsoft.com/en-us/library/aa140066%28office.10%29.aspx#odc_xmlss_ss:cell
// 
import groovy.xml.*
def cli = new CliBuilder(usage: "Render input file as an XML spreadsheet definition")
cli.i(longOpt: 'input', args: 1, required: true, "Input file")
cli.o(longOpt: 'output', args: 1, required: true, "Output file")
def opt = cli.parse(args)
if (!opt) {
  return
}
def input = new File(opt.i)
def output = new File(opt.o)
if (!input.exists()) {
  System.err.println "Cannot find file: ${input.getCanonicalFile()}"
}
output.withWriter {
  o ->
    o.println "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>"
    o.println "<?mso-application progid=\"Excel.Sheet\"?>"
    def m = new MarkupBuilder(o);
    m.setDoubleQuotes(true);
    m.'Workbook'(
      xmlns: "urn:schemas-microsoft-com:office:spreadsheet",
      'xmlns:o': "urn:schemas-microsoft-com:office:office",
      'xmlns:x': "urn:schemas-microsoft-com:office:excel",
      'xmlns:ss': "urn:schemas-microsoft-com:office:spreadsheet"
    ) {
      Worksheet('ss:Name': "PA-40 G-L") {
        /* Styles {
          Style('ss:ID': "Default", 'ss:Name': "Normal") 
          Style('ss:ID': "sLineNumber", 'ss:Name': "LineNumber") {
            Font('ss:Bold': 1)
            Borders {
              Border('ss:Position': 'Left',
                     'ss:Color':    '#000000',
                     'ss:Weight':   0,
                     'ss:LineStyle': 'Continuous')

              Border('ss:Position': 'Top',
                     'ss:Color':    '#000000',
                     'ss:Weight':   0,
                     'ss:LineStyle': 'Continuous')

              Border('ss:Position': 'Right',
                     'ss:Color':    '#000000',
                     'ss:Weight':   0,
                     'ss:LineStyle': 'Continuous')

              Border('ss:Position': 'Bottom',
                     'ss:Color':    '#000000',
                     'ss:Weight':   0,
                     'ss': 'Continuous')

            }
          }
          Style('ss:ID': "LineText", 'ss:Name': "Normal") {

          } 
          } */
        Table {
          input.readLines().eachWithIndex {
            line, r  ->
              Row {
                if (line =~ /^\|/) {
                  def fields = line.split(/ *\| */)
                  def lineNum = ""
                  if (fields.size() >= 2) {
                    (fields[1] =~ /^(I+\.\d+[a-z]*)\.$/).each {
                      lineNum = it[1] as String
                    }
                  }
                  // Cell {
                  //   Data('ss:Type': "String", 
                  //        //'ss:StyleID': 'sLineNumber', 
                  //        "${fields[1]}")
                  // }
                  fields[1..-1].eachWithIndex {
                    txt, c ->
                      Cell {
                        if (txt =~ /^%(\.[0-9]*)?d$/) {
                          Data('ss:Type': "Number", 0)
                        }
                        else {
                          Data('ss:Type': "String", "$txt")
                        }
                      }
                  }
                } else {
                  def opts = [:]
                  if (line.size() > 3) {
                    opts.'ss:MergeAcross' = 3
                  }
                  Cell(opts) { 
                    Data('ss:Type': "String", "$line")
                  }
                }
              } 
          }
        }
      }
    }
}

