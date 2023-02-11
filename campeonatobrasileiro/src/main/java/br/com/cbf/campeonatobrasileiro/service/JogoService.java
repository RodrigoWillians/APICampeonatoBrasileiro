package br.com.cbf.campeonatobrasileiro.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cbf.campeonatobrasileiro.dto.ClassificacaoDTO;
import br.com.cbf.campeonatobrasileiro.dto.ClassificacaoTimeDTO;
import br.com.cbf.campeonatobrasileiro.dto.JogoDTO;
import br.com.cbf.campeonatobrasileiro.dto.JogoFinalizadoDTO;
import br.com.cbf.campeonatobrasileiro.model.Jogo;
import br.com.cbf.campeonatobrasileiro.model.Time;
import br.com.cbf.campeonatobrasileiro.repository.JogoRepository;
import br.com.cbf.campeonatobrasileiro.repository.TimeRepository;

@Service
public class JogoService {
    
    @Autowired
    JogoRepository _jogoRepository;

    @Autowired
    TimeService _timeService;



    public void gerarJogos(LocalDateTime primeiraRodada) {
        final List<Time> times = _timeService.findAll();
        List<Time> all1 = new ArrayList<>();
        List<Time> all2 = new ArrayList<>();
        all1.addAll(times);
        all2.addAll(times);

        _jogoRepository.deleteAll();

        List<Jogo> jogos = new ArrayList<>();

        int t = times.size();
        int m = times.size() / 2; 
        LocalDateTime dataJogo = primeiraRodada;
        Integer rodada = 0;
        for (int i = 0; i < t - 1; i++) {
            rodada = i + 1;
            for (int j = 0; j < m; j++) {
                Time time1;
                Time time2;
                if (j % 2 == 1 || i % 2 == 1 && j == 0) {
                    time1 = times.get(t - j - 1);
                    time2 = times.get(j);
                } else {
                    time1 = times.get(j);
                    time2 = times.get(t - j - 1);
                }
                if (time1 == null) {
                    System.out.println("Time 1 null");
                }
                jogos.add(gerarJogo(dataJogo, rodada, time1, time2));
                dataJogo = dataJogo.plusDays(7);
            }
            
            times.add(1, times.remove(times.size() - 1));
        }

        jogos.forEach(jogo -> System.out.println(jogo));

        _jogoRepository.saveAll(jogos);

        List<Jogo> jogos2 = new ArrayList<>();

        jogos.forEach(jogo -> {
            Time time1 = jogo.getTime2();
            Time time2 = jogo.getTime1();
            jogos2.add(gerarJogo(jogo.getData().plusDays(7 * jogos.size()), jogo.getRodada() + jogos.size(), time1,
                    time2));
        });
        _jogoRepository.saveAll(jogos2);
    }


    private Jogo gerarJogo(LocalDateTime dataJogo, Integer rodada, Time time1, Time time2) {
        Jogo jogo = new Jogo();
        jogo.setTime1(time1);
        jogo.setTime2(time2);
        jogo.setRodada(rodada);
        jogo.setData(dataJogo);
        jogo.setEncerrado(false);
        jogo.setGolsTime1(0);
        jogo.setGolsTime2(0);
        jogo.setPublicoPagante(0);
        return jogo;
    }
    
    private JogoDTO toDto(Jogo entity) {
        JogoDTO dto = new JogoDTO();
        dto.setId(entity.getId());
        dto.setData(entity.getData());
        dto.setEncerrado(entity.getEncerrado());
        dto.setGolsTime1(entity.getGolsTime1());
        dto.setGolsTime2(entity.getGolsTime2());
        dto.setPublicoPagante(entity.getPublicoPagante());
        dto.setRodada(entity.getRodada());
        dto.setTime1(_timeService.toDto(entity.getTime1()));
        dto.setTime2(_timeService.toDto(entity.getTime2()));
        return dto;
    }


    public List<JogoDTO> listarJogos() {
        return _jogoRepository.findAll().stream().map(entity -> toDto(entity)).collect(Collectors.toList());
    }


    public JogoDTO finalizar(Integer id, JogoFinalizadoDTO jogoDto) throws Exception{
        Optional<Jogo> optionalJogo = _jogoRepository.findById(id);
        if (optionalJogo.isPresent()) {
            final Jogo jogo = optionalJogo.get();

            jogo.setGolsTime1(jogoDto.getGolsTime1());
            jogo.setGolsTime2(jogoDto.getGolsTime2());
            jogo.setEncerrado(true);
            jogo.setPublicoPagante(jogoDto.getPublicoPagante());
            _jogoRepository.save(jogo);
            return toDto(_jogoRepository.save(jogo));
        } else {
            throw new Exception("Jogo não existe");
        }
    }


    public ClassificacaoDTO obterClassificacao() {

        ClassificacaoDTO classificacaoDto = new ClassificacaoDTO();
        final List<Time> times = _timeService.findAll();

        times.forEach(time -> {
            
            final List<Jogo> jogosMandante = _jogoRepository.findByTime1AndEncerrado(time, true);
            final List<Jogo> jogosVisitante = _jogoRepository.findByTime2AndEncerrado(time, true);
            AtomicReference<Integer> vitorias = new AtomicReference<>(0);
            AtomicReference<Integer> empates = new AtomicReference<>(0);
            AtomicReference<Integer> derrotas = new AtomicReference<>(0);
            AtomicReference<Integer> golsSofridos = new AtomicReference<>(0);
            AtomicReference<Integer> golsMarcados = new AtomicReference<>(0);

            jogosMandante.forEach(jogo -> {
                if (jogo.getGolsTime1() > jogo.getGolsTime2()) {
                    vitorias.getAndSet(vitorias.get() + 1);
                } else if (jogo.getGolsTime1() < jogo.getGolsTime2()) {
                    derrotas.getAndSet(derrotas.get() + 1);
                } else {
                    empates.getAndSet(empates.get() + 1);
                }
                golsMarcados.set(golsMarcados.get() + jogo.getGolsTime1());
                golsSofridos.set(golsSofridos.get() + jogo.getGolsTime2());
            });

            jogosVisitante.forEach(jogo -> {
                if (jogo.getGolsTime2() > jogo.getGolsTime1()) {
                    vitorias.getAndSet(vitorias.get() + 1);
                } else if (jogo.getGolsTime2() < jogo.getGolsTime1()) {
                    derrotas.getAndSet(derrotas.get() + 1);
                } else {
                    empates.getAndSet(empates.get() + 1);
                }
                golsMarcados.set(golsMarcados.get() + jogo.getGolsTime2());
                golsSofridos.set(golsSofridos.get() + jogo.getGolsTime1());
            });

            ClassificacaoTimeDTO classificacaoTimeDto = new ClassificacaoTimeDTO();

            classificacaoTimeDto.setIdTime(time.getId());
            classificacaoTimeDto.setTime(time.getNome());
            classificacaoTimeDto.setPontos((vitorias.get() * 3) + empates.get());
            classificacaoTimeDto.setDerrotas(derrotas.get());
            classificacaoTimeDto.setEmpates(empates.get());
            classificacaoTimeDto.setVitorias(vitorias.get());
            classificacaoTimeDto.setGolsMarcados(golsMarcados.get());
            classificacaoTimeDto.setGolsSofridos(golsSofridos.get());
            classificacaoTimeDto.setJogos(derrotas.get() + empates.get() + vitorias.get());

            classificacaoDto.getTimes().add(classificacaoTimeDto);

        });

        Collections.sort(classificacaoDto.getTimes(), Collections.reverseOrder());
        int posicao = 0;
        for (ClassificacaoTimeDTO time : classificacaoDto.getTimes()) {
            time.setPosicao(posicao++);
        }
        

        return classificacaoDto;
    }


    public JogoDTO obterJogo(Integer id) {
        return toDto(_jogoRepository.findById(id).get());
    }
}
