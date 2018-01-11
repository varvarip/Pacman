package gr.auth.ee.dsproject.pacman;

import gr.auth.ee.dsproject.node.Node;
import java.util.*;

/**
 * <p>
 * Title: DataStructures2011
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

  public ArrayList<ArrayList<Node>> nodeArray = new ArrayList<ArrayList<Node>>();
  public ArrayList<Node> punk = new ArrayList<Node>();
  
  public int calculateNextPacmanPosition (Room[][] Maze, int[] currPosition)
  {
    Node parentNode = new Node(currPosition[0], currPosition[1], -1, 0, Maze);
    createSubTreePacman(1, parentNode,Maze,currPosition);
    for(int i =0; i<nodeArray.size(); i++) {
    	double min = nodeArray.get(i).get(0).getEvaluate();
    	for(int j=0; j<nodeArray.get(i).size(); j++) {    		
    		if(min > nodeArray.get(i).get(j).getEvaluate()) {
    			min = nodeArray.get(i).get(j).getEvaluate();
    		}
    	}
    	double[] leaveMin = new double[nodeArray.size()];
    	leaveMin[i] = min;
    	
    	int direction = 0;
    	double max = leaveMin[0];
    	if(max < leaveMin[i]) {
    		max  = leaveMin[i];
    		direction = i;
    	}
    	
    	return direction;
    }
    
    

    return 0;
  }

  void createSubTreePacman (int depth, Node parent, Room[][] Maze, int[] currPacmanPosition)
  {
	  ////////// traffic lights ////////////////
	  int d,g,l;	
	  	int pacmanX=currPacmanPosition[0], pacmanY=currPacmanPosition[1];
	    int [][] possPac= new int[4][2];
	    int[][] currGhost= new int[PacmanUtilities.numberOfGhosts][2];
	    int[][] possGhost= new int[4*PacmanUtilities.numberOfGhosts][2];
	    Node currPacNode= new Node(pacmanX, pacmanY, Maze);
	  	boolean[][] trafficLight= new boolean[4][3];
	  	ArrayList<Node> validMoves= new ArrayList<Node>(4);
		
		
	  	for (d=0; d<4; d++) {
	  			for (l=0; l <3; l++) {	  				
	  					trafficLight[d][l]=true;}
	  			possPac[d] = currPacNode.findTempPos(pacmanX, pacmanY, d);
	  			for (g=0; g<PacmanUtilities.numberOfGhosts; g++) {	  				
	  					possGhost[4*g+d]=currPacNode.findTempPos(currGhost[g][0],currGhost[g][1], d);	}}  
	  	for (d=0; d<4; d++) {
				if (Maze[pacmanX][pacmanY].walls[d]==0) {				
						trafficLight[d][0]=false;
						trafficLight[d][1]=false;
						trafficLight[d][2]=false;}}
	  	
				for (d=0; d<4; d++) { 
						for (g=0; g<PacmanUtilities.numberOfGhosts; g++) {
								if((possPac[d][0]==currGhost[g][0]) && (possPac[d][1]==currGhost[g][1])) {										
										trafficLight[d][0]=false;
										trafficLight[d][1]=false;}}}
						for (d=0; d<4; d++) { 
							for (g=0; g<4*PacmanUtilities.numberOfGhosts; g++) {
								if((possPac[d][0]==possGhost[g][0]) && (possPac[d][1]==possGhost[g][1])) {
										trafficLight[d][0]= false;}}}
		for (d=0; d<4; d++) {
				if (trafficLight[d][0]) {
						Node movedPacNode= new Node(pacmanX, pacmanY, d, Maze);
						validMoves.add(movedPacNode);
						System.out.println("Valid______"+movedPacNode.getNodeMove());}}
	if (validMoves.isEmpty()) {
				System.out.println("DEN EIXE FREE KAI YIOLARE");
				for (d=0; d<4; d++) { 					
						if (trafficLight[d][1]) {							
								Node movedPacNode= new Node(pacmanX, pacmanY, d, Maze);
								validMoves.add(movedPacNode);
								System.out.println("Valid__"+movedPacNode.getNodeMove());}}}
		if (validMoves.isEmpty()) {
				for (d=0; d<4; d++) {
						if (trafficLight[d][2]) {
								Node movedPacNode= new Node(pacmanX, pacmanY, d, Maze);
								validMoves.add(movedPacNode);}}}
		////////////////////////////////////////////////////////////////////////////////////////////////
		
		for(int i =0; i<validMoves.size();i++) {
			Room[][] roomCopy1 = PacmanUtilities.copy(Maze);
			
			int[] newPacPos = PacmanUtilities.evaluateNextPosition(roomCopy1, currPacmanPosition, validMoves.get(i).getNodeMove(), PacmanUtilities.borders);
			
			PacmanUtilities.movePacman(roomCopy1, currPacmanPosition, newPacPos);
			
			Node istChildNode = new Node(newPacPos[0], newPacPos[1],-1,depth,roomCopy1);
			
			parent.getChildren().add(i,istChildNode);
			
			createSubTreeGhosts(depth+1,istChildNode,roomCopy1,istChildNode.getGhostPos());
			
			nodeArray.add(validMoves.get(i).getNodeMove(),punk);			
		}
    

  }

  void createSubTreeGhosts (int depth, Node parent, Room[][] Maze, int[][] currGhostsPosition)
  {
	  ArrayList<int[][]> ghostMovesArray = PacmanUtilities.allGhostMoves(Maze, currGhostsPosition);
	  
	  
	  for(int i=0; i<ghostMovesArray.size(); i++) {
		  Room[][] roomCopy2 = PacmanUtilities.copy(Maze);
		  PacmanUtilities.moveGhosts(roomCopy2, currGhostsPosition, ghostMovesArray.get(i));
		  Node istNode = new Node(parent.getNodeX(), parent.getNodeY(), -1, depth, roomCopy2);
		  punk.add(i, istNode); 
		  
	  }	 
  }
  
  
  

  public int[] getPacPos (Room[][] Maze)
  {
    int[] pacmanPos = new int[2];
    for (int i = 0; i < PacmanUtilities.numberOfRows; i++) {
      for (int j = 0; j < PacmanUtilities.numberOfColumns; j++) {
        if (Maze[i][j].isPacman()) {
          pacmanPos[0] = i;
          pacmanPos[1] = j;
          return pacmanPos;
        }
      }
    }
    return pacmanPos;
  }

  public boolean[] comAvPos (Room[][] Maze, int[][] currentPos, int[] moves, int currentGhost)
  {

    boolean[] availablePositions = { true, true, true, true };

    int[][] newPos = new int[4][2];

    for (int i = 0; i < 4; i++) {

      if (Maze[currentPos[currentGhost][0]][currentPos[currentGhost][1]].walls[i] == 0) {
        availablePositions[i] = false;
        continue;
      }

      if (PacmanUtilities.flagColision(Maze, currentPos[currentGhost], i)) {
        availablePositions[i] = false;
      }

      else if (currentGhost == 0)
        continue;

      else {
        switch (i) {
        case Room.WEST:
          newPos[currentGhost][0] = currentPos[currentGhost][0];
          newPos[currentGhost][1] = currentPos[currentGhost][1] - 1;
          break;
        case Room.SOUTH:
          newPos[currentGhost][0] = currentPos[currentGhost][0] + 1;
          newPos[currentGhost][1] = currentPos[currentGhost][1];
          break;
        case Room.EAST:
          newPos[currentGhost][0] = currentPos[currentGhost][0];
          newPos[currentGhost][1] = currentPos[currentGhost][1] + 1;
          break;
        case Room.NORTH:
          newPos[currentGhost][0] = currentPos[currentGhost][0] - 1;
          newPos[currentGhost][1] = currentPos[currentGhost][1];

        }

        for (int j = (currentGhost - 1); j > -1; j--) {
          switch (moves[j]) {
          case Room.WEST:
            newPos[j][0] = currentPos[j][0];
            newPos[j][1] = currentPos[j][1] - 1;
            break;
          case Room.SOUTH:
            newPos[j][0] = currentPos[j][0] + 1;
            newPos[j][1] = currentPos[j][1];
            break;
          case Room.EAST:
            newPos[j][0] = currentPos[j][0];
            newPos[j][1] = currentPos[j][1] + 1;
            break;
          case Room.NORTH:
            newPos[j][0] = currentPos[j][0] - 1;
            newPos[j][1] = currentPos[j][1];
            // break;
          }

          if ((newPos[currentGhost][0] == newPos[j][0]) && (newPos[currentGhost][1] == newPos[j][1])) {

            availablePositions[i] = false;
            continue;
          }

          if ((newPos[currentGhost][0] == currentPos[j][0]) && (newPos[currentGhost][1] == currentPos[j][1]) && (newPos[j][0] == currentPos[currentGhost][0])
              && (newPos[j][1] == currentPos[currentGhost][1])) {

            availablePositions[i] = false;

          }
        }
      }
    }

    return availablePositions;
  }

  public int comBestPos (boolean[] availablePositions, int[] pacmanPosition, int[] currentPos)
  {

    int[] newVerticalDifference = new int[2];
    for (int i = 0; i < 2; i++)
      newVerticalDifference[i] = currentPos[i] - pacmanPosition[i];

    int[] distanceSquared = new int[4];

    for (int i = 0; i < 4; i++) {
      if (availablePositions[i] == true) {

        switch (i) {
        case Room.WEST:
          newVerticalDifference[1]--;
          break;
        case Room.SOUTH:
          newVerticalDifference[0]++;
          break;
        case Room.EAST:
          newVerticalDifference[1]++;
          break;
        case Room.NORTH:
          newVerticalDifference[0]--;
          break;
        }
        distanceSquared[i] = newVerticalDifference[0] * newVerticalDifference[0] + newVerticalDifference[1] * newVerticalDifference[1];
      } else
        distanceSquared[i] = PacmanUtilities.numberOfRows * PacmanUtilities.numberOfRows + PacmanUtilities.numberOfColumns * PacmanUtilities.numberOfColumns + 1;
    }

    int minDistance = distanceSquared[0];
    int minPosition = 0;

    for (int i = 1; i < 4; i++) {
      if (minDistance > distanceSquared[i]) {
        minDistance = distanceSquared[i];
        minPosition = i;
      }

    }

    return minPosition;
  }

  public int[] calculateNextGhostPosition (Room[][] Maze, int[][] currentPos)
  {

    int[] moves = new int[PacmanUtilities.numberOfGhosts];

    int[] pacmanPosition = new int[2];

    pacmanPosition = getPacPos(Maze);
    for (int i = 0; i < PacmanUtilities.numberOfGhosts; i++) {
      moves[i] = comBestPos(comAvPos(Maze, currentPos, moves, i), pacmanPosition, currentPos[i]);
    }

    return moves;

  }

  public boolean[] checkCollision (int[] moves, int[][] currentPos)
  {
    boolean[] collision = new boolean[PacmanUtilities.numberOfGhosts];

    int[][] newPos = new int[4][2];

    for (int i = 0; i < moves.length; i++) {

      if (moves[i] == 0) {
        if (currentPos[i][1] > 0) {
          newPos[i][0] = currentPos[i][0];
          newPos[i][1] = currentPos[i][1] - 1;
        } else {
          newPos[i][0] = currentPos[i][0];
          newPos[i][1] = PacmanUtilities.numberOfColumns - 1;
        }

      } else if (moves[i] == 1) {
        if (currentPos[i][0] < PacmanUtilities.numberOfRows - 1) {
          newPos[i][0] = currentPos[i][0] + 1;
          newPos[i][1] = currentPos[i][1];
        } else {
          newPos[i][0] = 0;
          newPos[i][1] = currentPos[i][1];
        }
      } else if (moves[i] == 2) {
        if (currentPos[i][1] < PacmanUtilities.numberOfColumns - 1) {
          newPos[i][0] = currentPos[i][0];
          newPos[i][1] = currentPos[i][1] + 1;
        } else {
          newPos[i][0] = currentPos[i][0];
          newPos[i][1] = 0;

        }
      } else {
        if (currentPos[i][0] > 0) {
          newPos[i][0] = currentPos[i][0] - 1;
          newPos[i][1] = currentPos[i][1];
        } else {

          newPos[i][0] = PacmanUtilities.numberOfRows - 1;
          newPos[i][1] = currentPos[i][1];

        }
      }

      collision[i] = false;
    }

    for (int k = 0; k < moves.length; k++) {

    }

    for (int i = 0; i < moves.length; i++) {
      for (int j = i + 1; j < moves.length; j++) {
        if (newPos[i][0] == newPos[j][0] && newPos[i][1] == newPos[j][1]) {

          collision[j] = true;
        }

        if (newPos[i][0] == currentPos[j][0] && newPos[i][1] == currentPos[j][1] && newPos[j][0] == currentPos[i][0] && newPos[j][1] == currentPos[i][1]) {

          collision[j] = true;
        }

      }

    }
    return collision;
  }

}
