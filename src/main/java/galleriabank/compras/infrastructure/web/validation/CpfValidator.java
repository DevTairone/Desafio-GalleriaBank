package galleriabank.compras.infrastructure.web.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfValidator implements ConstraintValidator<CPF, String> {

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        if (cpf == null) return false;


        cpf = cpf.replaceAll("\\D", "");

        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            int soma = 0;
            for (int i = 0; i < 9; i++) {
                soma += (cpf.charAt(i) - '0') * (10 - i);
            }
            int r1 = 11 - (soma % 11);
            int d1 = (r1 == 10 || r1 == 11) ? 0 : r1;

            soma = 0;
            for (int i = 0; i < 10; i++) {
                soma += (cpf.charAt(i) - '0') * (11 - i);
            }
            int r2 = 11 - (soma % 11);
            int d2 = (r2 == 10 || r2 == 11) ? 0 : r2;

            return (cpf.charAt(9) - '0' == d1) && (cpf.charAt(10) - '0' == d2);
        } catch (Exception e) {
            return false;
        }
    }
}