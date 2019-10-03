import java.util.List;
import java.util.ArrayList;
import java.lang.*;

public class AlphaBetaPruning {
    public int specifiedDepth = 0;
    public int currentDepth = 0;
    public int visitedNodes = 0;
    public int evaluateNodes = 0;
    public double finalValue = 0.0;
    public int notpronedTree = 0;
    public int leafNodes = 0;
    public int firstMove = 0;
    public boolean firstPlayer = true;

    public AlphaBetaPruning() {

    }

    /**
     * This function will print out the information to the terminal,
     * as specified in the homework description.
     */
    public void printStats() {
        // TODO Add your code here
        System.out.printf("Move: %d\n, " +
                "Value: %f\n" +
                "Number of Nodes Visited: %d\n" +
                "Number of Nodes Evaluated: %d\n" +
                "Max Depth Reached: %d\n" +
                "Avg Effective Branching Factor: %f"
                , firstMove, finalValue, visitedNodes, evaluateNodes, currentDepth, notpronedTree / (visitedNodes - leafNodes));
    }

    /**
     * This function will start the alpha-beta search
     * @param state This is the current game state
     * @param depth This is the specified search depth
     */
    public void run(GameState state, int depth) {
        // TODO Add your code here
        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;
        int nTaken = 0;
        double value = 0.0;

        specifiedDepth = depth;
        nTaken = state.removeStones();

        if (nTaken % 2 == 1) firstPlayer = false;
        finalValue = alphabeta(state, depth, alpha, beta, firstPlayer);
    }

    /**
     * This method is used to implement alpha-beta pruning for both 2 players
     * @param state This is the current game state
     * @param depth Current depth of search
     * @param alpha Current Alpha value
     * @param beta Current Beta value
     * @param maxPlayer True if player is Max Player; Otherwise, false
     * @return int This is the number indicating score of the best next move
     */
    private double alphabeta(GameState state, int depth, double alpha, double beta, boolean maxPlayer) {
        // TODO Add your code here
        double value = 0.0;
        if (maxPlayer)
            value = maxvalue(state, alpha, beta);
        else
            value = minvalue(state, alpha, beta);
        return value;
    }

    private double maxvalue(GameState state, double alpha, double beta){
        List<GameState> successors = state.getSuccessors();
        int nTakens = state.removeStones();

        visitedNodes += 1;

        if (successors.size() == 0 || terminalState(state)) {
            /*if (nTakens % 2 == 0) return -1;
            else return 1;*/
            currentDepth += 1;
            evaluateNodes += 1;
            if (successors.size() == 0) leafNodes += 1;
            return state.evaluate();
        }

        double value = Double.NEGATIVE_INFINITY;
        for (GameState currentState : successors) {
            notpronedTree += 1;
            value = Math.max(value, minvalue(currentState, alpha, beta));
            if (value >= beta) {
                return value;
            }
            if (firstPlayer && alpha < value) {
                firstMove = currentState.getLastMove();
            }
            alpha = Math.max(alpha, value);
        }
        currentDepth += 1;

        return value;
    }

    private double minvalue(GameState state, double alpha, double beta) {
        List<GameState> successors = state.getSuccessors();
        int nTakens = state.removeStones();

        visitedNodes += 1;

        if (successors.size() == 0 || terminalState(state)) {
            /*if (nTakens % 2 == 0) return -1;
            else return 1;*/
            currentDepth += 1;
            evaluateNodes += 1;
            if (successors.size() == 0) leafNodes += 1;
            return state.evaluate();
        }

        double value = Double.POSITIVE_INFINITY;
        for (GameState currentState : successors) {
            notpronedTree += 1;
            value = Math.min(value, maxvalue(currentState, alpha, beta));
            if (value <= alpha) {
                return value;
            }
            if (firstPlayer && beta > value) {
                firstMove = currentState.getLastMove();
            }
            beta = Math.min(beta, value);
        }
        currentDepth += 1;

        return value;
    }

    private boolean terminalState(GameState state) {
        if (specifiedDepth != 0 && specifiedDepth == currentDepth) {
            return true;
        }
        return false;
    }
}
