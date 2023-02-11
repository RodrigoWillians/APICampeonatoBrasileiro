package br.com.cbf.campeonatobrasileiro.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;


@Entity
@Data
public class Jogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private Integer golsTime1;
    private Integer golsTime2;
    private Integer publicoPagante;
    private LocalDateTime data;
    private Integer rodada;
    private boolean encerrado;

    public boolean getEncerrado() {
        return encerrado;
    }

    @ManyToOne
    @JoinColumn(name="time1")
    private Time time1;

    @ManyToOne
    @JoinColumn(name="time2")
    private Time time2;

    
}
