package finalproject;


import finalproject.system.Tile;
import finalproject.system.TileType;
import finalproject.tiles.MetroTile;

import java.util.ArrayList;

public class ShortestPath extends PathFindingService {
    //TODO level 4: find distance prioritized path
    public ShortestPath(Tile start) {
        super(start);
        generateGraph();
    }

	@Override
	public void generateGraph() {
		// TODO Auto-generated method stub
        ArrayList<Tile> everything = GraphTraversal.BFS(source);
        ArrayList<MetroTile> metros = new ArrayList<>(2);
        g = new Graph(everything);
        for (Tile t : everything) {
            for (Tile tile : t.neighbors) {
                if (t.type == TileType.Metro && tile.type == TileType.Metro){
                    g.addEdge(t, tile, ((MetroTile)tile).metroDistanceCost);
                }

                else if (tile.isWalkable()) {
                    g.addEdge(t, tile, tile.distanceCost);
                }
            }
        }
	}
    
}
