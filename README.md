# 🛒 Ecommerce - Productos Ecológicos

<p align="center">

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge\&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.x-6DB33F?style=for-the-badge\&logo=springboot)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge\&logo=mysql)
![Hibernate](https://img.shields.io/badge/Hibernate-JPA-59666C?style=for-the-badge\&logo=hibernate)
![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge\&logo=html5)
![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge\&logo=css3)
![JavaScript](https://img.shields.io/badge/JavaScript-ES6-F7DF1E?style=for-the-badge\&logo=javascript\&logoColor=black)

</p>

---

# 📖 Descripción

**Productos Ecológicos** es una aplicación web desarrollada como proyecto final del curso **Java Full Stack – Talento Tech**.

El proyecto implementa un **Ecommerce** completo utilizando una arquitectura en capas con **Spring Boot** para el backend y **HTML, CSS y JavaScript** para el frontend.

La aplicación permite administrar productos, categorías y un carrito de compras con control automático de stock, ofreciendo una experiencia de compra similar a la de una tienda online.

---

# ✨ Características principales

✔ Administración de productos.
✔ Administración de categorías.
✔ Carrito de compras persistente.
✔ Control automático de stock.
✔ API REST desarrollada con Spring Boot.
✔ Base de datos MySQL.
✔ Persistencia mediante Spring Data JPA.
✔ Validaciones con Jakarta Validation.
✔ Manejo global de excepciones mediante `@RestControllerAdvice`.
✔ DTO para optimizar las respuestas del carrito.
✔ Cors
✔ Código organizado siguiendo arquitectura en capas.

---

# 🚀 Tecnologías utilizadas

## Backend

* Java 17
* Spring Boot
* Spring MVC
* Spring Data JPA
* Hibernate
* MySQL
* Lombok
* Jakarta Validation
* Maven


## Herramientas

* Eclipse IDE
* MySQL Workbench
* Thunder Client
* Live Server
* Git
* GitHub

---

# 📑 Contenido

* 📖 Descripción
* ✨ Características
* 🛠 Tecnologías
* 🏗 Arquitectura
* 📂 Estructura del proyecto
* ⚙ Instalación
* 🚀 Ejecución
* 🛒 Endpoints REST
* 🧪 Validaciones
* 📸 Capturas de pantalla
* 📈 Mejoras implementadas
* 🔮 Mejoras futuras
* 👨‍💻 Autor

# 🏗 Arquitectura del Proyecto

El proyecto sigue una **arquitectura en capas (Layered Architecture)**, una de las más utilizadas en aplicaciones desarrolladas con Spring Boot.

Cada capa tiene una responsabilidad específica, favoreciendo la separación de responsabilidades, el mantenimiento del código y la escalabilidad.

```text
Frontend (HTML + CSS + JavaScript)
                │
                ▼
      Controllers (API REST)
                │
                ▼
           Services
                │
                ▼
         Repositories
                │
                ▼
     Base de Datos MySQL
```

## Capas del Backend

### 📌 Controllers

Reciben las solicitudes HTTP provenientes del frontend y delegan la lógica de negocio a los servicios.

Ejemplos:

* `ProductoController`
* `CategoriaController`
* `CarritoController`

---

### 📌 Services

Contienen toda la lógica de negocio de la aplicación.

Entre sus responsabilidades se encuentran:

* Validar operaciones.
* Actualizar el stock de productos.
* Agregar productos al carrito.
* Eliminar productos del carrito.
* Calcular el total del carrito.
* Crear DTOs para las respuestas.

---

### 📌 Repositories

Implementados mediante **Spring Data JPA**.

Permiten acceder a la base de datos sin escribir consultas SQL básicas.

Ejemplos:

* `ProductoRepository`
* `CategoriaRepository`
* `CarritoRepository`
* `CarritoProductoRepository`

---

### 📌 Models (Entidades)

Representan las tablas de la base de datos mediante entidades JPA.

Principales entidades:

* Producto
* Categoria
* Carrito
* CarritoProducto

---

### 📌 DTO (Data Transfer Object)

Para optimizar la comunicación entre el backend y el frontend se implementó un DTO específico para el carrito.

En lugar de enviar toda la información del carrito cada vez que cambia una cantidad, el backend responde únicamente con un resumen que contiene:

* Cantidad total de productos.
* Importe total del carrito.

Esto reduce el tráfico entre cliente y servidor y mejora el rendimiento de la aplicación.

---

# 📂 Estructura del Proyecto

## Backend

```text
src
└── main
    └── java
        └── com.techlab.productos_ecologicos
            ├── config
            ├── controllers
            ├── dto
            ├── exceptions
            ├── models
            ├── repositories
            ├── services
            └── ProductosEcologicosApplication.java
```


---

# ⚙ Requisitos Previos

Antes de ejecutar el proyecto es necesario tener instalado:

* Java JDK 17 o superior.
* Eclipse IDE o IntelliJ IDEA.
* Maven.
* MySQL Server 8.
* MySQL Workbench.
* Git.
* Visual Studio Code (opcional para el frontend).

---

# 🔧 Configuración del Backend

Crear una base de datos en MySQL.

```sql
CREATE DATABASE productos_ecologicos;
```

Configurar el archivo `application.properties`.

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/productos_ecologicos
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

---

# ▶ Ejecutar la aplicación

Clonar el repositorio:

```bash
git clone https://github.com/USUARIO/REPOSITORIO.git
```

Ingresar al proyecto:

```bash
cd productos_ecologicos
```

Ejecutar Spring Boot:

```bash
mvn spring-boot:run
```

El backend quedará disponible en:

```text
http://localhost:8080
```

---

# 🌐 Ejecutar el Frontend

Abrir el proyecto con Visual Studio Code.

Ejecutar **Live Server** sobre el archivo:

```text
index.html
```

La aplicación consumirá automáticamente la API REST desarrollada con Spring Boot.

# 🛒 API REST

La aplicación expone una API REST desarrollada con **Spring Boot**, permitiendo que el frontend interactúe con la base de datos mediante solicitudes HTTP.

Base URL:

```text
http://localhost:8080
```

---

# 📦 Productos

## Obtener todos los productos

**GET**

```http
GET /productos
```

Respuesta:

```json
[
  {
  "categoria": {
    "descripcion": "Bolsas de tela y textiles para reemplazar el plástico descartable",
    "id": 1,
    "nombre": "Bolsas y textiles reutilizables"
  },
  "descripcion": "Bolsa reutilizable de tela modelo clásico",
  "id": 1,
  "imagenUrl": "https://i.postimg.cc/crY80KCz/bolsa-Reutilizable.jpg",
  "nombre": "Bolsa reutilizable de tela modelo clásico",
  "precio": 9.99,
  "stock": 40
}
]
```

---

## Obtener un producto por ID

**GET**

```http
GET /productos/{id}
```

Ejemplo:

```http
GET /productos/1
```

---

## Crear un producto

**POST**

```http
POST /productos
```

Body:

```json
{
  "nombre": "Botella reutilizable",
  "precio": 4500,
  "descripcion": "Botella térmica",
  "stock": 30,
  "imagenUrl": "https://...",
  "categoria": {
    "id": 2
  }
}
```

---

## Modificar un producto

**PUT**

```http
PUT /productos/{id}
```

---

## Eliminar un producto

**DELETE**

```http
DELETE /productos/{id}
```

---

# 📂 Categorías

## Listar categorías

```http
GET /categorias
```

---

## Obtener categoría por ID

```http
GET /categorias/{id}
```

---

## Crear categoría

```http
POST /categorias
```

---

## Actualizar categoría

```http
PUT /categorias/{id}
```

---

## Eliminar categoría

```http
DELETE /categorias/{id}
```

---

# 🛒 Carrito de Compras

El carrito implementa una relación **muchos a muchos** entre **Carrito** y **Producto**, utilizando la entidad intermedia **CarritoProducto**, donde además se almacena la cantidad de cada producto.

---

## Crear un carrito

```http
POST /carritos
```

Respuesta:

```json
{
  "id": 1,
  "productos": []
}
```

---

## Obtener carrito

```http
GET /carritos/{id}
```

---

## Agregar producto

```http
POST /carritos/{carritoId}/productos/{productoId}
```

Ejemplo:

```http
POST /carritos/1/productos/3
```

Cada llamada agrega **una unidad** del producto.

---

## Descontar una unidad

```http
PUT /carritos/{carritoId}/productos/{productoId}/descontar
```

Si la cantidad llega a cero, el producto se elimina automáticamente del carrito.

---

## Eliminar un producto

```http
DELETE /carritos/{carritoId}/productos/{productoId}
```

Elimina completamente el producto del carrito y devuelve las unidades al stock.

---

## Vaciar carrito

```http
DELETE /carritos/{id}/vaciar
```

Elimina todos los productos del carrito manteniendo el carrito activo.

---

## Eliminar carrito

```http
DELETE /carritos/{id}
```

Elimina el carrito de forma permanente.

---

## Obtener resumen del carrito

```http
GET /carritos/{id}/resumen
```

Este endpoint devuelve un **DTO** con la información necesaria para actualizar el frontend sin tener que descargar nuevamente todo el carrito.

Respuesta:

```json
{
  "cantidadProductos": 8,
  "total": 25450.75
}
```

---

# 📡 Códigos de respuesta HTTP

| Código                        | Descripción                                             |
| ----------------------------- | ------------------------------------------------------- |
| **200 OK**                    | Operación realizada correctamente.                      |
| **201 Created**               | Recurso creado exitosamente.                            |
| **400 Bad Request**           | Error de validación en los datos enviados.              |
| **404 Not Found**             | Recurso no encontrado.                                  |
| **409 Conflict**              | Conflicto de negocio (por ejemplo, stock insuficiente). |
| **500 Internal Server Error** | Error interno del servidor.                             |

---

# 🧪 Pruebas de la API

Durante el desarrollo, la API fue probada utilizando herramientas como:

* Thunder Client
* Navegador Web
* Fetch API
* Consola del navegador (Developer Tools)

Todas las respuestas del backend son consumidas por el frontend mediante **JavaScript (Fetch API)**, manteniendo una separación clara entre la lógica de presentación y la lógica de negocio.





# ✅ Funcionalidades Implementadas

### Backend

* ✔ API REST con Spring Boot.
* ✔ Arquitectura en capas.
* ✔ Spring Data JPA.
* ✔ Hibernate.
* ✔ MySQL.
* ✔ Validaciones con Jakarta Validation.
* ✔ Manejo global de excepciones.
* ✔ DTO para el resumen del carrito.
* ✔ Relaciones JPA (`@OneToMany`, `@ManyToOne`, `@ManyToMany` mediante entidad intermedia).
* ✔ Gestión automática del stock.
* ✔ CRUD completo de Productos.
* ✔ CRUD completo de Categorías.
* ✔ CRUD completo del Carrito.

---


# 📈 Mejoras Implementadas Durante el Desarrollo

A medida que evolucionó el proyecto se incorporaron diversas mejoras para optimizar el rendimiento y la organización del código.

* Implementación de una arquitectura en capas.
* Refactorización del frontend para reutilizar funciones comunes.
* Centralización de todas las llamadas HTTP en `api.js`.
* Incorporación de DTO para reducir el tamaño de las respuestas del backend.
* Optimización de la actualización del carrito evitando consultas innecesarias.

---

# 🔮 Mejoras Futuras

El proyecto continuará evolucionando incorporando nuevas funcionalidades.

Entre las mejoras planificadas se encuentran:

* 🔐 Autenticación mediante Spring Security + JWT.
* 👤 Registro e inicio de sesión de usuarios.
* ❤️ Lista de favoritos.
* 📦 Gestión de pedidos.
* 💳 Integración con pasarelas de pago.
* 📧 Confirmación de compra por correo electrónico.
* 🔍 Búsqueda avanzada de productos.
* 🏷️ Filtros por categoría y precio.
* 📄 Paginación de productos.
* 🔄 Sincronización automática del carrito entre múltiples pestañas.
* 👨‍💼 Panel de administración para gestión de productos y categorías.
* ☁️ Despliegue del backend en la nube.

---

# 📚 Conceptos Aplicados

Durante el desarrollo del proyecto se aplicaron conocimientos de:

* Programación Orientada a Objetos (POO).
* Arquitectura en capas.
* Principios SOLID.
* API REST.
* HTTP y JSON.
* Spring Boot.
* Spring MVC.
* Spring Data JPA.
* Hibernate.
* DTO (Data Transfer Object).
* Lombok.
* Manejo de excepciones.
* Validaciones.
* JavaScript moderno (ES6).
* Manipulación del DOM.
* Fetch API.
* Persistencia con MySQL.

---

# 🌐 Comunicación entre el Frontend y el Backend

La aplicación implementa una arquitectura cliente-servidor donde el frontend consume una API REST desarrollada con **Spring Boot** mediante **Fetch API**.

El backend es responsable de toda la lógica de negocio, mientras que el frontend se encarga únicamente de la interfaz de usuario y de mostrar la información al usuario.

```text
               Frontend
      HTML + CSS + JavaScript
               │
               │ Fetch API
               ▼
       Spring Boot REST API
               │
               ▼
      Services (Lógica de negocio)
               │
               ▼
      Spring Data JPA / Hibernate
               │
               ▼
             MySQL
```

---

# 📦 Gestión de Productos

## Obtener todos los productos

**Endpoint**

```http
GET /productos
```

### Uso en el Frontend

Este endpoint se ejecuta automáticamente al ingresar a la tienda para obtener el catálogo completo de productos.

Archivo donde se utiliza:

```text
script/tienda.js
```

Funcionalidad:

* Cargar productos desde la base de datos.
* Generar dinámicamente las tarjetas de la tienda.
* Mostrar nombre, precio, imagen y categoría.

---

# 🛒 Gestión del Carrito

El carrito de compras representa una de las funcionalidades principales del sistema.

Cada acción realizada por el usuario se traduce en una llamada a la API REST.

---

## Crear un carrito

**Endpoint**

```http
POST /carritos
```

### Uso en el Frontend

Cuando el usuario ingresa por primera vez a la aplicación, el sistema verifica si existe un carrito asociado en el navegador.

Si no existe, se crea automáticamente uno nuevo y su identificador se almacena en el `localStorage`.

Archivo:

```text
script/api.js
```

---

## Mostrar el carrito

**Endpoint**

```http
GET /carritos/{id}
```

### Uso en el Frontend

Este endpoint obtiene el contenido completo del carrito.

Archivo:

```text
script/carrito.js
```

Permite mostrar:

* Productos agregados.
* Cantidad de unidades.
* Precio unitario.
* Subtotal por producto.
* Total general.

---

## Agregar un producto

**Endpoint**

```http
POST /carritos/{carritoId}/productos/{productoId}
```

### Botón asociado

```text
🛒 Agregar
```

Cuando el usuario presiona el botón **Agregar**, el frontend envía una solicitud al backend para incorporar una unidad del producto seleccionado al carrito.

El backend:

* Busca el carrito.
* Busca el producto.
* Verifica disponibilidad de stock.
* Actualiza la cantidad.
* Guarda los cambios en la base de datos.

Luego el frontend actualiza automáticamente:

* El badge del carrito.
* El total.
* El resumen.
* La notificación (Toast).

---

## Incrementar la cantidad

**Endpoint**

```http
POST /carritos/{carritoId}/productos/{productoId}
```

### Botón asociado

```text
➕
```

El mismo endpoint utilizado para agregar productos también permite aumentar la cantidad de unidades de un producto ya existente en el carrito.

Cada clic incrementa la cantidad en una unidad.

---

## Disminuir la cantidad

**Endpoint**

```http
PUT /carritos/{carritoId}/productos/{productoId}/descontar
```

### Botón asociado

```text
➖
```

Cada clic reduce una unidad del producto seleccionado.

Cuando la cantidad llega a cero, el producto es eliminado automáticamente del carrito.

Además, el backend actualiza el stock disponible.

---

## Eliminar un producto

**Endpoint**

```http
DELETE /carritos/{carritoId}/productos/{productoId}
```

### Botón asociado

```text
❌
```

Elimina completamente el producto del carrito, independientemente de la cantidad de unidades.

Antes de ejecutar esta acción se muestra un cuadro de confirmación personalizado para evitar eliminaciones accidentales.

---

## Vaciar el carrito

**Endpoint**

```http
DELETE /carritos/{id}/vaciar
```

### Botón asociado

```text
Vaciar carrito
```

Elimina todos los productos del carrito manteniendo el carrito activo para futuras compras.

Una vez completada la operación, la interfaz:

* Vacía la tabla de productos.
* Actualiza el total.
* Reinicia el badge del carrito.
* Muestra una notificación al usuario.

---

## Obtener el resumen del carrito

**Endpoint**

```http
GET /carritos/{id}/resumen
```

### Uso en el Frontend

Este endpoint devuelve un objeto DTO con la información mínima necesaria para actualizar la interfaz.

Respuesta:

```json
{
    "cantidadProductos": 5,
    "total": 18450.75
}
```

Este resumen permite actualizar:

* El badge del carrito.
* El importe total.
* La cantidad total de productos.

Sin necesidad de descargar nuevamente toda la información del carrito.

---

# 🔄 Flujo de una compra

```text
Usuario
    │
    ▼
Selecciona un producto
    │
    ▼
Presiona "Agregar"
    │
    ▼
JavaScript (Fetch API)
    │
    ▼
POST /carritos/{carritoId}/productos/{productoId}
    │
    ▼
Spring Boot
    │
    ▼
Service
    │
    ▼
Repository
    │
    ▼
MySQL
    │
    ▼
Actualización del carrito
    │
    ▼
Respuesta al Frontend
    │
    ▼
Actualización del total, badge y notificación
```

---

# 📊 Relación entre la interfaz y la API

| Acción del usuario   | Componente               | Endpoint                                                     |
| -------------------- | ------------------------ | ------------------------------------------------------------ |
| Ingresar a la tienda | Listado de productos     | `GET /productos`                                             |
| Crear carrito        | Automático               | `POST /carritos`                                             |
| Ver carrito          | Página del carrito       | `GET /carritos/{id}`                                         |
| Agregar producto     | Botón **Agregar**        | `POST /carritos/{carritoId}/productos/{productoId}`          |
| Incrementar cantidad | Botón **➕**              | `POST /carritos/{carritoId}/productos/{productoId}`          |
| Disminuir cantidad   | Botón **➖**              | `PUT /carritos/{carritoId}/productos/{productoId}/descontar` |
| Eliminar producto    | Botón **❌**              | `DELETE /carritos/{carritoId}/productos/{productoId}`        |
| Vaciar carrito       | Botón **Vaciar carrito** | `DELETE /carritos/{id}/vaciar`                               |
| Actualizar resumen   | Automático               | `GET /carritos/{id}/resumen`                                 |

---

# ✨ Beneficios de la implementación

* Arquitectura desacoplada entre frontend y backend.
* API REST reutilizable por diferentes clientes (web, móvil o escritorio).
* Comunicación mediante JSON utilizando Fetch API.
* Optimización de las respuestas mediante DTO para reducir el tráfico de datos.
* Separación clara entre presentación, lógica de negocio y acceso a datos.
* Código modular y fácil de mantener, permitiendo incorporar nuevas funcionalidades como autenticación, pedidos o integración con pasarelas de pago.



# 👨‍💻 Autor

**Alan Contreras Flores**

Proyecto desarrollado como trabajo final para el curso **BackEnd-Java – Talento Tech**.

GitHub:

```text
https://github.com/AlanContreras784
```

LinkedIn:

```text
https://www.linkedin.com/in/alanbenitocontrerasflores250784/
```

---

# 🙏 Agradecimientos

Agradezco a **Talento Tech**, al profesor Miguel Angel Nefle y compañeros del curso por los conocimientos compartidos durante la formación, que hicieron posible el desarrollo de este proyecto.

---

# 📄 Licencia

Este proyecto fue desarrollado con fines educativos como parte de la formación en **Java Full Stack**.
Puede utilizarse como material de estudio y referencia, respetando la autoría correspondiente.

---

<p align="center">

⭐ Si este proyecto te resultó útil o interesante, puedes darle una estrella en GitHub.

**¡Gracias por visitar el repositorio!**

</p>