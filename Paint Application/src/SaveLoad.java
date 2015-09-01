//
//
//
//import java.beans.XMLDecoder;
//import java.beans.XMLEncoder;
//import java.io.BufferedInputStream;
//import java.io.BufferedOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.ObjectOutputStream;
//import java.util.LinkedList;
//
//import javax.swing.JEditorPane;
//import javax.swing.JFileChooser;
//import javax.swing.JFrame;
//import javax.swing.filechooser.FileNameExtensionFilter;
//
//import com.thoughtworks.xstream.XStream;
//
//
//public class SaveLoad {
//	
//	//save
//	public void save (LinkedList<Shape> o , String filename ) {
//		if (filename=="cancel"){
//			return ; 
//		}
//		try {
//			File f=new File (filename ); 
//			if (!f.exists()){
//				f.createNewFile() ;
//			}
//			XStream x= new XStream () ;
//			FileOutputStream out = new FileOutputStream(f);
//			x.toXML(o, out);
//		}catch(Exception e) {
//			
//		}
//		
//	}
//	public String savefilechooser (){
//		JFrame parentFrame = new JFrame();
//		JFileChooser fileChooser = new JFileChooser();
//		fileChooser.setDialogTitle("Specify a file to save");   
//		int userSelection = fileChooser.showSaveDialog(parentFrame);
//		if (userSelection == JFileChooser.APPROVE_OPTION) {
//		    File fileToSave = fileChooser.getSelectedFile();
//		    String tmp =  fileToSave.getAbsolutePath();
//		    tmp+=".xml" ;
//		    return tmp ;
//		}
//		return "cancel" ;
//	}
//
//
//
//	
//	
//	//open 
//	public LinkedList<Shape> load (String filename ) {
//		LinkedList<Shape> o = new LinkedList<Shape> (); 
//		if (filename == "cancel"){
//			return o ;
//		}		
//		try {
//			FileInputStream fileinput = new FileInputStream(filename) ;
//			XStream x = new XStream();
//			o = (LinkedList<Shape>) x.fromXML(fileinput );
//		}catch (Exception e ){
//			
//		}
//		return o ;
//	}
//	public String openfilechooser () {
//		JFrame  parent = new JFrame () ;
//		JFileChooser chooser = new JFileChooser();
//		FileNameExtensionFilter filter = new FileNameExtensionFilter(
//		    "XML Files", "xml");
//		chooser.setFileFilter(filter);
//		int returnVal = chooser.showOpenDialog(parent);
//		if(returnVal == JFileChooser.APPROVE_OPTION) {
//		   return chooser.getSelectedFile().getPath();
//		}
//		return "cancel" ;
//	}
//	
//	
//	
//	
//}
//
//
//
//
//
//
//
//
//
//
//
//
////try {
////XMLEncoder xwriter  = new XMLEncoder (new BufferedOutputStream (new FileOutputStream (f ))) ;
////xwriter.writeObject (o) ;
////xwriter.close () ;
////}catch (FileNotFoundException e ){
////
////}
//
//
//
//
//
//
////XStream x= new XStream  () ;
////String xml = x.toXML(o) ;
////ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
////out.writeObject(xml);
//
//
//
////try {
////XMLDecoder xreader = new XMLDecoder (new BufferedInputStream (new FileInputStream (filename))) ;
////o=(LinkedList)xreader.readObject();
////}catch (FileNotFoundException e ){
////System.out.println("file corrupted");
////}
//
//
//
////XStream x= new XStream () ;
////o = (LinkedList) x.fromXML(new File (filename) );
