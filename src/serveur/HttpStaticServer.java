package serveur;

import java.io.BufferedReader;
import java.io.File;
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
	private Map<String, String> domainsMap;
	private Map<String,LocalSession> localSessionsMap;
	private String getValue, hostValue;
	private final static int port = 8181;

	public HttpStaticServer() {
		try {
			serverSocket = new ServerSocket(port);
			getValue = "";
			hostValue = "";
			IniFile ini = new IniFile("src/config.ini");
			domainsMap = ini.getMap();
			localSessionsMap = new HashMap<String, LocalSession>();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {

		while (true) {
			Socket s;
			List<String> socketData = new ArrayList<String>();
			try {
				s = serverSocket.accept();
				s.setKeepAlive(true);
				System.out.println("connecte");
				BufferedReader in = new BufferedReader(new InputStreamReader(
						s.getInputStream()));
				String inputLine;
				while (!(inputLine = in.readLine()).equals("")) {
					socketData.add(inputLine);
					System.out.println(inputLine);
				}

				parseData(socketData);
				String path = domainsMap.get(hostValue);
				if (!getValue.equals("")) {
					File fi = new File(path + "\\" +getValue);
					ObjectOutputStream oos = new ObjectOutputStream(
							s.getOutputStream());
					oos.writeObject(fi);
					oos.flush();
					oos.close();
				} else {
					
					String filesString = getFilesNames(hostValue);
					ObjectOutputStream oos = new ObjectOutputStream(
							s.getOutputStream());
					oos.writeObject(filesString);
					oos.flush();
					oos.close();
				}
				s.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	private void parseData(List<String> dataList) {
		for (String line : dataList) {
			String upperLine = line.toUpperCase();

			if (upperLine.startsWith("GET")) {
				getValue = line.split(" ")[1];
			}
			if (upperLine.startsWith("HOST")) {
				hostValue = line.split(" ")[1].split(":")[0];
			}
		}

	}

	private String getFilesNames(String path) {

		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		StringBuilder sbuilder = new StringBuilder();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				System.out.println("File " + listOfFiles[i].getName());
				sbuilder.append("<a href=" +listOfFiles[i].getName() + "</a>");
			} else if (listOfFiles[i].isDirectory()) {
				System.out.println("Directory " + listOfFiles[i].getName());
				sbuilder.append("<a href=" +listOfFiles[i].getName() + "</a>");
			}
		}
		return sbuilder.toString();
	}


}