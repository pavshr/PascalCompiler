package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

class Variable extends Factor {
	
	String name;
	Expression expression;
	PascalDecl ref;

	types.Type type;

	Variable(int lNum) {
		super(lNum);
	}


	static Variable parse(Scanner s) {
		enterParser("variable");
		Variable variable = new Variable(s.curLineNum());
		variable.name = s.curToken.id;
		s.skip(nameToken);
		if(s.curToken.kind == leftBracketToken) {
			s.skip(leftBracketToken);
			variable.expression = Expression.parse(s);
			s.skip(rightBracketToken);
		}
		leaveParser("variable");
		return variable;
	}

	@Override 
	public String identify() {
		return "<variable> " + name + " on line " + lineNum;
	}


	@Override
	void check(Block curScope, Library lib) {
		PascalDecl d = curScope.findDecl(name, this);
		d.checkWhetherValue(this);
		if(expression != null) {
			expression.check(curScope, lib);
		}
		ref = d;
		type = ref.type;
		if (expression != null) {
			ref.arrayDecl.indexType.checkType(expression.type, "array index", this, "array index is not matching.");
		}
	}

	@Override
	public types.Type getType() {
		return type;
	}

	@Override
	void prettyPrint() {
		Main.log.prettyPrint(name);
		if (expression != null)  {
			Main.log.prettyPrint("[");
			expression.prettyPrint();
			Main.log.prettyPrint("]");
		}
	}
}