package lab11.graphs;

import java.util.Random;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int[] cameFrom;
    private boolean foundCircle = false;

    public MazeCycles(Maze m) {
        super(m);
    }

    @Override
    public void solve() {
        cameFrom = new int[maze.V()];
        Random rand = new Random();
        int startX = rand.nextInt(maze.N());
        int startY = rand.nextInt(maze.N());
        int s = maze.xyTo1D(startX, startY);
        marked[s] = true;
        cameFrom[s] = s;

        dfs(s);
        announce();
    }

    private void dfs(int v) {
        for (int w : maze.adj(v)) {
            if (foundCircle) {
                return;
            }
            if (!marked[w]) {
                marked[w] = true;
                cameFrom[w] = v;
                dfs(w);
            } else if (w != cameFrom[v]){
                cameFrom[w] = v;
                int cur = v;  // reconstruct circle
                edgeTo[cur] = cameFrom[cur];
                while (cur != w) {
                    cur = cameFrom[cur];
                    edgeTo[cur] = cameFrom[cur];
                }
                foundCircle = true;
                return;
            }
        }
    }
}

