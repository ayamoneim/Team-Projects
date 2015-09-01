
/*
 * DeleteVertexMenuItem.java
 *
 * Created on March 21, 2007, 2:03 PM; Updated May 29, 2007
 *
 * Copyright March 21, 2007 Grotto Networking
 *
 */


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JMenuItem;

import edu.uci.ics.jung.visualization.VisualizationViewer;


public class DeleteVertexMenuItem<V> extends JMenuItem implements VertexMenuListener<V> {
    private V vertex;
    private VisualizationViewer visComp;
    
    public DeleteVertexMenuItem() {
        super("Delete Vertex");
        this.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
            	System.out.println(vertex);
            	
                visComp.getPickedVertexState().pick(vertex, false);
                
                visComp.getGraphLayout().getGraph().removeVertex(vertex);
                
                Iterator<GraphElements.MyVertex> it = visComp.getGraphLayout().getGraph().getVertices().iterator();
                
                int[]vertices = new int[visComp.getGraphLayout().getGraph().getVertexCount()];
                String[]oldVertices = new String[visComp.getGraphLayout().getGraph().getVertexCount()];
                int i = 0;
                while(it.hasNext()){
                	vertices[i] = Integer.parseInt(it.next().getName());
                	oldVertices[i] = ""+vertices[i];
                	i++;
                	
                }
                for(i = 0;i<visComp.getGraphLayout().getGraph().getVertexCount();i++){
                	for(int j = i+1;j<visComp.getGraphLayout().getGraph().getVertexCount()-1;j++){
                		if(vertices[j] >vertices[j+1]){
                			String temp0 = oldVertices[j];
                			oldVertices[j] = oldVertices[j+1];
                			oldVertices[j+1] = temp0;
                			
                			int temp = vertices[j];
                			vertices[j] = vertices[j+1];
                			vertices[j+1] = temp;
                		}
                	}
                }
                for(i = 0;i<visComp.getGraphLayout().getGraph().getVertexCount();i++){
                	if(vertices[i]!=i){
                		vertices[i] = i;
                		
                	}
                }
                Collection<GraphElements.MyVertex>c = visComp.getGraphLayout().getGraph().getVertices();
                i = 0;
                for(GraphElements.MyVertex ver:c){

                	for(int j =0;j<vertices.length;j++){
                		if(ver.getName().equals(oldVertices[j])){
                			ver.setName(""+vertices[j]);
                			break;
                		}
                	}
                	
                }
                visComp.repaint();
            }
        });
    }

 
    public void setVertexAndView(V v, VisualizationViewer visComp) {
        this.vertex = v;
        this.visComp = visComp;
        this.setText("Delete Vertex " + v.toString());
    }
    
}
