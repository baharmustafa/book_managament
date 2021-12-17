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
import project.masters.library.entities.GenreEntity;
import project.masters.library.repositories.GenreRepository;



@RestController
@CrossOrigin(origins="*")
public class GenreController {

	GenreRepository genreRepo;
	
	public GenreController(GenreRepository genreRepo) {
		this.genreRepo = genreRepo;
	}
	
	@GetMapping("genre/all")
	public List<GenreEntity> GetAll() {
		return genreRepo.findAll();
	}
	
	@GetMapping("/genre")
	public ResponseEntity<?> GetById(
			@RequestParam(value = "id") int id) {
		 if(genreRepo.existsById(id)) {
			 return ResponseEntity.
					 ok(genreRepo.findById(id).get());
		 }
		 else {
			 return ResponseEntity
					 .notFound().
					 build();
		 }
	}
	
	@PostMapping(path = "/genre")
	public ResponseEntity<?> CreateGenre(
			@RequestBody GenreEntity genre	
			) {
			
			GenreEntity newGenre = new GenreEntity();
			
			//TODO - add getters/setters/properties
        	newGenre.setType(genre.getType()); 
        	newGenre.setDescription(genre.getDescription());
			
			try {
				newGenre = genreRepo.saveAndFlush(newGenre);	
				return ResponseEntity.ok(newGenre);
			}
			catch(Exception ex) {
				return Helpers.ServerError(ex);
			}
	}
	
	@PutMapping(path = "/genre")
	public ResponseEntity<?> UpdateGenre(
			@RequestBody GenreEntity genre	
			) {
						
			if(genreRepo.existsById(genre.getId())) {
				GenreEntity genreEntity = genreRepo.findById(genre.getId()).get();
				
				//TODO
				genreEntity.setType(genre.getType());
				genreEntity.setDescription(genre.getDescription());
				
				try {
					genreRepo.saveAndFlush(genreEntity);	
					return ResponseEntity.
							ok(genreEntity);
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
	
	@DeleteMapping("/genre")
	public ResponseEntity<?> DeleteById(
			@RequestParam(value = "id") int id) {
		 if(genreRepo.existsById(id)) {
			 try {
			 genreRepo.deleteById(id);
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


