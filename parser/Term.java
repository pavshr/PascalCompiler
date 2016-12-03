package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

import java.util.ArrayList;

class Term extends PascalSyntax {
	
	ArrayList<Factor> factors = new ArrayList<Factor>();
	ArrayList<FactorOpr> factorOperators = new ArrayList<FactorOpr>();

	types.Type type;

	Term(int lNum) {
		super(lNum);
	}


	static Term parse(Scanner s) {
		enterParser("term");
		Term term = new Term(s.curLineNum());
		boolean condition = true;
		while (condition) {
			term.factors.add(Factor.parse(s));
			if(s.curToken.kind.isFactorOpr()) {
				term.factorOperators.add(FactorOpr.parse(s));
			}else{
				condition = false;
			}
		}
		leaveParser("term");
		return term;
	}

	@Override 
	public String identify() {
		return "<term> on line " + lineNum;
	}

	@Override
	void check(Block curScope, Library lib) {

		for(Factor factor : factors) {
			if(factor instanceof Negation) {
				//System.out.println("neg");
				factor = (Negation) factor;
				factor.check(curScope, lib);
			}
			if(factor instanceof InnerExpr) {
				//System.out.println("inner");
				factor = (InnerExpr) factor;
				factor.check(curScope, lib);
			}
			if(factor instanceof Variable) {
				//System.out.println("varia");
				factor = (Variable) factor;
				factor.check(curScope, lib);
			}
			if(factor instanceof FuncCall) {
				//System.out.println("funccall");
				factor = (FuncCall) factor;
				factor.check(curScope, lib);
			}
			if(factor instanceof UnsignedConstant) {
				factor = (UnsignedConstant) factor;
				factor.check(curScope, lib);
			}
		}
		type = factors.get(0).getType();
		for(int i = 0; i < factorOperators.size(); i++) {
			TokenKind tmp = factorOperators.get(i).operator.kind;
			if (tmp == TokenKind.multiplyToken || tmp == TokenKind.divToken || tmp == TokenKind.modToken) {
				factors.get(i).getType().checkType(lib.intType, "left " + tmp + " operand", this, "Type is not integer for " +tmp+ " operator.");
				factors.get(i + 1).getType().checkType(lib.intType, "right " + tmp + " operand", this, "Type is not integer for " +tmp+ " operator.");
			}else{
				factors.get(i).getType().checkType(lib.boolType, "left " + tmp + " operand", this, "Type is not boolean for and-operator.");
				factors.get(i + 1).getType().checkType(lib.boolType, "right " +tmp+ " operand", this, "Type is not boolean for and-operator.");
			}
		}
	}

	@Override
	public void genCode(CodeFile f) {
		for(Factor factor : factors) {
			//System.out.println("fac: " + factor);
			if(factor instanceof Negation) {
				//System.out.println("neg");
				factor = (Negation) factor;
				factor.genCode(f);
			}
			if(factor instanceof InnerExpr) {
				//System.out.println("inner");
				factor = (InnerExpr) factor;
				factor.genCode(f);
			}
			if(factor instanceof Variable) {
				System.out.println("varia");
				factor = (Variable) factor;
				factor.genCode(f);
			}
			if(factor instanceof FuncCall) {
				//System.out.println("funccall");
				factor = (FuncCall) factor;
				factor.genCode(f);
			}
			if(factor instanceof UnsignedConstant) {
				System.out.println("const act");
				factor = (UnsignedConstant) factor;
				factor.genCode(f);
			}
		}
	}

	@Override
	void prettyPrint() {
		for(int i = 0; i < factors.size(); i++) {
			factors.get(i).prettyPrint();
			if (i < factorOperators.size()) {
				factorOperators.get(i).prettyPrint();
				Main.log.prettyPrint(" ");
			}
		}
	}
}