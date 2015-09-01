package DynamicLoading;
import java.awt.Graphics;
import java.awt.Point;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import MainObjects.Shape;

public class ClassLoading extends ClassLoader{
	
    public ClassLoading(ClassLoader parent) {
    	super(parent);        
    }
    

    @SuppressWarnings("unchecked")
	public Class loadClass(String name , int classNum) throws ClassNotFoundException {

        
        if(DynamicLoader.names[classNum][2]!=name){
            return super.loadClass(name);
        }

        try {	
        	
        	File f = new File(DynamicLoader.names[classNum][0]);

            String url = f.toURI().toString();
            URL myUrl = new URL(url);
            URLConnection connection = myUrl.openConnection();
            InputStream input = connection.getInputStream();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int data = input.read();

            while(data != -1){
                buffer.write(data);
                data = input.read();
            }

            input.close();

            byte[] classData = buffer.toByteArray();
            
            return defineClass(DynamicLoader.names[classNum][2],classData, 0, classData.length);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    
    public static void main(String[] args){
    	LoaderFileChooser loader = new LoaderFileChooser();
    	loader.cfile();
    	ClassLoader parentClassLoader = ClassLoading.class.getClassLoader();
		ClassLoading classLoader = new ClassLoading(parentClassLoader);
		
		Shape CS=null ;
		if (LoaderFileChooser.filen!="cancel"){
			try {
				Class rcl = classLoader.loadClass(LoaderFileChooser.pacclassname);
	    		
				Class[] param1 = new Class [4] ;
	    		param1[0]=param1[1]=Integer.class ;
	    		param1[2]=param1[3]=String.class ;
	
	    		
	    		CS =(Shape )rcl.getDeclaredConstructor(param1).newInstance(20,100,"engi","alex") ;
	    	
			} catch (Exception  e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		
	}
}
