package aceptacion;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import dao.IDAOVideoclub;
import dominio.Credenciales;
import dominio.Pelicula;
import dominio.PeliculaAlquilada;
import dominio.Socio;
import excepciones.ExcepcionNoHayCopiasDisponibles;
import excepciones.ExcepcionPeliculaYaAlquilada;
import modelo.IVideoclubSocio;
import modelo.VideoclubSocio;

// HU01 junto con HU26, utilizando datos de HU01 (Hemos fusionado las 2 HU despues de la correccion)
// En las pruebas de aceptacion olvidamos la de que un usuario moroso no puede alquilar
// pero en el programa sí se tiene en cuenta
public class TestSocioAlquilaPelicula {
	private IVideoclubSocio videoclub;
	private IDAOVideoclub daoMock;
	private Credenciales credencialesSocio;

	private static final Long MILLISECONDS_HOUR = 1000L * 3600L;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	// Inicializamos antes de cada test
	@Before
	public void initTest() throws Exception {
		daoMock = Mockito.mock(IDAOVideoclub.class);
		videoclub = new VideoclubSocio(daoMock);

		credencialesSocio = new Credenciales();
		credencialesSocio.setUsuario("Javier83");
		credencialesSocio.setContrasenya("cualquiera");
		credencialesSocio.setAdmin(false);
		when(daoMock.comprobarCredenciales(credencialesSocio.getUsuario(), credencialesSocio.getContrasenya()))
				.thenReturn(credencialesSocio);

		Socio socio = new Socio();
		socio.setIdSocio("23671287G");
		when(daoMock.getSocioPorUsuario(credencialesSocio.getUsuario())).thenReturn(socio);
		when(daoMock.getSocioPorId(socio.getIdSocio())).thenReturn(socio);
		videoclub.iniciarSesion("Javier83", "cualquiera");

		PeliculaAlquilada peliculaAlquilada1 = new PeliculaAlquilada();
		peliculaAlquilada1.setTituloVO("El ultimo gran heroe");
		peliculaAlquilada1.setAnyo(1993);
		peliculaAlquilada1.setIdSocio("23671287G");
		PeliculaAlquilada peliculaAlquilada2 = new PeliculaAlquilada();
		peliculaAlquilada2.setTituloVO("Terminator");
		peliculaAlquilada2.setAnyo(1984);
		peliculaAlquilada2.setIdSocio("23671287G");
		PeliculaAlquilada peliculaAlquilada3 = new PeliculaAlquilada();
		peliculaAlquilada3.setTituloVO("Regreso al futuro");
		peliculaAlquilada3.setAnyo(1985);
		peliculaAlquilada3.setIdSocio("23671287G");
		List<PeliculaAlquilada> peliculasPendientes = new ArrayList<>();
		peliculasPendientes.add(peliculaAlquilada1);
		peliculasPendientes.add(peliculaAlquilada2);
		peliculasPendientes.add(peliculaAlquilada3);
		when(daoMock.listarPeliculasAlquiladas()).thenReturn(peliculasPendientes);

		Pelicula pelicula1 = new Pelicula();
		pelicula1.setTituloVO("Misión imposible");
		pelicula1.setAnyo(1996);
		pelicula1.setCopiasDisponibles(2);
		Pelicula pelicula2 = new Pelicula();
		pelicula2.setTituloVO("Regreso al futuro");
		pelicula2.setAnyo(1985);
		pelicula2.setCopiasDisponibles(1);
		Pelicula pelicula3 = new Pelicula();
		pelicula3.setTituloVO("El padrino");
		pelicula3.setAnyo(1972);
		pelicula3.setCopiasDisponibles(0);
		when(daoMock.listarPeliculasVideoclub())
				.thenReturn(Arrays.asList(new Pelicula[] { pelicula1, pelicula2, pelicula3 }));
		when(daoMock.getPelicula("Misión imposible", 1996)).thenReturn(pelicula1);
		when(daoMock.getPelicula("Regreso al futuro", 1985)).thenReturn(pelicula2);
		when(daoMock.getPelicula("El padrino", 1972)).thenReturn(pelicula3);
	}

	// E1.1
	@Test
	public void socioAlquilaPeliculaNoPendienteDisponible() throws Exception {

		Pelicula pelicula = daoMock.getPelicula("Misión imposible", 1996);
		videoclub.alquilarPelicula(pelicula);

		// Verificamos que se ha producido la llamada correcta y recuperamos el
		// parametro
		ArgumentCaptor<PeliculaAlquilada> paramCaptor = ArgumentCaptor.forClass(PeliculaAlquilada.class);
		verify(daoMock, times(1)).addPeliculaAlquilada(paramCaptor.capture());
		PeliculaAlquilada alquilada = paramCaptor.getValue();

		// Se ha anadido una pelicula alquilada de titulo "Mision imposible" de 1996
		// para el socio correspondente
		Mockito.verify(daoMock).addPeliculaAlquilada(alquilada);
		Assert.assertEquals(pelicula.getTituloVO(), alquilada.getTituloVO());
		Assert.assertEquals(pelicula.getAnyo(), alquilada.getAnyo());
		Assert.assertEquals(daoMock.getSocioPorUsuario(credencialesSocio.getUsuario()).getIdSocio(),
				alquilada.getIdSocio());

		// Aun no ha excedido su periodo de validez
		Timestamp momentoAlquiler = alquilada.getTiempoAlquiler();
		Assert.assertEquals(0,
				Math.round((System.currentTimeMillis() - momentoAlquiler.getTime()) / (MILLISECONDS_HOUR)));

		// Se ha generado su enlace
		Assert.assertEquals("http://amvideoclub.com" + "/peliculas" + "/" + Math.abs(momentoAlquiler.hashCode())
				+ alquilada.getTituloVO().replaceAll("\\s+", ""), alquilada.getEnlace());

		// No comprobamos el número de copias disponibles porque lo generamos
		// dinámicamente a partir de la BD
	}

	// E1.2
	@Test
	public void socioAlquilaPeliculaYaPendiente() throws Exception {

		// Excepcion esperada
		thrown.expect(ExcepcionPeliculaYaAlquilada.class);

		Pelicula pelicula = daoMock.getPelicula("Regreso al futuro", 1985);
		videoclub.alquilarPelicula(pelicula);

		// Verificamos que se no ha producido la llamada para anyadirla a la BD
		verify(daoMock, never()).addPeliculaAlquilada(null);

	}

	// E1.3
	@Test
	public void socioAlquilaPeliculaNoDisponible() throws Exception {

		// Excepcion esperada
		thrown.expect(ExcepcionNoHayCopiasDisponibles.class);

		Pelicula pelicula = daoMock.getPelicula("El padrino", 1972);
		videoclub.alquilarPelicula(pelicula);

		// Verificamos que se no ha producido la llamada para anyadirla a la BD
		verify(daoMock, never()).addPeliculaAlquilada(null);

	}

}
