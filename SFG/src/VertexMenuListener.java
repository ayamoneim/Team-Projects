
/*
 * VertexMenuListener.java
 *
 * Created on March 21, 2007, 1:50 PM; Updated May 29, 2007
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */


import edu.uci.ics.jung.visualization.VisualizationViewer;

public interface VertexMenuListener<V> {
    void setVertexAndView(V v, VisualizationViewer visView);    
}
