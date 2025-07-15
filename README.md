# TFG Software 2024
# Nombre de la aplicación: Cosmic News
# Nombre del alumno: Pedro Cristino Moreno
# Nombre del Tutor: Óscar Soto
# Fase 0
## Funcionalidades:
### Funcionalidades básicas:
- Cabecera y desplegable: En cualquier parte de la web, el usuario tendrá acceso a la cabecera donde podrá iniciar o cerrar sesión, registrarse, perfil, volver al inicio... Así como podrá abrir un desplegable para acceder a las distintas funcionalidades de la web, como ver las noticias, fotos, el calendario...
- Registrarse / Iniciar sesión
- Sección noticias: Los usuarios podrán ver una lista de noticias que podrán ser ordenadas por fecha de publicación, likes...
- Sección fotos: Los usuarios podrán ver una lista de fotos que podrán ser ordenadas por fecha, likes...
- Perfil: Los usuarios podrán dar like (o quitarlo) a noticias y fotos para que les aparezcan en su perfil.
- Sección videos: Los usuarios podrán ver una lista de vídeos relacionados con la astronomía que serán cargados desde youtube.
- Sección calendario: Los usuarios podrán ver un calendario con eventos futuros y pasados relacionados con la astronomía y podrán marcar días para recibir notificación sobre dichos eventos.
- Sección sistema solar: Los usuarios podrán ver una representación dinámica del sistema solar y podrán aprender sobre los cuerpos celestes que lo componen.
- Sección quizzes: Los usuarios podrán realizar tests sobre temas de astronomía tratados en la web. Podrán ver en qué han fallado y recibirán una insignia, que podrán ver en su perfil, como recompensa al completar al 100% cada test por primera vez.

Los admins serán los encargados de publicar gestionar la creación, modificación y eliminación de noticias, fotos, vídeos, eventos en el calendario y quizzes.

## Aspectos principales de la aplicación:
### Entidades:
- Usuarios
- Noticias
- Fotos
- Videos
- Eventos
- Quizzes
- Planetas
  
![Entidad-Relacion - Fase 0](https://github.com/user-attachments/assets/36cb0d51-30bf-44ff-b82a-ffc0b6635dd7)


### Permisos de usuarios:
- Usuario anónimo: Puede ver los vídeos y el sistema solar. En cuanto a las noticias y las fotos podrá verlas pero no podrá darle like. En cuanto al calendario podrá ver los eventos pero no podrá solicitar notificaciones. En cuanto a los quizzes podrá ver los nombres pero no hacerlos ni ver sus preguntas.
- Usuario registrado: Además de lo anterior puede dar like a noticias y fotos, solicitar notificaciones de eventos y realizar los quizzes.
- Admin: Además de todo lo anterior, podrá gestionar la creación, modificación y eliminación de noticias, fotos, vídeos, eventos en el calendario y quizzes.

### Imágenes:
Tendrán imágenes asociadas los usuarios (foto personal), las noticias, las fotos y los quizzes.

### Gráfico con consulta avanzada:
Se mostrará un grafo que muestre cuantas veces se ha completado al 100% cada quizz (un usuario solo puede completar cada quizz una vez)

### Tecnología complementaria:
- Youtube: Se podrán vídeos de youtube en la propia web o, si quieres, también podrás acceder al enlace del vídeo en youtube
- p5.js: Una librería que se usará para facilitar la creación de un sistema solar dinámico interactivo en la web.

### Wireframe
![wireframe](https://github.com/user-attachments/assets/6287d09a-c8fd-4ec1-af44-1334a0390cf4)

# Fase 1

## Diagramas de Navegación

En el primer diagrama veremos como podemos navegar desde la página inicial (de noticias) a todo lo relacionado con usuarios, quizzes, calendario y sistema solar.

![DiagramaNavegacion1](https://github.com/user-attachments/assets/e4c05d1e-1786-42eb-bf77-d0f760fbbb42)

En el segundo veremos como podemos navegar desde la página inicial a todo lo relacionado con las noticias, fotos y videos.

![DiagramaNavegacion2](https://github.com/user-attachments/assets/2acd1928-8207-4634-b9d6-75fbb5410ba6)

## Diagrama con las entidades de la base de datos

![Entidad-Relacion - Fase 1](https://github.com/user-attachments/assets/8db96792-95d3-4920-acf6-3912b1740132)

## Diagrama de clases del backend

![Diagrama Clases Backend](https://github.com/user-attachments/assets/b4958680-05da-43f4-bdb4-c79d9e60ff3e)

## Diagrama de clases y templates de la SPA

![Diagrama Clases SPA](https://github.com/user-attachments/assets/c899e056-cb82-4cb3-a52f-93ac732f6255)

## Construcción de la imagen docker

1. Instalar docker.
2. Ejecutarlo (mantener la aplicacion de escritorio abierta en windows, en linux se inicia automáticamente)
3. Clonamos el repositorio: git clone https://github.com/codeurjc-students/2024-cosmic-news
4. Vamos al directorio del docker: cd docker
5. Desplegamos nuestra aplicación: docker-compose up
6. Accedemos a ella en https://localhost:8443/

## Publicación de la imagen

1. Repetir los pasos del apartado anterior hasta el 3 (incluido).
2. Ejecutar el create_image.sh que hay en la carpeta docker, en nuestro caso ejecutamos ./docker/create-image.sh (es posible que previamente se necesite darle al script los permisos necesarios).
   
Posteriormente a su publicación se podrá acceder a la imagen desde: https://hub.docker.com/r/pedrocristino2020/cosmic_news

# Fase 2

## Tareas automáticas

### Integración Continua (CI) - Tests

Se ejecuta automáticamente en cada pull request (salvo si se actualiza el readme). Las tareas que realiza son:
1. Descargar el código (checkout), configurar el entorno de ejecución con JDK 17 y levantar un servicio de MySQL.
2. Ejecuta todos los tests de la aplicación, tanto las pruebas de interfaz de usuario como las de API Rest.

### Entrega Continua (CD) - Docker

Se ejecuta al realizar un commit en la rama main después de un merge. Las tareas que realiza son:
1. Descargar el código (checkout) y configurar JDK 17.
2. Generar una imagen Docker de la aplicación.
3. Inicia sesión y publica la imagen Docker en Docker Hub.

## Almacenamiento de Artefactos Generados
Los artefactos generados en el proceso de CI/CD se almacenan en los siguientes lugares:
1. Los resultados de los tests (CI), los logs y reportes, se encuentran en la sección "Actions" en Github.
2. Las imágenes docker (CD) se almacenan en Docker Hub.

# Fase 3

## Pruebas unitarias
Se han añadido pruebas unitarias utilizando la librería Mockito para consolidar que un administrador de la aplicación puede realizar correctamente operaciones CRUD sobre las entidades principales: noticias, fotografías, vídeos, eventos y cuestionarios. De esta forma, se refuerzan las pruebas de sistema ya existentes, añadiendo pruebas que verifican tanto crear como eliminar las entidades principales.

## Despliegue en Google Cloud
1. Iniciar la instancia de la máquina virtual correspondiente.
2. Acceder a la máquina virtual a través de SSH.
3. Repetir los pasos del apartado "Construcción de la imagen docker" (solo serán necesarios todos los pasos la primera vez).

## Despliegue en Amazon Web Services (AWS)
1. Iniciar la instancia EC2 y la base de datos Amazon RDS.
2. Conectarse a la instancia EC2 a través de SSH usando la clave que otorga AWS al crear dicha instancia (archivo .pem).
3. Instalar docker en la instancia EC2.

Con esto hecho, cada vez que reciba un commit la rama main se actualizará la imagen docker de la aplicación y será accesible en el DNS público de la instancia EC2.

## Link al video de youtube
https://www.youtube.com/watch?v=07N7VUCn42o&
