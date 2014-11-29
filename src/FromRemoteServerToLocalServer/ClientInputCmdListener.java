package FromRemoteServerToLocalServer;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import DDNS.DDNS;
import GettingLocalIpServer.ServerForGettingIPFromLS;

import Queue.Item;
import Queue.WriteQueue;
import RemoteDatabaseServer.Authentication;
import Server.Server;

public class ClientInputCmdListener implements Runnable{
	private WriteQueue wq;
	private Item its;
	private Server server;
	private Authentication login;
	
	private static AtomicBoolean updated = new AtomicBoolean(true);
	private HashMap<Integer,Item> commandQueue;
	private ForwardClientCmdToLocal remoteClient;
	private ServerForGettingIPFromLS getLocalServerIP;
	private boolean hasConnections=false;
	private DDNS ddns = new DDNS();
	public ClientInputCmdListener(){//DDNS Created by zijie, modified by Zijie and Tarik
		this.its = new Item();
		this.server= new Server(5906, 0, "/home/v4h4/git/keystore.jks", "password", "pwnage12",null);
		Thread connectionListener = new Thread(server,"Server");
		connectionListener.start();
		this.wq = server.getTheQueue();
		//ddns.updateIpWithDDNS();         <--------------
		try{
			Thread.currentThread().sleep(3000);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		login = new Authentication();
		this.getLocalServerIP= new ServerForGettingIPFromLS(this,0,"/home/v4h4/git/keystore.jks", "password", "pwnage12");
		Thread getLocalsIp = new Thread(this.getLocalServerIP,"Get Local Servers IP numnber");
		getLocalsIp.start();
		System.out.println("Object av protokollet är skapade");
	}
	
	public void run(){
		cmdReceiver();
	}
	
	public synchronized void cmdReceiver(){//@authors: created by Dino,Zijie,Tarik 
		try{
			commandQueue=wq.getMap();
			while(true){
				for(int i = 0; i < wq.getMap().size(); i++) {
					if(((Item) wq.getMap().get(i)).isAnswered() == false) {
						checkForUpdates();
						authenticationForUnauthorizedClients(i);
						forwardCmdToLocal(i);
					} 
				}	 
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void checkForUpdates(){
		if(wq.getHasAddedCommands()==true || wq.getMap().size()!=commandQueue.size()){//stop executing and update hasmap and sort it
			commandQueue=wq.getMap();
			wq.setHasAddedCommands(false);
		}
	}
	
	private void authenticationForUnauthorizedClients(int i){
		Item item = commandQueue.get(i);
		if(item.isAnswered()==false && item.getUser()==null){
			System.out.println("START - whole login process: "+0);
			long runningTime=System.currentTimeMillis();
			ArrayList authentication=login.authenticationResult(item.getCmd());
			if(!authentication.get(0).equals("FAILD")){
				item.setUser(authentication.get(0).toString());//login
				item.setUserPrio((int)authentication.get(1));
				System.out.println("2.3  -  logged in");
				item.setPriority(0);
				String command=item.getCmd();
				System.out.println("remoteClient.sendCommand("+command+");");
				remoteClient.sendCommand(command);
				String result=remoteClient.getCommand();
				item.setReply("error_"+result);
				System.out.println("Authentication == OK");
			}				
			runningTime = System.currentTimeMillis()-runningTime;
			System.out.println("END - whole login process: "+runningTime);
			runningTime=0;
		}		
	}
	
	private boolean forwardCmdToLocal(int i){
		if(((Item) commandQueue.get(i)).getCmd()!=null && ((Item) commandQueue.get(i)).getUser()!=null && ((Item) commandQueue.get(i)).isAnswered()==false){//user
			try{
				System.out.println("3.1");
				System.out.println("remoteClient.sendCommand(((Item) commandQueue.get(i)).getMsg());");
				System.out.println("remoteClient.sendCommand("+((Item) commandQueue.get(i)).getCmd()+");");
				remoteClient.sendCommand(((Item) commandQueue.get(i)).getCmd());	
				String replay=remoteClient.getCommand();
				/*Thread.currentThread().sleep(5100);
				*/((Item) wq.getMap().get(i)).setReply(replay);	
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return true;
	}
	
	private ArrayList convertStringToArrayList(String stringList) {
		ArrayList taskList = new ArrayList();
		int lastSeparator = -1;
		for (int i = 0; i < stringList.length(); i++) {
			if (stringList.charAt(i) == '_') {
				taskList.add(stringList.subSequence(lastSeparator + 1, i));
				lastSeparator = i;
			}
		}
		return taskList;
	}
	
	private boolean isMalawareCode(String command){
		String allowed="qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM_!1234567890";
		int counter=0;
		for(int i=0;i<command.length();i++){
			for(int q=0;q<allowed.length();q++){
				if(command.charAt(i)==allowed.charAt(q)){
					counter++;
				}
			}
		}
		if(counter==command.length() && command.contains("!")){
			return false;
		}
		return true;
	}
	public boolean getHasConnections(){
		return this.hasConnections;
	}
	
	public void setHasConnections(boolean hasConnections){
		this.hasConnections=hasConnections;
	}
	
	public void setThis(ForwardClientCmdToLocal remoteClient){
		this.remoteClient=remoteClient;
	}
	
}
















/*
  
 private void authenticationForUnauthorizedClients(int i){
		Item item = commandQueue.get(i);
		if(item.isAnswered()==false && item.getUser()==null){
			System.out.println("START - whole login process: "+0);
			long runningTime=System.currentTimeMillis();
			ArrayList authentication=login.authenticationResult(item.getCmd());
			if(!authentication.get(0).equals("FAILD")){
				for(int p=0;p<commandQueue.size();p++){
					((Item) wq.getMap().get(i)).setUser(authentication.get(0).toString());//login
					((Item) wq.getMap().get(i)).setUserPrio((int)authentication.get(1));
				    System.out.println("2.3  -  logged in");
				    ((Item) wq.getMap().get(i)).setPriority(0);
				    String command=commandQueue.get(i).getCmd();
				    System.out.println("remoteClient.sendCommand("+command+");");
				    remoteClient.sendCommand(command);
				    String result=remoteClient.getCommand();
				    ((Item) wq.getMap().get(i)).setReply("error_"+result);
				    System.out.println("Authentication == OK");
				}				
				
			}
			runningTime = System.currentTimeMillis()-runningTime;
			System.out.println("END - whole login process: "+runningTime);
			runningTime=0;
		}		
	} 
  
  */
