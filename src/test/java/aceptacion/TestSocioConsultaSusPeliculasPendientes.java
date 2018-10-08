package aceptacion;

import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import dao.IDAOVideoclub;
import dominio.Credenciales;
import dominio.PeliculaAlquilada;
import dominio.Socio;
import excepciones.ExcepcionNoHayPeliculasPendientes;
import modelo.IVideoclubSocio;
import modelo.VideoclubSocio;

// HU07
public class TestSocioConsultaSusPeliculasPendientes {
	private IVideoclubSocio videoclub;
	private IDAOVideoclub daoMock;
	private Credenciales credencialesSocio;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	// Inicializamos el test
	@Before
	public void initTest() throws Exception {
		daoMock = Mockito.mock(IDAOVideoclub.class);
		videoclub = new VideoclubSocio(daoMock);

		credencialesSocio = new Credenciales();
		credencialesSocio.setUsuario("Jocker66");
		credencialesSocio.setContrasenya("cualquiera");
		credencialesSocio.setAdmin(false);
		when(daoMock.comprobarCredenciales(credencialesSocio.getUsuario(), credencialesSocio.getContrasenya()))
				.thenReturn(credencialesSocio);
		videoclub.iniciarSesion("Jocker66", "cualquiera");

		Socio socio = new Socio();
		socio.setIdSocio("20475654M");
		when(daoMock.getSocioPorId("20475654M")).thenReturn(socio);
		when(daoMock.getSocioPorUsuario("Jocker66")).thenReturn(socio);

	}

	// E7.1
	@Test
	public void socioConsultaListaPendientesYHay() throws ExcepcionNoHayPeliculasPendientes {
		PeliculaAlquilada alquilada1 = new PeliculaAlquilada();
		alquilada1.setTituloVO("Cobra");
		alquilada1.setTituloEsp("Cobra");
		alquilada1.setAnyo(1986);
		alquilada1.setIdSocio(daoMock.getSocioPorUsuario(credencialesSocio.getUsuario()).getIdSocio());
		alquilada1.setEnlace("http://mivideoclub.com/20161113000000000000/cobra.avi");
		alquilada1.setTiempoAlquiler(new Timestamp(
				System.currentTimeMillis() - PeliculaAlquilada.DURACION_ALQUILER_MILIS + 30 * 60 * 60 * 1000));

		PeliculaAlquilada alquilada2 = new PeliculaAlquilada();
		alquilada2.setTituloVO("Escape plan");
		alquilada2.setTituloEsp("Plan de escape");
		alquilada2.setAnyo(2013);
		alquilada2.setIdSocio(daoMock.getSocioPorUsuario(credencialesSocio.getUsuario()).getIdSocio());
		alquilada2.setEnlace("http://mivideoclub.com/20161113100000000000/escapeplan.avi");
		alquilada2.setTiempoAlquiler(new Timestamp(
				System.currentTimeMillis() - PeliculaAlquilada.DURACION_ALQUILER_MILIS + 40 * 60 * 60 * 1000));

		PeliculaAlquilada alquilada3 = new PeliculaAlquilada();
		alquilada3.setTituloVO("Rambo III");
		alquilada3.setTituloEsp("Rambo III");
		alquilada3.setAnyo(1988);
		alquilada3.setIdSocio(daoMock.getSocioPorUsuario(credencialesSocio.getUsuario()).getIdSocio());
		alquilada3.setEnlace("http://mivideoclub.com/20161115010001000000/ramboiii.avi");
		alquilada3.setTiempoAlquiler(
				new Timestamp(System.currentTimeMillis() - PeliculaAlquilada.DURACION_ALQUILER_MILIS + 60 * 60 * 1000));

		List<PeliculaAlquilada> pendientes = Arrays
				.asList(new PeliculaAlquilada[] { alquilada1, alquilada2, alquilada3 });

		// Inicializamos mock
		when(daoMock.listarPeliculasAlquiladas()).thenReturn(pendientes);

		List<PeliculaAlquilada> recibidas = videoclub.misPeliculasAlquiladas();
		Assert.assertEquals(pendientes, recibidas);
	}

	// E7.1 var.
	@Test
	public void socioConsultaListaPendientesYHaySoloUna() throws ExcepcionNoHayPeliculasPendientes {
		PeliculaAlquilada alquilada1 = new PeliculaAlquilada();
		alquilada1.setTituloVO("Cobra");
		alquilada1.setTituloEsp("Cobra");
		alquilada1.setAnyo(1986);
		alquilada1.setIdSocio(daoMock.getSocioPorUsuario(credencialesSocio.getUsuario()).getIdSocio());
		alquilada1.setEnlace("http://mivideoclub.com/20161113000000000000/cobra.avi");
		alquilada1.setTiempoAlquiler(new Timestamp(
				System.currentTimeMillis() - PeliculaAlquilada.DURACION_ALQUILER_MILIS + 30 * 60 * 60 * 1000));

		List<PeliculaAlquilada> pendientes = Arrays.asList(new PeliculaAlquilada[] { alquilada1 });

		// Inicializamos mock
		when(daoMock.listarPeliculasAlquiladas()).thenReturn(pendientes);

		List<PeliculaAlquilada> recibidas = videoclub.misPeliculasAlquiladas();
		Assert.assertEquals(pendientes, recibidas);
	}

	// E7.2
	@Test
	public void socioConsultaListaPendientesNoHay() throws ExcepcionNoHayPeliculasPendientes {
		// Inicializamos mock
		when(daoMock.listarPeliculasAlquiladas()).thenReturn(new LinkedList<PeliculaAlquilada>());
		// Excepcion esperada
		thrown.expect(ExcepcionNoHayPeliculasPendientes.class);

		videoclub.misPeliculasAlquiladas();
	}

	// E7.3
	@Test
	public void socioConsultaListaPendientesUnaCaducadaTrasChequeo() throws Exception {
		PeliculaAlquilada alquilada1 = new PeliculaAlquilada();
		alquilada1.setTituloVO("Cobra");
		alquilada1.setTituloEsp("Cobra");
		alquilada1.setAnyo(1986);
		alquilada1.setIdSocio(daoMock.getSocioPorUsuario(credencialesSocio.getUsuario()).getIdSocio());
		alquilada1.setEnlace("http://mivideoclub.com/20161113000000000000/cobra.avi");
		alquilada1.setTiempoAlquiler(new Timestamp(
				System.currentTimeMillis() - PeliculaAlquilada.DURACION_ALQUILER_MILIS + 27 * 60 * 60 * 1000));

		PeliculaAlquilada alquilada2 = new PeliculaAlquilada();
		alquilada2.setTituloVO("Escape plan");
		alquilada2.setTituloEsp("Plan de escape");
		alquilada2.setAnyo(2013);
		alquilada2.setIdSocio(daoMock.getSocioPorUsuario(credencialesSocio.getUsuario()).getIdSocio());
		alquilada2.setEnlace("http://mivideoclub.com/20161113100000000000/escapeplan.avi");
		alquilada2.setTiempoAlquiler(new Timestamp(
				System.currentTimeMillis() - PeliculaAlquilada.DURACION_ALQUILER_MILIS + 37 * 60 * 60 * 1000));

		// Alquiler caducado por 2 horas
		PeliculaAlquilada alquilada3 = new PeliculaAlquilada();
		alquilada3.setTituloVO("Rambo III");
		alquilada3.setTituloEsp("Rambo III");
		alquilada3.setAnyo(1988);
		alquilada3.setIdSocio(daoMock.getSocioPorUsuario(credencialesSocio.getUsuario()).getIdSocio());
		alquilada3.setEnlace("http://mivideoclub.com/20161115010001000000/ramboiii.avi");
		alquilada3.setTiempoAlquiler(new Timestamp(
				System.currentTimeMillis() - PeliculaAlquilada.DURACION_ALQUILER_MILIS - 2 * 60 * 60 * 1000));

		List<PeliculaAlquilada> pendientes = Arrays
				.asList(new PeliculaAlquilada[] { alquilada1, alquilada2, alquilada3 });

		// Inicializamos mock
		when(daoMock.listarPeliculasAlquiladas()).thenReturn(pendientes);
		chequeo();

		List<PeliculaAlquilada> recibidas = videoclub.misPeliculasAlquiladas();
		Assert.assertTrue(recibidas.contains(alquilada1));
		Assert.assertTrue(recibidas.contains(alquilada2));
		// La 3 ya no debe estar
		Assert.assertTrue(!recibidas.contains(alquilada3));
	}

	// E7.4
	@Test
	public void socioConsultaListaPendientesUnaCaducadaSinChequeo() throws ExcepcionNoHayPeliculasPendientes {
		PeliculaAlquilada alquilada1 = new PeliculaAlquilada();
		alquilada1.setTituloVO("Cobra");
		alquilada1.setTituloEsp("Cobra");
		alquilada1.setAnyo(1986);
		alquilada1.setIdSocio(daoMock.getSocioPorUsuario(credencialesSocio.getUsuario()).getIdSocio());
		alquilada1.setEnlace("http://mivideoclub.com/20161113000000000000/cobra.avi");
		alquilada1.setTiempoAlquiler(new Timestamp(
				System.currentTimeMillis() - PeliculaAlquilada.DURACION_ALQUILER_MILIS + 27 * 60 * 60 * 1000));

		PeliculaAlquilada alquilada2 = new PeliculaAlquilada();
		alquilada2.setTituloVO("Escape plan");
		alquilada2.setTituloEsp("Plan de escape");
		alquilada2.setAnyo(2013);
		alquilada2.setIdSocio(daoMock.getSocioPorUsuario(credencialesSocio.getUsuario()).getIdSocio());
		alquilada2.setEnlace("http://mivideoclub.com/20161113100000000000/escapeplan.avi");
		alquilada2.setTiempoAlquiler(new Timestamp(
				System.currentTimeMillis() - PeliculaAlquilada.DURACION_ALQUILER_MILIS + 37 * 60 * 60 * 1000));

		// Alquiler caducado por 2 horas
		PeliculaAlquilada alquilada3 = new PeliculaAlquilada();
		alquilada3.setTituloVO("Rambo III");
		alquilada3.setTituloEsp("Rambo III");
		alquilada3.setAnyo(1988);
		alquilada3.setIdSocio(daoMock.getSocioPorUsuario(credencialesSocio.getUsuario()).getIdSocio());
		alquilada3.setEnlace("http://mivideoclub.com/20161115010001000000/ramboiii.avi");
		alquilada3.setTiempoAlquiler(new Timestamp(
				System.currentTimeMillis() - PeliculaAlquilada.DURACION_ALQUILER_MILIS - 2 * 60 * 60 * 1000));

		List<PeliculaAlquilada> pendientes = Arrays
				.asList(new PeliculaAlquilada[] { alquilada1, alquilada2, alquilada3 });

		// Inicializamos mock
		when(daoMock.listarPeliculasAlquiladas()).thenReturn(pendientes);

		// Al no haber realizado el chequeo se deben obtener las tres
		List<PeliculaAlquilada> recibidas = videoclub.misPeliculasAlquiladas();
		Assert.assertEquals(pendientes, recibidas);
	}

	// Simula chequeo de caducidad de alquiler de la lista de peliculas del usuario
	// "Jocker66"
	private void chequeo() throws Exception {
		List<PeliculaAlquilada> alquiladas = videoclub.misPeliculasAlquiladas();
		List<PeliculaAlquilada> chequeada = new LinkedList<>();
		for (PeliculaAlquilada alquilada : alquiladas)
			if (alquilada.getTiempoRestanteMilis() > 0)
				chequeada.add(alquilada);
		when(daoMock.listarPeliculasAlquiladas()).thenReturn(chequeada);
	}

}
