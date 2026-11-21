
package labsemana6;


import java.io.File;
import java.io.IOException;


public class EstadoConsola {

    private File raiz;      
    private File actual;   

    public EstadoConsola() {
        File carpeta = new File("ConsolaSimulada");

   
        if (carpeta.exists() == false) {
            carpeta.mkdir();
        }

      
        try {
            raiz = carpeta.getCanonicalFile();
        } catch (IOException e) {
            raiz = carpeta.getAbsoluteFile();
        }
        actual = raiz;
    }

    public File getRaiz() {
        return raiz;
    }

    public File getActual() {
        return actual;
    }

  
    public void setActual(File carpeta) {
        if (carpeta == null) {
            return;
        }
        if (carpeta.isDirectory() && estaDentroDe(carpeta, raiz)) {
            actual = carpeta;
        }
    }

  
    public String getRutaVisible(File f) {
        if (estaDentroDe(f, raiz) == false) {
            return "C:\\ConsolaSimulada";
        }
        String resto = f.getPath().substring(raiz.getPath().length());
        resto = resto.replace(File.separatorChar, '\\');
        return "C:\\ConsolaSimulada" + resto;
    }

    
    public String getPrompt() {
        return getRutaVisible(actual) + ">";
    }

   
    public File resolver(String nombre) {
        File f = new File(actual, nombre);
        try {
            f = f.getCanonicalFile();
        } catch (IOException e) {
            return null;
        }
        if (estaDentroDe(f, raiz)) {
            return f;
        }
        return null;
    }

   
    public boolean estaDentroDe(File f, File carpeta) {
        String rutaF = f.getPath();
        String rutaCarpeta = carpeta.getPath();
        if (rutaF.equals(rutaCarpeta)) {
            return true;
        }
        return rutaF.startsWith(rutaCarpeta + File.separator);
    }
}