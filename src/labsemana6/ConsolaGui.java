
package labsemana6;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class ConsolaGui extends JFrame {

    
    private final Color COLOR_FONDO = new Color(12, 12, 12);
    private final Color COLOR_TEXTO = new Color(204, 204, 204);

    
    private JTextArea areaSalida;
    private JTextField campoEntrada;
    private JLabel lblPrompt;

    
    private EstadoConsola estado;
    private ComandosCarpetas carpetas;   
    private ComandosArchivos archivos;   
    private ComandosBusqueda busqueda;  

    
    private boolean modoEscritura;
    private boolean modoAgregar;
    private File archivoEscritura;
    private String textoEscrito;

    public ConsolaGui() {
        estado = new EstadoConsola();
        carpetas = new ComandosCarpetas(estado);
        archivos = new ComandosArchivos(estado);
        busqueda = new ComandosBusqueda(estado);

        modoEscritura = false;
        modoAgregar = false;
        archivoEscritura = null;
        textoEscrito = "";

        configurarVentana();
        crearComponentes();
        mostrarBienvenida();
    }

   // Diseno ventana
    private void configurarVentana() {
        setTitle("Consola de Comandos - Equipo 4");
        setSize(850, 520);
        setMinimumSize(new Dimension(700, 450));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_FONDO);
    }

    
    private Font crearFuente() {
        Font fuente = new Font("Consolas", Font.PLAIN, 15);
        if (fuente.getFamily().equals("Dialog")) {
            fuente = new Font(Font.MONOSPACED, Font.PLAIN, 15);
        }
        return fuente;
    }

    private void crearComponentes() {
        Font fuente = crearFuente();

      
        areaSalida = new JTextArea();
        areaSalida.setEditable(false);
        areaSalida.setBackground(COLOR_FONDO);
        areaSalida.setForeground(COLOR_TEXTO);
        areaSalida.setFont(fuente);
        areaSalida.setLineWrap(true);
        areaSalida.setBorder(BorderFactory.createEmptyBorder(8, 8, 0, 8));

        JScrollPane scroll = new JScrollPane(areaSalida);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(COLOR_FONDO);
        add(scroll, BorderLayout.CENTER);

       
        lblPrompt = new JLabel(estado.getPrompt());
        lblPrompt.setForeground(COLOR_TEXTO);
        lblPrompt.setFont(fuente);

        campoEntrada = new JTextField();
        campoEntrada.setBackground(COLOR_FONDO);
        campoEntrada.setForeground(COLOR_TEXTO);
        campoEntrada.setCaretColor(COLOR_TEXTO);
        campoEntrada.setFont(fuente);
        campoEntrada.setBorder(BorderFactory.createEmptyBorder());

        JPanel panelAbajo = new JPanel(new BorderLayout());
        panelAbajo.setBackground(COLOR_FONDO);
        panelAbajo.setBorder(BorderFactory.createEmptyBorder(4, 8, 8, 8));
        panelAbajo.add(lblPrompt, BorderLayout.WEST);
        panelAbajo.add(campoEntrada, BorderLayout.CENTER);
        add(panelAbajo, BorderLayout.SOUTH);

        
        campoEntrada.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                procesarEntrada();
            }
        });

      
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                campoEntrada.requestFocusInWindow();
            }
        });
    }

    private void mostrarBienvenida() {
        imprimir("Consola Simulada - Escriba Help para ver los comandos.");
        imprimir("");
    }

   
    public void imprimir(String texto) {
        areaSalida.append(texto + "\n");
        areaSalida.setCaretPosition(areaSalida.getDocument().getLength());
    }

    

   
    private void procesarEntrada() {
        String linea = campoEntrada.getText();
        campoEntrada.setText("");                 

        imprimir(estado.getPrompt() + linea);     

        actualizarPrompt();
        campoEntrada.requestFocusInWindow();      
    }

   
    private void actualizarPrompt() {
        if (modoEscritura) {
            lblPrompt.setText("> ");
        } else {
            lblPrompt.setText(estado.getPrompt());
        }
    }
}