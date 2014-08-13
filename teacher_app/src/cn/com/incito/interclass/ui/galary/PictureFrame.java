package cn.com.incito.interclass.ui.galary;

import javax.swing.*;
import java.io.*;

public class PictureFrame extends javax.swing.JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Creates new form PictureFrame */
	public PictureFrame() {
		initComponents();
	}

	private void initComponents() {
		jPanel2 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		jButton1 = new javax.swing.JButton();
		jButton2 = new javax.swing.JButton();
		jMenuBar1 = new javax.swing.JMenuBar();
		jMenu1 = new javax.swing.JMenu();
		jMenuItem1 = new javax.swing.JMenuItem();
		jMenuItem2 = new javax.swing.JMenuItem();
		jMenuItem4 = new javax.swing.JMenuItem();
		jMenu2 = new javax.swing.JMenu();
		jMenuItem5 = new javax.swing.JMenuItem();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("java PictureView Manager");
		setBackground(new java.awt.Color(255, 255, 255));
		setBounds(new java.awt.Rectangle(200, 50, 500, 500));
		setMaximizedBounds(new java.awt.Rectangle(100, 200, 800, 800));
		setState(1);
		jPanel2.setEnabled(false);
		jLabel1.setIcon(new javax.swing.ImageIcon(
				"C:\\Java\\Wallpaper1349590065739.jpg"));
		jLabel1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1,
				1, new java.awt.Color(255, 0, 0)));

		jButton1.setText("\u7ee7\u7eed\u6d4f\u89c8");
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				JButton1Listener(evt);
			}
		});

		jButton2.setText("\u53d6\u6d88\u6d4f\u89c8");
		jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				jButton2MouseClicked(evt);
			}
		});

		javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(
				jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout.setHorizontalGroup(jPanel2Layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE,
						417, Short.MAX_VALUE)
				.addGroup(
						jPanel2Layout.createSequentialGroup()
								.addGap(88, 88, 88).addComponent(jButton1)
								.addGap(60, 60, 60).addComponent(jButton2)
								.addContainerGap(107, Short.MAX_VALUE)));
		jPanel2Layout
				.setVerticalGroup(jPanel2Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel2Layout
										.createSequentialGroup()
										.addComponent(
												jLabel1,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												559, Short.MAX_VALUE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												jPanel2Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(jButton1)
														.addComponent(jButton2))
										.addContainerGap()));

		jMenu1.setText("\u6587\u4ef6(F)");
		jMenuItem1.setText("\u65b0\u5efa(N)");
		jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				MenuItem1Listener(evt);
			}
		});

		jMenu1.add(jMenuItem1);

		jMenuItem2.setText("\u6253\u5f00(O)");
		jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				MenuItem2Listener(evt);
			}
		});

		jMenu1.add(jMenuItem2);

		jMenuItem4.setText("\u9000\u51fa(E)");
		jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				MenuItem4Listener(evt);
			}
		});

		jMenu1.add(jMenuItem4);

		jMenuBar1.add(jMenu1);

		jMenu2.setText("\u5e2e\u52a9(H)");
		jMenuItem5.setText("\u5173\u4e8e(about)");
		jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				aboutListener(evt);
			}
		});

		jMenu2.add(jMenuItem5);
		jMenuBar1.add(jMenu2);

		setJMenuBar(jMenuBar1);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup()
						.addComponent(jPanel2,
								javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(15, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup()
						.addComponent(jPanel2,
								javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(42, Short.MAX_VALUE)));
		pack();
	}// </editor-fold>

	private void aboutListener(java.awt.event.ActionEvent evt) {
		JOptionPane.showMessageDialog(jMenuItem5, "嗖嗖嗖,谢谢大家学习使用",
				"PictureView", JOptionPane.YES_NO_OPTION);
	}

	private void MenuItem4Listener(java.awt.event.ActionEvent evt) {
		// TODO 将在此处添加您的处理代码：
		System.exit(1);
	}

	private void MenuItem2Listener(java.awt.event.ActionEvent evt) {
		// TODO 将在此处添加您的处理代码：
		JFileChooser chooser = new JFileChooser();
		filename = chooser.getSelectedFile().getName();
		openPath = chooser.getCurrentDirectory().getPath();
		System.out.println(openPath);
		ImageIcon imag = new ImageIcon(openPath + "\\" + filename);
		jLabel1.setIcon(imag);
	}

	private void MenuItem1Listener(java.awt.event.ActionEvent evt) {
		// TODO 将在此处添加您的处理代码：
		ImageIcon imag = new ImageIcon();
		jLabel1.setIcon(imag);
	}

	private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {
		System.exit(1);
	}

	private void JButton1Listener(java.awt.event.ActionEvent evt) {
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
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new PictureFrame().setVisible(true);
			}
		});
	}

	// 变量声明 - 不进行修改
	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButton2;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JMenu jMenu1;
	private javax.swing.JMenu jMenu2;
	private javax.swing.JMenuBar jMenuBar1;
	private javax.swing.JMenuItem jMenuItem1;
	private javax.swing.JMenuItem jMenuItem2;
	private javax.swing.JMenuItem jMenuItem4;
	private javax.swing.JMenuItem jMenuItem5;
	private javax.swing.JPanel jPanel2;
	private String filename;
	private String openPath;
	private static int i = 0;
	// 变量声明结束

}
