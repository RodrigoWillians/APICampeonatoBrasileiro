package br.com.cbf.campeonatobrasileiro.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class TimeDTO implements Serializable {

    private Integer id;
    private String nome;
    private String sigla;
    private String uf;
    private String estadio;

    
}
