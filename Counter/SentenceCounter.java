package Counter;

import html.ParserGetter;
import html.TagStripper;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

import javax.swing.text.html.HTMLEditorKit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import common.PasteInputException;
import common.SentenceEntry;
import common.fileNotFoundException;

public class SentenceCounter {

	private StringTokenizer wordReader;
	protected ArrayList<String> list;
	protected ArrayList<SentenceEntry> commonWordList;
	private String stream;

	public List<String> sentenceBuilder(String type, String string)
			throws fileNotFoundException, PasteInputException {

		if (type.equals("P")) {

			stream = string;

		}else if(type.equals("U")) {

			Document doc;
			
			try {
				
				
				doc = Jsoup.connect(string).get();
				Elements ps = doc.select("p");
				stream = ps.text();
				
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else if (type.equals("F")) {

			try {

				final String EoL = System.getProperty("line.separator");
				List<String> lines = Files.readAllLines(Paths.get(string),
						Charset.defaultCharset());

				StringBuilder sb = new StringBuilder();
				for (String line : lines) {
					sb.append(line).append(" ");
				}

				stream = sb.toString();

			} catch (IOException e) {

				throw new fileNotFoundException();
			}
		}

		list = new ArrayList<String>();
		int startPoint = 0;

		try {
			
			stream = stream.replaceAll("\n", " ");
			stream = stream.replaceAll("\r", " ");
			stream = stream.replaceAll("\\[", " ").replaceAll("\\]"," ");


//			stream = stream.replace(System.getProperty("line.separator"), " ");
//			stream = stream.replaceAll("\\r|\\n", " ");
//			stream = stream.replaceAll("\\r\\n|\\r|\\n", " ");
//			stream = stream.replaceAll("(\\r|\\n)+", " ");

			
			
			for (int i = 0; i < stream.length(); i++) {

				if (String.valueOf(stream.charAt(i)).equals(".")
						|| String.valueOf(stream.charAt(i)).equals("?")) {

					String temp = (String) stream
							.subSequence(startPoint, i + 1);
															
					if( (stream.length() - i )  <=  2 ){
					
						list.add(temp);

					}
					
					if (String.valueOf(stream.charAt(i + 1)).equals(" ")
							|| String.valueOf(stream.charAt(i + 1)).equals("\"")) {
						
						if (String.valueOf(temp.charAt(0)).equals(" ")) {

							temp = temp.substring(1, temp.length());

						} else if (String.valueOf(temp.charAt(0)).equals(" ")
								&& String.valueOf(temp.charAt(1)).equals(" ")) {

							temp = temp.substring(2, temp.length());

						}

						list.add(temp);

						startPoint = i + 1;
					}
				}
			}

		} catch (StringIndexOutOfBoundsException e1) {

			throw new PasteInputException();

		} catch (NullPointerException e2) {

			throw new PasteInputException();

		}

		return list;
	}

	public List<SentenceEntry> nbrChars(int min, int max, String search) {

		ArrayList<SentenceEntry> returnList = new ArrayList<SentenceEntry>();
		StringBuilder indexSB = new StringBuilder();
		StringTokenizer st;
		Boolean add;

		for (String s : list) {

			add = true;
			
			st = new StringTokenizer(search);

			// Check lenght

			if (!(s.length() >= min && s.length() <= max)) {

				add = false;
			}

			// Check searchwords

			while (st.hasMoreTokens()) {

				String temp = st.nextToken();

				if (!s.contains(temp)) {

					add = false;
				}
			}

			if (add) {
				
				indexSB.append(s + " ");
				returnList.add(new SentenceEntry(s.length(), s));
			}
		}
		indexate(indexSB.toString());
		return returnList;
	}

	public List<SentenceEntry> nbrWords(int min, int max, String search) {

		ArrayList<SentenceEntry> returnList = new ArrayList<SentenceEntry>();
		StringBuilder indexSB = new StringBuilder();
		StringTokenizer st;
		Boolean add;

		for (String s : list) {

			int counter = 0;
			add = true;

			st = new StringTokenizer(search);

			wordReader = new StringTokenizer(s);

			while (wordReader.hasMoreTokens()) {

				wordReader.nextToken();
				counter++;

			}

			// Check lenght

			if (!(counter >= min && counter <= max)) {

				add = false;
			}

			// Check searchwords

			while (st.hasMoreTokens()) {

				String temp = st.nextToken();

				if (!s.contains(temp)) {

					add = false;
				}
			}

			if (add) {
				
				indexSB.append(s + " ");
				returnList.add(new SentenceEntry(counter, s));

			}
			
		}
		
		indexate(indexSB.toString());
		return returnList;

	}

	public List<SentenceEntry> getCommonList() {
		return commonWordList;
	}
	
	private void indexate(String stream){
		
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		StringTokenizer streamTokens = new StringTokenizer(stream);
		commonWordList = new ArrayList<SentenceEntry>();
		
		while(streamTokens.hasMoreTokens()){
			
			String input = streamTokens.nextToken();
							
			if(!map.containsKey(input)){
				
				map.put(input, 1);
				
			}else{
				
				map.put(input, map.get(input) + 1);
			}
		}
		
		Set set = map.entrySet();
		Iterator iterator = set.iterator();
		List<IndexEntry> sortList = new ArrayList<IndexEntry>();
		
		while(iterator.hasNext()){
			
			Entry<String,Integer> entry = (Entry) iterator.next();
			
			int value = entry.getValue();
			String key = entry.getKey();
			
			if(value > 1){
				
				sortList.add(new IndexEntry(key, value));
			}
		}
		Collections.sort(sortList);

		for(IndexEntry ie : sortList){
			
			commonWordList.add(new SentenceEntry(ie.value, ie.key));

		}
		
	}
}
