package io.github.commonslibs.selenium_lib.utilidades.robots;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import org.testng.Assert;

import io.github.commonslibs.selenium_lib.excepciones.PruebaAceptacionExcepcion;
import io.github.commonslibs.selenium_lib.webdriver.WebDriverFactory;
import io.github.commonslibs.selenium_lib.webdriver.WebDriverFactory.Navegador;
import lombok.extern.slf4j.Slf4j;


@Slf4j
/**
 * Proporciona una utilidad para poder realizar la firma con autofirma de forma externa al navegador. Hay que tener en
 * cuenta que cuando el navegador invoca a la aplicación externa de Autofirma, la primera vez siempre muestra el diálogo
 * para dar permiso a la aplicación, en siguientes llamadas dentro de la misma sesión del webdriver no se muestra.
 *
 * @author dmartinez
 */
public class Autofirma {

   public static void firmarConAutofirma(Navegador navegador) throws PruebaAceptacionExcepcion {
      Autofirma.firmarConAutofirma(navegador, true);
   }

   public static void firmarConAutofirma(Navegador navegador, boolean primeraEjecucion)
         throws PruebaAceptacionExcepcion {
      if (Navegador.FIREFOX == navegador) {
         Autofirma.autoFirmaFirefox(primeraEjecucion);
      }
      else if (Navegador.CHROME == navegador || Navegador.MSEDGE == navegador) {
         Autofirma.autoFirmaChromium(primeraEjecucion);
      }
      else {
         throw new PruebaAceptacionExcepcion("No existe manejador para Autofirma en el navegador " + navegador.name());
      }
   }

   /**
    * Robot de autofirma específico para Chromium (Chrome y Edge)
    *
    * @param primeraEjecucion
	*        indica si es una primera ejecucion o no
    * @throws PruebaAceptacionExcepcion prueba de aceptacion
    */
   public static void autoFirmaChromium(boolean primeraEjecucion) throws PruebaAceptacionExcepcion {
      Runnable runnable = new Runnable() {
         @Override
         public void run() {
            Autofirma.log.info("Iniciando robot para firmar con Autofirma (Chromium)...");
            Robot robot = null;
            try {
               robot = new Robot();
               robot.delay(5000);
            }
            catch (AWTException e) {
               Autofirma.log.error("Error al instanciar el robot para firmar con autofirma (Chromium)", e);
            }
            // Pantalla de dialogo de chrome para seleccionar aplicación que trate Autofirma (siempre se muestra)
            if (primeraEjecucion) {
               robot.keyPress(KeyEvent.VK_LEFT);
               robot.keyRelease(KeyEvent.VK_LEFT);

               robot.keyPress(KeyEvent.VK_ENTER);
               robot.keyRelease(KeyEvent.VK_ENTER);

               robot.delay(5000);
            }
            // En este punto se muestra el dialogo de seleccion de certificado --> Intro del certificado
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);

            Autofirma.log.info("Terminado robot para firmar con Autofirma (Chromium)...");
            robot.delay(2000);
         }
      };

      new Thread(runnable).start();
   }

   /**
    * Robot de autofirma específico para Firefox
    *
    * @param primeraEjecucion
	*        indica si es una primera ejecucion o no
    * @throws PruebaAceptacionExcepcion prueba de aceptacion
    */
   public static void autoFirmaFirefox(boolean primeraEjecucion) throws PruebaAceptacionExcepcion {
      Runnable runnable = new Runnable() {
         @Override
         public void run() {
            Autofirma.log.info("Iniciando robot para firmar con Autofirma (Firefox)...");
            Robot robot = null;
            try {
               robot = new Robot();
               robot.delay(5000);
            }
            catch (AWTException e) {
               Autofirma.log.error("Error al instanciar el robot para firmar con autofirma (Firefox)", e);
            }
            // Pantalla de dialogo de firefox para seleccionar aplicación que trate Autofirma (siempre se muestra)
            if (primeraEjecucion) {
               for (int i = 0; i < 4; i++) {
                  robot.keyPress(KeyEvent.VK_TAB);
                  robot.keyRelease(KeyEvent.VK_TAB);
               }
               robot.keyPress(KeyEvent.VK_ENTER);
               robot.keyRelease(KeyEvent.VK_ENTER);

               robot.delay(5000);
            }
            // En este punto se muestra el dialogo de seleccion de certificado --> Intro del certificado
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);

            Autofirma.log.info("Terminado robot para firmar con Autofirma (Firefox)...");
            robot.delay(2000);
         }
      };

      new Thread(runnable).start();
   }

   /**
    * Aceptar dialog auto firma.
    *
    * @throws PruebaAceptacionExcepcion
    *            la prueba aceptacion excepcion
    */
   public static void aceptarDialogAutoFirma() throws PruebaAceptacionExcepcion {

      Assert.assertFalse(WebDriverFactory.IS_REMOTE_SELENIUM_GRID,
            "Este método aceptarDialogAutoFirma, utiliza la clase Robot y su uso no es compatible con Selenium Grid");

      Autofirma.log.info("Aceptar Dialog Autofirma...");
      Robot robot;
      try {
         robot = new Robot();
         robot.delay(300);
      }
      catch (AWTException e) {
         Autofirma.log.error("Error al instanciar el robot para firmar con autofirma", e);
         throw new PruebaAceptacionExcepcion(e.getMessage());
      }

      robot.keyPress(KeyEvent.VK_TAB);
      robot.delay(100);
      robot.keyRelease(KeyEvent.VK_TAB);
      robot.delay(300);

      robot.keyPress(KeyEvent.VK_ENTER);
      robot.delay(100);
      robot.keyRelease(KeyEvent.VK_ENTER);
      robot.delay(300);

      Autofirma.log.info("Terminado robot para Aceptar Dialog Autofirma...");
   }
}
