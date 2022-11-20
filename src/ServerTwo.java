import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.UnknownHostException;

public class ServerTwo {

    private static ServerSocket serverSocket = null;
    private static Socket clientSocket = null;
    private static final int maxClientsCount = 10;
    private static int portNumber;

    private static final clientThread[] threads = new clientThread[maxClientsCount];

    ServerTwo(int pnumber)/* public static void main(String[] args) */{
        portNumber = pnumber;
        iport();
        try {
            serverSocket = new ServerSocket(portNumber);
            int i = 0;
            Server.frame.setVisible(true);
            System.out.println("Server running...");
            while (true) {
                clientSocket = serverSocket.accept();
                for (i = 0; i < maxClientsCount; i++) {
                    if (threads[i] == null) {
                        (threads[i] = new clientThread(clientSocket, threads)).start();
                        break;
                    }
                }
                if (i == maxClientsCount) {
                    PrintStream os = new PrintStream(clientSocket.getOutputStream());
                    os.println("Server too busy. Try later.");
                    os.close();
                    clientSocket.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void iport(){
        InetAddress ip;
        String hostname;
        try {
            ip = InetAddress.getLocalHost();
            hostname = ip.getHostName();
            System.out.println("Your current IP address : " + ip);
            System.out.println("Your current Hostname : " + hostname);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
class clientThread extends Thread {
    private DataInputStream is = null;
    private PrintStream os = null;
    private Socket clientSocket = null;
    private final clientThread[] threads;
    private final int maxClientsCount;

    public clientThread(Socket clientSocket, clientThread[] threads) {
        this.clientSocket = clientSocket;
        this.threads = threads;
        maxClientsCount = threads.length;
    }
    public void run(){
        int maxClientsCount = this.maxClientsCount;
        clientThread[] threads = this.threads;

        try {
            is = new DataInputStream(clientSocket.getInputStream());
            os = new PrintStream(clientSocket.getOutputStream());

            class Keychecker extends KeyAdapter {

                @Override
                public void keyPressed(KeyEvent event) {
                    //os.println(event.getKeyChar());
                    KeyStroke ks = KeyStroke.getKeyStroke(event.getKeyChar(), 0);
                    if(ks.getKeyCode() < 1000) os.println("Keycode:" + ks.getKeyCode());
                }
            }

            Server.frame.addKeyListener(new Keychecker());
            Server.frame.addMouseListener(new MouseListener() {
                public void mousePressed(MouseEvent me) {
                    if (me.getButton() == MouseEvent.BUTTON1) {
                        os.println("Left Click");
                    }
                    else if (me.getButton() == MouseEvent.BUTTON2) {
                        os.println("Middle Click");
                    }
                    else if (me.getButton() == MouseEvent.BUTTON3) {
                        os.println("Right Click");
                    }
                }
                public void mouseReleased(MouseEvent me) {
                    if (me.getButton() == MouseEvent.BUTTON1) {
                        os.println("Left Click Release");
                    }
                    else if (me.getButton() == MouseEvent.BUTTON2) {
                        os.println("Middle Click Release");
                    }
                    else if (me.getButton() == MouseEvent.BUTTON3) {
                        os.println("Right Click Release");
                    }
                }
                public void mouseEntered(MouseEvent me) { }
                public void mouseExited(MouseEvent me) { }
                public void mouseClicked(MouseEvent me) { }
            });

            Server.frame.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if(e.getKeyCode() == KeyEvent.VK_ENTER)
                        os.println("Enter");
                    else if(e.getKeyCode() == KeyEvent.VK_SHIFT)
                        os.println("Shift");
                    else if(e.getKeyCode() == KeyEvent.VK_CAPS_LOCK)
                        os.println("Caps Lock");
                    else if(e.getKeyCode() == KeyEvent.VK_CONTROL)
                        os.println("Control");
                    else if(e.getKeyCode() == KeyEvent.VK_ALT)
                        os.println("Alt");
                    else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
                        os.println("Back Space");
                    else if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
                        os.println("Escape");
                    else if(e.getKeyCode() == KeyEvent.VK_UP)
                        os.println("Up Arrow");
                    else if(e.getKeyCode() == KeyEvent.VK_DOWN)
                        os.println("Down Arrow");
                    else if(e.getKeyCode() == KeyEvent.VK_LEFT)
                        os.println("Left Arrow");
                    else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
                        os.println("Right Arrow");
                    else if(e.getKeyCode() == KeyEvent.VK_F6)
                        os.println("F6");
                    else if(e.getKeyCode() == KeyEvent.VK_F7)
                        os.println("F7");
                    else if(e.getKeyCode() == KeyEvent.VK_F8)
                        os.println("F8");
                }
            });

            for (int i = 0; i < maxClientsCount; i++) {
                if (threads[i] != null && threads[i] != this) {
                    threads[i].os.println("client has connected to the server!");
                }
            }
            while (true) {
                String line = is.readLine();
                System.out.println(line);
                os.println(line);
                if (line.startsWith("/exit")) {
                    break;
                }
                for (int i = 0; i < maxClientsCount; i++) {
                    if (threads[i] != null) {
                        threads[i].os.println(line);
                    }
                }
            }
            for (int i = 0; i < maxClientsCount; i++) {
                if (threads[i] != null && threads[i] != this) {
                    threads[i].os.println("client is disconnecting from the server.");
                }
            }

            for (int i = 0; i < maxClientsCount; i++) {
                if (threads[i] == this) {
                    threads[i] = null;
                }
            }
            is.close();
            os.close();
            clientSocket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}