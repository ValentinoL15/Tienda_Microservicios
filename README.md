# Proyecto Tienda Ventas con Microservicios

Este proyecto es una aplicación de tienda online desarrollada bajo una **arquitectura de microservicios**. Su objetivo principal es servir como un ejemplo práctico para aprender y aplicar conceptos clave como Service Discovery, API Gateway, seguridad centralizada y tolerancia a fallos.

## 📜 Tabla de Contenidos
* [Arquitectura](#-arquitectura)
* [Tecnologías Utilizadas](#-tecnologías-utilizadas)
* [Características Principales](#-características-principales)
* [Prerrequisitos](#-prerrequisitos)
* [Instalación y Puesta en Marcha](#-instalación-y-puesta-en-marcha)
* [Configuración](#-configuración)
* [Seguridad con JWT](#-seguridad-con-jwt)
* [API Gateway](#-Api-Gateway)

---

## 🏗️ Arquitectura

El sistema sigue un patrón de microservicios, donde cada componente es independiente, desplegable y se comunica con los demás a través de la red.

Los componentes principales son:

* **API Gateway (Spring Cloud Gateway):** Es el punto de entrada único para todas las peticiones del cliente. Se encarga de enrutar las solicitudes al microservicio correspondiente, además de manejar la seguridad y la resiliencia.
* **Service Discovery (Eureka):** Implementa los patrones *Service Registry and Discovery*. Cada microservicio se registra en Eureka al iniciarse, permitiendo que los demás servicios se encuentren dinámicamente sin necesidad de hardcodear IPs y puertos.
* **Circuit Breaker (Resilience4j):** Integrado en el API Gateway para proporcionar tolerancia a fallos. Si un microservicio falla o no responde, el Circuit Breaker "abre el circuito" para evitar que el sistema colapse, devolviendo una respuesta de contingencia.

### Microservicios
1.  **Servicio de Usuarios:** Gestiona toda la lógica relacionada con los usuarios, incluyendo registro, autenticación y gestión de perfiles.
2.  **Servicio de Productos:** Maneja el catálogo de productos, stock, precios y detalles.
3.  **Servicio de Carrito:** Administra el carrito de compras de cada usuario, permitiendo agregar, eliminar y modificar productos.
4.  **Servicio de Ventas:** Maneja las ventas realizadas permitiendo ver el carrito con los productos.

---

## 🛠️ Tecnologías Utilizadas

* **Lenguaje:** Java 17+
* **Framework:** Spring Boot 3
* **Gestión de Microservicios:** Spring Cloud
    * **API Gateway:** Spring Cloud Gateway
    * **Service Discovery:** Spring Cloud Netflix Eureka
    * **Tolerancia a Fallos:** Resilience4j (Circuit Breaker)
* **Seguridad:** Spring Security y JSON Web Tokens (JWT)
* **Base de Datos:** MySQL
* **Contenerización:** Docker & Docker Compose
* **Gestión de Dependencias:** Maven

---

## ✨ Características Principales

* **Registro y Autenticación de Usuarios** con JWT.
* **Catálogo de Productos** con operaciones CRUD.
* **Gestión de Carrito de Compras** por usuario.
* **Procesamiento de Ventas** y registro de órdenes.
* **Enrutamiento centralizado** a través de un API Gateway.
* **Descubrimiento dinámico de servicios** para una comunicación desacoplada.
* **Alta disponibilidad y resiliencia** gracias al patrón Circuit Breaker.

---

## 📋 Prerrequisitos

Para poder levantar el entorno de desarrollo, necesitas tener instalado:

* Java Development Kit (JDK) 17 o superior.
* Apache Maven 3.8 o superior.
* Docker y Docker Compose.

---

## 🚀 Instalación y Puesta en Marcha

Sigue estos pasos para poner en funcionamiento el proyecto:

1.  **Clona el repositorio:**
    ```bash
    git clone [https://github.com/ValentinoL15/Tienda_Microservicios.git]
    cd Tienda_Microservicios/SistemaTiendaMicroservicios
    ```

2.  **Construye todos los módulos con Maven:**
    Este comando compilará cada microservicio y creará los archivos `.jar` necesarios.
    ```bash
    mvn clean install
    ```

3.  **Prepara tus variables de entorno:**
    Crea tu archivo de secretos a partir de la plantilla proporcionada. Este archivo guardará tus contraseñas y claves de forma local y segura.
    ```bash
    # Si usas Linux/Mac/Git Bash
    cp .env.example .env

    # Si usas CMD de Windows
    copy .env.example .env
    ```
    A continuación, abre el nuevo archivo `.env` con un editor de texto y configura tus contraseñas y claves privadas donde se indica.

4.  **Construye y levanta los contenedores:**
    Este único comando construirá las imágenes de Docker para cada microservicio y los levantará en el orden correcto. La primera vez puede tardar varios minutos.
    ```bash
    docker-compose up --build -d
    ```
    * El flag `--build` asegura que las imágenes se construyan con la última versión del código.
    * El flag `-d` ejecuta los contenedores en segundo plano (detached mode).

## ⚙️ Configuración

Las configuraciones principales se encuentran en los archivos `application.yml` de cada microservicio. Las propiedades más importantes a tener en cuenta son:

* **Conexión a la base de datos:** URL, usuario y contraseña de MySQL.
* **URL del servidor Eureka:** `eureka.client.serviceUrl.defaultZone` para que los servicios sepan dónde registrarse.
* **Secreto de JWT:** `jwt.secret` en el servicio de usuarios/autenticación para la firma de tokens.

Estas configuraciones ya están preparadas para funcionar con Docker Compose.

---

## 🔐 Seguridad con JWT

La seguridad está centralizada en el **API Gateway**. El flujo es el siguiente:

1.  El usuario envía sus credenciales (`username`, `password`) al endpoint de login en el **Servicio de Usuarios**.
2.  Si las credenciales son válidas, el servicio genera un **JWT** y lo devuelve al cliente.
3.  Para acceder a rutas protegidas, el cliente debe enviar el JWT en la cabecera `Authorization` de cada petición con el prefijo `Bearer `.
    ```
    Authorization: Bearer <tu-jwt-token>
    ```
4.  El **API Gateway** intercepta la petición, valida el token y, si es correcto, extrae la información del usuario (roles, ID) y la reenvía al microservicio correspondiente.

   ¡Y listo! La aplicación estará corriendo. El API Gateway estará disponible en el puerto que hayas configurado en tu `docker-compose.yml` (ej. `http://localhost:8085`).

---

## 🗺️ API Gateway

Todas las peticiones deben realizarse a través del API Gateway (`http://localhost:8085`).

