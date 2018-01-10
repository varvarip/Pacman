package gr.auth.ee.dsproject.node;

import gr.auth.ee.dsproject.pacman.*;

import java.util.*;

public class Node
{

  int nodeX;
  int nodeY;
  int depth;
  int nodeMove;
  double nodeEvaluation;
  int[][] currentGhostPos;
  int[][] flagPos;
  boolean[] currentFlagStatus;

  Node parent;
  ArrayList<Node> children = new ArrayList<Node>();
  
  public ArrayList<Node> getChildren() {
		
		return children;
		
}
  
  public int[][] getGhostPos() {
		
		return currentGhostPos;
		
}
  public int getNodeX() {
		
		return nodeX;
		
}

  public int getNodeY() {
		
		return nodeY;
		
}
  // Constructor
  public Node (int x, int y,Room[][] Maze)
  {
	  	nodeX=x;
	  	nodeY=y;
	  	nodeMove=-1;
	  	nodeEvaluation= 0;
	  	currentGhostPos=findGhosts(Maze);
	  	flagPos=findFlags(Maze);
	  	currentFlagStatus=checkFlags(Maze);    
  }
  
  public Node (int x, int y, int d, Room[][] Maze) {

	  	nodeX=x;
	  	nodeY=y;
	  	nodeMove=d;
		currentGhostPos=findGhosts(Maze);
	  	flagPos=findFlags(Maze);
	  	currentFlagStatus=checkFlags(Maze);
	  	nodeEvaluation= evaluate();
}
  
  public Node (int x, int y, int d, int deep, Room[][] Maze) {

	  	nodeX=x;
	  	nodeY=y;
	  	nodeMove = d;
	  	depth = deep;
	  	currentGhostPos=findGhosts(Maze);
	  	flagPos=findFlags(Maze);
	  	currentFlagStatus=checkFlags(Maze);
	  	nodeEvaluation= evaluate();
}
  
  public int getNodeMove() {
		
		return nodeMove;
		
}

  ///////////// FIND GHOSTS //////////////////////
  private int[][] findGhosts (Room[][] Maze)
  {
	  int r,c, ctr=0;
   		int [][] curGhostPos= new int [PacmanUtilities.numberOfGhosts][2];		
		
		for ( r=0; r < PacmanUtilities.numberOfRows; r++) {
			
				for (c=0; c < PacmanUtilities.numberOfColumns; c++) {					
						if (Maze[r][c].isGhost()) {								
								curGhostPos[ctr][0]=r;
								curGhostPos[ctr][1]=c;								
								ctr++;
							}						
						if (ctr== PacmanUtilities.numberOfGhosts) {							
								break;	}
							}
				if (ctr== PacmanUtilities.numberOfGhosts) {						
						break;						
				}				
		}
		return curGhostPos;  
		
  }
  
  ///////////// FIND FLAGS ///////////////////////

  private int[][] findFlags (Room[][] Maze)
  {
	  int r,c, ctr=0;
		int [][] curFlagPos= new int [PacmanUtilities.numberOfFlags][2];
		
		for ( r=0; r < PacmanUtilities.numberOfRows; r++) {
			
				for (c=0; c < PacmanUtilities.numberOfColumns; c++) {							
						if (Maze[r][c].isFlag()) {									
								curFlagPos[ctr][0] = r;
								curFlagPos[ctr][1] = c;
								ctr++;										
						}								
						if (ctr== PacmanUtilities.numberOfFlags) {									
								break;										
						}								
				}
				if (ctr== PacmanUtilities.numberOfFlags) {						
						break;						
				}
			} 		
		return curFlagPos;  }
  
  ///////////// CHECK FLAGS /////////////////

  private boolean[] checkFlags (Room[][] Maze)
  {  
	  	int f;
		boolean[] flagStatus= new boolean[PacmanUtilities.numberOfFlags];	
		for (f=0; f<PacmanUtilities.numberOfFlags; f++) {			
				flagStatus[f]= Maze[flagPos[f][0]][flagPos[f][1]].isCapturedFlag();				
		}		
		return flagStatus;
  }

  
  /////////////// EVALUATE!!!! //////////////////////
  private double evaluate ()
  {double evaluation=0;
	int g,f, stepsAhead=0, captureableFlags=0;	
	int centerized=findDistToEdge(); 
	ArrayList<Integer> ghostsOfInterest= new ArrayList<Integer>(PacmanUtilities.numberOfGhosts);
	for (g=0; g<PacmanUtilities.numberOfGhosts; g++)  {			
			if (nodeMove%2==1){
					if (relativePos(currentGhostPos, PacmanUtilities.numberOfGhosts)[g][0]==nodeMove) {
							ghostsOfInterest.add(taxicabDistance(nodeX, nodeY, currentGhostPos[g][0], currentGhostPos[g][1] ));
					}					
			}
			else {if (relativePos(currentGhostPos, PacmanUtilities.numberOfGhosts)[g][1]==nodeMove) {
				ghostsOfInterest.add(taxicabDistance(nodeX, nodeY, currentGhostPos[g][0], currentGhostPos[g][1] ));							
					}					
			}			
	}
	HashMap <Integer, Integer > flagsOfInterest= new HashMap<Integer, Integer>(PacmanUtilities.numberOfFlags);
	
	for (f=0; f<PacmanUtilities.numberOfFlags; f++) {
			if (!currentFlagStatus[f]) {
					if (nodeMove%2==1){
							if (relativePos(flagPos, PacmanUtilities.numberOfFlags)[f][0]==nodeMove) {
									flagsOfInterest.put(f, taxicabDistance(nodeX, nodeY, flagPos[f][0], flagPos[f][1] ));									
							}							
					}	
					else {if (relativePos(flagPos, PacmanUtilities.numberOfFlags)[f][1]==nodeMove) {
									flagsOfInterest.put(f, taxicabDistance(nodeX, nodeY, flagPos[f][0], flagPos[f][1] ));
						}							
					}					
			}		
	}
	
	int ghostsInThisDirection=ghostsOfInterest.size(), flagsInThisDirection=flagsOfInterest.size();	
	int  nearestFlagToPacman=-1;
	
	
	if (!flagsOfInterest.isEmpty()) {
			nearestFlagToPacman=PacmanUtilities.numberOfColumns+PacmanUtilities.numberOfRows+1;
			for (f=0; f<PacmanUtilities.numberOfFlags; f++) {			
				if (flagsOfInterest.containsKey(f)) {				
						if (nearestFlagToPacman > flagsOfInterest.get(f)) {
						nearestFlagToPacman= flagsOfInterest.get(f);					
						}}}}
	
	int[][] stepsGhostToFlag= new int[PacmanUtilities.numberOfGhosts][PacmanUtilities.numberOfFlags];
	int[] nearestGhostToFlag=new int[PacmanUtilities.numberOfFlags];
	for ( g=0; g < PacmanUtilities.numberOfGhosts; g++) {	
			for ( f=0; f < PacmanUtilities.numberOfFlags; f++) {
					if (currentFlagStatus[f]) {						
							stepsGhostToFlag[g][f]=-1;							
					}
					else {stepsGhostToFlag[g][f]= taxicabDistance(flagPos[f][0], flagPos[f][1], currentGhostPos[g][0], currentGhostPos[g][1]);					
					}	
			}			
	}
	if (!flagsOfInterest.isEmpty()) {
			for (f=0; f < PacmanUtilities.numberOfFlags; f++) {
					nearestGhostToFlag[f]=-1; 
					if (flagsOfInterest.containsKey(f)) {						
						nearestGhostToFlag[f]=stepsGhostToFlag[0][f];							
							for(g=0; g<PacmanUtilities.numberOfGhosts; g++) {								
									if (stepsGhostToFlag[g][f]<nearestGhostToFlag[f]) {										
										nearestGhostToFlag[f]=stepsGhostToFlag[g][f];											
									}									
							}
							if(flagsOfInterest.get(f)<nearestGhostToFlag[f]) {																			
											captureableFlags++;										
												stepsAhead+= nearestGhostToFlag[f]-flagsOfInterest.get(f);										
							}								
					}
			}			
	}

	double w1, w2, w3, w4,w5, w6;
	
	w1=+25;
	w2=-25;
	w3=+50;
	w4=+25;
	
	if (nearestFlagToPacman<=1) {
			w5=100;	
	}else {
			w5=0;
	}	
	evaluation+= w1*flagsInThisDirection+w2*ghostsInThisDirection+
					  	w3*captureableFlags+w4*stepsAhead+w5;
	
	if(evaluation>0) {
		w6=5;
	}else {
		w6=0;
	}
	evaluation+=w6*centerized;
	if (flagsInThisDirection==0) {
		evaluation-=100;
	}
	
	if (captureableFlags==0) {
		evaluation-=100;
		for (f=0; f<PacmanUtilities.numberOfFlags; f++) {
			if (!currentFlagStatus[f]) {
				if (relativePos(flagPos, PacmanUtilities.numberOfFlags)[f][0]==-1) {
					for(g=0; g<PacmanUtilities.numberOfGhosts; g++) {
						if (relativePos(currentGhostPos, PacmanUtilities.numberOfGhosts)[g][0]!=-1) {
							if(nodeMove==relativePos(flagPos, PacmanUtilities.numberOfFlags)[f][1]) {
									evaluation =+100;}}}}}}}
return evaluation;}




////////// METHODS USED ///////////////
  
  public double getEvaluate() {
	  return evaluate();
  }

///////// FIND TEMP POS //////////////
public int[] findTempPos(int x, int y, int d) { 
	int[] nextMoveD= new int[2];	
	nextMoveD[0]=x;
	nextMoveD[1]=y;
	switch(d) {	
			case 0: nextMoveD[1]--;			
						break;						
			case 1: nextMoveD[0]++;			
						break;						
			case 2:nextMoveD[1]++;			
						break;						
			case 3: nextMoveD[0]--;			
						break;		
	}	
	return nextMoveD;}

///////////// FIND DIST TO EDGE //////////////////////
public int findDistToEdge() {
		int xOfWall, yOfWall;
		if (nodeX >= (int) (PacmanUtilities.numberOfRows/2)) {
				xOfWall =PacmanUtilities.numberOfRows -1;				
		}else {	xOfWall=0;				
		}
		if (nodeY >= (int) PacmanUtilities.numberOfColumns/2) {
				yOfWall=PacmanUtilities.numberOfColumns -1; 
		}else {yOfWall=0;				
		}
		
		int x=findTempPos(nodeX,nodeY,nodeMove)[0];
		int y=findTempPos(nodeX,nodeY,nodeMove)[1];
		return taxicabDistance(x, y, xOfWall,yOfWall);		
}

///////////// DISTANCE ///////////////

public int distance(int a,int b) {	
	return Math.abs(a-b);}

////////////// TAXI CUB DISTANCE //////////////////

public int taxicabDistance(int x1, int y1, int x2, int y2) {
	
	return distance(x1, x2)+distance(y1,y2);
	
}

//////////////// RELATIVE POS /////////////////

public int[][] relativePos(int[][] pos, int rows){
		int r;
		int[][] relPos= new int[rows][2];		
		for (r=0; r<rows; r++) {			
				if (pos[r][0]<nodeX) {					
						relPos[r][0]=Room.NORTH;						
				}else if(pos[r][0]==nodeX){					
					relPos[r][0]=-1;						
				}else {					
					relPos[r][0]=Room.SOUTH;					
				}				
				if (pos[r][1]<nodeY){					
						relPos[r][1]=Room.WEST;						
				}else if(pos[r][1]==nodeY){						
						relPos[r][1]=-1;						
				}else {					
						relPos[r][1]=Room.EAST;
						}			
		}		
		return relPos;		
}


//////////////// ALL ALLIGNED //////////////

public boolean allAlligned() {

int ctr=0;
for (int g=0; g<PacmanUtilities.numberOfGhosts; g++) {
	if (currentGhostPos[0][1]==currentGhostPos[g][1]) {
		ctr++;
	}
}
return (ctr==PacmanUtilities.numberOfGhosts);
}

}