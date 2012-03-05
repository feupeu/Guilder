package org.dazzlewire.Guilder;

//Java imports
import java.util.HashMap;

public class Rank {
	
	private HashMap<String, Boolean> permissionList = new HashMap<String, Boolean>(); 

	// Setter
	public void setPermission(String permission, boolean truefalse) {
		
		permissionList.put(permission, truefalse);
		
	}
	
	// Getter
	public boolean getPermission(String permission) {
		
		return permissionList.get(permission);
		
	}
	
	
}

