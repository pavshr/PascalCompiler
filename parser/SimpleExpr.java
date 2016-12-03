package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

import java.util.ArrayList;

class SimpleExpr extends PascalSyntax {
	
	PrefixOpr prefixOpr;
	ArrayList<Term> terms = new ArrayList<Term>();
	ArrayList<TermOpr> termOperators = new ArrayList<TermOpr>();

	types.Type type;

	SimpleExpr(int lNum) {
		super(lNum);
	}


	static SimpleExpr parse(Scanner s) {
		enterParser("simple expr");
		SimpleExpr simpleExpr = new SimpleExpr(s.curLineNum());
		if(s.curToken.kind.isPrefixOpr()) simpleExpr.prefixOpr = PrefixOpr.parse(s);
		boolean condition = true;
		while (condition) {
			simpleExpr.terms.add(Term.parse(s));
			if(s.curToken.kind.isTermOpr()) {
				simpleExpr.termOperators.add(TermOpr.parse(s));
			}else{
				condition = false;
			}
		}
		leaveParser("simple expr");
		return simpleExpr;
	}

	@Override 
	public String identify() {
		return "<simple expr> on line " + lineNum;
	}
	
	@Override
	void check(Block curScope, Library lib) {

		for (Term term : terms) {
			term.check(curScope, lib);
		}
		for(int i = 0; i < termOperators.size(); i++) {
			TokenKind tmp = termOperators.get(i).operator.kind;
			if (tmp == TokenKind.addToken || tmp == TokenKind.subtractToken) {
				terms.get(i).type.checkType(lib.intType, "left " + tmp + " operand", this, "Type is not integer for " +tmp+ " operator.");
				terms.get(i + 1).type.checkType(lib.intType, "right " + tmp + " operand", this, "Type is not integer for " +tmp+ " operator.");
			}else{
				terms.get(i).type.checkType(lib.boolType, "left " + tmp + " operand", this, "Type is not boolean for and-operator.");
				terms.get(i + 1).type.checkType(lib.boolType, "right " +tmp+ " operand", this, "Type is not boolean for and-operator.");
			}
		}

		if (prefixOpr != null) {
			String oprName = prefixOpr.operator.kind.toString();
			terms.get(0).type.checkType(lib.intType, "prefix " + oprName, this, "Prefix operator is not used with integer.");
		}
		type = terms.get(0).type;
	}

	@Override
	public void genCode(CodeFile f) {
		if (prefixOpr != null) prefixOpr.genCode(f);
		for(int i = 0; i < terms.size(); i++) {
			terms.get(i).genCode(f);
			if (i < termOperators.size()) {
				termOperators.get(i).genCode(f); // fix it
			}
		}
	}


	@Override
	void prettyPrint() {
		if (prefixOpr != null) prefixOpr.prettyPrint();
		for(int i = 0; i < terms.size(); i++) {
			terms.get(i).prettyPrint();
			if (i < termOperators.size()) {
				termOperators.get(i).prettyPrint();
				Main.log.prettyPrint(" ");
			}
		}

	}
}