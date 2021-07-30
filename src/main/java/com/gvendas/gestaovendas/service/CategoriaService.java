package com.gvendas.gestaovendas.service;

import com.gvendas.gestaovendas.entity.Categoria;
import com.gvendas.gestaovendas.exception.RegraNegocioException;
import com.gvendas.gestaovendas.repository.CategoriaRepositorio;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepositorio categoriaRepositorio;

    public List<Categoria> listarTodas(){
        return categoriaRepositorio.findAll();
    }

    public Optional<Categoria> buscaPorId(Long codigo){
        return categoriaRepositorio.findById(codigo);
    }

    public Categoria salvar(Categoria categoria){
        validarCategoriaDuplicada(categoria);
        return categoriaRepositorio.save(categoria);
    }

    public Categoria atualizar(Long codigo, Categoria categoria){
        Categoria categoriaSalvar = validarCategoriaExiste(codigo);
        validarCategoriaDuplicada(categoria);
        // altera o que está no banco de dados, menos o codigo
        BeanUtils.copyProperties(categoria,categoriaSalvar, "codigo");
        return categoriaRepositorio.save(categoriaSalvar);
    }

    public void deletar (Long codigo){
        categoriaRepositorio.deleteById(codigo);
    }

    private Categoria validarCategoriaExiste(Long codigo) {
        Optional<Categoria> categoria = buscaPorId(codigo);
        if (categoria.isEmpty()){
            throw new EmptyResultDataAccessException(1);
        }
        return categoria.get();
    }

    private void validarCategoriaDuplicada(Categoria categoria){
        Categoria categoriaEncontrada = categoriaRepositorio.findByNome(categoria.getNome());
        if(categoriaEncontrada != null && categoriaEncontrada.getCodigo() != categoria.getCodigo()){
            throw new RegraNegocioException(
                    String.format("A categoria %s ja esta cadastrada",categoria.getNome()));
        }
    }

}
