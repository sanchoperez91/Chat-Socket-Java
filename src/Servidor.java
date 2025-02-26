import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class Servidor {

    public static void main(String[] args) {
        new MarcoServidor();
    }
}

class MarcoServidor extends JFrame {
    private JTextArea areatexto;
    private JTextField campo1;

    public MarcoServidor() {
        setTitle("Servidor");
        setBounds(600, 300, 300, 350);
        areatexto = new JTextArea();
        areatexto.setEditable(false);
        
        areatexto.setForeground(new Color(60, 63, 65)); 
        areatexto.setBackground(new Color(240, 248, 255)); 
        areatexto.setFont(new Font("Serif", Font.PLAIN, 16));
        campo1 = new JTextField(20);
        
        campo1.setForeground(new Color(60, 63, 65)); 
        campo1.setBackground(new Color(230, 230, 250)); 
        JButton miboton = new JButton("Enviar");
        miboton.addActionListener(e -> enviarTexto());
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(areatexto), BorderLayout.CENTER);
        JPanel inputPanel = new JPanel();
        inputPanel.add(campo1);
        inputPanel.add(miboton);
        panel.add(inputPanel, BorderLayout.SOUTH);
        
        add(panel);
        setVisible(true);
        
        String conexion = "Servidor conectado"; //confirmamos la conexion
            areatexto.append("\n" +conexion);
        
        new Thread(this::escucharCliente).start();
    }

    private void enviarTexto() {
        try (Socket socket = new Socket("localhost", 9222);
             DataOutputStream salida = new DataOutputStream(socket.getOutputStream())) {
            salida.writeUTF(campo1.getText());
            areatexto.append("\nServidor: " + campo1.getText());
            campo1.setText("");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void escucharCliente() {
        try (ServerSocket serverSocket = new ServerSocket(9123)) {
            while (true) {
                try (Socket socket = serverSocket.accept();
                     DataInputStream entrada = new DataInputStream(socket.getInputStream())) {
                    areatexto.append("\nCliente: " + entrada.readUTF());
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
