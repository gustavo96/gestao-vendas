package com.gvendas.gestaovendas.service;

import com.gvendas.gestaovendas.entity.Produto;
import com.gvendas.gestaovendas.exception.RegraNegocioException;
import com.gvendas.gestaovendas.repository.ProdutoRepositorio;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepositorio produtoRepositorio;
    @Autowired
    private CategoriaService categoriaService;

    public Optional<Produto> buscaPorId(Long codigo, Long codigoCategoria) {
        return produtoRepositorio.buscaPorId(codigo,codigoCategoria);
    }

    public Produto salvar(Long codigoCategoria, Produto produto) {
        validarProdutoDuplicado(produto);
        validarCategoriaProdutoExiste(codigoCategoria);
        return produtoRepositorio.save(produto);
    }

    public List<Produto> listarTodos(Long codigoCategoria) {
        return produtoRepositorio.findByCategoriaCodigo(codigoCategoria);
    }

    public Produto atualizar(Long codigoCategoria, Long codigoProduto, Produto produto) {
        Produto produtoSalvar = validarProdutoExiste(codigoProduto, codigoCategoria);
        validarProdutoDuplicado(produto);
        validarCategoriaProdutoExiste(codigoCategoria);
        BeanUtils.copyProperties(produto, produtoSalvar, "codigo");
        return produtoRepositorio.save(produto);
    }

    protected Produto validarProdutoExiste(Long codigoProduto) {
        Optional<Produto> produto = produtoRepositorio.findById(codigoProduto);
        if(produto.isEmpty()){
            throw new RegraNegocioException(String.format("Produto de codigo %s nao encontrado",codigoProduto));
        }
        return produto.get();
    }

    private Produto validarProdutoExiste(Long codigoProduto, Long codigoCategoria) {
        Optional<Produto> produto = buscaPorId(codigoProduto,codigoCategoria);
        if(produto.isEmpty()){
            throw new EmptyResultDataAccessException(1);
        }
        return produto.get();
    }

    public void deletar(Long codigoCategoria, Long codigoProduto){
        Produto produto = validarProdutoExiste(codigoProduto,codigoCategoria);
        produtoRepositorio.delete(produto);
    }

    protected void atualizarQuantidadeAposVenda(Produto produto){
        produtoRepositorio.save(produto);
    }

    private void validarCategoriaProdutoExiste(Long codigoCategoria) {
        if (codigoCategoria == null){
            throw new RegraNegocioException("A categoria não pode ser nula");
        }
        if (categoriaService.buscaPorId(codigoCategoria).isEmpty()) {
            throw new RegraNegocioException(
                    String.format("A categoria de codigo %s nao existe no cadastro",
                            codigoCategoria));
        }
    }

    private void validarProdutoDuplicado(Produto produto){
        Optional<Produto> produtoPorDescricao = produtoRepositorio.findByCategoriaCodigoAndDescricao(
                produto.getCategoria().getCodigo(),produto.getDescricao());
        if (produtoPorDescricao.isPresent() && produtoPorDescricao.get().getCodigo()!= produto.getCodigo()){
            throw new RegraNegocioException(
                    String.format("O produto %s ja esta cadastrado",produto.getDescricao()));
        }
    }
}
