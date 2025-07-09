package es.codeurjc.cosmic_news.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import es.codeurjc.cosmic_news.model.Event;
import es.codeurjc.cosmic_news.model.User;
import es.codeurjc.cosmic_news.service.EventService;
import es.codeurjc.cosmic_news.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    EventService eventService;
    
    @GetMapping("/")
    public String getIndex(Model model, HttpServletRequest request, Pageable page) {
        model.addAttribute("notification", false);
        model.addAttribute("admin", request.isUserInRole("ADMIN"));
        if (request.getUserPrincipal() != null) {
            User user = userService.findUserByMail(request.getUserPrincipal().getName());
            if (user != null && !user.getEvents().isEmpty()){
                Optional<Event> eventOp = userService.findEventByDate(user, LocalDate.now());
                if (eventOp.isPresent()){
                    model.addAttribute("notification", true);
                    model.addAttribute("event", eventOp.get());
                }
            }
        }
        return "index";
    }

    @GetMapping("/error")
    public String getError() {
        return "error";
    }
    
    @GetMapping("/login")
    public String getLogin(HttpServletRequest request) {
        if (request.getUserPrincipal() != null) {
            String mail = request.getUserPrincipal().getName();
            return "redirect:/profile?mail="+mail;
        }
        else
            return "login";
    }

    @RequestMapping("/login/error")
    public String loginerror(Model model) {
        model.addAttribute("title", "Error");
        model.addAttribute("message", "Credenciales inválidas");
        model.addAttribute("back", "/login");

        return "message";
    }

    @GetMapping("/user/form")
    public String loadNewUser(Model model) {

        model.addAttribute("edit", false);

        return "user_form";
    }

    @PostMapping("/user/register")
	public String newUser(Model model, User user, MultipartFile photoField, HttpServletRequest request) throws IOException {

        String messageForm = userService.checkForm(request.getParameter("mail"), request.getParameter("nick"));
        if (!messageForm.equals("")){
            model.addAttribute("title", "Error");
            model.addAttribute("message", messageForm);
            model.addAttribute("back", "javascript:history.back()");
            return "message";
        }else{
            if(!photoField.isEmpty()){
                    user.setPhoto(BlobProxy.generateProxy(photoField.getInputStream(), photoField.getSize()));
                    user.setImage(true);
                }
            User createdUser = userService.saveUser(user);
            if (createdUser== null) {
                model.addAttribute("title", "Error");
                model.addAttribute("message", "Error al crear el usuario.");
                model.addAttribute("back", "javascript:history.back()");
                return "message";
            } else {
                model.addAttribute("title", "Completado");
                model.addAttribute("message", "Usuario registrado correctamente.");
                model.addAttribute("back", "/");
                return "message";
            }
        }
	}

    @GetMapping("/profile")
    public String profile(Model model, HttpServletRequest request, @RequestParam("mail") String profileMail) {

        if (request.getUserPrincipal() == null) {
            model.addAttribute("title", "Error");
            model.addAttribute("message", "Debes iniciar sesión para acceder a los perfiles");
            model.addAttribute("back", "/login");
            return "message";
        }else{
            String myMail = request.getUserPrincipal().getName();
            Boolean isMe = myMail.equals(profileMail);
            Boolean isAdmin = request.isUserInRole("ADMIN");
            model.addAttribute("me", isMe);
            model.addAttribute("iamAdmin", isAdmin);

            User user = userService.findUserByMail(profileMail);
            if (user != null){
                model.addAttribute("user", user);
            }else{
                model.addAttribute("title", "Error");
                model.addAttribute("message", "Usuario no encontrado");
                model.addAttribute("back", "/");
                return "message";
            }

            return "profile";
        }
    }

    @GetMapping("/user/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable long id) throws SQLException {
        User user = userService.findUserById(id);
        if (user != null && user.getPhoto() != null) {

            Resource file = new InputStreamResource(user.getPhoto().getBinaryStream());

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .contentLength(user.getPhoto().length()).body(file);

        } else {
            return ResponseEntity.notFound().build();
        }
    }
 
    @GetMapping("/badge/{id}/{position}/image")
    public ResponseEntity<Object> downloadBadgeImage(@PathVariable long id, @PathVariable int position) throws SQLException {
        User user = userService.findUserById(id);
        if (user != null && user.getBadges().get(position).getImage() != null) {

            Resource file = new InputStreamResource(user.getBadges().get(position).getImage().getBinaryStream());

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .contentLength(user.getBadges().get(position).getImage().length()).body(file);

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/profile/delete")
    public String deleteAccount(Model model, HttpServletRequest request, @RequestParam("mail") String mail) {

        boolean deleted = userService.deleteUser(mail);

        if(deleted){
            return "redirect:/";
        }else{
            model.addAttribute("title", "Error");
            model.addAttribute("message", "Error al eliminar al usuario");
            model.addAttribute("back", "/");
            return "message";
        }

    }

    @GetMapping("/user/{mail}/edit")
    public String showFormEdit(@PathVariable String mail, Model model, HttpServletRequest request) {
        User user = userService.findUserByMail(mail);
        if (user != null){
        model.addAttribute("user", user);
        model.addAttribute("edit", true);
        return "user_form";
        }else{
            model.addAttribute("title", "Error");
            model.addAttribute("message", "Usuario no encontrado");
            model.addAttribute("back", "/");
            return "message";
        }
    }

    @PostMapping("/user/{mail}/edit")
    public String editUser(@PathVariable String mail, HttpServletRequest request, Model model, @RequestParam("photoField") MultipartFile photoField) throws IOException {

        User user = userService.findUserByMail(mail);
        if (user != null){
            String messageForm = userService.checkNick(request.getParameter("nick"));
            if ((!messageForm.equals("")) && (!user.getNick().equals(request.getParameter("nick")))){
                model.addAttribute("title", "Error");
                model.addAttribute("message", messageForm);
                model.addAttribute("back", "javascript:history.back()");
                return "message";
            }else{
                if (!photoField.isEmpty()){
                    user.setPhoto(BlobProxy.generateProxy(photoField.getInputStream(), photoField.getSize()));
                }
                userService.updateUser(user,request);
                return "redirect:/profile?&mail="+mail;
            }
        }else{
            model.addAttribute("title", "Error");
            model.addAttribute("message", "Usuario no encontrado");
            model.addAttribute("back", "javascript:history.back()");
            return "message";
        }

    }

    @PostMapping("/notify")
    public String notifyEvent(Model model, @RequestParam("eventId3") Long id, HttpServletRequest request) {
        if (request.getUserPrincipal() != null) {
            User user = userService.findUserByMail(request.getUserPrincipal().getName());
            if (user != null){
                Event event = eventService.findEventById(id);
                if (event != null){
                    userService.addEvent(user,event);
                    model.addAttribute("title", "Hecho");
                    model.addAttribute("message", "¡Recibirás una notificación cuando se produzca el evento!");
                    model.addAttribute("back", "javascript:history.back()");
                    return "message";
                }else{
                    model.addAttribute("title", "Error");
                    model.addAttribute("message", "Evento no encontrado");
                    model.addAttribute("back", "javascript:history.back()");
                    return "message";
                }
            }else{
                model.addAttribute("title", "Error");
                model.addAttribute("message", "Usuario no encontrado");
                model.addAttribute("back", "javascript:history.back()");
                return "message";
            }
        }else{
            model.addAttribute("title", "Error");
            model.addAttribute("message", "Inicia sesión para poder recibir notificaciones de los próximos eventos astronómicos");
            model.addAttribute("back", "javascript:history.back()");
            return "message";
        }
        
    }
    

    @GetMapping("/availableMail")
    public ResponseEntity<?> checkMailAvailability(@RequestParam String mail) {
        boolean available = true;
        User user = userService.findUserByMail(mail);
        if (user != null){
            available = false;
        }
        return ResponseEntity.ok().body(Map.of("available", available));
    }

    @GetMapping("/availableNick")
    public ResponseEntity<?> checkNickAvailability(@RequestParam String nick) {
        boolean available = true;
        User user = userService.findUserByNick(nick);
        if (user != null){
            available = false;
        }
        return ResponseEntity.ok().body(Map.of("available", available));
    }

    @GetMapping("/space")
    public String getSpace() {
        return "space";
    }
    
    
}
