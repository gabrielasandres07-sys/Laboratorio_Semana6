/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package labsemana6;
/**
 *
 * @author marlo
 */
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class ComandosArchivos {
    private EstadoConsola estado;
    public ComandosArchivos(EstadoConsola estado) {
        this.estado = estado;
    }
    public String date() {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        return "La fecha actual es: " + formato.format(new Date());
    }
    public String time() {
        SimpleDateFormat formato = new SimpleDateFormat("HH:mm:ss");
        return "La hora actual es: " + formato.format(new Date());
    }
    public String validarEscritura(String nombre, boolean agregar) {
        if (nombre.isEmpty()) {
            return "La sintaxis del comando no es correcta";
        }
        File f = estado.resolver(nombre);
        if (f == null) {
            return "acceso denegado";
        }
        if (f.isDirectory()) {
            return nombre + " eso es una carpeta, no un archivo";
        }
        if (agregar && !f.exists()) {
            return "El sistema no encuentra el archivo " + nombre + ".";
        }
        return null;
    }
    public String guardarTexto(File archivo, String texto, boolean agregar) {
        try {
            FileWriter fw = new FileWriter(archivo, agregar);
            fw.write(texto);
            fw.close();
        }catch (IOException e) {
            return "Error al escribir: " + e.getMessage();
        }
        if (agregar) {
            return "Texto agregado a " + archivo.getName() + ".";
        }
        return "Texto guardado en " + archivo.getName() + ".";
    }
    public String rd(String nombre) {
        if (nombre.isEmpty()) {
            return "La sintaxis del comando no es correcta.";
        }
        File f = estado.resolver(nombre);
        if (f==null) {
            return "Acceso denegado.";
        }
        if (!f.exists()||f.isDirectory()) {
            return "El sistema no puede encontrar el archivo "+ nombre + ".";
        }
        String contenido = "";
        try {
            FileReader fr = new FileReader(f);
            int c;
            while ((c = fr.read()) != -1) {
            contenido += (char) c;
            }
            fr.close();
        }catch (IOException e) {
            return "Error al leer: " + e.getMessage();
        }
        if (contenido.isEmpty()) {
            return "(El archivo esta vacio)";
        }
        if (contenido.endsWith("\n")) {
            contenido = contenido.substring(0, contenido.length() - 1);
        }
        return contenido;
    }
    public String ren(String args) {
        String[] partes = args.split("\\s+");
        if (args.isEmpty() || partes.length != 2) {
            return "La sintaxis del comando no es correcta. Uso: Ren <actual> <nuevo>";
        }
        File viejo = estado.resolver(partes[0]);
        File nuevo = estado.resolver(partes[1]);
        if (viejo == null || nuevo == null) {
            return "Acceso denegado";
        }
        if (!viejo.exists()) {
            return "El sistema no puede encontrar " + partes[0];
        }
        if (nuevo.exists()) {
            return "Ya existe un archivo o carpeta llamado " + partes[1];
        }
        if (viejo.renameTo(nuevo)) {
            return partes[0] + " renombrado a " + partes[1];
        }
            return "No se pudo renombrar " + partes[0];
    }
    public String copy(String args) {
        String[] partes = args.split("\\s+");
        if (args.isEmpty() || partes.length != 2) {
            return "La sintaxis del comando no es correcta,uso: Copy <origen> <destino>";
        }
        File origen = estado.resolver(partes[0]);
        File destino = estado.resolver(partes[1]);
        if (origen==null|| destino== null) {
            return "Acceso denegado.";
        }
        if (!origen.exists()||origen.isDirectory()) {
            return "El sistema no puede encontrar el archivo " + partes[0];
        }
        if (destino.exists()){
            return "Ya existe un archivo llamado " + partes[1];
        }
        try {
            FileReader fr = new FileReader(origen);
            FileWriter fw = new FileWriter(destino);
            int c;
            while ((c=fr.read())!=-1) {
                fw.write(c);
            }
            fr.close();
            fw.close();
        } catch (IOException e) {
            return "eror al copiar: " + e.getMessage();
        }
        return " 1 archivo(s) copiado(s)!";
    }
    
}
