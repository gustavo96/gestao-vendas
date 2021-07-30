package com.gvendas.gestaovendas.service;

import com.gvendas.gestaovendas.entity.Categoria;
import com.gvendas.gestaovendas.entity.Cliente;
import com.gvendas.gestaovendas.exception.RegraNegocioException;
import com.gvendas.gestaovendas.repository.ClienteRepositorio;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    public List<Cliente> listarTodos(){
        return clienteRepositorio.findAll();
    }

    public Optional<Cliente> buscaPorId(Long codigo){
        return clienteRepositorio.findById(codigo);
    }

    public Cliente salvar(Cliente cliente){
        validarClienteDuplicado(cliente);
        return clienteRepositorio.save(cliente);
    }

    public Cliente atualizar(Long codigo, Cliente cliente){
        Cliente clienteAtualizar = validarClienteExiste(codigo);
        validarClienteDuplicado(cliente);
        BeanUtils.copyProperties(cliente,clienteAtualizar,"codigo");
        return  clienteRepositorio.save(clienteAtualizar);
    }

    private Cliente validarClienteExiste(Long codigo){
        Optional<Cliente> cliente = buscaPorId(codigo);

        if(cliente.isEmpty()){
            throw new EmptyResultDataAccessException(1);
        }
        return cliente.get();
    }

    private void validarClienteDuplicado(Cliente cliente){
        Cliente clientePorNome = clienteRepositorio.findByNome(cliente.getNome());

        if(clientePorNome != null && clientePorNome.getCodigo() != cliente.getCodigo()){
            throw new RegraNegocioException(String.format("O Cliente %s ja esta cadastrado",
                    cliente.getNome()));
        }
    }

    public void deletar(Long codigo){
        clienteRepositorio.deleteById(codigo);
    }

}
