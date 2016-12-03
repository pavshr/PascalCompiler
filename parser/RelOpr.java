package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

class RelOpr extends Operator {
	

	RelOpr(int lNum) {
		super(lNum);
	}

	static RelOpr parse(Scanner s) {
		enterParser("rel opr");
		RelOpr relOpr = new RelOpr(s.curLineNum());
		relOpr.operator = s.curToken;
		s.readNextToken();
		leaveParser("rel opr");
		return relOpr;
	}

	@Override 
	public String identify() {
		return "<rel opr> on line " + lineNum;
	}

	@Override
	void check(Block curScope, Library lib) {
	}

	@Override
	public void genCode(CodeFile f) {

	}

	@Override
	void prettyPrint() {
		Main.log.prettyPrint(" " + operator.kind.toString() + " ");
	}
}