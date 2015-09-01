
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

public class LoaderFileChooser {
	public static String filepn="cancel" ; //"omar/circle.class" 
	public static String filen="cancel" ; //"circle.class"
	public static String pacclassname="cancel"  ; // "geometry.circle" 
	public void cfile(){
		JFrame  parent = new JFrame () ;
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		    ".class files", "class");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(parent);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
		    filepn= chooser.getSelectedFile().getPath();
		    filen = chooser.getSelectedFile().getName() ;
		    String[] tmp = filen.split("\\."); 
		    pacclassname = "Geometry."+ tmp[0]; 
		}
		else {
			filepn="cancel"; 
			filen="cancel" ;
		}
		
	}
	
}
