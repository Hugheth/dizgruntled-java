package com.hugheth.dizgruntled.level;

import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import biz.source_code.base64Coder.Base64Coder;

/**
 * The Tile Packer packs and unpacks strings that represent the tiles of a layer.
 * 
 * Deflate compression and BASE64 encoding is used to represent the tiles in a compact way
 * in the map JSON file.
 * 
 * @author hugheth
 * @version alpha-2
 * @since alpha-2
 * 
 * @see com.hugheth.dizgruntled.manager.Map
 */
public class TilePacker {

	public static String Pack(String input) {
		
		Deflater compresser = new Deflater();
		
		// Store bytes
		int length = input.length() / 2;
		byte[] levelByte = new byte[length];
		// Convert to a byte string
		for (int i = 0; i < length; i++) {
			levelByte[i] = (byte) (Integer.parseInt(input.substring(i * 2, i * 2 + 2), 16) - 128);
		}
		byte[] levelOut = new byte[length];
		compresser.setInput(levelByte);
		compresser.finish();
		
		int newLength = compresser.deflate(levelOut);
		
		return String.valueOf(Base64Coder.encode(levelOut, newLength));
	}

	public static String Unpack(String input, int size) {
		
		byte[] decoded = Base64Coder.decode(input);
		
		Inflater inflater = new Inflater();
		
		byte[] levelByte = new byte[size * 2];
		
		// Inflate
		inflater.setInput(decoded);
		try {
			inflater.inflate(levelByte);
		} catch (DataFormatException e) {
			// TODO Unpack error
			e.printStackTrace();
		}
		
		// Output
		String output = "";
		
		// Return as string
		for (byte b: levelByte) {
			String hex = Integer.toHexString(b + 128);
			// Check length
			if (hex.length() == 1)
				hex = "0" + hex;
			// Append to output
			output += hex;
		}
		// Return
		return output.substring(0, size * 3);
	}
	
}
