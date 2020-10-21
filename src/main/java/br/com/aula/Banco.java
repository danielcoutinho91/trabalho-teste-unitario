package br.com.aula;

import java.util.ArrayList;
import java.util.List;

import br.com.aula.exception.ContaDestinoNaoExistenteException;
import br.com.aula.exception.ContaNaoExistenteException;
import br.com.aula.exception.ContaOrigemNaoExistenteException;
import br.com.aula.exception.ContaSemSaldoException;
import br.com.aula.exception.NomeDeClienteJaExistenteException;
import br.com.aula.exception.NumeroDaContaInvalidoException;
import br.com.aula.exception.NumeroDeContaJaExistenteException;
import br.com.aula.exception.ValorTransferidoNegativoException;

public class Banco {

	private List<Conta> contas = new ArrayList<Conta>();

	public Banco() {
	}

	public Banco(List<Conta> contas) {
		this.contas = contas;
	}


	public void cadastrarConta(Conta conta) throws NumeroDeContaJaExistenteException, NomeDeClienteJaExistenteException, NumeroDaContaInvalidoException {

		int numeroDaConta = conta.getNumeroConta();
		
		if (numeroDaConta <= 0) {
			throw new NumeroDaContaInvalidoException();
		}

		for (Conta c : contas) {
			boolean isNomeClienteIgual = c.getCliente().getNome().equals(conta.getCliente().getNome());
			boolean isNumeroContaIgual = c.getNumeroConta() == conta.getNumeroConta();

			if (isNumeroContaIgual) {
				throw new NumeroDeContaJaExistenteException();
			}
			
			if (isNomeClienteIgual) {
				throw new NomeDeClienteJaExistenteException();
			}		
			
		}
		
		this.contas.add(conta);

	}

	public void efetuarTransferencia(int numeroContaOrigem, int numeroContaDestino, int valor)
			throws ContaNaoExistenteException, ContaSemSaldoException, ContaOrigemNaoExistenteException, ContaDestinoNaoExistenteException, ValorTransferidoNegativoException {
		
		if (valor <= 0) {
			throw new ValorTransferidoNegativoException();
		}
		
		Conta contaOrigem = this.obterContaPorNumero(numeroContaOrigem);
		Conta contaDestino = this.obterContaPorNumero(numeroContaDestino);

		boolean isContaOrigemExistente = contaOrigem != null;
		boolean isContaDestinoExistente = contaDestino != null;
		
		if (!isContaOrigemExistente) {
			throw new ContaOrigemNaoExistenteException();
		}
		
		if (!isContaDestinoExistente) {
			throw new ContaDestinoNaoExistenteException();
		}

		if (isContaOrigemExistente && isContaDestinoExistente) {

			boolean isContaOrigemPoupanca = contaOrigem.getTipoConta().equals(TipoConta.POUPANCA);
			boolean isSaldoContaOrigemNegativo = contaOrigem.getSaldo() - valor < 0;

			if (isContaOrigemPoupanca && isSaldoContaOrigemNegativo) {
				throw new ContaSemSaldoException();
			}

			contaOrigem.debitar(valor);
			contaDestino.creditar(valor);

		} else {
			throw new ContaNaoExistenteException();
		}
	}

	public Conta obterContaPorNumero(int numeroConta) {

		for (Conta c : contas) {
			if (c.getNumeroConta() == numeroConta) {
				return c;
			}
		}

		return null;
	}

	public List<Conta> obterContas() {
		return this.contas;
	}
}
