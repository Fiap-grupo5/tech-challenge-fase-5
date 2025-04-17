package br.com.fiap.tech.facility.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Schema(description = "Detalhes sobre erro na API")
public class ApiErrorSchema {
    
    @Schema(description = "Data e hora do erro", example = "15-07-2023 10:30:00")
    private LocalDateTime timestamp;
    
    @Schema(description = "Código HTTP do erro", example = "400")
    private int status;
    
    @Schema(description = "Tipo de erro", example = "Erro de validação")
    private String error;
    
    @Schema(description = "Mensagem de erro", example = "Verifique os campos enviados")
    private String message;
    
    @Schema(description = "Caminho da requisição", example = "/api/v1/facilities")
    private String path;
    
    @Schema(description = "Detalhes do erro", example = "[\"nome: O nome da unidade de saúde é obrigatório\", \"cnpj: CNPJ deve conter exatamente 14 dígitos numéricos\"]")
    private List<String> details = new ArrayList<>();
} 