package word;
import java.io.*;
import java.util.ArrayList;

public class Word {
	
	public  static String[] resTable;
	public  static String[] tokenTable;
	public  double[] numberTable;
	public  static int currentToken;
	public  int currentNumber;
	public  ArrayList<ParseElement> rs = new ArrayList<ParseElement>();
	private String filename;

	public Word(){
		this("in.txt");
	}
	
	public Word(String filename){
		this.resTable = new String[200];
		this.tokenTable = new String[200];
		this.numberTable = new double[200];
		this.resTable[0]="void"; 
		this.resTable[1]="int";
		this.resTable[2]="float";
		this.resTable[3]="bool";
        this.resTable[4]="if";
	    this.resTable[5]="else";
		this.resTable[6]="do";
		this.resTable[7]="while";
	    this.resTable[8]="return";
		this.resTable[9]="main";
	
		currentToken = 0;
		currentNumber = 0;
		this.filename=filename;
	}
	public int reserve(String strToken){
		for(int i = 0;i < 10;i++)
		{
			if(strToken.equalsIgnoreCase(resTable[i])){
				return i+1;
			}
		}
		
		return 0;
		
	}
	
	public int entry(String i)
	{
		//System.out.println("currentToken="+currentToken);
		for(int index = 0;index< this.currentToken;index++)
		{
			//System.out.println(tokenTable[index]);
		
			//System.out.println(index);
			if(i.equalsIgnoreCase(tokenTable[index].toString()))
				return index;
		}
		for(int index = 0;index< this.currentNumber;index++)
		{
			//out.println(tokenTable[i]);
		
			//System.out.println("i="+i+" "+numberTable[index]);
			if(i.equalsIgnoreCase(Double.toString(numberTable[index])))
				return index;
		}
		return -1;
		
	}
	
	public String isBoolean(String strToken){

		if("true".equals(strToken))
		{
			return "true";
		}
		else if("false".equals(strToken))
		{
			return "false";
		}
		else return "null";
	}
	
	public int insertID(String strToken){
		for(int i = 0;i<this.currentToken;i++)
		{
			if(strToken.equals(tokenTable[i]))
			return currentToken;
		}
		tokenTable[currentToken] = strToken;
		currentToken++;
		//System.out.println(currentToken);
		return currentToken;
		
	}
	
	public int insertConst(String strToken){//
		for(int i = 0;i<this.currentNumber;i++)
		{
			if(strToken.equals(numberTable[i]))
			return currentNumber;
		}
		numberTable[currentNumber] = Double.valueOf(strToken);
		currentNumber++;
		
		return currentNumber;
		
	}
	
	public void init(){
		

		this.resTable[0]="void"; 
		this.resTable[1]="int";
		this.resTable[2]="float";
		this.resTable[3]="bool";
        this.resTable[4]="if";
	    this.resTable[5]="else";
		this.resTable[6]="do";
		this.resTable[7]="while";
	    this.resTable[8]="return";
		this.resTable[9]="main";
		currentToken = 0;
		currentNumber = 0;
	}
	
	public void printTokenTable(PrintWriter out)
	{
		for(int i = 0;i< this.currentToken;i++)
		{
			out.println(tokenTable[i]);
		}
	}
	
	public String judgeTypeByCode(int code)
	{
      if(code>=1&&code<=4)
    	  return "varType";
      else if(code==10)
    	  return "keyword";
      else
    	  return "keyword";
//		if(code==1)
//			return "void";
//		else if(code==2)
//			return "begin";
//		else if(code==3)
//			return "end";
//		else if(code==4)
//			return "var";
//		else if(code==5)
//			return "varType";
//		else if(code==6)
//			return "other";
//		else if(code==7)
//			return "other";
//		else if(code==8)
//			return "other";
//		else if(code==9)
//			return "other";
//		else if(code==10)
//			return "other";
//		else if(code==11)
//			return "varType";
//		else 
//			return "varType";

	}
	
	public void printNumberTable(PrintWriter out)//锟斤拷印锟斤拷锟斤拷锟�
	{
		for(int i = 0;i< this.currentNumber;i++)
		{
			//out.println(numberTable[i]);
		}
	}
	
	public ArrayList<ParseElement> WordParse() throws IOException{
		
		
		FileInputStream input = new FileInputStream(new File(filename));
		PushbackInputStream in = new PushbackInputStream(input);
		String strToken;
		int line = 0, col = 0,errorcount=0;
		Word s = this;
		
		char c;
		int intofc;
		PrintWriter out = new PrintWriter("out.txt");
		int code, value;
		while((intofc=in.read())!=-1)
		{
		strToken = "";
		c=(char)intofc;
		col++;
		if(Character.isLetter(c)){
			while(Character.isLetter(c)||Character.isDigit(c))
			{
				strToken+=c;
				c=(char)in.read();
				col++;
			}
			in.unread(c);
			col-=1;
			//strToken.substring(0, strToken.length()-1);
			code = s.reserve(strToken);
			
			if(code==0){
			value = s.insertID(strToken);
			out.println("$ID,"+value);
			ParseElement pe = new ParseElement();
			pe.setType("variable");
			pe.setValue(strToken);
			rs.add(pe);
			}
			else 
			{
				out.println(code+",-");
				ParseElement pe = new ParseElement();
				pe.setType(this.judgeTypeByCode(code));
				pe.setValue(strToken);
				rs.add(pe);
				
			}
//			if(s.isBoolean(strToken)=="true")
//			{
//				out.println("%BOOL,true");
//				ParseElement pe = new ParseElement();
//				pe.setType("num");
//				pe.setValue(strToken);
//				rs.add(pe);
//			}
//			else if(s.isBoolean(strToken)=="false")
//			{
//				out.println("%BOOL,false");
//				ParseElement pe = new ParseElement();
//				pe.setType("num");
//				pe.setValue(strToken);
//				rs.add(pe);
//			}
			
		}
		else if(Character.isDigit(c))
		{  
			
			boolean isDecimal=false;
			while(Character.isDigit(c)||c=='.')
			{  if(c=='.'&&!isDecimal)
				isDecimal=true;
			else if(c=='.'&&isDecimal)
			{
				break;
			}
				strToken+=c;
				c=(char)in.read();
				col++;
			}
			in.unread(c);
			col-=1;
			value = s.insertConst(strToken);
			out.println("$INT,"+value);
			ParseElement pe = new ParseElement();
			pe.setType("num");
			pe.setValue(strToken);
			rs.add(pe);
			
		}
		else{
			
		if(c=='=')
		{
			c = (char)in.read();
			col++;
			if(c=='=')
			{
				out.println("$ASSIGN,-");
				ParseElement pe = new ParseElement();
				pe.setType("operator");
				pe.setValue("==");
				rs.add(pe);
			}
			else 
				{
				ParseElement pe = new ParseElement();
				pe.setType("operator");
				pe.setValue("=");
				rs.add(pe);
				in.unread(c);
				col-=1;
				}
				
		}
		else if(c=='+')
		{
			out.println("$PLUS,-");
			ParseElement pe = new ParseElement();
			pe.setType("operator");
			pe.setValue("+");
			rs.add(pe);
		}
		else if(c=='-')
		{  
			out.println("$SUBSTRACT,-");
			ParseElement pe = new ParseElement();
			pe.setType("operator");
			pe.setValue("-");
			rs.add(pe);
		}
		else if(c=='*')
		{
			c = (char)in.read();
			col++;
			if(c=='*')
				{	
					out.println("$POWER,-");
					ParseElement pe = new ParseElement();
					pe.setType("operator");

					pe.setValue("**");
					rs.add(pe);
					}
			else {
					out.println("$STAR,-");
					ParseElement pe = new ParseElement();
					pe.setType("operator");
					pe.setValue("*");
					rs.add(pe);
					in.unread(c);
				}
			
		}
		else if(c==';')
		{
			out.println("$SEMICOLON,-");
			ParseElement pe = new ParseElement();
			pe.setType("other");
			pe.setValue(";");
			rs.add(pe);
		}
		else if(c=='(')
		{
			out.println("$LPAR,-");
			ParseElement pe = new ParseElement();
			pe.setType("other");
			pe.setValue("(");
			rs.add(pe);
			}
		else if(c==')')
		{	
			out.println("$RPAR,-");
			ParseElement pe = new ParseElement();
			pe.setType("other");
			pe.setValue(")");
			rs.add(pe);
		}
		else if(c=='{')
		{
			out.println("$LBRACE,-");
			ParseElement pe = new ParseElement();
			pe.setType("other");
			pe.setValue("{");
			rs.add(pe);
			}
		else if(c=='}')
		{	
			out.println("$RBRACE,-");
			ParseElement pe = new ParseElement();
			pe.setType("other");
			pe.setValue("}");
			rs.add(pe);
			}
		else if(c==' ')
		{}
		else if(c=='\r')
		{}
		else if(c==',')
		{					
			ParseElement pe = new ParseElement();
			pe.setType("other");
			pe.setValue(",");
			rs.add(pe);
			}
		else if(c=='\n')
		{
			line++;
			col = 0;
		}
		
		else if(c=='/')
		{
			c = (char) in.read();
			col++;
			if(c=='*')
			{
				while(true)
				{
					c=(char)in.read();
					col++;
					if(c=='\n')
					{
						line++;
						col = 0;
					}
					else if(c!='*')
						continue;
					else{
						if ((c = (char) in.read())=='/')
						{
							col++;
							break;}
						}
				}
			}
			else if(c=='/')
			{
					while(true)
					{	
						c = (char)in.read();
						col++;
						if(c!='\n')
							continue;
						else 
							break;
					}
			}
			else 
			{
				out.println("$DIVISION,-");
				ParseElement pe = new ParseElement();
				pe.setType("operator");
				pe.setValue("/");
				rs.add(pe);
				in.unread(c);
			}
		}

		else if(c=='<')
		{
			c = (char)in.read();
			col++;
			if(c=='=')
				{
					out.println("<=");
					ParseElement pe = new ParseElement();
					pe.setType("operator");
					pe.setValue("<=");
					rs.add(pe);
					}
			else if(c=='>')
				{
					out.println("unequal");
					ParseElement pe = new ParseElement();
					pe.setType("operator");
					pe.setValue("<>");
					rs.add(pe);
					}
			else
			{
				out.println("<");
				ParseElement pe = new ParseElement();
				pe.setType("operator");
				pe.setValue("<");
				rs.add(pe);
				in.unread(c);
				col-=1;
			}
		}
		else if(c=='>')
		{
			c = (char)in.read();
			col++;
			if(c=='=')
				{
					out.println(">=");
					ParseElement pe = new ParseElement();
					pe.setType("operator");
					pe.setValue(">=");
					rs.add(pe);
				}
			else
			{
				out.println(">");
				ParseElement pe = new ParseElement();
				pe.setType("operator");
				pe.setValue(">");
				rs.add(pe);
				in.unread(c);
				col-=1;
			}
				
		}
		else if(c=='!'){
			c = (char)in.read();
			col++;
			if(c=='=')
			{
				ParseElement pe = new ParseElement();
				pe.setType("operator");
				pe.setValue("!=");
				rs.add(pe);
				
			}
			else{
				
				ParseElement pe = new ParseElement();
				pe.setType("operator");
				pe.setValue("!");
				rs.add(pe);
				in.unread(c);
				col-=1;
				
			}
			
		}
		else if(c=='&'){
			
			c = (char)in.read();
			col++;
			if(c=='&')
			{
				ParseElement pe = new ParseElement();
				pe.setType("operator");
				pe.setValue("&&");
				rs.add(pe);
				
			}
			else{
				
				ParseElement pe = new ParseElement();
				pe.setType("operator");
				pe.setValue("&");
				rs.add(pe);
				in.unread(c);
				col-=1;
				
			}
			
			
		}
		else if(c=='|'){
			
			c = (char)in.read();
			col++;
			if(c=='|')
			{
				ParseElement pe = new ParseElement();
				pe.setType("operator");
				pe.setValue("||");
				rs.add(pe);
				
			}
			else{
				
				ParseElement pe = new ParseElement();
				pe.setType("operator");
				pe.setValue("|");
				rs.add(pe);
				in.unread(c);
				col-=1;
				
			}
			
			
			
		}
		else 
			{
			System.out.println("ERROR At line "+(line+1)+"col "+col+": undefined token "+"'"+c+"'");
			errorcount++;
			}
		}
		}
		//System.out.println("Total number of Word errors "+errorcount);
		//System.out.println("******* Table of numbers  *******");
		//s.printNumberTable(out);
		//System.out.println("******* Table of tokens   *******");
		//s.printTokenTable(out);
		//System.out.println();
		out.close();
		input.close();
		in.close();
		return rs;
		
	}

	public void TestArray()
	{
		for(int i = 0;i<rs.size();i++)
		{
			System.out.println("pos:"+i+"Type "+rs.get(i).getType()+ "   Values "+rs.get(i).getValue());
		}

	}
	
	
	public static void main(String args[]) throws IOException
	{
		Word w = new Word();
		w.WordParse();
		//w.TestArray();
		for(int i=0; i<w.rs.size();i++)
			w.rs.get(i).print();
		System.out.println(w.currentToken);
		System.out.println(w.currentNumber);
	}

}


