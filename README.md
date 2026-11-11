Sistema de Gestión de Hogar Inteligente
Aplicación de escritorio para administrar y automatizar un hogar inteligente, desarrollada con Java 21, JavaFX, FXML y Maven.

El sistema demuestra cinco patrones de comportamiento obligatorios y cuatro patrones creacionales o estructurales adicionales mediante flujos funcionales relacionados con dispositivos inteligentes, comandos, estados, eventos, notificaciones y automatizaciones.

Tecnologías utilizadas
Java 21
JavaFX 21.0.6
FXML
Maven
Maven Wrapper
IntelliJ IDEA
Requisitos previos
Para ejecutar la aplicación se necesita:

JDK 21.
Conexión a Internet durante la primera compilación, para que Maven descargue las dependencias.
Windows, Linux o macOS con soporte para JavaFX.
El proyecto incluye Maven Wrapper, por lo que no es necesario instalar Maven por separado.

Configuración de Java en IntelliJ IDEA
El proyecto debe configurarse con:

Project SDK: JDK 21
Language level: 21
Module SDK: Project SDK
No se deben activar las características Preview de Java 21, porque el proyecto no utiliza --enable-preview.

Instalación detallada
1. Instalar JDK 21
   El proyecto requiere Java 21. Se recomienda utilizar una distribución estable de OpenJDK, por ejemplo:

Oracle OpenJDK 21.
Microsoft Build of OpenJDK 21.
Eclipse Temurin 21.
Amazon Corretto 21.
Después de instalar el JDK, abre PowerShell o CMD y ejecuta:

java --version
La salida debe indicar una versión 21, por ejemplo:

openjdk 21.0.7
También puedes comprobar el compilador:

javac --version
Resultado esperado:

javac 21.0.7
Si Windows muestra otra versión, configura la variable de entorno JAVA_HOME para que apunte a la carpeta del JDK 21.

Ejemplo:

JAVA_HOME=C:\Program Files\Java\jdk-21
Agrega también esta entrada a la variable Path:

%JAVA_HOME%\bin
Cierra y abre nuevamente la terminal después de modificar las variables de entorno.

2. Obtener el proyecto
   Opción A: clonar con Git
   Abre PowerShell o CMD en la carpeta donde quieras guardar el proyecto y ejecuta:

git clone https://github.com/Isi578/Parcial3.git
Entra en la carpeta creada:

cd Parcial3
Comprueba que la raíz contenga:

pom.xml
mvnw
mvnw.cmd
src/
Opción B: descargar desde GitHub
Abre el repositorio: https://github.com/Isi578/Parcial3
Selecciona Code.
Selecciona Download ZIP.
Extrae el archivo en una carpeta local.
Abre una terminal dentro de la carpeta extraída.
Comprueba que pom.xml y src estén en esa misma carpeta.
No ejecutes los comandos Maven desde src, src/main ni otra subcarpeta. Los comandos deben ejecutarse desde la raíz donde está pom.xml.

3. Verificar Maven Wrapper
   El proyecto incluye Maven Wrapper, por lo que no es necesario instalar Maven globalmente.

En Windows PowerShell ejecuta:

.\mvnw.cmd --version
En Windows CMD ejecuta:

mvnw.cmd --version
En Linux o macOS ejecuta:

./mvnw --version
La salida debe mostrar:

Una versión de Apache Maven.
Java version: 21.
La ruta del JDK en Java home.
Ejemplo:

Apache Maven 3.x.x
Java version: 21.0.7
Java home: C:\Program Files\Java\jdk-21
Si PowerShell no reconoce mvnw.cmd, utiliza siempre:

.\mvnw.cmd
El prefijo .\ indica que el archivo está en la carpeta actual.

4. Abrir el proyecto en IntelliJ IDEA
   Abre IntelliJ IDEA.
   Selecciona File → Open.
   Busca la carpeta raíz de Parcial3.
   Selecciona la carpeta o el archivo pom.xml.
   Pulsa Open.
   Si IntelliJ pregunta cómo abrir el proyecto, selecciona Open as Project.
   Espera a que Maven descargue las dependencias y termine la indexación.
   No abras solamente la carpeta src. IntelliJ debe importar el proyecto completo desde la ubicación que contiene pom.xml.

5. Configurar JDK 21 en IntelliJ IDEA
   Abre:

File → Project Structure → Project
Configura:

Project SDK: JDK 21
Language level: 21 - Record patterns, pattern matching for switch
No selecciones:

21 (Preview)
El proyecto no requiere características Preview.

Después abre:

File → Project Structure → Modules → Dependencies
Configura:

Module SDK: Project SDK 21
Pulsa:

Apply → OK
Si JDK 21 no aparece:

Abre File → Project Structure → SDKs.
Pulsa +.
Selecciona Download JDK.
Selecciona la versión 21.
Elige Oracle OpenJDK, Microsoft OpenJDK, Eclipse Temurin o Amazon Corretto.
Espera la descarga.
Asigna el JDK descargado al proyecto y al módulo.
6. Configurar Maven en IntelliJ IDEA
   Abre:

File → Settings
→ Build, Execution, Deployment
→ Build Tools
→ Maven
→ Runner
En JRE, selecciona:

Project JDK 21
Después abre:

Build Tools → Maven → Importing
En JDK for importer, selecciona:

Project JDK 21
Pulsa:

Apply → OK
Abre la ventana de Maven:

View → Tool Windows → Maven
Pulsa Reload All Maven Projects para recargar las dependencias y plugins.

7. Compilar el proyecto
   Desde PowerShell, ubicado en la raíz del proyecto, ejecuta:

.\mvnw.cmd clean compile
En CMD:

mvnw.cmd clean compile
En Linux o macOS:

./mvnw clean compile
La primera compilación puede tardar mientras Maven descarga JavaFX y los plugins.

El resultado esperado es:

BUILD SUCCESS
La orden clean elimina la carpeta generada target y compile vuelve a compilar las clases. No elimina ni modifica el código fuente.

8. Ejecutar la aplicación
   En Windows PowerShell:

.\mvnw.cmd javafx:run
En Windows CMD:

mvnw.cmd javafx:run
En Linux o macOS:

./mvnw javafx:run
La aplicación debe abrir una ventana titulada:

Smart Home Management System
La vista inicial debe mostrar el Dashboard y el menú lateral con acceso a:

Dashboard.
Devices.
Rooms.
Automations.
Notifications.
Settings.
9. Verificación rápida después de instalar
   Realiza esta comprobación:

Abre Devices.
Comprueba que se muestran cuatro dispositivos de demostración.
Selecciona Living Room Light.
Pulsa Turn On.
Comprueba que el estado cambia de OFF a ON.
Pulsa Undo.
Comprueba que regresa a OFF.
Abre Automations.
Pulsa Simulate Motion.
Comprueba que la luz objetivo queda encendida.
Abre Notifications.
Comprueba que existen eventos y notificaciones.
Abre Settings y cambia el nombre del hogar.
Regresa a Dashboard y verifica que el nombre se conserva.
10. Solución de problemas comunes
    PowerShell no reconoce mvnw.cmd
    Mensaje posible:

The term 'mvnw.cmd' is not recognized
Usa:

.\mvnw.cmd compile
PowerShell requiere .\ para ejecutar archivos de la carpeta actual.

Maven utiliza una versión diferente de Java
Ejecuta:

.\mvnw.cmd --version
Comprueba que aparezca:

Java version: 21
Si muestra Java 17, 25 u otra versión:

Configura JAVA_HOME con el JDK 21.
Configura Maven Runner con Project JDK 21.
Cierra y abre de nuevo IntelliJ y la terminal.
IntelliJ muestra CreateProcess error=5
Este error indica que Windows no permitió iniciar el ejecutable Java usado por el compilador interno.

Comprueba la ruta del JDK:

& "C:\Program Files\Java\jdk-21\bin\java.exe" --version
Después revisa:

File → Project Structure → Project
File → Project Structure → Modules → Dependencies
Ambos deben utilizar el mismo JDK 21.

Si Maven devuelve BUILD SUCCESS, pero Rebuild Project falla, revisa la caché del compilador de IntelliJ y la protección de Windows Defender o del antivirus antes de cambiar el código.

IntelliJ no reconoce clases o dependencias
Abre la ventana Maven.
Pulsa Reload All Maven Projects.
Espera la indexación.
Ejecuta:
.\mvnw.cmd clean compile
Si el problema continúa, utiliza File → Invalidate Caches y reinicia IntelliJ.
La vista muestra Loading error
Revisa la terminal y busca:

javafx.fxml.LoadException
Comprueba:

Que el archivo FXML exista en src/main/resources/fxml.
Que fx:controller coincida con el paquete y la clase.
Que cada fx:id coincida con un campo @FXML.
Que cada onAction coincida con un método @FXML.
Que module-info.java contenga:
opens controller to javafx.fxml;
La aplicación no encuentra main.fxml
La ruta esperada es:

src/main/resources/fxml/main.fxml
MainApp carga el recurso como:

/fxml/main.fxml
No muevas el archivo fuera de src/main/resources/fxml.

Los datos desaparecen al cerrar la aplicación
Este comportamiento es esperado. La implementación usa almacenamiento en memoria:

Los datos se comparten al navegar entre pantallas.
Los datos se reinician cuando la aplicación termina.
No se usa una base de datos.
La carpeta target aparece en el proyecto
La carpeta target es generada por Maven.

No debe subirse al repositorio. Comprueba que .gitignore incluya:

target/
*.class

Compilación y ejecución
Windows PowerShell
Compilar:

.\mvnw.cmd clean compile
Ejecutar:

.\mvnw.cmd javafx:run
Windows CMD
Compilar:

mvnw.cmd clean compile
Ejecutar:

mvnw.cmd javafx:run
Linux o macOS
Compilar:

./mvnw clean compile
Ejecutar:

./mvnw javafx:run
Estructura general del proyecto
src/main/
├── java/
│   ├── app/
│   │   ├── ApplicationContext.java
│   │   └── MainApp.java
│   ├── controller/
│   ├── model/
│   ├── patterns/
│   │   ├── command/
│   │   ├── composite/
│   │   ├── facade/
│   │   ├── factory/
│   │   ├── mediator/
│   │   ├── observer/
│   │   ├── singleton/
│   │   ├── state/
│   │   └── strategy/
│   ├── repository/
│   ├── service/
│   ├── util/enums/
│   └── module-info.java
└── resources/
├── css/
│   └── app.css
└── fxml/
Funcionalidades principales
Dashboard
El dashboard muestra un resumen del estado de la aplicación:

Cantidad total de dispositivos.
Cantidad de dispositivos activos.
Eventos registrados.
Notificaciones generadas.
Comandos que se pueden deshacer.
Automatizaciones registradas.
Ejecuciones realizadas por el mediador.
Último evento.
Última notificación.
Estado de los dispositivos registrados.
Resumen de los patrones disponibles.
Gestión de dispositivos
La pantalla Devices contiene dispositivos de demostración creados mediante Factory Method:

Luz inteligente.
Sensor de movimiento.
Termostato.
Cerradura inteligente.
Las acciones disponibles son:

Encender un dispositivo.
Apagar un dispositivo.
Bloquear una cerradura.
Desbloquear una cerradura.
Deshacer la última acción efectiva.
Consultar el estado actual.
Visualizar el último evento.
Visualizar la última notificación.
Las cerraduras usan las operaciones específicas Lock y Unlock. No se tratan como dispositivos de encendido y apagado.

Grupos de habitaciones
La pantalla Rooms demuestra Composite organizando los dispositivos en grupos funcionales:

Living Room
Security
El grupo Living Room permite encender o apagar conjuntamente los dispositivos compatibles.

El grupo Security mantiene separadas las cerraduras, debido a que sus operaciones válidas son bloquear y desbloquear.

Automatización por movimiento
La pantalla Automations demuestra un flujo integrado:

El sensor detecta movimiento.
El componente reporta MOTION_DETECTED.
Mediator recibe el evento.
Mediator busca las automatizaciones asociadas.
Strategy verifica la detección de movimiento.
La automatización ejecuta sus comandos.
Command enciende la luz objetivo.
State cambia la luz de OFF a ON.
Observer registra el evento y genera una notificación.
Eventos y notificaciones
La pantalla Notifications muestra dos reacciones independientes de Observer:

HistorialObserver conserva los eventos del dominio.
NotificacionObserver genera mensajes legibles.
Los mensajes de notificación pueden desactivarse sin detener el historial de eventos.

La pantalla permite:

Actualizar las listas.
Limpiar las notificaciones.
Limpiar el historial de eventos.
Limpiar toda la actividad de la sesión.
Activar o desactivar los mensajes de notificación.
Configuración compartida
La pantalla Settings permite modificar:

Nombre del hogar.
Dirección.
Unidad de temperatura.
Preferencia de notificaciones.
Preferencia de ahorro energético.
La configuración se comparte entre todas las pantallas mediante Singleton.

Patrones de comportamiento obligatorios
Observer
Ubicación:

src/main/java/patterns/observer/
Participantes principales:

Sujeto
Observador
DispositivoObservable
HistorialObserver
NotificacionObserver
Problema que resuelve:

Permite que diferentes componentes reaccionen a un evento de un dispositivo sin acoplar las operaciones del dispositivo directamente con el historial o las notificaciones.

Flujo funcional:

HogarFacade
→ crea Evento
→ DispositivoObservable
→ HistorialObserver
→ NotificacionObserver
Demostración:

Abrir Devices.
Encender una luz.
Abrir Notifications.
Comprobar que se registraron un evento y una notificación.
Desactivar las notificaciones.
Ejecutar otra acción.
Comprobar que el historial aumenta, pero las notificaciones no.
Strategy
Ubicación:

src/main/java/patterns/strategy/
Participantes principales:

EstrategiaAutomatizacion
EstrategiaPorHorario
EstrategiaPorMovimiento
EstrategiaPorTemperatura
Automatizacion
Problema que resuelve:

Permite cambiar la condición que determina cuándo se ejecutan los comandos de una automatización.

Estrategias implementadas:

Ejecución por horario.
Ejecución por movimiento.
Ejecución por umbral de temperatura.
Flujo visible:

EstrategiaPorMovimiento
→ evalúa SensorMovimiento
→ Automatizacion.executeCommands()
→ Command
→ Dispositivo
Demostración:

Abrir Automations.
Pulsar Simulate Motion.
Comprobar que la estrategia detecta movimiento.
Verificar que la luz objetivo queda encendida.
Command
Ubicación:

src/main/java/patterns/command/
Participantes principales:

Comando
ControladorComandos
EncenderDispositivoCommand
ApagarDispositivoCommand
BloquearPuertaCommand
DesbloquearPuertaCommand
Problema que resuelve:

Encapsula las acciones sobre dispositivos, conserva un historial de operaciones efectivas y permite deshacer la última acción.

Flujo funcional:

ControladorComandos
→ Comando.ejecutar()
→ receptor
→ cambio de estado
→ historial
Demostración de encendido:

Abrir Devices.
Seleccionar una luz.
Pulsar Turn On.
Comprobar que pasa a ON.
Pulsar Undo.
Comprobar que regresa a OFF.
Demostración de cerradura:

Seleccionar la cerradura.
Pulsar Lock.
Comprobar que queda LOCKED.
Pulsar Undo.
Comprobar que regresa a UNLOCKED.
State
Ubicación:

src/main/java/patterns/state/
Participantes principales:

EstadoDispositivo
EstadoEncendido
EstadoApagado
EstadoBloqueado
EstadoDesbloqueado
Dispositivo
CerraduraInteligente
Problema que resuelve:

Controla las transiciones válidas y evita operaciones incompatibles con el estado o tipo de dispositivo.

Transiciones implementadas:

OFF → ON
ON → OFF
UNLOCKED → LOCKED
LOCKED → UNLOCKED
Reglas:

Encender un dispositivo ya encendido no genera otra transición.
Apagar un dispositivo ya apagado no genera otra transición.
Bloquear una cerradura ya bloqueada no crea otra acción.
Desbloquear una cerradura ya desbloqueada no crea otra acción.
Una cerradura no admite operaciones de encendido o apagado.
Un dispositivo de energía no admite operaciones de bloqueo.
Mediator
Ubicación:

src/main/java/patterns/mediator/
Participantes principales:

MediadorHogar
HogarMediator
ComponenteHogar
Problema que resuelve:

Coordina componentes y automatizaciones sin crear una dependencia directa entre el sensor y los dispositivos afectados.

Flujo funcional:

ComponenteHogar
→ reportEvent("MOTION_DETECTED")
→ HogarMediator
→ Automatizacion
→ Strategy
→ Command
→ State
Demostración:

Abrir Automations.
Pulsar Simulate Motion.
Verificar que el sensor queda en ON.
Verificar que Motion detected cambia a YES.
Verificar que Mediator ejecuta una automatización.
Verificar que la luz objetivo cambia a ON.
Patrones adicionales
Factory Method
Categoría: Creacional
Peso de complejidad: 2

Ubicación:

src/main/java/patterns/factory/
Participantes:

DispositivoFactory
LuzFactory
SensorFactory
TermostatoFactory
CerraduraFactory
Problema que resuelve:

Permite crear diferentes subtipos de dispositivo mediante un proceso común de validación, delegando la construcción concreta en cada fábrica.

Flujo:

DispositivoFactory.createDevice()
→ valida datos comunes
→ buildDevice()
→ crea un subtipo concreto
Composite
Categoría: Estructural
Peso de complejidad: 3

Ubicación:

src/main/java/patterns/composite/
Participantes:

ComponenteHogar
DispositivoIndividual
GrupoDispositivos
Problema que resuelve:

Permite tratar de la misma forma un dispositivo individual y un grupo de dispositivos.

Demostración:

Abrir Devices para registrar los dispositivos.
Abrir Rooms.
Seleccionar Living Room.
Pulsar Turn On Group.
Verificar que todos los dispositivos compatibles quedan activos.
Pulsar Turn Off Group.
Verificar que quedan apagados.
Facade
Categoría: Estructural
Peso de complejidad: 1

Ubicación:

src/main/java/patterns/facade/HogarFacade.java
Problema que resuelve:

Proporciona a los controladores una API simplificada para registrar, consultar, encender, apagar, asignar y observar dispositivos.

Los controladores no necesitan conocer directamente:

La implementación del repositorio.
La construcción de los comandos de energía.
El proceso de publicación de eventos.
El registro de observadores.
Singleton
Categoría: Creacional
Peso de complejidad: 1

Ubicación:

src/main/java/patterns/singleton/ConfiguracionHogar.java
Problema que resuelve:

Garantiza una única configuración compartida durante toda la ejecución.

ApplicationContext también mantiene un único grafo de dependencias compartidas para:

Repositorio.
Servicio.
Historial de comandos.
Facade.
Observadores.
Mediator.
Configuración.
Cálculo del peso de patrones adicionales
Patrón	Categoría	Peso
Factory Method	Creacional	2
Composite	Estructural	3
Facade	Estructural	1
Singleton	Creacional	1
Total		7
La selección cumple las condiciones:

Al menos tres patrones adicionales.
Al menos un patrón creacional.
Al menos un patrón estructural.
Peso total igual o superior a 5.
Arquitectura
La aplicación utiliza una organización por capas:

Controlador JavaFX
↓
HogarFacade
↓
Servicios de aplicación
↓
Repositorios
↓
Modelos del dominio
Flujo de control de dispositivos:

Controller
→ Facade
→ Command
→ Dispositivo
→ State
→ Observer
Flujo de automatización:

Componente
→ Mediator
→ Automatizacion
→ Strategy
→ Command
→ State
→ Observer
Estado compartido de la aplicación
ApplicationContext crea y comparte:

InMemoryDispositivoRepository
DispositivoService
InMemoryHabitacionRepository
HabitacionService
InMemoryAutomatizacionRepository
AutomatizacionService
ControladorComandos
HogarFacade
HistorialObserver
NotificacionObserver
EventoService
NotificacionService
HogarMediator
ConfiguracionHogar
Esto evita que cada pantalla cree repositorios, servicios o historiales independientes.

Persistencia
La implementación actual utiliza almacenamiento en memoria.

Consecuencias:

Los datos se conservan mientras la aplicación está abierta.
Las pantallas comparten los mismos datos.
Los datos se reinician al cerrar la aplicación.
No se requiere instalar ni configurar una base de datos.
Secuencia recomendada para la demostración
Iniciar la aplicación.
Abrir Devices.
Mostrar los cuatro tipos de dispositivo creados con Factory Method.
Encender la luz.
Explicar la transición de State de OFF a ON.
Pulsar Undo para demostrar Command.
Seleccionar la cerradura.
Bloquearla y desbloquearla.
Abrir Rooms.
Encender y apagar el grupo Living Room.
Explicar Composite.
Abrir Automations.
Pulsar Simulate Motion.
Explicar cómo Mediator activa Strategy.
Mostrar que Command enciende la luz.
Abrir Notifications.
Mostrar eventos y notificaciones.
Desactivar las notificaciones.
Ejecutar otra acción.
Mostrar que los eventos continúan, pero los mensajes se detienen.
Abrir Settings.
Cambiar el nombre del hogar y guardarlo.
Regresar a Dashboard.
Mostrar que Singleton conservó el nombre.
Explicar los contadores y el estado compartido.
Limitaciones actuales
Los datos se almacenan únicamente en memoria.
Los dispositivos son simulados.
No existe conexión con dispositivos IoT físicos.
La interfaz permite registrar, editar y eliminar dispositivos, habitaciones y reglas de automatización basadas en movimiento. Las estrategias de horario y temperatura están implementadas en el dominio, pero no tienen formularios de configuración en la interfaz.
La regla integrada de demostración está protegida contra edición y eliminación para que el flujo de patrones siempre pueda mostrarse.
La demostración visual de automatización utiliza movimiento.
Las estrategias por horario y temperatura están implementadas en el dominio, pero no tienen una pantalla dedicada.
La autenticación y el acceso remoto están fuera del alcance.
La persistencia en base de datos está fuera del alcance.
Al cerrar la aplicación se reinician los datos.
Lista de verificación
Antes de entregar:

.\mvnw.cmd clean compile muestra BUILD SUCCESS.
.\mvnw.cmd javafx:run abre la aplicación.
Dashboard carga correctamente.
Devices carga correctamente.
Rooms carga correctamente.
Automations carga correctamente.
Notifications carga correctamente.
Settings carga correctamente.
Una luz puede encenderse y apagarse.
El último comando puede deshacerse.
Una cerradura puede bloquearse y desbloquearse.
Observer registra eventos.
Observer genera notificaciones.
Las notificaciones se pueden desactivar de forma independiente.
El movimiento activa una automatización.
Mediator registra la ejecución.
Composite controla el grupo Living Room.
La configuración se conserva al navegar entre pantallas.
Los cinco patrones de comportamiento son demostrables.
Los patrones adicionales suman un peso de 7.
El repositorio contiene commits progresivos.
Existen Pull Requests y merges visibles.
Los aportes de todos los integrantes son identificables.
El video está publicado en YouTube.
Todos los integrantes aparecen en el video.
La entrega incluye los enlaces del repositorio y del video.
Integrantes
Reemplazar esta tabla con los datos reales.

Integrante	Contribución principal
Integrante 1	Dominio, Command y State
Integrante 2	Observer, Strategy y Mediator
Integrante 3	JavaFX, FXML y documentación
Repositorio
Repositorio del proyecto

Video
Reemplazar al finalizar la grabación:

URL de YouTube: PENDIENTE
Autores
Proyecto desarrollado para el tercer parcial de patrones de diseño de comportamiento.
