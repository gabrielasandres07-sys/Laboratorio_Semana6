
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
        bienvenida();
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
               procesarEntrada() ;
            }
        });

      
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                campoEntrada.requestFocusInWindow();
            }
        });
    }

    private void bienvenida() {
        imprimir("Consola Simulada - Escriba Help para ver los comandos.");
        imprimir("");
    }

   
    public void imprimir(String texto) {
        areaSalida.append(texto + "\n");
        areaSalida.setCaretPosition(areaSalida.getDocument().getLength());
    }


   
    private void actualizarPrompt() {
        if (modoEscritura) {
            lblPrompt.setText("> ");
        } else {
            lblPrompt.setText(estado.getPrompt());
        }
    }
     

    
    private void limpiarPantalla() {
        areaSalida.setText("");
    }

    
    private void salir() {
        dispose();
        System.exit(0);
    }

    
    private String comandoNoReconocido(String comando) {
        return "'" + comando + "' no se reconoce como un comando interno o externo,\n"
                + "programa o archivo por lotes ejecutable.";
    }

    // lista de comados
    private String ayuda() {
        return "Comandos disponibles (respetar mayúsculas y minúsculas):\n"
                + "  Mkdir <nombre>           Crea una nueva carpeta.\n"
                + "  Mfile <nombre.ext>       Crea un nuevo archivo.\n"
                + "  Rm <nombre>              Elimina un archivo o carpeta (con su contenido).\n"
                + "  Cd <carpeta>             Cambia a la carpeta indicada.\n"
                + "  ..                       Regresa a la carpeta anterior.\n"
                + "  Dir                      Lista los archivos y carpetas actuales.\n"
                + "  Date                     Muestra la fecha actual.\n"
                + "  Time                     Muestra la hora actual.\n"
                + "  Wr <archivo.ext>         Escribe texto en un archivo (termina con EXIT).\n"
                + "  Rd <archivo.ext>         Muestra el contenido de un archivo.\n"
                + "  Ap <archivo.ext>         Agrega texto al final de un archivo (termina con EXIT).\n"
                + "  Ren <actual> <nuevo>     Renombra un archivo o carpeta.\n"
                + "  Copy <origen> <destino>  Copia un archivo a uno nuevo.\n"
                + "  Find <nombre>            Busca archivos o carpetas por nombre.\n"
                + "  Info <nombre>            Muestra tipo, ruta, tamaño y última modificación.\n"
                + "  Tree                     Muestra el árbol de carpetas y archivos.\n"
                + "  Grep <texto> <archivo>   Busca un texto dentro de un archivo.\n"
                + "  Cls                      Limpia la pantalla.\n"
                + "  Help                     Muestra esta ayuda.\n"
                + "  Exit                     Cierra la consola.";
    }
   

    
    private void iniciarEscritura(String nombre, boolean agregar) {
        String error = archivos.validarEscritura(nombre, agregar);
        if (error == null) {
            archivoEscritura = estado.resolver(nombre);
            modoAgregar = agregar;
            textoEscrito = "";
            modoEscritura = true;
            imprimir("Escriba el texto. Escriba EXIT (en mayúsculas) para terminar.");
        } else {
            imprimir(error);
            imprimir("");
        }
    }
    private void procesarLineaEscritura(String linea) {
        imprimir(linea);
        if (linea.equals("EXIT")) {
            modoEscritura = false;
            imprimir(archivos.guardarTexto(archivoEscritura, textoEscrito, modoAgregar));
            imprimir("");
            archivoEscritura = null;
            textoEscrito = "";
        } else {
            textoEscrito = textoEscrito + linea + "\n";
        }
    }

    
     private void procesarEntrada() {
        String linea = campoEntrada.getText();
        campoEntrada.setText("");                 

        if (modoEscritura) {
            procesarLineaEscritura(linea);        
        } else {
            imprimir(estado.getPrompt() + linea);  
            ejecutar(linea.trim());                
        }

        actualizarPrompt();
        campoEntrada.requestFocusInWindow();       
    }
     private void ejecutar(String linea) {
        if (linea.isEmpty()) {
            return;     
        }

      
        String comando;
        String args;
        int espacio = linea.indexOf(' ');
        if (espacio == -1) {
            comando = linea;
            args = "";
        } else {
            comando = linea.substring(0, espacio);
            args = linea.substring(espacio + 1).trim();
        }

        String resultado;
        switch (comando) {
           
            case "Mkdir":
                resultado = carpetas.mkdir(args);
                break;
            case "Mfile":
                resultado = carpetas.mfile(args);
                break;
            case "Rm":
                resultado = carpetas.rm(args);
                break;
            case "Cd":
                resultado = carpetas.cd(args);
                break;
            case "..":
                resultado = carpetas.regresar();
                break;
            case "Dir":
                resultado = carpetas.dir();
                break;

         
            case "Date":
                resultado = archivos.date();
                break;
            case "Time":
                resultado = archivos.time();
                break;
            case "Rd":
                resultado = archivos.rd(args);
                break;
            case "Ren":
                resultado = archivos.ren(args);
                break;
            case "Copy":
                resultado = archivos.copy(args);
                break;
            case "Wr":
                iniciarEscritura(args, false);
                return;
            case "Ap":
                iniciarEscritura(args, true);
                return;

          
            case "Find":
                resultado = busqueda.find(args);
                break;
            case "Info":
                resultado = busqueda.info(args);
                break;
            case "Tree":
                resultado = busqueda.tree();
                break;
            case "Grep":
                resultado = busqueda.grep(args);
                break;

         
            case "Cls":
                limpiarPantalla();
                return;
            case "Help":
                resultado = ayuda();
                break;
            case "Exit":
                salir();
                return;

            default:
                resultado = comandoNoReconocido(comando);
        }

        if (resultado.isEmpty() == false) {
            imprimir(resultado);
        }
        imprimir("");  
    }

}