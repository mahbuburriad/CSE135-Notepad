package shop;


import javax.swing.*;

import java.awt.event.*;
import java.awt.*;

public class Shop extends JFrame {
	
	    private JList leftlist;
	    private JList rightlist;
	
	    private JButton calc;
		private static String[] stuff = {"Rice","Chicken","Beef","Mutton","Milk","Coffee"};
		
		public void initGUI(){
			
			
		}
		public Shop(){
		
			super("Shop");
			setLayout(new FlowLayout());
			
			leftlist = new JList(stuff);
		
			leftlist.setVisibleRowCount(6);
			leftlist.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			add(new JScrollPane(leftlist));
			
			 calc = new JButton("Calculate");
			 calc.addActionListener(  
					 new ActionListener(){
						 
						 public void actionPerformed(ActionEvent event) {
							 rightlist.setListData(leftlist.getSelectedValues());
							
							
						 }
					 }
					 );
			 add(calc);
			 
			 rightlist = new JList();
			 rightlist.setVisibleRowCount(6);
			 rightlist.setFixedCellHeight(20);
			 rightlist.setFixedCellWidth(100);
			 rightlist.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			 add(new JScrollPane(rightlist));
			 

	
}
}
