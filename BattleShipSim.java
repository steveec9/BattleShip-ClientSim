package battleshipclientsim;

import java.util.concurrent.TimeUnit;

/*
 * BattleShipSim performs the main actions necessary for two clients to compete
 * I have set this up to show how my Hunt/Target AI w/ parity will always beats a randomly guessing AI
 * This game is located on your local machine after you start jmulieri/battleship server on GitHub
 */
public class BattleShipSim {

	public static void main(String[] args) {

		Object gameStatus1, gameStatus2; //gameStatus keeps track of whose turn it is and who win/loss
		Object myTurn1, myTurn2; //keeps track of whose turn it is : true or false
		Board board1 = new Board(); //generate battleship boards
		Board board2 = new Board();
		Shot shot1 = new Shot(); //calcultes next shot
		Shot shot2 = new Shot();
		
		Object[][] sea1 = board1.getBoard(); //retrieve boards
		Object[][] sea2 = board2.getBoard();
	
		//Join game for both clients
		HttpClientPost post1 = new HttpClientPost("Hunt/Target w/ Parity", sea1, "http://0.0.0.0:3000/games/join");
		post1.JoinRequest();
		HttpClientPost post2 = new HttpClientPost("Random Guessing", sea2, "http://0.0.0.0:3000/games/join");
		post2.JoinRequest();
		
		
		
		HttpClientGet get1 = new HttpClientGet("Hunt/Target w/ Parity",post1.getGameID(), "http://0.0.0.0:3000/games/status");
		HttpClientGet get2 = new HttpClientGet("Random Guessing",post2.getGameID(), "http://0.0.0.0:3000/games/status");
		
		//initialize some flags 
		boolean targetOn1 = false;
		Object target1 = "A1";
		
		//insert delay before game begins
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		do{
			//insert small delay between turns
			try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//******************** Advanced Hunt/Target AI	**********************
		get1.StatusRequest();	//request Player 1 status	
		gameStatus1 = get1.getGameStatus(); //get status whether player 1 is playing/won/lost
		myTurn1 = get1.getMyTurn(); //returns String "true" if it is player 1 turn

		/*
		 * Flow of play: 
		 * 1. check if it is player's turn
		 * 2. check if in Target Mode else Hunt Mode
		 * 3. always begin game in Hunt Mode
		 * 4. Hunt Mode makes random guesses in checkerboard pattern
		 * 5. Target Mode is called when Hunt Mode scores a hit
		 * 6. No longer in Target Mode if Target Mode stack is empty and the previous hit was a miss, which means target was compltely encircled
		 * 7. Play until someone wins/loses 
		 */
		
		if(myTurn1.toString().equals("true")){
			//System.out.println("Player 1 Turn****************************");
	
			if(targetOn1 == true){
				//System.out.println("wasHit??? " + Boolean.valueOf(post1.getHit().toString()));
				target1 = shot1.target(target1, Boolean.valueOf(post1.getHit().toString()));
				post1.FireRequest(target1, post1.getGameID());
				targetOn1 = !(shot1.isEmpty() && !Boolean.valueOf(post1.getHit().toString())); 
			}
			else{
				target1 = shot1.hunt();
				//System.out.println("Target 1 = " + target1);
				post1.FireRequest(target1, post1.getGameID());
				targetOn1 = Boolean.valueOf(post1.getHit().toString());
			}
		}
		
		//************************Dumb Random Guess AI************************
		
		get2.StatusRequest();
		gameStatus2 = get2.getGameStatus();
		myTurn2 = get2.getMyTurn();
		
		if(myTurn2.toString().equals("true")){
			System.out.println("Player 2 Turn****************************");
			Object target2 = shot2.hunt();
			System.out.println("Target 2 = " + target2);
			post2.FireRequest(target2, post2.getGameID());			
		}
		
		
		}while(!gameStatus1.toString().equals("won") && !gameStatus2.toString().equals("lost"));
		System.out.println("Player 1 " + gameStatus1.toString());
		System.out.println("Player 2 "+ gameStatus2.toString());
	}
}
	