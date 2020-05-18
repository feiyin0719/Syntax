package syntax;

class Attribute
{
	public String PLACE;
	public int trueList;
	public int falseList;
	public int nextList;
	public int firstIndex;
	//public boolean IsEmpty;
	
	public void copy(Attribute a)
	{
		this.PLACE = a.PLACE;
		this.trueList = a.trueList;
		this.falseList = a.falseList;
		this.nextList = a.nextList;
		this.falseList = a.nextList;
		
	}
}