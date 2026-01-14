package com.fix_it.usecase.ordemservico;

import com.fix_it.usecase.ordemservico.output.AcompanhamentoOSOutput;
import java.util.UUID;

public interface ConsultarAcompanhamentoUseCase {
    AcompanhamentoOSOutput executar(UUID osId, String cpfCnpj);
}
