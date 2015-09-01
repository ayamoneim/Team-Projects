package Memento;

public class CareTaker {
	
	private static Memento previous ;
	private SaveLoad saver = new SaveLoad();
	
	public void addMemento(Memento m){
		previous = m;
		saver.save(m, saver.savefilechooser());
	}
	
	public Memento getMemento(){
		//return saver.load(saver.openfilechooser());
		return previous ;
	}
	
}
