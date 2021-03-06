package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Solver {

    // estimated distance
    private Map<WorldState, Integer> edCaches = new HashMap<>();
    // number of SearchNode's are enqueued
    private int searchedCnt = 0;
    private Stack<WorldState> path = new Stack<>();

    private class SearchNode {
        private WorldState state;
        private int moves;
        private Integer priority;
        private SearchNode prev;

        private SearchNode(WorldState state, SearchNode prev) {
            this.state = state;
            this.moves = prev == null ? 0 : prev.moves + 1;
            if (edCaches.containsKey(this.state)) {
                int ed = edCaches.get(this.state);
                this.priority = this.moves + ed;
            } else {
                int ed = this.state.estimatedDistanceToGoal();
                edCaches.put(this.state, ed);
                this.priority = this.moves + ed;
            }
            this.prev = prev;
        }
    }

    private class SearchNodeComparator implements Comparator<SearchNode> {
        @Override
        public int compare(SearchNode left, SearchNode right) {
            return left.priority.compareTo(right.priority);
        }
    }

    /**
     *  Constructor which solves the puzzle, computing everything necessary for moves()
     *  and solution() to not have to solve the problem again. Solves the
     *  puzzle using the A* algorithm. Assumes a solution exists.
     */
    public Solver(WorldState initial) {
        MinPQ<SearchNode> pq = new MinPQ<>(new SearchNodeComparator());
        SearchNode currentNode = new SearchNode(initial, null);
        while (!currentNode.state.isGoal()) {
            for (WorldState nextState : currentNode.state.neighbors()) {
                if (currentNode.prev == null || !nextState.equals(currentNode.prev.state)) {
                    SearchNode nextNode = new SearchNode(nextState, currentNode);
                    pq.insert(nextNode);
                    searchedCnt++;
                }
            }
            currentNode = pq.delMin();
        }
        for (SearchNode node = currentNode; node != null; node = node.prev) {
            path.push(node.state);
        }
    }

    /**
     *  Returns the minimum number of moves to solve the puzzle starting at the initial WorldState.
     */
    public int moves() {
        return path.size();
    }

    /**
     * Returns a sequence of WorldStates from the initial WorldState to the solution.
     * @return
     */
    public Iterable<WorldState> solution() {
        return path;
    }

    int searchedCount() {
        return searchedCnt;
    }
}
