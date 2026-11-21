/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package labsemana6;

/**
 *
 * @author USER
 */

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ComandosCarpetas {

    private EstadoConsola estado;

    public ComandosCarpetas(EstadoConsola estado) {
        this.estado = estado;
    }

    public String mkdir(String nombre) {
        if (nombre.isEmpty()) {
            return "La sintaxis del comando no es correcta.";
        }
        File carpeta = estado.resolver(nombre);
        if (carpeta == null) {
            return "Acceso denegado.";
        }
        if (carpeta.exists()) {
            return "Ya existe una carpeta o archivo llamado " + nombre + ".";
        }
        if (carpeta.mkdir()) {
            return "Carpeta " + nombre + " creada.";
        }
        return "No se pudo crear la carpeta " + nombre + ".";
    }

    public String mfile(String nombre) {
        if (nombre.isEmpty()) {
            return "La sintaxis del comando no es correcta.";
        }
        File archivo = estado.resolver(nombre);
        if (archivo == null) {
            return "Acceso denegado.";
        }
        if (archivo.exists()) {
            return "Ya existe una carpeta o archivo llamado " + nombre + ".";
        }
        try {
            if (archivo.createNewFile()) {
                return "Archivo " + nombre + " creado.";
            }
        } catch (IOException e) {
            return "Error al crear el archivo: " + e.getMessage();
        }
        return "No se pudo crear el archivo " + nombre + ".";
    }

    public String rm(String nombre) {
        if (nombre.isEmpty()) {
            return "La sintaxis del comando no es correcta.";
        }
        File f = estado.resolver(nombre);
        if (f == null) {
            return "Acceso denegado.";
        }
        if (!f.exists()) {
            return "No se encuentra " + nombre + ".";
        }
        if (estado.estaDentroDe(estado.getActual(), f)) {
            return "No se puede eliminar la carpeta actual.";
        }
        if (borrarRecursivo(f)) {
            return nombre + " eliminado.";
        }
        return "No se pudo eliminar " + nombre + ".";
    }

    private boolean borrarRecursivo(File f) {
        if (f.isDirectory()) {
            File[] hijos = f.listFiles();
            if (hijos != null) {
                for (File hijo : hijos) {
                    borrarRecursivo(hijo);
                }
            }
        }
        return f.delete();
    }

    public String cd(String nombre) {
        if (nombre.isEmpty()) {
            return estado.getRutaVisible(estado.getActual());
        }
        File carpeta = estado.resolver(nombre);
        if (carpeta == null) {
            return "Acceso denegado.";
        }
        if (!carpeta.exists() || !carpeta.isDirectory()) {
            return "El sistema no puede encontrar la ruta especificada.";
        }
        estado.setActual(carpeta);
        return "";
    }

    public String regresar() {
        if (estado.getActual().equals(estado.getRaiz())) {
            return "Ya se encuentra en la carpeta raiz.";
        }
        estado.setActual(estado.getActual().getParentFile());
        return "";
    }

    public String dir() {
        File actual = estado.getActual();
        File[] lista = actual.listFiles();
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy  hh:mm a");

        String salida = " Directorio de " + estado.getRutaVisible(actual) + "\n\n";
        int numArchivos = 0;
        int numCarpetas = 0;
        long bytes = 0;

        if (lista != null) {
            for (File f : lista) {
                String fecha = formato.format(new Date(f.lastModified()));
                if (f.isDirectory()) {
                    salida += fecha + "    <DIR>          " + f.getName() + "\n";
                    numCarpetas++;
                } else {
                    salida += fecha + "    " + String.format("%14d", f.length()) + " " + f.getName() + "\n";
                    numArchivos++;
                    bytes += f.length();
                }
            }
        }
        salida += "               " + numArchivos + " archivos     " + bytes + " bytes\n";
        salida += "               " + numCarpetas + " carpetas";
        return salida;
    }

}