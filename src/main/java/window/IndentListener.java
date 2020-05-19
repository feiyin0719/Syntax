package window;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.lang.ref.WeakReference;

public class IndentListener implements DocumentListener {

    private Style keywordStyle;
    private Style normalStyle;
    private WeakReference<JTextPane> ew;

    public IndentListener(JTextPane editor) {
        // 准备着色使用的样式
        keywordStyle = ((StyledDocument) editor.getDocument()).addStyle("Keyword_Style", null);
        normalStyle = ((StyledDocument) editor.getDocument()).addStyle("Keyword_Style", null);
        StyleConstants.setForeground(keywordStyle, Color.blue);
        StyleConstants.setFontSize(keywordStyle, 20);
        StyleConstants.setForeground(normalStyle, Color.BLACK);
        StyleConstants.setFontSize(normalStyle, 20);
        ew = new WeakReference<JTextPane>(editor);
    }


    /**
     * 取得在文档中下标在pos处的字符.
     * <p>
     * 如果pos为doc.getLength(), 返回的是一个文档的结束符, 不会抛出异常. 如果pos<0, 则会抛出异常.
     * 所以pos的有效值是[0, doc.getLength()]
     *
     * @param doc
     * @param pos
     * @return
     * @throws BadLocationException
     */
    public char getCharAt(Document doc, int pos) throws BadLocationException {
        return doc.getText(pos, 1).charAt(0);
    }


    public void changedUpdate(DocumentEvent e) {
        try {
            String s = e.getDocument().getText(e.getOffset(), e.getLength());
            if (s.equals("{")) {
                int num;
                for (num = 0; e.getOffset() - num >= 0; num++) {
                    if (getCharAt(e.getDocument(), e.getOffset() - num) == '\n')
                        break;

                }

                StringBuffer buffer = new StringBuffer();
                buffer.append("\n\n");
                for (int i = 0; i < num; i++)
                    buffer.append(" ");
                buffer.append("}");


                SwingUtilities.invokeLater(new InsertTask(e.getOffset() + 1, buffer.toString()));
                //ew.get().setCaretPosition(e.getOffset()+5);
            } else if (s.equals("(")) {

                SwingUtilities.invokeLater(new InsertTask(e.getOffset() + 1, ")"));

            } else if (s.endsWith("\n")) {

                int t = 0;
                if (e.getOffset() - 1 >= 0 && getCharAt(e.getDocument(), e.getOffset() - 1) == '{') {
                    t = 4;
                }
                int num = 0, i;
                for (i = e.getOffset() - 1; i >= 0; i--) {
                    if (getCharAt(e.getDocument(), i) == '\n')
                        break;

                }

                if (i < 0) i = 0;
                for (; i + num + 1 < e.getOffset(); num++)
                    if (getCharAt(e.getDocument(), i + num + 1) != ' ')
                        break;


                StringBuffer buffer = new StringBuffer();

                for (i = 0; i < num + t; i++)
                    buffer.append(" ");
                SwingUtilities.invokeLater(new InsertTask(e.getOffset() + 1, buffer.toString()));
                //System.out.println("换行");

            }
        } catch (BadLocationException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public void insertUpdate(DocumentEvent e) {

    }


    public void removeUpdate(DocumentEvent e) {

    }

    /**
     * 完成着色任务
     *
     * @author Biao
     */

    private class InsertTask implements Runnable {
        private int offest;
        private String str;

        public InsertTask(int off, String str) {
            // TODO Auto-generated constructor stub
            offest = off;
            this.str = str;
        }

        public void run() {
            // TODO Auto-generated method stub

            try {
                ew.get().getDocument().insertString(offest, str, normalStyle);
                if (str.equals("\n\n}")) {

                    ew.get().setCaretPosition(offest);
                } else if (str.endsWith(" ")) {
                    ew.get().setCaretPosition(offest + str.length());

                } else if (str.equals(")")) {
                    ew.get().setCaretPosition(offest);


                } else {


                }
            } catch (BadLocationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


    }


}