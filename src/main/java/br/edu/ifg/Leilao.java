package br.edu.ifg;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Leilao {

	private Long id;

	private String nome;

	private BigDecimal valorInicial;

	private Usuario usuario;

	private LocalDate dataAbertura;

	private List<Lance> lances = new ArrayList<>();

	private String status;

	@Deprecated
	public Leilao() {
	}

	public Leilao(String nome) {
		this.nome = nome;
	}

	public Leilao(String nome, BigDecimal valorInicial, LocalDate dataAbertura) {
		this.nome = nome;
		this.valorInicial = valorInicial;
		this.dataAbertura = dataAbertura;
		this.status = dataAbertura.compareTo(LocalDate.now()) > 0 ? "INATIVO"
				: dataAbertura.compareTo(LocalDate.now()) < 0 ? "EXPIRADOS" : "ABERTO";
	}

	public Leilao(String nome, BigDecimal valorInicial, Usuario usuario) {
		this.nome = nome;
		this.valorInicial = valorInicial;
		this.usuario = usuario;
		this.dataAbertura = LocalDate.now();
	}

	public Leilao(String nome, BigDecimal valorInicial, LocalDate data, Usuario usuario) {
		this.nome = nome;
		this.valorInicial = valorInicial;
		this.usuario = usuario;
		this.dataAbertura = data;
	}

	public Leilao(String nome, BigDecimal valorInicial, LocalDate dataAbertura, String status) {
		super();
		this.nome = nome;
		this.valorInicial = valorInicial;
		this.dataAbertura = dataAbertura;
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setDataAbertura(LocalDate dataAbertura) {
		this.dataAbertura = dataAbertura;
	}

	public LocalDate getDataAbertura() {
		return dataAbertura;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setValorInicial(BigDecimal valorInicial) {
		this.valorInicial = valorInicial;
	}

	public BigDecimal getValorInicial() {
		return valorInicial;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setLances(List<Lance> lances) {
		this.lances = lances;
	}

	public boolean propoe(Lance lance) {
		if (!ehValido(lance)) {
			return false;
		}

		if (this.estaSemLances() || ehUmLanceValido(lance)) {
			adicionarLance(lance);
			return true;
		}
		return false;
	}

	public void propoeVoid(Lance lance) {
		if (!ehValido(lance)) {
			throw new RuntimeException("Lance deve ser maior que zero");
		}

		if (this.estaSemLances() || ehUmLanceValido(lance)) {
			adicionarLance(lance);
		} else {
			throw new RuntimeException("Erro inesperado");
		}
	}

	private boolean ehValido(Lance lance) {
		return lance.getValor().compareTo(BigDecimal.ZERO) > 0;
	}

	private void adicionarLance(Lance lance) {
		lances.add(lance);
	}

	private boolean ehUmLanceValido(Lance lance) {
		return valorEhMaior(lance, ultimoLanceDado()) && oUltimoUsuarioNaoEhOMesmoDo(lance);
	}

	private boolean valorEhMaior(Lance lance, Lance ultimoLanceDado) {
		return lance.getValor().compareTo(ultimoLanceDado.getValor()) > 0;
	}

	private boolean totalDeLancesDoUsuarioEhMenorIgual5(Usuario usuario) {
		int totalDeLances = qtdDeLancesDo(usuario);
		return totalDeLances < 5;
	}

	private boolean oUltimoUsuarioNaoEhOMesmoDo(Lance lance) {
		Usuario ultimoUsuarioQueDeuLance = ultimoLanceDado().getUsuario();
		return !ultimoUsuarioQueDeuLance.equals(lance.getUsuario());
	}

	private int qtdDeLancesDo(Usuario usuario) {
		int total = 0;
		for (Lance l : lances) {
			if (l.getUsuario().equals(usuario))
				total++;
		}
		return total;
	}

	private boolean estaSemLances() {
		return this.lances.isEmpty();
	}

	private Lance ultimoLanceDado() {
		return lances.get(lances.size() - 1);
	}

	public List<Lance> getLances() {
		return Collections.unmodifiableList(lances);
	}

	public boolean finalizar() {
		if (!this.status.equalsIgnoreCase("INATIVO")) {
			this.status = "FINALIZADO";
			return true;
		}
		return false;

	}
	
	public Usuario getVencedor() {
		Lance l = null;
		for (int i = 0; i < lances.size(); i++) {
			if(l == null) {
				l = lances.get(i);
			}else {
				if(lances.get(i).getValor().doubleValue() > l.getValor().doubleValue()) {
					l = lances.get(i);
				}
			}
		}
		return l == null ? null : l.getUsuario();
	}
	
	public boolean avisarVencedor(Usuario u) {
		// envia email
		return true;
	}

	public boolean temLances() {
		return !this.lances.isEmpty();
	}
}