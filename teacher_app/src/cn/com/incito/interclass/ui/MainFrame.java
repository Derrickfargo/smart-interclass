package cn.com.incito.interclass.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import cn.com.incito.interclass.Listener.MySystemTrayEvent;
import cn.com.incito.interclass.Listener.MySystemTrayManager;
import cn.com.incito.interclass.po.Student;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.CoreSocket;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 199999999L;
	private static MainFrame instance;
	
	private MySystemTrayEvent mySystemTrayEvent;
	private JList<String> lstStudent;
	private JTextArea txtConsole;
	
	
	public static MainFrame getInstance() {
		if (instance == null) {
			instance = new MainFrame();
		}
		return instance;
	}

	private MainFrame() {
		//启动通讯线程
		CoreSocket.getInstance().start();
		initFrame();
		initData();
	}
	
	private Action send = new AbstractAction("Send") {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			doSend();
		}

	};

	private Action lock = new AbstractAction("锁屏") {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {

		}

	};

	private Action unlock = new AbstractAction("解锁屏") {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {

		}

	};

	private Action sendPicture = new AbstractAction("发送图片") {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			doSendPicture();
		}

	};

	public void initFrame() {
		setTitle("互动课堂");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		mySystemTrayEvent = new MySystemTrayManager();

		// center panel
		JPanel pnlLeft = new JPanel(new BorderLayout());
		lstStudent = new JList<String>();
		pnlLeft.add(new JScrollPane(lstStudent), BorderLayout.CENTER);
		pnlLeft.setBorder(BorderFactory.createTitledBorder("Pad List"));

		JPanel pnlRight = new JPanel(new BorderLayout());
		txtConsole = new JTextArea();
		txtConsole.setWrapStyleWord(true);
		txtConsole.setEditable(false);
		pnlRight.add(new JScrollPane(txtConsole), BorderLayout.CENTER);
		pnlRight.setBorder(BorderFactory.createTitledBorder("Console"));

		JSplitPane jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		jsp.add(pnlLeft, JSplitPane.LEFT);
		jsp.add(pnlRight, JSplitPane.RIGHT);
		jsp.setDividerLocation(130);
		jsp.setResizeWeight(0.3);
		add(jsp, BorderLayout.CENTER);

		// south panel
		JPanel pnlSouth = new JPanel(new FlowLayout());
		pnlSouth.add(new JButton(new AbstractAction("测试") {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {

			}

		}));
		pnlSouth.add(new JButton(lock));
		pnlSouth.add(new JButton(unlock));
		pnlSouth.add(new JButton(sendPicture));
		add(pnlSouth, BorderLayout.SOUTH);

		// frame attributes
		setSize(600, 400);
		Dimension dime = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((int) (dime.getWidth() - getWidth()) / 2,
				(int) (dime.getHeight() - getHeight()) / 2);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void initData() {
		Application app = Application.getInstance();
		List<Student> studentList = app.getStudentList();
		if (studentList == null || studentList.size() == 0) {
			return;
		}
		String[] nameList = new String[studentList.size()];
		for (int i = 0; i < studentList.size(); i++) {
			nameList[i] = studentList.get(i).getName() + "[未登录]";
		}
		lstStudent.setListData(nameList);
	}
	
	public void refreshStudent() {
		lstStudent.removeAll();
		initData();
	}

	private void doSend() {

	}

	private void doSendPicture() {

	}

	public void addLog(String log) {
		txtConsole.append(log + "\r\n");
		txtConsole.setCaretPosition(txtConsole.getDocument().getLength());
	}

	public void showPicture(String path) {
		new PhotoFrame(path);
	}

	public MySystemTrayEvent getMySystemTrayEvent() {
		return mySystemTrayEvent;
	}
}
