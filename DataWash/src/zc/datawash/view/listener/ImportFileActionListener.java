package zc.datawash.view.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import zc.datawash.DataWasher;
import zc.datawash.util.CsvFileFilter;
import zc.datawash.view.MainFrame;

public class ImportFileActionListener implements ActionListener{
	private MainFrame frame;
	
	public ImportFileActionListener(MainFrame frame){
		this.frame = frame;
	}
	
	public void actionPerformed(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser(); 
		FileFilter fileFilter = new CsvFileFilter();
		fileChooser.setFileFilter(fileFilter);
		int i = fileChooser.showOpenDialog(frame.getTopPanel()); 
		if (i == JFileChooser.APPROVE_OPTION) 
		{
			File selectedFile = fileChooser.getSelectedFile(); 
			String csvFilePath = selectedFile.getPath();
			frame.getSelectedFilePathLabel().setText(csvFilePath);
			DataWasher dataWasher = new DataWasher();
			frame.setDataWasher(dataWasher);
			try {
				dataWasher.parseCSVFile(csvFilePath);
				frame.displaySourceRecordCount();
				frame.enableStartWashButton(true);
				frame.cleanWashMessage();
			} catch (Exception e1) {
				frame.logErrorMessage("不支持该问卷格式，请检查问卷中的列是否符合清洗的规范!");
				e1.printStackTrace();
			}
		}
	}
}


