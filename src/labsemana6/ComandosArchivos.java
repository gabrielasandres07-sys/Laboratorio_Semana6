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
}
