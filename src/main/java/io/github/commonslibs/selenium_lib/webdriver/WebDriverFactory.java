package io.github.commonslibs.selenium_lib.webdriver;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.commonslibs.selenium_lib.utilidades.VariablesGlobalesTest;
import lombok.extern.slf4j.Slf4j;


/**
 * Factoria de Web drivers. Devuelve el driver del navegador especificado mediante obtenerInstancia().
 *
 * @author dmartinez
 */
@Slf4j
public class WebDriverFactory {

   public enum Navegador {
      CHROME, FIREFOX, MSEDGE
   }

   private static final String                              TX_FALSE                = "false";

   public static final boolean                              IS_REMOTE_SELENIUM_GRID =
         Boolean.parseBoolean(System.getProperty("remoteSG", WebDriverFactory.TX_FALSE).toLowerCase());

   public static final boolean                              IS_HEADLESS             =
         Boolean.parseBoolean(System.getProperty("java.awt.headless", WebDriverFactory.TX_FALSE).toLowerCase());

   public static final boolean                              IS_MODO_INCOGNITO       =
         Boolean.parseBoolean(System.getProperty("modoIncognito", WebDriverFactory.TX_FALSE).toLowerCase());

   public static final boolean                              IS_VIDEO_ENABLED        =
         Boolean.parseBoolean(System.getProperty("video.enabled", WebDriverFactory.TX_FALSE).toLowerCase());

   private static final Map<Navegador, Supplier<WebDriver>> navegadores;

   private static final String                              HTTPS_PROXY             =
         VariablesGlobalesTest.getPropiedad("HTTPS_PROXY");

   private static final String                              HTTP_HUB_SELENIUM       =
         VariablesGlobalesTest.getPropiedad("HTTP_HUB_SELENIUM");

   private static final Supplier<WebDriver>                 chromeDriverSupplier    = () -> {
                                                                                       WebDriverManager.chromedriver()
                                                                                             .proxy(
                                                                                                   WebDriverFactory.HTTPS_PROXY)
                                                                                             .setup();
                                                                                       System.setProperty(
                                                                                             "webdriver.chrome.whitelistedIps",
                                                                                             "");

                                                                                       ChromeOptions options =
                                                                                             new ChromeOptions();
                                                                                       // Parametros para evitar errores
                                                                                       // al ejecutar Google Chrome con
                                                                                       // usuario
                                                                                       // root en linux (se aconseja
                                                                                       // incluirla
                                                                                       // cuando se ejecuta chrome en un
                                                                                       // entorno docker como es el caso
                                                                                       options.addArguments(
                                                                                             "no-sandbox");

                                                                                       // se aconseja incluirla cuando
                                                                                       // se ejecuta
                                                                                       // chrome en un entorno docker
                                                                                       // como es el caso
                                                                                       options.addArguments(
                                                                                             "disable-dev-shm-usage");

                                                                                       // Para habilitar el registro
                                                                                       // detallado
                                                                                       options.addArguments("verbose");

                                                                                       // Se deshabiitan las extensiones
                                                                                       options.addArguments(
                                                                                             "disable-extensions");

                                                                                       // Se deshabiitan el sonido
                                                                                       // Aconsejado en entorno docker
                                                                                       // de tests como es el caso
                                                                                       options.addArguments(
                                                                                             "mute-audio");

                                                                                       // Para lanzar en modo incognito
                                                                                       if (WebDriverFactory.IS_MODO_INCOGNITO) {
                                                                                          options.addArguments(
                                                                                                "incognito");
                                                                                       }

                                                                                       // Parámetro para no abrir el
                                                                                       // modo gráfico del navegador.
                                                                                       if (WebDriverFactory.IS_HEADLESS) {
                                                                                          options.addArguments(
                                                                                                "--headless=new");
                                                                                       }
                                                                                       else {
                                                                                          // Deshabilitamos el mensaje
                                                                                          // del
                                                                                          // navegador: "Un Software
                                                                                          // automatizado de pruebas
                                                                                          // está
                                                                                          // controlando Chrome."
                                                                                          options.setExperimentalOption(
                                                                                                "useAutomationExtension",
                                                                                                false);
                                                                                          options.setExperimentalOption(
                                                                                                "excludeSwitches",
                                                                                                Collections
                                                                                                      .singletonList(
                                                                                                            "enable-automation"));
                                                                                       }

                                                                                       // Para lanzar en modo incognito
                                                                                       if (WebDriverFactory.IS_REMOTE_SELENIUM_GRID) {
                                                                                          DesiredCapabilities capabilities =
                                                                                                new DesiredCapabilities();

                                                                                          capabilities.setCapability(
                                                                                                ChromeOptions.CAPABILITY,
                                                                                                options);

                                                                                          return WebDriverManager
                                                                                                .chromedriver()
                                                                                                .capabilities(
                                                                                                      capabilities)
                                                                                                .remoteAddress(
                                                                                                      WebDriverFactory.HTTP_HUB_SELENIUM)
                                                                                                .create();
                                                                                       }
                                                                                       else {
                                                                                          return new ChromeDriver(
                                                                                                options);
                                                                                       }
                                                                                    };

   private static final Supplier<WebDriver>                 firefoxDriverSupplier   = () -> {
                                                                                       WebDriverManager.firefoxdriver()
                                                                                             .proxy(
                                                                                                   WebDriverFactory.HTTPS_PROXY)
                                                                                             .setup();

                                                                                       FirefoxOptions options =
                                                                                             new FirefoxOptions();

                                                                                       // Parámetro para no abrir el
                                                                                       // modo gráfico del navegador.
                                                                                       if (WebDriverFactory.IS_HEADLESS) {
                                                                                          options.addArguments(
                                                                                                "headless");
                                                                                       }

                                                                                       // Para lanzar en modo incognito
                                                                                       if (WebDriverFactory.IS_MODO_INCOGNITO) {
                                                                                          options.addArguments(
                                                                                                "-private");
                                                                                       }

                                                                                       if (WebDriverFactory.IS_REMOTE_SELENIUM_GRID) {
                                                                                          DesiredCapabilities capabilities =
                                                                                                new DesiredCapabilities();

                                                                                          capabilities.setCapability(
                                                                                                // FirefoxOptions.CAPABILITY,
                                                                                                // "moz:firefoxOptions"
                                                                                                FirefoxOptions.FIREFOX_OPTIONS,
                                                                                                options);

                                                                                          return WebDriverManager
                                                                                                .firefoxdriver()
                                                                                                .capabilities(
                                                                                                      capabilities)
                                                                                                .remoteAddress(
                                                                                                      WebDriverFactory.HTTP_HUB_SELENIUM)
                                                                                                .create();
                                                                                       }
                                                                                       else {
                                                                                          return new FirefoxDriver(
                                                                                                options);
                                                                                       }
                                                                                    };

   private static final Supplier<WebDriver>                 edgeDriverSupplier      = () -> {
                                                                                       WebDriverManager.edgedriver()
                                                                                             .proxy(
                                                                                                   WebDriverFactory.HTTPS_PROXY)
                                                                                             .setup();

                                                                                       EdgeOptions options =
                                                                                             new EdgeOptions();

                                                                                       // Para lanzar en modo incognito
                                                                                       if (WebDriverFactory.IS_MODO_INCOGNITO) {
                                                                                          options.addArguments(
                                                                                                "incognito");
                                                                                       }

                                                                                       // Parámetro para no abrir el
                                                                                       // modo gráfico del navegador.
                                                                                       if (WebDriverFactory.IS_HEADLESS) {
                                                                                          options.addArguments(
                                                                                                "headless");
                                                                                       }

                                                                                       if (WebDriverFactory.IS_REMOTE_SELENIUM_GRID) {
                                                                                          DesiredCapabilities capabilities =
                                                                                                new DesiredCapabilities();

                                                                                          capabilities.setCapability(
                                                                                                EdgeOptions.CAPABILITY,
                                                                                                options);

                                                                                          return WebDriverManager
                                                                                                .edgedriver()
                                                                                                .capabilities(
                                                                                                      capabilities)
                                                                                                .remoteAddress(
                                                                                                      WebDriverFactory.HTTP_HUB_SELENIUM)
                                                                                                .create();
                                                                                       }
                                                                                       else {
                                                                                          return new EdgeDriver(
                                                                                                options);
                                                                                       }

                                                                                    };

   static {
      navegadores = new HashMap<>();
      WebDriverFactory.navegadores.put(Navegador.CHROME, WebDriverFactory.chromeDriverSupplier);
      WebDriverFactory.navegadores.put(Navegador.FIREFOX, WebDriverFactory.firefoxDriverSupplier);
      WebDriverFactory.navegadores.put(Navegador.MSEDGE, WebDriverFactory.edgeDriverSupplier);

   }

   /**
    * No se implementa como singleton debido a una posible ejecución en paralelo.
    *
    * @param navegador
    * @return
    */
   public static WebDriver obtenerInstancia(Navegador navegador) {
      WebDriverFactory.log.info("Obteniendo una nueva instancia del navegador " + navegador.toString());
      WebDriver wd = WebDriverFactory.navegadores.get(navegador).get();
      WebDriverFactory.log.info("Instancia obtenida: " + wd.toString());
      return wd;
   }

}