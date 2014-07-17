package battleshipclientsim;

import java.util.*;

/*
 * Shot() holds the all the methods pertaining to calculating the next shot
 * There are two modes: Hunt Mode and Target Mode
 * Hunt Mode: random guessing of all black squares in checkerboard pattern
 * Target Mode: if a Hunt Mode hits something, completely surround target until all ships are destroyed
 */
public class Shot {
	
	private int shotBoard[];
	private Random rand;
	private final String alphabet = "ABCDEFGHIJ"; //for encoding shot
	private final int N = alphabet.length();
	private List<Integer> targetList; //all possible targets
	private List<Integer> huntList; //for hunt mode, only picks parity squares
	private Stack<Integer> list; //for Target mode which is different than targetList
	
	Shot(){
		rand = new Random();
		targetList = new ArrayList<Integer>();
		huntList = new ArrayList<Integer>();
		createTargetList(targetList, huntList); //create huntList and targetList
		list = new Stack<Integer>(); //create Target Mode list
		
	}
	
	
	/*
	 * createTargetList creates targetList and huntList which are integer lists
	 * @ param tL <--> targetList is all numbers 0-99 inclusive
	 * @param hL <--> huntList which only takes parity squares which are essentially only black/red squares in a checkerboard pattern
	 */
	public void createTargetList(List<Integer> tL, List<Integer> hL){
		//list of all numbers 0-99
		for(int i = 0; i < 100; i++){
			tL.add(i);
		}
		
		//pick only red/black squares in checkerboard pattern and add to list
		for(int j = 1; j<100; j+=2){
			if(Math.floor(j/10)%2 == 0){
				hL.add(j);
			}
			else if(Math.floor(j/10)%2 == 1){
				hL.add(j-1);
			}
		}
		//System.out.println(huntList);
		
	}
	
	/*
	 * hunt() only occurs at the start of game and when no ship has been located 
	 * it will pick random targets from the huntList
	 * it then will remove them from the huntlist and the targetList becuase they are out of play
	 * returns target as String
	 */
	public String hunt(){
		String target = "";
		int temp;
		int row=0;
		int col=0;
		int index = 0;
		list.empty(); //empty Target Mode stack because we are now in Hunt Mode
		
		//check to make sure huntList still has valid targets
		if(!huntList.isEmpty()){
		index = rand.nextInt(huntList.size()); //pick random target from huntList
		temp = huntList.get(index); //assign to temp
		
	//these following two lines of code are for encoding integers to battleship coordinates ("A1", "B2")
		
		row = (int) Math.floor(temp/10); //get 10^1 digit
		col = temp%10; //get 10^0 digit
		target = Character.toString(alphabet.charAt(col)) + Integer.toString(row+1); //coloumns are letters, rows are integers which are not 0 indexed
		
		//System.out.println("Target = " + target);
		
		//Remove target from both lists because it is no longe valid
		huntList.remove(new Integer(temp));
		targetList.remove(new Integer(temp));
		}
		//if huntList is empty, pick a target from targetList instead which holds remaining valid targets
		else if(huntList.isEmpty()){
			//same process as above
			index = rand.nextInt(targetList.size());
			temp = targetList.get(index);
			targetList.remove((Object) temp);
			row = (int) Math.floor(temp/10);
			col = temp%10;
			target = Character.toString(alphabet.charAt(col)) + Integer.toString(row+1);
		}

		return target;
	}
	
	
	/*
	 * target() : this is called when a ship has been hit in Hunt Mode
	 * it will create a stack of all surrounding targets around the previous hit provided they are valid still
	 * it is called from main program while the stack still has valid targets or has completely surrounded a sunk ship
	 * 
	 * @param Object shot : the previous shot
	 * @ boolean hit : true if the previous shot was hit, false otherwise
	 */
	public Object target(Object shot, boolean hit){
		Object target;
		String temp;
		int tempTarget;
		char tempCol;
		char tempRow;
		int row;
		int col;
		int startTarg;
		
		//if the previous shot was a hit, add surround targets to stack
		
		if(hit == true){
		
			//the following lines decode a shot from "A1" to an integer
			temp = shot.toString();
			tempRow = temp.charAt(1);
			row = 10*(Character.getNumericValue(tempRow)-1);
			tempCol = temp.charAt(0);
			col = alphabet.indexOf(tempCol);
			startTarg = row + col; //startTarget holds the integer value of the previous hit
			
			//calculate numbers of surrounding targets
			int north = startTarg-10;
			int south = startTarg+10;
			int east = startTarg+1;
			int west = startTarg-1;
			
			//if these targets are valid (still in targetList) are not not in the stack already then add to stack
			if(targetList.contains(new Integer(north))==true && list.contains(new Integer(north)) == false){
				list.add(north);
			}
			
			if(targetList.contains(new Integer(south))==true && list.contains(new Integer(south)) == false){
				list.add(south);
			}
			if(targetList.contains(new Integer(west)) == true && list.contains(new Integer(west)) == false){
				list.add(west);
			}
			if(targetList.contains(new Integer(east)) == true&& list.contains(new Integer(east)) == false){
				list.add(east);
			}
		}
		//sometimes stack is empty when TargetMode is called,
		//this happens when a few ships are close together and are completely encircled,
		//and the previous hit sunk the last shit in the enclosed area.
		//therefore there are no remaining valid targets in the area,
		//thus Target Mode has ran successfully and Hunt Mode should resume
		if(list.isEmpty()){
			target = hunt(); //Hunt Mode 
			
		}else{
		//get next item on stack remove from huntList and targetList	
		tempTarget = list.pop();
		targetList.remove((Object) tempTarget);
		if(huntList.contains((Object) tempTarget)){
			huntList.remove((Object) tempTarget);
		}
		//encode battleship coordinates
		row = (int) Math.floor(tempTarget/10);
		col = tempTarget%10;
		target = Character.toString(alphabet.charAt(col)) + Integer.toString(row+1);
		}
		
		return target;
	}
	
	//returns true if Target Mode stack is empty
	public boolean isEmpty(){
		return list.isEmpty();
	}
}
