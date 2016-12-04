package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

import java.util.ArrayList;
import java.util.HashMap;

class Block extends PascalSyntax{
	ConstDeclPart constDeclPart;
	VarDeclPart varDeclPart;
	ArrayList<ProcDecl> declarations = new ArrayList<ProcDecl>();
	StatmList statmList;

	HashMap<String, PascalDecl> decls = new HashMap<String, PascalDecl>();

	Block outerScope;

	int blockLevel; 
	int variableBytes = 0;
	int variableOffset = -36;

	Block(int lNum) {
		super(lNum);
	}

	static Block parse(Scanner s) {
		enterParser("block");
		Block block = new Block(s.curLineNum());
		switch (s.curToken.kind) {
			case constToken:
				block.constDeclPart = ConstDeclPart.parse(s);
			case varToken:
				block.varDeclPart = VarDeclPart.parse(s);
				break;
			default:
				break;
		}
		boolean condition = true;
		while (condition) {
			switch (s.curToken.kind) {
				case functionToken:
					block.declarations.add(FuncDecl.parse(s));
					break;
				case procedureToken:
					block.declarations.add(ProcDecl.parse(s));
					break;
				default:
					condition = false;
					break;
			}
		}
		s.skip(beginToken);
		block.statmList = StatmList.parse(s);
		s.skip(endToken);
		leaveParser("block");
		return block;
	}

	@Override 
	public String identify() {
		return "<block> on line " + lineNum;
	}

	@Override
	void prettyPrint() {
		if (constDeclPart != null) constDeclPart.prettyPrint();
		if (varDeclPart != null) varDeclPart.prettyPrint();
		for (PascalDecl pd : declarations) {
			Main.log.prettyPrintLn("");
			pd.prettyPrint();
			Main.log.prettyPrintLn(" {" + pd.name + "}");
					Main.log.prettyPrintLn("");
		}

		Main.log.prettyPrintLn("begin");
		Main.log.prettyIndent();
		statmList.prettyPrint();
		Main.log.prettyOutdent();
		Main.log.prettyPrint("end");
	}

	void addDecl(String id, PascalDecl d) {
		if(decls.containsKey(id)) d.error(id + " declared twice in same block!");
		decls.put(id, d);
	}

	PascalDecl findDecl(String id, PascalSyntax where) {
		PascalDecl d = decls.get(id);
		if (d != null) {
			Main.log.noteBinding(id, where, d);
			return d;
		}
		if (outerScope != null) return outerScope.findDecl(id, where);

		where.error("Name " + id + " is unknown!");
		return null;
	}


	@Override
	void check(Block curScope, Library lib) {
		blockLevel = curScope.blockLevel + 1;
		outerScope = curScope;
		// ConstDeclPart
		if(constDeclPart != null) {
			for (ConstDecl constDecl : constDeclPart.constDeclarations) {
				addDecl(constDecl.name, constDecl);
			}
			constDeclPart.check(this, lib);
		}

		//VarDeclPart
		if(varDeclPart != null) {
			for (VarDecl varDecl : varDeclPart.varDeclarations) {
				addDecl(varDecl.name, varDecl);
			}
			varDeclPart.check(this, lib);
		}

		//Func/ProcDecl
		for (ProcDecl procDecl : declarations) {
			if (procDecl instanceof FuncDecl) {
				procDecl = (FuncDecl) procDecl;
				procDecl.check(this, lib);
			}else{
				procDecl.check(this, lib);
			}
		} 
		//StatmList
		statmList.check(this, lib);
	}
	@Override
	public void genCode(CodeFile f) {
		statmList.genCode(f);
	}
}