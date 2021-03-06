package com.jmcloud.compute.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

public class SysUtils {

	public static URL getResourceURL(String pathInClassPath) {
		return ClassLoader.getSystemResource(pathInClassPath);
	}

	public static URI getResourceURI(String pathInClassPath) {
		try {
			return getResourceURL(pathInClassPath).toURI();
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Properties getProperties(String pathInClassPath) {
		Properties properties = new Properties();
		try {
			InputStream is = ClassLoader
					.getSystemResourceAsStream(pathInClassPath);
			properties.load(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
			return properties;
		}
		return properties;
	}

	public static Properties getProperties(URI uri) {
		Properties properties = new Properties();
		try {
			BufferedReader reader = Files.newBufferedReader(Paths.get(uri),
					Charset.forName("UTF-8"));
			properties.load(reader);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			return properties;
		}
		return properties;
	}

	public static boolean saveProperties(Properties inProperties,
			Path saveFilePath, String comment) {
		try {
			File saveFile = saveFilePath.toFile();
			if (!saveFile.exists()) {
				saveFile.getParentFile().mkdirs();
				saveFile.createNewFile();
			}
			BufferedWriter writer = Files.newBufferedWriter(saveFilePath,
					Charset.forName("UTF-8"),
					StandardOpenOption.TRUNCATE_EXISTING);
			inProperties.store(writer, comment);
			writer.close();
			return true;
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}

	}

	public static void sleep(int sec) {
		try {
			Thread.sleep(sec * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static final String CYGWIN_ROOT_PATH = "/cygdrive/";

	public static String convertIntoCygwinPath(String windowsPath) {
		String cygwinPath = windowsPath.replace("\\\\", "/").replace("\\", "/");
		if (cygwinPath.contains(":")) {
			String[] tempStringArray = cygwinPath.split(":", 2);
			cygwinPath = tempStringArray[0].toLowerCase() + tempStringArray[1];
		}
		return CYGWIN_ROOT_PATH + cygwinPath;
	}

}
