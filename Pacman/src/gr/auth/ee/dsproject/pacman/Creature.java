package gr.auth.ee.dsproject.pacman;

/**
 * <p>
 * Title: DataStructures2006
 * </p>
 * 
 * <p>
 * Description: Data Structures project: year 2011-2012
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * 
 * <p>
 * Company: A.U.Th.
 * </p>
 * 
 * @author Michael T. Tsapanos
 * @version 1.0
 */
public class Creature implements gr.auth.ee.dsproject.pacman.AbstractCreature
{

  public String getName ()
  {
    return "Mine";
  }

  private int step = 1;
  private boolean amPrey;

  public Creature (boolean isPrey)
  {
    amPrey = isPrey;

  }

  public int calculateNextPacmanPosition (Room[][] Maze, int[] currPosition)
  {

    int newDirection = -1;

    while (newDirection == -1) {

      int temp_dir = (int) (4 * Math.random());

      if (Maze[currPosition[0]][currPosition[1]].walls[temp_dir] == 1) {
        newDirection = temp_dir;
      }

    }
    step++;

    return newDirection;
  }
  

  // VARVARINGOS IOANNIS		8782	6930413691	varvarip@ece.auth.gr
  // VELENTZAS IASON-GEORGIOS	8785	6944144933	velentzas@ece.auth.gr
  
  
  /* calculateNextGhostPosition() is a method used in the game to calculate the random, but allowed moves the ghosts will implement.
     The function's arguments are the game's grid and and a two-dimensional table with the current position of each ghost.
     For this method two new integer table variables and one boolean table variable are introduced.
     
  	 - newGhostDirection has a length equal to the number of ghosts and holds the allowed random move each ghost will implement. This is the returned integer table.
  	 - temp_ghost_dir also has a length equal to the number of ghosts, but holds the random, not surely allowed, moves each ghost may implement.
  	 - ghostCollisionStatus also has a length equal to the number of ghosts and each element is true, if the ghost represented by that element collides with another,
  	   or false if it does not. */
  
  
  public int[] calculateNextGhostPosition (Room[][] Maze, int[][] currentPos)
  {	  
	  
	// Creation of a new integer table variable (newGhostDirection) with length equal to the number of ghosts.
	  int[] newGhostDirection = new int[PacmanUtilities.numberOfGhosts];  
	  
	// Creation of a new integer table variable (temp_ghot_dir) with length equal to the number of ghosts.
	  int[] tempGhostDir = new int[PacmanUtilities.numberOfGhosts];
	  	
	  /* At first, as in calculateNextPacmanPosition() above, we set each element of newGhostDirection equal to -1,
	 	 to make sure none of the direction numbers (0,1,2,3) are involved by mistake. We also set each element of temp_ghost_dir
	 	 equal to one of the four possible directions, which we will later check if is allowed.*/
	  
	  for(int i=0; i<PacmanUtilities.numberOfGhosts; i++){	
		  newGhostDirection[i] = -1;
		  tempGhostDir[i] = (int)(Math.random()*4);		  
	  }
	 	  
	  // For each ghost, newGhostDirection holds, after this for-loop, an allowed random move.
	  
	  for(int i=0; i<PacmanUtilities.numberOfGhosts; i++){	
		   
		  newGhostDirection[i] = -1;
		  tempGhostDir[i] = (int)(Math.random()*4);
		  
		  //As long as newGhostDirection remains equal to -1 for the i-ghost, an allowed move for the ghost has not yet been found.
		  
		  while(newGhostDirection[i] == -1){
			  
			 // We use the function checkCollision() to see whether the random moves that temp_ghost_dir holds, will cause a collision with another ghost.
						  
			  boolean[] ghostCollisionStatus = checkCollision(tempGhostDir, currentPos);
			  
			  // If the i-ghost's random move is allowed (there is no collision with another ghost and no wall in this direction),
			  // then the i-ghost's move is set to that direction. Differently, we generate another random direction for the i-ghost.
			  
			  if (Maze[currentPos[i][0]][currentPos[i][1]].walls[tempGhostDir[i]] == 1 && !ghostCollisionStatus[i]){
				  newGhostDirection[i] = tempGhostDir[i];
			  }else {
				  tempGhostDir[i] = (int)(Math.random()*4);
			  }
		  }
		  
		  //At this stage an allowed move has been found for the i-ghost and we are moving on to finding the next one.
	  }
	  
	  // We return the integer table that holds the new allowed random direction of each ghost.
	  
	  return newGhostDirection;
  }
  
  

  public boolean[] checkCollision (int[] moves, int[][] currentPos)
  {
    boolean[] collision = new boolean[PacmanUtilities.numberOfGhosts];

    int[][] newPos = new int[4][2];

    for (int i = 0; i < moves.length; i++) {

      if (moves[i] == 0) {
        newPos[i][0] = currentPos[i][0];
        newPos[i][1] = currentPos[i][1] - 1;
      } else if (moves[i] == 1) {
        newPos[i][0] = currentPos[i][0] + 1;
        newPos[i][1] = currentPos[i][1];
      } else if (moves[i] == 2) {
        newPos[i][0] = currentPos[i][0];
        newPos[i][1] = currentPos[i][1] + 1;
      } else {
        newPos[i][0] = currentPos[i][0] - 1;
        newPos[i][1] = currentPos[i][1];
      }

      collision[i] = false;
    }

    for (int k = 0; k < moves.length; k++) {
      // System.out.println("Ghost " + k + " new Position is (" + newPos[k][0] + "," + newPos[k][1] + ").");
    }

    for (int i = 0; i < moves.length; i++) {
      for (int j = i + 1; j < moves.length; j++) {
        if (newPos[i][0] == newPos[j][0] && newPos[i][1] == newPos[j][1]) {
          // System.out.println("Ghosts " + i + " and " + j + " are colliding");
          collision[j] = true;
        }

        if (newPos[i][0] == currentPos[j][0] && newPos[i][1] == currentPos[j][1] && newPos[j][0] == currentPos[i][0] && newPos[j][1] == currentPos[i][1]) {
          // System.out.println("Ghosts " + i + " and " + j + " are colliding");
          collision[j] = true;
        }

      }

    }
    return collision;
  }

}
