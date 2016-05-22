
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.ImageHelper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.ImageObserver;
import java.awt.image.Raster;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Main {
    public static void main(String[] args) {
        Tesseract instance = new Tesseract();
        instance.setLanguage("eng");
        try {
            BufferedImage image = ImageIO.read(new File("E:\\9.jpg"));
            int h = image.getHeight();
            int w = image.getWidth();
            int arr[][] = new int[w][h];
            // 获取图片每一像素点的灰度值
            for (int i = 0; i < w; i++) {
                for (int j = 0; j < h; j++) {
                    // getRGB()返回默认的RGB颜色模型(十进制)
                    arr[i][j] = getGray(image.getRGB(i, j));//该点的灰度值
                }
            }
//            int[] dataBuffInt = image.getRGB(0, 0, w, h, null, 0, w);
//            RGBLuminanceSource rgb = new RGBLuminanceSource(w, h, dataBuffInt);
//            GlobalHistogramBinarizer binarizer = new GlobalHistogramBinarizer(rgb);
//            BitMatrix matrix = binarizer.getBlackMatrix();
//            int[] data = matrix.getBits();getBits
//            BufferedImage textImage = ImageHelper.convertImageToGrayscale(ImageHelper.getSubImage(image, 0, 0, w, h));
//            textImage = ImageHelper.getScaledInstance(textImage, w * 5,  h * 5);
//            BufferedImage var1 = new BufferedImage(var0.getWidth(), var0.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
//            Graphics2D var2 = var1.createGraphics();
//            var2.drawImage(var0, 0, 0, (ImageObserver)null);
//            var2.dispose();


//            ByteBuffer byteBuffer = ByteBuffer.allocate(data.length * 4);
//            IntBuffer intBuffer = byteBuffer.asIntBuffer();
//            intBuffer.put(data);
//
//            System.out.println("[" + w + " - " + h + "(" + w*h);
//            byte[] array = byteBuffer.array();
//            BufferedImage img =new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
//            img.setData(Raster.createRaster(img.getSampleModel(), new DataBufferByte(array, array.length), new Point()));
            BufferedImage grayImage = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY);//重点，技巧在这个参数BufferedImage.TYPE_BYTE_BINARY
            for (int i = 0; i < w; i++) {
                for (int j = 0; j < h; j++) {
//                    int rgb = image.getRGB(i, j);
//                    grayImage.setRGB(i, j, rgb);
                    if (getAverageColor(arr, i, j, w, h) > 80) {
                        int white = new Color(255, 255, 255).getRGB();
                        grayImage.setRGB(i, j, white);
                    } else {
                        int black = new Color(0, 0, 0).getRGB();
                        grayImage.setRGB(i, j, black);
                    }
                }
            }
            File newFile = new File("e:\\8.jpg");
            ImageIO.write(grayImage, "jpg", newFile);

            String s = instance.doOCR(grayImage);
            System.out.println("[" + s.trim() + "----[" + parser(s.trim()) + "]");
        } catch (TesseractException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getGray(int rgb) {
        String str = Integer.toHexString(rgb);
        int r = Integer.parseInt(str.substring(2, 4), 16);
        int g = Integer.parseInt(str.substring(4, 6), 16);
        int b = Integer.parseInt(str.substring(6, 8), 16);
        //or 直接new个color对象
        Color c = new Color(rgb);
        r = c.getRed();
        g = c.getGreen();
        b = c.getBlue();
        int top = (r + g + b) / 3;
        return (int) (top);
    }

    public static String parser(String s) {
        StringBuilder sb = new StringBuilder();
        int next = 0;
        int prev = 0;
        for (char c : s.toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
            } else if (c >= '0' && c <= '9') {
            }
            else if (c >= 'a' && c <= 'z'){}
            else {
                next++;
                continue;
            }

//            System.out.println(c + " - " + prev + " - " + next);
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

    public static int getAverageColor(int[][] gray, int x, int y, int w, int h) {
        int rs = gray[x][y]
                + (x == 0 ? 255 : gray[x - 1][y])
                + (x == 0 || y == 0 ? 255 : gray[x - 1][y - 1])
                + (x == 0 || y == h - 1 ? 255 : gray[x - 1][y + 1])
                + (y == 0 ? 255 : gray[x][y - 1])
                + (y == h - 1 ? 255 : gray[x][y + 1])
                + (x == w - 1 ? 255 : gray[x + 1][y])
                + (x == w - 1 || y == 0 ? 255 : gray[x + 1][y - 1])
                + (x == w - 1 || y == h - 1 ? 255 : gray[x + 1][y + 1]);
        return rs / 9;
    }
}
