package mazeGenerator;

import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

import maze.Maze;

// COSC2123 Algorithms and Analysis | 2019 S2
// Author: 		S3602814 Thien Nguyen
// Completed: 	12/10/2019
// Description: Maze Generator using Kruskal's Algorithm
public class KruskalGenerator implements MazeGenerator {
	private final static int TREE_TOKENS = 4; // Required for cell tracking
	private int[][] trees;
	private ArrayList<Integer> uniqueTrees; // Assists in preventing cycles
	private ArrayList<String> edges;

	@Override
	public void generateMaze(Maze maze) {
		StringTokenizer temp;
		int index;
		int[] tok = new int[TREE_TOKENS];

		initializeTrees(maze.sizeR, maze.sizeC);

		// Repeat until all viable edges have been removed
		while (edges.size() != 0) {
			// Select a random edge and identify its coordinates
			index = randomizeIndex(edges.size());
			temp = new StringTokenizer(edges.get(index));
			for (int i = 1; temp.hasMoreTokens(); i++) {
				tok[i - 1] = Integer.parseInt(temp.nextToken());
			}

			// Check for cycles before breaking down wall
			if (checkTree(tok[0], tok[1], tok[2], tok[3]) == true) {
				joinTrees(trees[tok[0]][tok[1]], trees[tok[2]][tok[3]]);
				// Determine which wall to break down
				switch (selectWall(tok[0], tok[1], tok[2], tok[3])) {
				case Maze.NORTH:
					maze.map[tok[0]][tok[1]].wall[Maze.NORTH].present = false;
					break;
				case Maze.EAST:
					maze.map[tok[0]][tok[1]].wall[Maze.EAST].present = false;
					break;
				}
			}
			edges.remove(index);
		}

	} // end of generateMaze()

	/**
	 * Function that initializes the sets array based on maze sizes.
	 * 
	 * @param rowSize Total number of rows in the maze.
	 * @param colSize Total number of columns in the maze.
	 */
	private void initializeTrees(int rowSize, int colSize) {
		trees = new int[rowSize][colSize];
		uniqueTrees = new ArrayList<Integer>();
		edges = new ArrayList<String>();

		// Populate each tree cell with a unique value and create edge if able
		int element = 0;
		for (int row = 0; row < rowSize; row++) {
			for (int col = 0; col < colSize; col++) {
				trees[row][col] = element;
				uniqueTrees.add(element);
				element++;
				// Add edges after confirming have not reached maze border
				if (col < colSize - 1)
					edges.add(row + " " + col + " " + row + " " + (col + 1));
				if (row < rowSize - 1)
					edges.add(row + " " + col + " " + (row + 1) + " " + col);
			}
		}
	}

	/**
	 * Function that determines whether or not the two trees could join or not.
	 * This is mainly used to prevent creating a cycle in the maze.
	 * 
	 * @param tree1R First tree's set row.
	 * @param tree1C First tree's set column.
	 * @param tree2R Second tree's set row.
	 * @param tree2C Second tree's set column.
	 * @return Result of whether or not tree sets can be joined.
	 */
	private boolean checkTree(int tree1R, int tree1C, int tree2R, int tree2C) {
		// Compare tree sets to see if they could be joined
		if (trees[tree1R][tree1C] != trees[tree2R][tree2C])
			return true;

		// Exit if both trees belong in the same set
		return false;
	}

	/**
	 * Function that scans through all cells in tree array and reassigns those
	 * whose value matches the target tree to the model tree.
	 * 
	 * @param modelTree  The tree that is being kept in the Trees array.
	 * @param targetTree the tree that being replaced in the Trees array.
	 */
	private void joinTrees(int modelTree, int targetTree) {
		// Scan through Trees array and replace target tree with model tree
		for (int row = 0; row < trees.length; row++) {
			for (int col = 0; col < trees[row].length; col++) {
				if (trees[row][col] == targetTree)
					trees[row][col] = modelTree;
			}
		}

		// Remove target tree from list of unique trees
		for (int i = 0; i < uniqueTrees.size(); i++) {
			if (uniqueTrees.get(i).equals(targetTree)) {
				uniqueTrees.remove(i);
				break;
			}
		}
	}

	/**
	 * Function that returns a random index within a range that an intended
	 * array can reach based on the given array's total size.
	 * 
	 * @param arraySize Total size of the array.
	 * @return A random index that the array can reach.
	 */
	private int randomizeIndex(int arraySize) {
		Random random = new Random();
		int min = 0, max = arraySize - 1;
		return random.nextInt(max - min + 1) + min;
	}

	/**
	 * Function that determines the wall to select based on the coordinates of
	 * the two tree cells given.
	 * 
	 * @param tree1R First tree's set row.
	 * @param tree1C First tree's set column.
	 * @param tree2R Second tree's set row.
	 * @param tree2C Second tree's set column.
	 * @return Value of the wall that is to be selected.
	 */
	private int selectWall(int tree1R, int tree1C, int tree2R, int tree2C) {
		if (tree2R > tree1R)
			return Maze.NORTH;
		if (tree2C > tree1C)
			return Maze.EAST;
		return 0;
	}

} // end of class KruskalGenerator
