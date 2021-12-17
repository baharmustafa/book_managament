package project.masters.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.masters.library.entities.GenreEntity;

@Repository
public interface GenreRepository extends JpaRepository<GenreEntity, Integer> {

}
