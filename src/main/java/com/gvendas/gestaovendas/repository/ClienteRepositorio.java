package com.gvendas.gestaovendas.repository;

import com.gvendas.gestaovendas.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepositorio extends JpaRepository<Cliente, Long> {

    Cliente findByNome(String nome);
}
