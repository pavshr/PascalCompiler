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
	public void genCode(CodeFile f) {
		simpleExpr.genCode(f);
		if(relOpr != null) {
			f.genInstr("", "pushl", "%eax", "");
			oprSimpleExpr.genCode(f);
			f.genInstr("", "popl", "%ecx", "");
			f.genInstr("", "cmpl", "%eax,%ecx", "");
			f.genInstr("", "movl", "$0,%eax", "");
			switch (relOpr.operator.kind) {
				case equalToken:		
					f.genInstr("", "sete", "%al", "Test " + relOpr.operator.kind);
					break;
				case greaterToken:
					f.genInstr("", "setg", "%al", "Test " + relOpr.operator.kind);
					break;
				case lessToken:
					f.genInstr("", "setl", "%al", "Test " + relOpr.operator.kind);
					break;
				case greaterEqualToken:
					f.genInstr("", "setge", "%al", "Test " + relOpr.operator.kind);
					break;
				case lessEqualToken:
					f.genInstr("", "setle", "%al", "Test " + relOpr.operator.kind);
					break;
				default: // not equal token
					f.genInstr("", "setne", "%al", "Test " + relOpr.operator.kind);
					break;
			}
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