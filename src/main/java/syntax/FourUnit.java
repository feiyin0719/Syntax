package syntax;

public class FourUnit
{
	public int no;
	public String symbol;
	public String f1;
	public String f2;
	public String f3;
	public int jump;
	public char isCase;
	public char isExecute;
	FourUnit()
	{
		this.isExecute = 'N';
	}
	
	
	void setValue(int sp,String symbol, String f1, String f2, char isCase , int jump,String f3)
	{
		no = sp;
		this.symbol = symbol;
		this.f1 = f1;
		this.f2 = f2;
		this.isCase = isCase;
		//System.out.println(jump);
		if(isCase == 'Y')
			this.jump = jump;
		else
			this.f3 = f3;
	}


	@Override
	public String toString() {
		String str;
		if(isCase=='Y')
			str=no+" "+symbol+" "+f1+" "+f2+" "+jump+"   "+isCase;
			else
				str=no+" "+symbol+" "+f1+" "+f2+" "+f3+"   "+isCase;	
		return str;
	}
	
}

