package zc.datawash.view.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import zc.datawash.view.MainFrame;

public class ExportWashedFileActionListener implements ActionListener{

	private MainFrame frame;
	
	public ExportWashedFileActionListener(MainFrame frame){
		this.frame = frame;
	}
	
	public void actionPerformed(ActionEvent e) {
		try {
			String[] washedFilesNames = this.frame.getDataWasher().generateWashedFile();
			frame.logWashMessage("成功保存到" + washedFilesNames[0]);
			frame.logWashMessage("成功保存到" + washedFilesNames[1]);
		} catch (Exception e1) {
			frame.logErrorMessage("Sorry, 程序发生内部错误!");
			e1.printStackTrace();
			return;
		}
	}

}
