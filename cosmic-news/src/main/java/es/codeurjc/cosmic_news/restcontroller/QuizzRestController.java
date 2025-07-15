package es.codeurjc.cosmic_news.restcontroller;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.codeurjc.cosmic_news.DTO.QuestionDTO;
import es.codeurjc.cosmic_news.DTO.QuizzDTO;
import es.codeurjc.cosmic_news.model.Badge;
import es.codeurjc.cosmic_news.model.Question;
import es.codeurjc.cosmic_news.model.Quizz;
import es.codeurjc.cosmic_news.model.User;
import es.codeurjc.cosmic_news.service.QuizzService;
import es.codeurjc.cosmic_news.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/quizzes")
public class QuizzRestController {
    @Autowired
    private QuizzService quizzService;

	@Autowired
    private UserService userService;
    
     @Operation(summary = "Get paged quizzes.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Quizzes found", content = {
        @Content(mediaType = "application/json", schema = @Schema(implementation = QuizzDTO.class)) }),
        @ApiResponse(responseCode = "204", description = "Quizzes not found, probably high page number supplied", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<QuizzDTO>> getQuizzes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<Quizz> quizzes = quizzService.findAll(PageRequest.of(page, size));
        List<QuizzDTO> quizzesDTO = new ArrayList<>();

        for (Quizz quizz : quizzes) {
            quizzesDTO.add(new QuizzDTO(quizz));
        }

        if (quizzesDTO.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.status(HttpStatus.OK).body(quizzesDTO);
    }

        @Operation(summary = "Get a quizz by its id")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Found the quizz",
	 content = {@Content(
	 mediaType = "application/json",
	 schema = @Schema(implementation=Quizz.class)
	 )}
	 ),
	 @ApiResponse(
	 responseCode = "404",
	 description = "Quizz not found, probably invalid id supplied",
	 content = @Content
	 )
	})
    @GetMapping("/{id}")
    public ResponseEntity<QuizzDTO> getQuizz(@PathVariable long id){ 
        Quizz quizz = quizzService.findQuizzById(id);
        if (quizz != null) return ResponseEntity.status(HttpStatus.OK).body(new QuizzDTO(quizz));
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary = "Delete a quizz by its id. You need to be an administrator")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Quizz deleted",
	 content = @Content
	 ),
	 @ApiResponse(
	 responseCode = "401",
	 description = "You are not authorized",
	 content = @Content
	 ),
	 @ApiResponse(
	 responseCode = "404",
	 description = "Quizz not found, probably invalid id supplied",
	 content = @Content
	 )
	})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuizz(@PathVariable long id, Principal principal){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if(principal !=null && request.isUserInRole("ADMIN")){
			Quizz quizz = quizzService.findQuizzById(id);
			if (quizz != null){
				quizzService.removeQuizz(quizz.getId());
				return ResponseEntity.status(HttpStatus.OK).build();
			}else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Operation(summary = "Post a new quizz")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Quizz posted correctly",
	 content = {@Content(
		mediaType = "application/json",
		schema = @Schema(implementation=QuizzDTO.class)
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
	 )
	})
    @PostMapping()
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Quizz> createQuizz(@RequestBody QuizzDTO quizzDTO, Principal principal) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if(principal !=null && request.isUserInRole("ADMIN")){
			Quizz quizz = quizzDTO.toQuizz();
			quizzService.saveQuizzRest(quizz);
			return new ResponseEntity<>(quizz, HttpStatus.OK);
		}else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

    @Operation(summary = "Update a quizz fields by ID. You need to be an administrator.")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Quizz updated correctly. You can update only the fields you want",
	 content = {@Content(
		mediaType = "application/json",
		schema = @Schema(implementation=QuizzDTO.class)
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
	 description = "Quizz not found, probably invalid id supplied",
	 content = @Content
	 )
	})
    @PutMapping("/{id}")
	public ResponseEntity<Quizz> updateQuizz(@PathVariable long id, @RequestBody QuizzDTO quizzDTO, Principal principal) throws SQLException {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if(principal !=null && request.isUserInRole("ADMIN")){
			Quizz quizz = quizzService.findQuizzById(id);
			if (quizz != null) {
				updateQuizz(quizz, quizzDTO);
				return new ResponseEntity<>(quizz, HttpStatus.OK);
			} else	{
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		}else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

    @Operation(summary = "Post a new badge of a quizz by id")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "201",
	 description = "Badge posted correctly",
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
    @PostMapping("/{id}/badge")
	public ResponseEntity<Object> uploadBadge(@PathVariable long id, @RequestParam MultipartFile imageFile, Principal principal)
			throws IOException {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		if(principal !=null && request.isUserInRole("ADMIN")){
            Quizz quizz = quizzService.findQuizzById(id);
			if (quizz != null) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();

				quizz.setImage(true);
				quizz.setPhoto(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
				quizzService.saveQuizzRest(quizz);

				return ResponseEntity.created(location).build();
			}else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}
 
	@Operation(summary = "Get the badge of a quizz by id")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Found the badge",
	 content = @Content
	 ),
	 @ApiResponse(
	 responseCode = "400",
	 description = "Data entered incorrectly, probably invalid id supplied",
	 content = @Content
	 ),	 
	 @ApiResponse(
	 responseCode = "404",
	 description = "That quizz does not have a photo",
	 content = @Content
	 )
	})
	@GetMapping("/{id}/badge")
	public ResponseEntity<Object> downloadBadge(@PathVariable long id) throws SQLException {
        Quizz quizz = quizzService.findQuizzById(id);
		if (quizz != null) {
		
			if (quizz.getPhoto() != null) {

				org.springframework.core.io.Resource file = new InputStreamResource(quizz.getPhoto().getBinaryStream());

				return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
						.contentLength(quizz.getPhoto().length()).body(file);

			} else {
				return ResponseEntity.notFound().build();
			}
		}else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@Operation(summary = "Delete the badge of a quizz by id. You need to be an admin")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Badge deleted correctly",
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
	@DeleteMapping("/{id}/badge")
	public ResponseEntity<Object> deleteBadge(@PathVariable long id, Principal principal) throws IOException {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if(principal !=null && request.isUserInRole("ADMIN")){
            Quizz quizz = quizzService.findQuizzById(id);
			if (quizz != null) {
				quizz.setPhoto(null);
				quizz.setImage(false);

				quizzService.saveQuizzRest(quizz);

				return ResponseEntity.noContent().build();
			}else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@Operation(summary = "Submit a quizz")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Quizz submited correctly",
	 content = {@Content(
		mediaType = "application/json",
		schema = @Schema(implementation=QuizzDTO.class)
		)}
	 ),
	 @ApiResponse(
	 responseCode = "400",
	 description = "Data entered incorrectly.",
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
    @PostMapping("/{id}/submit")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<QuizzDTO> submitQuizz(@PathVariable long id,@RequestBody QuizzDTO quizzDTO, Principal principal) {
        if(principal !=null){
			Quizz quizz = quizzService.findQuizzById(id);
			if (quizz != null) {
				submitQuizz(quizz, quizzDTO);
				if (quizzDTO.getScore() == quizz.getQuestions().size()) {
                	quizzService.giveBadge(quizz, principal.getName());
            	}
				return new ResponseEntity<>(quizzDTO, HttpStatus.OK);
			}else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@GetMapping("/stats")
    public ResponseEntity<HashMap<String,Integer>> getPStats(){
        HashMap<String, Integer> map = fillQuizzes();
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

	public HashMap<String, Integer> fillQuizzes() {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        List<Quizz> quizzList = quizzService.getAllQuizzes();
        for (Quizz quizz : quizzList) {
            map.put(quizz.getName(), 0);
        }
        List<User> userList = userService.getAllUsers();
        for (User user : userList) {
            if (!user.getBadges().isEmpty()){
                for (Badge badge : user.getBadges()) {
                    map.put(badge.getName(), map.get(badge.getName()) + 1);
                }
            }
        }
        return map;
    }

    public void updateQuizz(Quizz quizz, QuizzDTO newQuizz){
		if (newQuizz.getName()!=null) quizz.setName(newQuizz.getName());
        if (newQuizz.getDifficulty()!=null) quizz.setDifficulty(newQuizz.getDifficulty());
        if (!newQuizz.getQuestions().isEmpty()){
            List<Question> questions = new ArrayList<>();
            for (QuestionDTO questionDTO: newQuizz.getQuestions()){
                Question question = questionDTO.toQuestion(quizz);
				question.setNum(questions.size()+1);
                questions.add(question);
            }
            quizz.setQuestions(questions);
        }
		quizzService.saveQuizzRest(quizz);
	}

	public void submitQuizz(Quizz quizz, QuizzDTO quizzDTO){
		List<Question> questions = quizz.getQuestions();
		int score = 0;
		int cont = 0;
		for (QuestionDTO questionDTO: quizzDTO.getQuestions()){
			questionDTO.setAnswer(questions.get(cont).getAnswer());
			if (questionDTO.getSelected().equals(questions.get(cont).getAnswer())) {
				score++;
			}
			cont++;
		}
		quizzDTO.setScore(score);
	}
}
