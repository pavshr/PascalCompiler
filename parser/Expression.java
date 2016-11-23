package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

class Expression extends PascalSyntax {
	
	SimpleExpr simpleExpr, oprSimpleExpr;
	RelOpr relOpr;

	types.Type type;

	Expression(int lNum) {
		super(lNum);
	}


	static Expression parse(Scanner s) {
		enterParser("expression");
		Expression expression = new Expression(s.curLineNum());
		expression.simpleExpr = SimpleExpr.parse(s);
		if (s.curToken.kind.isRelOpr()) {
			expression.relOpr = RelOpr.parse(s);
			expression.oprSimpleExpr = SimpleExpr.parse(s);
		}
		leaveParser("expression");
		return expression;
	}

	@Override 
	public String identify() {
		return "<expression> on line " + lineNum;
	}

	@Override
	void check(Block curScope, Library lib) {
		simpleExpr.check(curScope, lib);
		type = simpleExpr.type;
		if (oprSimpleExpr != null) {
			oprSimpleExpr.check(curScope, lib);
			String oprName = relOpr.operator.kind.toString();
            type.checkType(oprSimpleExpr.type, oprName+" operands", this,
                "Operands to "+oprName+" are of different type!");
            type = lib.boolType;
			//oprSimpleExpr.check(curScope, lib);
		}
	}

	@Override
	void prettyPrint() {
		simpleExpr.prettyPrint();
		if (relOpr != null) {
			relOpr.prettyPrint();
			oprSimpleExpr.prettyPrint();
		}
	}
}