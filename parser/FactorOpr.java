package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

class FactorOpr extends Operator {
	FactorOpr(int lNum) {
		super(lNum);
	}

static FactorOpr parse(Scanner s) {
		enterParser("factor opr");
		FactorOpr factorOpr = new FactorOpr(s.curLineNum());
		factorOpr.operator = s.curToken;
		s.readNextToken();
		leaveParser("factor opr");
		return factorOpr;
	}

	@Override 
	public String identify() {
		return "<factor opr> on line " + lineNum;
	}

	@Override
	void check(Block curScope, Library lib) {

	}

	@Override
	void prettyPrint() {
		Main.log.prettyPrint(" " + operator.kind.toString());
	}
}