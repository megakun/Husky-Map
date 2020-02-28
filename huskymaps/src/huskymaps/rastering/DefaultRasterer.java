package huskymaps.rastering;

import huskymaps.graph.Coordinate;
import huskymaps.utils.Constants;
/**
 * @see Rasterer
 */
public class DefaultRasterer implements Rasterer {
    public TileGrid rasterizeMap(Coordinate ul, Coordinate lr, int depth) {
        Tile[][] grid;

        if (ul.lat() < Constants.ROOT_LRLAT || ul.lon() > Constants.ROOT_LRLON ||
            lr.lat() > Constants.ROOT_ULLAT || lr.lon() < Constants.ROOT_ULLON) {
            grid = new Tile[1][1];
            grid[1][1] = new Tile(depth,
                0, 0);
        }

        int ulK = (int) ((ul.lon() - Constants.ROOT_ULLON) / Constants.LON_PER_TILE[depth]);
        int lrK = (int) ((lr.lon() - Constants.ROOT_ULLON) / Constants.LON_PER_TILE[depth]);
        int ulJ = (int) ((Constants.ROOT_ULLAT - ul.lat()) / Constants.LAT_PER_TILE[depth]);
        int lrJ = (int) ((Constants.ROOT_ULLAT - lr.lat()) / Constants.LAT_PER_TILE[depth]);

        if (ul.lon() < Constants.ROOT_ULLON) {
            ulK = 0;
        }

        if (ul.lat() > Constants.ROOT_ULLAT) {
            ulJ = 0;
        }

        if (lr.lon() > Constants.ROOT_LRLON) {
            lrK = Constants.NUM_X_TILES_AT_DEPTH[depth] - 1;
        }

        if (lr.lat() < Constants.ROOT_LRLAT) {
            lrJ = Constants.NUM_Y_TILES_AT_DEPTH[depth] - 1;
        }


        grid = new Tile[lrJ - ulJ + 1][lrK - ulK + 1];

        for (int k = ulK; k <= lrK; k++) {
            for (int j = ulJ; j <= lrJ; j++) {
                grid[j - ulJ][k - ulK] = new Tile(depth, k, j);
            }
        }
        return new TileGrid(grid);
    }
}
