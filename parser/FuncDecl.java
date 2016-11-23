package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

class FuncDecl extends ProcDecl {

	FuncDecl(String name, int lNum) {
		super(name, lNum);
	}


	static FuncDecl parse(Scanner s) {
		enterParser("func decl");
		s.skip(functionToken);
		FuncDecl funcDecl = new FuncDecl(s.curToken.id, s.curLineNum());
		s.skip(nameToken);
		if (s.curToken.kind == leftParToken) funcDecl.paramDeclList = ParamDeclList.parse(s);
		s.skip(colonToken);
		funcDecl.typeName = TypeName.parse(s);
		s.skip(semicolonToken);
		funcDecl.block = Block.parse(s);
		s.skip(semicolonToken);
		leaveParser("func decl");
		return funcDecl;
	}

	@Override 
	public String identify() {
		return "<func decl> on line " + lineNum;
	}

	@Override
	void check(Block curScope, Library lib) {
		curScope.addDecl(name, this);
		if (paramDeclList != null) paramDeclList.check(block, lib);
		typeName.check(curScope, lib);
		type = typeName.typeRef.type;
		block.check(curScope, lib);
	}

	@Override
	void checkWhetherAssignable(PascalSyntax where) {}

	@Override
	void checkWhetherFunction(PascalSyntax where) {}

	@Override
	void checkWhetherProcedure(PascalSyntax where) {
		where.error("Yoy are calling a function instead of procedure.");
	}

	@Override 
	void checkWhetherValue(PascalSyntax where) {}

	@Override
	void prettyPrint() {
		Main.log.prettyPrint("function " + name + " ");
		if(paramDeclList != null) paramDeclList.prettyPrint();
		Main.log.prettyPrint(": ");
		typeName.prettyPrint();
		Main.log.prettyPrintLn(";");
		block.prettyPrint();
		Main.log.prettyPrint(";");
	}
}