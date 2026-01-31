package finalproject;

import finalproject.system.Tile;
import finalproject.system.TileType;
import finalproject.tiles.MetroTile;

import java.util.ArrayList;

public class FastestPath extends PathFindingService {
    //TODO level 6: find time prioritized path
    public FastestPath(Tile start) {
        super(start);
        generateGraph();
    }

	@Override
	public void generateGraph() {
		// TODO Auto-generated method stub
		ArrayList<Tile> everything = GraphTraversal.BFS(source);
        g = new Graph(everything);
        for (Tile t : everything) {
            for (Tile tile : t.neighbors) {
                if (t.type == TileType.Metro && tile.type == TileType.Metro){
                    g.addEdge(t, tile, ((MetroTile)tile).metroTimeCost);
                }

                else if (tile.isWalkable()) {
                    g.addEdge(t, tile, tile.timeCost);
                }
            }
        }
	}

}
