package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

class NumericLiteral extends UnsignedConstant {
	
	int intVal;
	types.Type type;

	NumericLiteral(int lNum) {
		super(lNum);
	}


	static NumericLiteral parse(Scanner s) {
		enterParser("number literal");
		NumericLiteral numericLiteral = new NumericLiteral(s.curLineNum());
		numericLiteral.intVal = s.curToken.intVal;
		s.skip(intValToken);
		leaveParser("number literal");
		return numericLiteral;
	}

	@Override 
	public String identify() {
		return "number literal on line " + lineNum;
	}

	@Override
	void check(Block curScope, Library lib) {
		constVal = intVal;
		type = lib.intType;
	}

	@Override
	void prettyPrint() {
		Main.log.prettyPrint("" + intVal);
	}
}