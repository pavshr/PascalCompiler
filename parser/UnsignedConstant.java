package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

class UnsignedConstant extends Constant {
	String name;
	NumericLiteral numericLiteral;
	CharLiteral charLiteral;

	PascalDecl ref;

	UnsignedConstant(int lNum) {
		super(lNum);
	}


	static UnsignedConstant parse(Scanner s) {
		enterParser("unsigned const");
		UnsignedConstant unsignedConstant = new UnsignedConstant(s.curLineNum());
		switch (s.curToken.kind) {
			case nameToken:
				unsignedConstant.name = s.curToken.id;
				s.skip(nameToken);
				break;
			case intValToken:
				unsignedConstant.numericLiteral = NumericLiteral.parse(s);
				break;
			case charValToken:
				unsignedConstant.charLiteral = CharLiteral.parse(s);
				break;
			default: 
				break;
		}
		leaveParser("unsigned const");
		return unsignedConstant;
	}

	@Override 
	public String identify() {
		return "unsigned constant on line " + lineNum;
	}

	@Override
	void check(Block curScope, Library lib) {
		if (name != null ) {
			PascalDecl d = curScope.findDecl(name, this);
			ref = d;
			type = ref.type;
			constVal = fromStringToInt(name);
		}
		if(charLiteral != null) {
			charLiteral.check(curScope, lib);
			type = lib.charType;
		}
		if(numericLiteral != null) {
			numericLiteral.check(curScope, lib);
			type = lib.intType;
		}
	}

	@Override
	public void genCode(CodeFile f) {
		if (name != null) {

		}
		if(charLiteral != null) {
			charLiteral.genCode(f);
		}
		if(numericLiteral != null) {
			numericLiteral.genCode(f);
		}
	}

	@Override
	public types.Type getType() {
		return type;
	}

	@Override
	void prettyPrint() {
		if (name != null) Main.log.prettyPrint(name);
		if (numericLiteral != null) numericLiteral.prettyPrint();
		if (charLiteral != null) charLiteral.prettyPrint();
	}

	public int fromStringToInt(String s) {
		s = s.toLowerCase();
		char[] array = s.toCharArray();
		int intVal = 0;
		for (int i = 0; i < array.length; i++) {
			intVal += (int) array[i];
		}
		return intVal;
	}
}