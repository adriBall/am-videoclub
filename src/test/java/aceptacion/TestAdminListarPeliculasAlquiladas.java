package aceptacion;

import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;
import org.mockito.Mockito;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static util.Fechas.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dao.IDAOVideoclub;
import dominio.Credenciales;
import dominio.PeliculaAlquilada;
import dominio.Socio;
import modelo.IVideoclubAdmin;
import modelo.VideoclubAdmin;

//HU23
public class TestAdminListarPeliculasAlquiladas {
	private IVideoclubAdmin videoclub;
	private IDAOVideoclub daoMock;
	private Credenciales credencialesAdmin;

	// Inicializamos el test
	@Before
	public void initTest() throws Exception {
		daoMock = Mockito.mock(IDAOVideoclub.class);
		videoclub = new VideoclubAdmin(daoMock);

		credencialesAdmin = new Credenciales();
		credencialesAdmin.setUsuario("Kodos86");
		credencialesAdmin.setContrasenya("cualquiera");
		credencialesAdmin.setAdmin(true);
		when(daoMock.comprobarCredenciales(credencialesAdmin.getUsuario(), credencialesAdmin.getContrasenya()))
				.thenReturn(credencialesAdmin);
		videoclub.iniciarSesion("Kodos86", "cualquiera");

		Credenciales credencialesDavid = new Credenciales("David96", "cualquiera", false);
		Socio david = new Socio("65476543D", "David", "Una calle", 678564321, "david@david.es", credencialesDavid);
		when(daoMock.getSocioPorId("65476543D")).thenReturn(david);
		when(daoMock.getSocioPorUsuario("David96")).thenReturn(david);
		david.setUltimoPago(getDate(diasMes(mes(hoy()), anyo(hoy())), mes(hoy()), anyo(hoy()) - 1));

		Credenciales credencialesAlba = new Credenciales("Alba95", "cualquiera", false);
		Socio alba = new Socio("23671894D", "Alba", "Una calle", 673849208, "alba@alba.es", credencialesAlba);
		when(daoMock.getSocioPorId("23671894D")).thenReturn(alba);
		when(daoMock.getSocioPorUsuario("Alba95")).thenReturn(alba);
	}

	// 23.1
	@Test
	public void testListarPeliculasAlquiladasCuandoNoHay() {
		when(daoMock.listarPeliculasAlquiladas()).thenReturn(new ArrayList<>());

		List<PeliculaAlquilada> alquiladas = videoclub.listarPeliculasAlquiladas();
		assertTrue(alquiladas.isEmpty());
	}

	// 23.2
	@Test
	public void testListarPeliculasAlquiladasCuandoHay() {
		PeliculaAlquilada alquilada1 = new PeliculaAlquilada();
		alquilada1.setIdSocio("65476543D");
		alquilada1.setTituloEsp("Face / Off");
		alquilada1.setTituloVO("Face / Off");
		alquilada1.setAnyo(1997);

		PeliculaAlquilada alquilada2 = new PeliculaAlquilada();
		alquilada2.setIdSocio("23671894D");
		alquilada2.setTituloEsp("El padrino");
		alquilada2.setTituloVO("The godfather");
		alquilada2.setAnyo(1972);

		PeliculaAlquilada alquilada3 = new PeliculaAlquilada();
		alquilada3.setIdSocio("23671894D");
		alquilada3.setTituloEsp("Dracula, de Bram Stocker");
		alquilada3.setTituloVO("Bram Stoker’s Dracula");
		alquilada3.setAnyo(1992);

		List<PeliculaAlquilada> alquiladas = Arrays
				.asList(new PeliculaAlquilada[] { alquilada1, alquilada2, alquilada3 });
		when(daoMock.listarPeliculasAlquiladas()).thenReturn(alquiladas);

		List<PeliculaAlquilada> devueltas = videoclub.listarPeliculasAlquiladas();
		Assert.assertEquals(alquiladas, devueltas);
	}

}