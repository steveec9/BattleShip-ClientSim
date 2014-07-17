package battleshipclientsim;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.entity.ContentType;
import org.apache.http.HttpResponse;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.google.gson.*;

import java.util.ArrayList;
import java.util.Map;
 
/**
 *
 * @author joe666
 */
public class HttpClientPost {
 
    private String user;
    private Object board;
    private String URL;
    private Object gameID;
    private Object hit;
    private Object shipSunk;
 
 /*constructor for joining
  * @param user : username
  * @param board: the ship arrangements we are submitting to the server
  * @param URL : localhost:3000/games/join
  */
   
    public HttpClientPost(String user, Object[][] board, String URL) {        
        this.URL = URL;
        this.user = user;
        this.board = board;
    }
    
/*
 * JoinRequest uses Apach HttpClient library, json.simple, and Gson (googles json)
 * gson is only used to convert board into json object matrix 
 */
    public void JoinRequest() {
    	
            HttpClient c = new DefaultHttpClient();        
            HttpPost p = new HttpPost(this.URL);

            Gson gson = new Gson(); //convert into JSON object matrix
            JSONObject json = new JSONObject();
            try {
            //JSON parameters
    		String jsonString = gson.toJson(board);
    		json.put("user", user);
    		json.put("board", jsonString);
    		
            p.setEntity( new StringEntity(json.toJSONString(),"UTF8")); //encode
            p.setHeader("Content-type", "application/json"); 
            HttpResponse r = c.execute(p); //execute post to server
 
            //Read response from server
            BufferedReader rd = new BufferedReader(new InputStreamReader(r.getEntity().getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {
               //Parse our JSON response    
               JSONParser j = new JSONParser();
               JSONObject o = (JSONObject)j.parse(line);
               gameID = o.get("game_id"); //get game ID #
//               System.out.println(gameID);
   
            }
        }
        catch(ParseException e) {
            System.out.println(e);
        }
        catch(IOException e) {
            System.out.println(e);
        } finally{
        	p.releaseConnection(); //must release connection for best performance
        }
    }
    
    /*
     * FireRequest posts your firing target to server and receives response if there was a hit/ miss
     * and if there was a sunken ship.
     * 
     * @param Object s : your shot i.e. "A10", "J5", etc.
     * @param Object gID: the game ID # the client is playing on
     */
    public void FireRequest(Object s, Object gID){
    	HttpClient c = new DefaultHttpClient();
    	HttpPost p = new HttpPost("http://0.0.0.0:3000/games/fire"); //url to post firing target
    	
    	JSONObject json = new JSONObject();
    	try{
    		//add JSON parameters
    		json.put("user", user);
    		json.put("game_id", gID);
    		json.put("shot", s);
    		//System.out.println("Fire Request: " + json.toString());
    		
    		p.setEntity(new StringEntity(json.toString(), "UTF8"));
    		p.setHeader("Content-type", "application/json");
    		HttpResponse r = c.execute(p);
    		
    		 BufferedReader rd = new BufferedReader(new InputStreamReader(r.getEntity().getContent()));
             String line = "";
             while ((line = rd.readLine()) != null) {
                //Parse our JSON response    
                JSONParser j = new JSONParser();
                JSONObject o = (JSONObject)j.parse(line);
                hit = o.get("hit"); //save results
                shipSunk = o.get("sunk");
//                System.out.println("Hit? " + hit);
//                System.out.println("ShipSunk? :" + shipSunk);
    
             }
         }
         catch(ParseException e) {
             System.out.println(e);
         }
         catch(IOException e) {
             System.out.println(e);
         } finally{
         	p.releaseConnection();
         }
    }
    
    
    //accessors
    
    //game ID #
    public Object getGameID(){
    	return gameID;
    }
    // what ship did you sink? i.e. 5 ,4 ,3, 3, 2
    public Object getShipSunk(){
    	return shipSunk;
    }
    //was that shot a hit? true or false
    public Object getHit(){
    	return hit;
    }
}