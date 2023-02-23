# Librería común para pruebas basadas en SELENIUM

Esta librería define una serie de métodos comunes para utilizar en las pruebas funcionales basadas en Selenium.

## CONFIGURACIÓN DE LAS PRUEBAS

En el fichero de properties de la aplicación que se vaya a usar deberán existir las siguientes propiedades:
- HTTP_HUB_SELENIUM. URL del Hub de selenium. Normalmente: http://localhost:4444/wd/hub
- URL_APLICACION. URL en la que se encuentra la aplicación a probar.
- NAVEGADOR. Obligatorio. Posibles valores:
	- CHROME
	- FIREFOX
	- MSEDGE
- MAXIMIZAR. Opcional. Posibles valores:
	- TRUE. Es el valor por defecto. El navegador se maximizará hasta ocupar la pantalla completa al inicio de cada prueba.
	- FALSE
- TIEMPO_RETRASO_CORTO. Obligatorio. Tiempo en segundos de retraso mas corto posible. Valor típico: 1.
- TIEMPO_RETRASO_MEDIO Obligatorio. Tiempo en segundos de retraso medio. Valor típico: 5.
- TIEMPO_RETRASO_LARGO Obligatorio. Tiempo en segundos de retraso mas largo posible. Valor típico: 10.
- ID_ELEMENTO_PROCESANDO. Opcional. Identificador del elemento que se intentará localizar antes de cada acción. Si está visible, se esperará a que desaparezca antes de realizar la siguiente acción.
- POSICION_CERTIFICADO. Posición del certificado digital a usar dentro de la lista de certificados que ofrece el navegador.

En el fichero properties que se vaya a usar podrán existir otras propiedades adicionales que se estimen necesarias.

## Publicacion de la libreria en MAVEN CENTRAL ⚙️

Prerrequisitos: Debemos tener instalado en la maquina desde la que se quiera realizar la subida de la librería a Maven Central el certificado digital (GPG) del usuario que puede subir la libreria Maven Central: En concreto el certificado que estamos usando para esta libreria pertenece al usuario: josec.serrano@gmail.com
Ejemplo: Para instalar el certificado digital en un PC, podemos hacer uso de la aplicación Kleopatra (tanto para windows como para Linux).

Posteriormente y desde consola, ejecutaremos el siguiente comando: 

```
mvn clean deploy -PCENTRAL
```

## Analisis de librerias vulnerables. (dependency check) ⚙️

Antes de realizar una revisión de librerías vulnerables, es necesario que se compile el proyecto y se genere el JAR del mismo. Posteriormente, para generar el informe de librerías vulnerables debemos ejecutar el comando siguiente:

```
mvn clean package org.owasp:dependency-check-maven:check
```

Una vez ejecutado el comando anterior, se habrá generado el fichero _dependency-check-report.html_ en el directorio target con el resultado del análisis.

El proyecto usa la librería http://automation-remarks.com/video-recorder-java/ para guardar vídeos de las pruebas. Opcionalmente, es posible añadir propiedades adicionales al fichero properties usado, con las propiedades que permite el propio componente para refinar la configuración de los vídeos generados.

# Versiones

0.0.0.1: 06/02/2023: Version de prueba

0.0.0.2: 23/02/2023: Primera version que se considera OK.
