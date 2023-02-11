package br.com.cbf.campeonatobrasileiro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.cbf.campeonatobrasileiro.model.Jogo;
import br.com.cbf.campeonatobrasileiro.model.Time;

@Repository
public interface JogoRepository extends JpaRepository<Jogo, Integer> {
    
    List<Jogo> findByTime1AndEncerrado(Time time1, Boolean encerrado);

    List<Jogo> findByTime2AndEncerrado(Time time1, Boolean encerrado);
    
    List<Jogo> findByEncerrado(Boolean encerrado);
    
}
