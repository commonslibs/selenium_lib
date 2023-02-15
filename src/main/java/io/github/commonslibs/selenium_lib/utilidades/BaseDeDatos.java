package io.github.commonslibs.selenium_lib.utilidades;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.testng.Assert;

import io.github.commonslibs.selenium_lib.excepciones.PruebaAceptacionExcepcion;
import lombok.extern.slf4j.Slf4j;


/**
 * Clase que contiene las conexiones a base de datos.
 *
 * @author AGAPA
 */
@Slf4j
public class BaseDeDatos {

   /**
    * Open and return a conexion to database
    *
    * @return an instance of java.sql.Connection
    * @throws PruebaAceptacionExcepcion
    */
   public static Connection conectar(String bd, String host, String puerto, String usuario, String contrasena)
         throws PruebaAceptacionExcepcion {
      Connection conexion = null;

      String cadenaDeConexion = "jdbc:oracle:thin:@" + host + ":" + puerto + "/" + bd;

      try {
         conexion = DriverManager.getConnection(cadenaDeConexion, usuario, contrasena);
      }
      catch (SQLException e) {
         BaseDeDatos.log.error("Error al conectarse a la base de datos. " + e);
         throw new PruebaAceptacionExcepcion(e.getLocalizedMessage());
      }

      return conexion;
   }

   /**
    * execute a SQL query on database
    *
    * @param queryString
    *           SQL query string
    * @return a reference to returned data collection, an instance of java.sql.ResultSet
    * @throws PruebaAceptacionExcepcion
    */
   public static ResultSet executeQuery(Connection conexion, String queryString) throws PruebaAceptacionExcepcion {
      Assert.assertNotNull(conexion);

      Statement stm;

      ResultSet rs = null;
      try {
         stm = conexion.createStatement();

         rs = stm.executeQuery(queryString);
      }
      catch (SQLException e) {
         BaseDeDatos.log.error("Error al ejecutar consulta." + queryString + ". Error SQL: " + e.getErrorCode() + "-"
               + e.getLocalizedMessage(), e);
         throw new PruebaAceptacionExcepcion(e.getLocalizedMessage());
      }

      return rs;
   }

   public static void desconectar(Connection conexion) throws PruebaAceptacionExcepcion {
      try {
         if (conexion != null && !conexion.isClosed()) {

            conexion.close();

         }
      }
      catch (SQLException e) {
         BaseDeDatos.log.error("Error al cerrar la base de datos", e);
         throw new PruebaAceptacionExcepcion(e.getLocalizedMessage());
      }
   }

   /**
    * Execute non-query (usually INSERT/UPDATE/DELETE/COUNT/SUM...) on database
    *
    * @param queryString
    *           a SQL statement
    * @return single value result of SQL statement
    * @throws PruebaAceptacionExcepcion
    */
   public static boolean execute(Connection conexion, String queryString) throws PruebaAceptacionExcepcion {
      Assert.assertNotNull(conexion);

      Statement stm;

      boolean result = false;
      try {
         stm = conexion.createStatement();

         result = stm.execute(queryString);
      }
      catch (SQLException e) {
         BaseDeDatos.log.error("Error al ejecutar consulta." + queryString + ". Error SQL: " + e.getErrorCode() + "-"
               + e.getLocalizedMessage(), e);
         throw new PruebaAceptacionExcepcion(e.getLocalizedMessage());
      }

      return result;
   }
}
