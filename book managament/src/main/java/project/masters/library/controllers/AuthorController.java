package project.masters.library.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import project.masters.library.Helpers;
import project.masters.library.entities.AuthorEntity;
import project.masters.library.repositories.AuthorRepository;

@RestController
@CrossOrigin(origins="*")
public class AuthorController {
	
	AuthorRepository authorRepo;
	
	public AuthorController(AuthorRepository authorRepo) {
		this.authorRepo = authorRepo;
	}
	
	@GetMapping("/author/all")
	public List<AuthorEntity> GetAll() {
		return authorRepo.findAll();
	}
	
	@GetMapping("/author")
	public ResponseEntity<?> GetById(
			@RequestParam(value = "id") int id) {
		 if(authorRepo.existsById(id)) {
			 return ResponseEntity.
					 ok(authorRepo.findById(id).get());
		 }
		 else {
			 return ResponseEntity
					 .notFound().
					 build();
		 }
	}
	
	@PostMapping(path = "/author")
	public ResponseEntity<?> Createauthor(
			@RequestBody AuthorEntity author	
			) {
			
			AuthorEntity newauthor = new AuthorEntity();
			//TODO - AuthorEntity doesn't have description property
			newauthor.setName(author.getName());
			newauthor.setLname(author.getLname());
			
			try {
				newauthor = authorRepo.saveAndFlush(newauthor);	
				return ResponseEntity.ok(newauthor);
			}
			catch(Exception ex) {
				return Helpers.ServerError(ex);
			}
	}
	
	@PutMapping(path = "/author")
	public ResponseEntity<?> Updateauthor(
			@RequestBody AuthorEntity author	
			) {
						
			if(authorRepo.existsById(author.getId())) {
				AuthorEntity authorEntity = authorRepo.findById(author.getId()).get();
				
				authorEntity.setName(author.getName());
     			authorEntity.setLname(author.getLname());
				
				try {
					authorRepo.saveAndFlush(authorEntity);	
					return ResponseEntity.
							ok(authorEntity);
				}
				catch(Exception ex) {
					return Helpers.ServerError(ex);
				}
			 }
			else {
				return ResponseEntity.
						notFound()
						.build();
			}			
	}
	
	@DeleteMapping("/author")
	public ResponseEntity<?> DeleteById(
			@RequestParam(value = "id") int id) {
		 if(authorRepo.existsById(id)) {
			 try {
			 authorRepo.deleteById(id);
			 }
			 catch(Exception ex) {
					return Helpers.ServerError(ex);
			 }
			 return ResponseEntity.ok().build();
		 }
		 else {
			 return ResponseEntity
					 .notFound().
					 build();
		 }
	}

}
