import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.ImageHelper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Tesseract instance = new Tesseract();
        instance.setLanguage("eng");
        try {
            BufferedImage var0 = ImageIO.read(new File("E:\\3.jpg"));
//            int h = image.getHeight();
//            int w = image.getWidth();
//            BufferedImage textImage = ImageHelper.convertImageToGrayscale(ImageHelper.getSubImage(image, 0, 0, w, h));
//            textImage = ImageHelper.getScaledInstance(textImage, w * 5,  h * 5);
            BufferedImage var1 = new BufferedImage(var0.getWidth(), var0.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
            Graphics2D var2 = var1.createGraphics();
            var2.drawImage(var0, 0, 0, (ImageObserver)null);
            var2.dispose();

            String s = instance.doOCR(var1);
            System.out.println("[" + s.trim() + "----[" + parser(s.trim()) + "]");
        } catch (TesseractException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String parser(String s) {
        StringBuilder sb = new StringBuilder();
        int next = 0;
        int prev = 0;
        for (char c : s.toCharArray()) {
            if (c >= 'A' && c <= 'H') {
            } else if (c >= '0' && c <= '9') {
            } else {
                next++;
                continue;
            }

            System.out.println(c + " - " + prev + " - " + next);
            if ((next - prev) == 1) {
                sb.append(c);
                if (sb.length() == 9) break;
            } else {
                sb.setLength(0);
                sb.append(c);
            }

            prev = next;


            next++;
        }

        return sb.toString();
    }
}
