package galleriabank.compras.infrastructure.web.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CpfValidatorTest {

    private final CpfValidator validator = new CpfValidator();

    @Test
    @DisplayName("Deve aceitar CPF valido sem mascara")
    void deveAceitarCpfValidoSemMascara() {
        assertTrue(validator.isValid("52998224725", null));
    }

    @Test
    @DisplayName("Deve aceitar CPF valido com mascara")
    void deveAceitarCpfValidoComMascara() {
        assertTrue(validator.isValid("529.982.247-25", null));
    }

    @Test
    @DisplayName("Deve rejeitar CPF nulo, incompleto e repetido")
    void deveRejeitarCpfNuloIncompletoRepetido() {
        assertFalse(validator.isValid(null, null));
        assertFalse(validator.isValid("12345", null));
        assertFalse(validator.isValid("11111111111", null));
    }

    @Test
    @DisplayName("Deve rejeitar CPF com digitos verificadores invalidos")
    void deveRejeitarCpfComDigitosInvalidos() {
        assertFalse(validator.isValid("52998224724", null));
    }
}

