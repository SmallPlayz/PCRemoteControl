import java.awt.AWTException;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;

import javax.imageio.ImageIO;

public class Client extends Thread {

    private static Socket clientSocket = null;
    public static PrintStream os = null;
    private static DataInputStream is = null;
    public static void main(String[] args) {
        String serverName = "localhost";
        int port = 6066, port2 = 6065;
        try{
            clientSocket = new Socket(serverName, port2);
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
            Robot robot = new Robot();
            while ((responseLine = is.readLine()) != null) {
                System.out.println(responseLine);

                if(responseLine.substring(0, 8).equals("Keycode:"))
                    System.out.println("heuy");

                switch (responseLine) {
                    case "Left Click" -> robot.mousePress(InputEvent.BUTTON1_MASK);
                    case "Left Click Release" -> robot.mouseRelease(InputEvent.BUTTON1_MASK);
                    case "Middle Click" -> robot.mousePress(InputEvent.BUTTON2_MASK);
                    case "Middle Click Release" -> robot.mouseRelease(InputEvent.BUTTON2_MASK);
                    case "Right Click" -> robot.mousePress(InputEvent.BUTTON3_MASK);
                    case "Right Click Release" -> robot.mouseRelease(InputEvent.BUTTON3_MASK);
                    case "Enter" -> {robot.keyPress(KeyEvent.VK_ENTER);robot.keyRelease(KeyEvent.VK_ENTER);System.out.println("hii");}
                    case "Shift" -> {robot.keyPress(16);robot.keyRelease(16);}
                    case "Caps Lock" -> {robot.keyPress(20);robot.keyRelease(20);}
                    case "Control" -> {robot.keyPress(17);robot.keyRelease(17);}
                    case "Alt" -> {robot.keyPress(18);robot.keyRelease(18);}
                    case "Back Space" -> {robot.keyPress(8);robot.keyRelease(8);}
                    case "Escape" -> {robot.keyPress(27);robot.keyRelease(27);}
                    case "Up Arrow" -> {robot.keyPress(38);robot.keyRelease(38);}
                    case "Down Arrow" -> {robot.keyPress(40);robot.keyRelease(40);}
                    case "Left Arrow" -> {robot.keyPress(37);robot.keyRelease(37);}
                    case "Right Arrow" -> {robot.keyPress(39);robot.keyRelease(39);}
                    case "F6" -> {robot.keyPress(117);robot.keyRelease(117);}
                    case "F7" -> {robot.keyPress(118);robot.keyRelease(118);}
                    case "F8" -> {robot.keyPress(119);robot.keyRelease(119);}
                }
            }
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }
}