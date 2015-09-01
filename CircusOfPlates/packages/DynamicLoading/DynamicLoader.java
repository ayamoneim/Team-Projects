package DynamicLoading;

import MainObjects.*;

public class DynamicLoader {
	public static String[][] names= new String[3][3];
	private LoaderFileChooser chooser = new LoaderFileChooser();
	
	public void start(){
			chooser.cfile();
			names[0][0]=chooser.filepn;
			names[0][1]=chooser.filen;
			names[0][2]=chooser.pacclassname;
		
			chooser.cfile();
			names[1][0]=chooser.filepn;
			names[1][1]=chooser.filen;
			names[1][2]=chooser.pacclassname;
		
			chooser.cfile();
			names[2][0]=chooser.filepn;
			names[2][1]=chooser.filen;
			names[2][2]=chooser.pacclassname;
	}
	
	public Shape createShape(int classNum , int imgNum , int x , int y){
		ClassLoader parentClassLoader = ClassLoading.class.getClassLoader();
		ClassLoading classLoader = new ClassLoading(parentClassLoader);
		
		Shape CS = null ;
		try {
			Class rcl = classLoader.loadClass(names[classNum][2] , classNum);
    		
			Class[] param1 = new Class [3] ;
    		param1[0]=param1[1]=param1[2]=Integer.class ;

    		//check shape parameters
    		CS =(Shape )rcl.getDeclaredConstructor(param1).newInstance(imgNum,x,y) ;
    		
		} catch (Exception  e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return CS ;
	}
	
}
