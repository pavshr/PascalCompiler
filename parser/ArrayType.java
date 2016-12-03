package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

class ArrayType extends Type{
	
	Constant from, to;
	Type typeP;

	types.ArrayType type;

	ArrayType(int lNum) {
		super(lNum);
	}

	static ArrayType parse(Scanner s) {
		enterParser("array type");
		s.skip(arrayToken);
		s.skip(leftBracketToken);
		ArrayType arrayType = new ArrayType(s.curLineNum());
		arrayType.from = Constant.parse(s);
		s.skip(rangeToken);
		arrayType.to = Constant.parse(s);
		s.skip(rightBracketToken);
		s.skip(ofToken);
		arrayType.typeP = Type.parse(s);
		leaveParser("array type");
		return arrayType;
	}

	@Override 
	public String identify() {
		return "<array type> on line " + lineNum;
	}

	@Override
	void check(Block curScope, Library lib) {
		from.check(curScope, lib);
		to.check(curScope, lib);
		typeP.check(curScope, lib);
		type = new types.ArrayType(typeP.type, from.type, from.constVal, to.constVal);
		from.type.checkType(to.type, "array limits", this, "array limits are of different type");
	}

	@Override
	public void genCode(CodeFile f) {

	}

	@Override
	void prettyPrint() {
		Main.log.prettyPrint("array ");
		Main.log.prettyPrint("[");
		from.prettyPrint();
		Main.log.prettyPrint("..");
		to.prettyPrint();
		Main.log.prettyPrint("]");
		Main.log.prettyPrint(" of ");
		typeP.prettyPrint();
	}
}