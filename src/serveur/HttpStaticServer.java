package serveur;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class HttpStaticServer {
	

	private ServerSocket serverSocket;
	private Socket clientSocket;
	private Map<String, String> domainsMap;
	
	private final static int port = 8181;
	
	
	public HttpStaticServer()
	{
		try {
			serverSocket = new ServerSocket(port);
			IniFile ini = new IniFile("src/config.ini");
			domainsMap = ini.getMap();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	public void run()
	{
		
		while(true)
		{
			Socket s;
			List<String> socketData = new ArrayList<String>();
			try {
				s = serverSocket.accept();
				System.out.println("connecte");
				BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
				String inputLine;
				while (!(inputLine = in.readLine()).equals(""))
				{
					socketData.add(inputLine);
				    System.out.println(inputLine);    
				}
				in.close();
				String getValue = "";
				String hostValue = "";
				parseData(socketData,getValue,hostValue);
				String Path = domainsMap.get(hostValue);
				if(!getValue.equals(""))
				{
					File f = new File(getValue);
					 ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
					 oos.writeObject(f);
					 oos.flush();
					 oos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
			
	}
	
	private void parseData(List<String> dataList,String getval,String hostVal)
	{
		for (String line : dataList) {
			String upperLine = line.toUpperCase();
			if(upperLine.startsWith("GET"))
			{
				getval = line.split(" ")[1];
			}
			if(upperLine.startsWith("HOST"))
			{
				hostVal = line.split(" ")[1]
						.split(":")[0];
			}
		}
	}
	
	
}
	
	
	
