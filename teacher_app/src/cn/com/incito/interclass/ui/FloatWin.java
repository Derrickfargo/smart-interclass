package cn.com.incito.interclass.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import javax.swing.*;

import cn.com.incito.interclass.Listener.MySystemTrayEvent;
import cn.com.incito.server.core.CoreSocket;
import cn.com.incito.server.utils.Pic;

public class FloatWin implements MouseListener,
        MouseMotionListener {
    // private ServerSocket server = null;
    private MySystemTrayEvent mySystemTrayEvent;
    private Toolkit tool;
    private static int WIDTH = 100;
    private static int HEIGHT = 100;
    private JPanel jp;
    private int x, y;
    private MainFrame mainFrame;
    private TrayPopMenu popupMenu;
    private static FloatWin instance;
    private JDialog dialog = new JDialog();

    enum X {
        LEFT, RIGHT
    }

    enum Y {
        UP, DOWN
    }

    public static FloatWin getInstance() {
        if (instance == null) {
            instance = new FloatWin();
        }
        return instance;
    }

    public FloatWin() {
//        if (instanceIsRunning() && mainFrame != null) {
//            mySystemTrayEvent.openHide(mainFrame);
//        } else {
            mainFrame = MainFrame.getInstance();
            mainFrame.setVisible(true);
            this.mySystemTrayEvent = mainFrame.getMySystemTrayEvent();
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            initComponent();
//        }
    }

    private void initComponent() {
        jp = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(Pic.crateIcon("top.png").getImage(), 0, 0, this);
            }
        };
        jp.setOpaque(true);
        dialog.setSize(36, 36);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        dialog.setLocation((int) dimension.getWidth() - 84, 25);
        dialog.setLayout(new BorderLayout());
        dialog.add(jp, BorderLayout.CENTER);
        dialog.setUndecorated(true);
        dialog.setAlwaysOnTop(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.addMouseListener(this);
        dialog.addMouseMotionListener(this);
        dialog.setVisible(true);

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int x1 = e.getXOnScreen();
        int y1 = e.getYOnScreen();
        dialog.setLocation(x1 - x, y1 - y);

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int mods = e.getModifiers();
        if ((mods & InputEvent.BUTTON3_MASK) != 0) {
            // 弹出菜单
            popupMenu = new TrayPopMenu();
            popupMenu.show(dialog, e.getX(), e.getY());
        } else if (e.getClickCount() % 2 == 0) {
            mySystemTrayEvent.openHide(mainFrame);
        }

        // 鼠标右键
    }

    @Override
    public void mousePressed(MouseEvent e) {
        x = e.getX();
        y = e.getY();

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


}
