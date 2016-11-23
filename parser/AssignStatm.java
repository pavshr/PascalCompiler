package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

class AssignStatm extends Statement {
	
	Variable variable;
	Expression expression;

	AssignStatm(int lNum) {
		super(lNum);
	}


	static AssignStatm parse(Scanner s) {
		enterParser("assign statm");
		AssignStatm assignStatm = new AssignStatm(s.curLineNum());
		assignStatm.variable = Variable.parse(s);
		s.skip(assignToken);
		assignStatm.expression = Expression.parse(s);
		leaveParser("assign statm");
		return assignStatm;
	}

	@Override 
	public String identify() {
		return "<assign statm> on line " + lineNum;
	}

	@Override
	void check(Block curScope, Library lib) {
		variable.check(curScope, lib);
		variable.ref.checkWhetherAssignable(this);
		expression.check(curScope, lib);
		if(variable.ref.arrayDecl != null) {
			if(expression.simpleExpr.terms.get(0).factors.get(0) instanceof Variable) {
				Variable v = (Variable) expression.simpleExpr.terms.get(0).factors.get(0);
				if (v.ref.arrayDecl != null) {
					v.ref.arrayDecl.checkType(variable.ref.arrayDecl, "element type", this, "element type of the arrays differs!");
				}
			}
		}
		variable.type.checkType(expression.type, ":=", this,
            "Different types in assignment!");

	}

	@Override
	void prettyPrint() {
		variable.prettyPrint();
		Main.log.prettyPrint(" := ");
		expression.prettyPrint();
	}
}