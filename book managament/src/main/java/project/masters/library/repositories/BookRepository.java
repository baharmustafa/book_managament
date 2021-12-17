package project.masters.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.masters.library.entities.BookEntity;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Integer>{

}
