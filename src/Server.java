import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Server extends Thread
{
    private ServerSocket serverSocket;
    Socket server;

    static JFrame frame = new JFrame("Client Screen");
    static JLabel label = new JLabel();

    public Server(int port) throws IOException, SQLException, ClassNotFoundException, Exception
    {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(3600000);
    }

    public void run()
    {
        while(true)
        {
            try
            {
                server = serverSocket.accept();
                BufferedImage img=ImageIO.read(ImageIO.createImageInputStream(server.getInputStream()));
                label.setIcon(new ImageIcon(img));
                frame.getContentPane().add(label);
                frame.pack();
                Thread.sleep((long) 16.66);
            }
            catch(SocketTimeoutException st)
            {
                System.out.println("Socket timed out!");
                break;
            }
            catch(IOException e)
            {
                e.printStackTrace();
                break;
            }
            catch(Exception ex)
            {
                System.out.println(ex);
            }
        }
    }

    public static void main(String [] args) throws IOException, SQLException, ClassNotFoundException, Exception
    {
        Thread t = new Server(6066);
        t.start();
        frame.addMouseListener(new MouseListener() {
            public void mousePressed(MouseEvent me) { }
            public void mouseReleased(MouseEvent me) { }
            public void mouseEntered(MouseEvent me) { }
            public void mouseExited(MouseEvent me) { }
            public void mouseClicked(MouseEvent me) {
                if (me.getButton() == MouseEvent.BUTTON1) {
                    System.out.println("left click");
                }
                if (me.getButton() == MouseEvent.BUTTON2) {
                    System.out.println("middle click");
                }
                if (me.getButton() == MouseEvent.BUTTON3) {
                    System.out.println("right? click");
                }
            }
        });
        frame.setVisible(true);
        System.out.println("Server running...");
    }
}