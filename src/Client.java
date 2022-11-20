import java.awt.AWTException;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;

import javax.imageio.ImageIO;

public class Client extends Thread {

    private static Socket clientSocket = null;
    public static PrintStream os = null;
    private static DataInputStream is = null;
    private static BufferedReader inputLine = null;
    private static boolean closed = false;
    public static void main(String[] args) {
        String serverName = "localhost";
        int port = 6066, port2 = 6065;
        try{
            clientSocket = new Socket(serverName, port2);
            inputLine = new BufferedReader(new InputStreamReader(System.in));
            os = new PrintStream(clientSocket.getOutputStream());
            is = new DataInputStream(clientSocket.getInputStream());
            Client thread = new Client();
            thread.start();

            while(true) {
                Socket client = new Socket(serverName, port);
                Robot bot = new Robot();
                BufferedImage bimg = bot.createScreenCapture(new Rectangle(0, 0, 420, 420));
                ImageIO.write(bimg,"JPG",client.getOutputStream());
                client.close();
                Thread.sleep((long) 16.66);
            }
        } catch(IOException | AWTException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void run() {
        String responseLine;
        try {
            while ((responseLine = is.readLine()) != null) {
                System.out.println(responseLine);
            }
            closed = true;
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }
    }
}