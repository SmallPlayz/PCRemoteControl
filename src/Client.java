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
import javax.swing.*;

public class Client extends Thread {

    private static Socket clientSocket = null;
    public static PrintStream os = null;
    private static DataInputStream is = null;

    private static Socket clientSocket2 = null;
    public static PrintStream os2 = null;
    private static DataInputStream is2 = null;
    public static void main(String[] args) {

        JFrame frame = new JFrame("E");
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        frame.setVisible(true);

        String serverName = "10.10.3.242";
        int port = 6066, port2 = 6065, port3 = 6067;
        try{
            clientSocket = new Socket(serverName, port);
            os = new PrintStream(clientSocket.getOutputStream());
            is = new DataInputStream(clientSocket.getInputStream());

            clientSocket2 = new Socket(serverName, port3);
            os2 = new PrintStream(clientSocket2.getOutputStream());
            is2 = new DataInputStream(clientSocket2.getInputStream());

            Client thread = new Client();
            thread.start();

            while(true) {
                Socket client = new Socket(serverName, port);
                Robot bot = new Robot();
                BufferedImage bimg = bot.createScreenCapture(new Rectangle(0, 0, 1600, 900));
                ImageIO.write(bimg,"JPG",client.getOutputStream());
                client.close();

                Socket client2 = new Socket(serverName, port3);
                Robot bot2 = new Robot();
                BufferedImage bimg2 = bot2.createScreenCapture(new Rectangle(1600, 900, 1600, 900));
                ImageIO.write(bimg2,"JPG",client2.getOutputStream());
                client2.close();
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

                if(responseLine.length()>9 && responseLine.substring(0, 8).equals("Keycode:")){
                    robot.keyPress(Integer.parseInt(responseLine.substring(8)));
                    robot.keyRelease(Integer.parseInt(responseLine.substring(8)));
                }else if(responseLine.contains("|")){
                    robot.mouseMove(Integer.parseInt(responseLine.substring(0, responseLine.indexOf("|"))), Integer.parseInt(responseLine.substring(responseLine.indexOf("|")+1)));
                }else{
                    switch (responseLine) {
                        case "Left Click" -> robot.mousePress(InputEvent.BUTTON1_MASK);
                        case "Left Click Release" -> robot.mouseRelease(InputEvent.BUTTON1_MASK);
                        case "Middle Click" -> robot.mousePress(InputEvent.BUTTON2_MASK);
                        case "Middle Click Release" -> robot.mouseRelease(InputEvent.BUTTON2_MASK);
                        case "Right Click" -> robot.mousePress(InputEvent.BUTTON3_MASK);
                        case "Right Click Release" -> robot.mouseRelease(InputEvent.BUTTON3_MASK);
                        case "Enter" -> {robot.keyPress(KeyEvent.VK_ENTER);robot.keyRelease(KeyEvent.VK_ENTER);}
                        case "Shift" -> {robot.keyPress(KeyEvent.VK_SHIFT);robot.keyRelease(KeyEvent.VK_SHIFT);}
                        case "Caps Lock" -> {robot.keyPress(KeyEvent.VK_CAPS_LOCK);robot.keyRelease(KeyEvent.VK_CAPS_LOCK);}
                        case "Control" -> {robot.keyPress(KeyEvent.VK_CONTROL);robot.keyRelease(KeyEvent.VK_CONTROL);}
                        case "Alt" -> {robot.keyPress(KeyEvent.VK_ALT);robot.keyRelease(KeyEvent.VK_ALT);}
                        case "Back Space" -> {robot.keyPress(KeyEvent.VK_BACK_SPACE);robot.keyRelease(KeyEvent.VK_BACK_SPACE);}
                        case "Escape" -> {robot.keyPress(KeyEvent.VK_ESCAPE);robot.keyRelease(KeyEvent.VK_ESCAPE);}
                        case "Up Arrow" -> {robot.keyPress(KeyEvent.VK_UP);robot.keyRelease(KeyEvent.VK_UP);}
                        case "Down Arrow" -> {robot.keyPress(KeyEvent.VK_DOWN);robot.keyRelease(KeyEvent.VK_DOWN);}
                        case "Left Arrow" -> {robot.keyPress(KeyEvent.VK_LEFT);robot.keyRelease(KeyEvent.VK_LEFT);}
                        case "Right Arrow" -> {robot.keyPress(KeyEvent.VK_RIGHT);robot.keyRelease(KeyEvent.VK_RIGHT);}
                        case "F6" -> {robot.keyPress(KeyEvent.VK_F6);robot.keyRelease(KeyEvent.VK_F6);}
                        case "F7" -> {robot.keyPress(KeyEvent.VK_F7);robot.keyRelease(KeyEvent.VK_F7);}
                        case "F8" -> {robot.keyPress(KeyEvent.VK_F8);robot.keyRelease(KeyEvent.VK_F8);}
                        case "Windows" -> {robot.keyPress(KeyEvent.VK_WINDOWS);robot.keyRelease(KeyEvent.VK_WINDOWS);}
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }
}