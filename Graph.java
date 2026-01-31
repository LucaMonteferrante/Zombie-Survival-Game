package finalproject;

import java.util.ArrayList;
import java.util.HashSet;

import finalproject.system.Tile;
import finalproject.system.TileType;
import finalproject.tiles.*;

public class Graph {
	// TODO level 2: Add fields that can help you implement this data type
    private ArrayList<Edge> edges = new ArrayList<>(50);
    public ArrayList<Tile> all = new ArrayList<>(25);
    private ArrayList<Tile> v;
    public final double inf = 90000.92;

    // TODO level 2: initialize and assign all variables inside the constructor
	public Graph(ArrayList<Tile> vertices) {
		for (Tile t : vertices) {
            all.add(t);
        }
	}
	
    // TODO level 2: add an edge to the graph
    public void addEdge(Tile origin, Tile destination, double weight){
    	this.edges.add(new Edge(origin, destination, weight));
    }
    
    // TODO level 2: return a list of all edges in the graph
	public ArrayList<Edge> getAllEdges() {
		return edges;
	}
  
	// TODO level 2: return list of tiles adjacent to t
	public ArrayList<Tile> getNeighbors(Tile t) {
    	ArrayList<Tile> neigh = new ArrayList<>(4);
       for (Edge e: edges){
           if (e.origin == t && e.destination.isWalkable()){
               neigh.add(e.destination);
           }
       }
       return neigh;
    }
	
	// TODO level 2: return total cost for the input path
	public double computePathCost(ArrayList<Tile> path) {
		double x = 0.0;
        for (int i = 0; i <path.size()-1; i++){
            for (Edge e : edges){
                if (e.origin == path.get(i) && e.destination == path.get(i+1)){
                    x = x + e.weight;
                }
            }
        }
		return x;
	}
	
   
    public static class Edge{
    	Tile origin;
    	Tile destination;
    	double weight;

        // TODO level 2: initialize appropriate fields
        public Edge(Tile s, Tile d, double cost){
        	origin = s;
            destination = d;
            weight = cost;
        }
        
        // TODO level 2: getter function 1
        public Tile getStart(){
            return origin;
        }

        
        // TODO level 2: getter function 2
        public Tile getEnd() {
            return destination;
        }
        
    }
    
}
