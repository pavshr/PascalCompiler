package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

class IfStatm extends Statement {
	
	Expression expression;
	Statement statement, elseStatement;

	IfStatm(int lNum) {
		super(lNum);
	}


	static IfStatm parse(Scanner s) {
		enterParser("if statm");
		IfStatm ifStatm = new IfStatm(s.curLineNum());
		s.skip(ifToken);
		ifStatm.expression = Expression.parse(s);
		s.skip(thenToken);
		ifStatm.statement = Statement.parse(s);
		if (s.curToken.kind == elseToken) {
			s.skip(elseToken);
			ifStatm.elseStatement = Statement.parse(s);
		}
		leaveParser("if statm");
		return ifStatm;
	}

	@Override 
	public String identify() {
		return "<if-statm> on line " + lineNum;
	}

	@Override
	void check(Block curScope, Library lib) {
		expression.check(curScope, lib);
		expression.type.checkType(lib.boolType, "if-test", this, "If-test is not Boolean.");
		statement.check(curScope, lib);
		if (elseStatement != null) elseStatement.check(curScope, lib);
	}

	@Override
	public void genCode(CodeFile f) {
		String startLabel = f.getLocalLabel();
		String endLabel = null; 
		f.genInstr("", "", "", "Start if-statement");
		expression.genCode(f);
		f.genInstr("", "cmpl", "$0,%eax", "");
		f.genInstr("", "je", startLabel, "");
		statement.genCode(f);
		if (elseStatement != null) {
			endLabel = f.getLocalLabel();
			f.genInstr("", "jmp", endLabel, "");
		}
		f.genInstr(startLabel, "", "", "");
		if (elseStatement != null) {
			elseStatement.genCode(f);
			f.genInstr(endLabel, "", "", "End if-statement");
		}
	}

	@Override
	void prettyPrint() {
		Main.log.prettyPrint("if ");
		expression.prettyPrint();
		Main.log.prettyPrintLn(" then ");
		Main.log.prettyIndent();
		statement.prettyPrint();
		Main.log.prettyOutdent();
		if (elseStatement != null) {
			Main.log.prettyPrintLn("");
			Main.log.prettyPrintLn("else");
			Main.log.prettyIndent();
			elseStatement.prettyPrint();
			Main.log.prettyOutdent();
		}
	}
}