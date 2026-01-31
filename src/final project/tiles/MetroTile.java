package finalproject.tiles;

import finalproject.system.Tile;
import finalproject.system.TileType;

public class MetroTile extends Tile {
	public double metroTimeCost = 100;
	public double metroDistanceCost = 100;
	public double metroCommuteFactor = 0.2;
	
    //TODO level 0: finish constructor
    public MetroTile() {
        super(1,1,2);
        this.type = TileType.Metro;
    }
    
    // TODO level 7: updates the distance and time cost differently between metro tiles
    public void fixMetro(Tile node) {
        double M1 = this.xCoord - node.xCoord;
        double M2 = this.yCoord - node.yCoord;
        if (M1 < 0){
            M1 = -1 * M1;
        }
        if (M2 < 0){
            M2 = -1 * M2;
        }
        double M = M1 + M2;

        metroTimeCost = M * metroCommuteFactor;
        metroDistanceCost = M / metroCommuteFactor;
    }
}
