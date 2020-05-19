package syntax;


import word.ParseElement;
import word.Word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Syntax implements AddQuadListener {
    public int currentTokenNumber = 0;
    public int quadNum = 0;
    public FourUnitList quadList;
    public boolean fail = false;
    public ArrayList<String> errorList;
    int row = 1;
    Word word;
    ArrayList<ParseElement> rs;
    String str = "";
    int firstIndex;
    private int index = 0;
    private String vartype;
    private Map<String, Integer> opPriMap;

    public Syntax() throws Exception {
        this("in.txt");


    }

    public Syntax(String filename) throws Exception {
        quadList = new FourUnitList(this);
        word = new Word(filename);
        errorList = new ArrayList<String>();
        opPriMap = new HashMap<String, Integer>();
        opPriMap.put("+", 1);
        opPriMap.put("-", 1);
        opPriMap.put("*", 2);
        opPriMap.put("/", 2);
        opPriMap.put("(", 0);
        opPriMap.put(")", 0);

    }

    public static void main(String[] args) throws Exception {
        Syntax s = new Syntax();
        s.WordAnalysis();
        //	s.word.TestArray();
        s.P();
        s.quadList.print();
    }

    public ArrayList<ParseElement> getWordList() {
        return this.rs;
    }

    public void WordAnalysis() throws Exception {
        currentTokenNumber = 0;
        quadNum = 100;
        row = 1;
        index = 0;
        firstIndex = 0;
        rs = new ArrayList<ParseElement>();
        rs = word.WordParse();
        word.TestArray();

        currentTokenNumber = Word.currentToken;
    }

    public int nextQuad() {
        return quadNum + 1;
    }

    public void P() {
        //System.out.println(rs);
        if (rs.get(index).getValue().equalsIgnoreCase("main")) {

            //System.out.println(rs.get(index).getValue());
            this.PB();
        } else {
            System.out.println("程序必须以main关键字开始");
            errorList.add("程序必须以main关键字开始");
            fail = true;
        }

    }

    //段
    Attribute Y()//S
    {
        //System.out.println("进入Y");
        Attribute S = new Attribute();
        if (rs.get(index).getType().equalsIgnoreCase("variable")) {
            //System.out.println("赋值语句");

            S = this.F();

        } else if (rs.get(index).getValue().equalsIgnoreCase("while")) {

            S = this.X();
            if (index >= rs.size()) {
                System.out.println("缺少}");

                fail = true;
                errorList.add("缺少}");
            } else
                this.K();

            //System.out.println("循环语句");
        } else if (rs.get(index).getValue().equalsIgnoreCase("if")) {

            S = this.If();
            if (index >= rs.size()) {
                System.out.println("缺少}");

                fail = true;
                errorList.add("缺少}");
            } else
                this.K();
            //System.out.println("条件语句");
        } else if (rs.get(index).getValue().equalsIgnoreCase("{")) {


            index++;
            S = this.K();
            if (rs.get(index).getValue().equalsIgnoreCase("}")) {

                index++;

            } else {
                System.out.println("缺少}");

                fail = true;
                errorList.add("缺少}");
            }
        } else if (rs.get(index).getType().equalsIgnoreCase("varType")) {


            this.VARType();


        }
        return S;

    }

    void PB() {
        index++;
        if (rs.get(index) != null && rs.get(index).getValue().equalsIgnoreCase("("))//程序名
        {
            index++;
            if (rs.get(index).getValue().equalsIgnoreCase(")")) {
                //System.out.println(rs.get(index).getValue());


                index++;
                if (rs.get(index).getValue().equalsIgnoreCase("{")) {

                    this.Y();


                } else {
                    System.out.println("缺少}");

                    fail = true;
                    errorList.add("缺少}");
                }


            } else {
                System.out.println("缺少(");

                fail = true;
                errorList.add("缺少(");
            }
        } else {
            System.out.println("缺少(");

            fail = true;
            errorList.add("缺少(");
        }
    }

    //语句
    Attribute Sentence() {
        Attribute s = new Attribute();
        if (rs.get(index).getType().equalsIgnoreCase("varType")) {


            this.VARType();


        } else if (rs.get(index).getValue().equalsIgnoreCase("if")) {

            s = this.If();

        } else if (rs.get(index).getType().equalsIgnoreCase("variable")) {
            //System.out.println("赋值语句");

            s = this.F();

        }
        return s;

    }

    Attribute If() {
        M M1 = new M();
        M M2 = new M();
        N N1 = new N();
        boolean iselse = false;
        Attribute S1 = new Attribute();
        Attribute S2 = new Attribute();
        Attribute S = new Attribute();
        Attribute E = new Attribute();
        if (rs.get(index).getValue().equalsIgnoreCase("if")) {
            index++;
            // if(rs.get(index).getValue().equalsIgnoreCase("("))
            {
                //   index++;
                E = this.J();
                //if(rs.get(index).getValue().equalsIgnoreCase(")"))
                {


                    M1.quad = quadNum;

                    S1 = this.Y();


                    if (rs.get(index).getValue().equalsIgnoreCase("else")) {
                        iselse = true;
                        N1.nextlist = quadNum;
                        quadList.gen(quadNum, "J", "0", "0", 'Y', 0, null);//(  j  , — , — , 0 )
                        M2.quad = quadNum;

                        index++;
                        S2 = this.Y();
                        quadList.backPatch(N1.nextlist, quadNum
                        );
                        quadList.backPatch(E.trueList, M1.quad);
                        quadList.backPatch(E.falseList, M2.quad);
                        S.nextList = quadList.merge(S1.nextList, N1.nextlist, S2.nextList);
                    }

                    {


                        if (iselse) {
                            str += "if else语句，嵌套：" + str;

                        } else {
                            quadList.backPatch(E.trueList, M1.quad);
                            quadList.backPatch(E.falseList, quadNum);
                            S.nextList = quadList.merge(E.falseList, S1.nextList);
                        }
                    }

                }
            }


        }

        return S;


    }

    //声明语句
    void VARType() {
        vartype = rs.get(index).getValue();
        index++;
        if (!rs.get(index).getType().equalsIgnoreCase("variable")) {
            System.out.println("语法错误,变量类型后缺少标识符");
            fail = true;
            errorList.add("语法错误,变量类型后缺少标识符");

        }
        try {

            var();


        } catch (Exception e) {
            System.out.println("缺少';'");
            fail = true;
            errorList.add("缺少';'");
        }


    }

    void var() {
        String valuable;

        if (rs.get(index).getType().equalsIgnoreCase("variable")) {
            valuable = rs.get(index).getValue();
            //quadList.gen(quadNum,"new",vartype,null,'N',-1,valuable);
            index++;
            if (rs.get(index).getValue().equalsIgnoreCase(",")) {
            } else if (rs.get(index).getValue().equalsIgnoreCase("=")) {

                index++;
                if (rs.get(index).getType().equalsIgnoreCase("num") || rs.get(index).getType().equalsIgnoreCase("variable")) {
                    Attribute B = this.B();
                    if (B.firstIndex == 0)
                        quadList.gen(quadNum, "=", B.PLACE, null, 'N', -1, valuable);
                    else
                        quadList.gen(quadNum, "=", B.PLACE, null, 'N', -1, valuable);


                } else {
                    System.out.println("语法错误,变量初始化错误");
                    fail = true;
                    errorList.add("语法错误,变量初始化错误");
                }
            } else if (rs.get(index).getValue().equalsIgnoreCase(";")) {
            } else {
                System.out.println("语法错误,标识符后面只能为','或'='");
                fail = true;
                errorList.add("语法错误,标识符后面只能为','或'='");
            }
        } else {
            System.out.println("语法错误,变量类型后缺少标识符");
            fail = true;
            errorList.add("语法错误,变量类型后缺少标识符");
        }
        vara();

    }

    void vara() {
        if (rs.get(index).getValue().equalsIgnoreCase(",")) {
            index++;
            var();
        } else if (rs.get(index).getValue().equalsIgnoreCase(";")) {


        } else {
            System.out.println("语法错误,标识符后面只能为','或'='或';'");
        }


    }

    //赋值语句
    Attribute F() {
        String valuable;
        Attribute F1 = new Attribute();
        Attribute B = new Attribute();
        if (rs.get(index).getType().equalsIgnoreCase("variable")) {
            valuable = rs.get(index).getValue();
            index++;
            if (rs.get(index).getValue().equalsIgnoreCase("=")) {

                index++;
                B = this.B();
                if (B.firstIndex == 0)
                    quadList.gen(quadNum, "=", B.PLACE, null, 'N', -1, valuable);
                else
                    quadList.gen(quadNum, "=", B.PLACE, null, 'N', -1, valuable);


            } else System.out.println("语法错误： 缺少表达式右边部分");
        } else System.out.println("语法错误： 赋值语句左部必须为变量");
        F1.nextList = 0;
        return F1;
    }

    //表达式
    Attribute B() {
        Attribute B1 = new Attribute();
        ArrayList<String> opstack = new ArrayList<String>();
        ArrayList<String> numstack = new ArrayList<String>();
        firstIndex = 0;
        ParseElement pe;
        pe = rs.get(index);
        index++;
        String op, num1, num2;
        while (isOpOrNum(pe)) {
            if (pe.getType().equalsIgnoreCase("num") || pe.getType().equalsIgnoreCase("variable"))
                numstack.add(pe.getValue());
            else {
                if (opstack.size() <= 0)
                    opstack.add(pe.getValue());
                else {
                    if (opPriMap.get(opstack.get(opstack.size() - 1)) <= opPriMap.get(pe.getValue()) || pe.getValue().equalsIgnoreCase("("))
                        opstack.add(pe.getValue());
                    else {

                        if (pe.getValue().equalsIgnoreCase(")")) {
                            op = opstack.remove(opstack.size() - 1);
                            while (!op.equals("(")) {
                                num1 = numstack.remove(numstack.size() - 1);
                                num2 = numstack.remove(numstack.size() - 1);
                                quadList.gen(quadNum, op, num2, num1, 'N', -1, "T" + firstIndex);
                                numstack.add("T" + firstIndex);
                                firstIndex++;
                                try {
                                    op = opstack.remove(opstack.size() - 1);
                                } catch (Exception e) {

                                    break;
                                }

                            }


                        } else {
                            op = opstack.remove(opstack.size() - 1);
                            while (opPriMap.get(op) > opPriMap.get(pe.getValue())) {
                                num1 = numstack.remove(numstack.size() - 1);
                                num2 = numstack.remove(numstack.size() - 1);
                                quadList.gen(quadNum, op, num2, num1, 'N', -1, "T" + firstIndex);
                                numstack.add("T" + firstIndex);
                                firstIndex++;
                                try {
                                    op = opstack.remove(opstack.size() - 1);
                                } catch (Exception e) {

                                    break;
                                }


                            }

                            numstack.add(pe.getValue());


                        }

                    }


                }


            }

            pe = rs.get(index);
            index++;

        }
        while (opstack.size() > 0) {
            op = opstack.remove(opstack.size() - 1);
            num1 = numstack.remove(numstack.size() - 1);
            num2 = numstack.remove(numstack.size() - 1);
            quadList.gen(quadNum, op, num2, num1, 'N', -1, "T" + firstIndex);
            numstack.add("T" + firstIndex);
            firstIndex++;

        }
        index--;
        B1.firstIndex = firstIndex;
        B1.PLACE = numstack.remove(numstack.size() - 1);
        return B1;
    }

    //while语句
    Attribute X() {
        Attribute X = new Attribute();
        Attribute E = new Attribute();
        Attribute S = new Attribute();
        M M1 = new M();
        M M2 = new M();
        if (rs.get(index).getValue().equalsIgnoreCase("while")) {
            index++;
            //if(rs.get(index).getValue().equalsIgnoreCase("("))
            {
                //index++;
                M1.quad = quadNum;
                E = this.J();

                //if(rs.get(index).getValue().equalsIgnoreCase(")"))
                {
                    M2.quad = quadNum;
                    S = this.Y();
                    quadList.backPatch(S.nextList, M1.quad);
                    quadList.gen(quadNum, "J", "0", "0", 'Y', M1.quad, null);
                    quadList.backPatch(E.falseList, quadNum);
                    quadList.backPatch(E.trueList, M2.quad);

                    X.nextList = E.falseList;
                }
                //this.K();
            }
        }
        //this.Y();
        return X;
    }

    //布尔语句
    Attribute J() {
        Attribute E1 = new Attribute();
        Attribute E2 = new Attribute();
        Attribute J1 = new Attribute();
        M M1 = new M();

        E1 = E();

        if (rs.get(index).getValue().equalsIgnoreCase("&&")) {
            index++;
            M1.quad = quadNum;
            E2 = E();
            quadList.backPatch(E1.trueList, M1.quad);
            J1.trueList = E2.trueList;
            //System.out.println(E1.trueList+"  "+ E2.trueList);
            //quadList.print();
            J1.falseList = quadList.merge(E1.falseList, E2.falseList);
        } else if (rs.get(index).getValue().equalsIgnoreCase("||"))//or
        {
            index++;
            M1.quad = quadNum;
            E2 = E();
            quadList.backPatch(E1.falseList, M1.quad);
            J1.falseList = E2.falseList;
            J1.trueList = quadList.merge(E1.trueList, E2.trueList);
        } else {
            J1.trueList = E1.trueList;
            J1.falseList = E1.falseList;
        }
        while (rs.get(index).getValue().equalsIgnoreCase("&&") || rs.get(index).getValue().equalsIgnoreCase("||")) {
            if (rs.get(index).getValue().equalsIgnoreCase("&&")) {
                index++;
                M1.quad = quadNum;
                E1 = J1;
                E2 = E();
                quadList.backPatch(E1.trueList, M1.quad);
                J1.trueList = E2.trueList;
                //System.out.println(E1.trueList+"  "+ E2.trueList);
                //quadList.print();
                J1.falseList = quadList.merge(E1.falseList, E2.falseList);
            } else if (rs.get(index).getValue().equalsIgnoreCase("||"))//or
            {
                index++;
                M1.quad = quadNum;
                E1 = J1;
                E2 = E();
                quadList.backPatch(E1.falseList, M1.quad);
                J1.falseList = E2.falseList;
                J1.trueList = quadList.merge(E1.trueList, E2.trueList);
            }

        }
		/*while(rs.get(index).getValue().equalsIgnoreCase("and")||rs.get(index).getValue().equalsIgnoreCase("or"))
		{
			if(rs.get(index).getValue().equalsIgnoreCase("and"))
			{
				index++;
				M1.quad=quadNum;
				E2=E();
				quadList.backPatch(E1.trueList, M1.quad );
				J1.trueList =E2.trueList;
				System.out.println(E1.trueList+"  "+ E2.trueList);
				quadList.print();
				J1.falseList =quadList.merge(E1.falseList,E2.falseList);
			}
			else
			{
				index++;
				M1.quad=quadNum;
				E2.PLACE = this.E().PLACE;
				quadList.backPatch(E1.falseList, M1.quad );
				J1.falseList =E2.falseList;
				J1.trueList =quadList.merge(E1.trueList,E2.trueList);
			}

		}*/
        J1.PLACE = E1.PLACE;
        return J1;

    }

    //比较语句
    Attribute E() {
        Attribute B1 = new Attribute();
        Attribute B2 = new Attribute();
        Attribute E1 = new Attribute();
        B1.PLACE = this.B().PLACE;
        //while(rs.get(index).getValue().equalsIgnoreCase("=")||rs.get(index).getValue().equalsIgnoreCase(">")||rs.get(index).getValue().equalsIgnoreCase("<")||rs.get(index).getValue().equalsIgnoreCase(">=")||rs.get(index).getValue().equalsIgnoreCase("<="))
        //{
        if (rs.get(index).getValue().equalsIgnoreCase("==")) {
            index++;
            B2.PLACE = this.B().PLACE;
            E1.trueList = quadNum;
            quadList.gen(quadNum, "Jz", B1.PLACE, B2.PLACE, 'Y', 0, null);
            E1.falseList = quadNum;
            quadList.gen(quadNum, "J", "0", "0", 'Y', 0, null);
        } else if (rs.get(index).getValue().equalsIgnoreCase(">")) {
            index++;
            B2.PLACE = this.B().PLACE;
            E1.trueList = quadNum;
            quadList.gen(quadNum, "J>", B1.PLACE, B2.PLACE, 'Y', 0, null);
            E1.falseList = quadNum;
            quadList.gen(quadNum, "J", "0", "0", 'Y', 0, null);
        } else if (rs.get(index).getValue().equalsIgnoreCase("<")) {
            index++;
            B2.PLACE = this.B().PLACE;
            E1.trueList = quadNum;
            quadList.gen(quadNum, "J<", B1.PLACE, B2.PLACE, 'Y', 0, null);
            E1.falseList = quadNum;
            quadList.gen(quadNum, "J", "0", "0", 'Y', 0, null);
        } else if (rs.get(index).getValue().equalsIgnoreCase(">=")) {
            index++;
            B2.PLACE = this.B().PLACE;
            E1.trueList = quadNum;
            quadList.gen(quadNum, "J>=", B1.PLACE, B2.PLACE, 'Y', 0, null);
            E1.falseList = quadNum;
            quadList.gen(quadNum, "J", "0", "0", 'Y', 0, null);
        } else if (rs.get(index).getValue().equalsIgnoreCase("<=")) {
            index++;
            B2.PLACE = this.B().PLACE;
            E1.trueList = quadNum;
            quadList.gen(quadNum, "J<=", B1.PLACE, B2.PLACE, 'Y', 0, null);
            E1.falseList = quadNum;
            quadList.gen(quadNum, "J", "0", "0", 'Y', 0, null);
        } else//is itself
        {
            //index++;
            E1.trueList = quadNum;
            quadList.gen(quadNum, "Jnz", B1.PLACE, "0", 'Y', 0, null);
            E1.falseList = quadNum;
            quadList.gen(quadNum, "J", "0", "0", 'Y', 0, null);
        }

        //}

        return E1;
    }

    Attribute K()//L
    {
        Attribute K1 = new Attribute();
        Attribute Y1 = new Attribute();
        Y1 = this.Y();
        K1 = this.KA(Y1);


        return K1;

    }

    Attribute KA(Attribute Y1)//L'
    {
        M M1 = new M();
        Attribute KA = new Attribute();
        Attribute Y = new Attribute();

        KA.nextList = 0;
        if (rs.get(index).getValue().equalsIgnoreCase(";")) {

            index++;

            M1.quad = quadNum;
            quadList.backPatch(Y1.nextList, M1.quad);
            Y = this.Y();
            KA = this.KA(Y);
        } else {
            KA.copy(Y);
        }
        return KA;

    }

    //******************************************************
    private boolean isOpOrNum(ParseElement pe) {
        return pe.getType().equalsIgnoreCase("variable") || pe.getType().equalsIgnoreCase("num") || pe.getValue().equalsIgnoreCase("+") || pe.getValue().equalsIgnoreCase("-") || pe.getValue().equalsIgnoreCase("*") || pe.getValue().equalsIgnoreCase("/") || pe.getValue().equalsIgnoreCase("(") || pe.getValue().equalsIgnoreCase(")");


    }

    public void addQuad() {
        // TODO Auto-generated method stub
        quadNum++;
    }
}
