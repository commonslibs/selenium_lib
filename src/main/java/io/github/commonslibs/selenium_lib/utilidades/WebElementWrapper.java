package io.github.commonslibs.selenium_lib.utilidades;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.commonslibs.selenium_lib.excepciones.PruebaAceptacionExcepcion;
import io.github.commonslibs.selenium_lib.utilidades.VariablesGlobalesTest.PropiedadesTest;
import lombok.extern.slf4j.Slf4j;


/**
 * Clase que contiene las funcionalidades de selenium encapsulada, para usarlo de manera mas sencilla
 *
 * @author AGAPA
 */
@Slf4j
public class WebElementWrapper {

   private final WebDriver     driver;

   private static final String COLOR_AMARILLO         = "yellow";

   private static final String COLOR_AZUL             = "cyan";

   private static final int    NUMERO_MAXIMO_INTENTOS = 5;

   private static final String ERROR_CLICK            = "Error al hacer click";

   private static final String ERROR_DBL_CLICK        = "Error al hacer doble click";

   private static final String ERROR_MOSTRAR          = "Error al mostrar";

   private static final String ERROR_ESCRIBIR         = "Error al escribir";

   private static final String ERROR_SELECT           = "Error al seleccionar";

   private static final String ERROR_SELECTED         = "No está seleccionado";

   private static final String ERROR_VERIFICAR        = "Error al verificar";

   private static final String ERROR_PRESENTE         = "No está presente y debería";

   private static final String ERROR_NOT_PRESENTE     = "Está presente y no debería";

   private static final String ERROR_CHECKED          = "No está chequeado y debería";

   private static final String ERROR_NOT_CHECKED      = "Está chequeado y no debería";

   private static final String ERROR_ATRIBUTO         = "No existe el atributo";

   private static final String ERROR_ATRIBUTO_NE      = "Atributo con valor diferente al esperado";

   private static final String ERROR_UPLOAD           = "Error al hacer click para upload";

   private static final String ERROR_TEXTO            = "Error al obtener el texto";

   private static final String ERROR_UPLOAD_INPUT     = "Error al completar input de upload";

   private static final String ERROR_TABLA_FILA       = "Error al localizar la tabla";

   private static final String ERROR_TABLA_ID         = "Error al localizar la fila";

   private static final String ERROR_ESPERAR          = "Error espera";

   private static final String ERROR_PROCESANDO       = "La ventana procesando no desaparece";

   private static final String ERROR_NOT_VISIBLE      = "Error al esperar que no sea visible";

   private static final String ERROR_VISIBLE          = "Error al esperar que sea visible";

   private static final String ERROR_NOT_CLICKABLE    = "Error al esperar que el objeto sea clickable";

   private static final String ERROR_RESALTAR         = "No se pudo resaltar";

   public WebElementWrapper(WebDriver driver) {
      this.driver = driver;
   }

   /**
    * Acción de seleccionar un elemento con etiqueta @param labelValue de un combo (select desplegable) identificado por
    * el @param testObject, incluye una comprobación para verificar de que el elemento a desaparecido.
    *
    * @param testObject,
    *           objeto combo
    * @param labelValue,
    *           etiqueta que se quiere seleccionar del combo
    * @throws PruebaAceptacionExcepcion
    *            si se produce un error sobre el combo que se indica
    */
   public void seleccionarElementoComboConEspera(By testObject, String labelValue) throws PruebaAceptacionExcepcion {
      this.seleccionarElementoCombo(testObject, labelValue);
      // Enviamos el label con el texto, para saber si ese elemento ya se ha eliminado.
      this.esperarHastaQueElementoNoSeaVisible(By.xpath("//span[text() = '" + labelValue + "']"));
   }

   /**
    * Acción de seleccionar un elemento con etiqueta @param labelValue de un combo (select desplegable) identificado por
    * el @param testObject.
    *
    * @param testObject,
    *           objeto combo
    * @param labelValue,
    *           etiqueta que se quiere seleccionar del combo
    * @throws PruebaAceptacionExcepcion
    *            si se produce un error a la hora de seleccionar un elemento en el combo
    */
   public void seleccionarElementoCombo(By testObject, String labelValue) throws PruebaAceptacionExcepcion {
      this.click(testObject);
      this.click(By.xpath("//span[text() = '" + labelValue + "']"));
   }

   /**
    * Acción de hacer click en un elemento identificado por el @param testObject.
    *
    * @param testObject,
    *           objeto donde se hace click
    * @return elemento elemento
    * @throws PruebaAceptacionExcepcion
    *            si se produce error al hacer click en el objeto que se indica
    */
   public WebElement click(By testObject) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log.debug("click->" + testObject.toString());
      boolean conseguido = false;
      WebElement elemento = null;
      PruebaAceptacionExcepcion excepcion = null;
      for (int i = 1; !conseguido && i <= WebElementWrapper.NUMERO_MAXIMO_INTENTOS; i++) {
         try {

            elemento = this.esperaBasica(testObject);
            this.resaltaObjeto(elemento, WebElementWrapper.COLOR_AMARILLO);
            this.esperarHastaQueElementoClickable(elemento).click();
            conseguido = true;
         }
         catch (Exception e) {
            conseguido = false;
            excepcion = new PruebaAceptacionExcepcion(e.getMessage());
         }
      }
      if (!conseguido) {
         throw new PruebaAceptacionExcepcion(this.getMensajeError(WebElementWrapper.ERROR_CLICK, excepcion,
               Optional.of(testObject), Optional.empty()));
      }

      return elemento;
   }

   public WebElement clickSinColorear(By testObject) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log.debug("click->" + testObject.toString());
      boolean conseguido = false;
      WebElement elemento = null;
      PruebaAceptacionExcepcion excepcion = null;
      for (int i = 1; !conseguido && i <= WebElementWrapper.NUMERO_MAXIMO_INTENTOS; i++) {
         try {
            elemento = this.esperaBasica(testObject);
            // this.resaltaObjeto(elemento, WebElementWrapper.COLOR_AMARILLO)
            this.esperarHastaQueElementoClickable(elemento).click();
            conseguido = true;
         }
         catch (Exception e) {
            conseguido = false;
            excepcion = new PruebaAceptacionExcepcion(e.getMessage());
         }
      }
      if (!conseguido) {
         throw new PruebaAceptacionExcepcion(this.getMensajeError(WebElementWrapper.ERROR_CLICK, excepcion,
               Optional.of(testObject), Optional.empty()));
      }

      return elemento;
   }

   /**
    * Acción de hacer doble click en un elemento identificado por el @param testObject.
    *
    * @param testObject,
    *           objeto donde se hace doble click
    * @throws PruebaAceptacionExcepcion
    *            si se produce error al hacer doble click en el objeto que se indica
    */
   public void doubleClick(By testObject) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log.debug("doubleClick->" + testObject.toString());
      boolean conseguido = false;
      WebElement elemento = null;
      PruebaAceptacionExcepcion excepcion = null;
      for (int i = 1; !conseguido && i <= WebElementWrapper.NUMERO_MAXIMO_INTENTOS; i++) {
         try {
            elemento = this.esperaBasica(testObject);
            this.resaltaObjeto(elemento, WebElementWrapper.COLOR_AMARILLO);

            this.esperarHastaQueElementoClickable(elemento);
            Actions actions = new Actions(this.driver);
            actions.doubleClick(elemento).perform();
            conseguido = true;
         }
         catch (PruebaAceptacionExcepcion e) {
            conseguido = false;
            excepcion = e;
         }
      }
      if (!conseguido) {
         throw new PruebaAceptacionExcepcion(this.getMensajeError(WebElementWrapper.ERROR_DBL_CLICK, excepcion,
               Optional.of(testObject), Optional.empty()));
      }

   }

   /**
    * Acción de resaltar un elemento con un color determinado en el navegador si éste esta presente.
    *
    * @param testObject,
    *           objeto que se quiere resaltar
    * @throws PruebaAceptacionExcepcion
    *            si el elemento que se quiere resaltar no se encuentra
    */
   public void mostrarElementoPresente(By testObject) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log.debug("mostrarElementoPresente->" + testObject.toString());

      boolean conseguido = false;
      WebElement elemento = null;
      PruebaAceptacionExcepcion excepcion = null;
      for (int i = 1; !conseguido && i <= WebElementWrapper.NUMERO_MAXIMO_INTENTOS; i++) {
         try {
            elemento = this.esperarHastaQueElementoPresente(testObject);
            try {
               JavascriptExecutor js = (JavascriptExecutor) this.driver;
               js.executeScript("arguments[0].setAttribute('style', arguments[1]);", elemento, "display: block;");
            }
            catch (Exception e) {
               String mensaje = "El elemento " + elemento + " no puede ser mostrado";
               WebElementWrapper.log.error(mensaje);
               throw new PruebaAceptacionExcepcion(mensaje);
            }
            conseguido = true;
         }
         catch (PruebaAceptacionExcepcion e) {
            conseguido = false;
            excepcion = e;
         }
      }
      if (!conseguido) {
         throw new PruebaAceptacionExcepcion(this.getMensajeError(WebElementWrapper.ERROR_MOSTRAR, excepcion,
               Optional.of(testObject), Optional.empty()));
      }

   }

   /**
    * Acción de escribir texto en un elemento identificado por el @param testObject.
    *
    * @param testObject,
    *           objeto al que se le quiere indicar un texto
    * @param texto,
    *           texto que se quiere indicar en el campo de texto
    * @throws PruebaAceptacionExcepcion
    *            si se produce error al intentar escribir el texto en el objeto en cuestion
    */
   public void escribeTexto(By testObject, String texto) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log.debug("escribeTexto->" + testObject.toString() + ". Texto=" + texto);

      boolean conseguido = false;

      for (int i = 1; !conseguido && i <= WebElementWrapper.NUMERO_MAXIMO_INTENTOS; i++) {
         try {
            WebElement elemento = this.click(testObject);
            elemento.clear();
            for (int x = 0; x < texto.length(); x++) {
               elemento.sendKeys(texto.substring(x, x + 1));
            }
            elemento.sendKeys(Keys.TAB.toString()); // Hay veces que si no se pulsa TAB, no funciona
            conseguido = true;
         }
         catch (Exception e) {
            conseguido = false;
         }
      }
      if (!conseguido) {
         throw new PruebaAceptacionExcepcion(this.getMensajeError(WebElementWrapper.ERROR_ESCRIBIR, null,
               Optional.of(testObject), Optional.of(texto)));
      }
   }

   public void selectOptionByIndex(By testObject, Integer index) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log.debug("selectOptionByIndex->" + testObject.toString() + ". Index=" + index);
      boolean conseguido = false;
      WebElement elemento = null;
      PruebaAceptacionExcepcion excepcion = null;
      for (int i = 1; !conseguido && i <= WebElementWrapper.NUMERO_MAXIMO_INTENTOS; i++) {
         try {
            elemento = this.esperaBasica(testObject);
            this.resaltaObjeto(elemento, WebElementWrapper.COLOR_AMARILLO);

            Select comboBox = new Select(elemento);
            comboBox.selectByIndex(index);
            conseguido = true;
         }
         catch (PruebaAceptacionExcepcion e) {
            conseguido = false;
            excepcion = e;
         }
      }
      if (!conseguido) {
         throw new PruebaAceptacionExcepcion(this.getMensajeError(WebElementWrapper.ERROR_SELECT, excepcion,
               Optional.of(testObject), Optional.empty()));
      }
   }

   public void selectOptionByLabel(By testObject, String label) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log.debug("selectOptionByLabel->" + testObject.toString() + ". Label=" + label);
      boolean conseguido = false;
      WebElement elemento = null;
      PruebaAceptacionExcepcion excepcion = null;
      for (int i = 1; !conseguido && i <= WebElementWrapper.NUMERO_MAXIMO_INTENTOS; i++) {
         try {
            elemento = this.esperaBasica(testObject);
            this.resaltaObjeto(elemento, WebElementWrapper.COLOR_AMARILLO);

            Select comboBox = new Select(elemento);
            comboBox.selectByVisibleText(label);
            conseguido = true;
         }
         catch (PruebaAceptacionExcepcion e) {
            conseguido = false;
            excepcion = e;
         }
      }
      if (!conseguido) {
         throw new PruebaAceptacionExcepcion(this.getMensajeError(WebElementWrapper.ERROR_SELECT, excepcion,
               Optional.of(testObject), Optional.empty()));
      }
   }

   public void verifyOptionSelectedByLabel(By testObject, String label) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log.debug("verifyOptionSelectedByLabel->" + testObject.toString() + ". Label=" + label);
      boolean conseguido = false;
      WebElement elemento = null;
      PruebaAceptacionExcepcion excepcion = null;
      String etiquetaSeleccionada = "";
      for (int i = 1; !conseguido && i <= WebElementWrapper.NUMERO_MAXIMO_INTENTOS; i++) {
         try {
            elemento = this.esperaBasica(testObject);
            this.resaltaObjeto(elemento, WebElementWrapper.COLOR_AZUL);
            Select comboBox = new Select(elemento);
            etiquetaSeleccionada = comboBox.getFirstSelectedOption().getText().trim();
            conseguido = true;
         }
         catch (PruebaAceptacionExcepcion e) {
            conseguido = false;
            excepcion = e;
         }
      }
      if (!conseguido) {
         throw new PruebaAceptacionExcepcion(this.getMensajeError(WebElementWrapper.ERROR_SELECT, excepcion,
               Optional.of(testObject), Optional.empty()));
      }
      else { // Se ha obtenido el elemento web. Ahora se comprueba el valor del atributo
         if (!label.equals(etiquetaSeleccionada)) {
            throw new PruebaAceptacionExcepcion(this.getMensajeError(WebElementWrapper.ERROR_SELECTED, excepcion,
                  Optional.of(testObject), Optional.empty()));
         }
      }
   }

   public void selectOptionByValue(By testObject, String value) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log.debug("selectOptionByValue->" + testObject.toString() + ". Value=" + value);
      boolean conseguido = false;
      WebElement elemento = null;
      PruebaAceptacionExcepcion excepcion = null;
      for (int i = 1; !conseguido && i <= WebElementWrapper.NUMERO_MAXIMO_INTENTOS; i++) {
         try {
            elemento = this.esperaBasica(testObject);
            this.resaltaObjeto(elemento, WebElementWrapper.COLOR_AMARILLO);

            Select comboBox = new Select(elemento);
            comboBox.selectByValue(value);
            conseguido = true;
         }
         catch (PruebaAceptacionExcepcion e) {
            conseguido = false;
            excepcion = e;
         }
      }
      if (!conseguido) {
         throw new PruebaAceptacionExcepcion(this.getMensajeError(WebElementWrapper.ERROR_SELECT, excepcion,
               Optional.of(testObject), Optional.empty()));
      }
   }

   public String obtieneValorSeleccionadoEnCombo(By testObject, String selector) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log
            .debug("obtieneValorSeleccionadoEnCombo->" + testObject.toString() + ". Selector=" + selector);
      boolean conseguido = false;
      WebElement elemento = null;
      PruebaAceptacionExcepcion excepcion = null;
      String valorSeleccionado = "";
      for (int i = 1; !conseguido && i <= WebElementWrapper.NUMERO_MAXIMO_INTENTOS; i++) {
         try {
            elemento = this.esperaBasica(testObject);
            this.resaltaObjeto(elemento, WebElementWrapper.COLOR_AMARILLO);

            Select comboBox = new Select(elemento);
            valorSeleccionado = comboBox.getFirstSelectedOption().getText();
            conseguido = true;
         }
         catch (PruebaAceptacionExcepcion e) {
            conseguido = false;
            excepcion = e;
         }
      }
      if (!conseguido) {
         throw new PruebaAceptacionExcepcion(this.getMensajeError(WebElementWrapper.ERROR_SELECT, excepcion,
               Optional.of(testObject), Optional.empty()));
      }
      return valorSeleccionado;
   }

   public void selectOneMenu(String id, String label) throws PruebaAceptacionExcepcion {
      By selectOneMenu = By.id(id + "_label");
      By opcion = By.xpath("//*[@id='" + id + "_panel']/div/ul/li[text()='" + label + "']");
      this.click(selectOneMenu);
      WebElement we = this.click(opcion);
      if (we.isDisplayed()) {
         this.click(selectOneMenu); // Para cerrar y que no tape nada.
      }
   }

   public void verifyElementText(By testObject, String text) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log.debug("verifyElementText->" + testObject.toString() + ". Text=" + text);
      boolean conseguido = false;
      WebElement elemento = null;
      PruebaAceptacionExcepcion excepcion = null;

      for (int i = 1; !conseguido && i <= WebElementWrapper.NUMERO_MAXIMO_INTENTOS; i++) {
         try {
            elemento = this.esperaBasica(testObject);
            this.resaltaObjeto(elemento, WebElementWrapper.COLOR_AZUL);

            WebElementWrapper.log.debug("Comprobando que " + text + " es igual que " + elemento.getText());
            if (text.equals(elemento.getText())) {
               conseguido = true;
            }
         }
         catch (PruebaAceptacionExcepcion e) {
            excepcion = e;
         }
      }
      if (!conseguido) {
         throw new PruebaAceptacionExcepcion(this.getMensajeError(WebElementWrapper.ERROR_VERIFICAR, excepcion,
               Optional.of(testObject), Optional.of(text)));
      }
   }

   /**
    * Realiza una espera activa mientras exista el @param testObject con el @textoAComprobar. Puede provocar un bloqueo,
    * así que usarlo con cuidado
    *
    * @param testObject
    *           objeto
    * @param text
    *           texto que se quiere comprobar
    * @throws PruebaAceptacionExcepcion
    *            si se produce un error
    */
   public void waitUntilElementTextChangedOrDisapeared(By testObject, String text) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log
            .debug("waitUntilElementTextChangedOrDisapeared->" + testObject.toString() + ". Text=" + text);
      boolean conseguido = false;
      WebElement elemento = null;

      elemento = this.esperaBasica(testObject);
      this.resaltaObjeto(elemento, WebElementWrapper.COLOR_AMARILLO);

      while (!conseguido) {
         try {
            elemento = this.esperaBasica(testObject);
            // Desaparece o cambia.
            if (elemento == null || StringUtils.isBlank(elemento.getText()) || !text.equals(elemento.getText())) {
               conseguido = true;
            }

            if (conseguido) {
               this.resaltaObjeto(elemento, WebElementWrapper.COLOR_AZUL);
            }
         }
         catch (Exception e) {
            WebElementWrapper.log.debug("waitUntilElementTextChangedOrDisapeared", e);
            conseguido = false;
            WebElementWrapper.log.debug("se sigue intentando ...");
         }
      }
   }

   public boolean verifyElementPresentWithReturn(By testObject) throws PruebaAceptacionExcepcion {
      return this.verifyElementPresentWithReturn(testObject, WebElementWrapper.NUMERO_MAXIMO_INTENTOS);
   }

   public boolean verifyElementPresentWithReturn(By testObject, int numeroDeIntentos) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log
            .debug("verifyElementPresentWithReturn->" + testObject.toString() + " intentos: " + numeroDeIntentos);
      return this.checkObjetoPresente(testObject, numeroDeIntentos).isPresent();
   }

   public void verifyElementPresent(By testObject) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log.debug("verifyElementPresent->" + testObject.toString());
      if (this.checkObjetoPresente(testObject, WebElementWrapper.NUMERO_MAXIMO_INTENTOS).isEmpty()) {
         String mensaje = "El objeto con tipo selector no está presente cuando debería.";
         WebElementWrapper.log.error(mensaje);
         throw new PruebaAceptacionExcepcion(mensaje);
      }
   }

   public void verifyElementNotPresent(By testObject) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log.debug("verifyElementNotPresent->" + testObject.toString());
      if (!this.isObjetoNoPresente(testObject)) {
         String mensaje = "El objeto con tipo selector está presente cuando no debería.";
         WebElementWrapper.log.error(mensaje);
         throw new PruebaAceptacionExcepcion(mensaje);
      }
   }

   public void verifyElementChecked(By testObject) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log.debug("verifyElementChecked->" + testObject.toString());
      if (!this.isElementChecked(testObject)) {
         throw new PruebaAceptacionExcepcion(
               this.getMensajeError(WebElementWrapper.ERROR_CHECKED, null, Optional.of(testObject), Optional.empty()));
      }
   }

   public void verifyElementNotChecked(By testObject) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log.debug("verifyElementNotChecked->" + testObject.toString());
      if (this.isElementChecked(testObject)) {
         throw new PruebaAceptacionExcepcion(this.getMensajeError(WebElementWrapper.ERROR_NOT_CHECKED, null,
               Optional.of(testObject), Optional.empty()));
      }
   }

   public String getAttribute(By testObject, String atributo) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log.debug("getAttribute->" + testObject.toString() + ". Atributo=" + atributo);
      boolean conseguido = false;
      String valorAtributo = "";
      WebElement elemento = null;
      PruebaAceptacionExcepcion excepcion = null;
      for (int i = 1; !conseguido && i <= WebElementWrapper.NUMERO_MAXIMO_INTENTOS; i++) {
         try {
            elemento = this.esperaBasica(testObject);
            this.resaltaObjeto(elemento, WebElementWrapper.COLOR_AZUL);
            valorAtributo = elemento.getAttribute(atributo);
            conseguido = true;
         }
         catch (Exception e) {
            conseguido = false;
            excepcion = new PruebaAceptacionExcepcion(e.getLocalizedMessage());
         }
      }
      if (!conseguido) {
         throw new PruebaAceptacionExcepcion(this.getMensajeError(WebElementWrapper.ERROR_ATRIBUTO, excepcion,
               Optional.of(testObject), Optional.empty()));
      }
      return valorAtributo;
   }

   public void verifyElementAttributeValue(By testObject, String atributo, String value)
         throws PruebaAceptacionExcepcion {
      WebElementWrapper.log.debug(
            "verifyElementAttributeValue->" + testObject.toString() + ". Atributo=" + atributo + ". Value=" + value);
      boolean conseguido = false;
      String valorAtributo = "";
      WebElement elemento = null;
      PruebaAceptacionExcepcion excepcion = null;
      for (int i = 1; !conseguido && i <= WebElementWrapper.NUMERO_MAXIMO_INTENTOS; i++) {
         try {
            elemento = this.esperaBasica(testObject);
            this.resaltaObjeto(elemento, WebElementWrapper.COLOR_AZUL);
            valorAtributo = elemento.getAttribute(atributo);
            conseguido = true;

         }
         catch (PruebaAceptacionExcepcion e) {
            conseguido = false;
            excepcion = e;
         }
      }
      if (!conseguido) {
         throw new PruebaAceptacionExcepcion(this.getMensajeError(WebElementWrapper.ERROR_ATRIBUTO, excepcion,
               Optional.of(testObject), Optional.empty()));
      }
      else { // Se ha obtenido el elemento web. Ahora se comprueba el valor del atributo
         if (!value.equals(valorAtributo)) {
            throw new PruebaAceptacionExcepcion(this.getMensajeError(WebElementWrapper.ERROR_ATRIBUTO_NE, excepcion,
                  Optional.of(testObject), Optional.empty()));
         }
      }
   }

   /**
    * Click para subir un fichero.
    *
    * @param testObject
    *           valor para: test object
    * @param rutaFichero
    *           valor para: ruta fichero
    * @throws PruebaAceptacionExcepcion
    *            la prueba aceptacion excepcion
    */
   public void clickParaUploadFichero(By testObject, String rutaFichero) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log.debug("clickParaUploadFichero->" + testObject.toString() + ". RutaFichero=" + rutaFichero);
      boolean conseguido = false;
      WebElement elemento = null;
      PruebaAceptacionExcepcion excepcion = null;
      int tiempoMedio =
            Integer.parseInt(VariablesGlobalesTest.getPropiedad(PropiedadesTest.TIEMPO_RETRASO_MEDIO.name())) * 1000;
      int tiempoCorto =
            Integer.parseInt(VariablesGlobalesTest.getPropiedad(PropiedadesTest.TIEMPO_RETRASO_CORTO.name())) * 1000;

      for (int i = 1; !conseguido && i <= WebElementWrapper.NUMERO_MAXIMO_INTENTOS; i++) {
         try {
            elemento = this.esperaBasica(testObject);
            this.resaltaObjeto(elemento, WebElementWrapper.COLOR_AMARILLO);
            this.esperarHastaQueElementoClickable(elemento).click();
            StringSelection ss = new StringSelection(rutaFichero);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
            Robot robot = new Robot();
            robot.delay(tiempoMedio); // NOTE THE DELAY (500, 1000, 1500 MIGHT WORK FOR YOU)
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.delay(tiempoCorto); // NOTE THE DELAY (500, 1000, 1500 MIGHT WORK FOR YOU)
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);

            conseguido = true;
         }
         catch (PruebaAceptacionExcepcion e) {
            conseguido = false;
            excepcion = e;
         }
         catch (AWTException e) {
            conseguido = false;
            excepcion = new PruebaAceptacionExcepcion(e.getMessage());
         }
      }
      if (!conseguido) {
         throw new PruebaAceptacionExcepcion(this.getMensajeError(WebElementWrapper.ERROR_UPLOAD, excepcion,
               Optional.of(testObject), Optional.empty()));
      }
   }

   /**
    * Input upload fichero.
    *
    * @param inputTestObject
    *           valor para: input test object
    * @param rutaFichero
    *           valor para: ruta fichero
    * @throws PruebaAceptacionExcepcion
    *            la prueba aceptacion excepcion
    */
   public void inputUploadFichero(By inputTestObject, String rutaFichero) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log.debug("inputUploadFichero->" + inputTestObject.toString() + ". RutaFichero=" + rutaFichero);
      boolean conseguido = false;
      WebElement elemento = null;
      PruebaAceptacionExcepcion excepcion = null;

      for (int i = 1; !conseguido && i <= WebElementWrapper.NUMERO_MAXIMO_INTENTOS; i++) {
         try {
            elemento = this.esperarHastaQueElementoPresente(inputTestObject);
            elemento.sendKeys(rutaFichero);
            conseguido = true;
         }
         catch (PruebaAceptacionExcepcion e) {
            conseguido = false;
            excepcion = e;
         }
      }
      if (!conseguido) {
         throw new PruebaAceptacionExcepcion(this.getMensajeError(WebElementWrapper.ERROR_UPLOAD_INPUT, excepcion,
               Optional.of(inputTestObject), Optional.empty()));
      }
   }

   public String getText(By testObject) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log.debug("getText->" + testObject.toString());
      boolean conseguido = false;
      String cadena = "";
      WebElement elemento = null;
      PruebaAceptacionExcepcion excepcion = null;
      for (int i = 1; !conseguido && i <= WebElementWrapper.NUMERO_MAXIMO_INTENTOS; i++) {
         try {
            elemento = this.esperaBasica(testObject);
            this.resaltaObjeto(elemento, WebElementWrapper.COLOR_AMARILLO);
            cadena = elemento.getText();
            conseguido = true;
         }
         catch (PruebaAceptacionExcepcion e) {
            conseguido = false;
            excepcion = e;
         }
      }
      if (!conseguido) {
         throw new PruebaAceptacionExcepcion(this.getMensajeError(WebElementWrapper.ERROR_TEXTO, excepcion,
               Optional.of(testObject), Optional.empty()));
      }
      return cadena;
   }

   /**
    * Devuelve el numero de opciones presentes en el select cuyo objeto de test se pasa como parametro.
    *
    * @param testObject
    *           objeto de test consdierado.
    * @return el numero de opciones presentes en el select cuyo objeto de test se pasa como parametro.
    * @throws PruebaAceptacionExcepcion
    *            para la prueba de aceptacion
    */
   public int cuentaNumeroDeOpcionesEnSelect(By testObject) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log.debug("cuentaNumeroDeOpcionesEnSelect->" + testObject.toString());
      boolean conseguido = false;
      int numeroOpciones = 0;
      WebElement elemento = null;
      PruebaAceptacionExcepcion excepcion = null;
      for (int i = 1; !conseguido && i <= WebElementWrapper.NUMERO_MAXIMO_INTENTOS; i++) {
         try {
            elemento = this.esperaBasica(testObject);
            this.resaltaObjeto(elemento, WebElementWrapper.COLOR_AMARILLO);

            Select comboBox = new Select(elemento);
            List<WebElement> listOptionDropdown = comboBox.getOptions();
            numeroOpciones = listOptionDropdown.size();
            conseguido = true;
         }
         catch (PruebaAceptacionExcepcion e) {
            conseguido = false;
            excepcion = e;
         }
      }
      if (!conseguido) {
         throw new PruebaAceptacionExcepcion(this.getMensajeError(WebElementWrapper.ERROR_SELECT, excepcion,
               Optional.of(testObject), Optional.empty()));
      }
      return numeroOpciones;
   }

   /**
    * Obtiene la fila de una tabla que contiene el @param texto en alguna de sus columnas. Las filas están numeradas de
    * 0 en adelante. Si no encuentra el texto en ninguna fila, devuelve -1.
    *
    * @param testObject
    *           valor para: Locator del elemento tabla
    * @param texto
    *           valor para: texto
    * @return int numero de la fila
    * @throws PruebaAceptacionExcepcion
    *            la prueba aceptacion excepcion
    */
   public int obtenerFilaConTextoEnTabla(By testObject, String texto) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log.debug("obtenerFilaConTextoEnTabla->" + testObject.toString() + ". Texto=" + texto);
      int fila = -1;
      boolean conseguido = false;
      WebElement table = null;
      List<WebElement> rows = null;
      PruebaAceptacionExcepcion excepcion = null;
      String textoEsperado = texto;

      for (int i = 1; !conseguido && i <= WebElementWrapper.NUMERO_MAXIMO_INTENTOS; i++) {
         try {
            // Localizar el body de la tabla
            table = this.esperaBasica(testObject);
            conseguido = true;
         }
         catch (PruebaAceptacionExcepcion e) {
            conseguido = false;
            excepcion = e;
         }
      }

      if (conseguido && table != null) {
         // Se obtienen todas las filas de la tabla
         rows = table.findElements(By.tagName("tr"));
         // Se recorren todas las filas de la tabla
         for (int i = 0; i < rows.size(); i++) {
            // Se recorre columna por columna buscando el texto esperado
            List<WebElement> cols = rows.get(i).findElements(By.tagName("td"));
            for (WebElement col : cols) {
               if (col.getText().trim().equalsIgnoreCase(textoEsperado)) {
                  fila = i;
                  break;
               }
            }

         }
      }
      else {
         throw new PruebaAceptacionExcepcion(this.getMensajeError(WebElementWrapper.ERROR_TABLA_ID, excepcion,
               Optional.of(testObject), Optional.empty()));
      }

      if (fila == -1) {
         throw new PruebaAceptacionExcepcion(this.getMensajeError(WebElementWrapper.ERROR_TABLA_FILA, excepcion,
               Optional.of(testObject), Optional.of("idBodyTabla: " + table.getAttribute("id") + texto)));
      }

      return fila;
   }

   public int obtieneNumeroDeFilasListado(By testObject) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log.debug("obtieneNumeroDeFilasListado->" + testObject.toString());
      boolean conseguido = false;
      WebElement table = null;
      List<WebElement> rows = null;
      PruebaAceptacionExcepcion excepcion = null;
      int numeroDeFilas = 0;

      for (int i = 1; !conseguido && i <= WebElementWrapper.NUMERO_MAXIMO_INTENTOS; i++) {
         try {

            // Localizar el body de la tabla
            // FIXME : No hay forma de esperar después del evento filtrado, debido al evento
            // onKeyUp que mete un retraso aleatorio en la carga
            // del listado.
            Thread.sleep(
                  Integer.parseInt(VariablesGlobalesTest.getPropiedad(PropiedadesTest.TIEMPO_RETRASO_CORTO.name()))
                        * 1000L);
            table = this.esperaBasica(testObject);
            rows = table.findElements(By.tagName("tr"));
            numeroDeFilas = rows.size();
            conseguido = true;
         }
         catch (PruebaAceptacionExcepcion e) {
            excepcion = e;
         }
         catch (InterruptedException e) {
            WebElementWrapper.log.error(e.getLocalizedMessage());
         }
      }

      if (!conseguido) {
         throw new PruebaAceptacionExcepcion(this.getMensajeError(WebElementWrapper.ERROR_SELECT, excepcion,
               Optional.of(testObject), Optional.empty()));
      }

      return numeroDeFilas;
   }

   public void esperarFilaUnicaListado(By testObject) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log.debug("esperarFilaUnicaListado->" + testObject.toString());
      this.esperarListadoConFilas(testObject, 1);
   }

   public void esperarListadoConFilas(By testObject, int numeroFilasEsperadas) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log.debug(
            "esperarListadoConFilas->" + testObject.toString() + ". NumeroFilasEsperadas=" + numeroFilasEsperadas);
      boolean conseguido = false;
      WebElement table = null;
      List<WebElement> rows = null;
      PruebaAceptacionExcepcion excepcion = null;
      int numeroDeFilas = 0;

      for (int i = 1; !conseguido && i <= WebElementWrapper.NUMERO_MAXIMO_INTENTOS; i++) {
         try {
            // Localizar el body de la tabla
            // FIXME : No hay forma de esperar después del evento filtrado, debido al evento
            // onKeyUp que mete un retraso aleatorio en la carga
            // del listado.
            this.esperaIncondicional(
                  Integer.parseInt(VariablesGlobalesTest.getPropiedad(PropiedadesTest.TIEMPO_RETRASO_CORTO.name())));
            table = this.esperaBasica(testObject);
            rows = table.findElements(By.tagName("tr"));
            numeroDeFilas = rows.size();
            if (numeroDeFilas == numeroFilasEsperadas) {
               conseguido = true;
            }
         }
         catch (PruebaAceptacionExcepcion e) {
            excepcion = e;
         }
      }

      if (!conseguido) {
         throw new PruebaAceptacionExcepcion(this.getMensajeError(WebElementWrapper.ERROR_TABLA_ID, excepcion,
               Optional.of(testObject), Optional.empty()));
      }
   }

   public void esperaIncondicional(int segundos) {
      WebElementWrapper.log.debug("esperaIncondicional-> " + segundos + " segundos");
      this.esperaIncondicionalMilisegundos(segundos * 1000);
   }

   public void esperaIncondicionalMilisegundos(int milisegundos) {
      WebElementWrapper.log.debug("esperaIncondicionalMilisegundos-> " + milisegundos + " segundos");
      try {
         Thread.sleep(milisegundos);
      }
      catch (InterruptedException e) {
         // Seguramente nunca se produzca esta excepción
         WebElementWrapper.log.error("Error en espera activa");
      }
   }

   /**
    * Posicionar en porsición más alta de la página
    */
   public void scrollTopPagina() {
      WebElementWrapper.log.debug("scrollTopPagina");
      ((JavascriptExecutor) this.driver).executeScript("window.scrollTo(0, -document.body.scrollHeight)");
   }

   /**
    * Devuelve el nº de elementos de la @param tabla
    *
    * @param tabla,
    *           identificador de la tabla de la que se quiere tener el nº de registros
    * @return nº de registros de la @param tabla
    * @throws PruebaAceptacionExcepcion
    *            prueba de aceptacion
    */
   public int obtenerNumeroRegistrosTabla(By tabla) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log.debug("obtenerNumeroRegistrosTabla->" + tabla.toString());
      By spanPaginator = By.cssSelector("span.ui-paginator-current");
      return this.obtenerNumeroRegistrosTabla(tabla, spanPaginator, 1);
   }

   /**
    * Devuelve el número de elementos de la tabla tomándolo de currentPageReportTemplate
    *
    * @param tabla
    *           valor para: tabla
    * @param spanPaginator
    *           valor para: elemento que muestra el texto de currentPageReportTemplate
    * @param position
    *           valor para: posición del número de elementos en el currentPageReportTemplate. Ej: Total {totalRecords}
    *           elementos - Página {currentPage} de {totalPages}. En este caso el totalRecords está en la posición 1
    *           (empieza por el 0).
    * @return int Número de elementos en la tabla
    * @throws PruebaAceptacionExcepcion
    *            la prueba aceptacion excepcion
    */
   public int obtenerNumeroRegistrosTabla(By tabla, By spanPaginator, int position) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log.debug("obtenerNumeroRegistrosTabla->" + tabla.toString());
      WebElement t = this.esperaBasica(tabla);

      WebElement paginador = t.findElement(spanPaginator);

      String cadena = paginador.getText();
      String[] aux = cadena.split(" ");
      String res = aux[position];

      return Integer.valueOf(res);
   }

   public void esperarHastaQueElementoNoPresente(By testObject) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log.debug("esperarHastaQueElementoNoPresente->" + testObject.toString());
      WebDriverWait wait = new WebDriverWait(this.driver,
            Duration.ofSeconds(
                  Integer.parseInt(VariablesGlobalesTest.getPropiedad(PropiedadesTest.TIEMPO_RETRASO_LARGO.name()))),
            Duration.ofMillis(100));
      try {
         wait.until(ExpectedConditions.not(ExpectedConditions.presenceOfAllElementsLocatedBy(testObject)));
      }
      catch (TimeoutException e) {
         throw new PruebaAceptacionExcepcion(
               this.getMensajeError(WebElementWrapper.ERROR_ESPERAR, null, Optional.of(testObject), Optional.empty()));
      }
   }

   private boolean isElementChecked(By testObject) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log.debug("isElementChecked->" + testObject.toString());
      boolean conseguido = false;
      WebElement elemento = null;
      for (int i = 1; !conseguido && i <= WebElementWrapper.NUMERO_MAXIMO_INTENTOS; i++) {
         try {
            elemento = this.esperaBasica(testObject);
            this.resaltaObjeto(elemento, WebElementWrapper.COLOR_AZUL);
            conseguido = elemento.isSelected();
         }
         catch (PruebaAceptacionExcepcion e) {
            conseguido = false;
         }
      }
      if (!conseguido) {
         this.getMensajeError(WebElementWrapper.ERROR_NOT_CHECKED, null, Optional.of(testObject), Optional.empty());
      }
      return conseguido;
   }

   private Optional<WebElement> checkObjetoPresente(By testObject, int numeroDeIntentos)
         throws PruebaAceptacionExcepcion {
      WebElementWrapper.log.debug("isObjetoPresente->" + testObject.toString());
      WebElement elemento = null;
      for (int i = 1; i <= numeroDeIntentos; i++) {
         try {
            elemento = this.esperaBasica(testObject);
            this.resaltaObjeto(elemento, WebElementWrapper.COLOR_AZUL);
            return Optional.of(elemento);
         }
         catch (PruebaAceptacionExcepcion e) {
            WebElementWrapper.log.debug("Reintentando checkObjetoPresente");
         }
      }
      WebElementWrapper.log.warn(
            this.getMensajeError(WebElementWrapper.ERROR_PRESENTE, null, Optional.of(testObject), Optional.empty()));
      return Optional.empty();
   }

   private boolean isObjetoNoPresente(By testObject) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log.debug("isObjetoNoPresente->" + testObject.toString());
      boolean conseguido = false;
      try {
         this.esperaBasica(testObject);
      }
      catch (PruebaAceptacionExcepcion e) {
         conseguido = true;
      }
      if (!conseguido) {
         WebElementWrapper.log.warn(this.getMensajeError(WebElementWrapper.ERROR_NOT_PRESENTE, null,
               Optional.of(testObject), Optional.empty()));
      }
      return conseguido;
   }

   public void esperarDesaparezcaProcesando(WebDriver driver) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log.debug("esperarDesaparezcaProcesando");

      String idElementoProcesando = null;
      try {
         idElementoProcesando = VariablesGlobalesTest.getPropiedad(PropiedadesTest.ID_ELEMENTO_PROCESANDO.name());
      }
      catch (IllegalArgumentException e) {
         WebElementWrapper.log.trace("ID_ELEMENTO_PROCESANDO no definido en fichero properties");
      }
      if (idElementoProcesando != null) {
         By by = By.id(idElementoProcesando);
         this.esperarDesaparezcaElemento(driver, by);
      }
   }

   public void esperarDesaparezcaElemento(WebDriver driver, By elemento) throws PruebaAceptacionExcepcion {

      driver.manage().timeouts().implicitlyWait(Duration.ofMillis(1));
      List<WebElement> elementos = driver.findElements(elemento);
      try {
         if (!elementos.isEmpty() && elementos.get(0).isDisplayed()) {
            int tiempo = 0;
            while (elementos.get(0).isDisplayed()) {
               try {
                  Thread.sleep(100);
               }
               catch (InterruptedException e) {
                  // Seguramente nunca se produzca esta excepción
                  WebElementWrapper.log.error("Error al parar el procesamiento del hilo de ejecución");
               }
               tiempo += 100;
               if (tiempo > Integer.parseInt(
                     VariablesGlobalesTest.getPropiedad(PropiedadesTest.TIEMPO_RETRASO_MEDIO.name())) * 1000) {
                  throw new PruebaAceptacionExcepcion(this.getMensajeError(WebElementWrapper.ERROR_PROCESANDO, null,
                        Optional.empty(), Optional.empty()));
               }
            }
         }
      }
      catch (StaleElementReferenceException e) {
         WebElementWrapper.log.debug("La ventana procesando ya no está visible");
      }

   }

   public WebElement esperaBasica(By testObject) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log.debug("esperaBasica->" + testObject.toString());
      this.esperarDesaparezcaProcesando(this.driver);
      this.esperarHastaQueElementoPresente(testObject);
      return this.esperarHastaQueElementoVisible(testObject);
   }

   private WebElement esperarHastaQueElementoVisible(By testObject) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log.debug("esperarHastaQueElementoVisible->" + testObject.toString());
      WebDriverWait wait = new WebDriverWait(this.driver,
            Duration.ofSeconds(
                  Integer.parseInt(VariablesGlobalesTest.getPropiedad(PropiedadesTest.TIEMPO_RETRASO_MEDIO.name()))),
            Duration.ofMillis(100));
      try {
         return wait.until(ExpectedConditions.visibilityOfElementLocated(testObject));
      }
      catch (TimeoutException e) {
         throw new PruebaAceptacionExcepcion(this.getMensajeError(WebElementWrapper.ERROR_NOT_VISIBLE, null,
               Optional.of(testObject), Optional.empty()));
      }
   }

   /**
    * Método que permite saber si el @param ha dejado de ser visible. En caso de que siga visible lanza un error.
    *
    * @param testObject
    *           del objeto
    * @throws PruebaAceptacionExcepcion
    *            prueba de aceptacion
    */
   private void esperarHastaQueElementoNoSeaVisible(By testObject) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log.debug("esperarHastaQueElementoNoSeaVisible->" + testObject.toString());
      WebDriverWait wait = new WebDriverWait(this.driver,
            Duration.ofSeconds(
                  Integer.parseInt(VariablesGlobalesTest.getPropiedad(PropiedadesTest.TIEMPO_RETRASO_MEDIO.name()))),
            Duration.ofMillis(100));
      try {
         wait.until(ExpectedConditions.invisibilityOf(this.driver.findElement(testObject)));

      }
      catch (TimeoutException e) {
         throw new PruebaAceptacionExcepcion(
               this.getMensajeError(WebElementWrapper.ERROR_VISIBLE, null, Optional.of(testObject), Optional.empty()));
      }
   }

   private WebElement esperarHastaQueElementoPresente(By testObject) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log.debug("esperarHastaQueElementoPresente->" + testObject.toString());
      WebDriverWait wait = new WebDriverWait(this.driver,
            Duration.ofSeconds(
                  Integer.parseInt(VariablesGlobalesTest.getPropiedad(PropiedadesTest.TIEMPO_RETRASO_MEDIO.name()))),
            Duration.ofMillis(100));
      try {
         return wait.until(ExpectedConditions.presenceOfElementLocated(testObject));
      }
      catch (TimeoutException e) {
         throw new PruebaAceptacionExcepcion(
               this.getMensajeError(WebElementWrapper.ERROR_PRESENTE, null, Optional.of(testObject), Optional.empty()));
      }
   }

   private WebElement esperarHastaQueElementoClickable(WebElement testObject) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log.debug("esperarHastaQueElementoClickable->" + testObject.getAttribute("id"));
      WebDriverWait wait = new WebDriverWait(this.driver,
            Duration.ofSeconds(
                  Integer.parseInt(VariablesGlobalesTest.getPropiedad(PropiedadesTest.TIEMPO_RETRASO_MEDIO.name()))),
            Duration.ofMillis(100));
      try {
         return wait.until(ExpectedConditions.elementToBeClickable(testObject));
      }
      catch (TimeoutException e) {
         throw new PruebaAceptacionExcepcion(this.getMensajeError(WebElementWrapper.ERROR_NOT_CLICKABLE, null,
               Optional.empty(), Optional.of("Id: " + testObject.getAttribute("id"))));
      }
   }

   /**
    * Resalta el @element de @param color, útil para seguir una traza visual.
    *
    * @param element
    *           identificador del elemento para resaltar
    * @param color
    *           color con el que resaltara
    * @throws PruebaAceptacionExcepcion
    *            prueba de aceptacion
    */
   private void resaltaObjeto(WebElement element, String color) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log.trace("resaltaObjeto->" + element.toString() + ". Color=" + color);
      try {
         JavascriptExecutor js = (JavascriptExecutor) this.driver;
         js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element,
               "background: " + color + "; color: black; border: 3px solid black;");
      }
      catch (Exception e) {
         throw new PruebaAceptacionExcepcion(this.getMensajeError(WebElementWrapper.ERROR_RESALTAR, null,
               Optional.empty(), Optional.of("Elemento: " + element.toString())));
      }
   }

   /**
    * Comprueba si el elemento extiste y luego obtiene el texto. El obejeto no tiene porque estar presente.
    *
    * @param testObject
    *           identificador del objeto
    * @return si no existe el elemento devuelve "", sino el valor.
    * @throws PruebaAceptacionExcepcion
    *            para la prueba de aceptacion
    */
   public String obtenerTextoElemento(By testObject) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log.debug("obtenerTextoElemento->" + testObject.toString());
      Optional<WebElement> owe = this.checkObjetoPresente(testObject, WebElementWrapper.NUMERO_MAXIMO_INTENTOS);
      if (owe.isPresent()) {
         return owe.get().getText();
      }
      else {
         throw new PruebaAceptacionExcepcion(
               this.getMensajeError(WebElementWrapper.ERROR_PRESENTE, null, Optional.of(testObject), Optional.empty()));
      }
   }

   public boolean verifyElementVisibleWithReturn(By testObject) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log.debug("verifyElementPresentWithReturn->" + testObject.toString());
      return this.verifyElementPresentWithReturn(testObject, WebElementWrapper.NUMERO_MAXIMO_INTENTOS);
   }

   public boolean verificarElementoVisible(By testObject) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log.debug("verificarElementoVisible->" + testObject.toString());
      WebDriverWait wait = new WebDriverWait(this.driver, Duration.ofMillis(100), Duration.ofMillis(100));
      try {
         wait.until(ExpectedConditions.visibilityOfElementLocated(testObject));
      }
      catch (TimeoutException e) {
         return false;
      }
      return true;
   }

   public WebElement obtenerElemento(By testObject) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log.debug("obtenerElemento->" + testObject.toString());
      Optional<WebElement> owe = this.checkObjetoPresente(testObject, WebElementWrapper.NUMERO_MAXIMO_INTENTOS);
      if (owe.isPresent()) {
         return owe.get();
      }
      else {
         throw new PruebaAceptacionExcepcion(
               this.getMensajeError(WebElementWrapper.ERROR_PRESENTE, null, Optional.of(testObject), Optional.empty()));
      }
   }

   public void seleccionaDesplegableByLabel(String idDivSelect, String labelBuscada) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log.debug("seleccionaDesplegableByLabel->" + idDivSelect + ", " + labelBuscada);
      By label = By.id(idDivSelect + "_label");
      By filtro = By.id(idDivSelect + "_filter");
      this.seleccionaDesplegableByLabel(labelBuscada, label, filtro);
   }

   public void seleccionaDesplegableByLabel(String labelBuscada, By label, By filtro) throws PruebaAceptacionExcepcion {
      WebElementWrapper.log.debug("seleccionaDesplegableByLabel->" + label.toString());
      boolean conseguido = false;
      WebElement elementoLabel = null;
      PruebaAceptacionExcepcion excepcion = null;
      for (int i = 1; !conseguido && i <= WebElementWrapper.NUMERO_MAXIMO_INTENTOS; i++) {
         try {
            elementoLabel = this.esperaBasica(label);
            this.resaltaObjeto(elementoLabel, WebElementWrapper.COLOR_AMARILLO);
            this.esperarHastaQueElementoClickable(elementoLabel).click();
            this.esperarHastaQueElementoVisible(filtro).click();
            this.escribeTexto(filtro, labelBuscada);
            conseguido = true;
         }
         catch (Exception e) {
            conseguido = false;
            excepcion = new PruebaAceptacionExcepcion(e.getMessage());
         }
      }
      if (!conseguido) {
         throw new PruebaAceptacionExcepcion(
               this.getMensajeError(WebElementWrapper.ERROR_SELECT, excepcion, Optional.of(label), Optional.empty()));
      }
   }

   private String getMensajeError(String preMsg, PruebaAceptacionExcepcion excepcion, Optional<By> testObject,
         Optional<String> moreInfo) {
      StringBuilder msg = new StringBuilder("");
      msg.append(preMsg);
      if (testObject.isPresent()) {
         msg.append(" - Elemento: ").append(testObject.get().toString());
      }
      if (moreInfo.isPresent()) {
         msg.append(" - ").append(moreInfo.get());
      }
      if (excepcion != null) {
         msg.append(" - Motivo error: ").append(excepcion.getLocalizedMessage());
      }
      String mensaje = msg.toString();
      WebElementWrapper.log.error(mensaje);
      return mensaje;
   }
}
