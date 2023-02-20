package io.github.commonslibs.selenium_lib.suite;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;

import com.automation.remarks.testng.UniversalVideoListener;

import io.github.commonslibs.selenium_lib.excepciones.PruebaAceptacionExcepcion;
import io.github.commonslibs.selenium_lib.reports.InformeListener;
import io.github.commonslibs.selenium_lib.reports.ResumenListener;
import io.github.commonslibs.selenium_lib.utilidades.Traza;
import io.github.commonslibs.selenium_lib.utilidades.VariablesGlobalesTest;
import io.github.commonslibs.selenium_lib.utilidades.VariablesGlobalesTest.PropiedadesTest;
import io.github.commonslibs.selenium_lib.webdriver.WebDriverFactory;
import io.github.commonslibs.selenium_lib.webdriver.WebDriverFactory.Navegador;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


/**
 * Añade por defecto listeners
 */
@Slf4j
@Listeners({ ResumenListener.class, InformeListener.class, UniversalVideoListener.class })
public abstract class TestSeleniumAbstracto extends AbstractTestNGSpringContextTests {

   /** Instacia de webdriver usado en el caso de prueba */
   @Getter
   protected WebDriver driver;

   /**
    * Metodo que se ejecuta antes de los test.
    *
    * @throws PruebaAceptacionExcepcion
    */
   @BeforeTest
   public void beforeTest() throws PruebaAceptacionExcepcion {
      try {
         TestSeleniumAbstracto.log.debug("Pretest");
         // Indicara la carpeta donde se guardaran los videos.
         System.setProperty("video.folder", System.getProperty("user.dir") + "//target//surefire-reports//video//"
               + this.getClass().getSimpleName());

         // System.setProperty("video.screen.size", "102x76");
         // System.setProperty("ffmpeg.display", "1.0");
         // ffmpeg.display=:0.0

         this.iniciar();
      }
      catch (Exception e) {
         throw new PruebaAceptacionExcepcion(e.getLocalizedMessage());
      }
   }

   @AfterTest
   public void afterTest() throws PruebaAceptacionExcepcion {
      TestSeleniumAbstracto.log.debug("Posttest");
      this.terminar();
   }

   private void iniciar() throws PruebaAceptacionExcepcion {
      Navegador navegador = Navegador.valueOf(VariablesGlobalesTest.getPropiedad(PropiedadesTest.NAVEGADOR.name()));
      this.driver = WebDriverFactory.obtenerInstancia(navegador);
      Assert.assertNotNull(this.driver, "Error al instanciar el driver de " + navegador);

      // Borrado de todas las Cookies
      this.driver.manage().deleteAllCookies();

      // Si estamos en modo GRAFICO, pasamos a segundo monitor y maximizamos.
      if (!WebDriverFactory.IS_HEADLESS) {

         // Para ejecutar en segundo monitor
         if (!WebDriverFactory.IS_VIDEO_ENABLED) {
            GraphicsDevice[] screens = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
            if (screens.length > 1) {
               Rectangle area = screens[1].getDefaultConfiguration().getBounds();
               this.driver.manage().window().setPosition(new Point(area.x, 0));
            }
         }

         // Para Maximizar
         String propiedadMaximizar = null;
         try {
            propiedadMaximizar = VariablesGlobalesTest.getPropiedad(PropiedadesTest.MAXIMIZAR.name());
         }
         catch (Exception e) {
            Traza.info("Propiedad MAXIMIZAR no definida. Se asume como TRUE");
         }
         boolean maximizar = true;
         if (StringUtils.isNotEmpty(propiedadMaximizar)) {
            maximizar = Boolean.valueOf(propiedadMaximizar);
         }
         if (maximizar) {
            this.driver.manage().window().maximize();
         }
      }
   }

   private void terminar() throws PruebaAceptacionExcepcion {
      this.driver.quit();
   }

}
