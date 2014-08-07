package cn.com.incito.interclass.ui;

import cn.com.incito.interclass.Listener.MySystemTrayEvent;
import cn.com.incito.server.config.BaseConfig;
import cn.com.incito.server.utils.Pic;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class FloatWin extends JDialog implements MouseListener,
        MouseMotionListener {
    //	private ServerSocket server = null;
    private MySystemTrayEvent mySystemTrayEvent;
    private Toolkit tool;
    private static int WIDTH = 100;
    private static int HEIGHT = 100;
    private JPanel jp;
    private int x, y;
    private MainFrame serverFrame;

    enum X {
        LEFT, RIGHT
    }
    enum Y {
        UP, DOWN
    }

    public FloatWin() {
        serverFrame = MainFrame.getInstance();
        serverFrame.setVisible(true);

        this.mySystemTrayEvent = serverFrame.getMySystemTrayEvent();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        initComponent();

    }


    private void initComponent() {
        // jPopupMenu = new MyJPopupMenu();
        jp = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(Pic.crateIcon("top.png").getImage(), 0, 0, this);
            }
        };
        jp.setOpaque(true);
        this.setSize(36, 36);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((int) dimension.getWidth() - 84, 25);
        this.setLayout(new BorderLayout());
        this.add(jp, BorderLayout.CENTER);
        this.setUndecorated(true);
        this.setAlwaysOnTop(true);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.setVisible(true);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int x1 = e.getXOnScreen();
        int y1 = e.getYOnScreen();
        this.setLocation(x1 - x, y1 - y);

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() % 2 == 0) {
            mySystemTrayEvent.openHide(serverFrame.getFrame());
        }

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
