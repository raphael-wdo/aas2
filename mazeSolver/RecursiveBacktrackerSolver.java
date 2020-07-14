package mazeSolver;

import java.util.Random;
import java.util.Stack;

import maze.Cell;
import maze.Maze;

/**
 * Implements the recursive backtracking maze solving algorithm.
 */
public class RecursiveBacktrackerSolver implements MazeSolver {
	Maze maze;
	boolean solved;
	boolean[][] visited;

	public RecursiveBacktrackerSolver() {
		this.maze = null;
		// this.randGen = new Random();
		this.solved = false;
		this.visited = null;
	}

	@Override
	public void solveMaze(Maze maze) {
		// Prepares solver to use maze's properties
		this.maze = maze;
		this.visited = new boolean[maze.sizeR][maze.sizeC];

		// Tracks which cell to visit next, starting with maze's entrance
		Stack<Cell> stack = new Stack<Cell>();
		stack.push(maze.entrance);

		// Attempt to solve maze until all options are exhausted
		while (!stack.isEmpty()) {
			// Identify the top of the stack, mark as visited, and draw on maze
			Cell cell = stack.peek();
			this.visited[cell.r][cell.c] = true;
			maze.drawFtPrt(cell);

			// Stop solving maze if reached maze's exit
			if (cell == maze.exit) {
				// Mark as solved and exit function
				this.solved = true;
				return;
			}

			// Travel to end of tunnel if cell is a tunnel
			if ((cell.tunnelTo != null) && (!this.isVisited(cell.tunnelTo)))
				stack.push(cell.tunnelTo);
			// Else choose a route and travel down it till it ends
			else {
				boolean continueWalk = false;
				int[] directionSequence = this.randomizeDirectionSequence();

				for (int i = 0; i < directionSequence.length; i++) {
					Cell nextCell = cell.neigh[directionSequence[i]];
					if ((nextCell != null)
							&& (!cell.wall[directionSequence[i]].present)
							&& (!this.isVisited(nextCell))) {
						stack.push(nextCell);
						continueWalk = true;
						break;
					}
				}
				if (!continueWalk)
					stack.pop();
			}
		}
	} // end of solveMaze()

	@Override
	public boolean isSolved() {
		return this.solved;
	} // end if isSolved()

	@Override
	public int cellsExplored() {
		int visitedCells = 0;

		// Goes through the entire visited array and counts all visited cells
		for (int row = 0; row < this.maze.sizeR; row++) {
			for (int col = 0; col < this.maze.sizeC; col++) {
				if (this.visited[row][col])
					visitedCells++;
			}
		}
		return visitedCells;
	} // end of cellsExplored()

	/**
	 * Assists in identifying whether or not a cell has already been visited.
	 * 
	 * @param cell - The cell that is being inspected.
	 * @return True/False on whether the cell has been visited or not.
	 */
	private boolean isVisited(Cell cell) {
		return this.visited[cell.r][cell.c];
	}

	/**
	 * Generates a random sequence of directions that a cell can head towards.
	 * <br/>
	 * Max 4: North (2), South (5), East (0), West (3).
	 * 
	 * @return A randomized sequence of directions.
	 */
	private int[] randomizeDirectionSequence() {
		// Establish all directions needed for the sequence
		int[] sequence = { Maze.NORTH, Maze.SOUTH, Maze.EAST, Maze.WEST };

		// Shuffle the directions into a random order
		Random random = new Random();
		for (int i = 0; i < sequence.length; i++) {
			int randomIndex = random.nextInt(sequence.length);
			int temp = sequence[i];
			sequence[i] = sequence[randomIndex];
			sequence[randomIndex] = temp;
		}
		return sequence;
	}

} // end of class RecursiveBackTrackerSolver
