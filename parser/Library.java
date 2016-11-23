package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class Library extends Block {
	
	types.BoolType boolType;
    types.CharType charType;
    types.IntType intType;

    ConstDecl eolConst, falseConst, trueConst;
    ProcDecl writeDecl;

	public Library() {
		super(-1);

		//Boolean
		boolType = new types.BoolType();
		TypeDecl boolDecl = new TypeDecl("boolean", -1);
		boolDecl.type = boolType;
		decls.put("boolean", boolDecl);

		//Integer
		intType = new types.IntType();
		TypeDecl intDecl = new TypeDecl("integer", -1);
		intDecl.type = intType;
		decls.put("integer", intDecl);

		//Char
		charType = new types.CharType();
		TypeDecl charDecl = new TypeDecl("char", -1);
		charDecl.type = charType;
		decls.put("char", charDecl);

		//Constant eol
		eolConst = new ConstDecl("eol", -1);
		eolConst.name = "eol";
		eolConst.type = charType;
		//eolConst.constVal = 10 ? not sure, must be checked with teacher.
		decls.put("eol", eolConst);

		//Constant false
		falseConst = new ConstDecl("false", -1);
		falseConst.name = "false";
		falseConst.type = boolType;
		falseConst.constVal = 0;
		decls.put("false", falseConst);

		//Constant true
		trueConst = new ConstDecl("true", -1);
		trueConst.name = "true";
		trueConst.type = boolType;
		trueConst.constVal = 1;
		decls.put("true", trueConst);

		writeDecl = new ProcDecl("write", -1);
		decls.put("write", writeDecl);
	}

	@Override
	public String identify() {
		return "library class";
	}
}