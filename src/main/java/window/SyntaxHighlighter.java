package window;
import java.awt.Color;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class SyntaxHighlighter implements DocumentListener {
	private Set<String> keywords;
	private Style keywordStyle;
	private Style normalStyle;
    private WeakReference<JTextPane> ew;
	public SyntaxHighlighter(JTextPane editor) {
		// ׼����ɫʹ�õ���ʽ
		keywordStyle = ((StyledDocument) editor.getDocument()).addStyle("Keyword_Style", null);
		normalStyle = ((StyledDocument) editor.getDocument()).addStyle("Keyword_Style", null);
		StyleConstants.setForeground(keywordStyle, Color.blue);
		StyleConstants.setFontSize(keywordStyle, 20);
		StyleConstants.setForeground(normalStyle, Color.BLACK);
        StyleConstants.setFontSize(normalStyle, 20);
		// ׼���ؼ���
		keywords = new HashSet<String>();
		keywords.add("if");
		keywords.add("else");
		keywords.add("while");
		keywords.add("int");
		keywords.add("float");
		keywords.add("main");
		keywords.add("bool");
		keywords.add("void");
		keywords.add("return");
		ew=new WeakReference<JTextPane>(editor);
	}

	public void colouring(StyledDocument doc, int pos, int len) throws BadLocationException {
		// ȡ�ò������ɾ����Ӱ�쵽�ĵ���.
		// ����"public"��b�����һ���ո�, �ͱ����:"pub lic", ��ʱ������������Ҫ����:"pub"��"lic"
		// ��ʱҪȡ�õķ�Χ��pub��pǰ���λ�ú�lic��c�����λ��
		int start = indexOfWordStart(doc, pos);
		int end = indexOfWordEnd(doc, pos + len);

		char ch;
		while (start < end) {
			ch = getCharAt(doc, start);
			if (Character.isLetter(ch) || ch == '_') {
				// ���������ĸ�����»��߿�ͷ, ˵���ǵ���
				// posΪ���������һ���±�
				start = colouringWord(doc, start);
			} else {
				SwingUtilities.invokeLater(new ColouringTask(doc, start, 1, normalStyle));
				++start;
			}
		}
	}

	/**
	 * �Ե��ʽ�����ɫ, �����ص��ʽ������±�.
	 * 
	 * @param doc
	 * @param pos
	 * @return
	 * @throws BadLocationException
	 */
	public int colouringWord(StyledDocument doc, int pos) throws BadLocationException {
		int wordEnd = indexOfWordEnd(doc, pos);
		String word = doc.getText(pos, wordEnd - pos);

		if (keywords.contains(word)) {
			// ����ǹؼ���, �ͽ��йؼ��ֵ���ɫ, ����ʹ����ͨ����ɫ.
			// ������һ��Ҫע��, ��insertUpdate��removeUpdate�ķ������õĹ�����, �����޸�doc������.
			// ��������Ҫ�ﵽ�ܹ��޸�doc������, ���԰Ѵ�����ŵ��������������ȥִ��.
			// ʵ����һĿ��, ����ʹ�����߳�, ���ŵ�swing���¼�������ȥ��������һ��.
			SwingUtilities.invokeLater(new ColouringTask(doc, pos, wordEnd - pos, keywordStyle));
		} else {
			SwingUtilities.invokeLater(new ColouringTask(doc, pos, wordEnd - pos, normalStyle));
		}

		return wordEnd;
	}

	/**
	 * ȡ�����ĵ����±���pos�����ַ�.
	 * 
	 * ���posΪdoc.getLength(), ���ص���һ���ĵ��Ľ�����, �����׳��쳣. ���pos<0, ����׳��쳣.
	 * ����pos����Чֵ��[0, doc.getLength()]
	 * 
	 * @param doc
	 * @param pos
	 * @return
	 * @throws BadLocationException
	 */
	public char getCharAt(Document doc, int pos) throws BadLocationException {
		return doc.getText(pos, 1).charAt(0);
	}

	/**
	 * ȡ���±�Ϊposʱ, �����ڵĵ��ʿ�ʼ���±�. ?��wor^d?�� (^��ʾpos, ?����ʾ��ʼ��������±�)
	 * 
	 * @param doc
	 * @param pos
	 * @return
	 * @throws BadLocationException
	 */
	public int indexOfWordStart(Document doc, int pos) throws BadLocationException {
		// ��pos��ʼ��ǰ�ҵ���һ���ǵ����ַ�.
		for (; pos > 0 && isWordCharacter(doc, pos - 1); --pos);

		return pos;
	}

	/**
	 * ȡ���±�Ϊposʱ, �����ڵĵ��ʽ������±�. ?��wor^d?�� (^��ʾpos, ?����ʾ��ʼ��������±�)
	 * 
	 * @param doc
	 * @param pos
	 * @return
	 * @throws BadLocationException
	 */
	public int indexOfWordEnd(Document doc, int pos) throws BadLocationException {
		// ��pos��ʼ��ǰ�ҵ���һ���ǵ����ַ�.
		for (; isWordCharacter(doc, pos); ++pos);

		return pos;
	}

	/**
	 * ���һ���ַ�����ĸ, ����, �»���, �򷵻�true.
	 * 
	 * @param doc
	 * @param pos
	 * @return
	 * @throws BadLocationException
	 */
	public boolean isWordCharacter(Document doc, int pos) throws BadLocationException {
		char ch = getCharAt(doc, pos);
		if (Character.isLetter(ch) || Character.isDigit(ch) || ch == '_') { return true; }
		return false;
	}


	public void changedUpdate(DocumentEvent e) {
		
	}

	public void insertUpdate(DocumentEvent e) {
		try {
		    colouring((StyledDocument) e.getDocument(), e.getOffset(), e.getLength());
			
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}


	public void removeUpdate(DocumentEvent e) {
		try {
			// ��Ϊɾ�����������Ӱ��ĵ�������, ���Գ��ȾͲ���Ҫ��
			colouring((StyledDocument) e.getDocument(), e.getOffset(), 0);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * �����ɫ����
	 * 
	 * @author Biao
	 * 
	 */
	private class ColouringTask implements Runnable {
		private StyledDocument doc;
		private Style style;
		private int pos;
		private int len;

		public ColouringTask(StyledDocument doc, int pos, int len, Style style) {
			this.doc = doc;
			this.pos = pos;
			this.len = len;
			this.style = style;
		}
        
		public void run() {
			try {
				// ������Ƕ��ַ�������ɫ
				doc.setCharacterAttributes(pos, len, style, true);
			} catch (Exception e) {}
		}
	}
	
	
}