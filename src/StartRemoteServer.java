import FromRemoteServerToLocalServer.ClientInputCmdListener;





public class StartRemoteServer {

	public static void main(String[] args) {
		//localListener
		
		
		ClientInputCmdListener rs = new ClientInputCmdListener();
		Thread remoteServer = new Thread(rs,"Remote Server");
		remoteServer.start();
		
		
		//clientListener
	}

}
