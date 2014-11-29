package RemoteDatabaseServer;

import java.util.ArrayList;

public class Authentication {
	
	
	
	
	public ArrayList authenticationResult(String authentication){
		ArrayList authenticationList=convertStringToArrayList(authentication);		
		return getAuthenticationResult(authenticationList.get(0).toString(),authenticationList.get(1).toString());		
	}
	
	private ArrayList getAuthenticationResult(String username, String password){
		ArrayList loginList = new ArrayList();
		System.out.println("userName: " + username);
		System.out.println("password: " + password);
		if(username.equals("username") && password.equals("password")){
			System.out.println("authenticationIsCorrect("+username+", "+password+") == return true;");
			loginList.add("870724-0923");
			loginList.add(0);
			
			return loginList;
		}else{
			System.out.println("authenticationIsCorrect("+username+", "+password+") == return false;");
			loginList.add("FAILD");
			return loginList;
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

	private String convertArrayListToString(ArrayList arrayList) {
		String stringList = "";
		int lastSeparator = -1;
		for (int i = 0; i < arrayList.size(); i++) {
			stringList = stringList + arrayList.get(i) + "_";
		}
		return stringList;
	}
	
}
