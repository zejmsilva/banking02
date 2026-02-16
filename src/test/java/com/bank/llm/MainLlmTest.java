package com.bank.llm;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class MainLlmTest {

    @Test
    @DisplayName("Main.main deve imprimir os saldos esperados (args vazio)")
    void main_deveImprimirSaldosEsperados_argsVazio() {
        String out = runMainAndCaptureStdout(new String[0]);

        assertTrue(out.contains("Saldo CC: 1000.00"), "Deve imprimir saldo inicial da CC");
        assertTrue(out.contains("Saldo CC após transferência: 700.00"), "Deve imprimir saldo final da CC");
        assertTrue(out.contains("Saldo Poupança: 300.00"), "Deve imprimir saldo final da poupança");
    }

    @Test
    @DisplayName("Main.main deve imprimir os saldos esperados (args null)")
    void main_deveImprimirSaldosEsperados_argsNull() {
        // Em Java, main pode ser chamado com null; como o código não usa args, deve funcionar igual.
        String out = runMainAndCaptureStdout(null);

        assertTrue(out.contains("Saldo CC: 1000.00"));
        assertTrue(out.contains("Saldo CC após transferência: 700.00"));
        assertTrue(out.contains("Saldo Poupança: 300.00"));
    }

    @Test
    @DisplayName("Main.main deve imprimir os saldos esperados (args com valores)")
    void main_deveImprimirSaldosEsperados_argsComValores() {
        String out = runMainAndCaptureStdout(new String[] {"qualquer", "coisa"});

        assertTrue(out.contains("Saldo CC: 1000.00"));
        assertTrue(out.contains("Saldo CC após transferência: 700.00"));
        assertTrue(out.contains("Saldo Poupança: 300.00"));
    }

    @Test
    @DisplayName("Saída do Main deve conter exatamente 3 linhas relevantes (tolerando separadores de linha)")
    void main_deveConterTresLinhasRelevantes() {
        String out = runMainAndCaptureStdout(new String[0]).trim();

        // Normaliza para '\n' e remove linhas vazias
        String normalized = out.replace("\r\n", "\n").replace("\r", "\n");
        String[] lines = normalized.split("\n");
        long nonEmptyCount = java.util.Arrays.stream(lines).filter(s -> !s.trim().isEmpty()).count();

        // O Main imprime 3 println; se não houver outros logs, devem ser 3 linhas.
        assertEquals(3, nonEmptyCount, "Main deve imprimir exatamente 3 linhas não vazias");
    }

    private static String runMainAndCaptureStdout(String[] args) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream capture = new PrintStream(baos);

        try {
            System.setOut(capture);
            com.bank.Main.main(args);
        } finally {
            System.setOut(originalOut);
            capture.flush();
        }

        return baos.toString();
    }
}
