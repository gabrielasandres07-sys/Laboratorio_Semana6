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

}
