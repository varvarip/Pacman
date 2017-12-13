package gr.auth.ee.dsproject.node;

import gr.auth.ee.dsproject.pacman.*;
import java.util.*;

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
    
	  nodeX = 0;
	  nodeY = 0;
	  nodeMove = 0;
	  nodeEvaluation = 0;
	  currentGhostPos = new int[0][0];
	  flagPos = new int[0][0];
	  currentFlagStatus = new boolean[0];  
	  
  }
  
  public Node87828785 (int pacX, int pacY, int pacNext, Room[][] Maze) {
	  
	  nodeX = pacX;
	  nodeY = pacY;
	  nodeMove = pacNext;
	  nodeEvaluation = evaluate(Maze);
	  currentGhostPos = findGhosts(Maze);
	  flagPos = findFlags(Maze);
	  currentFlagStatus = checkFlags(Maze);
	  
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
		          if(k==PacmanUtilities.numberOfGhosts) {
		        	  break;
		            }
		          }

			  }
			 if(k==PacmanUtilities.numberOfGhosts) {
	        	  break;
		  }
	  }
		 return ghostPos;
	  
  }

  private int[][] findFlags (Room[][] Maze)
  {
	  int[][] flagPosition = new int[PacmanUtilities.numberOfFlags][2];
	  int k=0;
	   
		 for(int i=0;i<PacmanUtilities.numberOfRows;i++) {
			 for(int j=0; j<PacmanUtilities.numberOfColumns; j++) {
				 
				  if(Maze[i][j].isFlag()) {
					  flagPosition[k][0] = i;
					  flagPosition[k][1] = j;
					  k++;  
		          if(k==PacmanUtilities.numberOfFlags) {
		        	  break;
		            }
		          }

			  }
			 if(k==PacmanUtilities.numberOfFlags) {
	        	  break;
		  }
	  }
		 return flagPosition;
  }

  private boolean[] checkFlags (Room[][] Maze)
  {
	  boolean[] flagStatus= new boolean[PacmanUtilities.numberOfFlags];
		int f;
		
		for (f=0; f<PacmanUtilities.numberOfFlags; f++) {
			
				flagStatus[f]= Maze[flagPos[f][0]][flagPos[f][1]].isCapturedFlag();
				
		}
		
		return flagStatus;
  }

  private double evaluate (Room[][] Maze)
  {

    double evaluation = 0;
    int TempMinGhostDistance;
    int MinGhostDistance;
    
    if (Maze[nodeX][nodeY].walls[nodeMove] == 0) {
    	return -100;
    }else {
    
    ArrayList<Integer> Distances = new ArrayList<Integer>();
    
    for(int i=0; i< PacmanUtilities.numberOfGhosts; i++) {
    	Distances.add(absolutValue(currentGhostPos[i][0] - nodeY) + absolutValue(currentGhostPos[i][1] - nodeX));
    }
    
    Collections.sort(Distances);
    MinGhostDistance = Distances.get(0);
   
    
    switch (nodeMove) {
    	case 0: nodeX--;
    			break;
    	case 1: nodeY++;
    			break;
    	case 2: nodeX++;
    			break;
    	case 3: nodeY--;
    			break;    	
    }
    
    for(int i=0; i< PacmanUtilities.numberOfGhosts; i++) {
    	Distances.add(absolutValue(currentGhostPos[i][0] - nodeY) + absolutValue(currentGhostPos[i][1] - nodeX));
    }
    
    Collections.sort(Distances);
    TempMinGhostDistance = Distances.get(0);
    
    evaluation = TempMinGhostDistance - MinGhostDistance;
        
    return evaluation;
    }

  }
  
  private int absolutValue(int x) {
	  if(x<=0) {
		  x=-x;
		  return x;
	  }else {return x;}
  }
  

}

