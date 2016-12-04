package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

import java.util.ArrayList;

class ProcCall extends Statement {
	
	String name; 
	ArrayList<Expression> expressions = new ArrayList<Expression>();
	ProcDecl procRef;

	ProcCall(int lNum) {
		super(lNum);
	}

	static ProcCall parse(Scanner s) {
		enterParser("proc call");
		ProcCall procCall = new ProcCall(s.curLineNum());
		procCall.name = s.curToken.id;
		s.skip(nameToken);
		if (s.curToken.kind == leftParToken) {
			s.skip(leftParToken);
			boolean condition = true;
			while(condition) {
				procCall.expressions.add(Expression.parse(s));
				if(s.curToken.kind == commaToken) s.skip(commaToken);
				if(s.curToken.kind == rightParToken) condition = false;
			}
			s.skip(rightParToken);
		}
		leaveParser("proc call");
		return procCall;
	}
	
	@Override
	void check(Block curScope, Library lib) {
		PascalDecl d = curScope.findDecl(name, this);
		d.checkWhetherProcedure(this);
		for (Expression expression : expressions) {
			expression.check(curScope, lib);
		}
		procRef = (ProcDecl) d;
		if (procRef == lib.writeDecl) {
			for (Expression expression : expressions) {
				if (expression.type != lib.boolType && expression.type != lib.intType && expression.type != lib.charType) {
					error("write-proc is called with wrong arguments!");
				}
			}
		}else{
			for (int i = 0; i < expressions.size(); i++) {
				if ((i + 1) > procRef.paramDeclList.paramDeclarations.size()) error("Too many parameter in call on " + procRef.name + "!");
				if (expressions.size() < procRef.paramDeclList.paramDeclarations.size()) error("Too few parameter in call on " + procRef.name + "!");
				expressions.get(i).type.checkType(procRef.paramDeclList.paramDeclarations.get(i).type, "param #" + (i+1), this, "Param in ProcCall is not the same type as in ProcDecl.");
			}
		}

	}

	@Override
	public void genCode(CodeFile f) {
		if(name.equals("write")) { // write proc call
			for (int i = 0; i < expressions.size(); i++) { // must be pushed backwards
				expressions.get(i).genCode(f);
				if (expressions.get(i).simpleExpr.terms.get(0).factors.get(0) instanceof UnsignedConstant) {
					UnsignedConstant unsCon = (UnsignedConstant) expressions.get(i).simpleExpr.terms.get(0).factors.get(0);
					if (unsCon.charLiteral != null) {
						writeProcCallForGenCode(f, "write_char");
					}
					if (unsCon.numericLiteral != null) {
						writeProcCallForGenCode(f, "write_int");
					}
					if(unsCon.name != null) {
						//TODO: named const
					}
				}else if (expressions.get(i).simpleExpr.terms.get(0).factors.get(0) instanceof Variable) {
					Variable variable = (Variable) expressions.get(i).simpleExpr.terms.get(0).factors.get(0);
					if (variable.constDeclRef != null) {
						if (variable.constDeclRef.constant != null) {
							if (variable.constDeclRef.type instanceof types.CharType) {
								writeProcCallForGenCode(f, "write_char");
							} else if (variable.constDeclRef.type instanceof types.IntType) {
								writeProcCallForGenCode(f, "write_int");
							} else {
								writeProcCallForGenCode(f, "write_bool");
							}
						}else{
							if (variable.ref.type instanceof types.CharType) {
								writeProcCallForGenCode(f, "write_char");
							}else if (variable.ref.type instanceof types.IntType) {
								writeProcCallForGenCode(f, "write_int");
							}else{
								writeProcCallForGenCode(f, "write_bool");	
							}
						}
					}else{
						if (variable.ref.type instanceof types.CharType) {
								writeProcCallForGenCode(f, "write_char");
						} else if (variable.ref.type instanceof types.IntType) {
							writeProcCallForGenCode(f, "write_int");
						} else {
							writeProcCallForGenCode(f, "write_bool");
						}
					}
				}
			}
		}else{ // end of write
			for (int i = expressions.size() - 1; i >= 0; i--) { // must be pushed backwards
				expressions.get(i).genCode(f);
				f.genInstr("", "pushl", "%eax", "");
			}
			f.genInstr("", "call", "proc$" + name + procRef.declLevel, "");
			f.genInstr("", "addl", "$8,%esp", "");
		}
	}

	@Override 
	public String identify() {
		return "<proc call> on line " + lineNum;
	}

	@Override
	void prettyPrint() {
		Main.log.prettyPrint(name);
		Main.log.prettyPrint("(");
		for (int i = 0; i < expressions.size(); i++) {
			expressions.get(i).prettyPrint();
			if (i < expressions.size() - 1) Main.log.prettyPrint(", ");
		}
		Main.log.prettyPrint(")");
	}

	private void writeProcCallForGenCode(CodeFile f, String typeOfCall) {
		f.genInstr("", "pushl", "%eax", "Push next param.");
		f.genInstr("", "call", typeOfCall, "");
		f.genInstr("", "addl", "$4,%esp", "Pop param.");
	}
}