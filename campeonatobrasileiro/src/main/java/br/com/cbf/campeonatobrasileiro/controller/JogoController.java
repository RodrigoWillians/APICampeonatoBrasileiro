package br.com.cbf.campeonatobrasileiro.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cbf.campeonatobrasileiro.dto.ClassificacaoDTO;
import br.com.cbf.campeonatobrasileiro.dto.JogoDTO;
import br.com.cbf.campeonatobrasileiro.dto.JogoFinalizadoDTO;
import br.com.cbf.campeonatobrasileiro.model.Jogo;
import br.com.cbf.campeonatobrasileiro.service.JogoService;

@RestController
@RequestMapping(value = "/jogos")
public class JogoController {

    @Autowired
    private JogoService _jogoService;

    @PostMapping(value = "gerar-jogos")
    public ResponseEntity<Void> gerarJogos() {
        _jogoService.gerarJogos(LocalDateTime.now());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<JogoDTO>> obterJogos() {
        return ResponseEntity.ok().body(_jogoService.listarJogos());
    }


    @PostMapping(value = "/finalizar/{id}")
    public ResponseEntity<JogoDTO> finalizar(@PathVariable Integer id, @RequestBody JogoFinalizadoDTO jogoDto) throws Exception{
        return ResponseEntity.ok().body(_jogoService.finalizar(id, jogoDto));
    }



    @GetMapping(value = "/classificacao")
    public ResponseEntity<ClassificacaoDTO> classificacao() {
        return ResponseEntity.ok().body(_jogoService.obterClassificacao());
    }


    @GetMapping(value = "/jogo/{id}")
    public ResponseEntity<JogoDTO> obterJogo(@PathVariable Integer id) {
        return ResponseEntity.ok().body(_jogoService.obterJogo(id));
    }

}

