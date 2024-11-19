package MedicalChat.app.servers.clients;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class DoctorClient extends JFrame {

    private static final String SERVER_ADDRESS = "localhost"; // Dirección del servidor
    private static final int SERVER_PORT = 8080;             // Puerto del servidor

    private JTextArea mensajesChat;
    private JTextField tfMensaje;
    private JButton btEnviar;
    private Socket socket;
    private DataOutputStream salida;
    private DataInputStream entrada;
    private boolean conectado = true;

    public DoctorClient() {
        super("Doctor - Cliente Chat");

        // Configuración de la ventana
        mensajesChat = new JTextArea();
        mensajesChat.setEditable(false); // El área de mensajes no debe ser editable
        mensajesChat.setLineWrap(true);
        mensajesChat.setWrapStyleWord(true);
        JScrollPane scrollMensajesChat = new JScrollPane(mensajesChat);

        tfMensaje = new JTextField();
        btEnviar = new JButton("Enviar");

        Container c = this.getContentPane();
        c.setLayout(new BorderLayout());

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(tfMensaje, BorderLayout.CENTER);
        panelInferior.add(btEnviar, BorderLayout.EAST);

        c.add(scrollMensajesChat, BorderLayout.CENTER);
        c.add(panelInferior, BorderLayout.SOUTH);

        this.setSize(500, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        inicializarConexion();

        // Acción del botón enviar
        btEnviar.addActionListener(e -> enviarMensaje());

        // Acción al presionar Enter en el campo de texto
        tfMensaje.addActionListener(e -> enviarMensaje());

        // Inicia el hilo para recibir mensajes
        new Thread(this::recibirMensajesServidor).start();
    }

    private void inicializarConexion() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            salida = new DataOutputStream(socket.getOutputStream());
            entrada = new DataInputStream(socket.getInputStream());

            // Identificación del cliente como "DOCTOR"
            salida.writeUTF("DOCTOR");

            String bienvenida = entrada.readUTF();
            mensajesChat.append("Servidor: " + bienvenida + "\n");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al conectar con el servidor: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            conectado = false;
        }
    }

    private void enviarMensaje() {
        String mensaje = tfMensaje.getText().trim();
        if (!mensaje.isEmpty() && conectado) {
            try {
                salida.writeUTF(mensaje);
                mensajesChat.append("Tú: " + mensaje + "\n");
                tfMensaje.setText("");
            } catch (IOException e) {
                mensajesChat.append("Error al enviar el mensaje: " + e.getMessage() + "\n");
                conectado = false;
            }
        }
    }

    private void recibirMensajesServidor() {
        try {
            while (conectado) {
                String mensaje = entrada.readUTF();
                mensajesChat.append("Paciente: " + mensaje + "\n");
            }
        } catch (IOException e) {
            mensajesChat.append("Conexión cerrada: " + e.getMessage() + "\n");
            conectado = false;
        } finally {
            cerrarConexion();
        }
    }

    private void cerrarConexion() {
        try {
            if (entrada != null) entrada.close();
            if (salida != null) salida.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            mensajesChat.append("Error al cerrar la conexión: " + e.getMessage() + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DoctorClient::new);
    }
}
