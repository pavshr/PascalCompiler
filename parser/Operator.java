package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

abstract class Operator extends PascalSyntax {
	
	Token operator;

	Operator(int lNum) {
		super(lNum);
	}

	@Override 
	public String identify() {
		return "<operator token> on line " + lineNum;
	}

	@Override
	void prettyPrint() {
	}
}