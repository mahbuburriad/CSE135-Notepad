package project;


import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;
import java.io.*;

import javax.swing.text.DefaultEditorKit;
import javax.swing.undo.CannotUndoException;



public class NotePad implements ActionListener,KeyListener{

		JFrame window = new JFrame();
		
		
		JTextArea wordPad = new JTextArea();
		
		JScrollPane areaScrollPane;
		
		JMenuBar jmub = new JMenuBar();
		
		JMenu file = new JMenu("File");
		JMenu edit = new JMenu("Edit");
		JMenu format = new JMenu("Format");
		JMenu view = new JMenu("View");
		JMenu about = new JMenu("About");
		
		JMenuItem open = new JMenuItem("Open");
		JMenuItem newFile = new JMenuItem("New");
		JMenuItem save = new JMenuItem("Save");
		JMenuItem close = new JMenuItem("Close");
		
		JMenuItem undo = new JMenuItem("Undo");
		JMenuItem cut = new JMenuItem("Cut");
		JMenuItem copy = new JMenuItem("Copy");
		JMenuItem paste = new JMenuItem("Paste");
		JMenuItem delete = new JMenuItem("Delete");
		JMenuItem selectAll = new JMenuItem("Select All");
		
		JMenuItem font = new JMenuItem("Font");
		JMenuItem fontFormat = new JMenuItem("Font format");
		JMenuItem fontSize = new JMenuItem("Font Size");
		
		/*JMenuItem fontStyle = new JMenuItem("Font Style");
		
		JMenuItem bold = new JMenuItem("Bold");
		JMenuItem Italic = new JMenuItem("Italic");
		JMenuItem Underline = new JMenuItem("Underline");*/
		
		JMenuItem wordCount =  new JMenu("Word Count");
		
		JMenuItem lineCount = new JMenu("Line Count");
		JMenuItem WordCount = new JMenu("Word Count");
		JMenuItem CharCount = new JMenu("Character Count");
		
		JMenuItem WWrap = new JMenuItem("Word Wrap");
		JMenuItem authors = new JMenuItem("Authors");
		
		
		
	
		public NotePad(){
			
			open.addActionListener(this);
			newFile.addActionListener(this);
			save.addActionListener(this);
			close.addActionListener(this);
			
			undo.addActionListener(this);
			cut.addActionListener(this);
			copy.addActionListener(this);
			paste.addActionListener(this);
			delete.addActionListener(this);
			selectAll.addActionListener(this);
			
			font.addActionListener(this);
			fontSize.addActionListener(this);
			fontFormat.addActionListener(this);
			//fontStyle.addActionListener(this);
			
			
			wordCount.addActionListener(this);
			lineCount.addActionListener(this);
			WordCount.addActionListener(this);
			CharCount.addActionListener(this);
			
			WWrap.addActionListener(this);
			authors.addActionListener(this);
			
			file.add(open);
			file.add(newFile);
			file.add(save);
			file.add(close);
			
			edit.add(undo);
			edit.add(cut);
			edit.add(copy);
			edit.add(paste);
			edit.add(delete);
			edit.add(selectAll);
			
			format.add(WWrap);
			format.add(font);
			format.add(fontSize);
			
			view.add(wordCount);
			wordCount.add(lineCount);
			wordCount.add(WordCount);
			wordCount.add(CharCount);
			
			about.add(authors);
			
			jmub.add(file);
			jmub.add(edit);
			jmub.add(format);
			jmub.add(view);
			jmub.add(about);
			
			window.setJMenuBar(jmub);
			
			wordPad.addKeyListener(this);
			wordPad.setBounds(50,60,260,260);
			window.add(wordPad);
		
			
			window.setSize(1200, 600);
			window.setTitle("Java Notepad");
			window.setVisible(true);
			
			window.setDefaultCloseOperation(window.EXIT_ON_CLOSE);
			wordPad.setFont(new Font("Times New Roman", Font.BOLD, 16));
			wordPad.setDragEnabled(true);
			window.getContentPane().setLayout(new BorderLayout());
			window.getContentPane().add(wordPad);
			
			areaScrollPane = new JScrollPane(wordPad);
	        this.areaScrollPane.setVerticalScrollBarPolicy(
	                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	        this.areaScrollPane.setHorizontalScrollBarPolicy(
	                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	        this.areaScrollPane.setPreferredSize(new Dimension(250, 250));
	        window.getContentPane().add(areaScrollPane);
			
			newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
	        newFile.setMnemonic(KeyEvent.VK_N);
	        
	        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
	        open.setMnemonic(KeyEvent.VK_O);
	        
	        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
	        save.setMnemonic(KeyEvent.VK_S);
	        
			close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.CTRL_MASK));
	        close.setMnemonic(KeyEvent.VK_F4);
	        
	        undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
	        undo.setMnemonic(KeyEvent.VK_U);
	        
	        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
	        cut.setMnemonic(KeyEvent.VK_T);
	        
	        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
	        copy.setMnemonic(KeyEvent.VK_C);
	        
	        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
	        paste.setMnemonic(KeyEvent.VK_P);
	       
	        delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
	        delete.setMnemonic(KeyEvent.VK_D);
	        
	        selectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
	        selectAll.setMnemonic(KeyEvent.VK_A);
			
			
		}




		@Override
		public void actionPerformed(ActionEvent e) {
			
				if (e.getSource() == close)
				window.dispose();
			
				if(e.getSource()==newFile){
					
						wordPad.setText("");
				}
						
			
				if(e.getSource()==open){
					
						JFileChooser jfc = new JFileChooser();
						int option = jfc.showOpenDialog(window);
			            if (option == JFileChooser.APPROVE_OPTION) {
			                this.wordPad.setText("");
			                try {
			                    Scanner scan = new Scanner(new FileReader(jfc
			                            .getSelectedFile().getPath()));
			                    while (scan.hasNext())
			                        this.wordPad.append(scan.nextLine() + "\n");
			                } catch (Exception ex) {
			                    System.out.println(ex.getMessage());
			                }
			            }
				}
				
				if(e.getSource()==save){
					
					JFileChooser jfc = new JFileChooser();
					jfc.showSaveDialog(null);
					File savedFile = jfc.getSelectedFile();
					saveFile(savedFile,wordPad.getText());
				}
				
				if(e.getSource()==cut)
		        {
		            wordPad.cut();
		            }
		        if(e.getSource()==copy)
		        {
		        	wordPad.copy();
		        }
		        if(e.getSource()==paste)
		        {
		            wordPad.paste();
		        }
		        
		        if(e.getSource()==selectAll)
		        {
		            wordPad.selectAll();
		        }
		        
		       if(e.getSource()==delete)
		        {
		            wordPad.replaceSelection("");
		        }
		        
		        /*if(e.getSource()==undo)
		        {
		        	undo();
		        	
}
		        }
		        
		        */
		       
		     /*  if(e.getSource()==wordCount)
		        {
		    	   countLineWordChar(wordPad);
		        }*/
				
				if (e.getSource() == this.authors) {
		            JOptionPane jp1 = new JOptionPane();
		            jp1.showMessageDialog(null,
		                    "Created by \n\nMahbubur Rahman\nJamiur Jami\nFarhin Farhad\nFarin Tasneem\nAbdur Roni ");
		            
				}
		}




	/*	public static int[] countLineWordChar(String wordPad2)
	     {
	          int[] countInfo = new int[3]; 
	          int lineCount = 0;
	          int wordCount = 0;
	          int charCount = 0; 
	          
	          try 
	          {
	               FileReader fr = new FileReader(wordPad2);
	               StreamTokenizer st = new StreamTokenizer(fr);
	               
	               st.eolIsSignificant(true);
	               
	               while(st.nextToken() != StreamTokenizer.TT_EOF)
	               {
	                    switch(st.ttype)
	                    {
	                         case StreamTokenizer.TT_WORD:
	                         wordCount++;
	                         String wordRead = st.sval; 
	                         charCount += wordRead.length();
	                         break;
	                         
	                         case StreamTokenizer.TT_EOL:
	                         lineCount++;
	                         break;
	                         
	                         default:
	                         break;
	                    }
	               }
	          }
	          catch (FileNotFoundException fnfe)
	          {
	               fnfe.showFileNotFoundError();
	          }
	          catch (IOException ioe)
	          {
	               JOptionPane.showMessageDialog(null, ioe.toString(), "Error", 
	               JOptionPane.ERROR_MESSAGE);
	          }
	               
	          countInfo[0] = lineCount;
	          countInfo[1] = wordCount;
	          countInfo[2] = charCount;
	          
	          return countInfo;
	          
	     }
	     {
	          return countInfo[0]; 
	     }
	     {
	          return countInfo[1]; 
	     }
	     {
	          return countInfo[2]; 
	     }
*/



		public void saveFile(File file,String contents){
			
			// you will implement this method by writing the contents of the textarea to the file
		try{	
			FileWriter fw = new FileWriter(file);
			fw.write(contents);
			fw.close();
		}
		
		catch(Exception e){}
			
		}
		
		public String openFile(File file){
			
			Scanner scan;
			String contents = "";
			try {
				scan = new Scanner(file);
			 
			
			
			while(scan.hasNextLine())
				contents = contents + scan.nextLine();
			}
			catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// read the contents of the file and return it in the form of a String
			return contents;
		}
	
		public static void main(String[] args) {
			
			new NotePad();
			

	}



		@Override
		public void keyPressed(KeyEvent e) {
			
			if(e.getKeyChar()=='t'){
				
				JOptionPane.showMessageDialog(null,"You have entered a T");
				
			}
			
			
			
		}



		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}



		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}


		
	
}

