package de.twimbee;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length != 5) {
            System.out.println("Usage:");
            System.out.println("java de.twimbee.Main Host Port PositionX PositionY ImagePath");
            System.out.println("Example:");
            System.out.println("java de.twimbee.Main 94.45.224.9 1337 500 230 /home/stefan/Downloads/raked-half.png");
        } else {
            String host = args[0];
            int port = Integer.parseInt(args[1]);
            int positionX = Integer.parseInt(args[2]);
            int positionY = Integer.parseInt(args[3]);

            BufferedImage image = ImageIO.read(new File(args[4]));
            int width = image.getWidth();
            int height = image.getHeight();

            int threads = Runtime.getRuntime().availableProcessors();
            System.out.println("Using " + threads + " threads");

            int widthPerThread = width / threads;

            for (int t = 0; t < threads; t++) {
                WriterThread writerThread = new WriterThread(host, port, positionX, positionY, t * widthPerThread, ((t + 1) * widthPerThread) - 1, 0, height, image);
                Thread thread = new Thread(writerThread, "Thread #" + (t + 1));
                thread.start();
                System.out.println(thread.getName() + " started");
            }
        }
    }
}
