package ca.uleth.cpsc3770;

import java.util.ArrayList;
import java.util.List;

public class TicTacToeMiniMaxAi implements TicTacToeAi {
	
	String lastAction;
	
	private int alphaBeta(String node, int depth, int alpha, int beta, String player) throws Exception {
		if (depth == 0)
			return utility(node);
		
		List<String> children = generateActions(node, player);
		
		if (player == "Max") {
			for (String child : children) {
				alpha = max(alpha, alphaBeta(child, depth - 1, alpha, beta, "Min"));
				if (beta <= alpha) {
					return alpha;
				}
			}
		} else {
			for (String child : children) {
				beta = min(beta, alphaBeta(child, depth-1, alpha, beta, "Max"));
				if (beta <= alpha) {
					return beta;
				}
			}
		}
		throw new Exception("You hit the end of minimax, this should never happen.");
	}
	
	
	/* (non-Javadoc)
	 * @see ca.uleth.cpsc3770.TicTacToeAi#getMove(java.lang.String)
	 */
	public String getMove(String state) {
		return alphaBetaSearch(state);
	}
	
	private List<String> generateActions(String state, String player) {
		return new ArrayList<String>();
	}
	
	private int utility(String state) {
		return 1;
	}
	
	String alphaBetaSearch(String state) {
		int v = maxvalue(state, -1, 1);	
		return lastAction;
	}
	
	int min(int a, int b) {
		return (a > b) ? b : a;
	}
	
	int max(int a, int b) {
		return (a > b) ? a : b;
	}

	int maxvalue(String state, int alpha, int beta) {
		if (utility(state) != 0)
			return utility(state);

		int v = Integer.MIN_VALUE;
		String action = "";
		List<String> actions = generateActions(state, "X");

		if (actions.size() == 0)
			return 0;
		
		for (String a : actions) {
			v = max(v, minvalue(a, alpha, beta));
			if (v >= beta) {
				lastAction = a;
				return v;
			}
		}
		
		lastAction = action;
		return v;
	}

	int minvalue(String state, int alpha, int beta) {
		if (utility(state) != 0)
			return utility(state);

		int v = Integer.MAX_VALUE;
		String action = "";
		List<String> actions = generateActions(state, "O");

		if (actions.size() == 0)
			return 0;
		
		for (String a : actions) {
			v = min(v, maxvalue(action, alpha, beta));
			if (v <= alpha) {
				return v;
			}
		}

		return v;
	}

}
