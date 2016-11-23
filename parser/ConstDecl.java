package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

class ConstDecl extends PascalDecl {
	
	Constant constant;
	int constVal;
	
	ConstDecl(String name, int lNum) {
		super(name, lNum);
	}


	static ConstDecl parse(Scanner s) {
		enterParser("<const decl>");
		String name = s.curToken.id;
		s.skip(nameToken);
		ConstDecl constDecl = new ConstDecl(name, s.curLineNum());
		s.skip(equalToken);
		constDecl.constant = Constant.parse(s);
		s.skip(semicolonToken);
		leaveParser("<const decl>");
		return constDecl;
	}

	@Override 
	public String identify() {
		return "<const decl> " + name + " on line " + lineNum;
	}

	@Override
	void check(Block curScope, Library lib) {
		constant.check(curScope, lib);
		type = constant.type;
		constVal = constant.constVal;
	}

	@Override
	void checkWhetherAssignable(PascalSyntax where) {
		where.error("You can not assign to a constant.");
	}

	@Override
	void checkWhetherFunction(PascalSyntax where) {
		where.error("You are calling a constant instead of a function");
	}

	@Override
	void checkWhetherProcedure(PascalSyntax where) {
		where.error("You are calling a constant instead of a procedure.");
	}

	@Override 
	void checkWhetherValue(PascalSyntax where) {}

	@Override
	void prettyPrint() {
		Main.log.prettyPrint(name + " = ");
		constant.prettyPrint();
		Main.log.prettyPrint("; ");
	}
}