BattleShip-ClientSim
====================

This is a battleship simulation that uses RESTful API (apache's HttpClient) between two clients that is governed by the Ruby on Rails server here: https://github.com/jmulieri/battleship and on my own github under 'battleship'

One client uses an advanced AI called Hunt/Target with Parity as described here: http://www.datagenetics.com/blog/december32011/
The second client is a dumb AI that uses random guessing.


Description:
1)Board.java creates a random battleship board.
2)Shot.java has functions that calculates the next target. It has functions hunt() and target() 
  that correspond to Hunt Mode and Target mode as described in datagenetics. 
3)HttpClientPost() and HttpClientGet() implement Apache's HttpClient to communicate with the Ruby on Rails server which is located on localhost:3000
4)BattleShipSim() is the main program that handles the interation of the two clients with the server
5)BattleShipSim.jar is a runnable executable. It can be used once the Ruby server is online

To Run:
1)Start Ruby server with the command "rails server" depending on what version of ruby you have
2)Double click on BattleShipSim.jar
3)Go to localhost:3000 on your web browser
4)Select game from under Start Game button, sometimes you have to click the game twice to get it functioning

Troubleshooting:
1) You might need some .jar files for httpcomponents-client which you need to turn into a .jar with Maven
2) You also might need the commons.logging which also needs to be turned into a .jar with Maven
3) You need json-simple-1.1.1.jar for JSON Objects
4) You need gson.jar
These are all in the Libraries.jar folder
