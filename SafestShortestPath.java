package finalproject;


import java.util.ArrayList;
import java.util.LinkedList;

import finalproject.system.Tile;
import finalproject.system.TileType;
import finalproject.tiles.MetroTile;

public class SafestShortestPath extends ShortestPath {
	public int health;
	public Graph costGraph;
	public Graph damageGraph;
	public Graph aggregatedGraph;

	//TODO level 8: finish class for finding the safest shortest path with given health constraint
	public SafestShortestPath(Tile start, int health) {
		super(start);
		this.health = health;
	}

	
	public void generateGraph() {
		// TODO Auto-generated method stub
		ArrayList<Tile> everything = GraphTraversal.BFS(source);
		ArrayList<MetroTile> metros = new ArrayList<>(2);
		costGraph = new Graph(everything);
		for (Tile t : everything) {
			for (Tile tile : t.neighbors) {
				if (t.type == TileType.Metro && tile.type == TileType.Metro){
					costGraph.addEdge(t, tile, ((MetroTile)tile).metroDistanceCost);
				}

				else if (tile.isWalkable()) {
					costGraph.addEdge(t, tile, tile.distanceCost);
				}
			}
		}


		// DAMAGECOST GRAPH
		ArrayList<Tile> everything1 = GraphTraversal.BFS(source);
		damageGraph = new Graph(everything1);
		for (Tile t : everything1) {
			for (Tile tile : t.neighbors) {
				if (tile.isWalkable()) {
					damageGraph.addEdge(t, tile, tile.damageCost);
				}
			}
		}

		//AGGREGATED COST GRAPH
		ArrayList<Tile> everything2 = GraphTraversal.BFS(source);
		aggregatedGraph = new Graph(everything2);
		for (Tile t : everything2) {
			for (Tile tile : t.neighbors) {
				if (tile.isWalkable()) {
					aggregatedGraph.addEdge(t, tile, tile.damageCost);
				}
			}
		}
	}
public ArrayList<Tile> findPath(Tile start, LinkedList<Tile> waypoints) {
		//Step #1
		g = costGraph;
		Tile temporaryEnd = null;
		for (Tile tile: g.all){
			if (tile.isDestination){
				waypoints.addLast(tile);
				temporaryEnd = tile;
			}
		}
		ArrayList<ArrayList<Tile>> dist = new ArrayList<ArrayList<Tile>>(50);
		ArrayList<Tile> distpath = new ArrayList<Tile>(50);
		dist.add(0, findPath(start, waypoints.get(0)));
		if (waypoints.size() > 1) {
			for (int i = 1; i < waypoints.size(); i++) {
				ArrayList<Tile> temp = dist.get(i - 1);
				int tempSize = temp.size() - 1; //listOfLists.get(i-1).size()-1
				dist.add(i, findPath(temp.get(tempSize), waypoints.get(i)));
			}
			for (Tile t : dist.get(0)) {
				distpath.add(t);
			}
			for (int i = 1; i < dist.size(); i++) {
				for (int j = 1; j < dist.get(i).size(); j++) {
					distpath.add(dist.get(i).get(j));
				}
			}
		}
		else {
			for (int j = 0; j < dist.get(0).size(); j++) {
				distpath.add(dist.get(0).get(j));
			}
		}
		for (int i = 0;i< dist.size()-1;i++){
			distpath.get(i+1).predecessor = distpath.get(i);
		}
		double distHP = 0.0;
		for (int i=1;i<distpath.size();i++){
			distHP += distpath.get(i).damageCost;
		}
		if (distHP < health) {
			waypoints.remove(temporaryEnd);
			return distpath;
		}

		//Step 2
		g = damageGraph;
		ArrayList<ArrayList<Tile>> dmg = new ArrayList<ArrayList<Tile>>(50);
		ArrayList<Tile> dmgpath = new ArrayList<Tile>(50);
		dmg.add(0, findPath(start, waypoints.get(0)));
		if (waypoints.size() > 1) {
			for (int i = 1; i < waypoints.size(); i++) {
				ArrayList<Tile> temp = dmg.get(i - 1);
				int tempSize = temp.size() - 1; //listOfLists.get(i-1).size()-1
				dmg.add(i, findPath(temp.get(tempSize), waypoints.get(i)));
			}
			for (Tile t : dmg.get(0)) {
				dmgpath.add(t);
			}
			for (int i = 1; i < dmg.size(); i++) {
				for (int j = 1; j < dmg.get(i).size(); j++) {
					dmgpath.add(dmg.get(i).get(j));
				}
			}
		}
		else {
			for (int j = 0; j < dmg.get(0).size(); j++) {
				dmgpath.add(dmg.get(0).get(j));
			}
		}
		for (int i = 0;i< dmgpath.size()-1;i++){
			dmgpath.get(i+1).predecessor = dmgpath.get(i);
		}
		double dmgHP = 0.0;
		for (int i = 1;i<dmgpath.size();i++){
			dmgHP += dmgpath.get(i).damageCost;
		}
		if (dmgHP > health){
			waypoints.remove(temporaryEnd);
			return null;
		}
		while(true) {
			double distCost = 0.0;                // Step 3
			distHP = 0.0;
			double dmgCost = 0.0;
			dmgHP = 0.0;
			for (int i = 1; i < distpath.size(); i++) {
				distCost += distpath.get(i).distanceCost;
				distHP += distpath.get(i).damageCost;
			}
			for (int i = 1; i < dmgpath.size(); i++) {
				dmgCost += dmgpath.get(i).distanceCost;
				dmgHP += dmgpath.get(i).damageCost;
			}
			double lambda = (distCost - dmgCost) / (dmgHP - distHP);

			for (Graph.Edge e : aggregatedGraph.getAllEdges()) {
				if (e.origin.type == TileType.Metro && e.destination.type == TileType.Metro){
					e.weight = ((MetroTile)e.destination).metroDistanceCost + (lambda * e.destination.damageCost);
				}
				else {
					e.weight = e.destination.distanceCost + (lambda * e.destination.damageCost);
				}
			}
			g = aggregatedGraph; //Step 4
			ArrayList<ArrayList<Tile>> agg = new ArrayList<ArrayList<Tile>>(50);
			ArrayList<Tile> aggrist = new ArrayList<Tile>(50);
			agg.add(0, findPath(start, waypoints.get(0)));
			if (waypoints.size() > 1) {
				for (int i = 1; i < waypoints.size(); i++) {
					ArrayList<Tile> temp = agg.get(i - 1);
					int tempSize = temp.size() - 1; //listOfLists.get(i-1).size()-1
					agg.add(i, findPath(temp.get(tempSize), waypoints.get(i)));
				}
				for (Tile t : agg.get(0)) {
					aggrist.add(t);
				}
				for (int i = 1; i < agg.size(); i++) {
					for (int j = 1; j < agg.get(i).size(); j++) {
						aggrist.add(agg.get(i).get(j));
					}
				}
			} else {
				for (int j = 0; j < agg.get(0).size(); j++) {
					aggrist.add(agg.get(0).get(j));
				}
			}
			for (int i = 0; i < aggrist.size() - 1; i++) {
				aggrist.get(i + 1).predecessor = aggrist.get(i);
			}
			//Step 5
			double aggrost = 0.0;
			double aggramage = 0.0;
			for (int i = 1; i < aggrist.size(); i++) {
				aggramage += aggrist.get(i).damageCost;
				for (Graph.Edge e : aggregatedGraph.getAllEdges()) {
					if (e.origin == aggrist.get(i - 1) && e.destination == aggrist.get(i)) {
						aggrost += e.weight;
						break;
					}
				}
			}

			ArrayList<Graph.Edge> updedges = new ArrayList<>();
			for (Graph.Edge e : costGraph.getAllEdges()) {
				if (e.origin.type == TileType.Metro && e.destination.type == TileType.Metro){
					updedges.add(new Graph.Edge(e.origin, e.destination, ((MetroTile)e.destination).metroDistanceCost + (lambda * e.destination.damageCost)));
				}
				else {
					updedges.add(new Graph.Edge(e.origin, e.destination, e.destination.distanceCost + (lambda * e.destination.damageCost)));
				}
			}
//			for (Graph.Edge e : costGraph.getAllEdges()) {
//				e.weight = e.destination.distanceCost + (lambda * e.destination.damageCost);
//			}
			double Pcaggrost = 0.0;
			for (int i = 1; i < distpath.size(); i++) {
				for (Graph.Edge e :updedges) {
					if (e.origin == distpath.get(i - 1) && e.destination == distpath.get(i)) {
						Pcaggrost += e.weight;
					}
				}
			}

			if (aggrost == Pcaggrost) {
				waypoints.remove(temporaryEnd);
				return dmgpath;
			} else if (aggramage <= health) {
				dmgpath = aggrist;
			} else {
				distpath = aggrist;
			}
		}
	}
}
