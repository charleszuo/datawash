package zc.datawash.util;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class CsvFileFilter extends FileFilter{

	public boolean accept(File file) {
		return file.getName().toLowerCase().endsWith(".csv");
	}

	public String getDescription() {
		return "CSV File";
	}

}
