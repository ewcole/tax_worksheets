#! /usr/bin/groovy

import groovy.xml.*


// http://www.openscope.net/2010/02/09/transforming-xml-into-ms-excel-xml/
// https://msdn.microsoft.com/en-us/library/aa140066%28office.10%29.aspx#odc_xmlss_ss:cell

def cli = new CliBuilder(usage: "Convert text file into spreadsheet, breaking on verticals")
cli.i(longOpt: "input", args: 1, required: true, "Input file");
def opt = cli.parse(args);
assert opt
def input = new File(opt.i);
assert input.exists();
def output = new File(opt.i.replaceAll(/\..*$/,'')+'.xml');
System.out.println "creating $output"
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
    Styles  { 
      Style("ss:ID":"s63") {
        Alignment("ss:Vertical":"Top", "ss:WrapText":"1")
      }
    }
    Worksheet('ss:Name': "IRS f1040") {
      /*
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
        } */
        Table {
        def maxColumns = input.readLines().collect {
          it.split(/ *\| */)?.size()
        }.max();
        println "maxColumns=$maxColumns"
          input.eachLine {
            line ->
              Row {
                if (line =~ /^\|/) {
                  def fields = line.split(/ *\| */)
              //////////////////////////////////////////////////////////////////////
              // Experiment
              if (fields.size() > 2) {
                println "${fields.size()}: $line"
                println fields
                def dataColumn = (3..maxColumns-1).inject(null) {
                  dcol, fieldNum ->
                  if (dcol) {
                    return dcol;
                  } else if (fields[fieldNum] =~ /\S/) {
                    return fieldNum;
                  } else {
                    return null
                  }
                }
                println "dataColumn = $dataColumn: ${fields[dataColumn]}"
                // Line number at beginning of row
                Cell {
                  Data('ss:Type': "String", 
                       // 'ss:StyleID': 'sLineNumber', 
                       "${fields[1]}")
                }
                // Descriptive text.  Expand it as far as width permits
                // Cell('ss:MergeAcross':2) {
                def mergeAcross = 2 * (dataColumn -1)
                Cell('ss:MergeAcross': mergeAcross) {
                  Data('ss:Type': "String", "${fields[2]}")
                }
                Cell {
                  Data('ss:Type': "String", 
                       // 'ss:StyleID': 'sLineNumber', 
                       "${fields[1]}")
                }
              
              // End experiment
              //////////////////////////////////////////////////////////////////////
              } else if (fields.size() > 4) {
                    Cell {
                      Data('ss:Type': "String", 
                           // 'ss:StyleID': 'sLineNumber', 
                           "${fields[1]}")
                    }
                    Cell('ss:MergeAcross':2) {
                      Data('ss:Type': "String", "${fields[2]}")
                    }
                    Cell {
                      Data('ss:Type': "String", 
                           // 'ss:StyleID': 'sLineNumber', 
                           "${fields[1]}")
                    }
              } else if (fields.size() > 2) {
                println "${fields.size()}: $line"
                println fields
                    Cell {
                      Data('ss:Type': "String", 
                           // 'ss:StyleID': 'sLineNumber', 
                           "${fields[1]}")
                    }
                    Cell {
                      Data('ss:Type': "String", "${fields[2]}")
                    }
                    Cell {
                      Data('ss:Type': "String", 
                           // 'ss:StyleID': 'sLineNumber', 
                           "${fields[1]}")
                    }
                  }
                } else {
                  def opts = [:]
                  if (line.size() > 3) {
                    opts.'ss:MergeAcross' = maxColumns
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
