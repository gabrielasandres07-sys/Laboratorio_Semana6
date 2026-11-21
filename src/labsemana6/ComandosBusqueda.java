/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package labsemana6;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *Laboratorio de programacion II semana 6, 21/7/2026
 * @author kendr
 */
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ComandosBusqueda {
    private EstadoConsola estado;
    private int encontrados;

    public ComandosBusqueda(EstadoConsola estado) {
        this.estado=estado;
    }

    public String find(String texto) {
        if (texto.isEmpty()) {
            return "La sintaxis del comando no es correcta.";
        }
        encontrados=0;
        String resultado=buscar(estado.getActual(),texto.toLowerCase());
        
        if (encontrados==0) {
            return "No se encontró ningún archivo o carpeta con \""+texto+"\".";
        }
        return resultado+encontrados+" resultado(s) encontrado(s).";
    }
    private String buscar(File carpeta, String texto) {
        String salida="";
        File[] hijos=carpeta.listFiles();
        if (hijos==null) {
            return salida;
        }
        for (File hijo:hijos) {
            if (hijo.getName().toLowerCase().contains(texto)) {
                salida+=estado.getRutaVisible(hijo)+"\n";
                encontrados++;
            }
            if (hijo.isDirectory()) {
                salida+=buscar(hijo, texto);
            }
        }
        return salida;
    }

    public String info(String nombre) {
        if (nombre.isEmpty()) {
            return "La sintaxis del comando no es correcta.";
        }
        File f=estado.resolver(nombre);
        if (f==null) {
            return "Acceso denegado.";
        }
        
        if (!f.exists()) {
            return "El sistema no puede encontrar "+nombre+".";
        }
        
        
        
        SimpleDateFormat formato=new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        String tipo=f.isDirectory()?"Carpeta":"Archivo";
        return "Nombre: "+f.getName()+"\n"+ "Tipo: "+tipo+"\n"+"Ruta: "+estado.getRutaVisible(f)+"\n"+"Tamaño: "+tamaño(f)+" bytes\n"+"Última modificación: "+formato.format(new Date(f.lastModified()));
    }

    private long tamaño(File f){
        if (f.isFile()){
            return f.length();
        }
        long total=0;
        File[]hijos=f.listFiles();
        if (hijos!=null) {
            for (File hijo:hijos) {
                total+=tamaño(hijo);
            }
        }
        return total;
    }

    public String tree() {
        return estado.getRutaVisible(estado.getActual())+arbol(estado.getActual(),"");
    }

    
    
    
    
    private String arbol(File carpeta, String sangria) {
        String salida="";
        File[] hijos=carpeta.listFiles();
        if (hijos==null) {
            return salida;
        }
        for (int i=0;i<hijos.length;i++) {
            boolean ultimo=(i==hijos.length-1);
            salida+="\n"+sangria+(ultimo ? "\\---" : "+---")+hijos[i].getName();
            if(hijos[i].isDirectory()) {
                salida+=arbol(hijos[i], sangria+(ultimo ? "    " : "|   "));
            }
        }
        return salida;
    }
        public String grep(String args) {
        int ultimoEspacio=args.lastIndexOf(' ');
        if (ultimoEspacio==-1) {
            return "La sintaxis del comando no es correcta. Uso: Grep <texto> <archivo.ext>";
        }
        String texto=args.substring(0, ultimoEspacio).trim();
        String nombre=args.substring(ultimoEspacio + 1).trim();
        File f=estado.resolver(nombre);
        if(f==null) {
            return "Acceso denegado.";
        }
        if (!f.exists()||f.isDirectory()) {
            return "El sistema no puede encontrar el archivo "+nombre+".";
        }
        String contenido = "";
        try {
            FileReader fr=new FileReader(f);
            int c;
            while((c=fr.read())!=-1) {
                contenido+=(char) c;
            }
            fr.close();
        } catch (IOException e) {
            return "Error al leer el archivo.";
        }
        String[]lineas=contenido.replace("\r", "").split("\n");
        String resultado="Buscando \""+texto+"\" en "+nombre+"...\n";
        int coincidencias=0;
        for (int i=0;i<lineas.length;i++) {
            if (lineas[i].toLowerCase().contains(texto.toLowerCase())) {
                resultado+="Línea "+(i+1)+": "+lineas[i]+"\n";
                coincidencias++;
            }
        }
        if (coincidencias==0) {
            return "No se encontraron coincidencias de \""+texto+"\" en "+nombre+".";
        }
        return resultado+coincidencias+" coincidencia(s) encontrada(s).";
    }

   
    
}
