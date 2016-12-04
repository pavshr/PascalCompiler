package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

import java.util.ArrayList;

class VarDeclPart extends PascalSyntax {
	
	ArrayList<VarDecl> varDeclarations = new ArrayList<VarDecl>();

	VarDeclPart(int lNum) {
		super(lNum);
	}

	static VarDeclPart parse(Scanner s) {
		enterParser("var decl part");
		s.skip(varToken);
		VarDeclPart varDeclPart = new VarDeclPart(s.curLineNum());
		boolean condition = true;
		while (condition) {
			varDeclPart.varDeclarations.add(VarDecl.parse(s));
			if (s.curToken.kind != nameToken) condition = false;
		}
		leaveParser("var decl part");
		return varDeclPart;
	}

	@Override 
	public String identify() {
		return "<var decl part> on line " + lineNum;
	}

	@Override
	void check(Block curScope, Library lib) {
		for (VarDecl varDecl : varDeclarations) {
			varDecl.check(curScope, lib);
		}
	}

	@Override
	public void genCode(CodeFile f) {
		for (VarDecl varDecl : varDeclarations) {
			varDecl.genCode(f);
		}
	}

	@Override
	void prettyPrint() {
		Main.log.prettyPrintLn("var");
		Main.log.prettyIndent();
		for (VarDecl vd : varDeclarations) {
			vd.prettyPrint();
		}
		Main.log.prettyOutdent();
	}
}