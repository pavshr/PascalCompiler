package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

class InnerExpr extends Factor {
	
	Expression expression;

	types.Type type;

	InnerExpr(int lNum) {
		super(lNum);
	}


	static InnerExpr parse(Scanner s) {
		enterParser("inner expr");
		InnerExpr innerExpr = new InnerExpr(s.curLineNum());
		s.skip(leftParToken);
		innerExpr.expression = Expression.parse(s);
		s.skip(rightParToken);
		leaveParser("inner expr");
		return innerExpr;
	}

	@Override 
	public String identify() {
		return "<inner expr> on line " + lineNum;
	}

	@Override
	void check(Block curScope, Library lib) {
		expression.check(curScope, lib);
		type = expression.type;
	}

	@Override
	public void genCode(CodeFile f) {
		expression.genCode(f);
	}

	@Override
	public types.Type getType() {
		return type;
	}

	@Override
	void prettyPrint() {
		Main.log.prettyPrint("(");
		expression.prettyPrint();
		Main.log.prettyPrint(") ");
	}
}