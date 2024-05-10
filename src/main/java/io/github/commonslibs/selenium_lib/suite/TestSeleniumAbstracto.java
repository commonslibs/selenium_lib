package io.github.commonslibs.selenium_lib.suite;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.ITestContext;
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
import io.github.commonslibs.selenium_lib.utilidades.WebElementWrapper;
import io.github.commonslibs.selenium_lib.webdriver.WebDriverFactory;
import io.github.commonslibs.selenium_lib.webdriver.WebDriverFactory.Navegador;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


/**
 * Añade por defecto listeners
 */
@Slf4j
@Listeners({ ResumenListener.class, InformeListener.class, UniversalVideoListener.class })
public abstract class TestSeleniumAbstracto extends AbstractTestNGSpringContextTests {

   @Getter
   @Setter
   private WebElementWrapper webElementWrapper = null;

   // /** Instacia de webdriver usado en el caso de prueba */
   // @Getter
   // protected WebDriver driver;

   // @Getter
   // protected Navegador navegador;

   @Getter
   protected String          codigoTest        = null;

   /**
    * Metodo que se ejecuta antes de los test.
    *
    * @throws PruebaAceptacionExcepcion
    *            si se produce un error en las operaciones Pretest
    */
   @BeforeTest
   public void beforeTest(ITestContext context) throws PruebaAceptacionExcepcion {
      try {
         TestSeleniumAbstracto.log.debug("Pretest");
         // Indicara la carpeta donde se guardaran los videos.
         System.setProperty("video.folder", System.getProperty("user.dir") + "//target//surefire-reports//video//"
               + (this.codigoTest != null ? this.codigoTest + "//" : this.getClass().getSimpleName()));

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
      TestSeleniumAbstracto.log.info("Iniciar WebDriver");
      Navegador navegador = Navegador.valueOf(VariablesGlobalesTest.getPropiedad(PropiedadesTest.NAVEGADOR.name()));
      TestSeleniumAbstracto.log.info("WebDriver, set + obtenerInstancia");
      try {
         WebDriverFactory webDriverFactory = new WebDriverFactory();
         WebDriver driver = webDriverFactory.obtenerInstancia(navegador);
         this.webElementWrapper = new WebElementWrapper(driver);
      }
      catch (Throwable e) {
         TestSeleniumAbstracto.log.error("error", e);
         throw new PruebaAceptacionExcepcion("error");
      }

      Assert.assertNotNull(this.webElementWrapper, "Error al instanciar el driver de " + navegador);

      // Borrado de todas las Cookies
      TestSeleniumAbstracto.log.info("WebDriver, borrar cookies");
      this.webElementWrapper.getDriver().manage().deleteAllCookies();

      // Si estamos en modo GRAFICO, pasamos a segundo monitor y maximizamos.
      if (!WebDriverFactory.IS_HEADLESS) {

         // Para ejecutar en segundo monitor
         if (!WebDriverFactory.IS_VIDEO_ENABLED) {
            GraphicsDevice[] screens = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
            int posicion = 0;
            if (screens.length > 1) {
               for (GraphicsDevice screen : screens) {
                  int margen = screen.getDefaultConfiguration().getBounds().x;
                  if (margen != 0) {
                     posicion = margen;
                  }
               }
               this.webElementWrapper.getDriver().manage().window().setPosition(new Point(posicion, 0));
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
            this.webElementWrapper.getDriver().manage().window().maximize();
         }
      }
   }

   private void terminar() throws PruebaAceptacionExcepcion {
      if (this.webElementWrapper != null) {
         this.webElementWrapper.setWait(null);
         if (this.webElementWrapper.getDriver() != null) {
            this.webElementWrapper.getDriver().close();
         }
         if (this.webElementWrapper.getDriver() != null) {
            this.webElementWrapper.getDriver().quit();
         }
      }
   }

}
