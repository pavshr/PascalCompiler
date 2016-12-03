package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

class PrefixOpr extends Operator {

	Token operator;

	PrefixOpr(int lNum) {
		super(lNum);
	}


	static PrefixOpr parse(Scanner s) {
		enterParser("prefix opr");
		PrefixOpr prefixOpr = new PrefixOpr(s.curLineNum());
		prefixOpr.operator = s.curToken;
		s.readNextToken();
		leaveParser("prefix opr");
		return prefixOpr;
	}

	@Override 
	public String identify() {
		return "prefix opr on line " + lineNum;
	}

	@Override
	void check(Block curScope, Library lib) { }

	@Override
	public void genCode(CodeFile f) {

	}


	@Override
	void prettyPrint() {
		Main.log.prettyPrint(" " + operator.kind.toString() + " ");
	}
}