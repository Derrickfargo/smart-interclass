package cn.com.incito.interclass.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

import cn.com.incito.interclass.po.Device;
import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Quiz;
import cn.com.incito.interclass.po.Student;
import cn.com.incito.interclass.po.Table;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.utils.ScaleImageUtils;

/**
 * 任务缩略图列表面板
 * 
 * @author popoy
 */
public class QuizLookupPanel extends JPanel {

	/**
     *
     */
	private static final long serialVersionUID = 6316121486627261595L;

	private static final String PAD_ONLINE = "images/main/ico_pad_connection.png";
	private static final String PAD_OFFLINE = "images/main/ico_pad_disconnect.png";

	private Application app = Application.getInstance();
	/**
	 * 当前教室所有Table，初始化界面时初始化本属性
	 */
	private List<QuizItemPanel> tableList = new ArrayList<QuizItemPanel>();
	/**
	 * 当前教室所有Group，初始化数据时初始化本属性
	 */
	private List<Group> groupList = new ArrayList<Group>();
	/**
	 * 试卷列表
	 */
	private List<Quiz> quizs = new ArrayList<Quiz>();
	GridBagConstraints c;
	GridBagLayout gridBagLayout;

	public QuizLookupPanel() {
		// this.setSize(878, 620);
		// setting a default constraint value
		this.setOpaque(false);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0 };
		gridBagLayout.rowHeights = new int[] { 0 };
		gridBagLayout.columnWeights = new double[] { Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { Double.MIN_VALUE };
		setLayout(gridBagLayout);
		c = new GridBagConstraints();
		// setting a default constraint value
		c.fill = GridBagConstraints.HORIZONTAL;

		c.gridx = 0; // x grid position
		c.gridy = 0; // y grid position

		// 加载数据
		// refresh();
		revalidate();
	}

	public void refresh() {
		worker.execute();
		initData();
		// clearView();
		// 遍历内存模型，绑定到物理模型
		for (int i = 0; i < groupList.size(); i++) {// 遍历分组内存模型
			Group group = groupList.get(i);
			QuizItemPanel quizItemPanel = new QuizItemPanel(group);
			quizItemPanel.setVisible(true);
			gridBagLayout.setConstraints(quizItemPanel, c); // associate the
															// label with
			// a constraint object
			add(quizItemPanel);
		}

	}

	private void initData() {
		groupList = new ArrayList<Group>();
		// 课桌绑定分组，生成内存模型
		List<Table> tableList = app.getTableList();
		for (Table table : tableList) {
			// 获得课桌对应的分组
			Group group = app.getTableGroup().get(table.getId());
			if (group == null) {
				group = new Group();
			}
			group.setTableId(table.getId());
			group.setTableNumber(table.getNumber());
			group.setDevices(table.getDevices());
			groupList.add(group);
		}
		Collections.sort(groupList);
	}

	private SwingWorker worker = new SwingWorker<List<String>, Void>() {

		@Override
		protected List<String> doInBackground() throws Exception {
			List<String> quizPath = getQuizById(Application.getInstance()
					.getQuizId());
			return quizPath;
		}
	};

	private List<String> getQuizById(String quizId) {
		File dir = new File("c:/image2.jpg");
		List<String> list = new ArrayList<String>();
		File[] files = dir.listFiles();
		try {
			for (File f : files) {
				BufferedImage bufferedImage = ImageIO.read(f);
				String path = ScaleImageUtils.resize(100, 100,
						"c:/temp/image2.jpg", bufferedImage);
				list.add(path);
			}
		} catch (IOException e) {
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		}
		return list;
	}
}
