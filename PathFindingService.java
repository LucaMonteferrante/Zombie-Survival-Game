package finalproject;

import finalproject.system.Tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public abstract class PathFindingService {
	Tile source;
	Graph g;
	
	public PathFindingService(Tile start) {
    	this.source = start;
    }

	public abstract void generateGraph();
    
    //TODO level 4: Implement basic dijkstra's algorithm to find a path to the final unknown destination
    public ArrayList<Tile> findPath(Tile startNode) {
    	for (Tile t: g.all){
            if (t == startNode){
                t.costEstimate = 0.0;
                t.predecessor = null;
            }
            else{
                t.costEstimate = g.inf;
                t.predecessor = null;
            }
        }
        ArrayList<Tile> S = new ArrayList<>(g.all.size());
        ArrayList<Tile> end = new ArrayList<>(g.all.size()/2);
        TilePriorityQ pQ = new TilePriorityQ(g.all);
        ArrayList<Graph.Edge> edgesUsed = new ArrayList<>(g.getAllEdges().size());
        Tile temp = null;
        while(pQ.getQ().size() != 1){
            Tile present = pQ.removeMin();
            S.add(present);
            for (Graph.Edge e: g.getAllEdges()){
                if (edgesUsed.contains(e)){
                    continue;
                }
                if (present == e.origin){
                    if (e.destination.costEstimate > e.origin.costEstimate+ e.weight){
                        pQ.updateKeys(e.destination, e.origin, e.origin.costEstimate+ e.weight);
                        relax(e.origin, e.destination, e.weight, edgesUsed);
                    }
                    edgesUsed.add(e);
                }
            }
        }
        for (Tile t: S){
            if (t.isDestination){
                end.add(t);
                break;
            }
        }
        Tile cur = end.get(0);
        while(cur.predecessor != null){
            end.add(0, cur.predecessor);
            cur = cur.predecessor;
        }
    	return end;
    }

    public Tile relax(Tile first, Tile second, double weight, ArrayList<Graph.Edge> edgesUsed){
        boolean changed = false;
        for (Graph.Edge e : edgesUsed){
            if (e.origin == second && e.destination == first){
                return null;
            }
        }
        if (second.costEstimate> first.costEstimate+ weight){
            second.costEstimate = first.costEstimate + weight;
            changed = true;
        }
        second.predecessor = first;
        return second;
    }
    
    //TODO level 5: Implement basic dijkstra's algorithm to path find to a known destination
    public ArrayList<Tile> findPath(Tile start, Tile end) {
    	for (Tile t: g.all){
            if (t == start){
                t.costEstimate = 0.0;
                t.predecessor = null;
            }
            else{
                t.costEstimate = g.inf;
                t.predecessor = null;
            }
        }
        ArrayList<Tile> S = new ArrayList<>(g.all.size());
        ArrayList<Tile> finish = new ArrayList<>(g.all.size()/2);
        TilePriorityQ pQ = new TilePriorityQ(g.all);
        ArrayList<Graph.Edge> edgesUsed = new ArrayList<>(g.getAllEdges().size());
        Tile temp = null;
        while(pQ.getQ().size() != 1){
            Tile present = pQ.removeMin();
            S.add(present);
            for (Graph.Edge e: g.getAllEdges()){
                if (edgesUsed.contains(e)){
                    continue;
                }
                if (present == e.origin){
                    if (e.destination.costEstimate > e.origin.costEstimate+ e.weight){
                        pQ.updateKeys(e.destination, e.origin, e.origin.costEstimate+ e.weight);
                        relax(e.origin, e.destination, e.weight, edgesUsed);
                    }
                    edgesUsed.add(e);
                }
            }
        }
        for (Tile t: S){
            if (t == end){
                finish.add(t);
                break;
            }
        }
        Tile cur = finish.get(0);
        while(cur.predecessor != null){
            finish.add(0, cur.predecessor);
            cur = cur.predecessor;
        }
        return finish;
    }

    //TODO level 5: Implement basic dijkstra's algorithm to path find to the final destination passing through given waypoints
    public ArrayList<Tile> findPath(Tile start, LinkedList<Tile> waypoints){
    	ArrayList<ArrayList<Tile>> listOfLists = new ArrayList<ArrayList<Tile>>(50);
        ArrayList<Tile> result = new ArrayList<Tile>(50);
        Tile temporaryEnd = null;
        for (Tile tile: g.all){
            if (tile.isDestination){
                waypoints.addLast(tile);
                temporaryEnd = tile;
            }
        }
        listOfLists.add(0, findPath(start, waypoints.get(0)));
        if (waypoints.size() > 1) {
            for (int i = 1; i < waypoints.size(); i++) {
                ArrayList<Tile> temp = listOfLists.get(i - 1);
                int tempSize = temp.size() - 1; //listOfLists.get(i-1).size()-1
                listOfLists.add(i, findPath(temp.get(tempSize), waypoints.get(i)));
            }
            for (Tile t : listOfLists.get(0)) {
                result.add(t);
            }
            for (int i = 1; i < listOfLists.size(); i++) {
                for (int j = 1; j < listOfLists.get(i).size(); j++) {
                    result.add(listOfLists.get(i).get(j));
                }
            }
        }
        else {
            for (int j = 0; j < listOfLists.get(0).size(); j++) {
                result.add(listOfLists.get(0).get(j));
            }
        }
        for (int i = 0;i< result.size()-1;i++){
            result.get(i+1).predecessor = result.get(i);
        }
        waypoints.remove(temporaryEnd);
    	return result;
    }
        
}

