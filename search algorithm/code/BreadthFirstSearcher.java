import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Breadth-First Search (BFS)
 * 
 * You should fill the search() method of this class.
 */
public class BreadthFirstSearcher extends Searcher {

	/**
	 * Calls the parent class constructor.
	 * 
	 * @see Searcher
	 * @param maze initial maze.
	 */
	public BreadthFirstSearcher(Maze maze) {
		super(maze);
	}

	/**
	 * Main breadth first search algorithm.
	 * 
	 * @return true if the search finds a solution, false otherwise.
	 */
	public boolean search() {
		// FILL THIS METHOD

		// explored list is a 2D Boolean array that indicates if a state associated with a given position in the maze has already been explored.
		boolean[][] explored = new boolean[maze.getNoOfRows()][maze.getNoOfCols()];

		// ...

		// Queue implementing the Frontier list
		LinkedList<State> queue = new LinkedList<State>();

		// add initialState into queue
		State initialState = new State(maze.getPlayerSquare(), null, 0, 0);
		queue.add(initialState);

		while (!queue.isEmpty()) {
			// TODO return true if find a solution
			// TODO update the maze if a solution found
			State currentState = queue.poll();
			explored[currentState.getX()][currentState.getY()] = true;

			maxDepthSearched = currentState.getDepth() > maxDepthSearched ? currentState.getDepth() : maxDepthSearched;
			noOfNodesExpanded += 1;

			//System.out.printf("current position: %d %d %c\n", currentState.getX(), currentState.getY(), maze.getSquareValue(currentState.getX(), currentState.getY()));
			if (currentState.isGoal(maze)) {
				State recursiveState = currentState.getParent();
				cost = 1;
				while (recursiveState != null && maze.getSquareValue(recursiveState.getX(), recursiveState.getY()) != 'S') {
					maze.setOneSquare(recursiveState.getSquare(), '.');
					recursiveState = recursiveState.getParent();
					cost += 1;
				}
				return true;
			}
			// TODO maintain the cost, noOfNodesExpanded (a.k.a. noOfNodesExplored),
			// maxDepthSearched, maxSizeOfFrontier during
			// the search
			ArrayList<State> currentSuccessors = currentState.getSuccessors(explored, maze);
			for (State successor : currentSuccessors) {
				boolean addFlag = true;
				for (State frontiers : queue) {
					if (frontiers.getX() == successor.getX() && frontiers.getY() == successor.getY()) {
						addFlag = false;
						break;
					}
				}
				if (addFlag)
					queue.add(successor);
			}
			maxSizeOfFrontier = queue.size() > maxSizeOfFrontier ? queue.size() : maxSizeOfFrontier;

			// use queue.pop() to pop the queue.
			// use queue.add(...) to add elements to queue
		}

		// TODO return false if no solution
		return false;
	}
}
