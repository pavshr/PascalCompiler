package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

/* <program> ::= 'program' <name> ';' <block> '.' */
public class Program extends PascalSyntax {

	String progName;
	Block block;

	Program(String progName, int lNum) {
		super(lNum);
		this.progName = progName;	
	}

	public static Program parse(Scanner s) {
		enterParser("program");
		s.skip(programToken);
		Program prog = new Program(s.curToken.id, s.curLineNum());
		s.skip(nameToken);		
		s.skip(semicolonToken);
		prog.block = Block.parse(s);
		s.skip(dotToken);
		leaveParser("program");
		return prog;

	}

	@Override 
	public String identify() {
		return "<program> " + progName + " on line " + lineNum;
	}

	@Override
	public void check(Block curScope, Library lib) {
		block.check(curScope, lib);
	}


	@Override
	public void prettyPrint() {
		Main.log.prettyPrint("program " + progName);
		Main.log.prettyPrintLn(";");
		block.prettyPrint();
		Main.log.prettyPrint(".");
	}
}