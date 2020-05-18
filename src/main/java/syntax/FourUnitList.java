package syntax;

public class FourUnitList
{
	public FourUnit[] a;
	public int sp;
	public int length;
	protected int size;
	AddQuadListener al;
	FourUnitList(AddQuadListener l)
	{   
		sp=100;
		length=0;
		size=100;
		a=new FourUnit[100];
		al=l;
	}
	
	public boolean extend()
	{
		int l=size*2;
		FourUnit[] b=new FourUnit[l];
		if(b==null)
			return false;
		for(int i=0;i<a.length;i++)
			b[i]=a[i];
		a=b;
		size=size*2;
		return true;
	};
	
	public int backPatch(int startPoint, int value)
	{
		
		int temp;
		while(startPoint!=0)
		{
			temp=a[startPoint-sp].jump;
			a[startPoint-sp].jump=value;
			startPoint=temp;
		}
		
		return value; 
	}
	
	public int merge(int p1,int p2)
	{
		int temp;
		int startPoint=p2;
		if(p1==0)
			return p2;
		if(p2==0)
			return p1;
		
		while(a[startPoint-sp].jump!=0)
		{
			temp=a[startPoint-sp].jump;
			startPoint=temp;
		}
		a[startPoint-sp].jump=p1;
		return p2;
		
	}
	
	public int merge(int p1,int p2,int p3)
	{
		int temp;
		temp=merge(p2,p1);
		return merge(p3,temp);
		
		
	}
	public int gen(int startPoint,String symbol, String f1, String f2,char isCase, int jump, String f3)
	{   if(startPoint-sp>=size)extend();
		a[startPoint-sp]=new FourUnit();
		a[startPoint-sp].setValue(startPoint,symbol,f1,f2,isCase,jump,f3);
	  //  System.out.println(a[startPoint-sp].toString());
		length++;
		if(al!=null)
		al.addQuad();
		return a[startPoint-sp].no;
	}
	
	public void print()
	{
		int i=0;
		for(;i<length;i++)
		{
			if(a[i].isCase=='Y')
			    System.out.println(a[i].no+" "+a[i].symbol+" "+a[i].f1+" "+a[i].f2+" "+a[i].jump+"   "+a[i].isCase);
			else
				System.out.println(a[i].no+" "+a[i].symbol+" "+a[i].f1+" "+a[i].f2+" "+a[i].f3+"   "+a[i].isCase);	
			
		}
	}

	@Override
	public String toString() {
		StringBuffer str=new StringBuffer();
		int i=0;
		for(;i<length;i++)
		{
			if(a[i].isCase=='Y')
			    str.append((i+100)+":"+a[i].symbol+"   "+a[i].f1+"   "+a[i].f2+"   "+a[i].jump+"\n");
			else
				str.append((i+100)+":"+a[i].symbol+"   "+a[i].f1+"   "+a[i].f2+"   "+a[i].f3+"\n");	
			
			
		}
		return str.toString();
	}


}