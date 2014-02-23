package zc.datawash.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import zc.datawash.DataWasher;
import zc.datawash.view.listener.ExportWashedFileActionListener;
import zc.datawash.view.listener.ImportFileActionListener;
import zc.datawash.view.listener.StartWashActionListener;

public class MainFrame {
	private final JFrame jframe = new JFrame();
	private final JPanel topPanel = new JPanel();
	private final JPanel centerPanel = new JPanel();
	private final JPanel bottomPanel = new JPanel();
	
	// top panel
	private final JButton importButton = new JButton();
	private final JLabel importLabel = new JLabel();
	private final JLabel selectedFilePathLabel = new JLabel();
	private final JLabel errorMessageLabel = new JLabel();
	
	// center panel
	private final JLabel step1Label = new JLabel();
	private final JButton step1InfoIconButton= new JButton();
	private final JLabel step1IdLabel = new JLabel();
	private final JTextField step1IdTextBox = new JTextField();
	private final JLabel step2Label = new JLabel();
	private final JButton step2InfoIconButton= new JButton();
	private final JLabel step2StartTimeLabel = new JLabel();
	private final JTextField startTimeTextBox = new JTextField();
	private final JLabel step2StartTimeFormatLabel = new JLabel();
	private final JLabel step3Label = new JLabel();
	private final JButton step3InfoIconButton= new JButton();
	private final JLabel step3TimeSpanLabel = new JLabel();
	private final JLabel step3ShortSpanLabel = new JLabel();
	private final JTextField step3ShortSpanTextBox = new JTextField();
	private final JLabel step3ShortSpanMinuteLabel = new JLabel();
	private final JTextField step3LongSpanTextBox = new JTextField();
	private final JLabel step3LongSpanMinuteLabel = new JLabel();
	private final JLabel step3LongSpanLabel = new JLabel();
	private final JLabel step4Label = new JLabel();
	private final JButton step4InfoIconButton= new JButton();
	private final JTextField step4KeyQuestionTextBox = new JTextField();
	private final JLabel step5Label = new JLabel();
	private final JButton step5InfoIconButton= new JButton();
	private final JTextArea step5NotTargetUserTextBox= new JTextArea();
	private final JScrollPane step5NotTargetUserTextBoxScrollPan = new JScrollPane(step5NotTargetUserTextBox);
	private final JLabel step6Label = new JLabel();
	private final JButton step6InfoIconButton= new JButton();
	private final JTextArea step6LogicConflictTextBox = new JTextArea();
	private final JScrollPane step6LogicConflictTextBoxScrollPan = new JScrollPane(step6LogicConflictTextBox);
	
	// bottom panel
	private final JButton startWashButton = new JButton();
	private final JLabel washedLabel = new JLabel();
	private final JButton exportButton = new JButton();
	private final JLabel exportWashedLabel = new JLabel();

	private final String step1Help = "<html>请在文本框中输入“去重依据的题号”（一般以旺旺ID一题为依据，或卖家店铺名称等）<br/><br/>格式要求：<br/><font style=\"color: red\">Q+题目数字序号，如Q 1（中间无空格）</font><br/><br/>注意点：<br/>对于旺旺ID完全一样的记录，只保留填答时间最早的记录；<br/>对于旺旺ID为空的记录，会对完全相同（包括填答时间）记录进行清洗，只保留一条记录。</html>";
	private final String step2Help = "<html>即去除填答时间在正式投放时间之前的试答记录，请在文本框中填写问卷正式投放时间即可，<br/>注意格式为“2013-09-01 18:11:11”，所有填答开始时间早于输入的问卷开始时间的记录会被清洗。<br/><br/>格式要求：<br/><font style=\"color: red\">年-月-日 时:分:秒，其中破折号，空格，冒号都需在英文输入法下输入。</font><br/><br/>注意点：<br/>年是4位数字；月是2位数字；日是2位数字；时是2位数字，24小时制；分是2位数字；<br/>秒是2位数字。比如2013-09-01 20:20:20表示2013年9月1号晚上8点20分20秒。</html>";
	private final String step3Help = "<html>即去除填答时间过长或过短的记录，在文本框中填写本次调研可接受的最短填答时间<br/>和最长填答时间，直接填写数字即可。<br/><br/>注意点：<br/><font style=\"color: red\">目前只支持到分钟，1分钟以内的时间目前无法读取，格式是必须为整数数字；</font><br/>最短填答时间和最长填答时间可以都写，也可以只写其一。</html>";
	private final String step4Help1 = "<html>即去除关键题目没有数据的记录。直接在文本框中填写题号即可，如果需要对多题的缺失进行清洗，多题题号之间用<font style=\"color: red\">英文输入法的逗号</font>隔开即可。<br/>特别提醒的是系统目前无法判断是否是应该回答此问题的用户缺失，因此最好放入的题目序号为全样本的必答题，对于有逻辑展现的题目缺失记录<br/>放在第四步清洗，具体请看注意点1.<br/>比如清洗第5题缺失的记录，需要输入Q5；如要清洗第5题或者第10题缺失的记录，需要输入Q5, Q10，<br/>如要清洗第5题并且第10题缺失的记录，请在第四步去除非目标用户的文本框中输入Q5 = empty && Q10 = empty。<br/><br/>";
	private final String step4Help2 = "注意点：<br/>1. <font style=\"color: red\">如果该题含有题目展现逻辑，即由之前的问题判断用户是否需要回答这题，对于这类题目最好不要放在此步中去清洗，</font>因为会把本来不需要回答<br/>此题的用户记录也清洗掉，<font style=\"color: red\">建议放在第四步去除非目标用户记录中由表达式来清洗，</font>举例如果下面两题为关键问题，需要把没有答这两题的用户清<br/>洗掉，可在第四步文本框中录入表达式：<br/>Q17=empty || (Q17=[O1:1] &&Q18=empty)<br/>";
	private final String step4Help3 = "Q17 请问您在哪些渠道购买过机票？（多选）<br/><div style=\"padding-left:10px\">1淘宝<br/>2携程<br/>3去哪儿<br/>4其他<br/></div>Q18请问在购买机票的过程中您的角色是？（单选）（第17题选择1回答）<br/><div style=\"padding-left:10px\">1主要决策者<br/>2不参与决策，只负责购买 <br/>3参与决策，但不是主要决策者</div><br/>";
	private final String step4Help4 = "2. 如该题文本框中无内容，系统默认不对该条件进行清洗，但下面的步骤仍然继续，不会清洗终止。<br/>3. 问卷数据中在部分问题上有缺失的记录不占少数，<font style=\"color: red\">因此建议大家把最关键的问题有缺失的记录清洗掉，而不是把一些在非关键问题有缺失的记录<br/>也都清洗掉。</font></html>";
	private final String step5Help1 = "<html>即清洗掉非本次调研关注的目标用户记录，这个功能会根据输入的表达式进行清洗。支持对多个表达式的清洗。多个表达式直接用换行分割。<br/>表达式语法：<br/><font style=\"color: red\">Q加上数字表示题号。= 表示等于。!=表示不等于。&&表示“且”。 ||表示“或者”。（）表示表达式的组。empty表示该题值为空。目前！=只能用于单选题。</font><br/><br/>";
	private final String step5Help2 = "单选题表达式：<br/>Q1=1 （等号后为选项）<br/><br/>多选题表达式：<br/>Q2=[O1:1]表示第二题选1<br/>Q2=[O1:1] || Q2=[O2:1]表示第二题选1或者选2<br/>[ ]里面是多选题的选项。O（字母O）加上数字表示多选题中的第几个选项。冒号后为多选题该选项的值，即0或1。<br/><br/>";
	private final String step5Help3 = "多题复合表达式：<br/>Q1=1 && Q2=2 表示清洗第一题选1且第二题选2的记录。<br/>如第二题为多选题，则为Q1=1 && (Q2=[O1:1] || Q2=[O2:1]) 表示清洗第二题选1或2且第一题选1的记录。括号的优先级高于&&和||。如没加括号，清洗按照从左往右的顺序执行<br/>表达式支持无穷多个表达式同时&&或||。如Q1=1 && (Q4=[O1:1] || Q4=[O2:1]) && Q6=empty表示清洗第一题选1 且 （第四题选1或2）且第六题为空的记录.<br/><br/>";
	private final String step5Help4 = "案例：<br/>如某调研问卷考察的是淘宝机票用户的购买决策模式（问题如下），即没有在淘宝购买过机票且不参与决策的用户不是我们的目标用户，要把这些用户清洗掉。<br/>清洗表达式：Q17=[O1:0] || Q18=2 <br/><font style=\"color: red\">特别提醒，括号中的条件是要被清洗掉的条件，而不是要保留的条件</font><br/>Q17 请问您在哪些渠道购买过机票？（多选）<br/><div style=\"padding-left:10px\">1淘宝<br/>2携程<br/>3去哪儿<br/>4其他<br/></div>Q18请问在购买机票的过程中您的角色是？（单选）（第17题选择1回答）<br/><div style=\"padding-left:10px\">1主要决策者<br/>2不参与决策，只负责购买 <br/>3参与决策，但不是主要决策者</div></html>";
	private final String step6Help = "<html>即清洗掉问卷填答有逻辑矛盾的用户记录，这个功能与上一步一样会根据输入的表达式进行清洗。支持对多个表达式的清洗。多个表达式直接用换行分割。<br/><br/>案例:<br/>某调研想要看成熟彩民的购彩行为，彩票每注为2元，如用户选择一周购彩频率在8次以上，选择一周购彩金额却在10元以下，则明显为矛盾的回答，需要清洗掉。<br/>清洗表达式：Q5=5 &&（Q6=1 || Q6=2）<br/>Q5 请问您一周购买彩票的频率为？（单选）<br/><div style=\"padding-left:10px\">1 一周1-2次<br/>2 一周3-4次<br/>3 一周5-6次<br/>4 一周7-8次<br/>5 一周8次以上<br/></div>Q6请问您一周购买彩票的金额为？（单选）<br/><div style=\"padding-left:10px\">1 一周5元以下<br/>2  一周5-10元<br/>3 一周10-20元<br/>4 一周20-50元<br/>5 一周50-100次<br/>6 一周100元以上</div></html>";
	
	private DataWasher dataWasher = null;

	public MainFrame(String title) {
		jframe.setTitle(title);
		jframe.getContentPane().setLayout(null);
		jframe.setResizable(false);

		topPanel.setBounds(new Rectangle(0, 0, 700, 80));
		topPanel.setBorder(BorderFactory.createRaisedBevelBorder());
		centerPanel.setBounds(new Rectangle(0, 80, 700, 310));
		centerPanel.setBorder(BorderFactory.createRaisedBevelBorder());
		centerPanel.setLayout(new BorderLayout());
		bottomPanel.setBounds(new Rectangle(0, 390, 700, 250));
		bottomPanel.setBorder(BorderFactory.createRaisedBevelBorder());
		jframe.getContentPane().add(topPanel);
		jframe.getContentPane().add(centerPanel);
		jframe.getContentPane().add(bottomPanel);
		
		init();
	}

	private void initTopPanel(){
		topPanel.setLayout(null);
		importButton.addActionListener(new ImportFileActionListener(this));
		importButton.setText("导入问卷");
		importButton.setBounds(new Rectangle(30, 15, 120, 30));
		
		selectedFilePathLabel.setBounds(new Rectangle(180, 15, 500, 30));
		
		importLabel.setText("共 条记录");
		importLabel.setBounds(new Rectangle(30, 50, 220, 30));
		
		errorMessageLabel.setBounds(new Rectangle(300, 50, 420, 30));
		
		topPanel.add(importButton); 
		topPanel.add(selectedFilePathLabel);
		topPanel.add(importLabel);
		topPanel.add(errorMessageLabel);
	}
	
	private void initCenterPanel() {
		centerPanel.setLayout(null);
		ImageIcon icon = new ImageIcon(getClass().getResource("info_icon.gif"));
		
		step2Label.setText("第一步: 去除试答记录");
		step2Label.setBounds(new Rectangle(30, 0, 180, 30));
		
		step2InfoIconButton.setBounds(new Rectangle(156, 4, 20, 20));
		step2InfoIconButton.setIcon(icon);
		step2InfoIconButton.setBorder(null);
		step2InfoIconButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(jframe, step2Help);
			}
		});
		
		step2StartTimeLabel.setText("问卷开始时间");
		step2StartTimeLabel.setBounds(new Rectangle(190, 0, 100, 30));
		
		startTimeTextBox.setBounds(new Rectangle(280, 2, 150, 30));
		
		step2StartTimeFormatLabel.setText("如: 2013-09-01 18:18:18");
		step2StartTimeFormatLabel.setBounds(new Rectangle(440, 0, 150, 30));
		
		step3Label.setText("第二步: 去除填答时间不合适记录");
		step3Label.setBounds(new Rectangle(30, 33, 200, 30));
		
		step3InfoIconButton.setBounds(new Rectangle(220, 37, 20, 20));
		step3InfoIconButton.setIcon(icon);
		step3InfoIconButton.setBorder(null);
		step3InfoIconButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(jframe, step3Help);
			}
		});
		
		step3TimeSpanLabel.setText("合理时间范围");
		step3TimeSpanLabel.setBounds(new Rectangle(250, 33, 100, 30));
		
		step3ShortSpanLabel.setText("最短填答时间");
		step3ShortSpanLabel.setBounds(new Rectangle(350, 33, 80, 30));
		
		step3ShortSpanTextBox.setBounds(new Rectangle(435, 33, 40, 30));
		
		step3ShortSpanMinuteLabel.setText("分钟");
		step3ShortSpanMinuteLabel.setBounds(new Rectangle(480, 33, 40, 30));
		
		step3LongSpanLabel.setText("最长填答时间");
		step3LongSpanLabel.setBounds(new Rectangle(520, 33, 80, 30));
		step3LongSpanTextBox.setBounds(new Rectangle(605, 33, 40, 30));
		
		step3LongSpanMinuteLabel.setText("分钟");
		step3LongSpanMinuteLabel.setBounds(new Rectangle(650, 33, 40, 30));
		
		step4Label.setText("第三步: 去除关键问题缺失记录");
		step4Label.setBounds(new Rectangle(30, 68, 200, 30));
		
		step4InfoIconButton.setBounds(new Rectangle(208, 72, 20, 20));
		step4InfoIconButton.setIcon(icon);
		step4InfoIconButton.setBorder(null);
		step4InfoIconButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(jframe, step4Help1 + step4Help2 + step4Help3 + step4Help4);
			}
		});
		
		step4KeyQuestionTextBox.setBounds(new Rectangle(240, 68, 426, 30));
		
		step5Label.setText("第四步: 去除非目标用户记录");
		step5Label.setBounds(new Rectangle(30, 103, 200, 30));
		
		step5InfoIconButton.setBounds(new Rectangle(195, 107, 20, 20));
		step5InfoIconButton.setIcon(icon);
		step5InfoIconButton.setBorder(null);
		step5InfoIconButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(jframe, step5Help1 + step5Help2 + step5Help3 + step5Help4);
			}
		});
		
		step5NotTargetUserTextBox.setLineWrap(true);
		step5NotTargetUserTextBoxScrollPan.setBounds(new Rectangle(240, 103, 426, 80));
		
		step6Label.setText("第五步: 去除逻辑矛盾记录");
		step6Label.setBounds(new Rectangle(30, 188, 200, 30));
		
		step6InfoIconButton.setBounds(new Rectangle(180, 192, 20, 20));
		step6InfoIconButton.setIcon(icon);
		step6InfoIconButton.setBorder(null);
		step6InfoIconButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(jframe, step6Help);
			}
		});
		step6LogicConflictTextBox.setLineWrap(true);
		step6LogicConflictTextBoxScrollPan.setBounds(new Rectangle(240, 188, 426, 80));
		
		step1Label.setText("第六步: 去除重复填答记录");
		step1Label.setBounds(new Rectangle(30, 270, 180, 30));
		
		step1InfoIconButton.setBounds(new Rectangle(182, 275, 20, 20));
		step1InfoIconButton.setIcon(icon);
		step1InfoIconButton.setBorder(null);
		step1InfoIconButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(jframe, step1Help);
			}
		});

		step1IdLabel.setText("请输入去重依据的问题题号");
		step1IdLabel.setBounds(new Rectangle(220, 270, 170, 30));
		step1IdTextBox.setBounds(new Rectangle(387, 272, 50, 30));
		
		centerPanel.add(step1Label);
		centerPanel.add(step1InfoIconButton);
		centerPanel.add(step1IdLabel);
		centerPanel.add(step1IdTextBox);
		centerPanel.add(step2Label);
		centerPanel.add(step2InfoIconButton);
		centerPanel.add(step2StartTimeLabel);
		centerPanel.add(startTimeTextBox);
		centerPanel.add(step2StartTimeFormatLabel);
		centerPanel.add(step3Label);
		centerPanel.add(step3InfoIconButton);
		centerPanel.add(step3TimeSpanLabel);
		centerPanel.add(step3ShortSpanLabel);
		centerPanel.add(step3ShortSpanTextBox);
		centerPanel.add(step3ShortSpanMinuteLabel);
		centerPanel.add(step3LongSpanLabel);
		centerPanel.add(step3LongSpanTextBox);
		centerPanel.add(step3LongSpanMinuteLabel);
		centerPanel.add(step4Label);
		centerPanel.add(step4InfoIconButton);
		centerPanel.add(step4KeyQuestionTextBox);
		centerPanel.add(step5Label);
		centerPanel.add(step5InfoIconButton);
		centerPanel.add(step5NotTargetUserTextBoxScrollPan);
		centerPanel.add(step6Label);
		centerPanel.add(step6InfoIconButton);
		centerPanel.add(step6LogicConflictTextBoxScrollPan);
	}
	
	private void initBottomPanel() {
		bottomPanel.setLayout(null);
		startWashButton.setEnabled(false);
		startWashButton.addActionListener(new StartWashActionListener(this));
		startWashButton.setText("开始清洗问卷");
		startWashButton.setBounds(new Rectangle(30, 5, 120, 30));
		
		exportButton.addActionListener(new ExportWashedFileActionListener(this));
		exportButton.setEnabled(false);
		exportButton.setText("导出清洗后问卷");
		exportButton.setBounds(new Rectangle(180, 5, 150, 30));
		
		washedLabel.setBounds(new Rectangle(360, 5, 220, 30));
		
		exportWashedLabel.setBounds(new Rectangle(30, 40, 600, 150));
		exportWashedLabel.setVerticalAlignment(SwingConstants.TOP);
		
		bottomPanel.add(startWashButton); 
		bottomPanel.add(exportButton); 
		bottomPanel.add(washedLabel);
		bottomPanel.add(exportWashedLabel);
	}
	
	private void init(){
		initTopPanel();
		initCenterPanel();
		initBottomPanel();
		
		jframe.pack();
		jframe.setVisible(true);
		jframe.setSize(700, 650);
		jframe.setDefaultCloseOperation(jframe.EXIT_ON_CLOSE);
		
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int)screensize.getWidth();
		int height = (int)screensize.getHeight();
		
		jframe.setLocation(width / 2 - 350, height / 2 - 350);
	}

	public DataWasher getDataWasher() {
		return dataWasher;
	}

	public void setDataWasher(DataWasher dataWasher) {
		this.dataWasher = dataWasher;
	}

	public JLabel getImportLabel() {
		return importLabel;
	}

	public JLabel getSelectedFilePathLabel() {
		return selectedFilePathLabel;
	}

	public JLabel getErrorMessageLabel() {
		return errorMessageLabel;
	}

	public JTextField getStartTimeTextBox() {
		return startTimeTextBox;
	}

	public JTextField getStep3ShortSpanTextBox() {
		return step3ShortSpanTextBox;
	}

	public JTextField getStep3LongSpanTextBox() {
		return step3LongSpanTextBox;
	}

	public JTextField getStep4KeyQuestionTextBox() {
		return step4KeyQuestionTextBox;
	}

	public JTextArea getStep5NotTargetUserTextBox() {
		return step5NotTargetUserTextBox;
	}

	public JTextArea getStep6LogicConflictTextBox() {
		return step6LogicConflictTextBox;
	}

	public JButton getStartWashButton() {
		return startWashButton;
	}

	public JButton getExportButton() {
		return exportButton;
	}

	public JPanel getTopPanel() {
		return topPanel;
	}

	public JLabel getWashedLabel() {
		return washedLabel;
	}
	
	public JTextField getStep1IdTextBox() {
		return step1IdTextBox;
	}

	public void displaySourceRecordCount(){
		StringBuilder sb = new StringBuilder();
		sb.append("导入成功! 共").append(dataWasher.getSourceRecordCount()).append("条记录");
		importLabel.setText(sb.toString());
	}
	
	public void displayWashedRecordCount(){
		StringBuilder sb = new StringBuilder();
		sb.append("清洗后共").append(dataWasher.getWashedRecordCount()).append("条记录");
		washedLabel.setText(sb.toString());
	}
	
	public void enableStartWashButton(boolean enable){
		startWashButton.setEnabled(enable);
	}
	
	public void enableExportButton(boolean enable){
		exportButton.setEnabled(enable);
	}
	
	public void cleanErrorMessage() {
		errorMessageLabel.setText("");
	}
	
	public void cleanWashMessage(){
		exportWashedLabel.setText("");
	}
	
	public void cleanDisplayedMessage(){
		cleanErrorMessage();
		cleanWashMessage();
	}

	public void logErrorMessage(String errorMessage) {
		StringBuilder sb = new StringBuilder();
		sb.append("<html><font style=\"color: red\">").append(errorMessage)
				.append("</font></html>");
		errorMessageLabel.setText(sb.toString());
	}
	
	public void logWashMessage(String washMessage){
		String oldText = exportWashedLabel.getText().trim();
		if("".equals(oldText)){
			oldText = "<html></html>";
		}
		
		int index = oldText.lastIndexOf("<");
		StringBuilder sb = new StringBuilder();
		sb.append(oldText.substring(0, index));
		sb.append("<font style=\"color: green\">").append(washMessage).append("</font><br /></html>");
		exportWashedLabel.setText(sb.toString());
	}
	
	
	public static void main(String args[]) {
		MainFrame mywin = new MainFrame("问卷清洗 V3.0");
	}
}