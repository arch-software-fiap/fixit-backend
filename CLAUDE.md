# CLAUDE.md

Este arquivo contém instruções para o Claude Code ao trabalhar neste repositório.

## Regras de Commit

- Utilizar **Conventional Commits** para todas as mensagens de commit
- Mensagens de commit devem ser escritas em **português**
- **Não incluir** co-author nas mensagens de commit
- **Antes de realizar o commit**, exibir a mensagem proposta e aguardar aprovação do usuário

### Formato do Conventional Commit

```
<tipo>(escopo opcional): descrição curta

corpo opcional com mais detalhes
```

### Tipos permitidos

- `feat`: nova funcionalidade
- `fix`: correção de bug
- `docs`: alterações na documentação
- `style`: formatação, ponto e vírgula, etc (sem alteração de código)
- `refactor`: refatoração de código
- `test`: adição ou correção de testes
- `chore`: tarefas de manutenção, atualizações de dependências
- `ci`: alterações em arquivos de CI/CD
- `perf`: melhorias de performance

### Exemplo de fluxo

1. Preparar as alterações para commit
2. Exibir a mensagem proposta para o usuário
3. Aguardar confirmação antes de executar o commit