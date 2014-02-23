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

	private final String step1Help = "<html>�����ı��������롰ȥ�����ݵ���š���һ��������IDһ��Ϊ���ݣ������ҵ������Ƶȣ�<br/><br/>��ʽҪ��<br/><font style=\"color: red\">Q+��Ŀ������ţ���Q 1���м��޿ո�</font><br/><br/>ע��㣺<br/>��������ID��ȫһ���ļ�¼��ֻ�������ʱ������ļ�¼��<br/>��������IDΪ�յļ�¼�������ȫ��ͬ���������ʱ�䣩��¼������ϴ��ֻ����һ����¼��</html>";
	private final String step2Help = "<html>��ȥ�����ʱ������ʽͶ��ʱ��֮ǰ���Դ��¼�������ı�������д�ʾ���ʽͶ��ʱ�伴�ɣ�<br/>ע���ʽΪ��2013-09-01 18:11:11�����������ʼʱ������������ʾ�ʼʱ��ļ�¼�ᱻ��ϴ��<br/><br/>��ʽҪ��<br/><font style=\"color: red\">��-��-�� ʱ:��:�룬�������ۺţ��ո�ð�Ŷ�����Ӣ�����뷨�����롣</font><br/><br/>ע��㣺<br/>����4λ���֣�����2λ���֣�����2λ���֣�ʱ��2λ���֣�24Сʱ�ƣ�����2λ���֣�<br/>����2λ���֡�����2013-09-01 20:20:20��ʾ2013��9��1������8��20��20�롣</html>";
	private final String step3Help = "<html>��ȥ�����ʱ���������̵ļ�¼�����ı�������д���ε��пɽ��ܵ�������ʱ��<br/>������ʱ�䣬ֱ����д���ּ��ɡ�<br/><br/>ע��㣺<br/><font style=\"color: red\">Ŀǰֻ֧�ֵ����ӣ�1�������ڵ�ʱ��Ŀǰ�޷���ȡ����ʽ�Ǳ���Ϊ�������֣�</font><br/>������ʱ�������ʱ����Զ�д��Ҳ����ֻд��һ��</html>";
	private final String step4Help1 = "<html>��ȥ���ؼ���Ŀû�����ݵļ�¼��ֱ�����ı�������д��ż��ɣ������Ҫ�Զ����ȱʧ������ϴ���������֮����<font style=\"color: red\">Ӣ�����뷨�Ķ���</font>�������ɡ�<br/>�ر����ѵ���ϵͳĿǰ�޷��ж��Ƿ���Ӧ�ûش��������û�ȱʧ�������÷������Ŀ���Ϊȫ�����ıش��⣬�������߼�չ�ֵ���Ŀȱʧ��¼<br/>���ڵ��Ĳ���ϴ�������뿴ע���1.<br/>������ϴ��5��ȱʧ�ļ�¼����Ҫ����Q5����Ҫ��ϴ��5����ߵ�10��ȱʧ�ļ�¼����Ҫ����Q5, Q10��<br/>��Ҫ��ϴ��5�Ⲣ�ҵ�10��ȱʧ�ļ�¼�����ڵ��Ĳ�ȥ����Ŀ���û����ı���������Q5 = empty && Q10 = empty��<br/><br/>";
	private final String step4Help2 = "ע��㣺<br/>1. <font style=\"color: red\">������⺬����Ŀչ���߼�������֮ǰ�������ж��û��Ƿ���Ҫ�ش����⣬����������Ŀ��ò�Ҫ���ڴ˲���ȥ��ϴ��</font>��Ϊ��ѱ�������Ҫ�ش�<br/>������û���¼Ҳ��ϴ����<font style=\"color: red\">������ڵ��Ĳ�ȥ����Ŀ���û���¼���ɱ��ʽ����ϴ��</font>���������������Ϊ�ؼ����⣬��Ҫ��û�д���������û���<br/>ϴ�������ڵ��Ĳ��ı�����¼����ʽ��<br/>Q17=empty || (Q17=[O1:1] &&Q18=empty)<br/>";
	private final String step4Help3 = "Q17 ����������Щ�����������Ʊ������ѡ��<br/><div style=\"padding-left:10px\">1�Ա�<br/>2Я��<br/>3ȥ�Ķ�<br/>4����<br/></div>Q18�����ڹ����Ʊ�Ĺ��������Ľ�ɫ�ǣ�����ѡ������17��ѡ��1�ش�<br/><div style=\"padding-left:10px\">1��Ҫ������<br/>2��������ߣ�ֻ������ <br/>3������ߣ���������Ҫ������</div><br/>";
	private final String step4Help4 = "2. ������ı����������ݣ�ϵͳĬ�ϲ��Ը�����������ϴ��������Ĳ�����Ȼ������������ϴ��ֹ��<br/>3. �ʾ��������ڲ�����������ȱʧ�ļ�¼��ռ������<font style=\"color: red\">��˽����Ұ���ؼ���������ȱʧ�ļ�¼��ϴ���������ǰ�һЩ�ڷǹؼ�������ȱʧ�ļ�¼<br/>Ҳ����ϴ����</font></html>";
	private final String step5Help1 = "<html>����ϴ���Ǳ��ε��й�ע��Ŀ���û���¼��������ܻ��������ı��ʽ������ϴ��֧�ֶԶ�����ʽ����ϴ��������ʽֱ���û��зָ<br/>���ʽ�﷨��<br/><font style=\"color: red\">Q�������ֱ�ʾ��š�= ��ʾ���ڡ�!=��ʾ�����ڡ�&&��ʾ���ҡ��� ||��ʾ�����ߡ���������ʾ���ʽ���顣empty��ʾ����ֵΪ�ա�Ŀǰ��=ֻ�����ڵ�ѡ�⡣</font><br/><br/>";
	private final String step5Help2 = "��ѡ����ʽ��<br/>Q1=1 ���Ⱥź�Ϊѡ�<br/><br/>��ѡ����ʽ��<br/>Q2=[O1:1]��ʾ�ڶ���ѡ1<br/>Q2=[O1:1] || Q2=[O2:1]��ʾ�ڶ���ѡ1����ѡ2<br/>[ ]�����Ƕ�ѡ���ѡ�O����ĸO���������ֱ�ʾ��ѡ���еĵڼ���ѡ�ð�ź�Ϊ��ѡ���ѡ���ֵ����0��1��<br/><br/>";
	private final String step5Help3 = "���⸴�ϱ��ʽ��<br/>Q1=1 && Q2=2 ��ʾ��ϴ��һ��ѡ1�ҵڶ���ѡ2�ļ�¼��<br/>��ڶ���Ϊ��ѡ�⣬��ΪQ1=1 && (Q2=[O1:1] || Q2=[O2:1]) ��ʾ��ϴ�ڶ���ѡ1��2�ҵ�һ��ѡ1�ļ�¼�����ŵ����ȼ�����&&��||����û�����ţ���ϴ���մ������ҵ�˳��ִ��<br/>���ʽ֧�����������ʽͬʱ&&��||����Q1=1 && (Q4=[O1:1] || Q4=[O2:1]) && Q6=empty��ʾ��ϴ��һ��ѡ1 �� ��������ѡ1��2���ҵ�����Ϊ�յļ�¼.<br/><br/>";
	private final String step5Help4 = "������<br/>��ĳ�����ʾ�������Ա���Ʊ�û��Ĺ������ģʽ���������£�����û�����Ա��������Ʊ�Ҳ�������ߵ��û��������ǵ�Ŀ���û���Ҫ����Щ�û���ϴ����<br/>��ϴ���ʽ��Q17=[O1:0] || Q18=2 <br/><font style=\"color: red\">�ر����ѣ������е�������Ҫ����ϴ����������������Ҫ����������</font><br/>Q17 ����������Щ�����������Ʊ������ѡ��<br/><div style=\"padding-left:10px\">1�Ա�<br/>2Я��<br/>3ȥ�Ķ�<br/>4����<br/></div>Q18�����ڹ����Ʊ�Ĺ��������Ľ�ɫ�ǣ�����ѡ������17��ѡ��1�ش�<br/><div style=\"padding-left:10px\">1��Ҫ������<br/>2��������ߣ�ֻ������ <br/>3������ߣ���������Ҫ������</div></html>";
	private final String step6Help = "<html>����ϴ���ʾ�������߼�ì�ܵ��û���¼�������������һ��һ�����������ı��ʽ������ϴ��֧�ֶԶ�����ʽ����ϴ��������ʽֱ���û��зָ<br/><br/>����:<br/>ĳ������Ҫ���������Ĺ�����Ϊ����ƱÿעΪ2Ԫ�����û�ѡ��һ�ܹ���Ƶ����8�����ϣ�ѡ��һ�ܹ��ʽ��ȴ��10Ԫ���£�������Ϊì�ܵĻش���Ҫ��ϴ����<br/>��ϴ���ʽ��Q5=5 &&��Q6=1 || Q6=2��<br/>Q5 ������һ�ܹ����Ʊ��Ƶ��Ϊ������ѡ��<br/><div style=\"padding-left:10px\">1 һ��1-2��<br/>2 һ��3-4��<br/>3 һ��5-6��<br/>4 һ��7-8��<br/>5 һ��8������<br/></div>Q6������һ�ܹ����Ʊ�Ľ��Ϊ������ѡ��<br/><div style=\"padding-left:10px\">1 һ��5Ԫ����<br/>2  һ��5-10Ԫ<br/>3 һ��10-20Ԫ<br/>4 һ��20-50Ԫ<br/>5 һ��50-100��<br/>6 һ��100Ԫ����</div></html>";
	
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
		importButton.setText("�����ʾ�");
		importButton.setBounds(new Rectangle(30, 15, 120, 30));
		
		selectedFilePathLabel.setBounds(new Rectangle(180, 15, 500, 30));
		
		importLabel.setText("�� ����¼");
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
		
		step2Label.setText("��һ��: ȥ���Դ��¼");
		step2Label.setBounds(new Rectangle(30, 0, 180, 30));
		
		step2InfoIconButton.setBounds(new Rectangle(156, 4, 20, 20));
		step2InfoIconButton.setIcon(icon);
		step2InfoIconButton.setBorder(null);
		step2InfoIconButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(jframe, step2Help);
			}
		});
		
		step2StartTimeLabel.setText("�ʾ�ʼʱ��");
		step2StartTimeLabel.setBounds(new Rectangle(190, 0, 100, 30));
		
		startTimeTextBox.setBounds(new Rectangle(280, 2, 150, 30));
		
		step2StartTimeFormatLabel.setText("��: 2013-09-01 18:18:18");
		step2StartTimeFormatLabel.setBounds(new Rectangle(440, 0, 150, 30));
		
		step3Label.setText("�ڶ���: ȥ�����ʱ�䲻���ʼ�¼");
		step3Label.setBounds(new Rectangle(30, 33, 200, 30));
		
		step3InfoIconButton.setBounds(new Rectangle(220, 37, 20, 20));
		step3InfoIconButton.setIcon(icon);
		step3InfoIconButton.setBorder(null);
		step3InfoIconButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(jframe, step3Help);
			}
		});
		
		step3TimeSpanLabel.setText("����ʱ�䷶Χ");
		step3TimeSpanLabel.setBounds(new Rectangle(250, 33, 100, 30));
		
		step3ShortSpanLabel.setText("������ʱ��");
		step3ShortSpanLabel.setBounds(new Rectangle(350, 33, 80, 30));
		
		step3ShortSpanTextBox.setBounds(new Rectangle(435, 33, 40, 30));
		
		step3ShortSpanMinuteLabel.setText("����");
		step3ShortSpanMinuteLabel.setBounds(new Rectangle(480, 33, 40, 30));
		
		step3LongSpanLabel.setText("����ʱ��");
		step3LongSpanLabel.setBounds(new Rectangle(520, 33, 80, 30));
		step3LongSpanTextBox.setBounds(new Rectangle(605, 33, 40, 30));
		
		step3LongSpanMinuteLabel.setText("����");
		step3LongSpanMinuteLabel.setBounds(new Rectangle(650, 33, 40, 30));
		
		step4Label.setText("������: ȥ���ؼ�����ȱʧ��¼");
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
		
		step5Label.setText("���Ĳ�: ȥ����Ŀ���û���¼");
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
		
		step6Label.setText("���岽: ȥ���߼�ì�ܼ�¼");
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
		
		step1Label.setText("������: ȥ���ظ�����¼");
		step1Label.setBounds(new Rectangle(30, 270, 180, 30));
		
		step1InfoIconButton.setBounds(new Rectangle(182, 275, 20, 20));
		step1InfoIconButton.setIcon(icon);
		step1InfoIconButton.setBorder(null);
		step1InfoIconButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(jframe, step1Help);
			}
		});

		step1IdLabel.setText("������ȥ�����ݵ��������");
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
		startWashButton.setText("��ʼ��ϴ�ʾ�");
		startWashButton.setBounds(new Rectangle(30, 5, 120, 30));
		
		exportButton.addActionListener(new ExportWashedFileActionListener(this));
		exportButton.setEnabled(false);
		exportButton.setText("������ϴ���ʾ�");
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
		sb.append("����ɹ�! ��").append(dataWasher.getSourceRecordCount()).append("����¼");
		importLabel.setText(sb.toString());
	}
	
	public void displayWashedRecordCount(){
		StringBuilder sb = new StringBuilder();
		sb.append("��ϴ��").append(dataWasher.getWashedRecordCount()).append("����¼");
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
		MainFrame mywin = new MainFrame("�ʾ���ϴ V3.0");
	}
}