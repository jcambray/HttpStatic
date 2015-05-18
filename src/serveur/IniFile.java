package serveur;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IniFile {

	private Pattern _section = Pattern.compile("\\s*([^=]*)=(.*)");
	
	private Map<String, String> map = new HashMap();
	private ArrayList<String> arrayList = new ArrayList<String>();
	
	public IniFile(String path) throws IOException {
		load(path);
	}

	public void load(String path) throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			String line;
			while ((line = br.readLine()) != null) {
				Matcher m = _section.matcher(line);
				if (m.matches()) {					
					arrayList.add(m.group(2).trim());
				}
			}
			
			
			int i = 0;
			while(arrayList.size()>i){
				map.put( arrayList.get(i), arrayList.get(i+1) );
				i=i+2;
			}
            
		}
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}
	
	
}