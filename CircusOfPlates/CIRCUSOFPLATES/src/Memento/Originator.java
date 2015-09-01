package Memento;

import MVC.*;

public class Originator {
	
	private Model model ;
	
	public void Set(Model m){
		model = m;
	}
	
	public Memento StoreInMemento(){
		return new Memento(model);
	}
	
	public Model RestoreFromMemento(Memento me){
		return me.getSaved();
	}
	
}
