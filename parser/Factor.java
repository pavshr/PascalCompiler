package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

abstract class Factor extends PascalSyntax {
	
	types.Type type;

	Factor(int lNum) {
		super(lNum);
	}

	static Factor parse(Scanner s) {
		enterParser("factor");
		Factor factor = null;
		switch (s.curToken.kind) { 
			case notToken:
				factor = Negation.parse(s);
				break;
			case leftParToken:
				factor = InnerExpr.parse(s);
				break;
			case nameToken:
				switch (s.nextToken.kind) { 
					case leftBracketToken:
						factor = Variable.parse(s);
						break; 
					case leftParToken:
						factor = FuncCall.parse(s);
						break; 
					default:
						factor = Variable.parse(s);
						break;
				}break;
			default:
				if ((s.curToken.kind != nameToken) && (s.curToken.kind != intValToken) && (s.curToken.kind != charValToken)) {
					Main.error(s.curLineNum(), "Expected a value but found a " + s.curToken.kind + "!");
				}
				factor = UnsignedConstant.parse(s);
				break;
		}
		leaveParser("factor");
		return factor;
	}

	@Override 
	public String identify() {
		return "<factor> on line " + lineNum;
	}

	abstract types.Type getType();

	@Override
	void prettyPrint() {
	}
}