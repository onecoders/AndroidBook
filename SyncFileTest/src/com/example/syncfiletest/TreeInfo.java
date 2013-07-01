package com.example.syncfiletest;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TreeInfo implements Iterable<File> {

	public List<File> files = new ArrayList<File>();
	public List<File> dirs = new ArrayList<File>();

	@Override
	public Iterator<File> iterator() {
		return files.iterator();
	}

	void addAll(TreeInfo other) {
		files.addAll(other.files);
		dirs.addAll(other.dirs);
	}

	public static TreeInfo walk(File start, String regex) {
		return recurseDirs(start, regex);
	}

	public static TreeInfo walk(String start, String regex) {
		return walk(new File(start), regex);
	}

	private static TreeInfo recurseDirs(File startDir, String regex) {
		TreeInfo result = new TreeInfo();
		for (File item : startDir.listFiles()) {
			if (item.isDirectory()) {
				result.dirs.add(item);
				result.addAll(recurseDirs(item, regex));
			} else {
				if (item.getName().matches(regex)) {
					result.files.add(item);
				}
			}
		}
		return result;
	}
}
