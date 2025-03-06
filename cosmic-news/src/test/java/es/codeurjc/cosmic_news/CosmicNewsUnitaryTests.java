package es.codeurjc.cosmic_news;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import es.codeurjc.cosmic_news.model.Event;
import es.codeurjc.cosmic_news.model.News;
import es.codeurjc.cosmic_news.model.Picture;
import es.codeurjc.cosmic_news.model.Question;
import es.codeurjc.cosmic_news.model.Quizz;
import es.codeurjc.cosmic_news.model.Video;
import es.codeurjc.cosmic_news.repository.EventRepository;
import es.codeurjc.cosmic_news.repository.NewsRepository;
import es.codeurjc.cosmic_news.repository.PictureRepository;
import es.codeurjc.cosmic_news.repository.QuizzRepository;
import es.codeurjc.cosmic_news.repository.VideoRepository;
import es.codeurjc.cosmic_news.service.EventService;
import es.codeurjc.cosmic_news.service.NewsService;
import es.codeurjc.cosmic_news.service.PictureService;
import es.codeurjc.cosmic_news.service.QuizzService;
import es.codeurjc.cosmic_news.service.VideoService;

public class CosmicNewsUnitaryTests {
    @Mock
    private NewsRepository newsRepository;

    @Mock
    private PictureRepository pictureRepository;

    @Mock
    private VideoRepository videoRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private QuizzRepository quizzRepository;

    @InjectMocks
    private NewsService newsService;

    @InjectMocks
    private PictureService pictureService;

    @InjectMocks
    private VideoService videoService;

    @InjectMocks
    private QuizzService quizzService;

    @InjectMocks
    private EventService eventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveNews() {
        News news = new News(
            "Noticia Test Unitario", 
            "Prueba", 
            "Admin", 
            "Soporte", 
            "Descripción por defecto",
            1, 
            LocalDate.now()
        );
        when(newsRepository.save(news)).thenReturn(news);

        News savedNews = newsService.saveNews(news);

        assertNotNull(savedNews);
        assertEquals("Noticia Test Unitario", savedNews.getTitle());
    }

    @Test
    void testDeleteNews() {
        Long newsId = 3L;
        doNothing().when(newsRepository).deleteById(newsId);

        assertDoesNotThrow(() -> newsService.deleteNews(newsId));
    }

    @Test
    void testSavePicture() {
        Picture picture = new Picture(
            "Picture Test Unitario", 
            "Admin", 
            "Admin Kingdom", 
            LocalDate.of(2023, 8, 15), 
            "Descripción por defecto."
        );
        when(pictureRepository.save(picture)).thenReturn(picture);

        Picture savedPicture = pictureService.savePicture(picture);

        assertNotNull(savedPicture);
        assertEquals("Picture Test Unitario", savedPicture.getTitle());
    }

    @Test
    void testDeletePicture() {
        Long pictureId = 3L;
        doNothing().when(pictureRepository).deleteById(pictureId);

        assertDoesNotThrow(() -> pictureService.deletePicture(pictureId));
    }

    @Test
    void testSaveVideo() {
        Video video = new Video(
            "Video Test Unitario", 
            "00:01", 
            "Descripción por defecto", 
            "TestUrl"
        );
        when(videoRepository.save(video)).thenReturn(video);

        Video savedVideo = videoService.saveVideo(video);

        assertNotNull(savedVideo);
        assertEquals("Video Test Unitario", savedVideo.getTitle());
    }

    @Test
    void testDeleteVideo() {
        Long videoId = 3L;
        doNothing().when(videoRepository).deleteById(videoId);

        assertDoesNotThrow(() -> videoService.deleteVideo(videoId));
    }

    @Test
    void testSaveEvent() {
        Event event = new Event(
            LocalDate.of(2025, 5, 20), 
            "bi bi-moon-fill", 
            "Descripción de evento de test unitario"
        );
        when(eventRepository.save(event)).thenReturn(event);

        Event savedEvent = eventService.saveEvent(event);

        assertNotNull(savedEvent);
        assertEquals("Descripción de evento de test unitario", savedEvent.getDescription());
    }

    @Test
    void testDeleteEvent() {
        Long eventId = 3L;
        doNothing().when(eventRepository).deleteById(eventId);

        assertDoesNotThrow(() -> eventService.deleteEvent(eventId));
    }

    @Test
    void testSaveQuizz() {
        Quizz quizz = new Quizz("Quizz Test Unitario", "Fácil");

        List<Question> questions = new ArrayList<>();
        questions.add(new Question(
            "Pregunta 1 Test Unitario",
            "a", "b", "c", "d",
            "a", 1, quizz
        ));

        questions.add(new Question(
            "Pregunta 2 Test Unitario",
            "e", "f Way", "g", "h",
            "e", 2, quizz
        ));

        quizz.setQuestions(questions);
        when(quizzRepository.save(quizz)).thenReturn(quizz);

        Quizz savedQuizz = quizzRepository.save(quizz);

        assertNotNull(savedQuizz);
        assertEquals("Quizz Test Unitario", savedQuizz.getName());
        assertEquals("Pregunta 2 Test Unitario", savedQuizz.getQuestions().get(1).getQuestion());
    }

    @Test
    void testDeleteQuizz() {
        Long quizzId = 3L;
        doNothing().when(quizzRepository).deleteById(quizzId);

        assertDoesNotThrow(() -> quizzService.removeQuizz(quizzId));
    }
}
