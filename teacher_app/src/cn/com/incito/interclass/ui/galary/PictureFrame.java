package cn.com.incito.interclass.ui.galary;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;

public class PictureFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int JPANEL2_WIDTH = 800;
	private static final int JPANEL2_HEIGHT = 555;

	/** Creates new form PictureFrame */
	public PictureFrame() {
		initComponents();
	}

	private void initComponents() {
		this.setSize(800, 600);
		jPanel2 = new JPanel();
		jPanel2.setSize(800, 600);
		jLabel1 = new JLabel();
		jButton1 = new JButton();
		jButton2 = new JButton();
		this.setSize(800, 400);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("java PictureView Manager");
		setBackground(new Color(255, 255, 255));
		setBounds(new Rectangle(200, 50, 500, 500));
		setMaximizedBounds(new Rectangle(100, 200, 800, 800));
		setState(1);
		jPanel2.setEnabled(false);
		jLabel1.setIcon(new ImageIcon("C:\\Java\\Wallpaper1349590065739.jpg"));
		jLabel1.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1,
				new Color(255, 0, 0)));

		jButton1.setText("上一个");
		jButton1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				JButton1Listener(evt);
			}
		});

		jButton2.setText("下一个");
		jButton2.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				jButton2MouseClicked(evt);
			}
		});

		GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout.setHorizontalGroup(jPanel2Layout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, JPANEL2_WIDTH,
						Short.MAX_VALUE)
				.addGroup(
						jPanel2Layout.createSequentialGroup()
								.addGap(252, 252, 252).addComponent(jButton1)
								.addGap(130, 130, 130).addComponent(jButton2)
								.addContainerGap(107, Short.MAX_VALUE)));
		jPanel2Layout
				.setVerticalGroup(jPanel2Layout
						.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel2Layout
										.createSequentialGroup()
										.addComponent(jLabel1,
												GroupLayout.PREFERRED_SIZE,
												JPANEL2_HEIGHT, Short.MAX_VALUE)
										.addPreferredGap(
												LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												jPanel2Layout
														.createParallelGroup(
																GroupLayout.Alignment.BASELINE)
														.addComponent(jButton1)
														.addComponent(jButton2))
										.addContainerGap()));

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup()
						.addComponent(jPanel2, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addContainerGap(15, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup()
						.addComponent(jPanel2, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addContainerGap(42, Short.MAX_VALUE)));
		pack();
	}// </editor-fold>

	private void MenuItem2Listener(ActionEvent evt) {
		// TODO 将在此处添加您的处理代码：
		JFileChooser chooser = new JFileChooser();
		filename = chooser.getSelectedFile().getName();
		openPath = chooser.getCurrentDirectory().getPath();
		System.out.println(openPath);
		ImageIcon imag = new ImageIcon(openPath + "\\" + filename);
		jLabel1.setIcon(imag);
	}

	private void MenuItem1Listener(ActionEvent evt) {
		// TODO 将在此处添加您的处理代码：
		ImageIcon imag = new ImageIcon();
		jLabel1.setIcon(imag);
	}

	private void jButton2MouseClicked(MouseEvent evt) {
		System.exit(1);
	}

	private void JButton1Listener(ActionEvent evt) {
		File file = new File(openPath);
		String[] str = file.list();
		str = file.list();
		ImageIcon imag = new ImageIcon(openPath + "\\" + str[i]);
		jLabel1.setIcon(imag);
		i++;
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new PictureFrame().setVisible(true);
			}
		});
	}

	// 变量声明 - 不进行修改
	private JButton jButton1;
	private JButton jButton2;
	private JLabel jLabel1;
	private JPanel jPanel2;
	private String filename;
	private String openPath;
	private static int i = 0;
	// 变量声明结束

}
