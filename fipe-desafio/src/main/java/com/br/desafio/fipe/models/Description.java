package com.br.desafio.fipe.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Description(
        @JsonAlias("codigo") String code,
        @JsonAlias("nome") String name
) {
}
