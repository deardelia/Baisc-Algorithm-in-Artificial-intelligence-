import java.util.List;


public class TakeStones {

	/**
	* This is the main method.
	* @param args A sequence of integer numbers, including the number of stones,
	* the number of taken stones, a list of taken stone and search depth
	* @exception IndexOutOfBoundsException On input error.
	*/
	public static void main (String[] args) {
		try {
			// Read input from command line
			int n = Integer.parseInt(args[0]);		// the number of stones
			int nTaken = Integer.parseInt(args[1]);	// the number of taken stones
			
			// Initialize the game state
			GameState state = new GameState(n);		// game state
			int stone;
			for (int i = 0; i < nTaken; i++) {
				stone = Integer.parseInt(args[i + 2]);
				state.removeStone(stone);
			}

			int depth = Integer.parseInt(args[nTaken + 2]);	// search depth
			// Process for depth being -1
			if (0 == depth)
				depth = n + 1;

			// Get next move
			var searcher = new AlphaBetaPruning();
			searcher.run(state, depth);

			// Print search stats
			searcher.printStats();

		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}
}