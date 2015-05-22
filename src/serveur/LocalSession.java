package serveur;

import java.util.Map;

public class LocalSession {
	
	private Map<String,Object> sessionMap;
	private String id;
	
	public LocalSession(String id)
	{
		this.id = id;
	}
	
	public void setAttribute(String key,Object value)
	{
		sessionMap.put(key,value);
	}
	
	public Object getAttribute(String key)
	{
		return sessionMap.get(key);
	}

}
