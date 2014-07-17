package battleshipclientsim;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class HttpClientGet {

	
	private String user;
	private Object gameID;
	private String url;
	private Object gameStatus; //possible values "playing" "won" or "lost"
	private Object myTurn; //possible values "true" or "false"
	
	
	/* constructor
	 * 
	 * @param user : username
	 * @param gameID : game ID #
	 * @param url : GET url i.e. localhost:3000/games/status
	 */
	public HttpClientGet(String user, Object gameID, String url){
		this.user = user;
		this.gameID = gameID;
		this.url = url;
	}
	
	
	/*StatusRequest() is a HTTP Get request
	 * takes not parameters
	 * will store JSON object that holds game status i.e. whose turn it is and if somebody won/lost or still playing
	 */
	public void StatusRequest(){
		
			HttpClient c = new DefaultHttpClient();
			HttpGet g = new HttpGet();
			try{
				
			//construct JSON paramters for a GET request	
			URIBuilder builder = new URIBuilder(url)
            .addParameter("user", user)
            .addParameter("game_id", gameID.toString());
			
			URI uri = builder.build();
			g = new HttpGet(uri); //add parameters to HttpGet object
			
			HttpResponse r = c.execute(g); //execute GET
			//read respone from server
			BufferedReader br = new BufferedReader(new InputStreamReader(r.getEntity().getContent()));
			String line = "";
			while ((line = br.readLine()) != null) {
				 JSONParser j = new JSONParser();
	             JSONObject o = (JSONObject)j.parse(line);
	             myTurn = o.get("my_turn");
	             gameStatus = o.get("game_status");
			     //content = content + line;
			}
			//System.out.println(content);
		}
		catch(URISyntaxException e) {
            System.out.println(e);
        }
        catch(IOException e) {
            System.out.println(e);
        } 
		catch(ParseException e){
			System.out.println(e);
		}finally{
        	g.releaseConnection();
        }
	}
	
	//accessors
	
	public Object getGameStatus(){
		return gameStatus;
	}
	
	public Object getMyTurn(){
		return myTurn;
	}
}
