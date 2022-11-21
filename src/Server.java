import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Server extends Thread {
    private ServerSocket serverSocket;
    Socket server;

    static JFrame frame = new JFrame("Client Screen");
    static JLabel label = new JLabel();
    //static boolean fullscreen = false;

    static JFrame frame2 = new JFrame("Client Screen2");
    static JLabel label2 = new JLabel();
    private ServerSocket serverSocket2;
    Socket server2;

    public Server(int port) throws IOException, SQLException, ClassNotFoundException, Exception {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(86400000);
        serverSocket2 = new ServerSocket(port+1);
        serverSocket2.setSoTimeout(86400000);
    }
    public void run() {
        while(true) {
            try {
                server = serverSocket.accept();
                BufferedImage img=ImageIO.read(ImageIO.createImageInputStream(server.getInputStream()));
                label.setIcon(new ImageIcon(img));
                label.setLocation(0,0);
                frame.getContentPane().add(label);
                //frame.pack();
                server2 = serverSocket2.accept();
                BufferedImage img2=ImageIO.read(ImageIO.createImageInputStream(server2.getInputStream()));
                label2.setIcon(new ImageIcon(img2));
                label2.setLocation(0,0);
                frame2.getContentPane().add(label2);
                Thread.sleep((long) 16.66);
            } catch(SocketTimeoutException st) {
                System.out.println("Socket timed out!");
                break;
            } catch(IOException e) {
                e.printStackTrace();
                break;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void main(String [] args) throws IOException, SQLException, ClassNotFoundException, Exception {
        Thread t = new Server(6066);
        t.start();
        frame2.setLocation(2500,400);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // delete if needed
        Server.frame.setUndecorated(true); // delete if needed
        frame.setVisible(true);
        frame2.setExtendedState(JFrame.MAXIMIZED_BOTH); // delete if needed
        Server.frame2.setUndecorated(true); // delete if needed
        frame2.setVisible(true);
        ServerTwo serverTwo = new ServerTwo(6065);
    }
}