package br.edu.ifg;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestesLeilao {
	@Test
	public void verificarRetornosDePossiveisFiltrosTest() {
		LeilaoPersistence lPersistence = new LeilaoPersistence();
		Leilao leilao = new Leilao("PS 2", BigDecimal.valueOf(1000), LocalDate.now(), "ABERTO");
		Leilao leilao1 = new Leilao("PS 3", BigDecimal.valueOf(1000), LocalDate.now(), "INATIVO");
		Leilao leilao2 = new Leilao("PS 4", BigDecimal.valueOf(1000), LocalDate.now(), "FINALIZADO");
		Leilao leilao3 = new Leilao("PS 5", BigDecimal.valueOf(1000), LocalDate.now(), "EXPIRADOS");
		lPersistence.insere(leilao);
		lPersistence.insere(leilao1);
		lPersistence.insere(leilao2);
		lPersistence.insere(leilao3);
		List<Leilao> list = lPersistence.listagem("ABERTO");
		assertEquals(list.get(0).getStatus(), "ABERTO");
		list = lPersistence.listagem("INATIVO");
		assertEquals(list.get(0).getStatus(), "INATIVO");
		list = lPersistence.listagem("FINALIZADO");
		assertEquals(list.get(0).getStatus(), "FINALIZADO");
		list = lPersistence.listagem("EXPIRADOS");
		assertEquals(list.get(0).getStatus(), "EXPIRADOS");
	}

	@Test
	public void verificarInatividadeDoLeilaoTest() {
		Leilao leilao = new Leilao("PS 2", BigDecimal.valueOf(1000), LocalDate.of(2025, 04, 13));
		assertEquals(leilao.getStatus(), "INATIVO");
	}

	@Test
	public void verificarAbertoLeilaoTest() {
		Leilao leilao = new Leilao("PS 2", BigDecimal.valueOf(1000), LocalDate.now());
		assertEquals(leilao.getStatus(), "ABERTO");
	}

	@Test
	public void verificarExpiradoLeilaoTest() {
		Leilao leilao = new Leilao("PS 2", BigDecimal.valueOf(1000), LocalDate.of(1967, 04, 13));
		assertEquals(leilao.getStatus(), "EXPIRADOS");
	}

	@Test
	public void verificarFinalizaLeilaoTest() {
		Leilao leilao = new Leilao("PS 2", BigDecimal.valueOf(1000), LocalDate.now());
		assertTrue(leilao.finalizar());
		assertEquals(leilao.getStatus(), "FINALIZADO");
	}

	@Test
	public void verificarFinalizaEAvisaVencedorLeilaoTest() {
		Leilao leilao = new Leilao("PS 2", BigDecimal.valueOf(1000), LocalDate.now());
		Usuario ana = new Usuario(1L, "Ana", "muito forte mesmo", "ana@gmail.com");
		Lance lanceAna = new Lance(ana, BigDecimal.valueOf(1001), LocalDateTime.now());
		leilao.propoe(lanceAna);
		assertTrue(leilao.finalizar());
		assertNotNull(leilao.getVencedor());
		assertEquals(leilao.getStatus(), "FINALIZADO");
		assertTrue(leilao.avisarVencedor(ana));
	}

}
