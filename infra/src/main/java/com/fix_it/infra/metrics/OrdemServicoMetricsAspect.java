package com.fix_it.infra.metrics;

import com.fix_it.core.domain.enums.SituacaoOrdemServico;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class OrdemServicoMetricsAspect {

    private final MeterRegistry meterRegistry;
    private final Counter osCreatedCounter;
    private final Timer osCreatedTimer;

    public OrdemServicoMetricsAspect(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.osCreatedCounter = Counter.builder("ordens_servico_criadas_total")
                .description("Total de ordens de servico criadas")
                .register(meterRegistry);
        this.osCreatedTimer = Timer.builder("ordens_servico_criacao_duracao_segundos")
                .description("Tempo de criacao de ordem de servico")
                .register(meterRegistry);
    }

    @Around("execution(* com.fix_it.application.usecasesimpl.CriarOrdemServicoUseCaseImpl.executar(..))")
    public Object medirCriacaoOrdemServico(ProceedingJoinPoint pjp) throws Throwable {
        long inicio = System.nanoTime();
        try {
            Object resultado = pjp.proceed();
            osCreatedCounter.increment();
            return resultado;
        } finally {
            osCreatedTimer.record(System.nanoTime() - inicio, TimeUnit.NANOSECONDS);
        }
    }

    @Around("execution(* com.fix_it.application.usecasesimpl.AtualizarStatusOrdemServicoUseCaseImpl.executar(..))")
    public Object medirAtualizacaoStatus(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        SituacaoOrdemServico situacao = args.length > 1 ? (SituacaoOrdemServico) args[1] : null;
        try {
            return pjp.proceed();
        } finally {
            Counter.builder("ordens_servico_status_total")
                    .description("Transicoes de status de ordens de servico")
                    .tag("status", situacao != null ? situacao.name() : "desconhecido")
                    .register(meterRegistry)
                    .increment();
        }
    }
}
