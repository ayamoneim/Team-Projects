package Memento;

import java.io.Serializable;

import MVC.*;

public class Memento implements Serializable{
	
	private Model model ;
	
	public Memento(Model m){
		model = m;
	}
	
	public Model getSaved(){
		return model;
	}
	
}
