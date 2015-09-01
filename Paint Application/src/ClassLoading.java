

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ClassLoading extends ClassLoader{
	
    public ClassLoading(ClassLoader parent) {
    	super(parent);        
    }
    

    @SuppressWarnings("unchecked")
	public Class loadClass(String name) throws ClassNotFoundException {

        
        if(LoaderFileChooser.pacclassname!=name){
            return super.loadClass(name);
        }

        try {	
        	
        	File f = new File(LoaderFileChooser.filepn);

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
            
            return defineClass(LoaderFileChooser.pacclassname,classData, 0, classData.length);
//            return defineClass(pacclassname,classData, 0, classData.length);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
