// http://www.openscope.net/2010/02/09/transforming-xml-into-ms-excel-xml/
// https://msdn.microsoft.com/en-us/library/aa140066%28office.10%29.aspx#odc_xmlss_ss:cell
// 
import groovy.xml.*

def input = new File('w2-2015.txt')
def output = new File('w2-2015.xml')

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
      Worksheet('ss:Name': "IRS f1040") {
        Styles {
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
                     'ss:LineStyle': 'Continuous')

            }
          }
          Style('ss:ID': "LineText", 'ss:Name': "Normal") {

          } 
        }
        Table {
          input.eachLine {
            line ->
              Row {
                if (line =~ /^\|/) {
                  def fields = line.split(/ *\| */)
                  if (fields.size() > 2) {
                    Cell {
                      Data('ss:Type': "String", 
                           'ss:StyleID': 'sLineNumber', 
                           "${fields[1]}")
                    }
                    Cell {
                      Data('ss:Type': "String", "${fields[2]}")
                    }
                    Cell {
                      Data('ss:Type': "String", 
                           'ss:StyleID': 'sLineNumber', 
                           "${fields[1]}")
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
