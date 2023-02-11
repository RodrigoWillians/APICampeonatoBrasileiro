package br.com.cbf.campeonatobrasileiro.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class JogoDTO {
    private Integer id;
    private LocalDateTime data;
    private Integer golsTime1;
    private Integer golsTime2;
    private Integer publicoPagante;
    private boolean encerrado;
    private Integer rodada;
    private TimeDTO time1;
    private TimeDTO time2;

    
}
