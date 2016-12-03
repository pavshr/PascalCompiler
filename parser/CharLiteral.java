package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

class CharLiteral extends UnsignedConstant {
	
	char charVal;

	CharLiteral(int lNum) {
		super(lNum);
	}


	static CharLiteral parse(Scanner s) {
		enterParser("char literal");
		CharLiteral charLiteral = new CharLiteral(s.curLineNum());
		charLiteral.charVal = s.curToken.charVal;
		s.skip(charValToken);
		leaveParser("char literal");
		return charLiteral;
	}

	@Override 
	public String identify() {
		return "char literal on line " + lineNum;
	}

	@Override
	void check(Block curScope, Library lib) {
		constVal = (int) charVal;
		type = lib.charType;
	}

	@Override
	public void genCode(CodeFile f) {
		f.genInstr("", "movl", "$" + constVal + ",%eax", "\'" + charVal + "\'");
	}

	@Override
	void prettyPrint() {
		Main.log.prettyPrint("'" + charVal + "'");
	}
}