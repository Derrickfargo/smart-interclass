package cn.com.incito.interclass.ui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Quiz;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.utils.ScaleImageUtils;

/**
 * 任务缩略图列表面板
 * 
 * @author popoy
 */
public class QuizPanel extends JPanel {

	/**
     *
     */
	private static final long serialVersionUID = 6316121486627261595L;

	private Application app = Application.getInstance();
	/**
	 * 当前教室所有Group，初始化数据时初始化本属性
	 */
	private List<Group> groupList = new ArrayList<Group>();
	/**
	 * 当前教室所有试题，初始化界面时初始化本属性
	 */
	private List<QuizGroupPanel> quizGroupList = new ArrayList<QuizGroupPanel>();
	
	public QuizPanel() {
		// this.setSize(878, 620);
		this.setLayout(null);
		this.setOpaque(true);
		groupList = app.getGroupList();
		
		// 初始化界面
		initView();

		// 加载数据
		refresh();
	}

	private void initView() {
		int i = 0;
		int y = 10;
		while (i < 12) {
			QuizGroupPanel pnlLeft = new QuizGroupPanel();
			pnlLeft.setBounds(10, y, 410, 374);
			add(pnlLeft);
			quizGroupList.add(pnlLeft);
			if (++i < 12) {
				QuizGroupPanel pnlRight = new QuizGroupPanel();
				pnlRight.setBounds(438, y, 410, 374);
				add(pnlRight);
				quizGroupList.add(pnlRight);
			}
			i++;
			y += 380;
		}
	}

	public void refresh() {
		initData();
		
	}

	private void initData() {
		
	}

	private SwingWorker worker = new SwingWorker<List<String>, Void>() {

		@Override
		protected List<String> doInBackground() throws Exception {
			List<String> quizPath = getQuizById(Application.getInstance().getQuizId());
			return quizPath;
		}
		
		private List<String> getQuizById(String quizId) {
			File dir = new File("c:/image2.jpg");
			List<String> list = new ArrayList<String>();
			File[] files = dir.listFiles();
			try {
				for (File f : files) {
					BufferedImage bufferedImage = ImageIO.read(f);
					String path = ScaleImageUtils.resize(100, 100, "c:/temp/image2.jpg", bufferedImage);
					list.add(path);
				}
			} catch (IOException e) {
				e.printStackTrace(); 
			}
			return list;
		}
	};

}
