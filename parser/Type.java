package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

class Type extends PascalSyntax{
	TypeName typeName;
	ArrayType arrayType;

	types.Type type;

	Type(int lNum) {
		super(lNum);
	}


	static Type parse(Scanner s) {
		enterParser("type");
		Type type = new Type(s.curLineNum());
		if (s.curToken.kind == nameToken) type.typeName = TypeName.parse(s);
		if (s.curToken.kind == arrayToken) type.arrayType = ArrayType.parse(s);
		leaveParser("type");
		return type;
	}

	@Override 
	public String identify() {
		return "<type> on line " + lineNum;
	}

	@Override
	void check(Block curScope, Library lib) {
		if(typeName != null) {
			typeName.check(curScope, lib);
			type = typeName.type;
		}
		
		if(arrayType != null) {
			arrayType.check(curScope, lib);
			type = arrayType.typeP.type;
		}
	}

	@Override
	public void genCode(CodeFile f) {

	}

	@Override
	void prettyPrint() {
		if(typeName != null) typeName.prettyPrint();
		if(arrayType != null) arrayType.prettyPrint();
	}
}