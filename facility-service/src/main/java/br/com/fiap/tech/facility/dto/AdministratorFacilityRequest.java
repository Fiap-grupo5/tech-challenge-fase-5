package br.com.fiap.tech.facility.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Schema(description = "Dados para associação de administrador a uma unidade de saúde")
public class AdministratorFacilityRequest {
    @NotNull(message = "O ID do administrador é obrigatório")
    @Positive(message = "O ID do administrador deve ser positivo")
    @Schema(
        description = "ID do administrador",
        example = "1"
    )
    private Long administratorId;
    
    @NotNull(message = "O ID da unidade de saúde é obrigatório")
    @Positive(message = "O ID da unidade de saúde deve ser positivo")
    @Schema(
        description = "ID da unidade de saúde",
        example = "1"
    )
    private Long healthcareFacilityId;
}
