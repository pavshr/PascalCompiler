package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

class Constant extends Factor {
	
	PrefixOpr prefixOpr;
	UnsignedConstant unsignedConst;
	int constVal;

	Constant(int lNum) {
		super(lNum);
	}


	static Constant parse(Scanner s) {
		enterParser("constant");
		Constant constant = new Constant(s.curLineNum());
		if (s.curToken.kind.isPrefixOpr()) constant.prefixOpr = PrefixOpr.parse(s);
		constant.unsignedConst = UnsignedConstant.parse(s);
		leaveParser("constant");
		return constant;
	}

	@Override 
	public String identify() {
		return "<constant> on line " + lineNum;
	}

	@Override
	void check(Block curScope, Library lib) {
		
		if (unsignedConst.name != null) {
			PascalDecl d = curScope.findDecl(unsignedConst.name, unsignedConst);
			unsignedConst.ref = (ConstDecl) d;
			unsignedConst.type = unsignedConst.ref.type;
			
		}
		if(unsignedConst.charLiteral != null) {
			unsignedConst.check(curScope, lib);
		}
		if(unsignedConst.numericLiteral != null) {
			unsignedConst.check(curScope, lib);
		}

		constVal = unsignedConst.constVal;
		type = unsignedConst.type;

		if(prefixOpr != null) {
			String oprName = prefixOpr.operator.kind.toString(); 
			unsignedConst.type.checkType(lib.intType, "Prefix "+oprName, this, "Prefix + or - may only be applied to Integers.");
			if (prefixOpr.operator.kind == subtractToken) {
				constVal = -constVal;
			}
		}
	}

	@Override
	public void genCode(CodeFile f) {
		
		unsignedConst.genCode(f);
		if (prefixOpr != null) prefixOpr.genCode(f); //todo:
	}

	@Override
	types.Type getType() {
		return type;
	}

	@Override
	void prettyPrint() {
		if(prefixOpr != null) prefixOpr.prettyPrint();
		unsignedConst.prettyPrint();
	}
}