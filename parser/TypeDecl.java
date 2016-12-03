package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

class TypeDecl extends PascalDecl {
	TypeDecl(String name, int lNum) {
		super(name, lNum);
	}

	static TypeDecl parse(Scanner s) {
		enterParser("type decl");
		TypeDecl typeDecl = new TypeDecl(s.curToken.id, s.curLineNum());
		s.skip(nameToken);
		leaveParser("type decl");
		return typeDecl;
	}

	@Override
	public String identify() {
		return "<type decl> on line " + lineNum;
	}

	@Override
	void check(Block curScope, Library lib) {}

	@Override
	public void genCode(CodeFile f) {

	}

	@Override
	void checkWhetherAssignable(PascalSyntax where) {
		where.error("You cannot assign to the TypeDecl");
	}

	@Override
	void checkWhetherFunction(PascalSyntax where) {
		where.error("You are calling a TypeDecl instead of a function.");
	}

	@Override
	void checkWhetherProcedure(PascalSyntax where) {
		where.error("You are calling a TypeDecl instead of a procedure.");
	}

	@Override 
	void checkWhetherValue(PascalSyntax where) {}

	@Override
	void prettyPrint() {}
}