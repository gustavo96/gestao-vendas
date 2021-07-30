package com.gvendas.gestaovendas.repository;

import com.gvendas.gestaovendas.entity.Venda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VendaRepositorio extends JpaRepository<Venda, Long> {

    List<Venda> findByClienteCodigo(Long codigoCliente);

}
