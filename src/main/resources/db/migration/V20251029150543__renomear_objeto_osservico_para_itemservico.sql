alter table public.os_servico
    rename to os_item_servico;

alter table public.os_item_servico
    rename column sq_os_servico to sq_os_item_servico;

alter table public.os_item_servico
    rename constraint pk_os_servico to pk_os_item_servico;
