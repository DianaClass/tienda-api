# tienda-api

API REST de productos y categorías con Spring Boot, Spring Data JPA, PostgreSQL y Swagger UI.

## Requisitos

- Java 21
- Maven 3.9+ (o el Maven Wrapper incluido)
- PostgreSQL local o un proyecto de Supabase

## Ejecutar localmente

Configura las variables de entorno antes de iniciar la aplicación. Para PowerShell:

```powershell
$env:DB_URL = "jdbc:postgresql://localhost:5432/tienda"
$env:DB_USERNAME = "postgres"
$env:DB_PASSWORD = "tu-clave-local"
.\mvnw.cmd spring-boot:run
```

En Supabase, usa la URL JDBC del **Session pooler** (puerto 5432), el usuario que incluye el identificador del proyecto y la contraseña de la base de datos. No guardes esos valores en el repositorio.

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- Salud: `http://localhost:8080/actuator/health`

## Endpoints

| Método | Ruta | Acción |
|---|---|---|
| POST | `/api/categorias` | Crear categoría |
| GET | `/api/categorias` | Listar categorías |
| GET | `/api/categorias/{id}` | Obtener categoría |
| PUT | `/api/categorias/{id}` | Actualizar categoría |
| DELETE | `/api/categorias/{id}` | Eliminar categoría sin productos asociados |
| POST | `/api/productos` | Crear producto |
| GET | `/api/productos` | Listar productos |
| GET | `/api/productos/{id}` | Obtener producto |
| PUT | `/api/productos/{id}` | Actualizar producto |
| DELETE | `/api/productos/{id}` | Eliminar producto |
| GET | `/api/productos/buscar?nombre=cafe` | Buscar por nombre, sin distinguir mayúsculas |

Ejemplo para crear un producto (primero crea una categoría y usa el `id` devuelto):

```json
{
  "nombre": "Cafe molido",
  "descripcion": "Paquete de 500 g",
  "precio": 25000.00,
  "stock": 12,
  "categoriaId": 1
}
```

## Desplegar en Render

1. Sube este proyecto a un repositorio de GitHub.
2. En Render, crea un **Web Service** desde ese repositorio y selecciona **Docker**.
3. Configura estas variables en el servicio:
   - `DB_URL`: URL JDBC de Supabase Session pooler, puerto `5432`, con `sslmode=require`.
   - `DB_USERNAME`: nombre de usuario del pooler de Supabase.
   - `DB_PASSWORD`: contraseña de la base de datos.
4. Configura el health check path como `/actuator/health`.
5. Render define `PORT`; en local la aplicación usa el puerto `8080`.

El Dockerfile compila con Java 21 y ejecuta el JAR con límites de memoria adecuados para un contenedor pequeño. Hibernate crea y actualiza las tablas al iniciar.

## Variables de entorno y seguridad

Las variables de entorno mantienen las credenciales fuera del código y del historial de Git, permiten usar valores diferentes por entorno y facilitan su rotación desde el panel del proveedor. Si se publican credenciales en un repositorio público, terceros podrían usarlas para acceder a la base de datos; revócalas y cámbialas inmediatamente. No basta con borrar el archivo del último commit: el secreto puede permanecer en el historial.

## Pruebas

Las pruebas usan una base H2 aislada y no requieren una instancia local de PostgreSQL:

```powershell
.\mvnw.cmd test
```
