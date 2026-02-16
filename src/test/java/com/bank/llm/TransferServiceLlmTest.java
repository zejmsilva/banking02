package com.bank.llm;

import com.bank.Account;
import com.bank.TransferService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes de Transferência – TransferService (LLM)")
class TransferServiceLlmTest {

    @Test
    @DisplayName("Deve transferir valor corretamente quando os dados são válidos")
    void transfer_deveTransferirQuandoDadosValidos() {
        Account from = new Account();
        Account to = new Account();
        from.deposit(new BigDecimal("100.00"));

        new TransferService().transfer(from, to, new BigDecimal("30.00"));

        assertEquals(new BigDecimal("70.00"), from.getBalance());
        assertEquals(new BigDecimal("30.00"), to.getBalance());
    }

    @Test
    @DisplayName("Deve lançar NullPointerException quando a conta de origem é nula")
    void transfer_deveLancarExcecaoSeContaFromNula() {
        Account to = new Account();

        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> new TransferService().transfer(null, to, new BigDecimal("10.00"))
        );

        assertEquals("Conta nula", ex.getMessage());
    }

    @Test
    @DisplayName("Deve lançar NullPointerException quando a conta de destino é nula")
    void transfer_deveLancarExcecaoSeContaToNula() {
        Account from = new Account();

        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> new TransferService().transfer(from, null, new BigDecimal("10.00"))
        );

        assertEquals("Conta nula", ex.getMessage());
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException ao tentar transferir para a mesma conta")
    void transfer_deveLancarExcecaoSeMesmaConta() {
        Account acc = new Account();

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new TransferService().transfer(acc, acc, new BigDecimal("10.00"))
        );

        assertEquals("Mesma conta", ex.getMessage());
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando o valor da transferência é nulo")
    void transfer_deveLancarExcecaoSeValorNulo() {
        Account from = new Account();
        Account to = new Account();

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new TransferService().transfer(from, to, null)
        );

        assertEquals("Valor inválido", ex.getMessage());
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando o valor da transferência é zero ou negativo")
    void transfer_deveLancarExcecaoSeValorZeroOuNegativo() {
        Account from = new Account();
        Account to = new Account();

        IllegalArgumentException ex1 = assertThrows(
                IllegalArgumentException.class,
                () -> new TransferService().transfer(from, to, BigDecimal.ZERO)
        );
        assertEquals("Valor inválido", ex1.getMessage());

        IllegalArgumentException ex2 = assertThrows(
                IllegalArgumentException.class,
                () -> new TransferService().transfer(from, to, new BigDecimal("-1"))
        );
        assertEquals("Valor inválido", ex2.getMessage());
    }

    @Test
    @DisplayName("Deve lançar IllegalStateException quando o saldo da conta de origem é insuficiente")
    void transfer_deveFalharSeSaldoInsuficiente() {
        Account from = new Account();
        Account to = new Account();
        from.deposit(new BigDecimal("10.00"));

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> new TransferService().transfer(from, to, new BigDecimal("50.00"))
        );

        assertEquals("Saldo insuficiente", ex.getMessage());
    }
}
