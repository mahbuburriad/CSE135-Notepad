package notepad2;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;
import java.awt.datatransfer.*;

/**
   A simple text editor that supports Open, Create, Save, Copy/Cut/Paste,
   Word Count, Find, and Replace operations.
   @author Sugiharto Widjaja
   @version 08/22/02
*/
public class SimpleNotePad extends JFrame
{
   /**
      Construct the frame that has the text area and all the menu bars
   */
   public SimpleNotePad()
   {
      setSize(WIDTH, HEIGHT);
      setTitle(DEF_TITLE);
      askToSave = false;
      chooser = new JFileChooser();
      chooser.setCurrentDirectory(currDir);
      chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
      JPanel panel = new JPanel();
      text = new JTextArea(ROWS, COLS);
      text.setTabSize(TABSIZE);
      text.setLineWrap(true);
      JScrollPane pane = new JScrollPane(text);
      panel.add(pane);
      Container contentPane = getContentPane();
      contentPane.add(panel);
      clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
      currDir = new File(".");
      setMenuBar();
   }

   /**
      Adding the menus to the menubar
   */
   private void setMenuBar()
   {
      JMenuBar menuBar = new JMenuBar();
      setJMenuBar(menuBar);
      file = new JMenu("File");
      file.setMnemonic('F');
      help = new JMenu("Help");
      help.setMnemonic('H');
      edit = new JMenu("Edit");
      edit.setMnemonic('E');
      tool = new JMenu("Tool");
      tool.setMnemonic('T');
      open = new JMenuItem("Open", 'O');
      close = new JMenuItem("Close");
      exit = new JMenuItem("Exit", 'E');
      newMenu = new JMenuItem("New", 'N');
      save = new JMenuItem("Save As", 'S');
      about = new JMenuItem("About Java NotePad...");
      selectAll = new JMenuItem("Select All");
      copy = new JMenuItem("Copy");
      cut = new JMenuItem("Cut");
      find = new JMenuItem("Find");
      replace = new JMenuItem("Replace");
      paste = new JMenuItem("Paste");
      wordCount = new JMenuItem("Word Count", 'W');
      setSubMenu();
      menuBar.add(file);
      menuBar.add(edit);
      menuBar.add(tool);
      menuBar.add(help);
      addAllListeners();
   }

   /**
      Adding the sub menu items to each menu
   */
   private void setSubMenu()
   {
      copy.setEnabled(false);
      cut.setEnabled(false);
      find.setEnabled(false);
      replace.setEnabled(false);
      newMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
      open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
      save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
      copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
      cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
      paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
      file.add(newMenu);
      file.add(open);
      file.add(close);
      file.add(save);
      file.addSeparator();
      file.add(exit);
      edit.add(selectAll);
      edit.addSeparator();
      edit.add(copy);
      edit.add(cut);
      edit.add(paste);
      tool.add(wordCount);
      tool.add(find);
      tool.add(replace);
      help.add(about);
   }

   /**
      Add the listeners associated with each sub menus.
      Also adding the document listener for the text area
      to detect changes in the document
   */
   private void addAllListeners()
   {
      addWinListener();
      addNewListener();
      addOpenListener();
      addCloseListener();
      addSaveListener();
      addExitListener();
      addAboutListener();
      addSAListener();
      addCopyListener();
      addCutListener();
      addPasteListener();
      addWordCountListener();
      addFindListener();
      addReplaceListener();
      DocumentListener listener = new textListener();
      text.getDocument().addDocumentListener(listener);
   }

   /**
      Add the window listener
   */
   private void addWinListener()
   {
      this.addWindowListener(new
         WindowAdapter()
         {
            public void windowClosing(WindowEvent e)
            {
               saveBeforeExit();
            }
         });
   }

   /**
      Add the listener to the "Open" submenu
   */
   private void addOpenListener()
   {
      open.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent e)
            {
               int result = chooser.showOpenDialog(SimpleNotePad.this);
               if(result == JFileChooser.APPROVE_OPTION)
               {
                  fileName = chooser.getSelectedFile().getPath();
                  setTitle(fileName);
                  text.selectAll();
                  int begin = text.getSelectionStart();
                  int end = text.getSelectionEnd();
                  text.replaceRange("", begin, end);
                  putTextIntoTextArea();
                  openForTheFirstTime = true;
                  askToSave = false;
                  if(findDialog != null)
                     findDialog.initToZero();
                  if(repDialog != null)
                     repDialog.initToZero();
                  text.setCaretPosition(0);
               }
            }
         });
   }

   /**
      Add the listener to the "Close" submenu
   */
   private void addCloseListener()
   {
      close.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent e)
            {
               text.selectAll();
               if(text.getSelectedText() != null && askToSave)
               {
                  int select = JOptionPane.showConfirmDialog(SimpleNotePad.this,
                     "Save the texts to a file before Closing?",
                     "JavaNotePad", JOptionPane.YES_NO_CANCEL_OPTION,
                     JOptionPane.WARNING_MESSAGE, null);
                  if(select == JOptionPane.YES_OPTION)
                  {
                     int result = chooser.showSaveDialog(SimpleNotePad.this);
                     if(result == JFileChooser.APPROVE_OPTION)
                     {
                        fileName = chooser.getSelectedFile().getPath();
                        saveToFile(fileName);
                        askToSave = false;
                        //text.setText("");
                        //setTitle(DEF_TITLE);
                     }
                  }
                  else if(select == JOptionPane.NO_OPTION)
                  {
                     text.setText("");
                     setTitle(DEF_TITLE);
                  }
                  else if(select == JOptionPane.CANCEL_OPTION)
                     text.setCaretPosition(0);
               }
               else
               {
                  text.setText("");
                  setTitle(DEF_TITLE);
               }
            }
         });
   }

   /**
      Add the listener to the "Exit" submenu
   */
   private void addExitListener()
   {
      exit.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent e)
            {
               saveBeforeExit();
            }
         });
   }

   /**
      Ask user whether they want to save the texts to a file before exiting
   */
   private void saveBeforeExit()
   {
      text.selectAll();
      if(text.getSelectedText() != null && askToSave)
      {
         int select = JOptionPane.showConfirmDialog(SimpleNotePad.this,
            "Save the texts to a file before exiting?",
            "JavaNotePad", JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.WARNING_MESSAGE, null);
         if(select == JOptionPane.YES_OPTION)
         {
            int result = chooser.showSaveDialog(SimpleNotePad.this);
            if(result == JFileChooser.APPROVE_OPTION)
            {
               fileName = chooser.getSelectedFile().getPath();
               saveToFile(fileName);
            }
         }
         else if(select == JOptionPane.NO_OPTION)
            System.exit(0);
         else if(select == JOptionPane.CANCEL_OPTION)
            text.setCaretPosition(0);
      }
      else
         System.exit(0);
   }

   /**
      Add the listener to the "New" submenu
   */
   private void addNewListener()
   {
      newMenu.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent e)
            {
               text.selectAll();
               int begin = text.getSelectionStart();
               int end = text.getSelectionEnd();
               text.replaceRange("", begin, end);
               setTitle(DEF_TITLE);
               if(findDialog != null)
                  findDialog.initToZero();
            }
         });
   }

   /**
      Add the listener to the "Save As" submenu
   */
   private void addSaveListener()
   {
      save.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent e)
            {
               int result = chooser.showSaveDialog(SimpleNotePad.this);
               if(result == JFileChooser.APPROVE_OPTION)
               {
                  fileName = chooser.getSelectedFile().getPath();
                  saveToFile(fileName);
                  askToSave = false;
               }
            }
         });
   }

   /**
      Add the listener to the "Word Count" submenu
   */
   private void addWordCountListener()
   {
      wordCount.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent e)
            {
               text.selectAll();
               String theText = text.getSelectedText();

               if(theText != null)
               {
                  text.setCaretPosition(0);
                  WordCounter counter = new WordCounter(theText);
                  counter.countWord();
                  counter.countLine();
                  wordDialog = new WordCountDialog(SimpleNotePad.this, counter);
                  wordDialog.show();
               }
            }
         });
   }

   /**
      Add the listener to the "Find" submenu
   */
   private void addFindListener()
   {
      find.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent e)
            {
               findDialog = new FindDialog(SimpleNotePad.this, text);
               findDialog.show();
            }
         });
   }

   /**
      Add the listener to the "Replace" submenu
   */
   private void addReplaceListener()
   {
      replace.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent e)
            {
               repDialog = new ReplaceDialog(SimpleNotePad.this, text);
               repDialog.show();
            }
         });
   }

   /**
      Attempt to save the contents of the text area to a file
      @param filename the file name
   */
   private void saveToFile(String filename)
   {
      writeToFile(filename);
      setTitle(filename);
   }

   /**
      Add the listener to the "About" menu
   */
   private void addAboutListener()
   {
      about.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent e)
            {
               if(dialog == null)
                  dialog = new AboutDialog(SimpleNotePad.this);
               dialog.show();
            }
         });
   }

   /**
      Add the listener to the "Select all" submenu
   */
   private void addSAListener()
   {
      selectAll.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent e)
            {
               text.selectAll();
            }
         });
   }

   /**
      Add the listener to the "Copy" submenu
   */
   private void addCopyListener()
   {
      copy.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent e)
            {
               String selText = text.getSelectedText();
               if(selText != null)
               {
                  StringSelection text = new StringSelection(selText);
                  clipBoard.setContents(text, null);
               }
            }
         });
   }

   /**
      Add the listener to the "Cut" submenu
   */
   private void addCutListener()
   {
      cut.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent e)
            {
               if(text.getSelectedText() != null)
               {
                  int begin = text.getSelectionStart();
                  int end = text.getSelectionEnd();
                  text.replaceRange("", begin, end);
               }
            }
         });
   }

   /**
      Add the listener to the "paste" submenu
   */
   private void addPasteListener()
   {
      paste.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent e)
            {
               int pos = text.getCaretPosition();
               Transferable contents = clipBoard.getContents(this);
               if(contents != null)
               {
                  try
                  {
                     String selText = (String) (contents.getTransferData(DataFlavor.stringFlavor));
                     text.insert(selText, pos);
                  }
                  catch(Exception c) {}
               }
            }
         });
   }

   /**
      Open a requested file and display the content of file in the text area
      @throws FileNotFoundException
      @throws IOException
   */
   private void putTextIntoTextArea()
   {
      try
      {
         FileReader file = new FileReader(fileName);
         BufferedReader in = new BufferedReader(file);
         boolean done = false;
         while (!done)
         {
            String line = in.readLine();
            if(line == null)
               done = true;
            else
               text.append(line + "\n");
         }
      }
      catch(FileNotFoundException e)
      {
         text.append(fileName +  " not found \n");
      }
      catch(IOException e)
      {
         text.append("Error when trying to read " + fileName + " \n");
      }
   }

   /**
      Save the contents of the text area to a file
      @throws IOException
   */
   private void writeToFile(String filename)
   {
      try
      {
         text.selectAll();
         String input = text.getSelectedText();
         PrintWriter out = new PrintWriter(new FileOutputStream(filename, false));
         int start = 0;
         String temp = "";
         String chars = "";
         if(input.indexOf("\n") == -1)
            out.println(input);
         else
         {
            for(int i = 0; i < input.length(); i++)
            {
               chars = chars + input.charAt(i);
               if(chars.equals("\n"))
               {
                  out.println(temp);
                  start = i + 1;
                  temp = "";
               }
               else
                  temp = temp + input.charAt(i);
               chars = "";
            }
         }
         out.close();
      }
      catch(IOException f) {}
   }

   /** The default frame title
   */
   public static final String DEF_TITLE = "Untitled - Java NotePad";
   /** The width of the frame
   */
   public static final int WIDTH = 950;
   /** The height of the frame
   */
   public static final int HEIGHT = 750;
   /** The number of rows of the text area
   */
   public static final int ROWS = 42;
   /** The number of columns of the text area
   */
   public static final int COLS = 85;
   /** The tab size
   */
   private static final int TABSIZE = 8;
   // The current directory
   private File currDir;
   // The file menu
   private JMenu file;
   //The edit menu
   private JMenu edit;
   // The help menu
   private JMenu help;
   // The tool menu
   private JMenu tool;
   // The open submenu
   private JMenuItem open;
   // The close submenu
   private JMenuItem close;
   // The exit submenu
   private JMenuItem exit;
   // The new submenu
   private JMenuItem newMenu;
   // The save submenu
   private JMenuItem save;
   // The about submenu
   private JMenuItem about;
   // The select all submenu
   private JMenuItem selectAll;
   // The copy submenu
   private JMenuItem copy;
   // The cut submenu
   private JMenuItem cut;
   // The paste submenu
   private JMenuItem paste;
   // The word count submenu
   private JMenuItem wordCount;
   // The find submenu
   private JMenuItem find;
   // The replace submenu
   private JMenuItem replace;
   // The replace dialog box
   private ReplaceDialog repDialog;
   // The about dialog box
   private AboutDialog dialog;
   // The dialog box display number of words, characters, and lines
   private WordCountDialog wordDialog;
   // The dialog box to perform find operation
   private FindDialog findDialog;
   // The file chooser
   private JFileChooser chooser;
   // The text area
   private JTextArea text;
   // The targer filename
   private String fileName;
   // The clipboard used to copy/paste operations
   private Clipboard clipBoard;
   // Is there any modifications to the text area?
   private boolean askToSave;
   // File opened for the first time (not modified yet)?
   private boolean openForTheFirstTime;

   /**
      The listener class for the text area
   */

   private class textListener implements DocumentListener
   {
      /**
         This method is invoked whenever something is inserted into the text area
      */
      public void insertUpdate(DocumentEvent e)
      {
         if(openForTheFirstTime)
            openForTheFirstTime = false;
         askToSave = true;
         copy.setEnabled(true);
         cut.setEnabled(true);
         find.setEnabled(true);
         replace.setEnabled(true);
      }

      /**
         This method is invoked whenever something is inserted into the text area
      */
      public void removeUpdate(DocumentEvent e)
      {
         askToSave = true;
         if(text.getText().equals(""))
         {
            copy.setEnabled(false);
            cut.setEnabled(false);
            find.setEnabled(false);
            replace.setEnabled(false);
         }
      }

      /**
         Not used
      */
      public void changedUpdate(DocumentEvent e) {}
   }
}

/**
   The dialog box that contains the information about this program.
   @author Taken from "Core Java 2, Vol 1"
   @version 08/22/02
*/

class AboutDialog extends JDialog
{
   /**
      Create the dialog box
   */
   public AboutDialog(JFrame master)
   {
      super(master, "About Java NotePad", true);
      Container contentPane = getContentPane();
      contentPane.add(new JLabel(
         "<HTML><H1>Java NotePad</H1>"
         + "Copyright 2002 Sugiharto Widjaja<BR>"
         + "All Rights Reserved</HTML>"),
         BorderLayout.CENTER);
      JButton ok = new JButton("Ok");
      ok.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent e)
            {
               setVisible(false);
            }
         });

      JPanel pane = new JPanel();
      pane.add(ok);
      contentPane.add(pane, BorderLayout.SOUTH);

      setSize(WSIZE, HSIZE);
   }

   /** The width of the frame
   */
   public static final int WSIZE = 300;
   /** The height of the frame
   */
   public static final int HSIZE = 150;
}

/**
   The dialog box that displays the number of words, lines, and characters.
   @author Sugiharto Widjaja
   @version 08/22/02
*/

class WordCountDialog extends JDialog
{
   /**
      Create the dialog box
   */
   public WordCountDialog(JFrame master, WordCounter counter)
   {
      super(master, "Word Count", false);
      Container contentPane = getContentPane();
      contentPane.add(new JLabel(
         "<HTML>Statistics :<BR><HR>"
         + "Words : " + counter.getNumOfWords() + "<BR>"
         + "Characters : " + counter.getNumOfChars() + "<BR>"
         + "Lines : " + counter.getNumOfLines() + "</HTML>"),
         BorderLayout.CENTER);
      JButton close = new JButton("Close");
      close.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent e)
            {
               setVisible(false);
            }
         });
         JPanel pane = new JPanel();
         pane.add(close);
         contentPane.add(pane, BorderLayout.SOUTH);

         setSize(WSIZE, HSIZE);
   }

   /** The width of the frame
   */
   public static final int WSIZE = 300;
   /** The height of the frame
   */
   public static final int HSIZE = 150;
}

/**
   The dialog box where user can use to search for certain words on the text.
   @author Sugiharto Widjaja
   @version 08/22/02
*/

class FindDialog extends JDialog
{
   /**
      Construct the find dialog box
      @param master the parent frame
      @param text the text area
   */
   public FindDialog(JFrame master, JTextArea text)
   {
      super(master, "Find", false);
      this.master = master;
      this.text = text;
      Container content = getContentPane();
      content.setLayout(new BorderLayout());
      fText = new JTextField(10);
      mCase = new JCheckBox("Match Case");
      fButton = new JButton("Find Next");
      cButton = new JButton("Close");
      JPanel pane1 = new JPanel();
      pane1.add(new JLabel("Find : "));
      pane1.add(fText);
      pane1.add(mCase);
      content.add(pane1, BorderLayout.CENTER);
      JPanel pane2 = new JPanel();
      pane2.setLayout(new GridLayout(1, 2));
      pane2.add(fButton);
      pane2.add(cButton);
      content.add(pane2, BorderLayout.SOUTH);
      addListener();

      setSize(WSIZE, HSIZE);
   }

   /**
      Set the search index to the beginning of the column of the text area
   */
   public void initToZero()
   {
      init = 0;
   }

   /**
      Add needed action listener
   */
   private void addListener()
   {
      mCase.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent e)
            {
               if(mCase.isSelected())
                  caseSensitive = true;
               else
                  caseSensitive = false;
            }
         });
      fButton.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent e)
            {
               String sText = fText.getText();
               if(sText != null)
               {
                  String tSearch = text.getText();
                  int size = tSearch.length();
                  boolean endOfSearch = false;
                  while(!endOfSearch)
                  {
                     int index = 0;
                     if(caseSensitive)
                        index = tSearch.indexOf(sText, init);
                     else
                        index = tSearch.toLowerCase().indexOf(sText.toLowerCase(), init);
                     if(index != -1)
                     {
                        endOfSearch = true;
                        text.select(index, index + sText.length());
                        init = text.getCaretPosition();
                     }
                     else
                     {
                        endOfSearch = true;
                        JOptionPane.showMessageDialog(master, "\"" + sText + "\"" + " not found ");
                     }
                  }
               }
            }
         });

      cButton.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent e)
            {
               setVisible(false);
            }
         });
   }

   /** The width of the frame
   */
   public static final int WSIZE = 300;
   /** The height of the frame
   */
   public static final int HSIZE = 150;
   // The parent frame
   private JFrame master;
   // index to search from
   private int init;
   // The text area
   private JTextArea text;
   // The text to search for
   private JTextField fText;
   // Perform case sensitive search or not
   private JCheckBox mCase;
   // The find button
   private JButton fButton;
   // The close button
   private JButton cButton;
   // Is this case sensitive search
   private boolean caseSensitive;
}

/**
   The dialog box where user can use to find and replace certain words on the text.
   @author Sugiharto Widjaja
   @version 08/22/02
*/

class ReplaceDialog extends JDialog
{
   /**
      Construct the replace dialog box
      @param master the parent frame
      @param text the text area
   */
   public ReplaceDialog(JFrame master, JTextArea text)
   {
      super(master, "Replace", false);
      this.master = master;
      this.text = text;
      JPanel panel1 = new JPanel();
      JPanel panel2 = new JPanel();
      panel1.setLayout(new GridLayout(2,2));
      panel2.setLayout(new GridLayout(1,4));
      JLabel fLabel = new JLabel("Find :");
      JLabel rLabel = new JLabel("Replace w/ :");
      fText = new JTextField(10);
      rText = new JTextField(10);
      box = new JCheckBox("Match Case");
      find = new JButton("Find Next");
      replace = new JButton("Replace");
      replaceAll = new JButton("Replace all");
      close = new JButton("Close");
      panel1.add(fLabel);
      panel1.add(fText);
      panel1.add(rLabel);
      panel1.add(rText);
      panel2.add(find);
      panel2.add(replace);
      panel2.add(replaceAll);
      panel2.add(close);
      findAddListener();
      checkAddListener();
      repAddListener();
      repAllAddListener();
      closeAddListener();
      getContentPane().add(panel1, BorderLayout.NORTH);
      getContentPane().add(box, BorderLayout.CENTER);
      getContentPane().add(panel2, BorderLayout.SOUTH);

      setSize(WSIZE, HSIZE);
   }

   /**
      Set the search index to the beginning of the column of the text area
   */
   public void initToZero()
   {
      init = 0;
   }

   /**
      Add the listener for find button
   */
   private void findAddListener()
   {
      find.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent e)
            {

               String sText = fText.getText();
               if(sText != null)
               {
                  String tSearch = text.getText();
                  int size = tSearch.length();
                  boolean endOfSearch = false;
                  while(!endOfSearch)
                  {
                     index = 0;
                     init = text.getCaretPosition();
                     if(matchCase)
                        index = tSearch.indexOf(sText, init);
                     else
                        index = tSearch.toLowerCase().indexOf(sText.toLowerCase(), init);
                     if(index != -1)
                     {
                        endOfSearch = true;
                        text.select(index, index + sText.length());
                        init = text.getCaretPosition();
                     }
                     else
                     {
                        endOfSearch = true;
                        JOptionPane.showMessageDialog(master, "\"" + sText + "\"" + " not found ");
                     }
                  }
               }
            }
         });
   }

   /**
      Add the listener for replace button
   */
   private void repAddListener()
   {
      replace.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent e)
            {
               String from = fText.getText();
               if(from != null)
               {
                  String replacer = rText.getText();
                  if(index >= 0 && replacer.length() > 0)
                  {
                     int end = index + from.length();
                     text.replaceRange(replacer, index, end);
                     init = text.getCaretPosition();
                  }
               }
            }
         });
   }

   /**
      Add the listener for replace all button
   */
   private void repAllAddListener()
   {
      replaceAll.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent e)
            {
               String from = fText.getText();
               if(from != null)
               {
                  String tSearch = text.getText();
                  String replacer = rText.getText();
                  if(replacer.length() > 0)
                  {
                     if(matchCase)
                        index = tSearch.indexOf(from, init);
                     else
                        index = tSearch.toLowerCase().indexOf(from.toLowerCase(), init);
                     if(index == -1)
                        JOptionPane.showMessageDialog(master, "\"" + from + "\"" + " not found ");
                     else
                     {
                        while(index != -1)
                        {
                           int end = index + from.length();
                           text.replaceRange(replacer, index, end);
                           init = init + replacer.length() + 1;
                           tSearch = text.getText();
                           index = tSearch.indexOf(from, init);
                        }
                     }
                  }
               }
            }
         });
   }

   /**
      Add the listener for match case checkbox
   */
   private void checkAddListener()
   {
      box.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent e)
            {
               if(box.isSelected())
                  matchCase = true;
               else
                  matchCase = false;
            }
         });
   }

   /**
      Add the listener for the close button
   */
   private void closeAddListener()
   {
      close.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent e)
            {
               setVisible(false);
            }
         });
   }

   /** Thw width of the frame
   */
   public static final int WSIZE = 400;
   /** The height of the frame
   */
   public static final int HSIZE = 120;
   // The parent frame
   private JFrame master;
   // The textfield containing word to be find/replaced
   private JTextField fText;
   // The textfield containing the replacement word
   private JTextField rText;
   // The text area
   private JTextArea text;
   // The check box for case matching
   private JCheckBox box;
   // The find button
   private JButton find;
   // The replace button
   private JButton replace;
   // The replace all button
   private JButton replaceAll;
   // The close button
   private JButton close;
   // Do the case matching search?
   private boolean matchCase;
   // The initial index to search the word from
   private int init;
   // The current index in the text area
   private int index;
}
