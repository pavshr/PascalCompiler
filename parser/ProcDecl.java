package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

class ProcDecl extends PascalDecl {
	
	ParamDeclList paramDeclList;
	TypeName typeName;
	Block block;

	String procLabel;
	int procParamDecls = 0;


	ProcDecl(String name, int lNum) {
		super(name, lNum);
	}


	static ProcDecl parse(Scanner s) {
		enterParser("proc decl");
		s.skip(procedureToken);
		ProcDecl procDecl = new ProcDecl(s.curToken.id, s.curLineNum());
		s.skip(nameToken);
		if (s.curToken.kind == leftParToken) procDecl.paramDeclList = ParamDeclList.parse(s);
		s.skip(semicolonToken);
		procDecl.block = Block.parse(s);
		s.skip(semicolonToken);
		leaveParser("proc decl");
		return procDecl;
	}

	@Override 
	public String identify() {
		return "<proc decl> on line " + lineNum;
	}

	@Override
	void check(Block curScope, Library lib) {
		declLevel = curScope.blockLevel + 1;
		curScope.addDecl(name, this);
		if (paramDeclList != null) {
			paramDeclList.blockLevel = declLevel;
			paramDeclList.blockOffset = curScope.variableBytes;
			paramDeclList.check(block, lib);
			typeName = paramDeclList.paramDeclarations.get(0).typeName;
			procParamDecls = paramDeclList.paramDeclarations.size();
		}
		block.check(curScope, lib);
	}

	@Override
	public void genCode(CodeFile f) {
		procLabel = f.getLabel(name);
		f.genInstr("proc$" + procLabel, "enter", "$" + (32 + block.variableBytes) + ",$" + declLevel, "Start of " + name);
		if (paramDeclList != null) paramDeclList.genCode(f);
		block.genCode(f);
		f.genInstr("", "leave", "", "End of " + name);
		f.genInstr("", "ret", "", "");
		
	}

	@Override
	void checkWhetherAssignable(PascalSyntax where) {
		where.error("You cannot assign to the procedure");
	}

	@Override
	void checkWhetherFunction(PascalSyntax where) {
		where.error("You are calling a procedure instead of a function");
	}

	@Override
	void checkWhetherProcedure(PascalSyntax where) {}

	@Override 
	void checkWhetherValue(PascalSyntax where) {}

	@Override
	void prettyPrint() {
		Main.log.prettyPrint("procedure " + name);
		if(paramDeclList != null) {
			Main.log.prettyPrint(" ");
			paramDeclList.prettyPrint();
		}
		Main.log.prettyPrintLn(";");
		block.prettyPrint();
		Main.log.prettyPrint(";");
	}
}