import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.lang.*;

public class GameState {
    private int size;            // The number of stones
    private boolean[] stones;    // Game state: true for available stones, false for taken ones
    private int lastMove;        // The last move
    private Helper helper;

    /**
     * Class constructor specifying the number of stones.
     */
    public GameState(int size) {

        this.helper = new Helper();

        this.size = size;

        //  For convenience, we use 1-based index, and set 0 to be unavailable
        this.stones = new boolean[this.size + 1];
        this.stones[0] = false;

        // Set default state of stones to available
        for (int i = 1; i <= this.size; ++i) {
            this.stones[i] = true;
        }

        // Set the last move be -1
        this.lastMove = -1;
    }

    /**
     * Copy constructor
     */
    public GameState(GameState other) {
        this.size = other.size;
        this.stones = Arrays.copyOf(other.stones, other.stones.length);
        this.lastMove = other.lastMove;
    }


    /**
     * This method is used to compute a list of legal moves
     *
     * @return This is the list of state's moves
     */
    public List<Integer> getMoves() {
        // TODO Add your code here
        List<Integer> moves = new ArrayList<>();

        // judge whether the first move
        int takens = removeStones();
        int maxEdge = this.size / 2;
        if (this.size % 2 == 1) maxEdge += 1;

        if (takens == 0) {
            for (int i = 1; i < maxEdge; i+=2) {
                if (this.stones[i])
                    moves.add(i);
            }
        } else {
            // get factor
            if (this.stones[1]) moves.add(1);
            int n = this.lastMove;
            for(int i = 2; i <= Math.sqrt(n); i++) {
                if (n % i == 0) {
                    if (this.stones[i]){
                        moves.add(i);
                    }
                    if (this.stones[n/i]){
                        moves.add(n/i);
                    }
                }
            }

            // get multiples
            for (int i = 2; i <= this.size / this.lastMove; i++) {
                if (this.stones[i*this.lastMove]) {
                    moves.add(i*this.lastMove);
                }
            }
        }

        return moves;
    }


    /**
     * This method is used to generate a list of successors
     * using the getMoves() method
     *
     * @return This is the list of state's successors
     */
    public List<GameState> getSuccessors() {
        return this.getMoves().stream().map(move -> {
            var state = new GameState(this);
            state.removeStone(move);
            return state;
        }).collect(Collectors.toList());
    }


    /**
     * This method is used to evaluate a game state based on
     * the given heuristic function
     *
     * @return int This is the static score of given state
     */
    public double evaluate() {
        // TODO Add your code here
        int num = removeStones();
        List<GameState> successors = getSuccessors();
        if (successors.size() == 0) {
            if (num % 2 == 0)
                return -1.0;
            else
                return 1.0;
        } else {
            double score = 0.0;
            if (this.stones[1])
                score = 0;
            else if (this.lastMove == 1) {
                if (successors.size() % 2 == 1) {
                    score = 0.5;
                } else {
                    score = -0.5;
                }
            } else if (this.helper.isPrime(this.lastMove)) {
                int multiples = 0;
                for (int i = 2; i <= this.size / this.lastMove; i++) {
                    if (this.stones[i*this.lastMove])
                        multiples += 1;
                }
                if (multiples % 2 == 1) {
                    score = 0.7;
                } else {
                    score = -0.7;
                }
            } else {
                int largestPriem = this.helper.getLargestPrimeFactor(this.lastMove);
                int multiples = 0;
                for (int i = 1; i <= this.size / largestPriem; i++) {
                    if (this.stones[i*largestPriem])
                        multiples += 1;
                }
                if (multiples % 2 == 1) {
                    score = 0.6;
                } else {
                    score = -0.6;
                }
            }
            if (num % 2 == 0) {
                return score;
            } else {
                return (0 - score);
            }
        }
    }

    public int removeStones() {
        int num = 0;
        for (int i = 1; i <= this.size; ++i) {
            if (!this.stones[i])
                num += 1;
        }
        return num;
    }


    /**
     * This method is used to take a stone out
     *
     * @param idx Index of the taken stone
     */
    public void removeStone(int idx) {
        this.stones[idx] = false;
        this.lastMove = idx;
    }

    /**
     * These are get/set methods for a stone
     *
     * @param idx Index of the taken stone
     */
    public void setStone(int idx) {
        this.stones[idx] = true;
    }

    public boolean getStone(int idx) {
        return this.stones[idx];
    }

    /**
     * These are get/set methods for lastMove variable
     *
     * @param move Index of the taken stone
     */
    public void setLastMove(int move) {
        this.lastMove = move;
    }

    public int getLastMove() {
        return this.lastMove;
    }

    /**
     * This is get method for game size
     *
     * @return int the number of stones
     */
    public int getSize() {
        return this.size;
    }
}	
