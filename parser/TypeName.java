package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

class TypeName extends Type {
	String name;
	TypeDecl typeRef;

	TypeName(int lNum) {
		super(lNum);
	}

	static TypeName parse(Scanner s) {
		enterParser("type name");
		TypeName typeName = new TypeName(s.curLineNum());
		typeName.name = s.curToken.id;
		s.skip(nameToken);
		leaveParser("type name");
		return typeName;
	}

	@Override 
	public String identify() {
		return "<type name> on line " + lineNum;
	}

	@Override
	void check(Block curScope, Library lib) {
		PascalDecl d = lib.findDecl(name, this);
		typeRef = (TypeDecl) d;
		type = typeRef.type;
	}

	@Override
	void prettyPrint() {
		Main.log.prettyPrint(name);
	}
}