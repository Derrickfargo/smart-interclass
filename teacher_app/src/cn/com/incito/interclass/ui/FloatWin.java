package cn.com.incito.interclass.ui;

import cn.com.incito.interclass.Listener.MySystemTrayEvent;
import cn.com.incito.server.config.BaseConfig;
import cn.com.incito.server.handler.TimeServerHandler;
import cn.com.incito.server.utils.Pic;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

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
        serverFrame.addLog("Server started success at port ");


        this.mySystemTrayEvent = serverFrame.getMySystemTrayEvent();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        initComponent();
//		try {
//			server = new ServerSocket(CoreSocket.);
//
//		} catch (IOException ex) {
//			ex.printStackTrace();
//		}
//		new MonitorThread().start();
//        CoreSocket.getInstance().start();
        IoAcceptor acceptor = new NioSocketAcceptor();

        // 设置Filter链
        acceptor.getFilterChain().addLast("logger", new LoggingFilter());
        // 协议解析，采用mina现成的UTF-8字符串处理方式
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));

        // 设置消息处理类（创建、关闭Session，可读可写等等，继承自接口IoHandler）
        acceptor.setHandler(new TimeServerHandler());
        // 设置接收缓存区大小
        acceptor.getSessionConfig().setReadBufferSize(2048);
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
        try {
            // 服务器开始监听
            acceptor.bind(new InetSocketAddress(BaseConfig.PORT));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//	class MonitorThread extends Thread {
//
//		@Override
//		public void run() {
//			try {
//				Socket socket = null;
//				while (true) {
//					socket = server.accept();
//					serverFrame.addLog("Server started success at port "
//							+ Utils.PORT);
//					new ServerHander(socket, serverFrame);
//				}
//			} catch (IOException ex) {
//				ex.printStackTrace();
//			}
//		}
//
//	}

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
            mySystemTrayEvent.openHide(serverFrame);
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
