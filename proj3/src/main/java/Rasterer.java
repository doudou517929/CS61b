import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {

    // MapServer's (lrlon - ullon) / 256
    public static final double TOP_DPP = 0.00034332275390625;

    private int depth;
    private double rasterUllon, rasterUllat, rasterLrlon, rasterLrlat;
    private int ulx, uly, lrx, lry;
    private String[][] filenames;

    private class QueryBox {
        private double ullon, ullat, lrlon, lrlat;
        private double w, h;

        public QueryBox(double ullat, double ullon, double lrlat, double lrlon) {
            this.ullon = ullon;
            this.ullat = ullat;
            this.lrlon = lrlon;
            this.lrlat = lrlat;
        }

        public void setW(double w) {
            this.w = w;
        }

        public void setH(double h) {
            this.h = h;
        }
    }

    private class Area {
        private double ullon, ullat, lrlon, lrlat;

        public Area(double ullon, double ullat, double lrlon, double lrlat) {
            this.ullon = ullon;
            this.ullat = ullat;
            this.lrlon = lrlon;
            this.lrlat = lrlat;
        }
    }

    private void solve(QueryBox queryBox) {
        setDepth(queryBox);
        boolean findul = false;
        boolean findlr = false;
        for (int i = 0; i < pow2(depth); i++) {
            for (int j = 0; j < pow2(depth); j++) {
                Area area = getFileArea(depth, i, j);
                // area's lr goes into queryBox's field
                if (area.lrlat < queryBox.ullat && area.lrlon > queryBox.ullon) {
                    rasterUllat = area.ullat;
                    rasterUllon = area.ullon;
                    ulx = i;
                    uly = j;
                    findul = true;
                    break;
                }
            }
            if (findul) {
                break;
            }
        }
        for (int i = pow2(depth) - 1; i >= 0; i--) {
            for (int j = pow2(depth) - 1; j >= 0; j--) {
                Area area = getFileArea(depth, i, j);
                // area's ul goes into queryBox's field
                if (area.ullat > queryBox.lrlat && area.ullon < queryBox.lrlon) {
                    rasterLrlat = area.lrlat;
                    rasterLrlon = area.lrlon;
                    lrx = i;
                    lry = j;
                    findlr = true;
                    break;
                }
            }
            if (findlr) {
                break;
            }
        }
        getFileNames();
    }

    private void setDepth(QueryBox queryBox) {
        double dpp = (queryBox.lrlon - queryBox.ullon) / queryBox.w;
        for (int i = 0; i <= 7; i++) {
            if (calDepthDPP(i) <= dpp) {
                depth = i;
                return;
            }
        }
        depth = 7;
    }

    private double calDepthDPP(int dep) {
        return TOP_DPP / pow2(dep);
    }

    private int pow2(int n) {
        int res = 1;
        while (n-- > 0) {
            res = res * 2;

        }
        return res;
    }

    private Area getFileArea(int dep, int x, int y) {
        double lonDelta = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / pow2(dep);
        double latDelta = (MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT) / pow2(dep);
        double ullon = MapServer.ROOT_ULLON + x * lonDelta;
        double ullat = MapServer.ROOT_ULLAT - y * latDelta;
        double lrlon = ullon + lonDelta;
        double lrlat = ullat - latDelta;
        return new Area(ullon, ullat, lrlon, lrlat);
    }

    private void getFileNames() {
        filenames = new String[lry - uly + 1][lrx - ulx + 1];
        for (int i = 0; i <= lry - uly; i++) {
            for (int j = 0; j <= lrx - ulx; j++) {
                filenames[i][j] = "d" + depth + "_x" + (ulx + j) + "_y" + (uly + i) + ".png";
            }
        }
    }

    public Rasterer() {
        // YOUR CODE HERE
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        System.out.println(params);
        Map<String, Object> results = new HashMap<>();
        double ullat = params.get("ullat");
        double ullon = params.get("ullon");
        double lrlat = params.get("lrlat");
        double lrlon = params.get("lrlon");
        QueryBox queryBox = new QueryBox(ullat, ullon, lrlat, lrlon);
        queryBox.setH(params.get("h"));
        queryBox.setW(params.get("w"));

        solve(queryBox);
        results.put("render_grid", filenames);
        results.put("raster_ul_lon", rasterUllon);
        results.put("raster_ul_lat", rasterUllat);
        results.put("raster_lr_lon", rasterLrlon);
        results.put("raster_lr_lat", rasterLrlat);
        results.put("depth", depth);
        results.put("query_success", true);
        return results;
    }

}
