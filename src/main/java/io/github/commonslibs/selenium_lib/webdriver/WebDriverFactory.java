package io.github.commonslibs.selenium_lib.webdriver;

import java.util.Collections;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
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

   // // RAULM Pendiente
   // @Getter
   // static @Setter private ExtentTest logger;

   // // Almacén del WebDriver compartido por todos los servicios y clases de test del proyecto.
   // @Getter
   // @Setter
   // private WebDriver driver;

   // @Getter
   // @Setter
   // private WebDriverWait wait;
   //
   // private WebElementWrapper webElementWrapper;

   public enum Navegador {
      CHROME, FIREFOX, MSEDGE
   }

   private static final String TX_FALSE                = "false";

   public static final boolean IS_REMOTE_SELENIUM_GRID =
         Boolean.parseBoolean(System.getProperty("remoteSG", WebDriverFactory.TX_FALSE).toLowerCase());

   public static final boolean IS_HEADLESS             =
         Boolean.parseBoolean(System.getProperty("java.awt.headless", WebDriverFactory.TX_FALSE).toLowerCase());

   public static final boolean IS_MODO_INCOGNITO       =
         Boolean.parseBoolean(System.getProperty("modoIncognito", WebDriverFactory.TX_FALSE).toLowerCase());

   public static final boolean IS_VIDEO_ENABLED        =
         Boolean.parseBoolean(System.getProperty("video.enabled", WebDriverFactory.TX_FALSE).toLowerCase());

   // private static final Map<Navegador, Supplier<WebDriver>> navegadores;

   private static final String HTTPS_PROXY             = VariablesGlobalesTest.getPropiedad("HTTPS_PROXY");

   private static final String HTTP_HUB_SELENIUM       = VariablesGlobalesTest.getPropiedad("HTTP_HUB_SELENIUM");

   // public WebElementWrapper getWebElementWrapper() {
   // if (this.webElementWrapper == null) {
   // this.webElementWrapper = new WebElementWrapper();
   // }
   // return this.webElementWrapper;
   // }

   private WebDriverManager asignarProxy(WebDriverManager webDriver) {
      if (StringUtils.isNotBlank(WebDriverFactory.HTTPS_PROXY)) {
         return webDriver.proxy(WebDriverFactory.HTTPS_PROXY);
      }
      else {
         return webDriver;
      }
   }

   // FUENTES:
   // https://github.com/GoogleChrome/chrome-launcher/blob/main/docs/chrome-flags-for-tools.md
   // https://source.chromium.org/chromium/chromium/src/+/main:chrome/common/pref_names.h
   private WebDriver chrome() {
      WebDriverFactory.log.info("Instancia de WebDriverManager.chromedriver()");
      WebDriverManager webDriver = WebDriverManager.chromedriver();
      WebDriverFactory.log.info("asignarProxy");
      WebDriverManager webDriverConProxy = this.asignarProxy(webDriver);
      WebDriverFactory.log.info("setup()");
      webDriverConProxy.setup();

      System.setProperty("webdriver.chrome.whitelistedIps", "");

      ChromeOptions options = new ChromeOptions();

      // Parametros para evitar errores al ejecutar Google Chrome con usuario root en linux (se aconseja incluirla
      // cuando se ejecuta chrome en un entorno docker como es el caso
      options.addArguments("--no-sandbox");

      // IMPORTANTE: Para que sirve la siguiente propiedad?
      options.addArguments("--enable-npapi");

      // Incompatibilidad de selenium y chrome 111 al utilizar direcciones sin https
      // https: // stackoverflow.com/questions/75678572/java-io-ioexception-invalid-status-code-403-text-forbidden
      options.addArguments("--remote-allow-origins=*");

      // Para ignorar las advertencias al no utilizar https
      options.addArguments("ignore-certificate-errors");

      // Habilitar la opción "Ejecutar contenido Java"
      options.addArguments("--enable-internal-ehb");

      // Se aconseja incluirla cuando se ejecuta chrome en un entorno docker
      options.addArguments("--disable-dev-shm-usage");

      // https://stackoverflow.com/a/49123152/1689770
      options.addArguments("--disable-browser-side-navigation");

      // https://stackoverflow.com/questions/51959986/how-to-solve-selenium-chromedriver-timed-out-receiving-message-from-renderer-exc
      options.addArguments("--disable-gpu");

      // Para habilitar el registro detallado
      options.addArguments("--verbose");

      // Se deshabilitan las extensiones
      options.addArguments("--disable-extensions");

      // Se deshabiitan el sonido. Aconsejado en entorno docker
      options.addArguments("--mute-audio");

      // Desactiva la traducción de Chrome, tanto la opción manual como el mensaje emergente cuando se detecta una
      // página con un idioma diferente.
      options.addArguments("--disable-features=Translate");

      // Desactive la verificación del navegador predeterminado, no solicite configurarlo como tal
      options.addArguments("--no-default-browser-check");

      // No actualice los 'componentes' del navegador enumerados en chrome://components/
      options.addArguments("--disable-component-update");

      // Desactiva la comunicación del servidor de autocompletar. Esta función no se desactiva mediante otras banderas
      // "principales".
      options.addArguments("--disable-features=AutofillServerCommunication");

      // Desactivar la sincronización con una cuenta de Google
      options.addArguments("--disable-sync");

      // Para lanzar en modo incognito
      if (WebDriverFactory.IS_MODO_INCOGNITO) {
         options.addArguments("--incognito");
      }

      // Parámetro para no abrir el modo gráfico del navegador.
      if (WebDriverFactory.IS_HEADLESS) {
         options.addArguments("--headless=new");
      }

      // Deshabilitamos el mensaje delnavegador: "Un Software automatizado de pruebas está controlando Chrome."
      options.setExperimentalOption("excludeSwitches", Collections.singletonList("--enable-automation"));

      // Para no mostrar el dialogo de "donde guardar archivos". Bandera especial para el modo incognito a partir de
      // chrome 119
      options.addArguments("disable-features=DownloadBubble,DownloadBubbleV2");

      // Experimental OPTIONS
      HashMap<String, Object> prefs = new HashMap<>();
      // Para descargar archivos XML
      prefs.put("download.extensions_to_open", "application/xml");
      // Para no mostrar el dialogo de "donde guardar archivos"
      prefs.put("safebrowsing.enabled", false);
      prefs.put("download.prompt_for_download", false);
      prefs.put("download.directory_upgrade", true);
      prefs.put("profile.default_content_settings.popups", 0);
      prefs.put("profile.default_content_setting_values.automatic_downloads", 1);

      // Boolean that specifies if file selection dialogs are shown.
      prefs.put("select_file_dialogs.allowed", false);

      options.setExperimentalOption("prefs", prefs);

      WebDriverFactory.log.info("Fin set de opciones y preferencias");
      // Para lanzar en modo incognito
      if (WebDriverFactory.IS_REMOTE_SELENIUM_GRID) {
         DesiredCapabilities capabilities = new DesiredCapabilities();
         capabilities.setCapability(ChromeOptions.CAPABILITY, options);
         try {
            WebDriverFactory.log.info("Llamada a webDriver.capabilities(capabilities)");
            webDriver.capabilities(capabilities).remoteAddress(WebDriverFactory.HTTP_HUB_SELENIUM);
            WebDriverFactory.log.info("Llamada a webDriver.create()");
            return webDriver.create();
         }
         catch (Throwable e) {
            WebDriverFactory.log.error("Error en webDriver.create()", e);
         }
         return null;
      }
      else {
         WebDriverFactory.log.info("Llamada a new ChromeDriver(options)");
         return new ChromeDriver(options);
      }
   }

   private WebDriver firefox() {
      WebDriverManager webDriver = WebDriverManager.firefoxdriver();
      WebDriverManager webDriverConProxy = this.asignarProxy(webDriver);
      webDriverConProxy.setup();

      FirefoxOptions options = new FirefoxOptions();

      // Parámetro para no abrir el modo gráfico del navegador.
      if (WebDriverFactory.IS_HEADLESS) {
         options.addArguments("headless");
      }

      // Para lanzar en modo incognito
      if (WebDriverFactory.IS_MODO_INCOGNITO) {
         options.addArguments("-private");
      }

      if (WebDriverFactory.IS_REMOTE_SELENIUM_GRID) {
         DesiredCapabilities capabilities = new DesiredCapabilities();

         capabilities.setCapability(
               // FirefoxOptions.CAPABILITY, "moz:firefoxOptions"
               FirefoxOptions.FIREFOX_OPTIONS, options);

         return WebDriverManager.firefoxdriver().capabilities(capabilities)
               .remoteAddress(WebDriverFactory.HTTP_HUB_SELENIUM).create();
      }
      else {
         return new FirefoxDriver(options);
      }
   }

   private WebDriver edge() {
      WebDriverManager webDriver = WebDriverManager.edgedriver();
      WebDriverManager webDriverConProxy = this.asignarProxy(webDriver);
      webDriverConProxy.setup();

      EdgeOptions options = new EdgeOptions();

      // Para lanzar en modo incognito
      if (WebDriverFactory.IS_MODO_INCOGNITO) {
         options.addArguments("incognito");
      }

      // Parámetro para no abrir el modo gráfico del navegador.
      if (WebDriverFactory.IS_HEADLESS) {
         options.addArguments("headless");
      }

      if (WebDriverFactory.IS_REMOTE_SELENIUM_GRID) {
         DesiredCapabilities capabilities = new DesiredCapabilities();

         capabilities.setCapability(EdgeOptions.CAPABILITY, options);

         return WebDriverManager.edgedriver().capabilities(capabilities)
               .remoteAddress(WebDriverFactory.HTTP_HUB_SELENIUM).create();
      }
      else {
         return new EdgeDriver(options);
      }
   }

   /**
    * No se implementa como singleton debido a una posible ejecución en paralelo.
    *
    * @param navegador
    *           navegador web que se usara para la prueba
    * @return wd el webdriver
    */
   public WebDriver obtenerInstancia(Navegador navegador) throws Throwable {
      WebDriverFactory.log.info("Obteniendo una nueva instancia del navegador " + navegador.toString());
      WebDriver wd = null;
      try {
         switch (navegador) {
            case CHROME:
               wd = this.chrome();
               break;
            case FIREFOX:
               wd = this.firefox();
               break;
            case MSEDGE:
               wd = this.edge();
               break;
            default:
               wd = this.chrome();
         }
         WebDriverFactory.log.info("Instancia obtenida: " + wd.toString());
         if (wd == null) {
            throw new Throwable("WD null");
         }
         // this.driver = wd;
         // WebDriverFactory.log.info("WebDriverWait");
         // this.setWait(new WebDriverWait(wd,
         // Duration.ofSeconds(
         // Integer.parseInt(VariablesGlobalesTest.getPropiedad(PropiedadesTest.TIEMPO_RETRASO_LARGO.name()))),
         // Duration.ofMillis(100)));

         return wd;
      }
      catch (Throwable e) {
         WebDriverFactory.log.error("Error no controlado", e);
         throw e;
      }
   }
}