package io.github.commonslibs.selenium_lib.utilidades.robots;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;

import io.github.commonslibs.selenium_lib.excepciones.PruebaAceptacionExcepcion;
import io.github.commonslibs.selenium_lib.webdriver.WebDriverFactory;
import io.github.commonslibs.selenium_lib.webdriver.WebDriverFactory.Navegador;
import lombok.extern.slf4j.Slf4j;


@Slf4j
/**
 * Proporciona una utilidad para poder seleccionar un certificado digital. Es importante que durante el proceso, el
 * navegador de pruebas se mantenga en primer plano, de lo contrario el robot ejecutará los comandos sobre la aplicación
 * que se encuentre en primer plano
 */
public class Certificado {

   /**
    * Método que lanza un proceso en segundo plano para seleccionar un certificado digial IMPORTANTE: Hay que llamar
    * esta función justo antes de que se abra la ventana de selección de certificados
    *
    * @param navegador
    *           El navegador en el que está corriendo selenium
    * @param posicionCertificado
    *           La posición donde se encuentra el certificado, siendo 1 la primera posición
    */
   public static void seleccionarCertificado(Navegador navegador, int posicionCertificado)
         throws PruebaAceptacionExcepcion {
      if (Navegador.FIREFOX == navegador) {
         // TODO - Implementar la selección de certificado con firefox
      }
      else if (Navegador.CHROME == navegador) {
         Certificado.seleccionarCertificadoChrome(posicionCertificado);
      }
      else {
         throw new PruebaAceptacionExcepcion(
               "No hay implementado un robot para seleccionar el certificado digital en el navegador "
                     + navegador.name());
      }
   }

   public static void seleccionarCertificadoChrome(int posicionCertificado) throws PruebaAceptacionExcepcion {
      Runnable runnable = new Runnable() {
         @Override
         public void run() {
            Certificado.log.info("Iniciando selección de certificado digital (chrome)...");
            Robot robot = null;
            try {
               robot = new Robot();
               robot.delay(1500);

               for (int i = 1; i < posicionCertificado; i++) {
                  robot.keyPress(KeyEvent.VK_DOWN);
                  robot.keyRelease(KeyEvent.VK_DOWN);
                  robot.delay(300);
               }

               robot.delay(300);
               robot.keyPress(KeyEvent.VK_ENTER);
               robot.keyRelease(KeyEvent.VK_ENTER);

            }
            catch (AWTException e) {
               Certificado.log.error("Error al seleccionar el certificado digital con robots (chrome)", e);
            }

         }
      };

      new Thread(runnable).start();
   }

   public static void seleccionarCertificado(String posicionCertificado) throws PruebaAceptacionExcepcion {

      Assert.assertFalse(WebDriverFactory.IS_REMOTE_SELENIUM_GRID,
            "Este test utiliza la clase Robot y su uso no es compatible con Selenium Grid");

      int posCertificado = 1;
      if (!StringUtils.isBlank(posicionCertificado)) {
         posCertificado = Integer.parseInt(posicionCertificado);
      }

      Certificado.log.info("Iniciando robot para seleccionar certificado...");
      Robot robot;
      try {
         robot = new Robot();
         robot.delay(3000);
      }
      catch (AWTException e) {
         Certificado.log.error("Error al instanciar el robot para seleccionar certificado", e);
         throw new PruebaAceptacionExcepcion(e.getMessage());
      }

      // Seleccion del certificado, segun su posicion
      for (int i = 1; i < posCertificado; i++) {
         robot.keyPress(KeyEvent.VK_DOWN);
         robot.delay(100);
         robot.keyRelease(KeyEvent.VK_DOWN);
         robot.delay(500);
      }

      // En este punto se muestra el dialogo de seleccion de certificado --> Intro del certificado
      robot.keyPress(KeyEvent.VK_ENTER);
      robot.keyRelease(KeyEvent.VK_ENTER);
      robot.delay(2000);

      Certificado.log.info("Terminado robot para seleccionar certificado...");
   }

}
