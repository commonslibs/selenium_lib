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
    * Abre y retorna una conexion a BBDD
    *
    * @param bd
    *           base de datos para la conexion a la BBDD
    * @param host
    *           host donde esta la BBDD
    * @param puerto
    *           puerto del host donde se encuentra la BBDD
    * @param usuario
    *           usuario de la BBDD
    * @param contrasena
    *           password de la BBDD
    *
    * @return una instancia de tipo java.sql.Connection
    * @throws PruebaAceptacionExcepcion si se produce un error de conexion a BBDD
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
    * ejecuta una consulta SQL a la BBDD
    *
    * @param conexion
    *           conexion a BBDD
    * @param queryString
    *           cadena SQL
    * @return una referencia a la coleccion de retorno, del tipo java.sql.ResultSet
    * @throws PruebaAceptacionExcepcion si se produce un error al ejecutar una consulta de BBDD
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

   /**
    * cierra la conexion con la BBDD
    *
    * @param conexion
    *           conexion a BBDD
    * @throws PruebaAceptacionExcepcion si se produce un error al cerrar la conexion de BBDD
    */
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
    * @param conexion
    *           conexion a BBDD
    * @param queryString
    *           a SQL statement
    * @return single value result of SQL statement
    * @throws PruebaAceptacionExcepcion si se produce un error al ejecutar la query a la BBDD
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
