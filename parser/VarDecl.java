package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

class VarDecl extends PascalDecl{
	
	Type typeParser;

	VarDecl(String name, int lNum) {
		super(name, lNum);
	}


	static VarDecl parse(Scanner s) {
		enterParser("var decl");
		VarDecl varDecl = new VarDecl(s.curToken.id, s.curLineNum());
		s.skip(nameToken);
		s.skip(colonToken);
		varDecl.typeParser = Type.parse(s);
		s.skip(semicolonToken);
		leaveParser("var decl");
		return varDecl;
	}

	@Override 
	public String identify() {
		return "<var decl> " + name + " on line " + lineNum;
	}

	@Override
	void check(Block curScope, Library lib) {
		curScope.variableBytes += 4; // each var needs 4 bytes in memory.
		typeParser.check(curScope, lib);
		if(typeParser.arrayType != null) {
			arrayDecl = new types.ArrayType(typeParser.arrayType.typeP.type, typeParser.arrayType.from.type, typeParser.arrayType.from.constVal, typeParser.arrayType.to.constVal);
		}
		type = typeParser.type;
		declOffset = curScope.variableOffset;
		curScope.variableOffset -= 4;
		declLevel = curScope.blockLevel;
	}

	@Override
	void checkWhetherAssignable(PascalSyntax where) {}

	@Override
	void checkWhetherFunction(PascalSyntax where) {
		where.error("You are calling a VarDecl instead of a function");
	}

	@Override
	void checkWhetherProcedure(PascalSyntax where) {
		where.error("You are calling a VarDecl instead of a procedure.");
	}

	@Override 
	void checkWhetherValue(PascalSyntax where) {}

	@Override
	void prettyPrint() {
		Main.log.prettyPrint(name);
		Main.log.prettyPrint(": ");
		typeParser.prettyPrint();
		Main.log.prettyPrintLn(";");
	}

	@Override
	public void genCode(CodeFile f) {}
}