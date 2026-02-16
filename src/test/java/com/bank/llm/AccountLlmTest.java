package com.bank.llm;

import com.bank.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountLlmTest {

    @Test
    @DisplayName("Saldo inicial deve ser BigDecimal.ZERO")
    void getBalance_deveIniciarEmZero() {
        Account acc = new Account();
        assertEquals(BigDecimal.ZERO, acc.getBalance());
    }

    @Test
    @DisplayName("deposit(null) deve lançar NPE com mensagem 'Depósito inválido'")
    void deposit_deveLancarNpeQuandoValorNulo() {
        Account acc = new Account();

        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> acc.deposit(null)
        );

        assertEquals("Depósito inválido", ex.getMessage());
        // garante que não houve alteração de estado
        assertEquals(BigDecimal.ZERO, acc.getBalance());
    }

    @Test
    @DisplayName("deposit(0) deve lançar IllegalArgumentException com mensagem 'Valor deve ser > 0'")
    void deposit_deveLancarIaeQuandoValorZero() {
        Account acc = new Account();

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> acc.deposit(BigDecimal.ZERO)
        );

        assertEquals("Valor deve ser > 0", ex.getMessage());
        assertEquals(BigDecimal.ZERO, acc.getBalance());
    }

    @Test
    @DisplayName("deposit(valor negativo) deve lançar IllegalArgumentException com mensagem 'Valor deve ser > 0'")
    void deposit_deveLancarIaeQuandoValorNegativo() {
        Account acc = new Account();

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> acc.deposit(new BigDecimal("-1"))
        );

        assertEquals("Valor deve ser > 0", ex.getMessage());
        assertEquals(BigDecimal.ZERO, acc.getBalance());
    }

    @Test
    @DisplayName("deposit(valor positivo) deve somar ao saldo mantendo precisão")
    void deposit_deveSomarAoSaldoQuandoValorPositivo() {
        Account acc = new Account();

        acc.deposit(new BigDecimal("100.00"));
        assertEquals(new BigDecimal("100.00"), acc.getBalance());

        acc.deposit(new BigDecimal("0.01"));
        assertEquals(new BigDecimal("100.01"), acc.getBalance());
    }

    @Test
    @DisplayName("withdraw(null) deve lançar NPE com mensagem 'Saque inválido'")
    void withdraw_deveLancarNpeQuandoValorNulo() {
        Account acc = new Account();
        acc.deposit(new BigDecimal("10.00"));

        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> acc.withdraw(null)
        );

        assertEquals("Saque inválido", ex.getMessage());
        // garante que não houve alteração de estado
        assertEquals(new BigDecimal("10.00"), acc.getBalance());
    }

    @Test
    @DisplayName("withdraw(0) deve lançar IllegalArgumentException com mensagem 'Valor deve ser > 0'")
    void withdraw_deveLancarIaeQuandoValorZero() {
        Account acc = new Account();
        acc.deposit(new BigDecimal("10.00"));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> acc.withdraw(BigDecimal.ZERO)
        );

        assertEquals("Valor deve ser > 0", ex.getMessage());
        assertEquals(new BigDecimal("10.00"), acc.getBalance());
    }

    @Test
    @DisplayName("withdraw(valor negativo) deve lançar IllegalArgumentException com mensagem 'Valor deve ser > 0'")
    void withdraw_deveLancarIaeQuandoValorNegativo() {
        Account acc = new Account();
        acc.deposit(new BigDecimal("10.00"));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> acc.withdraw(new BigDecimal("-0.01"))
        );

        assertEquals("Valor deve ser > 0", ex.getMessage());
        assertEquals(new BigDecimal("10.00"), acc.getBalance());
    }

    @Test
    @DisplayName("withdraw(valor maior que saldo) deve lançar IllegalStateException com mensagem 'Saldo insuficiente'")
    void withdraw_deveLancarIseQuandoSaldoInsuficiente() {
        Account acc = new Account();
        acc.deposit(new BigDecimal("10.00"));

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> acc.withdraw(new BigDecimal("10.01"))
        );

        assertEquals("Saldo insuficiente", ex.getMessage());
        assertEquals(new BigDecimal("10.00"), acc.getBalance());
    }

    @Test
    @DisplayName("withdraw(valor igual ao saldo) deve zerar o saldo")
    void withdraw_deveZerarSaldoQuandoValorIgualAoSaldo() {
        Account acc = new Account();
        acc.deposit(new BigDecimal("50.00"));

        acc.withdraw(new BigDecimal("50.00"));
        assertEquals(new BigDecimal("0.00"), acc.getBalance(),
                "Após sacar o saldo total, saldo deve ficar 0.00");
    }

    @Test
    @DisplayName("Sequência de operações deve resultar no saldo final esperado")
    void sequencia_depositarSacarDepositarDeNovo() {
        Account acc = new Account();

        acc.deposit(new BigDecimal("100.00"));
        acc.withdraw(new BigDecimal("40.00"));
        acc.deposit(new BigDecimal("10.00"));

        assertEquals(new BigDecimal("70.00"), acc.getBalance());
    }

    @Test
    @DisplayName("Falhas não devem alterar saldo: deposit inválido e withdraw inválido")
    void operacoesInvalidas_naoDevemAlterarSaldo() {
        Account acc = new Account();
        acc.deposit(new BigDecimal("20.00"));

        // deposit inválido
        assertThrows(IllegalArgumentException.class, () -> acc.deposit(BigDecimal.ZERO));
        assertEquals(new BigDecimal("20.00"), acc.getBalance());

        // withdraw inválido
        assertThrows(IllegalArgumentException.class, () -> acc.withdraw(BigDecimal.ZERO));
        assertEquals(new BigDecimal("20.00"), acc.getBalance());

        // withdraw saldo insuficiente
        assertThrows(IllegalStateException.class, () -> acc.withdraw(new BigDecimal("20.01")));
        assertEquals(new BigDecimal("20.00"), acc.getBalance());
    }
}
