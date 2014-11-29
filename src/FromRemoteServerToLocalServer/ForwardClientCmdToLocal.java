package FromRemoteServerToLocalServer;



import javax.net.ssl.SSLSocket;

import Queue.ACQueue;
import Client.Client;
import Server.Server;


public class ForwardClientCmdToLocal {
	Client cl;
	Queue.ACQueue ac;
	private SSLSocket socket;
	private Server send;
	public ForwardClientCmdToLocal(String ip){//
		cl=new Client(ip,7238,"/home/v4h4/git/keystore.jks","password","_","no_command");
		ac = cl.getClientQueue();
		Thread clientThread = new Thread(cl, "ClientThread");
		clientThread.start();
	}
	
	public void sendCommand(String message){
			cl.setCommand(message);
	}
	
	public String getCommand(){
		int counter=0;
		while(cl.isFinished()!=true){
			try{
				if(counter==5){
					return null;
				}
				counter++;
				Thread.currentThread().sleep(1000);				
			}catch(Exception ex){
				
			}
		}
		cl.setFinished(false);
		//String clReply
		//skriv ut och returnera
		return cl.getReply();
		
		/*if(cl.getFinished()==true){
		System.out.println("cl.getReply() == "+cl.getReply());
			cl.setFinished(false);
			return cl.getReply();
		}
		return null;*/
	}
	
}
