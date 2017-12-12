package gr.auth.ee.dsproject.node;

import gr.auth.ee.dsproject.pacman.*;

public class Node87828785
{

  int nodeX;
  int nodeY;
  int nodeMove;
  double nodeEvaluation;
  int[][] currentGhostPos;
  int[][] flagPos;
  boolean[] currentFlagStatus;
  
  int nextPacmanPosition = calculateNextPacmanPosition (Room[][] Maze, int[] currPosition);

  public Node87828785 () // Constructor
  {
    // TODO Fill this
	  nodeX = 0;
	  nodeY = 0;
	  nodeMove = 0;
	  nodeEvaluation = 0;
	  currentGhostPos = new int[0][0];
	  flagPos = new int[0][0];
	  currentFlagStatus = new boolean[0];  
	  
  }
  
  public Node87828785 (int pacX, int pacY, int pacNext, double ev, int [][] ghostPos, int[][] flags, boolean[] flagStat) {
	  
	  nodeX = pacX;
	  nodeY = pacY;
	  nodeMove = pacNext;
	  nodeEvaluation = ev;
	  currentGhostPos = ghostPos;
	  flagPos = flags;
	  currentFlagStatus = flagStat;
	  
  }

  private int[][] findGhosts (Room[][] Maze)
  {    	  
	  int[][] ghostPos = new int[PacmanUtilities.numberOfGhosts][2];
	  int k=0;
	   
		 for(int i=0;i<PacmanUtilities.numberOfRows;i++) {
			 for(int j=0; j<PacmanUtilities.numberOfColumns; j++) {
				 
				  if(Maze[i][j].isGhost()) {
					  ghostPos[k][0] = i;
					  ghostPos[k][1] = j;
					  k++;  
		          if(k==4) {
		        	  break;
		            }
		          }

			  }
			 if(k==4) {
	        	  break;
		  }
	  }
		 return ghostPos;
	  
  }

  private int[][] findFlags ()
  {
    // TODO Fill this
  }

  private boolean[] checkFlags ()
  {
    // TODO Fill this
  }

  private double evaluate ()
  {

    double evaluation = 0;
    // TODO Fill this
    return evaluation;

  }

}
