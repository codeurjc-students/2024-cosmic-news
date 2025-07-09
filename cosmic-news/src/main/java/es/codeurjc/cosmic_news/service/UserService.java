package es.codeurjc.cosmic_news.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.codeurjc.cosmic_news.model.Event;
import es.codeurjc.cosmic_news.model.News;
import es.codeurjc.cosmic_news.model.Picture;
import es.codeurjc.cosmic_news.model.User;
import es.codeurjc.cosmic_news.repository.EventRepository;
import es.codeurjc.cosmic_news.repository.NewsRepository;
import es.codeurjc.cosmic_news.repository.PictureRepository;
import es.codeurjc.cosmic_news.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private NewsRepository newsRepository;

   @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User saveUser(User user){
        if (userRepository.findByMail(user.getMail()).isPresent()){
            return null;
        } else{
            user.setPass(passwordEncoder.encode(user.getPass()));
            user.setRoles("USER");
            userRepository.save(user);
            return user;
        }
    }

    public void saveUserRest(User user){
        userRepository.save(user);
    }

    public boolean deleteUser(String mail){
        Optional<User> userOp = userRepository.findByMail(mail);

        if (userOp.isPresent()){
            User user = userOp.get();
            for (Event event: user.getEvents()){
                user.removeEvent(event);
                eventRepository.save(event);
            }
            for (Picture picture: user.getPictures()){
                user.removePicture(picture);
                picture.setLikes(picture.getLikes()-1);
                pictureRepository.save(picture);
            }
            for (News news: user.getNews()){
                user.removeNews(news);
                news.setLikes(news.getLikes()-1);
                newsRepository.save(news);
            }
            userRepository.deleteById(user.getId());

            return true;
        }else {
            return false;
        }
    }

    public void updateUser(User user, HttpServletRequest request) {
        user.setName(request.getParameter("name"));
        user.setSurname(request.getParameter("surname"));
        user.setDescription(request.getParameter("description"));
        user.setNick(request.getParameter("nick"));
        userRepository.save(user);
    }

    public void addEvent(User user, Event event){
        user.addEvent(event);
        userRepository.save(user);
    }

    public User findUserByMail(String mail){
        Optional<User> user = userRepository.findByMail(mail);
        if (user.isPresent()){
            return user.get();
        }else{
            return null;
        }
    }

    public User findUserByNick(String nick){
        Optional<User> user = userRepository.findByNick(nick);
        if (user.isPresent()){
            return user.get();
        }else{
            return null;
        }
    }

    public User findUserById(Long id){
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()){
            return user.get();
        }else{
            return null;
        }
    }
    
    public String checkForm(String mail, String nick) {
        String message1 = "";

        Optional<User> user = userRepository.findByMail(mail);
        if (user.isPresent()) {
            message1 = "Correo ya en uso por otro usuario. ";
        }

        String message2 = checkNick(nick);

        return message1 + message2;
        
    }

    public String checkNick(String nick){
        String message = "";
        Optional<User> user = userRepository.findByNick(nick);
        if (user.isPresent()) {
            message = "Nickname ya en uso por otro usuario. ";
        }
        return message;
    }

    public Optional<Event> findEventByDate(User user, LocalDate date) {
        return user.getEvents().stream()
                .filter(event -> event.getDate().equals(date))
                .findFirst();
    }

}
