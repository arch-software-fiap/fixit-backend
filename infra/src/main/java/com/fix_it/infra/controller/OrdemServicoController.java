package com.fix_it.infra.controller;

import com.fix_it.core.domain.enums.SituacaoOrdemServico;
import com.fix_it.infra.controller.dto.AprovacaoOrcamentoDTO;
import com.fix_it.infra.controller.dto.AtualizacaoStatusDTO;
import com.fix_it.usecase.ordemservico.*;
import com.fix_it.usecase.ordemservico.input.AdicionarItemPecaInput;
import com.fix_it.usecase.ordemservico.input.AdicionarItemServicoInput;
import com.fix_it.usecase.ordemservico.input.CriarOrdemServicoInput;
import com.fix_it.usecase.ordemservico.output.OrdemServicoOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ordem-servico")
@RequiredArgsConstructor
public class OrdemServicoController {

    private final CriarOrdemServicoUseCase criarOrdemServicoUseCase;
    private final BuscarOrdemServicoPorIdUseCase buscarOrdemServicoPorIdUseCase;
    private final AdicionarItemServicoUseCase adicionarItemServicoUseCase;
    private final AdicionarItemPecaUseCase adicionarItemPecaUseCase;
    private final ConsultarStatusOrdemServicoUseCase consultarStatusOrdemServicoUseCase;
    private final AprovarOrcamentoUseCase aprovarOrcamentoUseCase;
    private final ListarOrdensServicoUseCase listarOrdensServicoUseCase;
    private final AtualizarStatusOrdemServicoUseCase atualizarStatusOrdemServicoUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrdemServicoOutput create(@RequestBody CriarOrdemServicoInput input) {
        return criarOrdemServicoUseCase.executar(input);
    }

    @GetMapping("/{id}")
    public OrdemServicoOutput findById(@PathVariable UUID id) {
        return buscarOrdemServicoPorIdUseCase.executar(id);
    }

    @PostMapping("/{osId}/item-servico")
    public void adicionarItemServico(@PathVariable UUID osId, @RequestBody AdicionarItemServicoInput input) {
        adicionarItemServicoUseCase.executar(osId, input);
    }

    @PostMapping("/{osId}/item-peca")
    public void adicionarItemPeca(@PathVariable UUID osId, @RequestBody AdicionarItemPecaInput input) {
        adicionarItemPecaUseCase.executar(osId, input);
    }

    @GetMapping("/{id}/status")
    public SituacaoOrdemServico getStatus(@PathVariable UUID id) {
        return consultarStatusOrdemServicoUseCase.executar(id);
    }

    @PostMapping("/{id}/aprovar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void aprovarOrcamento(@PathVariable UUID id, @RequestBody AprovacaoOrcamentoDTO aprovacao) {
        aprovarOrcamentoUseCase.executar(id, aprovacao.isAprovado());
    }

    @GetMapping
    public List<OrdemServicoOutput> listar() {
        return listarOrdensServicoUseCase.executar();
    }

    @PutMapping("/{id}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizarStatus(@PathVariable UUID id, @RequestBody AtualizacaoStatusDTO atualizacao) {
        atualizarStatusOrdemServicoUseCase.executar(id, atualizacao.getSituacao());
    }
}
