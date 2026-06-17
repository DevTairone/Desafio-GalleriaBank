package galleriabank.compras.infrastructure.web.exceptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("Deve retornar 404 para ResourceNotFoundException")
    void deveRetornar404ParaResourceNotFound() {
        ResponseEntity<Map<String, Object>> response = handler.handleNotFound(
                new ResourceNotFoundException("Nao encontrado"));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().get("status"));
        assertEquals("Nao encontrado", response.getBody().get("message"));
    }

    @Test
    @DisplayName("Deve retornar 400 para BusinessException")
    void deveRetornar400ParaBusinessException() {
        ResponseEntity<Map<String, Object>> response = handler.handleBusiness(
                new BusinessException("Regra de negocio"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().get("status"));
        assertEquals("Regra de negocio", response.getBody().get("message"));
    }

    @Test
    @DisplayName("Deve retornar mapa de erros para MethodArgumentNotValidException")
    @SuppressWarnings("unchecked")
    void deveRetornarMapaDeErrosParaMethodArgumentNotValidException() throws NoSuchMethodException {
        BindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "obj");
        bindingResult.addError(new FieldError("obj", "nome", "Nome invalido"));
        bindingResult.addError(new FieldError("obj", "cpf", "CPF invalido"));

        MethodParameter methodParameter = new MethodParameter(
                GlobalExceptionHandlerTest.class.getDeclaredMethod("dummyMethod", Object.class), 0);
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(methodParameter, bindingResult);

        ResponseEntity<Map<String, Object>> response = handler.handleValidation(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().get("status"));

        Map<String, String> errors = (Map<String, String>) response.getBody().get("errors");
        assertEquals("Nome invalido", errors.get("nome"));
        assertEquals("CPF invalido", errors.get("cpf"));
    }

    private void dummyMethod(Object obj) {
        // metodo auxiliar apenas para construir MethodParameter em teste unitario
    }
}

