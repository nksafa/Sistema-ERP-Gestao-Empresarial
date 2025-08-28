package com.suplementos.erp.model;

public record Usuario(
        int codigo,
        String nome,
        String senha,
        TipoUsuario tipo
) {}