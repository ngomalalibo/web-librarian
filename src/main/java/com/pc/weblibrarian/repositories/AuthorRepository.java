package com.pc.weblibrarian.repositories;

import com.pc.weblibrarian.entity.Author;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<Author, String>
{
}
