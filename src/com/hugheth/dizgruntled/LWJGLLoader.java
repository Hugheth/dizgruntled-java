package com.hugheth.dizgruntled;

import java.io.File;
import java.lang.reflect.Field;

public class LWJGLLoader {
	public static void init() {
	String s = File.separator;
	System.out.println("hi");
	// Modify this to point to the location of the native libraries.
	String newLibPath = System.getProperty("user.dir") + s + "lib";
	System.setProperty("java.library.path", newLibPath);
	
		Field fieldSysPath = null;
		try {
		    fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
		} catch (SecurityException e) {
		    e.printStackTrace();
		} catch (NoSuchFieldException e) {
		    e.printStackTrace();
		}
	
		if (fieldSysPath != null) {
		    try {
			fieldSysPath.setAccessible(true);
			fieldSysPath.set(System.class.getClassLoader(), null);
		    } catch (IllegalArgumentException e) {
			e.printStackTrace();
		    } catch (IllegalAccessException e) {
			e.printStackTrace();
		    }
		}
}
		
}
