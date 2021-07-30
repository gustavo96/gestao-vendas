package com.gvendas.gestaovendas.repository;

import com.gvendas.gestaovendas.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepositorio extends JpaRepository<Categoria, Long> {

    Categoria findByNome(String nome);
}
