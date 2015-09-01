

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Stack;

import javax.swing.*;


public class PaintWindow extends JFrame{
	

	private JFrame f;
	private JPanel p1;
	private JPanel p2;
	private JMenuBar bar;
	private JMenu menu;
	private JMenuItem item1;
	private JMenuItem item2;
	private JMenuItem item3;
	private DrawPad drawPad;
	private GridBagConstraints c1 ;
	private GridBagConstraints c ;
	
	
	public PaintWindow(){
		setFrame();
		setButtons();	
		setBackground(Color.WHITE);
	}
	
	
	private void setFrame(){
		f = new JFrame("Paint Application");
		f.setExtendedState(JFrame.MAXIMIZED_BOTH);//set window to full screen
		Color myColor = new Color(55,55,55);
		bar = new JMenuBar();
		f.setJMenuBar(bar);
		menu = new JMenu("File");
		bar.add(menu);
		JLabel l = new JLabel();
		l.setFont(new Font("Serif", Font.BOLD, 50));
		l.setText("PAINT APP");
		l.setForeground(myColor);
		p2 = new JPanel(new GridBagLayout());
		ImageIcon file = new ImageIcon("file.png");
		ImageIcon save = new ImageIcon("floppy.png");
		ImageIcon load = new ImageIcon("folder.png");
		
		
		item1 = new JMenuItem("New File",file);
		item1.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				drawPad.shape = 0;
				drawPad.s.clear();
				drawPad.current=-1;		
				drawPad.undo.add((LinkedList) drawPad.cpy.copy(drawPad.s));
				drawPad.redo.clear();
				
			}
			
		});
//		item2 = new JMenuItem("Save File",save);
//		item2.addActionListener(new ActionListener() {
//			
//			public void actionPerformed(ActionEvent e) {
//				
//			//	SaveLoad sl = new SaveLoad() ;
//			//	String filename = sl.savefilechooser() ;
//			//	sl.save(drawPad.s, filename);
//			}
//		});
//
//		item3 = new JMenuItem("Load Older Files",load);
//		item3.addActionListener(new ActionListener() {
//			
//			public void actionPerformed(ActionEvent e) {
//				SaveLoad sl = new SaveLoad (); 
//				String filename = sl.openfilechooser() ;
//				LinkedList<Shape> o= new LinkedList<Shape> () ;
//				o=(LinkedList<Shape>)sl.load(filename) ;
//				if (o.size()!=0){
//					drawPad.s=o ;
//				}
//				
//			}
//		});
		menu.add(item1);
		menu.addSeparator();
//		menu.add(item2);
//		menu.addSeparator();
//		menu.add(item3);

		JLabel background1 = new JLabel(new ImageIcon("paint.jpg"));
//		JPanel p3 = new JPanel();
//		p3.add(background1);
//		f.add(p3,BorderLayout.EAST);
		drawPad = new DrawPad();
		f.add(drawPad,BorderLayout.CENTER);
		p1 = new JPanel(new GridBagLayout());
		c1 = new GridBagConstraints();
		c = new GridBagConstraints();
		c.insets = new Insets(10,10,10,10);
		c.gridx = 0;
		c.gridy = 1;
		
//		Color c = c.
		c1.insets = new Insets(20,20,20,20);
		p1.setBackground(myColor);
		p2.setBackground(myColor);
//		f.add(l, BorderLayout.NORTH);
		f.add(p1,BorderLayout.BEFORE_FIRST_LINE);
		f.add(p2,BorderLayout.WEST);
		
//		f.pack();
		
	}
	
	private void setButtons (){
		ImageIcon circle = new ImageIcon("circle.png");

		JButton b1 = new JButton("Circle",circle);
		b1.setBackground(new Color(176,176,176));
		b1.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				drawPad.shape=1;
			}
			
		});
		p1.add(b1,c);
		c.gridx = 1;
		c.gridy = 1;
		ImageIcon rect = new ImageIcon("rectangle.png");

		JButton b2 = new JButton("Rectangle",rect);
		b2.setBackground(new Color(176,176,176));
		b2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				drawPad.shape=2;
				
			}
			
		});
		p1.add(b2, c);
		
		c.gridx = 2;
		c.gridy = 1;
		ImageIcon triangle = new ImageIcon("triangle.png");
		JButton b3 = new JButton("Triangle",triangle);
		b3.setBackground(new Color(176,176,176));
		b3.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				drawPad.shape=3;
				
			}
			
		});
		p1.add(b3, c);
		
		c.gridx = 3;
		c.gridy = 1;
		ImageIcon ellipse = new ImageIcon("ellipse.png");

		JButton b4 = new JButton("Ellipse",ellipse);
		b4.setBackground(new Color(176,176,176));
		b4.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				drawPad.shape=4;
				
			}
			
		});
		p1.add(b4, c);
		
		c.gridx = 4;
		c.gridy = 1;
		ImageIcon square = new ImageIcon("square.png");
		JButton b5 = new JButton("Square",square);
		b5.setBackground(new Color(176,176,176));
		b5.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				drawPad.shape=5;
				
			}
			
		});
		
		p1.add(b5, c);
		
		
		c.gridx = 5;
		c.gridy = 1;
		ImageIcon line = new ImageIcon("line.png");

		JButton b9 = new JButton("Line",line);
		b9.setBackground(new Color(176,176,176));
		b9.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				drawPad.shape=9;
				
			}
			
		});
		p1.add(b9, c);
		
		
		c.gridx = 6;
		c.gridy = 1;
		ImageIcon roundedrect = new ImageIcon("rounded.png");

		JButton b6 = new JButton("Rounded Rectangle",roundedrect);
		b6.setBackground(new Color(176,176,176));
		b6.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				drawPad.shape=6;
				
			}
			
		});
		p1.add(b6, c);
		
		
		c.gridx = 7;
		c.gridy = 1;
		ImageIcon load = new ImageIcon("loadshape.png");

		
		JButton b14 = new JButton("Load Rounded Rectangle",load);
		b14.setBackground(new Color(176,176,176));
		b14.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				LoaderFileChooser lf = new LoaderFileChooser() ;
				lf.cfile(); 
				drawPad.shape = 14;
			}
			
			
		});
		p1.add(b14, c);
		
		c.gridx = 1;
		c.gridy = 3;
		ImageIcon redo = new ImageIcon("Redo-icon.png");
		JButton b7 = new JButton("REDO",redo);
		b7.setBackground(new Color(176,176,176));
		b7.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				drawPad.shape=0;
				if(!drawPad.redo.isEmpty()){
					drawPad.undo.add((LinkedList) drawPad.cpy.copy(drawPad.redo.pop()));
					
					drawPad.s=(LinkedList<Shape>) drawPad.cpy.copy(drawPad.undo.peek());
				}
				
				drawPad.current = drawPad.s.size()-1;
				
			}
			
		});
		p2.add(b7, c);
		
		c.gridx = 1;
		c.gridy = 4;
		ImageIcon undo = new ImageIcon("Undo.png");
		JButton b8 = new JButton("UNDO",undo);
		b8.setBackground(new Color(176,176,176));
		b8.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				drawPad.shape=0;
				if(!drawPad.undo.isEmpty()){
					drawPad.redo.add((LinkedList) drawPad.cpy.copy(drawPad.undo.pop()));
					if(!drawPad.undo.isEmpty())
						drawPad.s=(LinkedList<Shape>) drawPad.cpy.copy(drawPad.undo.peek());
					else
						drawPad.s.clear();
				}
				else
					drawPad.s.clear();
				
			}
			
			
		});
		p2.add(b8, c);
		

		
		c.gridx = 1;
		c.gridy = 5;
		ImageIcon del = new ImageIcon("del.png");
		JButton b10 = new JButton("Delete",del);
		b10.setBackground(new Color(176,176,176));
		b10.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				drawPad.shape=10;
				LinkedList<Shape> temp = new LinkedList<Shape>();
				for(int i=0;i<drawPad.s.size();i++){
					if(!drawPad.s.get(i).isSelected())
						temp.add(drawPad.s.get(i));
				}
				
				drawPad.s=temp; 
				drawPad.current=-1;		
				drawPad.undo.add((LinkedList) drawPad.cpy.copy(drawPad.s));
				drawPad.redo.clear();
			}
			
		});
		p2.add(b10, c);
		
		c.gridx = 1;
		c.gridy = 6;
		ImageIcon fill = new ImageIcon("fill.png");
		JButton b11 = new JButton("Color",fill);
		b11.setBackground(new Color(176,176,176));
		b11.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				drawPad.shape=11;
				Color co = JColorChooser.showDialog(null,  "Pick a Fill", Color.BLACK);
				//change color
				for(int i = 0;i<drawPad.s.size();i++){
					if(drawPad.s.get(i).isSelected()){
						drawPad.s.get(i).c = co;
					}
				}
				
				drawPad.undo.add((LinkedList) drawPad.cpy.copy(drawPad.s));
				drawPad.redo.clear();
				
				
				
			}
			
		});
		p2.add(b11, c);
		
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
