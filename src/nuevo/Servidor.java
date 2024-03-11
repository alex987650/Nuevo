/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nuevo;
import java.io.*;
import java.net.*;
import java.nio.file.Paths;

public class Servidor {

    public static void main(String[] args) {
        final int PUERTO = 5000;

        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            System.out.println("Esperando conexión en el puerto " + PUERTO + "...");

            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    System.out.println("Cliente conectado desde " + socket.getInetAddress());

                    // Recibir el archivo y guardar en la carpeta "archivos"
                    recibirArchivo(socket);

                    System.out.println("Transferencia completada");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void recibirArchivo(Socket socket) throws IOException {
        try (InputStream inputStream = socket.getInputStream();
             DataInputStream dataInputStream = new DataInputStream(inputStream)) {

            // Recibe el nombre del archivo
            String nombreArchivo = dataInputStream.readUTF();
            String rutaCompleta = Paths.get("archivos", nombreArchivo).toString();

            try (FileOutputStream fileOutputStream = new FileOutputStream(rutaCompleta)) {
                byte[] buffer = new byte[1024];
                int bytesRead;

                // Leer datos del socket y escribir en el archivo
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }

                System.out.println("Archivo recibido correctamente: " + nombreArchivo);
                System.out.println("Archivo se guardará en: " + rutaCompleta);
            }
        }
    }
}
