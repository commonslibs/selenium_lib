package io.github.commonslibs.selenium_lib.utilidades;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;


/**
 * Clase que reocge el valor de las varaible del property
 */
@Data
@Slf4j
public class VariablesGlobalesTest {

   private static Properties propiedades = null;

   /**
    * Contiene el nombre de la variable del property por el que busca
    *
    * @author AGAPAGAPA
    */
   public enum PropiedadesTest {
      NAVEGADOR, MAXIMIZAR, TIEMPO_RETRASO_CORTO, TIEMPO_RETRASO_MEDIO, TIEMPO_RETRASO_LARGO, ID_ELEMENTO_PROCESANDO,
      POSICION_CERTIFICADO
   }

   /**
    * Recoge el valor del porperty filtrando por el entorno de desarollo, por defecto recoge IC
    *
    * @param propiedad
    * @return Valor de las propiedades del property
    * @throws IllegalArgumentException
    */
   public static String getPropiedad(String propiedad) throws IllegalArgumentException {
      if (VariablesGlobalesTest.propiedades == null) {
         String entorno = System.getProperty("entorno");
         if (entorno != null && !entorno.equals("")) {
            VariablesGlobalesTest.propiedades =
                  VariablesGlobalesTest.getFilePathToSaveStatic("application-" + entorno + ".properties");
         }
         else {
            VariablesGlobalesTest.propiedades =
                  VariablesGlobalesTest.getFilePathToSaveStatic("application-ic.properties");
         }
      }

      if (!VariablesGlobalesTest.propiedades.containsKey(propiedad)) {
         throw new IllegalArgumentException(
               "Parámetro " + propiedad + " no se ha encontrado en el fichero de configuración");
      }

      // Comentado porque hay parametros sin valor
      // if (StringUtils.isBlank(propiedades.get(propiedad).toString())) {
      // throw new IllegalArgumentException("Parámetro " + propiedad + " de configuración inválido");
      // }

      return VariablesGlobalesTest.propiedades.get(propiedad).toString();
   }

   /**
    * Saca los valores del archivo.
    *
    * @param fileName
    * @return Valores del archivo.
    */
   private static Properties getFilePathToSaveStatic(String fileName) throws IllegalArgumentException {

      Properties prop = new Properties();
      if (!VariablesGlobalesTest.existeFicheroResource(fileName)) {
         throw new IllegalArgumentException("No se ha encontrado el fichero " + fileName);
      }
      try (InputStream inputStream = VariablesGlobalesTest.class.getClassLoader().getResourceAsStream(fileName)) {
         prop.load(inputStream);
      }
      catch (IOException e) {
         VariablesGlobalesTest.log.error(e.getLocalizedMessage());
      }

      return prop;
   }

   protected static boolean existeFicheroResource(String fichero) {
      ClassLoader classLoader = VariablesGlobalesTest.class.getClassLoader();
      URL url = classLoader.getResource(fichero);
      return (url != null);
   }

}