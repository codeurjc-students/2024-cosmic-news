package es.codeurjc.cosmic_news.service;

import java.io.IOException;
import java.sql.Blob;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import es.codeurjc.cosmic_news.model.Badge;
import es.codeurjc.cosmic_news.model.Event;
import es.codeurjc.cosmic_news.model.News;
import es.codeurjc.cosmic_news.model.Picture;
import es.codeurjc.cosmic_news.model.Planet;
import es.codeurjc.cosmic_news.model.Question;
import es.codeurjc.cosmic_news.model.Quizz;
import es.codeurjc.cosmic_news.model.User;
import es.codeurjc.cosmic_news.model.Video;
import es.codeurjc.cosmic_news.repository.EventRepository;
import es.codeurjc.cosmic_news.repository.NewsRepository;
import es.codeurjc.cosmic_news.repository.PictureRepository;
import es.codeurjc.cosmic_news.repository.PlanetRepository;
import es.codeurjc.cosmic_news.repository.QuizzRepository;
import es.codeurjc.cosmic_news.repository.UserRepository;
import es.codeurjc.cosmic_news.repository.VideoRepository;
import jakarta.annotation.PostConstruct;

@Component
public class DataBaseInitializer {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizzRepository quizzRepository;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired 
    private EventRepository eventRepository;

    @Autowired 
    private VideoRepository videoRepository;

    @Autowired 
    private PlanetRepository planetRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    private void initDatabase(){
        initQuizzes();
        initUsers();
        initPictures();
        initNews();
        initEvents();
        initVideos();
        initPlanets();
    }

    private void initUsers(){
        User user1 = new User(
            "Juan",
            "Saturno",
            "Usuario inicializado",
            "JuanitoSaturno42",
            "juanSaturno@gmail.com",
            passwordEncoder.encode("juan"),
            "USER");
        
        User user2 = new User(
            "Pepe",
            "Jupiter",
            "Usuario inicializado",
            "PepitoInJupiter",
            "a",
            passwordEncoder.encode("a"),
            "USER");
        
        User user3 = new User(
            "Pedro",
            "Admin",
            "Admin inicializado",
            "GodPedro",
            "xd",
            passwordEncoder.encode("xd"),
            "USER","ADMIN");

        Blob photo = null;
        photo = photoToBlob("static/images/logo2.png");
        user3.addBadge(new Badge(photo,"Bienvenida", 0));
 
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        
    }

    private void initNews(){
        News news1 = new News(
            "Muere el legendario artista espacial Adolf Schaller a los 68 años",
            "La comunidad astronómica ha perdido a uno de los grandes artistas visuales de la historia, que se dedicaba a las ilustraciones astronómicas.",
            "Don Davis",
            "Ciencia",
            "Uno de los artistas espaciales pioneros y más talentosos del mundo, Adolf Schaller, murió a principios de agosto a los 68 años. Deja una obra publicada que lo establece como uno de los más grandes artistas espaciales. Su atención a los descubrimientos en curso, desde el sistema solar hasta galaxias distantes, dio como resultado representaciones que reflejan tanto lo que se sabe sobre estos lugares como su visión de su atractivo visual.\n\n"
        + "Nacido en 1956, la vida de Schaller abarcó la era espacial desde sus inicios con el Sputnik 1 hasta muchos descubrimientos recientes en el espacio cercano y distante.\n\n"
        + "A mediados de la década de 1970, la revista Astronomy fue una presentadora pionera del arte espacial, y los trabajos anteriores de Schaller vistos allí formaron una gran parte de la “edad de oro” inicial de esa revista. Un editor de Astronomy, Terence Dickinson, publicó posteriormente varias ediciones de un magnífico libro, The Universe and Beyond (Camden House, 1992), que contenía numerosas ilustraciones de Schaller, muchas de ellas ejecutadas específicamente para el libro.\n\n"
        + "Más tarde, Schaller pasó a formar parte del equipo de artistas del programa Cosmos de PBS de Carl Sagan y creó muchas obras de arte de animación, así como globos pintados y fondos de modelos de paisajes para la serie. Más tarde prestó su talento a los programas de PBS Planet Earth e Infinite Voyage. Schaller también produjo muchas capas de arte para un breve acercamiento al centro galáctico para la película Brainstorm de Douglas Trumbull. La aportación de Schaller al paisaje de Europa para la película 2010 fue demasiado exigente para el director, que lo despidió cuando los conocimientos informados del artista chocaron con las ideas preconcebidas del director.\n\n"
        + "El arte espacial de Schaller apareció más tarde en comunicados de prensa de muchos observatorios y en exhibiciones rediseñadas para el Observatorio Griffith en Los Ángeles. Un libro de Dickinson y Schaller, Extraterrestrials: A Field Guide for Earthlings (Camden House, 1994), muestra además la amplitud de sus habilidades conceptuales y artísticas.\n\n"
        + "Echaremos de menos la visión de Adolf Schaller, pero su trabajo seguirá inspirando.",
            5,
            LocalDate.of(2024,8,9)                  
        );

        Blob photo = null;
        photo = photoToBlob("static/images/adolfSchaller.jpeg");
        news1.setPhoto(photo);
        news1.setImage(photo != null);

        News news2 = new News(
            "El primer mapa de Io de volcanes insinúa un océano de magma subterráneo",
            "El nuevo mapa también destaca los extraordinarios efectos del calentamiento de las mareas que sufre la luna, gracias a su configuración orbital.",
            "Theo Nicitopoulos",
            "Sistema Solar",
            "Io, uno de los satélites galileanos de Júpiter, es el cuerpo volcánicamente más activo del sistema solar. Imágenes de naves espaciales han mostrado lava fundida en erupción a lo largo de paredes que represan lagos de lava gigantes e imponentes columnas de gas y polvo que se elevan desde amplias calderas.\n\n"
        + "Ahora, un equipo dirigido por la científica planetaria Ashley Davies del Laboratorio de Propulsión a Chorro de la NASA ha preparado el primer mapa global de flujo de calor de la actividad volcánica de Ío. El nuevo mapa muestra los lugares donde ha surgido magma desde el interior de la luna. Los resultados, publicados en The Planetary Science Journal, apuntan a un posible océano de magma en el interior del satélite y resaltan cómo las resonancias comunes en las órbitas de los cuerpos celestes podrían conducir a otros océanos en el sistema solar y más allá.\n"
        + "El equipo encontró 87 puntos calientes previamente desconocidos utilizando datos de la nave espacial Juno de la NASA y 14 sitios adicionales de este tipo a partir de datos recopilados con telescopios terrestres. Combinaron los nuevos sitios con los previamente conocidos para mapear un total de 343 fuentes de actividad volcánica en la luna.\n\n"
        + "Los resultados muestran, por primera vez, que los volcanes de Ío están distribuidos uniformemente a escala global, pero que el calor que emiten no. Los volcanes de las regiones polares emiten menos calor que los de latitudes más bajas. Además, los volcanes del polo norte de Ío liberan el doble de energía que los de la región del polo sur. \"Esa fue la mayor sorpresa\", dice Davies, \"un verdadero enigma que aún no hemos resuelto\".\n\n"
        + "La distribución desigual del calor, más en latitudes medias que en los polos, es consistente con los modelos que sugieren que el calentamiento en Io ocurre más cerca de su superficie y derrite la roca del subsuelo. Sin embargo, Davies dice que no está claro cuán extenso sería el derretimiento: \"No sabemos si se trata de un océano de magma global o parcial\".\n\n"
        + "Una de las razones por las que Ío es tan volcánicamente activo es que la luna está atrapada en un patrón repetitivo de configuraciones orbitales, llamadas resonancias orbitales, con dos de los otros satélites de Júpiter, Europa y Ganímedes. En particular, Europa tarda el doble en orbitar a Júpiter que Io, y Ganímedes tarda cuatro veces más que Io. Debido a estas resonancias, las lunas experimentan tirones gravitacionales que se repiten regularmente entre sí durante las alineaciones, lo que ha hecho que sus órbitas sean menos circulares. Por lo tanto, debido a que Ío se acerca y se aleja de Júpiter durante cada revolución, las fuerzas de marea estiran y comprimen la luna, produciendo calor.\n\n"
        + "\"Las fuerzas de marea crean fricción\", dice Giacomo Lari, matemático de la Universidad de Pisa en Italia y primer autor de otro artículo reciente sobre las lunas de Júpiter, publicado en Celestial Mechanics and Dynamical Astronomy. Y como Júpiter es tan masivo e Io orbita tan cerca del planeta, el calentamiento resultante de las mareas es extremo. El efecto ha disminuido pero sigue presente en Europa, cuya órbita se ha vuelto menos circular y también sufre calentamiento por mareas, lo que potencialmente permitiría que existiera un océano de agua debajo de su corteza helada.\n\n"
        + "Júpiter no es el único lugar donde tales efectos importan: también se cree que Encelado, la luna de Saturno, tiene un océano de agua, que puede mantenerse por el calentamiento de las mareas causado por las resonancias con la luna Dione.\n\n"
        + "\"Comprender cómo funcionan las resonancias y cómo la actividad resultante ha afectado a estas increíbles lunas es de vital importancia para comprender la evolución del sistema solar\", afirma Davies. Esto también incluye la historia del vulcanismo en la Tierra, la Luna y Marte, que probablemente experimentaron una actividad similar a la escala de Io desde el principio, añade Davies.\n\n"
        + "Tres no es una multitud\n"
        + "El artículo de Lari demostró que las órbitas de Io, Europa y Ganímedes están dominadas por la interacción combinada de los tres cuerpos, en lugar de resonancias separadas entre pares de lunas, un hallazgo que no era necesariamente obvio, dice Lari.\n\n"
        + "Añade que las tres lunas se encuentran actualmente en una \"zona segura\" donde las resonancias son estables y deberían seguir siéndolo durante al menos mil millones de años. Teniendo en cuenta que esta resonancia de tres cuerpos es la única conocida entre lunas del sistema solar, la oportunidad de observar los efectos de las mareas es extraordinaria.\n\n"
        + "Y los efectos también van mucho más allá de nuestro sistema solar. Los astrónomos han descubierto sistemas exoplanetarios donde varios planetas importantes orbitan en cadenas resonantes. En el futuro, tal vez los investigadores puedan examinar tales sistemas (o un sistema de exolunas alrededor de un solo exoplaneta) en busca de puntos calientes similares que podrían indicar calentamiento de las mareas y la formación de magma u océanos de agua.\n\n"
        + "Y mientras tanto, los nuevos resultados en Júpiter ciertamente harán que los investigadores anticipen más.",
    15,
            LocalDate.of(2024,8,6)   
        );

        photo = photoToBlob("static/images/vulcans.jpeg");
        news2.setPhoto(photo);
        news2.setImage(photo != null);

        News news3 = new News(
            "¿Qué es un eco de luz?",
            "Este fenómeno hace que los objetos se expandan más rápido que la velocidad de la luz, pero es un truco de la geometría cósmica.",                                  "Howard E.Bond",
            "Ciencia",
            "Felicitaciones por notar un efecto llamado expansión superluminal, un fenómeno que vemos en un eco de luz. Nos parece que el eco se expande muchas veces la velocidad de la luz, aparentemente violando el límite de velocidad fundamental en el universo.\n\n"
        + "Un eco de luz ocurre cuando una estrella experimenta una erupción o un objeto explota en una región rodeada de polvo interestelar. La luz de la erupción viaja directamente a la Tierra, por lo que eso es lo primero que vemos. Pero la luz también viaja hacia un lado, se refleja en el polvo y luego se dirige hacia nosotros. Debido a la distancia adicional recorrida, la luz reflejada llega a la Tierra más tarde, como la voz de un cantor que resuena en las montañas que los rodean.\n\n"
        + "Consideremos la imagen del Hubble del eco luminoso de la estrella en erupción V838 Monocerotis, tomada en octubre de 2002, unos 230 días después de que la luz de la explosión alcanzara su punto máximo en marzo de ese año. Sin embargo, el eco de luz en ese momento parece tener un radio de unos 3 años luz. ¿Cómo pudo la luz de la explosión viajar 3 años luz en menos de un año?\n\n"
        + "La respuesta está en la geometría de un eco de luz, ilustrada en el diagrama anterior. Aquí vemos V838 Mon en el centro, rodeado por una capa de polvo interestelar que se extiende en un radio de unos 6 o 7 años luz. En cualquier momento, el eco de la luz se encuentra sobre una superficie parabólica. Esta superficie marca el lugar en el espacio donde el tiempo adicional de viaje para que la luz rebote en el polvo y llegue a la Tierra es de 230 días. El polvo iluminado que parece estar a 3 años luz al lado de V838 Mon está en realidad a casi 6 años luz delante de la estrella. Entonces, es casi como si estuviéramos mirando un largo túnel en el polvo, con la estrella casi al final del túnel. ¡La velocidad de la luz no ha sido violada!\n\n"
        + "A medida que pasa el tiempo, el paraboloide se abre y se propaga hacia el fondo. Por lo tanto, cada observación del Hubble tomó muestras de una región diferente en el polvo circundante a medida que la iluminación la atravesaba. ¡Es un TAC astrofísico!",
            8,
            LocalDate.of(2024,8,4)   
        );

        photo = photoToBlob("static/images/light.jpeg");
        news3.setPhoto(photo);
        news3.setImage(photo != null);

        News news4 = new News(
            "¿Existe una temperatura más alta posible en el universo?",
            "El límite teórico puede ser lo que se llama temperatura de Planck, derivada utilizando constantes universales fundamentales.",
            "Sten Odenwald",
            "Cosmología",
            "Todos aceptamos que la temperatura más fría que puede alcanzar cualquier objeto es el cero absoluto. ¿Existe una temperatura máxima teórica que algo pueda alcanzar?\n\n"
        + "Esteban Kitterman\n"
        + "Thomasville, Carolina del Norte\n\n"
        + "La temperatura es una medida de la energía promedio de un conjunto de partículas. (Esto contrasta con la energía cinética, que es una medida de la energía transportada por una sola partícula). Entonces, para responder una pregunta sobre la temperatura, tenemos que encontrar algún conjunto de partículas para medir.\n\n"
        + "Una de esas colecciones se encuentra en el núcleo de nuestro Sol, donde se alcanzan temperaturas de unos 15 millones de kelvins (27 millones de grados Fahrenheit o 15 millones de grados Celsius). Las estrellas muy masivas pueden tener temperaturas centrales superiores a 500 millones de kelvins (900 millones de F o 500 millones de C). Justo antes de que una estrella masiva explote como supernova, algunos modelos teóricos predicen que su núcleo puede alcanzar temperaturas superiores a los 100 mil millones de Kelvin en el proceso de implosión, a medida que se acerca a la densidad extremadamente alta de una estrella de neutrones.\n\n"
        + "Antes de que el universo tuviera un segundo de edad, la materia era un plasma denso de nucleones y partículas elementales con densidades superiores a la del plomo. Podemos predecir la temperatura de este plasma, T, en kelvins usando la fórmula T = 1010/√t, donde t es el tiempo en segundos desde el Big Bang. En 10 microsegundos, la temperatura promedio del contenido del universo es de 3 billones de kelvins, equivalente a las energías que el Gran Colisionador de Hadrones explora en los plasmas de quarks y gluones (alrededor de 300 millones de electronvoltios). Pero dependiendo de qué tan atrás (es decir, más cerca en el tiempo del Big Bang) quieras retroceder, puedes obtener temperaturas aún más altas.\n\n",
            5,
            LocalDate.of(2024,7,18)   
        );

        photo = photoToBlob("static/images/temperature.jpg");
        news4.setPhoto(photo);
        news4.setImage(photo != null);

        News news5 = new News(
            "Cómo ver las Perseidas, la mejor lluvia de meteoritos de 2024",
            "Este año, las Perseidas ocurren sin Luna en el cielo de la mañana, lo que las eleva hasta convertirse probablemente en la mejor lluvia de meteoritos de 2024.",
            "Alison Klesman",
            "Próximos eventos",
            "Con altas tasas de meteoros y temperaturas agradables en verano, la lluvia de meteoritos de las Perseidas es una de las más celebradas del año. La lluvia de meteoritos de las Perseidas, que ocupa el segundo lugar en tasa máxima después de las Gemínidas de diciembre, este año ocupa un lugar central, gracias a la sincronización de las fases de la Luna.\n\n"
        + "Este año, la lluvia de meteoritos de las Perseidas alcanza su punto máximo el 12 de agosto con un primer cuarto de luna, que se pone antes de la medianoche para dejar las primeras horas de la mañana, las mejores para disfrutar del espectáculo, oscuras y sin luna. Por el contrario, el pico de las Gemínidas el 14 de diciembre de este año coincide con una Luna casi llena, que proyecta su luz brillante que oculta los meteoritos desde aproximadamente el anochecer hasta el amanecer.\n\n"
        + "Eso significa que las Perseidas se perfilan como la mejor lluvia de meteoritos del año, que no debe perderse. Aquí se explica cómo ver el programa.\n\n"
        + "Cuando ver las Perseidas\n"
        + "El pico oficial de las Perseidas ocurre el 12 de agosto en horas de la mañana para los observadores en los EE. UU. Esto coincide con el mejor momento en general para captar una lluvia de meteoritos, ya que por la mañana la Tierra gira hacia la corriente de escombros que causa la lluvia, lo que da como resultado trenes de meteoritos más largos que surcan el cielo. Sin embargo, la mañana anterior y la mañana posterior al pico (los días 11 y 13) también contarán con altas tasas de meteoros Perseidas, por lo que puedes extender tu observación durante varios días o planificar copias de seguridad si la fecha pico específica está nublada o es inconveniente.\n\n"
        + "Cómo ver las Perseidas\n"
        + "No necesitas ningún equipo para observar una lluvia de meteoritos; de hecho, tus ojos son mejores, ya que puedes escanear una gran región del cielo rápidamente sin sacrificar el campo de visión por la ampliación. Como mucho, es posible que desees un sillón o una manta, así como algo para mantenerte hidratado y quizás algún repelente de insectos, dependiendo de tu ubicación.\n\n"
        + "Los meteoros Perseidas parecen provenir de un punto específico, o radiante, en la constelación de Perseo, que alcanza aproximadamente 60° sobre el horizonte una hora antes del amanecer de esta semana. Cuanto más alto sea el radiante, más meteoros verás: las lluvias de meteoritos se caracterizan por su velocidad horaria cenital máxima, que estima la cantidad de meteoros visibles si el radiante estuviera directamente sobre tu cabeza (90°). Teniendo en cuenta la altura que alcanzará el radiante de las Perseidas antes del amanecer, la velocidad máxima de la lluvia de unos 100 meteoros por hora debería atenuarse sólo en un 15 por ciento aproximadamente, lo que significa que seguirá produciendo más de 80 meteoros por hora. ¡Eso es más de un meteoro por minuto!\n\n"
        + "(Tenga en cuenta, sin embargo, que eso es un promedio: es más probable que vea muchos meteoros en un corto período de tiempo, seguidos de una breve pausa, que exactamente la misma cantidad de meteoros cada minuto en una hora).\n\n"
        + "Para encontrar a Perseo, mira hacia el este una o dos horas antes del amanecer. Verás a Géminis y Orión recién ascendiendo, con la constelación en forma de V de Tauro sobre el patrón curvo de estrellas que delinea el arco de Orión. Actualmente, Tauro alberga tres planetas, incluidos los brillantes Marte y Júpiter, que están a solo unos días de una conjunción cercana el día 14. Se encuentran a la izquierda de la brillante estrella de color rojo anaranjado Aldebarán, que sirve como ojo de Tauro. (Consulte nuestra columna Sky This Week para obtener más detalles sobre cómo observar esta conjunción y esté atento a nuestro sitio para obtener información adicional a medida que se acerca el evento).\n\n"
        + "Por encima de Tauro en el cielo está Perseo, con el radiante de las Perseidas en la región norte de la constelación. El radiante está aproximadamente a 23° directamente encima de la brillante estrella Capella en la constelación de Auriga, en la parte inferior izquierda de Perseo.\n\n"
        + "Una vez que haya encontrado el área general del radiante, asegúrese de escanear entre 40° y 60° desde este punto. Debido a que las Perseidas parecen alejarse del radiante, verás los senderos más largos e impresionantes en el cielo a cierta distancia de este punto. Las Perseidas en particular son conocidas por sus trenes brillantes y duraderos. Como todos los meteoros, estas rayas son causadas por pequeñas motas de roca y polvo que se queman en nuestra atmósfera. Pero no te preocupes: los fragmentos de escombros que generan las lluvias de meteoritos son tan pequeños que se queman por completo antes de tocar el suelo.\n\n"
        + "Las Perseidas se generan a partir de restos de visitas anteriores del cometa 109P/Swift-Tuttle, que vuelve a girar alrededor del Sol cada 133 años. Su perihelio más reciente (paso cercano del Sol) fue en 1992, y no volverá a adornar nuestros cielos hasta 2125. Sin embargo, Swift-Tuttle es un cometa grande de unas 16 millas (26 kilómetros) de diámetro, lo que significa que tiene mucho de material que se desprende cada vez que se calienta cerca del Sol. Ese material, que permanece aproximadamente a lo largo de la órbita del cometa, cruza la órbita de la Tierra en agosto y crea los meteoros Perseidas que vemos.\n\n"
        + "A continuación se muestran las horas de salida y puesta del sol, así como la puesta y salida de la luna, en el pico de las Perseidas y un día a cada lado de ese pico. En general, las Perseidas están activas desde finales de julio hasta finales de agosto, por lo que es posible que sigas viendo rezagados durante las próximas dos semanas, pero el verdadero espectáculo será del 11 al 13 de agosto, ¡así que asegúrate de no perdértelo!\n\n"
        + "Domingo 11 de agosto\n"
        + "Amanecer: 6:08 a.m.\n"
        + "Atardecer: 8:01 p.m.\n"
        + "Salida de la luna: 1:04 p.m.\n"
        + "Puesta de luna: 23:12\n"
        + "Fase lunar: Luna creciente (41%)\n"
        + "*Las horas de salida, puesta, salida y puesta de la luna se dan en la hora local desde 40° N 90° W. La iluminación de la Luna se da a las 12 p.m. hora local desde el mismo lugar.\n\n"
        + "Lunes 12 de agosto: pico de las Perseidas\n"
        + "Amanecer: 6:09 a.m.\n"
        + "Atardecer: 20:00 h.\n"
        + "Salida de la luna: 2:08 p.m.\n"
        + "Puesta de luna: 23:42\n"
        + "Fase lunar: menguante creciente (51%)\n\n"
        + "Martes 13 de agosto\n"
        + "Amanecer: 6:10 a.m.\n"
        + "Atardecer: 19:59\n"
        + "Salida de la luna: 3:14 p.m.\n"
        + "Puesta de luna: —\n"
        + "Fase lunar: gibosa creciente (61%)",
            12,
            LocalDate.of(2024,8,8)   
        );

        photo = photoToBlob("static/images/meteors.jpeg");
        news5.setPhoto(photo);
        news5.setImage(photo != null);

        newsRepository.save(news1);
        newsRepository.save(news2);
        newsRepository.save(news3);
        newsRepository.save(news4);
        newsRepository.save(news5);

    }


    private void initPictures(){
        Picture picture1 = new Picture(
            "El cazador se levanta",
            "Panagiotis Andreou",
            "Nueva Zelanda",
            LocalDate.of(2024,2,18),
            "Orión el Cazador se eleva sobre Aoraki, también conocido como Monte Cook, la montaña más alta de Nueva Zelanda. El fotógrafo utilizó una cámara DSLR Nikon y un objetivo de 40 mm f/1,4 con ISO 1600 para realizar 12 minutos de exposiciones del cielo, más un fotograma de 90 segundos para la montaña."
        );

        Blob photo = null;
        photo = photoToBlob("static/images/hunter.jpeg");
        picture1.setPhoto(photo);
        picture1.setImage(photo != null);

        Picture picture2 = new Picture(
            "La marca de un lobo",
            "Andrea Arbizzi",
            "Modena, Italia",
            LocalDate.of(2024,4,4),
            "WR 134 en Cygnus es una de las primeras estrellas Wolf-Rayet conocidas, una clase de estrellas raras que llevan el nombre de sus descubridores, los astrónomos franceses Charles Wolf y Georges Rayet. Las estrellas WR originalmente destacaban por sus amplias bandas de emisión. Más tarde, los investigadores descubrieron que eran el resultado de intensos vientos estelares, que pueden formar nebulosas como la que se ve aquí. Esta imagen fue tomada durante 24¾ horas con un filtro de doble banda Ha/OIII y un visor de 8 pulgadas a f/7,3." //
        );

        photo = photoToBlob("static/images/wolf.jpeg");
        picture2.setPhoto(photo);
        picture2.setImage(photo != null);

        Picture picture3 = new Picture(
            "Un diario de viaje de la estrella de la mañana",
            "Tunç Tezel",
            "Bursa, Turquía",
            LocalDate.of(2024,6,4),
            "La aparición matutina de Venus en 2023-2024 se captura en esta secuencia de imágenes, tomadas al mismo tiempo cada mañana despejada desde finales de agosto de 2023 hasta principios de febrero de 2024. Venus ahora se está volviendo más visible en el cielo nocturno, después de haber pasado por una conjunción superior el 4 de junio."
        );

        photo = photoToBlob("static/images/travelog.jpeg");
        picture3.setPhoto(photo);
        picture3.setImage(photo != null);

        Picture picture4 = new Picture(
            "Soplando burbujas",
            "Steve Leonard",
            "Markham, Ontario, Canadá",
            LocalDate.of(2024,1,21),
            "La Nebulosa Burbuja (NGC 7635) es una nebulosa de emisión que rodea a la estrella SAO 20575 en Casiopea; el débil caparazón es creado por sus intensos vientos. El generador de imágenes utilizó un refractor de 4,5 pulgadas f/5,7 para tomar 24 horas de datos SHO y 3 horas de datos RGB. La imagen final es una combinación de una interpretación de la paleta Hubble y una paleta dinámica Foraxx, que utiliza una combinación no lineal de canales."
        );

        photo = photoToBlob("static/images/bubble.jpeg");
        picture4.setPhoto(photo);
        picture4.setImage(photo != null);

        Picture picture5 = new Picture(
            "Centauro A",
            "Vikas Chander",
            "Observatorio El Sauce in the Río Hurtado Valley, Chile",
            LocalDate.of(2023,11,30),
            "A sólo 12 millones de años luz de distancia, Centaurus A (NGC 5128) es la galaxia activa más cercana a nosotros, con un agujero negro supermasivo en su núcleo que escupe material al medio intergaláctico. Es famoso que estos flujos de salida se puedan ver en las emisiones de radio como enormes lóbulos a ambos lados de la galaxia. Sin embargo, esta imagen Hα/OIII/LRGB tomada desde el desierto de Atacama en Chile con un telescopio robótico de 24 pulgadas (y casi 24 horas de exposición) ofrece una vista detallada de las eyecciones de materia en luz visible en la parte inferior derecha."
        );

        photo = photoToBlob("static/images/centaurus.jpeg");
        picture5.setPhoto(photo);
        picture5.setImage(photo != null);

        Picture picture6 = new Picture(
            "Historia de dos senderos",
            "Giovanni Passalacqua",
            "Sicilia, Italia",
            LocalDate.of(2024,6,28),
            "Los rastros dejados por bolas brillantes de gas y trozos de roca fundida se complementan en esta fotografía del Monte Etna en erupción el 28 de junio."
        );

        photo = photoToBlob("static/images/trails.jpeg");
        picture6.setPhoto(photo);
        picture6.setImage(photo != null);

        Picture picture7 = new Picture(
            "Acumulación de tormentas",
            "Mark Johnston",
            "Scottsdale, Arizona",
            LocalDate.of(2024,3,15),
            "Los fuegos artificiales del ciclo solar 25 continúan, como se ve en esta fotografía de Hα del 2 de julio que presenta un filamento en la parte inferior izquierda y protuberancias que saltan del borde del Sol activo. La foto se tomó con un refractor de 6 pulgadas equipado con un filtro de rechazo de energía y un etalón Lunt con un “ocular” Daystar Hα."
        );

        photo = photoToBlob("static/images/storm.jpeg");
        picture7.setPhoto(photo);
        picture7.setImage(photo != null);

        Picture picture8 = new Picture(
            "Estrellas y estrellas de mar",
            "Rob Lyons",
            "Vancouver, Canadá",
            LocalDate.of(2024,5,17),
            "El fotógrafo cuenta cómo consiguió esta espectacular foto:\r\n" + //
            "\r\n" + //
            "He intentado tomar esta fotografía durante tres años durante mis viajes anuales a Tofino, Columbia Británica. El primer año no pude encontrar ninguna estrella de mar. En el segundo año encontré algunas e hice una imagen, pero las estrellas de mar eran tan pequeñas que necesitarías un telescopio para verlas. El año pasado, después de una semana de búsqueda, encontré algunos en mi última noche de viaje. Estas estrellas de mar estaban adheridas a una roca que se encuentra en un lugar popular para practicar surf, por lo que las olas son muy activas. Para obtener el primer plano, fotografié en la hora azul y utilicé mi cámara Sony A7R con una lente de 20 mm f/1,8 para acercarme a la estrella de mar. La marea subía rápido y la estrella de mar ya estaba medio sumergida, así que trabajé rápido para capturar la foto. Luego instalé mi rastreador de estrellas en la seguridad de los acantilados rocosos cercanos, tomé 15 imágenes de la Vía Láctea [con un corte UV/IR de Kolari Vision y un filtro de paso Hα] y las apilé. Luego se combinaron los dos disparos."
        );

        photo = photoToBlob("static/images/starfish.jpeg");
        picture8.setPhoto(photo);
        picture8.setImage(photo != null);

        Picture picture9 = new Picture(
            "Eruipción de auroras",
            "Marty Weintraub",
            "Duluth, Minnesota",
            LocalDate.of(2024,2,6),
            "Las auroras, las nubes y el terreno se combinan para crear la ilusión de un vórtice de luz que brota de las montañas, visto desde Tungeneset en la isla noruega de Senja en esta fotografía tomada el 6 de febrero. El fotógrafo utilizó una cámara sin espejo Sony y una lente de 14 mm a f /1,8 para realizar una exposición de 2,5 segundos a ISO 1600."
        );

        photo = photoToBlob("static/images/aurora.jpg");
        picture9.setPhoto(photo);
        picture9.setImage(photo != null);

        Picture picture10 = new Picture(
            "Un sol tormentoso",
            "Marco Meniero",
            "Roma, Italia",
            LocalDate.of(2023,12,12),
            "La enormidad total del grupo de manchas solares AR 3664 (en la parte inferior del disco solar en esta imagen) es evidente en esta fotografía tomada con una cámara sin espejo Nikon Z9 y una lente de 400 mm a ISO 32. La fotografía es una fotografía compuesta, expuesta al sol. disco (1/32.000 de segundo a f/32) y uno para el paisaje (1/4.000 de segundo y f/9). AR 3664 tiene aproximadamente 16 veces el tamaño de la Tierra y es responsable de la tormenta geomagnética de este fin de semana."
        );

        photo = photoToBlob("static/images/sun.jpeg");
        picture10.setPhoto(photo);
        picture10.setImage(photo != null);

        pictureRepository.save(picture1);
        pictureRepository.save(picture2);
        pictureRepository.save(picture3);
        pictureRepository.save(picture4);
        pictureRepository.save(picture5);
        pictureRepository.save(picture6);
        pictureRepository.save(picture7);
        pictureRepository.save(picture8);
        pictureRepository.save(picture9);
        pictureRepository.save(picture10);
    }

    private void initQuizzes(){
        //Initialize quizz1
        Quizz quizz1 = new Quizz();
        quizz1.setName("Bienvenida");
        quizz1.setDifficulty("Fácil");

        Blob photo = null;
        photo = photoToBlob("static/images/logo2.png");
        quizz1.setPhoto(photo);
        quizz1.setImage(photo != null);

        List<Question> questions = new ArrayList<Question>();
        Question question1 = new Question();
        question1.setQuestion("¿Cómo se llama la página web?");
        question1.setOption1("Astro News");
        question1.setOption2("Cosmic News");
        question1.setOption3("Astronomy For All");
        question1.setOption4("Hidden World");
        question1.setAnswer("Cosmic News");
        question1.setNum(1);
        question1.setQuizz(quizz1);

        question1.setCorrect1(false);
        question1.setCorrect2(true);
        question1.setCorrect3(false);
        question1.setCorrect4(false);

        questions.add(question1);

        Question question2 = new Question();
        question2.setQuestion("¿Si quieres informarte acerca del Sistema Solar dónde debes acudir?");
        question2.setOption1("Sistema Solar");
        question2.setOption2("Perfil");
        question2.setOption3("Fotos");
        question2.setOption4("Calendario");
        question2.setAnswer("Sistema Solar");
        question2.setNum(2);
        question2.setQuizz(quizz1);

        question2.setCorrect1(true);
        question2.setCorrect2(false);
        question2.setCorrect3(false);
        question2.setCorrect4(false);

        questions.add(question2);

        Question question3 = new Question();
        question3.setQuestion("¿Qué puedes encontrar en la aplicación?");
        question3.setOption1("Noticias");
        question3.setOption2("Fotos");
        question3.setOption3("Vídeos");
        question3.setOption4("Todas son correctas");
        question3.setAnswer("Todas son correctas");
        question3.setNum(3);
        question3.setQuizz(quizz1);

        question3.setCorrect1(false);
        question3.setCorrect2(false);
        question3.setCorrect3(false);
        question3.setCorrect4(true);

        questions.add(question3);

        Question question4 = new Question();
        question4.setQuestion("¿Dónde puedes encontrar la fecha de eventos astronómicos?");
        question4.setOption1("Perfil");
        question4.setOption2("Sistema Solar");
        question4.setOption3("Calendario");
        question4.setOption4("Quizzes");
        question4.setAnswer("Calendario");
        question4.setNum(4);
        question4.setQuizz(quizz1);

        question4.setCorrect1(false);
        question4.setCorrect2(false);
        question4.setCorrect3(true);
        question4.setCorrect4(false);

        questions.add(question4);

        Question question5 = new Question();
        question5.setQuestion("¿Puedes dar like a las fotos y noticias?");
        question5.setOption1("Sí");
        question5.setOption2("No");
        question5.setOption3("Sólo fotos");
        question5.setOption4("Sólo noticias");
        question5.setAnswer("Sí");
        question5.setNum(5);
        question5.setQuizz(quizz1);

        question5.setCorrect1(true);
        question5.setCorrect2(false);
        question5.setCorrect3(false);
        question5.setCorrect4(false);

        questions.add(question5);
        quizz1.setQuestions(questions);

        Quizz quizz2 = new Quizz();
        quizz2.setName("El Sol");
        quizz2.setDifficulty("Difícil");

        photo = photoToBlob("static/images/sun.png");
        quizz2.setPhoto(photo);
        quizz2.setImage(photo != null);

        questions = new ArrayList<Question>();
        question1 = new Question();
        question1.setQuestion("¿Hace cuántos años se formó?");
        question1.setOption1("4600 millones de años");
        question1.setOption2("5000 millones de años");
        question1.setOption3("5400 millones de años");
        question1.setOption4("6000 millones de años");
        question1.setAnswer("4600 millones de años");
        question1.setNum(1);
        question1.setQuizz(quizz2);

        question1.setCorrect1(true);
        question1.setCorrect2(false);
        question1.setCorrect3(false);
        question1.setCorrect4(false);

        questions.add(question1);

        question2 = new Question();
        question2.setQuestion("¿Cuál es la distancia media del Sol a la Tierra?");
        question2.setOption1("100 millones de kilometros");
        question2.setOption2("125 millones de kilometros");
        question2.setOption3("150 millones de kilometros");
        question2.setOption4("200 millones de kilometros");
        question2.setAnswer("150 millones de kilometros");
        question2.setNum(2);
        question2.setQuizz(quizz2);

        question2.setCorrect1(false);
        question2.setCorrect2(false);
        question2.setCorrect3(true);
        question2.setCorrect4(false);

        questions.add(question2);

        question3 = new Question();
        question3.setQuestion("El Sol es una estrella de tipo...");
        question3.setOption1("F");
        question3.setOption2("G");
        question3.setOption3("O");
        question3.setOption4("Z");
        question3.setAnswer("G");
        question3.setNum(3);
        question3.setQuizz(quizz2);

        question3.setCorrect1(false);
        question3.setCorrect2(true);
        question3.setCorrect3(false);
        question3.setCorrect4(false);

        questions.add(question3);

        question4 = new Question();
        question4.setQuestion("El Sol es de edad...");
        question4.setOption1("Joven");
        question4.setOption2("Intermedia");
        question4.setOption3("Avanzada");
        question4.setOption4("Vieja");
        question4.setAnswer("Intermedia");
        question4.setNum(4);
        question4.setQuizz(quizz2);

        question4.setCorrect1(false);
        question4.setCorrect2(true);
        question4.setCorrect3(false);
        question4.setCorrect4(false);

        questions.add(question4);

        question5 = new Question();
        question5.setQuestion("¿Cuánto tarda en viajar la luz del Sol a la Tierra?");
        question5.setOption1("10 minutos");
        question5.setOption2("11 minutos y 10 segundos");
        question5.setOption3("9 minutos y 31 segundos");
        question5.setOption4("8 minutos y 20 segundos");
        question5.setAnswer("8 minutos y 20 segundos");
        question5.setNum(5);
        question5.setQuizz(quizz2);

        question5.setCorrect1(false);
        question5.setCorrect2(false);
        question5.setCorrect3(false);
        question5.setCorrect4(true);

        questions.add(question5);

        quizz2.setQuestions(questions);

        Quizz quizz3 = new Quizz();
        quizz3.setName("Sistema Solar");
        quizz3.setDifficulty("Media");

        photo = photoToBlob("static/images/earth.png");
        quizz3.setPhoto(photo);
        quizz3.setImage(photo != null);

        questions = new ArrayList<Question>();
        question1 = new Question();
        question1.setQuestion("¿Cuál es el planeta más cercano al Sol?");
        question1.setOption1("Venus");
        question1.setOption2("Mercurio");
        question1.setOption3("Marte");
        question1.setOption4("Plutón");
        question1.setAnswer("Mercurio");
        question1.setNum(1);
        question1.setQuizz(quizz3);

        question1.setCorrect1(false);
        question1.setCorrect2(true);
        question1.setCorrect3(false);
        question1.setCorrect4(false);

        questions.add(question1);

        question2 = new Question();
        question2.setQuestion("La luna es un satélite de...");
        question2.setOption1("La Tierra");
        question2.setOption2("Marte");
        question2.setOption3("Júpiter");
        question2.setOption4("Saturno");
        question2.setAnswer("La Tierra");
        question2.setNum(2);
        question2.setQuizz(quizz3);

        question2.setCorrect1(true);
        question2.setCorrect2(false);
        question2.setCorrect3(false);
        question2.setCorrect4(false);

        questions.add(question2);

        question3 = new Question();
        question3.setQuestion("El planeta más antiguo del Sistema Solar es...");
        question3.setOption1("La Tierra");
        question3.setOption2("Mercurio");
        question3.setOption3("Júpiter");
        question3.setOption4("Neptuno");
        question3.setAnswer("Júpiter");
        question3.setNum(3);
        question3.setQuizz(quizz3);

        question3.setCorrect1(false);
        question3.setCorrect2(false);
        question3.setCorrect3(true);
        question3.setCorrect4(false);

        questions.add(question3);

        question4 = new Question();
        question4.setQuestion("¿Qué planeta recibe su nombre debido a la diosa romana del amor (en la Antigua Grecia, Afrodita)?");
        question4.setOption1("Urano");
        question4.setOption2("Mercurio");
        question4.setOption3("Neptuno");
        question4.setOption4("Venus");
        question4.setAnswer("Venus");
        question4.setNum(4);
        question4.setQuizz(quizz3);

        question4.setCorrect1(false);
        question4.setCorrect2(true);
        question4.setCorrect3(false);
        question4.setCorrect4(false);

        questions.add(question4);

        question5 = new Question();
        question5.setQuestion("¿Qué planeta es conocido como 'El planeta rojo'?");
        question5.setOption1("Mercurio");
        question5.setOption2("Júpiter");
        question5.setOption3("Marte");
        question5.setOption4("Urano");
        question5.setAnswer("Marte");
        question5.setNum(5);
        question5.setQuizz(quizz3);

        question5.setCorrect1(false);
        question5.setCorrect2(false);
        question5.setCorrect3(true);
        question5.setCorrect4(false);

        questions.add(question5);

        Question question6 = new Question();
        question6.setQuestion("¿Quién fue la primera persona en observar los anillos de Saturno?");
        question6.setOption1("Galileo Galilei");
        question6.setOption2("Nicolás Copérnico");
        question6.setOption3("Johannes Kepler");
        question6.setOption4("Sócrates");
        question6.setAnswer("Galileo Galilei");
        question6.setNum(6);
        question6.setQuizz(quizz3);

        question6.setCorrect1(true);
        question6.setCorrect2(false);
        question6.setCorrect3(false);
        question6.setCorrect4(false);

        questions.add(question6);

        quizz3.setQuestions(questions);

        quizzRepository.save(quizz1);
        quizzRepository.save(quizz2);
        quizzRepository.save(quizz3);
    }

    private void initEvents(){
        Event event1 = new Event(
            LocalDate.of(2025,7,7),
            "bi bi-moon-fill",
            "¡Luna llena! La Luna está en el lado opuesto de la Tierra por lo que el Sol la ilumina por completo. La Luna Llena es a las 18:27 UTC."
        );

        Event event2 = new Event(
            LocalDate.of(2025,7,19),
            "bi bi-diagram-3",
            "¡Conjunción de la Luna y Marte! La Luna pasa a unos 5,1º al norte de Marte a las 00:23 UTC. La Luna tiene una magnitud de -11,4 y Marte una magnitud de 0,8. En este momento la fase lunar es del 32,9%."
        );

        eventRepository.save(event1);
        eventRepository.save(event2);
    }

    private void initVideos(){
        Video video1 = new Video(
            "¿Qué pasa dentro de un agujero negro?",
            "6:34",
            "Los agujeros negros son uno de los fenómenos más enigmáticos del universo: para que use formen, primero una estrella tiene morir. Además, todo lo que entra, jamás logra escapar,  tampoco la luz. Y en su corazón, en la llamada singularidad, el tiempo y el espacio se detienen. ¿Pero qué son exactamente los agujeros negros? ¿Y por qué es importante entender qué es la singularidad, ese punto en el que dejan de tener sentido las leyes de la naturaleza que conocemos?",
            "dMEho2ZcVtE"
        );

        Video video2 = new Video(
            "Toda la evolución de la Tierra en solo 10 minutos",
            "10:41",
            "¿Y si repasáramos toda la evolución de la Tierra en solo 9 minutos? ¿Preparado?",
            "XrA5qcs31iw"
        );

        Video video3 = new Video(
            "NOTICIAS DE MARTE - AGOSTO 2024",
            "29:59",
            "Repasamos las últimas noticias acerca del planeta Marte",
            "7HV-grdOmGo"
        );

        Video video4 = new Video(
            "¿Qué es el Sol y cómo funciona? ¿Es una bola de fuego?",
            "13:07",
            "Qué es realmente el Sol y cómo funciona su interior. Aprovechar el Sol mediante esferas de Dyson. Gracias Giordano Bruno a Kepler, Newton y Einstein entre otros, descubrimos el Universo y que el Sol es una estrella, además de ser un reactor de fusión nuclear que nos da calor en forma de radiación electromagnética.",
            "ePY07OymPkU"
        );

        Video video5 = new Video(
            "¿Qué pasaría si un asteroide impactara la Tierra en 2024?",
            "8:03",
            "Un asteroide viene directo hacia la Tierra. Al principio, los científicos dijeron que pasaría de largo, pero al parecer, han cometido graves errores de cálculo.\r\n" + //
                                "\r\n" + //
                                "¿Podríamos alterar la trayectoria del asteroide?\r\n" + //
                                "¿Y por qué dicen que podría ocurrir antes de 2029?",
            "V5Eh_tZP5yQ"
        );

        Video video6 = new Video(
            "¿Cómo es realmente la Vía Láctea?",
            "16:34",
            "Según la hipótesis científica actualmente dominante, hace unos 13.800 millones de años, un grandioso evento llamado \"Big Bang\" dio origen a nuestro universo. Poco después, ondas gravitatorias de increíble fuerza comprimieron el gas primordial en coágulos gigantes, y las inexorables fuerzas de atracción encendieron un fuego termonuclear en sus profundidades, dando al universo la luz de las primeras estrellas. Las mismas fuerzas unieron las luminarias en cúmulos a gran escala y, luego, en estructuras aún más complejas de dimensiones colosales. Miles de millones de años después, uno de estos se convirtió en la cuna de un fenómeno asombroso: la \"vida\". Y aunque el hombre aún no es capaz de salir de su sistema de origen, ha aprendido mucho sobre su propia Galaxia. Entonces, ¿qué sabemos ya sobre la Vía Láctea?",
            "aqxpcRod_9k"
        );

        videoRepository.save(video1);
        videoRepository.save(video2);
        videoRepository.save(video3);
        videoRepository.save(video4);
        videoRepository.save(video5);
        videoRepository.save(video6);     
    }

    private void initPlanets(){
        Planet planet1 = new Planet(
            "Mercurio",
            4,
            110,
            0.01,
            "Mercurio es el planeta del sistema solar más cercano al Sol y el más pequeño. Forma parte de los denominados planetas interiores y carece de satélites naturales al igual que Venus. Se conocía muy poco sobre su superficie hasta que fue enviada la sonda planetaria Mariner 10 y se hicieron observaciones con radar y radiotelescopios. Posteriormente fue estudiado por la sonda MESSENGER de la NASA y actualmente la astronave de la Agencia Europea del Espacio (ESA) denominada BepiColombo, lanzada en octubre de 2018, se halla en vuelo rumbo a Mercurio a donde llegará en 2025 y se espera que aporte nuevos conocimientos sobre el origen y composición del planeta, así como de su geología y campo magnético.\n\n"
                + "Antiguamente se pensaba que Mercurio siempre presentaba la misma cara al Sol (rotación capturada), situación similar al caso de la Luna con la Tierra; es decir, que su periodo de rotación era igual a su periodo de traslación, ambos de 88 días. Sin embargo, en 1965 se mandaron impulsos de radar hacia Mercurio, con lo cual quedó definitivamente demostrado que su periodo de rotación era de 58,7 días, lo cual es ⅔ de su periodo de traslación. Esto no es coincidencia, y es una situación denominada resonancia orbital.\n\n"
                + "Al ser un planeta cuya órbita es inferior a la de la Tierra, lo observamos pasar periódicamente delante del Sol, fenómeno que se denomina tránsito astronómico. Observaciones de su órbita a través de muchos años demostraron que el perihelio gira 43\" de arco más por siglo de lo predicho por la mecánica clásica de Newton. Esta discrepancia llevó a un astrónomo francés, Urbain Le Verrier, a pensar que existía un planeta aún más cerca del Sol, al cual llamaron Vulcano, que perturbaba la órbita de Mercurio. Ahora se sabe que Vulcano no existe; la explicación correcta del comportamiento del perihelio de Mercurio se encuentra en la teoría general de la relatividad de Einstein.",
            "images/mercury.png"
        );

        Blob photo = null;
        photo = photoToBlob("static/images/mercury.png");
        planet1.setPhoto(photo);

        Planet planet2 = new Planet(
            "Venus",
            7,
            170,
            0.007,
            "Venus es el segundo planeta del sistema solar en orden de proximidad al Sol y el tercero más pequeño después de Mercurio y Marte. Al igual que Mercurio, carece de satélites naturales. Recibe su nombre en honor a Venus, la diosa romana del amor (en la Antigua Grecia, Afrodita). Al ser el segundo objeto natural más brillante después de la Luna, puede ser visto en un cielo nocturno despejado a simple vista. Aparece al despuntar el día y al atardecer. Debido a las distancias de las órbitas de Venus y la Tierra desde el Sol, Venus nunca es visible más de tres horas antes del amanecer o tres horas después del ocaso.\n\n"
                + "Se trata de un planeta interior de tipo rocoso y terrestre, llamado con frecuencia el planeta hermano de la Tierra, ya que ambos son similares en cuanto a tamaño, masa y composición, aunque totalmente diferentes en cuestiones térmicas y atmosféricas (la temperatura media de Venus es de 463.85 °C). Su órbita es una elipse con una excentricidad de menos del 1 %, formando la órbita más circular de todos los planetas; apenas supera la de Neptuno. Su presión atmosférica es 90 veces superior a la terrestre; es, por lo tanto, la mayor presión atmosférica de todos los planetas rocosos del sistema solar. Es de color amarillento debido a su atmósfera, que está compuesta en su mayoría por dióxido de carbono (CO2), ácido sulfhídrico (H2S) y nitrógeno (N2).\n\n"
                + "Pese a situarse más lejos del Sol que Mercurio, Venus posee la atmósfera más caliente del sistema solar; esto se debe a que está principalmente compuesta por gases de efecto invernadero, como el dióxido de carbono, atrapando mucho más calor del Sol. Actualmente carece de agua líquida y sus condiciones en superficie se consideran incompatibles con la vida conocida, aunque en descubrimientos recientes se ha encontrado fosfina en su superficie nebular, una molécula que en la Tierra es generada por microbios, lo que da indicios de una posible existencia de vida. No obstante, el Instituto Goddard de Estudios Espaciales de la NASA y otros han postulado que en el pasado Venus pudo tener océanos con tanta agua como el terrestre y reunir condiciones de habitabilidad planetaria.\n\n"
                + "Este planeta además posee el día más largo del sistema solar —243 días terrestres—, su movimiento es dextrógiro, es decir, gira en el sentido de las manecillas del reloj, contrario al movimiento de los otros planetas. Por ello, en un día venusiano el Sol sale por el oeste y se pone por el este. Sus nubes, sin embargo, pueden dar la vuelta al planeta en cuatro días terrestres. De hecho, previamente a estudiarlo con naves no tripuladas en su superficie o con radares, se pensaba que el período de rotación de Venus era de unos cuatro días terrestres.\n\n"
                + "Al encontrarse Venus más cercano al Sol que la Tierra, siempre se puede encontrar en las inmediaciones del Sol (su mayor elongación es de 47.8°), por lo que desde la Tierra se puede ver solo durante unas pocas horas antes del orto (salida del Sol) en unos determinados meses del año; también durante unas pocas horas después del ocaso (puesta del Sol) en el resto del año. A pesar de ello, cuando Venus es más brillante puede ser visto durante el día, siendo uno de los tres únicos cuerpos celestes que pueden ser vistos de día a simple vista además de la Luna y el Sol. Conocido como la estrella de la mañana («lucero del alba») o de la tarde («lucero vespertino»), cuando es visible en el cielo nocturno es el segundo objeto más brillante del firmamento tras la Luna, por lo que Venus debió ser ya conocido desde los tiempos prehistóricos.\n\n"
                + "La mayoría de las antiguas civilizaciones conocían los movimientos en el cielo de Venus, por lo que adquirió importancia en casi todas las interpretaciones astrológicas del movimiento planetario. En particular, la civilización maya elaboró un calendario religioso basado en los ciclos astronómicos, incluidos los ciclos de Venus. El símbolo del planeta Venus es una representación estilizada del espejo de la diosa Venus: un círculo con una pequeña cruz debajo, utilizado también hoy para denotar el sexo femenino.",
            "images/venus.png"
        );

        photo = photoToBlob("static/images/venus.png");
        planet2.setPhoto(photo);

        Planet planet3 = new Planet(
            "La Tierra",
            8,
            240,
            0.005,
            "La Tierra (del latín Terra, deidad romana equivalente a Gea, diosa griega de la feminidad y la fecundidad) es un planeta del sistema solar que gira alrededor de su estrella —el Sol— en la tercera órbita más interna. Es el más denso y el quinto mayor de los ocho planetas del sistema solar. También es el más grande de los cuatro planetas terrestres o rocosos.\n\n"
                + "La Tierra se formó hace aproximadamente 4550 millones de años y la vida surgió unos mil millones de años después. Es el hogar de millones de especies, incluidos los seres humanos y actualmente el único cuerpo astronómico donde se conoce la existencia de vida. La atmósfera y otras condiciones abióticas han sido alteradas significativamente por la biosfera del planeta, favoreciendo la proliferación de organismos aerobios, así como la formación de una capa de ozono que junto con el campo magnético terrestre bloquean la radiación solar dañina, permitiendo así la vida en la Tierra. Las propiedades físicas de la Tierra, la historia geológica y su órbita han permitido que la vida siga existiendo. Se estima que el planeta seguirá siendo capaz de sustentar vida durante otros 500 millones de años, ya que según las previsiones actuales, pasado ese tiempo la creciente luminosidad del Sol terminará causando la extinción de la biosfera.\n\n"
                + "La superficie terrestre o corteza está dividida en varias placas tectónicas que se deslizan sobre el magma durante periodos de varios millones de años. La superficie está cubierta por continentes e islas; estos poseen varios lagos, ríos y otras fuentes de agua, que junto con los océanos de agua salada que representan cerca del 71 % de la superficie constituyen la hidrósfera. No se conoce ningún otro planeta con este equilibrio de agua líquida, que es indispensable para cualquier tipo de vida conocida. Los polos de la Tierra están cubiertos en su mayoría de hielo sólido (indlandsis de la Antártida) o de banquisas (casquete polar ártico). El interior del planeta es geológicamente activo, con una gruesa capa de manto relativamente sólido, un núcleo externo líquido que genera un campo magnético, y un sólido núcleo interior compuesto por aproximadamente un 88 % de hierro.\n\n"
                + "La Tierra interactúa gravitatoriamente con otros objetos en el espacio, especialmente el Sol y la Luna. En la actualidad, la Tierra completa una órbita alrededor del Sol cada vez que realiza 366.26 giros sobre su eje, lo cual es equivalente a 365.26 días solares o un año sideral. El eje de rotación de la Tierra se encuentra inclinado 23.4° con respecto a la perpendicular a su plano orbital, lo que produce las variaciones estacionales en la superficie del planeta con un período de un año tropical (365.24 días solares). La Tierra posee un único satélite natural, la Luna, que comenzó a orbitar la Tierra hace 4530 millones de años; esta produce las mareas, estabiliza la inclinación del eje terrestre y reduce gradualmente la velocidad de rotación del planeta. Hace aproximadamente 3800 a 4100 millones de años, durante el llamado bombardeo intenso tardío, numerosos asteroides impactaron en la Tierra, causando significativos cambios en la mayor parte de su superficie.\n\n"
                + "Tanto los minerales del planeta como los productos de la biosfera aportan recursos que se utilizan para sostener a la población humana mundial. Sus habitantes están agrupados en unos 200 estados soberanos independientes, que interactúan a través de la diplomacia, los viajes, el comercio y la acción militar. Las culturas humanas han desarrollado muchas ideas sobre el planeta, incluida la personificación de una deidad, la creencia en una Tierra plana o en la Tierra como centro del universo, y una perspectiva moderna del mundo como un entorno integrado que requiere administración.",
            "images/earth.png"
        );

        photo = photoToBlob("static/images/earth.png");
        planet3.setPhoto(photo);

        Planet planet4 = new Planet(
            "Marte",
            7,
            320,
            0.004,
            "Marte es el cuarto planeta en orden de distancia al Sol y el segundo más pequeño del sistema solar, después de Mercurio. Recibió su nombre en homenaje al homónimo dios de la guerra de la mitología romana (Ares en la mitología griega), y también es conocido como «el planeta rojo» debido a la apariencia rojiza que le confiere el óxido de hierro predominante en su superficie. Marte es el planeta interior más alejado del Sol. Es un planeta telúrico con una atmósfera delgada de dióxido de carbono, y tiene dos satélites pequeños y de forma irregular, Fobos y Deimos (hijos del dios griego), que podrían ser asteroides capturados similares al asteroide troyano (5261) Eureka. Sus características superficiales recuerdan tanto a los cráteres de la Luna como a los valles, desiertos y casquetes polares de la Tierra.\n\n"
                + "El periodo de rotación y los ciclos estacionales son similares a los de la Tierra, ya que la inclinación es lo que genera las estaciones. Marte alberga el Monte Olimpo, la montaña y el volcán más grande y alto conocido en el sistema solar, y los Valles Marineris, uno de los mayores cañones del sistema solar. La llana cuenca Boreal en el hemisferio norte cubre el 40% del planeta y puede ser característica de un gigantesco impacto. Aunque en apariencia podría parecer un planeta muerto, no lo es. Sus campos de dunas siguen siendo mecidos por el viento marciano, sus casquetes polares cambian con las estaciones e incluso parece que hay algunos pequeños flujos estacionales de agua.\n\n"
                + "Las investigaciones en curso evalúan su habitabilidad potencial en el pasado, así como la posibilidad de existencia actual de vida. Se planean futuras investigaciones astrobiológicas, entre ellas la Mars 2020 de la NASA y la ExoMars de la ESA. El agua en estado líquido no puede existir en la superficie de Marte debido a su baja presión atmosférica, que es unas 100 veces inferior a la de la Tierra, excepto en las zonas menos elevadas durante cortos periodos de tiempo. Sus dos casquetes polares parecen estar formados en su mayor parte por agua. El volumen de agua helada del casquete polar sur, si se derritiera, sería suficiente como para cubrir la superficie planetaria al completo con una profundidad de 11 metros (36 pies).\n\n"
                + "Marte se puede observar fácilmente a simple vista desde la Tierra, así como su coloración rojiza. Su magnitud aparente alcanza −2.97, y es solamente superada por Júpiter, Venus, la Luna y el Sol. Los telescopios ópticos terrestres están normalmente limitados a resoluciones de aproximadamente 300 km (190 millas) de distancia, cuando la Tierra y Marte están más cercanos, debido a la atmósfera terrestre.\n\n"
                + "El astrónomo danés del siglo XVI Tycho Brahe midió con gran precisión el movimiento de Marte en el cielo. Los datos sobre el movimiento retrógrado aparente (los llamados «lazos») permitieron a Kepler hallar la naturaleza elíptica de su órbita y determinar las leyes del movimiento planetario conocidas como leyes de Kepler.",
            "images/mars.png"
        );

        photo = photoToBlob("static/images/mars.png");
        planet4.setPhoto(photo);

        Planet planet5 = new Planet(
            "Jupiter",
            14,
            400,
            0.002,
            "Júpiter es el planeta más grande del sistema solar y el quinto en orden de lejanía al Sol. Es un gigante gaseoso que forma parte de los denominados planetas exteriores. Recibe su nombre del dios romano Júpiter (Zeus en la mitología griega). Es uno de los objetos naturales más brillantes en un cielo nocturno despejado, superado solo por la Luna, Venus y algunas veces Marte.\n\n"
                + "Se trata del planeta que ofrece un mayor brillo a lo largo del año dependiendo de su fase. Es, además, después del Sol, el mayor cuerpo celeste del sistema solar, con una masa casi dos veces y media de la de los demás planetas juntos (con una masa 318 veces mayor que la de la Tierra y tres veces mayor que la de Saturno, además de ser, en cuanto a volumen, 1321 veces más grande que la Tierra). También es el planeta más antiguo del sistema solar, siendo incluso más antiguo que el Sol; este descubrimiento fue realizado por investigadores de la universidad de Münster en Alemania.\n\n"
                + "Júpiter es un cuerpo masivo gaseoso, formado principalmente por hidrógeno y helio, carente de una superficie interior definida. Entre los detalles atmosféricos es notable la Gran Mancha Roja (un enorme anticiclón situado en las latitudes tropicales del hemisferio sur), la estructura de nubes en bandas oscuras y zonas brillantes, y la dinámica atmosférica global determinada por intensos vientos zonales alternantes en latitud y con velocidades de hasta 140 m/s (504 km/h).",
            "images/jupiter.png"
        );

        photo = photoToBlob("static/images/jupiter.png");
        planet5.setPhoto(photo);

        Planet planet6 = new Planet(
            "Saturno",
            12,
            500,
            0.0015,
            "Saturno es el sexto planeta del sistema solar contando desde el Sol, el segundo en tamaño y masa después de Júpiter y el único con un sistema de anillos visible desde la Tierra. Su nombre proviene del dios romano Saturno. Forma parte de los denominados planetas exteriores o gaseosos. El aspecto más característico de Saturno son sus brillantes y grandes anillos. Antes de la invención del telescopio, Saturno era el más lejano de los planetas conocidos y, a simple vista, no parecía luminoso ni interesante.\n\n"
                + "El primero en observar los anillos fue Galileo en 1610, pero la baja inclinación de los anillos y la baja resolución de su telescopio le hicieron pensar en un principio que se trataba de grandes satélites. Christiaan Huygens, con mejores medios de observación, pudo en 1659 observar con claridad los anillos. James Clerk Maxwell, en 1859, demostró matemáticamente que los anillos no podían ser un único objeto sólido sino que debían ser la agrupación de millones de partículas de menor tamaño. Las partículas que componen los anillos de Saturno giran a una velocidad de 48 000 km/h, 15 veces más rápido que una bala.",
            "images/saturn.png"
        );

        photo = photoToBlob("static/images/saturn.png");
        planet6.setPhoto(photo);

        Planet planet7 = new Planet(
            "Urano",
            11,
            600,
            0.0012,
            "Urano es el séptimo planeta del sistema solar, el tercero de mayor tamaño, y el cuarto más masivo. Se llama así en honor de la divinidad griega del cielo Urano (del griego antiguo Οὐρανός), el padre de Crono (Saturno) y el abuelo de Zeus (Júpiter). Aunque es detectable a simple vista en el cielo nocturno, no fue catalogado como planeta por los astrónomos de la antigüedad debido a su escasa luminosidad y a la lentitud de su órbita. William Herschel anunció su descubrimiento el 13 de marzo de 1781, ampliando las fronteras entonces conocidas del sistema solar, por primera vez en la historia moderna. Urano es también el primer planeta descubierto por medio de un telescopio.\n\n"
                + "Urano es similar en composición a Neptuno, y los dos tienen una composición diferente de los otros dos gigantes gaseosos (Júpiter y Saturno). Por ello, los astrónomos a veces los clasifican en una categoría diferente, los gigantes helados. La atmósfera de Urano, aunque es similar a la de Júpiter y Saturno por estar compuesta principalmente de hidrógeno y helio, contiene una proporción superior tanto de 'hielo' como de agua, amoníaco y metano, junto con trazas de hidrocarburos. Posee la atmósfera planetaria más fría del sistema solar, con una temperatura mínima de 49 K (-224 °C). Asimismo, tiene una estructura de nubes muy compleja, acomodada por niveles, donde se cree que las nubes más bajas están compuestas de agua y las más altas de metano. En contraste, el interior de Urano se encuentra compuesto principalmente de hielo y roca.\n\n"
                + "Como los otros planetas gigantes, Urano tiene un sistema de anillos, una magnetosfera, y numerosos satélites. El sistema de Urano tiene una configuración única respecto a los otros planetas puesto que su eje de rotación está muy inclinado, casi hasta su plano de revolución alrededor del Sol. Por lo tanto, sus polos norte y sur se encuentran en donde la mayoría de los otros planetas tienen el ecuador. Vistos desde la Tierra, los anillos de Urano dan el aspecto de que rodean el planeta como una diana, y que los satélites giran a su alrededor como las agujas de un reloj, aunque en 2007 y 2008, los anillos aparecían justo de lado. El 24 de enero de 1986, las imágenes del Voyager 2 mostraron a Urano como un planeta sin ninguna característica especial de luz visible e incluso sin bandas de nubes o tormentas asociadas con los otros gigantes. Sin embargo, los observadores terrestres han visto señales de cambios de estación y un aumento de la actividad meteorológica en los últimos años a medida que Urano se acerca a su equinoccio. Las velocidades del viento en Urano pueden llegar o incluso sobrepasar los 250 m/s (900 km/h).",
            "images/uranus.png"
        );

        photo = photoToBlob("static/images/uranus.png");
        planet7.setPhoto(photo);

        Planet planet8 = new Planet(
            "Neptuno",
            10,
            700,
            0.001,
            "Neptuno es el octavo planeta en distancia respecto al Sol y el más lejano del sistema solar. Forma parte de los denominados planetas exteriores, y dentro de estos, es uno de los gigantes helados, y es el primero que fue descubierto gracias a predicciones matemáticas. Su nombre fue puesto en honor al dios romano del mar —Neptuno—, y es el cuarto planeta en diámetro y el tercero más grande en masa. Su masa es diecisiete veces la de la Tierra y ligeramente mayor que la de su planeta 'gemelo' Urano, que tiene quince masas terrestres y no es tan denso. En promedio, Neptuno orbita el Sol a una distancia de 30,1 ua. Su símbolo astronómico es ♆, una versión estilizada del tridente del dios Neptuno.\n\n"
                + "Tras el descubrimiento de Urano, se observó que las órbitas de Urano, Saturno y Júpiter no se comportaban tal como predecían las leyes de Kepler y de Newton. Adams y Le Verrier, de forma independiente, calcularon la posición de un hipotético planeta, Neptuno, que finalmente fue encontrado por Galle, el 23 de septiembre de 1846, a menos de un grado de la posición calculada por Le Verrier. Más tarde se advirtió que Galileo ya había observado Neptuno en 1612, pero lo había confundido con una estrella.\n\n"
                + "Neptuno es un planeta dinámico, con manchas que recuerdan las tempestades de Júpiter. La más grande, la Gran Mancha Oscura, tenía un tamaño similar al de la Tierra, pero en 1994 desapareció y se ha formado otra. Los vientos más fuertes de cualquier planeta del sistema solar se encuentran en Neptuno.\n\n"
                + "Neptuno tiene una composición bastante similar a la del planeta Urano, y ambos tienen composiciones que difieren mucho de los demás gigantes gaseosos, Júpiter y Saturno. La atmósfera de Neptuno, como las de Júpiter y de Saturno, se compone principalmente de hidrógeno y helio, junto con vestigios de hidrocarburos y posiblemente nitrógeno. Contiene una mayor proporción de hielos, tales como agua (H2O), amoníaco (NH3) y metano (CH4). Los científicos muchas veces categorizan Urano y Neptuno como gigantes helados para enfatizar la distinción entre estos y los gigantes de gas Júpiter y Saturno. El interior de Neptuno, como el de Urano, está compuesto principalmente de hielos y roca. Los rastros de metano en las regiones periféricas exteriores contribuyen para el aspecto azul vívido de este planeta.",
            "images/neptune.png"
        );

        photo = photoToBlob("static/images/neptune.png");
        planet8.setPhoto(photo);

        Planet planet9 = new Planet(
            "Sol",
            50,
            0,
            0,
            "El Sol (del latín sol, solis, ‘dios Sol invictus’ o ‘sol’, a su vez de la raíz protoindoeuropea sauel, ‘luz’) es una estrella de tipo-G de la secuencia principal y clase de luminosidad V que se encuentra en el centro del sistema solar y constituye la mayor fuente de radiación electromagnética de este sistema planetario. Es una esfera casi perfecta de plasma, con un movimiento convectivo interno que genera un campo magnético a través de un proceso de dinamo. Cerca de tres cuartas partes de la masa del Sol constan de gases como el hidrógeno; el resto es principalmente helio, con cantidades mucho más pequeñas de elementos, incluyendo el oxígeno, carbono, neón y hierro.\n\n"
    + "Se formó hace aproximadamente 4600 millones de años a partir del colapso gravitacional de la materia dentro de una región de una gran nube molecular. La mayor parte de esta materia se acumuló en el centro, mientras que el resto se aplanó en un disco en órbita que se convirtió en el sistema solar. La masa central se volvió cada vez más densa y caliente, dando lugar con el tiempo al inicio de la fusión nuclear en su núcleo. Se cree que casi todas las estrellas se forman por este proceso. El Sol es más o menos de edad intermedia y no ha cambiado drásticamente desde hace más de cuatro mil millones de años, y seguirá siendo bastante estable durante otros 5000 millones de años más. Sin embargo, después de que la fusión del hidrógeno en su núcleo se haya detenido, el Sol sufrirá cambios importantes y se convertirá en una gigante roja. Se estima que el Sol se volverá entonces lo suficientemente grande como para engullir las órbitas actuales de Mercurio, Venus y posiblemente la Tierra.\n\n"
    + "La Tierra y otros cuerpos (incluidos otros planetas, asteroides, meteoroides, cometas y polvo) orbitan alrededor del Sol. Por sí solo, representa alrededor del 99,86 % de la masa del sistema solar. La distancia media del Sol a la Tierra fue definida exactamente por la Unión Astronómica Internacional en 149 597 870 700 metros (aproximadamente 150 millones de kilómetros). Su luz recorre esta distancia en 8 minutos y 20 segundos.\n\n"
    + "La energía del Sol, en forma de luz solar, sustenta a casi todas las formas de vida en la Tierra a través de la fotosíntesis, y determina el clima de la Tierra y la meteorología.\n\n"
    + "Es la estrella del sistema planetario en el que se encuentra la Tierra; por lo tanto, es el astro con mayor brillo aparente. Su visibilidad en el cielo local determina, respectivamente, el día y la noche en diferentes regiones de diferentes planetas. En la Tierra, la energía radiada por el Sol es aprovechada por los seres fotosintéticos que constituyen la base de la cadena trófica, siendo así la principal fuente de energía de la vida. También aporta la energía que mantiene en funcionamiento los procesos climáticos.\n\n"
    + "El Sol es una estrella que se encuentra en la fase denominada secuencia principal, con un tipo espectral G2 y clase de luminosidad V, por tanto, también es denominada como enana amarilla. Se formó hace entre 4567,9 y 4570,1 millones de años y permanecerá en la secuencia principal aproximadamente 5000 millones de años más. El Sol, junto con todos los cuerpos celestes que orbitan a su alrededor, incluida la Tierra, forman el sistema solar.\n\n"
    + "A pesar de ser una estrella enana, es la única cuya forma se puede apreciar a simple vista, con un diámetro angular de 32′35″ de arco en el perihelio y 31′31″ en el afelio, lo que da un diámetro medio de 32′03″. La combinación de tamaños y distancias del Sol y la Luna son tales que se ven, aproximadamente, con el mismo tamaño aparente en el cielo. Esto permite una amplia gama de eclipses solares distintos (totales, anulares o parciales).\n\n"
    + "El vasto efecto del Sol sobre la Tierra ha sido reconocido desde tiempos prehistóricos y el astro ha sido considerado por algunas culturas como una deidad. El movimiento de la Tierra alrededor del Sol es la base del calendario solar, el cual es el calendario predominante en uso hoy en día.\n\n"
    + "La disciplina científica que se encarga del estudio del Sol en su totalidad es la física solar.",
            "images/sun.png"
        );

        photo = photoToBlob("static/images/sun.png");
        planet9.setPhoto(photo);

        Planet planet10 = new Planet(
            "Luna",
            3,
            30,
            0.04,
            "La Luna es el único satélite natural de la Tierra. Con un diámetro ecuatorial de 3474,8 km, es el quinto satélite más grande del sistema solar, mientras que en cuanto al tamaño proporcional respecto a su planeta es el satélite más grande: un cuarto del diámetro de la Tierra y 1/81 de su masa. Es, además, después de Ío, el segundo satélite más denso. Se encuentra en relación síncrona con la Tierra, siempre mostrando la misma cara hacia el planeta. El hemisferio visible está marcado con oscuros mares lunares de origen volcánico entre las brillantes montañas antiguas y los destacados astroblemas.\n\n"
    + "A pesar de ser, en apariencia, el objeto más brillante en el cielo después del Sol, su superficie es en realidad muy oscura, con una reflexión similar a la del carbón. Su prominencia en el cielo y su ciclo regular de fases han hecho de la Luna un objeto con importante influencia cultural desde la antigüedad, tanto en el lenguaje como en el calendario, el arte o la mitología. La influencia gravitatoria de la Luna produce las mareas y el aumento de la duración del día. La distancia orbital de la Luna, cerca de treinta veces el diámetro de la Tierra, hace que se vea en el cielo con el mismo tamaño que el Sol y permite que la Luna cubra exactamente al Sol en los eclipses solares totales.\n\n"
    + "La Luna es el único cuerpo celeste en el que el ser humano ha realizado un descenso tripulado. Aunque el programa Luna de la Unión Soviética fue el primero en alcanzar la Luna con una nave espacial no tripulada, el programa Apolo de Estados Unidos realizó las únicas misiones tripuladas al satélite terrestre hasta la fecha, comenzando con la primera órbita lunar tripulada por el Apolo 8 en 1968, y seis alunizajes tripulados entre 1969 y 1972, siendo el primero el Apolo 11 en 1969, y el último el Apolo 17. Estas misiones regresaron con más de 380 kg de roca lunar, que han permitido alcanzar una detallada comprensión geológica de los orígenes de la Luna –se cree que se formó hace 4 500 000 000 (cuatro mil quinientos millones) de años después de un gran impacto–, la formación de su estructura interna y su posterior historia.\n\n"
    + "En 1970 la Unión Soviética puso en la superficie el primer vehículo robótico controlado desde la Tierra: Lunojod 1. El rover fue enviando fotografías y vídeos de la superficie que recorrió (10 km.) durante casi un año.\n\n"
    + "Desde la misión Apolo 17, en 1972, ha sido visitada únicamente por sondas espaciales no tripuladas, en particular por el astromóvil soviético Lunojod 2. Desde 2004, Japón, China, India, Estados Unidos y la Agencia Espacial Europea han enviado orbitadores. Estas naves espaciales han confirmado el descubrimiento de agua helada fijada al regolito lunar en cráteres que se encuentran en la zona de sombra permanente y están ubicados en los polos. Se han planeado futuras misiones tripuladas a la Luna, pero no se han puesto en marcha aún.\n\n"
    + "La Luna se mantiene, bajo el Tratado sobre el espacio ultraterrestre, libre para la exploración de cualquier nación con fines pacíficos.",
            "images/moon.png"
        );

        photo = photoToBlob("static/images/moon.png");
        planet10.setPhoto(photo);

        planetRepository.save(planet1);
        planetRepository.save(planet2);
        planetRepository.save(planet3);
        planetRepository.save(planet4);
        planetRepository.save(planet5);
        planetRepository.save(planet6);
        planetRepository.save(planet7);
        planetRepository.save(planet8);
        planetRepository.save(planet9);
        planetRepository.save(planet10);
    }

    public Blob photoToBlob(String photoStr) {
        ClassPathResource image = new ClassPathResource(photoStr);
        try {
            Blob photoBlob = BlobProxy.generateProxy(image.getInputStream(), image.contentLength());
            return photoBlob;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

}
