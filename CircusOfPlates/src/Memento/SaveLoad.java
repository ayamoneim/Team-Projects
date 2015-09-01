package Memento;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.thoughtworks.xstream.XStream;


public class SaveLoad {
	
	//save
	public void save (Memento o , String filename ) {
		if (filename=="cancel"){
			return ; 
		}
		try {
			File f=new File (filename ); 
			if (!f.exists()){
				f.createNewFile() ;
			}
			XStream x= new XStream () ;
			FileOutputStream out = new FileOutputStream(f);
			x.toXML(o, out);
			 XMLEncoder encoder =
			           new XMLEncoder(
			              new BufferedOutputStream(
			                new FileOutputStream(filename)));
			        encoder.writeObject(o);
			        encoder.close();
		}catch(Exception e) {
			
		}
		
	}
	
	public String savefilechooser (){
		JFrame parentFrame = new JFrame();
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Specify a file to save");   
		int userSelection = fileChooser.showSaveDialog(parentFrame);
		if (userSelection == JFileChooser.APPROVE_OPTION) {
		    File fileToSave = fileChooser.getSelectedFile();
		    String tmp =  fileToSave.getAbsolutePath();
		    tmp+=".xml" ;
		    parentFrame.dispose();
		    return tmp ;
		}
	    parentFrame.dispose();
		return "cancel" ;
	}


	//open 
	public Memento load (String filename ) {
		Memento o = null; 
		if (filename == "cancel"){
			return o ;
		}		
		try {
			FileInputStream fileinput = new FileInputStream(filename) ;
			XStream x = new XStream();
			o = (Memento) x.fromXML(fileinput );
			
		}catch (Exception e ){
			
		}
		return o ;
	}
	
	public String openfilechooser () {
		JFrame  parent = new JFrame () ;
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		    "XML Files", "xml");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(parent);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			parent.dispose();
			return chooser.getSelectedFile().getPath();
		}
		parent.dispose();
		return "cancel" ;
	}
		
}
