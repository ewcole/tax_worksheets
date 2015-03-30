// http://www.openscope.net/2010/02/09/transforming-xml-into-ms-excel-xml/
// https://msdn.microsoft.com/en-us/library/aa140066%28office.10%29.aspx#odc_xmlss_ss:cell
// 
import groovy.xml.*

def input = new File('it203.txt')
def output = new File('it203.xml')

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
      Worksheet('ss:Name': "New York IT203") {
        Styles {
          Style('ss:ID': "Default", 'ss:Name': "Normal") 
          Style('ss:ID': "sLineNumber", 'ss:Name': "LineNumber") {
            Font('ss:Bold': 1)
          }
        }
        Table {
          input.eachLine {
            line ->
              Row {
                if (line =~ /^\|/) {
                  def fields = line.split(/ *\| */)
                  if (fields.size() >= 4) {
                    def lineNum = fields[1]
                    def desc = fields[2]
                    def col1 = fields[3]
                    def col2 = (fields.size() >= 5)?fields[4]:""
                    // Begin with line number
                    Cell {
                      Data('ss:Type': "String", 
                           'ss:StyleID': 'sLineNumber', 
                           "${lineNum}")
                    }
                    Cell {
                      Data('ss:Type': "String", "${desc}")
                    }
                    Cell {
                      if (col1 =~ /^$lineNum /) {
                        Data('ss:Type': "String", 
                             'ss:StyleID': 'sLineNumber', 
                             "${lineNum}")
                      }
                    }
                    Cell {}
                    Cell {
                      if (col2 =~ /^$lineNum /) {
                        Data('ss:Type': "String", 
                             'ss:StyleID': 'sLineNumber', 
                             "${lineNum}")
                      }
                    }
                    Cell {}
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