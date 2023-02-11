package br.com.cbf.campeonatobrasileiro.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cbf.campeonatobrasileiro.dto.TimeDTO;
import br.com.cbf.campeonatobrasileiro.model.Time;
import br.com.cbf.campeonatobrasileiro.repository.JogoRepository;
import br.com.cbf.campeonatobrasileiro.repository.TimeRepository;

@Service
public class TimeService {

    @Autowired
    private TimeRepository _repository;

    
    public TimeDTO cadastrarTime(TimeDTO time) {
        Time entity = toEntity(time);
        entity = _repository.save(entity);
        return toDto(entity);

    } 

    private Time toEntity(TimeDTO time) {
        Time entity = new Time();
        entity.setId(time.getId());
        entity.setEstadio(time.getEstadio());
        entity.setSigla(time.getSigla());
        entity.setNome(time.getNome());
        entity.setUf(time.getUf());
        return entity;
    }

    public TimeDTO toDto(Time entity) {
        TimeDTO dto = new TimeDTO();
        dto.setId(entity.getId());
        dto.setEstadio(entity.getEstadio());
        dto.setSigla(entity.getSigla());
        dto.setNome(entity.getNome());
        dto.setUf(entity.getUf());
        return dto;

    }

    public List<TimeDTO> listarTimes() {
        
        return _repository.findAll().stream().map(entity -> toDto(entity)).collect(Collectors.toList());
        
    }

    public TimeDTO obterTime(Integer id) {
        return toDto(_repository.findById(id).get());
    }

    public List<Time> findAll() {
        return _repository.findAll();
    }



}
