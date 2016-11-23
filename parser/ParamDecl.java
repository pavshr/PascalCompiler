package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

class ParamDecl extends PascalDecl {
	
	TypeName typeName;

	ParamDecl(String name, int lNum) {
		super(name, lNum);
	}


	static ParamDecl parse(Scanner s) {
		enterParser("param decl");
		ParamDecl paramDecl = new ParamDecl(s.curToken.id, s.curLineNum());
		s.skip(nameToken);
		s.skip(colonToken);
		paramDecl.typeName = TypeName.parse(s);
		leaveParser("param decl");
		return paramDecl;
	}

	@Override 
	public String identify() {
		return "<param decl> on line " + lineNum;
	}

	@Override
	void check(Block curScope, Library lib) {
		curScope.addDecl(name, this);
		//System.out.println("added a param " + name + "on line " +lineNum+ " in " + curScope.identify());
		typeName.check(curScope, lib);
		type = typeName.typeRef.type;
	}

	@Override
	void checkWhetherAssignable(PascalSyntax where) {
		where.error("You cannot assign to the parameter");
	}

	@Override
	void checkWhetherFunction(PascalSyntax where) {
		where.error("You are calling a parameter instead of a function");
	}

	@Override
	void checkWhetherProcedure(PascalSyntax where) {
		where.error("You are calling a parameter instead of a procedure.");
	}

	@Override 
	void checkWhetherValue(PascalSyntax where) {}

	@Override
	void prettyPrint() {
		Main.log.prettyPrint(name + ": ");
		typeName.prettyPrint();
	}
}