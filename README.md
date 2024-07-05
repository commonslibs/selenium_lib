# Librería común para pruebas basadas en SELENIUM

Esta librería define una serie de métodos comunes para utilizar en las pruebas funcionales basadas en Selenium.

IMPORTANTE: Se ha iniciado el desarrollo de la rama 0.3.0.0. Esta será una actualización importante dirigida a la optimización del uso de memoria del driver de selenium y otros mejoras de algunos metodos mas.

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
- TIEMPO_PARADA_TITULO_TEST. Obligatorio. Tiempo en segundos que la aplicación parará en la entrada para mostrar el titulo del test
- TIEMPO_RETRASO_CORTO. Obligatorio. Tiempo en segundos de retraso mas corto posible. Valor típico: 1.
- TIEMPO_RETRASO_MEDIO Obligatorio. Tiempo en segundos de retraso medio. Valor típico: 5.
- TIEMPO_RETRASO_LARGO Obligatorio. Tiempo en segundos de retraso mas largo posible. Valor típico: 10.
- ID_ELEMENTO_PROCESANDO. Opcional. Identificador del elemento que se intentará localizar antes de cada acción. Si está visible, se esperará a que desaparezca antes de realizar la siguiente acción.
- POSICION_CERTIFICADO. Posición del certificado digital a usar dentro de la lista de certificados que ofrece el navegador.

En el fichero properties que se vaya a usar podrán existir otras propiedades adicionales que se estimen necesarias.

## Publicacion de la libreria en MAVEN CENTRAL ⚙️

Prerrequisitos: Debemos tener instalado en la maquina desde la que se quiera realizar la subida de la librería a Maven Central el certificado digital (GPG) del usuario que puede subir la libreria Maven Central: En concreto el certificado que estamos usando para esta libreria pertenece al usuario: j o s e c . s e r r a n o a r r o b a g m a i l p u n t o c o m
Ejemplo: Para instalar el certificado digital en un PC, podemos hacer uso de la aplicación Kleopatra (tanto para windows como para Linux).

Posteriormente y desde consola, ejecutaremos el siguiente comando:

```
mvn clean deploy -PCENTRAL
```

URLs de repositorios donde se encuentra disponible la librería:
https://mvnrepository.com/artifact/io.github.commonslibs/selenium_lib
https://central.sonatype.com/namespace/io.github.commonslibs

## Analisis de librerias vulnerables. (dependency check) ⚙️

Antes de realizar una revisión de librerías vulnerables, es necesario que se compile el proyecto y se genere el JAR del mismo. Posteriormente, para generar el informe de librerías vulnerables debemos ejecutar el comando siguiente:

```
mvn clean package org.owasp:dependency-check-maven:check
```

Una vez ejecutado el comando anterior, se habrá generado el fichero _dependency-check-report.html_ en el directorio target con el resultado del análisis.

El proyecto usa la librería http://automation-remarks.com/video-recorder-java/ para guardar vídeos de las pruebas. Opcionalmente, es posible añadir propiedades adicionales al fichero properties usado, con las propiedades que permite el propio componente para refinar la configuración de los vídeos generados.

# Versiones - Changelog

0.2.1.2: 07/05/2024: Se actualizan diversas librerias y se incluyen diversos metodos para cuando se cierra el driver de selenium, ya que se ha encontrado un problema de memoria cuando se usa el driver de selenium

0.2.1.1: 12/03/2024: Se actualizan diversas librerias

0.2.1.0: 08/02/2024: Se corrige error de replace NULL

0.2.0.9: 08/02/2024: Se actualizan diversas librerias y se obtiene del sistema los valores del proxy

0.2.0.8: 12/01/2024: Se actualizan diversas librerias (testng, webdrivermanager y maven-compiler)

0.2.0.7: 20/12/2023: Se actualizan dos librerias (selenium y httpclient5) a las últimas versiones disponibles.

0.2.0.0: 27/07/2023: Pasamos a Java 17. Actualizacion de librerias (SB: 2.7.14), se realizan diversos comentarios en el código del proyecto y se mejora la gestión de los combos

0.1.0.8: 26/06/2023: Actualizacion de librerias y se incluyen mejoras para organizar menjor la información que se muestra en los informes de casos de pruebas y directorio de videos.

0.1.0.7: 21/06/2023: Se actualizan diversas del proyecto y seincluyen varias mejoras relativas al manejo de combos y generación de logs.
		     Se realiza limpieza de código fuente

0.1.0.6: 01/05/2023: Se actualizan tres librerias del proyecto

0.1.0.5: 01/04/2023: Nuevas mejoras sobre el manejador de objetos de la libreria

0.1.0.4: 28/03/2023: Se incluye el manejador para diversos componetes que no se habian probado
                     Actualización de librerias

0.1.0.3: 22/03/2023: El driver JDBC de Oracle pasa a ser el optimo para Oracle 19.
                     Se incluye propiedad: TIEMPO_PARADA_TITULO_TEST para forzar una parada en la entrada de la aplicación para mostrar titulo de test

0.1.0.2: 21/03/2023: Se incluye compatibilidad con Google Chrome 111

0.1.0.1: 15/03/2023: Pequeñas mejoras en las librerias del proyecto

0.1.0.0: 11/03/2023: Actualizacion de librerias

0.0.0.2: 23/02/2023: Primera version que se considera OK

0.0.0.1: 06/02/2023: Version de prueba

Prueba1
