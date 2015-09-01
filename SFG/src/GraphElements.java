


import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;


public class GraphElements {
    
    public GraphElements() {
    }
    
    public static class MyVertex {
        private String name;
        
        public MyVertex(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


        
        public String toString() {
            return name;
        }
    }
    
    public static class MyEdge {
        private double weight;
        private String name;

        public MyEdge(String name) {
            this.name = name;
        }
    


        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
            this.name = ""+weight;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }             
        
        public String toString() {
            return name;
        }
    }
    
    // Single factory for creating Vertices...
    public static class MyVertexFactory implements Factory<MyVertex> {
        private static MyVertexFactory instance = new MyVertexFactory();
        private static DirectedSparseMultigraph<GraphElements.MyVertex, GraphElements.MyEdge>g;
        private MyVertexFactory() {            
        }
        
        public static MyVertexFactory getInstance(DirectedSparseMultigraph<GraphElements.MyVertex, GraphElements.MyEdge> g2) {
           g = g2;
        	return instance;
            
        }
        
        public GraphElements.MyVertex create() {

            String name = ""+(g.getVertexCount());
           
            MyVertex v = new MyVertex(name);
            return v;
        }        


    }
    
    // Singleton factory for creating Edges...
    public static class MyEdgeFactory implements Factory<MyEdge> {
        private static int linkCount = 0;
        private static double defaultWeight;

        private static MyEdgeFactory instance = new MyEdgeFactory();
        
        private MyEdgeFactory() {            
        }
        
        public static MyEdgeFactory getInstance() {
            return instance;
        }
        
        public GraphElements.MyEdge create() {
        	String name =""+ defaultWeight;
            MyEdge link = new MyEdge(name);
            link.setWeight(defaultWeight);
            
            return link;
        }    

        public static double getDefaultWeight() {
            return defaultWeight;
        }

        public static void setDefaultWeight(double aDefaultWeight) {
            defaultWeight = aDefaultWeight;
        }

    
    }

}
