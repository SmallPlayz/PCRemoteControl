import java.awt.AWTException;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Socket;

import javax.imageio.ImageIO;

public class Client
{
    public static void main(String [] args)
    {
        String serverName = "localhost";
        int port = 6066;
        try
        {
            while(true){
                Socket client = new Socket(serverName, port);
                Robot bot = new Robot();
                BufferedImage bimg = bot.createScreenCapture(new Rectangle(0, 0, 1280, 720));
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
}