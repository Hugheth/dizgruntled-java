package com.hugheth.dizgruntled.tools;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class URLReader {
	
	public static void saveResult(String text, String name, String hash) throws Exception {
		
		URL result = new URL("http://gruntz2.hugheth.com/server/alpha2.php?name=" + name + "&hash=" + hash + "&text=" + text);
		
		BufferedReader in = new BufferedReader(
				new InputStreamReader(
						result.openStream()));

		String inputLine;

		while ((inputLine = in.readLine()) != null)
			System.out.println(inputLine);

		in.close();
	}
}