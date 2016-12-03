// NB! from Kompendium page 43.

package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

/* <while-statm> ::= ’while’ <expression> ’do’ <statement> */
public class WhileStatm extends Statement {
	Expression expr;
	Statement body;

	WhileStatm(int lNum) {
		super(lNum);
	}

		static WhileStatm parse(Scanner s) {
		enterParser("while-statm");

		WhileStatm ws = new WhileStatm(s.curLineNum());
		s.skip(whileToken);

		ws.expr = Expression.parse(s);
		s.skip(doToken);
		ws.body = Statement.parse(s);

		leaveParser("while-statm");
		return ws;
	}

	@Override 
	public String identify() {
		return "<while-statm> on line " + lineNum;
	}

	@Override
	void check(Block curScope, Library lib) {
		expr.check(curScope, lib);
		expr.type.checkType(lib.boolType, "while-test", this,
            "While-test is not Boolean.");
		body.check(curScope, lib);
	}

	@Override
	public void genCode(CodeFile f) {
		String testLabel = f.getLocalLabel(),
               endLabel  = f.getLocalLabel();
        f.genInstr(testLabel, "", "", "Start while-statement");
        expr.genCode(f);
        f.genInstr("", "cmpl", "$0,%eax", "");
        f.genInstr("", "je", endLabel, "");
        body.genCode(f);
        f.genInstr("", "jmp", testLabel, "");
        f.genInstr(endLabel, "", "", "End while-statement");
	}

	@Override 
	void prettyPrint() {
		Main.log.prettyPrint("while ");
		expr.prettyPrint();
		Main.log.prettyPrintLn(" do");
		Main.log.prettyIndent();
		body.prettyPrint();
		Main.log.prettyOutdent();
	}
}