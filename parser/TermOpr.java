package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

class TermOpr extends Operator {

	TermOpr(int lNum) {
		super(lNum);
	}


	static TermOpr parse(Scanner s) {
		enterParser("term opr");
		TermOpr termOpr = new TermOpr(s.curLineNum());
		termOpr.operator = s.curToken;
		s.readNextToken();
		leaveParser("term opr");
		return termOpr;
	}

	@Override 
	public String identify() {
		return "<term opr on line> " + lineNum;
	}

	@Override
	void check(Block curScope, Library lib) {

	}

	@Override
	public void genCode(CodeFile f) {}

	@Override
	void prettyPrint() {
		Main.log.prettyPrint(" " + operator.kind.toString());
	}
}