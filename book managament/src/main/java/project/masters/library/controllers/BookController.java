package project.masters.library.controllers;
import java.util.ArrayList;
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
import project.masters.library.entities.BookEntity;
import project.masters.library.repositories.BookRepository;
import project.masters.library.FieldFilter;

@RestController
@CrossOrigin(origins="*")
public class BookController {
	BookRepository bookRepo;
	
	public BookController(BookRepository bookRepo) {
		this.bookRepo = bookRepo;
	}
	
	@PostMapping("/book/all")
	public ResponseEntity<?> GetFiltered(
			@RequestBody List<FieldFilter> filters) {
		
		List<BookEntity> books = bookRepo.findAll();

		//no filters provided
		if(filters.isEmpty()) {
			return ResponseEntity.
					ok(books);
		}
		
		List<BookEntity> filteredbooks = new ArrayList<BookEntity>();
		
		for(BookEntity book : books) {
			boolean match = false;
			
			//TODO FieldFilter doesn't have methods like getName() and getValues
			
			for(FieldFilter filter : filters) {
				//if any filter matches, add the entity to filtered list
				switch(filter.getName()) {
					case "genre":
						if(book.getGenre().hasIdInList(filter.getValues()))
							match=true;
							break;
					case "author":
						if(book.getAuthor().hasIdInList(filter.getValues()))
							match=true;
							break;
					case "title":
						if(book.hasTitleNameInList(filter.getValues()))
							match=true;
							break; 
				}
			}
		
			if(match)
				filteredbooks.add(book);
		}
		
		return ResponseEntity.
				ok(filteredbooks);
	}
	
	@GetMapping("/book")
	public ResponseEntity<?> GetById(
			@RequestParam(value = "id") int id) {
		 if(bookRepo.existsById(id)) {
			 return ResponseEntity.
					 ok(bookRepo.findById(id).get());
		 }
		 else {
			 return ResponseEntity
					 .notFound().
					 build();
		 }
	}
	
	@PostMapping(path = "/book")
	public ResponseEntity<?> Addbook(
			@RequestBody BookEntity book	
			) {
			
			BookEntity newbook = new BookEntity();
			
			newbook.setTitle(book.getTitle());
			newbook.setGenre(book.getGenre());
			newbook.setAuthor(book.getAuthor());
			
			try {
				newbook = bookRepo.saveAndFlush(newbook);
				return ResponseEntity.ok(newbook);
			}
			catch(Exception ex) {
				return Helpers.ServerError(ex);
			}
	}
	
	@PutMapping(path = "/book")
	public ResponseEntity<?> Updatebook(
			@RequestBody BookEntity book	
			) {
						
			if(bookRepo.existsById(book.getId())) {
				BookEntity bookEntity = bookRepo.findById(book.getId()).get();
				
				bookEntity.setTitle(book.getTitle());
				bookEntity.setGenre(book.getGenre());
				bookEntity.setAuthor(book.getAuthor());
				
				try {
					bookRepo.saveAndFlush(bookEntity);	
					return ResponseEntity.
							ok(bookEntity);
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
	
	@DeleteMapping("book")
	public ResponseEntity<?> DeleteById(
			@RequestParam(value = "id") int id) {
		 if(bookRepo.existsById(id)) {
			 try {
			 bookRepo.deleteById(id);
			 }
			 catch(Exception ex) {
					return Helpers.ServerError(ex);
			 }
			 return ResponseEntity.
					 ok()
					 .build();
		 }
		 else {
			 return ResponseEntity
					 .notFound().
					 build();
		 }
	}
}


