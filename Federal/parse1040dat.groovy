// Parse the input file using a recursive descent state machine.

def states = [:]

def stateStack = []

class State {
  Closure processLine
  String  stateCode
}

def pushState(State s) {
  stateStack.push(s);
}
def pushState = {
  String s ->
    assert states[s]
    stateStack.push(states[s]);
}

def popState = {
  stateStack.pop()
}

/** Get top of stack */
def currState = {
  stateStack[-1]
}

def defState = {
  String stateCode,  Closure c ->
    states[stateCode] = new State(stateCode: stateCode,
                                  processLine: c);
}


String lineNum;
String lineLetter;
String lineText;

defState('DEFAULT') {
  String line ->
    if (line ==~ /\d+/) {
      lineNum = line;
      pushState "EXPECT_LINE"
    } else {
      println line
    }
}

defState('EXPECT_LINE') {
  String line ->
    def printLine = {
      println "| ${lineNum}. | $lineText | ${lineNum}. |"
      lineText = '';
    }
    if (line == lineNum) {
      printLine()
      popState()
      lineNum = null;
      lineText = '';
    } else if (line ==~ /[a-z]/) {
      lineLetter = line;
      lineText = ''
      popState()
      pushState "EXPECT_SUBLINE"
    } else if (line ==~ /\d+/) {
      // Starting a new line.
      popState();
      currState().processLine(line);
    }else {
      lineText += line;
    }
}
defState('EXPECT_SUBLINE') {
  String line ->
    String llet = (lineNum?:'') + (lineLetter?.size()?lineLetter:'');
    def printLine = {
      println "| ${llet}. | $lineText | ${llet}. |"
      lineText = '';
    }
    if (line == lineNum) {
      printLine()
      popState()
      lineNum = null;
      lineText = '';
    } else if (lineLetter?.size() && line == "$lineNum$lineLetter") {
      // End of a sub number
      printLine();
      lineLetter = null;
      popState();
      pushState "EXPECT_LINE"
    } else if (line ==~ /\d+/) {
      // Starting a new line.
      printLine();
      popState();
    }else {
      lineText += line;
    }
}

pushState('DEFAULT')

def datFile = new File(args[0]);
assert datFile.exists()
//println "states=$states"
//println "currState() == ${currState()}"
datFile.readLines().each {
  line ->
    System.err.println "${currState().stateCode}: $line"
    currState().processLine(line)
}