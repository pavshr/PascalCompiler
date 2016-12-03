package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

class CompoundStatm extends Statement {
	
	StatmList statmList;

	CompoundStatm(int lNum) {
		super(lNum);
	}

	static CompoundStatm parse(Scanner s) {
		enterParser("compound statm");
		CompoundStatm compoundStatm = new CompoundStatm(s.curLineNum());
		s.skip(beginToken);
		compoundStatm.statmList = StatmList.parse(s);
		s.skip(endToken);
		leaveParser("compound statm");
		return compoundStatm;
	}

	@Override 
	public String identify() {
		return "<compound statm> on line " + lineNum;
	}

	@Override
	void check(Block curScope, Library lib) {
		statmList.check(curScope, lib);
	}

	@Override
	public void genCode(CodeFile f) {
		statmList.genCode(f);
	}

	@Override
	void prettyPrint() {
		Main.log.prettyPrintLn("begin");
		Main.log.prettyIndent();
		statmList.prettyPrint();
		Main.log.prettyOutdent();
		Main.log.prettyPrint("end");

	}
}