package de.twimbee;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.Socket;

public class WriterThread implements Runnable {

    private final String host;
    private final int port;
    private final int offsetY;
    private final int offsetX;
    private final int xFrom;
    private final int xTo;
    private final int yFrom;
    private final int yTo;
    private final BufferedImage image;

    public WriterThread(String host, int port, int offsetX, int offsetY, int xFrom, int xTo, int yFrom, int yTo, BufferedImage image) {
        this.host = host;
        this.port = port;
        this.offsetY = offsetY;
        this.offsetX = offsetX;
        this.xFrom = xFrom;
        this.xTo = xTo;
        this.yFrom = yFrom;
        this.yTo = yTo;
        this.image = image;

        System.out.println("Thread initialized " + xFrom + " " + yFrom);
    }

    @Override
    public void run() {
        try {
            Socket server = new Socket(Inet4Address.getByName(host), port);
            PrintWriter out = new PrintWriter(server.getOutputStream());
            while (true) {
                for (int x = xFrom; x < xTo; x++) {
                    for (int y = yFrom; y < yTo; y++) {
                        writePixel(out, x + offsetX, y + offsetY, new Color(image.getRGB(x, y)));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writePixel(PrintWriter out, int x, int y, Color color) {
        String hexColor = Integer.toHexString(color.getRGB());
        if (!"ff000000".equals(hexColor)) {
            String colorString = hexColor.substring(2) + hexColor.substring(0, 2);
            String cmd = "PX " + x + " " + y + " " + colorString;
            //System.out.println(cmd);
            out.println(cmd);
        }
    }
}
