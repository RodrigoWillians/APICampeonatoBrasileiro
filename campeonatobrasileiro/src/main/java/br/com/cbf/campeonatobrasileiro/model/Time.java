package br.com.cbf.campeonatobrasileiro.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;


@Entity
@Data
public class Time {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 20)
    private String nome;
    @Column(length = 43)
    private String sigla;
    @Column(length = 22)
    private String uf;
    private String estadio;
    
}
