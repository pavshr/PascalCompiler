package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

import java.util.ArrayList;

class StatmList extends PascalSyntax{
	
	ArrayList<Statement> statementList = new ArrayList<Statement>();

	StatmList(int lNum) {
		super(lNum);
	}


	static StatmList parse(Scanner s) {
		enterParser("statm list");
		StatmList statmList = new StatmList(s.curLineNum());
		boolean condition = true;
		while (condition) {
			statmList.statementList.add(Statement.parse(s));
			if(s.curToken.kind != semicolonToken) condition = false;
			else s.skip(semicolonToken);
		}
		leaveParser("statm list");
		return statmList;
	}

	@Override 
	public String identify() {
		return "<statm list> on line " + lineNum;
	}

	@Override
	void check(Block curScope, Library lib) {
		for (Statement statm : statementList) {
			if (statm instanceof CompoundStatm) {
				statm = (CompoundStatm) statm;
				statm.check(curScope, lib);
			}
			if (statm instanceof IfStatm) {
				statm = (IfStatm) statm;
				statm.check(curScope, lib);
			}
			if (statm instanceof AssignStatm) {
				statm = (AssignStatm) statm;
				statm.check(curScope, lib);
			}
			if (statm instanceof ProcCall) {
				statm = (ProcCall) statm;
				statm.check(curScope, lib);
			}
			if (statm instanceof WhileStatm) {
				statm = (WhileStatm) statm;
				statm.check(curScope, lib);
			}
			if (statm instanceof EmptyStatm) {
				statm = (EmptyStatm) statm;
				statm.check(curScope, lib);
			}
		}
	}

	@Override
	public void genCode(CodeFile f) {
		for (Statement statm : statementList) {
			if (statm instanceof CompoundStatm) {
				statm = (CompoundStatm) statm;
				statm.genCode(f);
			}
			if (statm instanceof IfStatm) {
				statm = (IfStatm) statm;
				statm.genCode(f);
			}
			if (statm instanceof AssignStatm) {
				statm = (AssignStatm) statm;
				statm.genCode(f);
			}
			if (statm instanceof ProcCall) {
				statm = (ProcCall) statm;
				statm.genCode(f);
			}
			if (statm instanceof WhileStatm) {
				statm = (WhileStatm) statm;
				statm.genCode(f);
			}
			if (statm instanceof EmptyStatm) {
				statm = (EmptyStatm) statm;
				statm.genCode(f);
			}
		}
	}

	@Override
	void prettyPrint() {
		for (int i = 0; i < statementList.size(); i++) {
			statementList.get(i).prettyPrint();
			if(i < statementList.size() - 1) Main.log.prettyPrintLn(";");
		}
		Main.log.prettyPrintLn("");
	}
}