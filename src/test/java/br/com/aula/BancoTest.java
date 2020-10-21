package br.com.aula;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import br.com.aula.exception.NumeroDeContaJaExistenteException;
import br.com.aula.exception.ValorTransferidoNegativoException;
import br.com.aula.exception.ContaDestinoNaoExistenteException;
import br.com.aula.exception.ContaNaoExistenteException;
import br.com.aula.exception.ContaOrigemNaoExistenteException;
import br.com.aula.exception.ContaSemSaldoException;
import br.com.aula.exception.NomeDeClienteJaExistenteException;
import br.com.aula.exception.NumeroDaContaInvalidoException;

public class BancoTest {
	
	// Item A.a
	@Test
	public void deveCadastrarConta() throws NumeroDeContaJaExistenteException, NomeDeClienteJaExistenteException, NumeroDaContaInvalidoException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta conta = new Conta(cliente, 123, 0, TipoConta.CORRENTE);
		Banco banco = new Banco();

		// Ação
		banco.cadastrarConta(conta);

		// Verificação
		assertEquals(1, banco.obterContas().size());
	}
	
	// Item A.b
	@Test(expected = NumeroDeContaJaExistenteException.class)
	public void naoDeveCadastrarContaNumeroRepetido() throws NumeroDeContaJaExistenteException, NomeDeClienteJaExistenteException, NumeroDaContaInvalidoException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta conta1 = new Conta(cliente, 123, 0, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Maria");
		Conta conta2 = new Conta(cliente2, 123, 0, TipoConta.POUPANCA);

		Banco banco = new Banco();

		// Ação
		banco.cadastrarConta(conta1);
		banco.cadastrarConta(conta2);

		Assert.fail();
	}
	
	// Item A.d
	@Test(expected = NomeDeClienteJaExistenteException.class)
	public void naoDeveCadastrarContaClienteRepetido() throws NumeroDeContaJaExistenteException, NomeDeClienteJaExistenteException, NumeroDaContaInvalidoException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta conta1 = new Conta(cliente, 123, 0, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Joao");
		Conta conta2 = new Conta(cliente2, 321, 0, TipoConta.POUPANCA);

		Banco banco = new Banco();

		// Ação
		banco.cadastrarConta(conta1);
		banco.cadastrarConta(conta2);

		Assert.fail();
	}
	
	// Item A.c
	@Test(expected = NumeroDaContaInvalidoException.class)
	public void naoDeveCadastrarContaComNumeroInvalido() throws NumeroDeContaJaExistenteException, NomeDeClienteJaExistenteException, NumeroDaContaInvalidoException {
		
		Cliente cliente = new Cliente("Joao");
		Conta conta = new Conta(cliente, -123, 0, TipoConta.CORRENTE);
		
		Banco banco = new Banco();
		
		banco.cadastrarConta(conta);
		
		Assert.fail();
	}
	
	@Test
	public void deveEfetuarTransferenciaContasCorrentes() throws ContaSemSaldoException, ContaNaoExistenteException, ContaOrigemNaoExistenteException, ContaDestinoNaoExistenteException, ValorTransferidoNegativoException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 0, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 0, TipoConta.CORRENTE);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));

		// Ação
		banco.efetuarTransferencia(123, 456, 100);

		// Verificação
		assertEquals(-100, contaOrigem.getSaldo());
		assertEquals(100, contaDestino.getSaldo());
	}
	
	// Item B.a
	@Test
	public void deveEfetuarTransferenciaContasCorrentePoupanca() throws ContaSemSaldoException, ContaNaoExistenteException, ContaOrigemNaoExistenteException, ContaDestinoNaoExistenteException, ValorTransferidoNegativoException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 0, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 0, TipoConta.POUPANCA);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));

		// Ação
		banco.efetuarTransferencia(123, 456, 100);

		// Verificação
		assertEquals(-100, contaOrigem.getSaldo());
		assertEquals(100, contaDestino.getSaldo());
	}
	
	// Item B.b
	@Test(expected = ContaOrigemNaoExistenteException.class)
	public void deveExistirContaDeOrigemNoBanco() throws NumeroDeContaJaExistenteException, NomeDeClienteJaExistenteException, NumeroDaContaInvalidoException, ContaNaoExistenteException, ContaSemSaldoException, ContaOrigemNaoExistenteException, ContaDestinoNaoExistenteException, ValorTransferidoNegativoException {
		
		Cliente cliente = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente, 456, 0, TipoConta.CORRENTE);
		
		Banco banco = new Banco();
		
		banco.cadastrarConta(contaDestino);
		
		banco.efetuarTransferencia(123, 456, 100);
		
		Assert.fail();
		
	}
	
	// Item B.d
	@Test(expected = ContaDestinoNaoExistenteException.class)
	public void deveExistirContaDeDestinoNoBanco() throws NumeroDeContaJaExistenteException, NomeDeClienteJaExistenteException, NumeroDaContaInvalidoException, ContaNaoExistenteException, ContaSemSaldoException, ContaOrigemNaoExistenteException, ContaDestinoNaoExistenteException, ValorTransferidoNegativoException {
		
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 0, TipoConta.CORRENTE);
		
		Banco banco = new Banco();
		
		banco.cadastrarConta(contaOrigem);
		
		banco.efetuarTransferencia(123, 456, 100);
		
		Assert.fail();
		
	}
	
	// Item B.c
	@Test(expected = ContaSemSaldoException.class)
	public void saldoDaContaPoupancaNaoDeveFicarNegativo() throws NumeroDeContaJaExistenteException, NomeDeClienteJaExistenteException, NumeroDaContaInvalidoException, ContaNaoExistenteException, ContaSemSaldoException, ContaOrigemNaoExistenteException, ContaDestinoNaoExistenteException, ValorTransferidoNegativoException {
		
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 0, TipoConta.POUPANCA);
		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 0, TipoConta.CORRENTE);
		
		Banco banco = new Banco();
		
		banco.cadastrarConta(contaOrigem);
		banco.cadastrarConta(contaDestino);
		
		banco.efetuarTransferencia(123, 456, 100);
		
		Assert.fail();
		
	}
	
	// Item B.e
	@Test(expected = ValorTransferidoNegativoException.class)
	public void valorTransferidoNaoDeveSerNegativo() throws NumeroDeContaJaExistenteException, NomeDeClienteJaExistenteException, NumeroDaContaInvalidoException, ContaNaoExistenteException, ContaSemSaldoException, ContaOrigemNaoExistenteException, ContaDestinoNaoExistenteException, ValorTransferidoNegativoException {
		
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 0, TipoConta.POUPANCA);
		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 0, TipoConta.CORRENTE);
		
		Banco banco = new Banco();
		
		banco.cadastrarConta(contaOrigem);
		banco.cadastrarConta(contaDestino);
		
		banco.efetuarTransferencia(123, 456, -100);
		
		Assert.fail();
		
	}
}
