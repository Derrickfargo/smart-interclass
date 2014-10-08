package cn.com.incito.server.utils;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.Map;

public class LogoUtils {
	private static LogoUtils instance;
	private Map<String, String> logos;
	private Map<String, String> logos24;

	public static LogoUtils getInstance() {
		if (instance == null) {
			instance = new LogoUtils();
		}
		return instance;
	}
	
	public Map<String, String> getLogos24() {
		return logos24;
	}

	public String getLogo24(String name) {
		return logos24.get(name);
	}
	
	public Map<String, String> getLogos() {
		return logos;
	}

	public String getLogo(String name) {
		return logos.get(name);
	}

	private LogoUtils() {
		logos = getTeamLogos();
		logos24 = getTeamLogos24();
	}

	private Map<String, String> getTeamLogos() {
		logos = new HashMap<String, String>();
		File file = new File("images/logo/");
		File[] files = file.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if (pathname.getAbsolutePath().endsWith(".png")) {
					return true;
				}
				return false;
			}
		});
		for (File aFile : files) {
			String fileName = aFile.getName();
			logos.put(fileName.replace(".png", ""), "images/logo/" + fileName);
		}
		return logos;
	}
	
	private Map<String, String> getTeamLogos24() {
		logos24 = new HashMap<String, String>();
		File file = new File("images/logo/24/");
		File[] files = file.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if (pathname.getAbsolutePath().endsWith(".png")) {
					return true;
				}
				return false;
			}
		});
		for (File aFile : files) {
			String fileName = aFile.getName();
			logos24.put(fileName.replace(".png", ""), "images/logo/24/" + fileName);
		}
		return logos24;
	}
}
