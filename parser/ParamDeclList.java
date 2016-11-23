package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

import java.util.ArrayList;

class ParamDeclList extends PascalSyntax{
	
	ArrayList<ParamDecl> paramDeclarations = new ArrayList<ParamDecl>();

	ParamDeclList(int lNum) {
		super(lNum);
	}


	static ParamDeclList parse(Scanner s) {
		enterParser("param decl list");
		s.skip(leftParToken);
		ParamDeclList paramDeclList = new ParamDeclList(s.curLineNum());
		boolean condition = true;
		while (condition) {
			paramDeclList.paramDeclarations.add(ParamDecl.parse(s));
			if (s.curToken.kind != semicolonToken) condition = false;
			else s.skip(semicolonToken);	
		}
		s.skip(rightParToken);
		leaveParser("param decl list");
		return paramDeclList;
	}

	@Override 
	public String identify() {
		return "<param decl list> on line " + lineNum;
	}

	@Override
	void check(Block curScope, Library lib) {
		for (ParamDecl paramDecl : paramDeclarations) {
			paramDecl.check(curScope, lib);
		}
	}

	@Override
	void prettyPrint() {
		Main.log.prettyPrint("(");
		for (int i = 0; i < paramDeclarations.size(); i++) {
			paramDeclarations.get(i).prettyPrint();
			if (i < paramDeclarations.size() - 1) Main.log.prettyPrint("; ");

		}
		Main.log.prettyPrint(")");
	}
}