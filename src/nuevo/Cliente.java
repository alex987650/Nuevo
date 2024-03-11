/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nuevo;

/**
 *
 * @author aleja
 */
import java.io.*;
import java.net.*;
import javax.swing.JFileChooser;

public class Cliente {

    public static void main(String[] args) {
        final String HOST = "192.168.1.66";
        final int PUERTO = 5000;

        try {
            System.out.println("Directorio actual: " + System.getProperty("user.dir"));

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
            int result = fileChooser.showOpenDialog(null);
            if (result != JFileChooser.APPROVE_OPTION) {
                return;
            }

            File selectedFile = fileChooser.getSelectedFile();

            try (Socket sc = new Socket(HOST, PUERTO);
                 DataInputStream in = new DataInputStream(sc.getInputStream());
                 DataOutputStream out = new DataOutputStream(sc.getOutputStream());
                 BufferedInputStream bis = new BufferedInputStream(new FileInputStream(selectedFile));
                 BufferedOutputStream bos = new BufferedOutputStream(sc.getOutputStream())) {

                // Envía solo el nombre del archivo al servidor
                out.writeUTF(selectedFile.getName());

                // Envía el contenido del archivo al servidor
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = bis.read(buffer)) != -1) {
                    bos.write(buffer, 0, bytesRead);
                }

                // Recibe mensaje de confirmación del servidor
                String mensaje = in.readUTF();
                System.out.println(mensaje);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
