package DDNS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class DDNS {
	
	public void updateIpWithDDNS(){
		submitIpUpdate(getMyCurrentIp(), "smarthousehkr.ddns.net");
	}
	
	public String reciveIpFromDDNS(){
		String ip="";
		InetSocketAddress addr = new InetSocketAddress("smarthousehkr.ddns.net", 8080);
		ip = getIpOnly(addr.getAddress().toString());
		return ip;
	}
	
	
	private String getMyCurrentIp(){
		 String ip = "null";
	        String host = "http://bot.whatismyipaddress.com";
	        try {
	            URL url = new URL(host);
	            HttpURLConnection http = (HttpURLConnection) url.openConnection();
	            http.setRequestProperty("User-Agent","Java NoIP Updated 1.0");    
	            BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream()));
	            ip = br.readLine();
	            br.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return ip;
	}
	
	private boolean submitIpUpdate(String ip,String hostname){
        String host = "http://dynupdate.no-ip.com/nic/update?hostname="+hostname+"&myip="+ip;
        String username="jamesf0406@gmail.com";
        String password="123456";
        try {
            URL url = new URL(host);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            String passwordAndUsername=username+":"+password;
            String auth = Base64.encode(passwordAndUsername.getBytes());
            http.setRequestProperty("Authorization","Basic "+auth);    
            http.setRequestProperty("User-Agent","Java NoIP Updated 1.0");    
            BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream()));
            String line;
            while((line = br.readLine()) != null){
                if(line.startsWith("good")){
                    System.out.println("IP Update Successful: "+line.substring(5));
                    return true;
                } else if(line.startsWith("nochg")){
                    return true;
                } else if(line.startsWith("badauth")){
                    System.out.println("Invalid Login Details");
                } else if(line.startsWith("badagent")){
                    System.out.println("Bad user agent supplied");
                } else if(line.startsWith("abuse")){
                    System.out.println("Account hs been disabled");
                } else if(line.startsWith("911")){
                    System.out.println("Server fallover");
                } else if(line.startsWith("!donator")){
                    System.out.println("You cannot use donator features");
                    return true;
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
 } 
	private String getIpOnly(String domainAndIpNumber) {
		  String ip = "";
		  for (int i = 0; i < domainAndIpNumber.length(); i++) {
			  if (domainAndIpNumber.charAt(i) == '/') {
				  ip = domainAndIpNumber.substring(i+1);
			  }
		  }
		  return ip;
	}
	
	
}

	
