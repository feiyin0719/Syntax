package window;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.Document;

import dart.Dart;
import dart.VarTable;
import syntax.FourUnitList;
import word.ParseElement;

public class MainFrame extends JFrame {

	private JPanel contentPane;
    Document doc;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 978, 743);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu menu_1 = new JMenu("文件");
		menuBar.add(menu_1);
		
		JMenuItem menuItem_1 = new JMenuItem("打开");
		menuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				int flag=fc.showOpenDialog(MainFrame.this);
				  if(flag==JFileChooser.APPROVE_OPTION)   
		             {   
		              
		                try {
		                    file=fc.getSelectedFile();
		                    MainFrame.this.setTitle(file.getName());
							BufferedReader reader=new BufferedReader(new FileReader(file));
							String str="",str1;
							while((str1=reader.readLine())!=null){
								
								str+=str1+"\n";
								
							}
							reader.close();
							//doc.removeDocumentListener(indentListener);
							textPane.setText(str);
							new AddListenerThread().start();
							
							
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		                   
		             }  
				
				
			}
		});
		
		JMenuItem menuItem_3 = new JMenuItem("新建");
		menuItem_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				file=null;
				textPane.setText("");
				setTitle("新建");
				
				
				
			}
		});
		menu_1.add(menuItem_3);
		menu_1.add(menuItem_1);
		
		JMenuItem menuItem_2 = new JMenuItem("保存");
		menuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			
				if(file==null){
					if(fc.showSaveDialog(MainFrame.this)==JFileChooser.APPROVE_OPTION){
						
						file=fc.getSelectedFile();
						setTitle(file.getName());
						
						
						
						
						
					}
					else{
						return;
					}
					
					
				}
				
				
				
				try {
					PrintWriter out = new PrintWriter(file);
					
					out.print(textPane.getText());
					out.flush();
					out.close();
					
					
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				
				
			}
		});
		menu_1.add(menuItem_2);
		
		JMenuItem menuItem_4 = new JMenuItem("另存为");
		menuItem_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(fc.showSaveDialog(MainFrame.this)==JFileChooser.APPROVE_OPTION){
					
					file=fc.getSelectedFile();
					setTitle(file.getName());
					
					
					
					
					
				}
				else{
					return;
				}
			
					PrintWriter out1;
					try {
						out1 = new PrintWriter(file);

						out1.print(textPane.getText());
						out1.flush();
						out1.close();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				
			}
		});
		menu_1.add(menuItem_4);
		
		JMenu menu = new JMenu("编译");
		menuBar.add(menu);
		
		JMenuItem menuItem = new JMenuItem("生成四元式");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			
					if(file==null){
						if(fc.showSaveDialog(MainFrame.this)==JFileChooser.APPROVE_OPTION){
							
							file=fc.getSelectedFile();
							setTitle(file.getName());
							
							
							
							
							
						}
						else{return;}
						
						
						
					}
					try {
						PrintWriter out1 = new PrintWriter(file);
						
						out1.print(textPane.getText());
						out1.flush();
						out1.close();
						
						
						
					
					
					
					
					Dart d=new Dart(file.getAbsolutePath());
					d.run();
					ArrayList<ParseElement> wordList=d.getWordList();
				    ArrayList<String> keylist=new ArrayList<String>();
				    ArrayList<String> numlist=new ArrayList<String>();
				    ArrayList<String> otherlist=new ArrayList<String>();
				    ArrayList<String> varlist=new ArrayList<String>();
				    ArrayList<String> oplist=new ArrayList<String>();
				    for(ParseElement p:wordList){
				    	if(p.getType().equals("keyword")||p.getType().equals("varType"))
				    	{
				    		if(!keylist.contains(p.getValue()))
				    		keylist.add(p.getValue());
				    	}
				    	else if(p.getType().equals("variable"))
				    	{
				    		if(!varlist.contains(p.getValue()))
				    			varlist.add(p.getValue());
				    		
				    	}
				    	else if(p.getType().equals("operator")){
				    		if(!oplist.contains(p.getValue()))
				    			oplist.add(p.getValue());
				    		
				    		
				    	}
				    	else if(p.getType().equals("num")){
				    		
				    		if(!numlist.contains(p.getValue()))
				    			numlist.add(p.getValue());
				    	}
				    	else {
				    		if(!otherlist.contains(p.getValue()))
				    		otherlist.add(p.getValue());
				    	}
				    }
				    StringBuffer str=new StringBuffer();
				    str.append("关键字：");
				    for(String s:keylist)
				    	str.append(s+"  ");
				    str.append("\n运算符：");
				    for(String s:oplist)
				    	str.append(s+" ");
				    str.append("\n标识符：");
				    for(String s:varlist)
				    	str.append(s+" ");
				    str.append("\n数字：");
				    for(String s:numlist)
				    	str.append(s+" ");
				    str.append("\n其他：");
				    for(String s:otherlist)
				    	str.append(s+" ");
				    textArea.setText(str.toString());
				    
				    FourUnitList fourUnitList=d.getFourUnitList();
				    textArea_1.setText(fourUnitList.toString());
				    StringBuffer varstr=new StringBuffer();
				    VarTable [] varTableList=d.getVarList();
				    int varnum=d.getVarNum();
				    for(int i=0;i<varnum;i++)
				    { if(varlist.contains(varTableList[i].getName()))
				    	varstr.append(varTableList[i].getName()+"   "+varTableList[i].getValue()+"\n");
				    	
				    	
				    	
				    }
				    //textArea_2.setText(varstr.toString());
					if(d.s.fail){
						
						StringBuffer buffer1=new StringBuffer();
						for(String s:d.s.errorList)
							buffer1.append(s+"\n");
						
						textArea_3.setText(buffer1.toString());
						
						
					}
					else{
						textArea_3.setText("语法分析正确");
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				
			}
		});
		menu.add(menuItem);
		
		JMenuBar menuBar_1 = new JMenuBar();
		menu.add(menuBar_1);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
	
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setBounds(5, 5, 956, 677);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setDividerLocation(400);
		contentPane.add(splitPane);
		
		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setDividerLocation(450);
		//contentPane.add(splitPane_1);
		JScrollPane scrollPane = new JScrollPane();
		splitPane_1.setLeftComponent(scrollPane);
		splitPane.setLeftComponent(splitPane_1);
	    textPane = new JTextPane();
	    textPane.setFont(new Font("Courier", Font.PLAIN,20));
	    doc=textPane.getDocument();
	    syntaxListener=new SyntaxHighlighter(textPane);
	    indentListener=new IndentListener(textPane);
//	    doc.addDocumentListener(syntaxListener);
//	    doc.addDocumentListener(indentListener);
	   // new UndoWrapper(textPane);
		scrollPane.setViewportView(textPane);
		
		JPanel panel_1 = new JPanel();
		splitPane_1.setRightComponent(panel_1);
		panel_1.setLayout(null);
		
		JLabel label = new JLabel("词法分析");
		label.setBounds(15, 35, 81, 21);
		panel_1.add(label);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		splitPane.setRightComponent(scrollPane_1);
		
		JPanel panel = new JPanel();
		scrollPane_1.setViewportView(panel);
		panel.setLayout(null);
		
		JLabel label_1 = new JLabel("四元式");
		label_1.setBounds(469, 15, 81, 21);
		panel.add(label_1);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(15, 70, 376, 314);
		//panel.add(scrollPane_2);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane_2.setViewportView(textArea);
		panel_1.add(scrollPane_2);
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(469, 51, 379, 172);
		panel.add(scrollPane_3);
		
	    textArea_1 = new JTextArea();
	    textArea_1.setEditable(false);
		scrollPane_3.setViewportView(textArea_1);
		
		JLabel label_3 = new JLabel("语法分析");
		label_3.setBounds(46, 15, 81, 21);
		panel.add(label_3);
		
		JScrollPane scrollPane_5 = new JScrollPane();
		scrollPane_5.setBounds(46, 51, 359, 172);
		panel.add(scrollPane_5);
		
	    textArea_3 = new JTextArea();
		scrollPane_5.setViewportView(textArea_3);
		
		
		fc= new JFileChooser("codes") ;
	    this.setTitle("新建");
	}
	JTextPane textPane;
	JTextArea textArea;
	JTextArea textArea_1; 
	JTextArea textArea_3 ;
    File file=null;
    JFileChooser fc;
    SyntaxHighlighter syntaxListener;
    IndentListener indentListener;
    private class AddListenerThread extends Thread{
    	
    	public void run(){
    		
    		try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		//doc.addDocumentListener(indentListener);
    		
    	}
    	
    	
    }
}
