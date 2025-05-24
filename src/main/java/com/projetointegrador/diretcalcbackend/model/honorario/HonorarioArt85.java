package com.projetointegrador.diretcalcbackend.model.honorario;

import com.projetointegrador.diretcalcbackend.enums.Aliquota;
import jakarta.persistence.Entity;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class HonorarioArt85 extends Honorario {
    private LocalDate fixacaoVerba;

    private Aliquota aliquota;
}
