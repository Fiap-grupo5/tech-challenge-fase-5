package br.com.fiap.tech.facility.dto;

import br.com.fiap.tech.facility.domain.FacilityType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(description = "Dados para criação de unidade de saúde")
public class HealthcareFacilityRequest {
    @NotBlank(message = "O nome da unidade de saúde é obrigatório")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    @Schema(
        description = "Nome da unidade de saúde",
        example = "Hospital São Luiz"
    )
    private String name;
    
    @NotNull(message = "O tipo da unidade de saúde é obrigatório")
    @Schema(
        description = "Tipo da unidade de saúde",
        example = "HOSPITAL"
    )
    private FacilityType facilityType;
    
    @NotBlank(message = "O CNPJ é obrigatório")
    @Pattern(regexp = "^\\d{14}$", message = "CNPJ deve conter exatamente 14 dígitos numéricos")
    @Schema(
        description = "CNPJ da unidade de saúde",
        example = "12345678901234"
    )
    private String cnpj;
    
    @NotBlank(message = "O telefone é obrigatório")
    @Pattern(regexp = "^\\(?\\d{2}\\)? ?\\d{4,5}-?\\d{4}$", message = "Formato de telefone inválido")
    @Schema(
        description = "Número de telefone da unidade de saúde",
        example = "(11) 98765-4321"
    )
    private String phoneNumber;
    
    @NotBlank(message = "O endereço é obrigatório")
    @Size(max = 200, message = "O endereço deve ter no máximo 200 caracteres")
    @Schema(
        description = "Endereço da unidade de saúde",
        example = "Av. Paulista, 1000"
    )
    private String address;
    
    @NotBlank(message = "A cidade é obrigatória")
    @Size(max = 100, message = "A cidade deve ter no máximo 100 caracteres")
    @Schema(
        description = "Cidade da unidade de saúde",
        example = "São Paulo"
    )
    private String city;
    
    @NotBlank(message = "O estado é obrigatório")
    @Pattern(regexp = "^[A-Z]{2}$", message = "Estado deve ser representado por 2 letras maiúsculas")
    @Schema(
        description = "Estado da unidade de saúde",
        example = "SP"
    )
    private String state;
    
    @NotBlank(message = "O CEP é obrigatório")
    @Pattern(regexp = "^\\d{8}$", message = "CEP deve conter exatamente 8 dígitos numéricos")
    @Schema(
        description = "CEP da unidade de saúde",
        example = "01310100"
    )
    private String zipCode;
    
    @NotNull(message = "A latitude é obrigatória")
    @DecimalMin(value = "-90.0", message = "Latitude mínima: -90.0")
    @DecimalMax(value = "90.0", message = "Latitude máxima: 90.0")
    @Schema(
        description = "Latitude da localização da unidade de saúde",
        example = "-23.5505"
    )
    private Double latitude;
    
    @NotNull(message = "A longitude é obrigatória")
    @DecimalMin(value = "-180.0", message = "Longitude mínima: -180.0")
    @DecimalMax(value = "180.0", message = "Longitude máxima: 180.0")
    @Schema(
        description = "Longitude da localização da unidade de saúde",
        example = "-46.6333"
    )
    private Double longitude;
    
    @NotNull(message = "A capacidade máxima diária é obrigatória")
    @Min(value = 1, message = "A capacidade máxima diária deve ser maior que zero")
    @Schema(
        description = "Capacidade máxima diária de atendimentos",
        example = "100"
    )
    private Integer maxDailyCapacity;
}
