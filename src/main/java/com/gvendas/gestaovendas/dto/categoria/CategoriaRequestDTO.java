package com.gvendas.gestaovendas.dto.categoria;

import com.gvendas.gestaovendas.entity.Categoria;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

@ApiModel("Categoria requisicao DTO")
public class CategoriaRequestDTO {

    @ApiModelProperty(value = "Nome")
    @NotBlank(message = "nome")
    @Length(min = 3, max = 50, message = "nome")
    private String nome;


    public Categoria converterParaEntidade(){
    return new Categoria(nome);
    }

    public Categoria converterParaEntidade(Long codigo){
        return new Categoria(codigo,nome);
    }
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
