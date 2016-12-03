package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

import java.util.ArrayList;

class FuncCall extends Factor {
	
	String name;
	ArrayList<Expression> expressions = new ArrayList<Expression>();
	FuncDecl funcRef;

	types.Type type;

	FuncCall(int lNum) {
		super(lNum);
	}

	static FuncCall parse(Scanner s) {
		enterParser("func call");
		FuncCall funcCall = new FuncCall(s.curLineNum());
		funcCall.name = s.curToken.id;
		s.skip(nameToken);
		if (s.curToken.kind == leftParToken) {
			s.skip(leftParToken);
			boolean condition = true;
			while(condition) {
				funcCall.expressions.add(Expression.parse(s));
				if(s.curToken.kind == commaToken) s.skip(commaToken);
				if(s.curToken.kind == rightParToken) condition = false;
			}
			s.skip(rightParToken);
		}
		leaveParser("func call");
		return funcCall;
	}

	@Override
	void check(Block curScope, Library lib) {
		PascalDecl d = curScope.findDecl(name, this);
		d.checkWhetherFunction(this);
		for (Expression expression : expressions) {
			expression.check(curScope, lib);
		}
		funcRef = (FuncDecl) d;
		type = funcRef.type;
		for (int i = 0; i < expressions.size(); i++) {
			expressions.get(i).type.checkType(funcRef.paramDeclList.paramDeclarations.get(i).type, "param #" + (i+1), this, "Param in ProcCall is not the same type as in FuncDecl.");
		}
	}

	@Override
	public void genCode(CodeFile f) {

	}

	@Override
	public types.Type getType() {
		return type;
	}

	@Override
	void prettyPrint() {
		Main.log.prettyPrint(name);
		Main.log.prettyPrint("(");
		for (int i = 0; i < expressions.size(); i++) {
			expressions.get(i).prettyPrint();
			if(i < expressions.size() - 1) Main.log.prettyPrint(", ");
		}
		Main.log.prettyPrint(")");
	}
}