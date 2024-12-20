package es.codeurjc.cosmic_news.restcontroller;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.codeurjc.cosmic_news.DTO.NewsDTO;
import es.codeurjc.cosmic_news.DTO.PictureDTO;
import es.codeurjc.cosmic_news.DTO.UserDTO;
import es.codeurjc.cosmic_news.model.Badge;
import es.codeurjc.cosmic_news.model.Event;
import es.codeurjc.cosmic_news.model.News;
import es.codeurjc.cosmic_news.model.Picture;
import es.codeurjc.cosmic_news.model.User;
import es.codeurjc.cosmic_news.service.NewsService;
import es.codeurjc.cosmic_news.service.PictureService;
import es.codeurjc.cosmic_news.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private UserService userService;

	@Autowired
    private NewsService newsService;

	@Autowired
    private PictureService pictureService;

	@Operation(summary = "Get a list of email addresses")
	@ApiResponses(value = {
    @ApiResponse(
        responseCode = "200",
        description = "List of email addresses retrieved successfully",
        content = @Content(mediaType = "application/json")
    )
	})
	@GetMapping("/mails")
	public ResponseEntity<List<String>> getMails() {
		List<String> mails = new ArrayList<String>();
		long i = 1;
		boolean done = false;
		while (!done){
			User user = userService.findUserById(i);
			if (user != null) {
				mails.add(user.getMail());
				i = i+1;
			}else{
				done = true;
			}
		}
		return ResponseEntity.status(HttpStatus.OK).body(mails);
	}

	@Operation(summary = "Get a list of nicks")
	@ApiResponses(value = {
    @ApiResponse(
        responseCode = "200",
        description = "List of nicks retrieved successfully",
        content = @Content(mediaType = "application/json")
    )
	})
	@GetMapping("/nicks")
	public ResponseEntity<List<String>> getNicks() {
		List<String> nicks = new ArrayList<String>();
		long i = 1;
		boolean done = false;
		while (!done){
			User user = userService.findUserById(i);
			if (user != null) {
				nicks.add(user.getNick());
				i = i+1;
			}else{
				done = true;
			}
		}
		return ResponseEntity.status(HttpStatus.OK).body(nicks);
	}
    
    @Operation(summary = "Get a user by its id")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Found the user",
	 content = {@Content(
	 mediaType = "application/json",
	 schema = @Schema(implementation=User.class)
	 )}
	 ),
	 @ApiResponse(
	 responseCode = "404",
	 description = "User not found, probably invalid id supplied",
	 content = @Content
	 )
	})
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable long id){ 
        User user = userService.findUserById(id);
        if (user != null) return ResponseEntity.status(HttpStatus.OK).body(user);
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary = "Delete a user by its id. You need to be an administrator")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "User deleted",
	 content = @Content
	 ),
	 @ApiResponse(
	 responseCode = "401",
	 description = "You are not authorized",
	 content = @Content
	 ),
	 @ApiResponse(
	 responseCode = "404",
	 description = "User not found, probably invalid id supplied",
	 content = @Content
	 )
	})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id, Principal principal){ //Only for admin and owner
        if(principal !=null){
			User user = userService.findUserById(id);
			if (user != null){
				userService.deleteUser(user.getMail());
				return ResponseEntity.status(HttpStatus.OK).build();
			}else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Operation(summary = "Post a new user")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "User posted correctly",
	 content = {@Content(
		mediaType = "application/json",
		schema = @Schema(implementation=UserDTO.class)
		)}
	 ),
	 @ApiResponse(
	 responseCode = "400",
	 description = "Data entered incorrectly.",
	 content = @Content
	 )
	})
    @PostMapping()
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
		String messageForm = userService.checkForm(userDTO.getMail(), userDTO.getNick());
		if (messageForm.equals("")) {
            User user = userDTO.toUser();
			userService.saveUser(user);
			return new ResponseEntity<>(userDTO, HttpStatus.OK);
		}else{
			HttpHeaders headers = new HttpHeaders();
			headers.add("Error-Message", messageForm);
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).build();
		}
	}

    @Operation(summary = "Update a user fields by ID. You need to be an administrator or the own user.")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "User updated correctly. You can update only the fields you want",
	 content = {@Content(
		mediaType = "application/json",
		schema = @Schema(implementation=UserDTO.class)
		)}
	 ),
	 @ApiResponse(
	 responseCode = "400",
	 description = "Data entered incorrectly.",
	 content = @Content
	 ),	
	 @ApiResponse(
	 responseCode = "401",
	 description = "You are not authorized",
	 content = @Content
	 ),
	 @ApiResponse(
	 responseCode = "404",
	 description = "User not found, probably invalid id supplied",
	 content = @Content
	 )
	})
    @PutMapping("/{id}")
	public ResponseEntity<User> updateUser(@PathVariable long id, @RequestBody UserDTO userDTO, Principal principal) throws SQLException {
		if(principal !=null){
            User user = userService.findUserById(id);
			if (user != null) {
				String messageForm = userService.checkForm(userDTO.getMail(), userDTO.getNick());
				if (messageForm.equals("") || (userDTO.getMail().equals(user.getMail()) && userDTO.getNick().equals(user.getNick()))) {
					updateUser(user, userDTO);
					return new ResponseEntity<>(user, HttpStatus.OK);
				}else{
					HttpHeaders headers = new HttpHeaders();
					headers.add("Error-Message", messageForm);
        			return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).build();
				}
			} else	{
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		}else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

    @Operation(summary = "Post a new photo of a user by id")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "201",
	 description = "Photo posted correctly",
	 content = @Content
	 ),
	 @ApiResponse(
	 responseCode = "404",
	 description = "Data entered incorrectly, probably invalid id supplied",
	 content = @Content
	 ),	 
	 @ApiResponse(
	 responseCode = "401",
	 description = "You are not authorized",
	 content = @Content
	 )
	})
    @PostMapping("/{id}/image")
	public ResponseEntity<Object> uploadPhotoUser(@PathVariable long id, @RequestParam MultipartFile imageFile, Principal principal)
			throws IOException {
		if(principal !=null){
            User user = userService.findUserById(id);
			if (user != null) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();

				user.setImage(true);
				user.setPhoto(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
				userService.saveUserRest(user);

				return ResponseEntity.created(location).build();
			}else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}
 
	@Operation(summary = "Get the photo of a user by id")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Found the photo",
	 content = @Content
	 ),
	 @ApiResponse(
	 responseCode = "400",
	 description = "Data entered incorrectly, probably invalid id supplied",
	 content = @Content
	 ),	 
	 @ApiResponse(
	 responseCode = "404",
	 description = "That user does not have a photo",
	 content = @Content
	 )
	})
	@GetMapping("/{id}/image")
	public ResponseEntity<Object> downloadPhoto(@PathVariable long id) throws SQLException {
        User user = userService.findUserById(id);
		if (user != null) {
		
			if (user.getPhoto() != null) {

				org.springframework.core.io.Resource file = new InputStreamResource(user.getPhoto().getBinaryStream());

				return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
						.contentLength(user.getPhoto().length()).body(file);

			} else {
				return ResponseEntity.notFound().build();
			}
		}else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@Operation(summary = "Delete the photo of a user by id. You need to be an admin or the own user")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Photo deleted correctly",
	 content = @Content
	 ),
	 @ApiResponse(
	 responseCode = "404",
	 description = "Data entered incorrectly, probably invalid id supplied",
	 content = @Content
	 ),	 
	 @ApiResponse(
	 responseCode = "401",
	 description = "You are not authorized",
	 content = @Content
	 )
	})
	@DeleteMapping("/{id}/image")
	public ResponseEntity<Object> deletePhoto(@PathVariable long id, Principal principal) throws IOException {
		if(principal !=null){
            User user = userService.findUserById(id);
			if (user != null) {
				user.setPhoto(null);
				user.setImage(false);

				userService.saveUserRest(user);

				return ResponseEntity.noContent().build();
			}else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@Operation(summary = "Get paged news of a user.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "News found", content = {
        @Content(mediaType = "application/json", schema = @Schema(implementation = NewsDTO.class)) }),
        @ApiResponse(responseCode = "204", description = "News not found, probably high page number supplied", content = @Content),
		@ApiResponse(responseCode = "400", description = "Data entered incorrectly, probably invalid id supplied", content = @Content
	 )
    })
    @GetMapping("/{id}/news")
    public ResponseEntity<List<NewsDTO>> getNews(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "5") int size, @PathVariable long id) {

		User user = userService.findUserById(id);
		if (user != null){

			Page<News> newsList = newsService.findAllByUserId(id,page, size);
			List<NewsDTO> newsDTO = new ArrayList<>();

			for (News news : newsList) {
				newsDTO.add(new NewsDTO(news));
			}

			if (newsDTO.isEmpty())
				return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

			return ResponseEntity.status(HttpStatus.OK).body(newsDTO);
		} else{
			return ResponseEntity.notFound().build();
		}
    }
	
	@Operation(summary = "Get paged pictures of a user.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pictures found", content = {
        @Content(mediaType = "application/json", schema = @Schema(implementation = PictureDTO.class)) }),
        @ApiResponse(responseCode = "204", description = "Pictures not found, probably high page number supplied", content = @Content),
		@ApiResponse(responseCode = "400", description = "Data entered incorrectly, probably invalid id supplied", content = @Content)
    })
    @GetMapping("/{id}/pictures")
    public ResponseEntity<List<PictureDTO>> getPictures(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "5") int size, @PathVariable long id) {

		User user = userService.findUserById(id);
		if (user != null){
			Page<Picture> pictures = pictureService.findAllByUserId(id,page, size);
			List<PictureDTO> picturesDTO = new ArrayList<>();

			for (Picture picture : pictures) {
				picturesDTO.add(new PictureDTO(picture));
			}

			if (picturesDTO.isEmpty())
				return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        	return ResponseEntity.status(HttpStatus.OK).body(picturesDTO);
		}else{
			return ResponseEntity.notFound().build();
		}
    }

	@Operation(summary = "Get events of a user.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Events found", content = {
        @Content(mediaType = "application/json", schema = @Schema(implementation = Event.class)) }),
		@ApiResponse(responseCode = "400", description = "Data entered incorrectly, probably invalid id supplied", content = @Content)
    })
    @GetMapping("/{id}/events")
	public ResponseEntity<Collection<Event>> getUserEvents(@PathVariable long id) {
		User user = userService.findUserById(id);
		if (user != null){
			return ResponseEntity.status(HttpStatus.OK).body(user.getEvents());
		}else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@Operation(summary = "Get badges of a user.")
    @ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Badges found", content = {
		@Content(mediaType = "application/json", schema = @Schema(implementation = Badge.class)) }),
		@ApiResponse(responseCode = "400", description = "Data entered incorrectly, probably invalid id supplied", content = @Content)
    })
    @GetMapping("/{id}/badges")
	public ResponseEntity<Collection<Badge>> getUserBadges(@PathVariable long id) {
		User user = userService.findUserById(id);
		if (user != null){
			return ResponseEntity.status(HttpStatus.OK).body(user.getBadges());
		}else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@Operation(summary = "Get the photo of a badge of a user by id")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Found the photo",
	 content = @Content
	 ),
	 @ApiResponse(
	 responseCode = "400",
	 description = "Data entered incorrectly, probably invalid id supplied",
	 content = @Content
	 ),	 
	 @ApiResponse(
	 responseCode = "404",
	 description = "That badge does not have a photo",
	 content = @Content
	 )
	})
	@GetMapping("/{id}/badges/{position}")
	public ResponseEntity<Object> downloadPhotoBadge(@PathVariable long id, @PathVariable int position) throws SQLException {
        User user = userService.findUserById(id);
		if (user != null) {
			Badge badge = user.getBadges().get(position);
			if (badge != null && badge.getImage() != null) {

				org.springframework.core.io.Resource file = new InputStreamResource(badge.getImage().getBinaryStream());

				return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
						.contentLength(badge.getImage().length()).body(file);

			} else {
				return ResponseEntity.notFound().build();
			}
		}else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

    public void updateUser(User user, UserDTO newUser){
		if (newUser.getName()!=null) user.setName(newUser.getName());
        if (newUser.getNick()!=null) user.setNick(newUser.getNick());
        if (newUser.getDescription()!=null) user.setDescription(newUser.getDescription());
		if (newUser.getSurname()!=null) user.setSurname(newUser.getSurname());
		userService.saveUserRest(user);
	}

}
