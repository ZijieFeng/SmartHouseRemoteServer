package GettingLocalIpServer;

import java.util.ArrayList;
import java.util.HashMap;

import FromRemoteServerToLocalServer.ClientInputCmdListener;
import FromRemoteServerToLocalServer.ForwardClientCmdToLocal;
import Queue.ACQueue;
import Queue.Item;
import Server.Server;
import Queue.WriteQueue;

public class ServerForGettingIPFromLS implements Runnable {
	private ClientInputCmdListener cmdListener;
	private WriteQueue wq;
	private Item its;
	private Server server;
	private String localServerIp;
	private int connectionSelection;
	private String keyStorePath;
	private String keyStorePassword;
	private String connectionPassword;
	private HashMap<Integer,Item> commandQueue;
	public ServerForGettingIPFromLS(ClientInputCmdListener cmdListener,int connectionSelection,String keyStorePath, String connectionPassword, String keyStorePassword){
		System.out.println("\n\nI'm now working in the ServerForGettingIPFromLS\n\n");
		this.cmdListener=cmdListener;
		this.connectionSelection=connectionSelection;
		this.keyStorePath=keyStorePath;
		this.connectionPassword=connectionPassword;
		this.keyStorePassword=keyStorePassword;
		this.its = new Item();
		this.server= new Server(6915, 0, "/home/v4h4/git/keystore.jks", "password", "pwnage12",null);
		Thread connectionListener = new Thread(server,"Server");
		connectionListener.start();
		this.wq = server.getTheQueue();
		commandQueue=wq.getMap();
		
	}
	
	
	public void run(){
		System.out.println("connectionListener() is now working");
		connectionListener();
	}
	
	private void connectionListener(){
		while(true){
			for(int p=0;p<commandQueue.size();p++){
				Item item;
				if((item= commandQueue.get(p)).isAnswered() == false){
					String localIP=convertStringToArrayList(item.getCmd()).get(0).toString();
					System.out.println("\n\nlocalIP == "+localIP+"\n\n\n\n");
					item.setReply("error_Close Connection_");	
					ForwardClientCmdToLocal remoteClient = new ForwardClientCmdToLocal(localIP);
					cmdListener.setThis(remoteClient);
					//remoteClient.sendCommand(localIP+"_connected to RS");
				}
			}
		}
		
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
	
	
}
