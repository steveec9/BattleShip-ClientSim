package battleshipclientsim;

import java.util.ArrayList;
import java.util.Random;

/*
 * Board() handles all aspects of creating a random board 
 * the boardt is a 2d array of Objects, and the objects are integers and strings
 * integers take the place of what ship resides where
 * empty strings take the place of an empty spot
 */

public class Board {
	
	private int board [][] ;//your board
	private ArrayList<Object>[][] list;
	private Random rand;
	private Object[][] object; //what Board returns
	
	
	public Board(){
		board = new int[10][10]; //easier to work with numbers first
		object = new Object[10][10]; //then convert to Object{}{}
		rand = new Random(); //creat random number generator object
		//place boats in order
		setBoat(5); //aircraft carrier
		setBoat(4); //battleship
		setBoat(3); //submarine
		setBoat(3); //destroyer
		setBoat(2); //patrol boat
		
		//This convertes int board[][] into Object objest[] which is what is sent to server
		//for all empty places int 0, turn into empty string
		//else add integer wrapper object if there is a boat there
		for(int i=0;i<10;i++){
			for(int j = 0; j<10; j++){
				if(board[i][j]==0)
					object[i][j]= new String("");
				else object[i][j] = new Integer(board[i][j]);
				
			}
		}

	}
	
	
	public void setBoat(int bn){
		int row=-1, col=-1;
		int direction=-1;
		//0 horizontal
		//1 vertical
		
		do{
		direction = rand.nextInt(2); //pick random direction
	
		if(direction == 0){ //if direction is horizontal
			do{
			row = rand.nextInt(10); //pick random row
			col = rand.nextInt(10-bn); //pick random col so that boat will be within board area
			}while(board[row][col]!=0); //pick a spot until valid
		}
		
		else if(direction == 1){ //if direction is vertical
			do{
				row = rand.nextInt(10-bn); //random row so that boat will fit in board
				col = rand.nextInt(10);
				} while(board[row][col]!=0); //peform until valid
		}
		
		else System.out.println("Error Direction"); //this never happens but just in jace
		
		}while(!isCheckDirection(row, col, bn, direction)); //double check that boat is properly placed

		setGrid(bn, row, col, direction); //set grid numbers accordingly
	}
		
		
		
/*
 * isCheckDirection() returns true if boat fits properly, false otherwise
 * @param x : x coordinate
 * @param y : y coordiante
 * @param bn : boat number / boat size
 * @param direction: direction of boat is horizontal = 0 and vertical = 1
 */
	
	public boolean isCheckDirection(int x, int y, int bn, int direction){

		boolean check = true; //initlialize check
		
		//check horizonatal direction;
		if(direction == 0){
			for(int i=0; i<bn; i++ ){
				if(board[x][y+i]!=0)
					check = false;
			}
		}
		
		//check vertical direction
		if(direction == 1){
			for(int i=0; i<bn; i++ ){
				if(board[x+i][y]!=0)
					check = false;
			}
		}
		
		return check;
		
		
	}
	
	/*setGrid() sets the boat number inside board[][]
	 * @param bn : boat number / size of boat
	 * @param x : x coordinate
	 * @param y : y coordinate
	 * @param dir : direction of boat 0 if horizontal, 1 if vertical
	 */
	public void setGrid(int bn, int x, int y, int dir){
		if(dir==0){
			for(int i = 0; i<bn; i++){
				 board[x][y+i]=bn;
			}
		}
		else if(dir==1){
			for(int i = 0; i<bn; i++){
				 board[x+i][y]=bn;
			}
		}
		else System.out.println("SetGrid Error");
	}

	/*
	 * printBoard()
	 * prints the board in the console for debugging purposes
	 */
	public void printBoard(){
	for(int i = 0; i<10;i++){
		for(int j = 0; j< 10 ; j++){
			System.out.print(board[i][j]+ ",");
		}
		System.out.println("");
	 }
	}
	
	//returns board to main program
	public Object[][] getBoard(){
		return object;
	}
	
	

}


