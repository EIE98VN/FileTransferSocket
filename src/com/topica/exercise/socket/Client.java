package com.topica.exercise.socket;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
    private static final int PORT = 1111;
    private static final String IP_ADDRESS = "127.0.0.1";
    private static final String INPUT_PATH = "./testvideo.mp4";

    public static void main(String[] args) {
        Socket socket = null;
        FileInputStream fis = null;
        OutputStream os = null;
        long totalSent = 0;
        long time = 0;
        byte[] buffer = new byte[256 * 1024];
        int bytesRead = 0;
        try {
            socket = new Socket(IP_ADDRESS, PORT);
            System.out.println("Connected to socket " + socket);

            os = socket.getOutputStream();
            fis = new FileInputStream(INPUT_PATH);

            // send file name to host first
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeUTF(INPUT_PATH);

            time = System.currentTimeMillis();
            while ((bytesRead = fis.read(buffer)) != -1) {
                if (bytesRead > 0) {
                    os.write(buffer, 0, bytesRead);
                    totalSent += bytesRead;
                    System.out.println("sent " + totalSent);
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("Can not transfer file !~");
        } catch (IOException e) {
            System.out.println("Connection refused !");
        } finally {
            try {
                if (socket != null)
                    socket.close();
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        long duration = System.currentTimeMillis() - time;
        System.out.println("Sent total " + totalSent + " bytes in " + duration + "ms " + "at average speed "
                + totalSent * 1000 / 1024 / 1024 / duration + "Mb/s");
    }
}
