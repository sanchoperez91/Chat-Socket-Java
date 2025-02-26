import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.*;
import java.net.*;
import javax.swing.*;


public class Cliente {

    public static void main(String[] args) {
        new MarcoCliente();
    }
}

class MarcoCliente extends JFrame {
    private JTextArea areatexto;
    private JTextField campo1;

    public MarcoCliente() {
        setTitle("Cliente");
        setBounds(600, 300, 300, 350);
        areatexto = new JTextArea();
        areatexto.setEditable(false);
        areatexto.setForeground(new Color(60, 63, 65)); 
        areatexto.setBackground(new Color(230, 230, 250));
        areatexto.setFont(new Font("Serif", Font.PLAIN, 16));
        campo1 = new JTextField(20);
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
        
        String conexion = "Cliente conectado"; //confirmamos la conexion
            areatexto.append("\n" +conexion);
        new Thread(this::escucharServidor).start();
    }

    private void enviarTexto() {
        try (Socket socket = new Socket("localhost", 9123);
             DataOutputStream salida = new DataOutputStream(socket.getOutputStream())) {
            salida.writeUTF(campo1.getText());
            areatexto.append("\nCliente: " + campo1.getText());
            campo1.setText("");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void escucharServidor() {
        try (ServerSocket serverSocket = new ServerSocket(9222)) {
            while (true) {
                try (Socket socket = serverSocket.accept();
                     DataInputStream entrada = new DataInputStream(socket.getInputStream())) {
                    areatexto.append("\nServidor: " + entrada.readUTF());
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}