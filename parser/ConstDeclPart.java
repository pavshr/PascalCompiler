package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

import java.util.ArrayList;

class ConstDeclPart extends PascalSyntax {
	ArrayList<ConstDecl> constDeclarations = new ArrayList<ConstDecl>();
	int constVal = 0; //maybe not needed.

	ConstDeclPart(int lNum) {
		super(lNum);
	}


	static ConstDeclPart parse(Scanner s) {
		enterParser("<const decl part>");
		s.skip(constToken);
		ConstDeclPart constDeclPart = new ConstDeclPart(s.curLineNum());
		boolean condition = true;
		while (condition) {
			constDeclPart.constDeclarations.add(ConstDecl.parse(s));
			if (s.curToken.kind != nameToken) condition = false;
		}
		leaveParser("const decl part");
		return constDeclPart;
	}

	@Override 
	public String identify() {
		return "<const decl part> on line " + lineNum;
	}
	
	@Override
	void check(Block curScope, Library lib) {
		for (ConstDecl constDecl : constDeclarations) {
			constDecl.check(curScope, lib);
			constVal += constDecl.constVal;
		}
	}

	@Override
	void prettyPrint() {
		Main.log.prettyPrintLn("const");
		Main.log.prettyIndent();
		for (ConstDecl cd : constDeclarations) {
			cd.prettyPrint();
			Main.log.prettyPrintLn("");
		}
		Main.log.prettyOutdent();

	}
}