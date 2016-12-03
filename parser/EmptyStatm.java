package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

class EmptyStatm extends Statement {

	EmptyStatm(int lNum) {
		super(lNum);
	}

	static EmptyStatm parse(Scanner s) {
		enterParser("empty statm");
		EmptyStatm emptyStatm = new EmptyStatm(s.curLineNum());
		leaveParser("empty statm");
		return emptyStatm;
	}

	@Override 
	public String identify() {
		return "<empty statm> on line " + lineNum;
	}

	@Override
	void check(Block curScope, Library lib) {
		//do nothing
	}

	@Override
	public void genCode(CodeFile f) {

	}


	@Override
	void prettyPrint() {
	}
}