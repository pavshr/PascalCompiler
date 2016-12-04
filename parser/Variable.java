package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

class Variable extends Factor {
	
	String name;
	Expression expression;
	PascalDecl ref;
	ConstDecl constDeclRef;

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
		if (ref instanceof ConstDecl) {
			constDeclRef = (ConstDecl) ref;
		}
		type = ref.type;
		if (expression != null) {
			ref.arrayDecl.indexType.checkType(expression.type, "array index", this, "array index is not matching.");
		}
	}

	@Override
	public void genCode(CodeFile f) {
		System.out.println("var ref is " + ref.type + " " + name);
		if (ref instanceof VarDecl) {
			f.genInstr("", "movl", (-4 * ref.declLevel) + "(%ebp),%edx", "");
			f.genInstr("", "movl", ref.declOffset + "(%edx),%eax", name);
		} else if (ref instanceof ParamDecl){
			f.genInstr("", "movl", (-4 * ref.declLevel) + "(%ebp),%edx", "");
			f.genInstr("", "movl", ref.declOffset + "(%edx),%eax", name);
		} else {
			if (constDeclRef.constant != null) {
				constDeclRef.constant.genCode(f);
			} else {
				f.genInstr("", "movl", "$" + constDeclRef.constVal + ",%eax", "" + constDeclRef.constVal);
			}
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