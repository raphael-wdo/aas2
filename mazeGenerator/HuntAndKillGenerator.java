package mazeGenerator;

import maze.Maze;
import maze.Cell;
import java.util.concurrent.ThreadLocalRandom;

// AUTH: RAPHAEL WONG
// DATE: 6/10/2019
// DESC: MAZE GENERATIOR USING HUNT AND KILL ALGORITHM
public class HuntAndKillGenerator implements MazeGenerator {

	public int size;
	public Cell[] visitedCellArray;
	public int visitedCells;
	public int[] direction;

	public HuntAndKillGenerator() {
		visitedCells = 0;
	}

	@Override
	public void generateMaze(Maze maze) {
		// TODO Auto-generated method stub

		size = maze.sizeR * maze.sizeC;
		visitedCellArray  = new Cell[size];
		direction = new int[]{maze.NORTH, maze.EAST, maze.SOUTH, maze.WEST};

		// Pick a random starting cell
		int randomX = ThreadLocalRandom.current().nextInt(0, maze.sizeC);
		int randomY = ThreadLocalRandom.current().nextInt(0, maze.sizeR);
		Cell randomCell = maze.map[randomY][randomX];
		visitedCellArray[visitedCells] = randomCell;
		visitedCells++;

		boolean generateComplete = false;
		while(!generateComplete) {
			walk(randomCell, maze);
			randomCell = hunt(maze);
			if (randomCell==null) {
				generateComplete = true;

			}
		}

	} // end of generateMaze()

	/* Randomly select an unvisited neighbouring cell and carve a passage to
	* the neighbour.
	* Repeating this until the current cell has no unvisited neighbours.
	*/
	private void walk(Cell randomCell, Maze maze) {
		boolean unvisitedNeighboursRemain = true;

		// Go to tunnel if exists
		while (unvisitedNeighboursRemain) {
			int randDirection = ThreadLocalRandom.current().nextInt(0, direction.length);
			Cell randomCellNeighbour;

			//Check if randomCellNeighbour is visited
			int numOfNeighboursChecked = 0;
			for (int i = 0; i < direction.length; i++) {
				if (!checkCellVisited(randomCell.neigh[direction[randDirection]]) && randomCell.neigh[direction[randDirection]] != null) {
					randomCellNeighbour = randomCell.neigh[direction[randDirection]];
					// Connect the cell to the neighbourhood cell
					randomCell.wall[direction[randDirection]].present = false;
					randomCell = randomCellNeighbour;
					visitedCellArray[visitedCells] = randomCell;
					//System.out.println("C: " + randomCell.c + " | R: " + randomCell.r);
					visitedCells++;

					// Go to tunnel if exists
					if (randomCell.tunnelTo != null)
					{
						randomCell = randomCell.tunnelTo;
						visitedCellArray[visitedCells] = randomCell;
						visitedCells++;
					}
					break;
				}
				else {
					// change neighbour cell
					randDirection++;
					numOfNeighboursChecked++;
					if (randDirection == direction.length) {
						randDirection = 0;
					}
					// If the neighbour changed four times, the program cannot locate anymore unvisited neighbours
					if (numOfNeighboursChecked == direction.length) {
						unvisitedNeighboursRemain = false;
						break;
					}
					// else {
					// 	randomCellNeighbour = randomCell.neigh[direction[randDirection]];
					// }
				}
			}
		}


	}

	private boolean checkCellVisited(Cell randomCellNeighbour) {
		for (Cell visitedCell : visitedCellArray) {
			if (randomCellNeighbour == visitedCell) {
				return true;
			}
		}
		return false;
	}

	/* Enter the “hunt” mode, where the algorithm scans the grid searching for
	* an unvisited cell that is adjacent to a visited cell. If found, carve a
	* passage between the two and let the formerly unvisited cell be the new
	* starting location.
	*/
	private Cell hunt(Maze maze) {

		// random cell from visited cells
		int randomCellNumber = ThreadLocalRandom.current().nextInt(0, visitedCells-1);

		for (int rcnOffset = 0; rcnOffset < visitedCells-1; rcnOffset++) {
			//get random visited cell
			Cell randomCellVisited = visitedCellArray[randomCellNumber];

			// check for neighbour
			int randDirection = ThreadLocalRandom.current().nextInt(0, direction.length);
			for (int i = 0; i < direction.length; i++) {
				if (randomCellVisited.neigh[direction[randDirection]] != null && !checkCellVisited(randomCellVisited.neigh[direction[randDirection]])) {
					randomCellVisited.wall[direction[randDirection]].present = false;
					visitedCellArray[visitedCells] = randomCellVisited.neigh[direction[randDirection]];
					visitedCells++;
					return randomCellVisited.neigh[direction[randDirection]];
				}
				else {
					randDirection++;
					if (randDirection == direction.length) {
						randDirection = 0;
					}
				}
			}

			// if the random cell has no neighbour, go to next cell visited until there is no cell left
			randomCellNumber++;
			if (randomCellNumber == visitedCells-1) {
				randomCellNumber = 0;
			}
		}

		return null;
	}

} // end of class HuntAndKillGenerator
