package finalproject;

import finalproject.system.Tile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

public class GraphTraversal
{


	//TODO level 1: implement BFS traversal starting from s
	public static ArrayList<Tile> BFS(Tile s)
	{
		ArrayList<Tile> path = new ArrayList<Tile>(9);
		LinkedList<Tile> list = new LinkedList<Tile>();
		if (s.neighbors.size() == 0){
			return path;
		}
		
		path.add(s);
		list.add(s);

		while(!list.isEmpty()){
			for (Tile t: list.element().neighbors){
				if (!path.contains(t) && t.isWalkable()){ // could use a boolean called isVisited in the tile class instead of checking throught the entire thing
					list.add(t);
					path.add(t);
				}
			}
			list.removeFirst();
		}

		return path;
	}


	//TODO level 1: implement DFS traversal starting from s
	public static ArrayList<Tile> DFS(Tile s) {
		ArrayList<Tile> path = new ArrayList<Tile>(25);
		ArrayList<Tile> ar = new ArrayList<Tile>(25);
		ar.add(s);
		Tile cur;

		while(!ar.isEmpty()) {
			cur = ar.remove(ar.size() - 1);

			if (!path.contains(cur)) {
				path.add(cur);
				for (Tile t : cur.neighbors) {
					if (t.isWalkable() && !path.contains(t)) {
						ar.add(t);
					}
				}
			}
		}

		return path;
	}

}  
