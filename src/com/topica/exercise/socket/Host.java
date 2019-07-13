package com.topica.exercise.socket;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Host {

    private static final int PORT = 1111;

    public static void main(String[] args) {
        File file = null;
        ServerSocket server = null;
        byte[] buffer = new byte[256 * 1024];
        int bytesRead = 0;

        InputStream is = null;
        FileOutputStream fos = null;

        try {
            System.out.println("Binding to port " + PORT + ", please wait  ...");
            server = new ServerSocket(PORT);
            System.out.println("Server started: " + server);
            System.out.println("Waiting for a client ...");

            try {
                Socket client = server.accept();
                System.out.println("Accept a client: " + client);

                is = client.getInputStream();

                // get file name from client
                DataInputStream in = new DataInputStream(is);
                String fileName = in.readUTF();
                fileName = (new StringBuffer()).append("ouput").append(getFileExtension(fileName)).toString();
                file = new File(fileName);
                fos = new FileOutputStream(file);

                while ((bytesRead = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }

            } catch (FileNotFoundException e) {
                System.out.println("FIle not found !~");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (server != null)
                    server.close();
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getFileExtension(String fileName) {
        int index = 0;
        for (int i = fileName.length() - 1; i >= 0; i--) {
            if (fileName.charAt(i) == '.') {
                index = i;
                break;
            }
        }
        return fileName.substring(index, fileName.length());
    }
}
