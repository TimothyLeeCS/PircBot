import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import org.jibble.pircbot.PircBot;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MyBot extends PircBot {
	public MyBot() {
		this.setName("TimLpircbcot");
	}
	
	public void onMessage(String channel, String sender, String login, String hostname, String message) {
		if (message.contains("weather")) {
			String location;
			String[] words = message.split(" ");
			location = words[1];
			String start = ("http://api.openweathermap.org/data/2.5/weather?zip="); 
			String end = (",us&APPID=5b968a27788091cd8ef3d7703e8b5b85");
			String sURL = (start + location + end);
			try {
				URL url = new URL(sURL.toString());
				HttpURLConnection con = (HttpURLConnection)url.openConnection();
				con.setRequestMethod("GET");
				con.connect();
				int responsecode = con.getResponseCode();
				if(responsecode != 200) {
					throw new RuntimeException("HttpResponseCode: " +responsecode);
				}
				else {
					Scanner sc = new Scanner(url.openStream());
					String inline = "";
					while (sc.hasNext()) {
						inline += sc.nextLine();
					}
					sc.close();
					JSONParser parse = new JSONParser();
	                JSONObject jobj = (JSONObject) parse.parse(inline);
	                JSONObject mainObj = (JSONObject) jobj.get("main");
	                double temp = (double) mainObj.get("temp");
	                double temp_h = (double) mainObj.get("temp_max");
	                double temp_l = (double) mainObj.get("temp_min");
	                temp -= 273;
	                temp_h -= 273;
	                temp_l -= 273;
	                String tempout = Double.toString(temp);
	                String temphout = Double.toString(temp_h);
	                String templout = Double.toString(temp_l);
					sendMessage(channel, "The weather's going to be " + temp + " with a high of " + temphout + " and a low of " +templout);
				}		
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (message.contains("currency")) {
			String[] words = message.split(" ");
			double temp_money = Double.parseDouble(words[1]);
			String a_country = words[2];
			String b_country = words[3];
			try {
				URL url = new URL("https://api.exchangeratesapi.io/latest");
				HttpURLConnection con = (HttpURLConnection)url.openConnection();
				con.setRequestMethod("GET");
				con.connect();
				int responsecode = con.getResponseCode();
				if(responsecode != 200) {
					throw new RuntimeException("HttpResponseCode: " + responsecode);
				}
				else {
					Scanner sc = new Scanner(url.openStream());
					String inline = "";
					while (sc.hasNext()) {
						inline += sc.nextLine();
					}
					sc.close();
					JSONParser parse = new JSONParser();
					JSONObject jobj = (JSONObject) parse.parse(inline);
					JSONObject ratesOBJ = (JSONObject) jobj.get("rates");
					double countrya = (double) ratesOBJ.get(a_country);
					double countryb = (double) ratesOBJ.get(b_country);
					double convert = temp_money * (countryb / countrya);
					String outmoney = Double.toString(temp_money);
					String output = Double.toString(convert);
					sendMessage(channel, outmoney + " " + a_country + " is " + output + " " + b_country);
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

/* list of possible currency exchanges
CAD
HKD
ISK
PHP
DKK
HUF
CZK
AUD
RON
SEK
IDR
INR
BRL
RUB
HRK
JPY
THB
CHF
SGD
PLN
BGN
TRY
CNY
NOK
NZD
ZAR
USD
MXN
ILS
GBP
KRW
MYR
*/