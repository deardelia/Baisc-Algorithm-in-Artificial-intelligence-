import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * A* algorithm search
 * 
 * You should fill the search() method of this class.
 */
public class AStarSearcher extends Searcher {

	/**
	 * Calls the parent class constructor.
	 * 
	 * @see Searcher
	 * @param maze initial maze.
	 */
	public AStarSearcher(Maze maze) {
		super(maze);
	}

	/**
	 * Main a-star search algorithm.
	 * 
	 * @return true if the search finds a solution, false otherwise.
	 */
	public boolean search() {

		// FILL THIS METHOD

		// explored list is a Boolean array that indicates if a state associated with a given position in the maze has already been explored. 
		boolean[][] explored = new boolean[maze.getNoOfRows()][maze.getNoOfCols()];
		// ...

		PriorityQueue<StateFValuePair> frontier = new PriorityQueue<StateFValuePair>();

		// TODO initialize the root state and add
		// to frontier list
		double hValue = (Math.sqrt(Math.pow((maze.getPlayerSquare().X-maze.getGoalSquare().X), 2) +
				Math.pow((maze.getPlayerSquare().Y-maze.getGoalSquare().Y), 2)));
		State initialState = new State(maze.getPlayerSquare(), null, 0, 0);
		StateFValuePair initialStatef = new StateFValuePair(initialState, hValue);
		frontier.add(initialStatef);

		while (!frontier.isEmpty()) {
			// TODO return true if a solution has been found
			// TODO maintain the cost, noOfNodesExpanded (a.k.a. noOfNodesExplored),
			// maxDepthSearched, maxSizeOfFrontier during
			// the search
			// TODO update the maze if a solution found
			maxSizeOfFrontier = frontier.size() > maxSizeOfFrontier ? frontier.size() : maxSizeOfFrontier;
			StateFValuePair currentStatef = frontier.poll();
			explored[currentStatef.getState().getX()][currentStatef.getState().getY()] = true;
			noOfNodesExpanded += 1;
			maxDepthSearched = currentStatef.getState().getDepth() > maxDepthSearched ?
					currentStatef.getState().getDepth() : maxDepthSearched;
			if (currentStatef.getState().isGoal(maze)) {
				State currentState = currentStatef.getState();
				while (maze.getSquareValue(currentState.getX(), currentState.getY()) != 'S') {
					cost += 1;
					if (maze.getSquareValue(currentState.getX(), currentState.getY()) != 'G') {
						maze.setOneSquare(currentState.getSquare(), '.');
					}
					currentState = currentState.getParent();
				}
				return true;
			}

			ArrayList<State> currentSuccessors = currentStatef.getState().getSuccessors(explored, maze);
			for (State successor : currentSuccessors) {
				boolean addFlag = true;
				StateFValuePair removeObj = null;
				for (StateFValuePair frontierf : frontier) {
					if (frontierf.getState().getX() == successor.getX() && frontierf.getState().getY() == successor.getY()) {
						if (frontierf.getState().getGValue() > successor.getGValue()) {
							removeObj = frontierf;
							break;
						} else {
							addFlag = false;
							break;
						}
					}
				}
				if (removeObj != null) {
					hValue = removeObj.getFValue() - removeObj.getState().getGValue();
					frontier.remove(removeObj);
					StateFValuePair successorf = new StateFValuePair(successor, hValue + successor.getGValue());
					frontier.add(successorf);
				} else if (addFlag) {
					hValue = (Math.sqrt(Math.pow((successor.getX()-maze.getGoalSquare().X), 2) +
							Math.pow((successor.getY()-maze.getGoalSquare().Y), 2)));
					StateFValuePair successorf = new StateFValuePair(successor, hValue + successor.getGValue());
					frontier.add(successorf);
				}

			}

			// use frontier.poll() to extract the minimum stateFValuePair.
			// use frontier.add(...) to add stateFValue pairs
		}

		// TODO return false if no solution
		return false;
	}

}
