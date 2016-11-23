package scanner;

import main.Main;
import static scanner.TokenKind.*;

import java.io.*;

public class Scanner {
    public Token curToken = null, nextToken = null; 

    private LineNumberReader sourceFile = null;
    private String sourceFileName, sourceLine = "";
    private int sourcePos = 0;
    private boolean eof = false;
    private boolean init = false;

    public Scanner(String fileName) {
	sourceFileName = fileName;
	try {
	    sourceFile = new LineNumberReader(new FileReader(fileName));
	} catch (FileNotFoundException e) {
	    Main.error("Cannot read " + fileName + "!");
	}

	readNextToken();
	readNextToken();
    }


    public String identify() {
	return "Scanner reading " + sourceFileName;
    }


    public int curLineNum() {
	if (curToken == null) return getFileLineNum();
	return curToken.lineNum;
    }

    private void error(String message) {
	// Main.error("Scanner error on " +
	// 	   (curLineNum()<0 ? "last line" : "line "+curLineNum()) + 
	// 	   ": " + message);

    	Main.error("Scanner error" +
             (getFileLineNum()>0 ? " on line "+getFileLineNum() : "") +
             ": " + message);
    }


    public void readNextToken() {
	curToken = nextToken;  nextToken = null;

	// Del 1 her:
	if (!init) {
		readNextLine();
		init = true;
	}
	if (sourceLine.equals(" ")) {
		readNextLine();
	}
	
	boolean tokenFound = false;
	while (!tokenFound) {
		// skipping spaces and tabs
		while ((sourcePos < sourceLine.length() && sourceLine.charAt(sourcePos) == ' ') || (sourcePos < sourceLine.length() && sourceLine.charAt(sourcePos) == '\t')) {
			sourcePos++;
			if (sourcePos == sourceLine.length()) {
				readNextLine();
			}
		}
		// eof token.
		if (sourceLine.equals("")) {
			nextToken = new Token(TokenKind.eofToken, -1);
			if (!eof) Main.log.noteToken(nextToken);
			eof = true;
			tokenFound = true;
			return;
		}

		char c = sourceLine.charAt(sourcePos);
		switch (c) {
			// /*...*/-comments.
			case '/':
				sourcePos++;
				if (sourceLine.charAt(sourcePos) == '*') {
					if (findCommentEnd1()) sourcePos++;
					else {
						error("No end for comment starting on line " + curLineNum() + "!");
					}
				}else{
					error("'/' is allowed to use only as a comment opener in pascal 2016");
				}
				break;
			// {...}-comments.
			case '{':
				if (findCommentEnd2()); // do nothing.
				else {
					error("No end for comment starting on line " + curLineNum() + "!");
				}
				break;
			// Add token.
			case '+' :
				nextToken = new Token(TokenKind.addToken, getFileLineNum());
				Main.log.noteToken(nextToken);
				sourcePos++;
				tokenFound = true;
				break;
			// Subtract token.
			case '-' :
				nextToken = new Token(TokenKind.subtractToken, getFileLineNum());
				Main.log.noteToken(nextToken);
				sourcePos++;
				tokenFound = true;
				break;
			// Multiply token.
			case '*' :
				nextToken = new Token(TokenKind.multiplyToken, getFileLineNum());
				Main.log.noteToken(nextToken);
				sourcePos++;
				tokenFound = true;
				break;
			// Equal token.
			case '=' :
				nextToken = new Token(TokenKind.equalToken, getFileLineNum());
				Main.log.noteToken(nextToken);
				sourcePos++;
				tokenFound = true;
				break;
			// Semicolon token.
			case ';' :
				nextToken = new Token(TokenKind.semicolonToken, getFileLineNum());
				Main.log.noteToken(nextToken);
				sourcePos++;
				tokenFound = true;
				break;
			// Colon/Assignment token token.
			case ':' :
				if (sourceLine.charAt(sourcePos + 1) == '=') { // assignment
					nextToken = new Token(TokenKind.assignToken, getFileLineNum());
					Main.log.noteToken(nextToken);
					sourcePos+=2;
				} else { // colon
					nextToken = new Token(TokenKind.colonToken, getFileLineNum());
					Main.log.noteToken(nextToken);
					sourcePos++;
				}
				tokenFound = true;
				break;
			// Comma token.
			case ',' :
				nextToken = new Token(TokenKind.commaToken, getFileLineNum());
				Main.log.noteToken(nextToken);
				sourcePos++;
				tokenFound = true;
				break;
			// Dot/Range token.
			case '.' :
				if (sourceLine.charAt(sourcePos + 1) == '.') { // range
					nextToken = new Token(TokenKind.rangeToken, getFileLineNum());
					Main.log.noteToken(nextToken);
					sourcePos+=2;
				} else { // dot
					nextToken = new Token(TokenKind.dotToken, getFileLineNum());
					Main.log.noteToken(nextToken);
					sourcePos++;
				}
				tokenFound = true;
				break;
			// Greater/GreaterEqual token.
			case '>' :
				if (sourceLine.charAt(sourcePos + 1) == '=') { // greaterequal
					nextToken = new Token(TokenKind.greaterEqualToken, getFileLineNum());
					Main.log.noteToken(nextToken);
					sourcePos+=2;
				} else { //greater
					nextToken = new Token(TokenKind.greaterToken, getFileLineNum());
					Main.log.noteToken(nextToken);
					sourcePos++;
				}
				tokenFound = true;
				break;
			// Less/Not equal/LessEqual token.
			case '<' :
				if (sourceLine.charAt(sourcePos + 1) == '=') { // lessequal
					nextToken = new Token(TokenKind.lessEqualToken, getFileLineNum());
					Main.log.noteToken(nextToken);
					sourcePos+=2;
				} else if (sourceLine.charAt(sourcePos + 1) == '>') { // not equal
					nextToken = new Token(TokenKind.notEqualToken, getFileLineNum());
					Main.log.noteToken(nextToken);
					sourcePos+=2;
				} else { // less
					nextToken = new Token(TokenKind.lessToken, getFileLineNum());
					Main.log.noteToken(nextToken);
					sourcePos++;
				}
				tokenFound = true;
				break;
			// LeftParanthes token.
			case '(' :
				nextToken = new Token(TokenKind.leftParToken, getFileLineNum());
				Main.log.noteToken(nextToken);
				sourcePos++;
				tokenFound = true;
				break;
			// RightParanthes token.
			case ')' :
				nextToken = new Token(TokenKind.rightParToken, getFileLineNum());
				Main.log.noteToken(nextToken);
				sourcePos++;
				tokenFound = true;
				break;
			// LeftBracket token.
			case '[' :
				nextToken = new Token(TokenKind.leftBracketToken, getFileLineNum());
				Main.log.noteToken(nextToken);
				sourcePos++;
				tokenFound = true;
				break;
			// RightBracket token.
			case ']' :
				nextToken = new Token(TokenKind.rightBracketToken, getFileLineNum());
				Main.log.noteToken(nextToken);
				sourcePos++;
				tokenFound = true;
				break;
			// Character token.
			case '\'' :
				sourcePos++;
				if (sourceLine.charAt(sourcePos) == '\'') {
					sourcePos++;
					if (sourceLine.charAt(sourcePos) != '\'') { // empty char
						nextToken = new Token('\0', getFileLineNum());
						Main.log.noteToken(nextToken);
					}else{
						sourcePos++;
						if (sourceLine.charAt(sourcePos) == '\'') { // ''' char
							nextToken = new Token('\'', getFileLineNum());
							Main.log.noteToken(nextToken);
							sourcePos++;
						}else{
							error("Illegal character \'" + sourceLine.charAt(sourcePos - 1) + "\'");
						}
					}
				}
				else if (sourceLine.charAt(sourcePos + 1) != '\'') {
					error("Illegal char literal!");
				} else {
					char ch = sourceLine.charAt(sourcePos);
					nextToken = new Token(ch, getFileLineNum());
					Main.log.noteToken(nextToken);
					sourcePos += 2;
				}
				tokenFound = true;
				break;
			
			default:
				// Number token.
				if (isDigit(c)) {
					String numberStr = "";
					while (isDigit(sourceLine.charAt(sourcePos))) {
						numberStr += sourceLine.charAt(sourcePos);
						sourcePos++;
					}
					sourcePos--;
					int number = Integer.parseInt(numberStr);
					nextToken = new Token(number, getFileLineNum());
					Main.log.noteToken(nextToken);
				// Word token.	
				} else if (isLetterAZ(c)) {
					String str = "";
					while (isLetterAZ(sourceLine.charAt(sourcePos)) || isDigit(sourceLine.charAt(sourcePos))) {
						str += sourceLine.charAt(sourcePos);
						sourcePos++;
					}
					sourcePos--;
					nextToken = new Token(str.toLowerCase(), getFileLineNum());
					Main.log.noteToken(nextToken);
				} else {
					error("Illegal character: \'" + c + "\'");
					return;
				}
				sourcePos++;
				tokenFound = true;
				break;
			}
		}
    }

	/**
	*Searches the file for the end to /*-starting comment.
	*@return bool True if it occurs. 
	*/
    private boolean findCommentEnd1() {
		while (!sourceLine.equals("")) {
			while (sourcePos < sourceLine.length() - 1)  {
				if(sourceLine.substring(sourcePos, sourcePos + 2).equals("*/")) {
					sourcePos++;
					return true;
				}
				sourcePos++;
			}
			readNextLine();
		}
		return false;
	}
	/**
	*Searches the file for the end to {-starting comment.
	*@return bool True if it occurs. 
	*/
	private boolean findCommentEnd2() {
		while (!sourceLine.equals("")) {
			while (sourcePos < sourceLine.length() - 1)  {
				if(sourceLine.charAt(sourcePos) == '}') {
					sourcePos++;
					return true;
				}
				sourcePos++;
			}
			readNextLine();
		}
		return false;
	}

    private void readNextLine() {
	if (sourceFile != null) {
	    try {
		sourceLine = sourceFile.readLine();
		if (sourceLine == null) {
		    sourceFile.close();  sourceFile = null;
		    sourceLine = "";
		} else {
		    sourceLine += " ";
		}
		sourcePos = 0;
	    } catch (IOException e) {
		Main.error("Scanner error: unspecified I/O error!");
	    }
	}
	if (sourceFile != null) 
	    Main.log.noteSourceLine(getFileLineNum(), sourceLine);
    }


    private int getFileLineNum() {
	return (sourceFile!=null ? sourceFile.getLineNumber() : 0);
    }


    // Character test utilities:

    private boolean isLetterAZ(char c) {
	return 'A'<=c && c<='Z' || 'a'<=c && c<='z';
    }


    private boolean isDigit(char c) {
	return '0'<=c && c<='9';
    }


    // Parser tests:

    public void test(TokenKind t) {
	if (curToken.kind != t)
	    testError(t.toString());
    }

    public void testError(String message) {
	Main.error(curLineNum(), 
		   "Expected a " + message +
		   " but found a " + curToken.kind + "!");
    }

    public void skip(TokenKind t) {
	test(t);  
	readNextToken();
    }
}
