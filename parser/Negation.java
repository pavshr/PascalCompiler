package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

class Negation extends Factor {
	
	Factor factor;

	types.Type type;

	Negation(int lNum) {
		super(lNum);
	}


	static Negation parse(Scanner s) {
		enterParser("negation");
		Negation negation = new Negation(s.curLineNum());
		s.skip(notToken);
		negation.factor = Factor.parse(s);
		leaveParser("negation");
		return negation;
	}

	@Override 
	public String identify() {
		return "<negation> on line " + lineNum;
	}

	@Override
	void check(Block curScope, Library lib) {
		factor.check(curScope, lib);
		factor.getType().checkType(lib.boolType, "not", this, "Negation is not used with boolean.");
		type = lib.boolType;
	}

	@Override
	public void genCode(CodeFile f) {
		factor.genCode(f);
		f.genInstr("", "xorl", "$0x1,%eax", "not");
	}

	@Override
	public types.Type getType() {
		return type;
	}

	@Override
	void prettyPrint() {
		Main.log.prettyPrint("not ");
		factor.prettyPrint();

	}
}