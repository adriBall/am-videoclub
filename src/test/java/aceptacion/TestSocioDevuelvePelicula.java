package aceptacion;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import dao.IDAOVideoclub;
import dominio.Credenciales;
import dominio.Pelicula;
import dominio.PeliculaAlquilada;
import dominio.Socio;
import modelo.IVideoclubSocio;
import modelo.VideoclubSocio;

// HU02 junto con HU27, utilizando datos de HU27 (Hemos fusionado las 2 HU despues de la correccion)
public class TestSocioDevuelvePelicula {
	private IVideoclubSocio videoclub;
	private IDAOVideoclub daoMock;
	private Credenciales credencialesSocio;

	// Inicializamos antes de cada test
	@Before
	public void initTest() throws Exception {
		daoMock = Mockito.mock(IDAOVideoclub.class);
		videoclub = new VideoclubSocio(daoMock);

		credencialesSocio = new Credenciales();
		credencialesSocio.setUsuario("Santi00");
		credencialesSocio.setContrasenya("cualquiera");
		credencialesSocio.setAdmin(false);
		when(daoMock.comprobarCredenciales(credencialesSocio.getUsuario(), credencialesSocio.getContrasenya()))
				.thenReturn(credencialesSocio);
		videoclub.iniciarSesion("Santi00", "cualquiera");

		Socio socio = new Socio();
		socio.setIdSocio("12523657F");
		when(daoMock.getSocioPorUsuario(credencialesSocio.getUsuario())).thenReturn(socio);

		PeliculaAlquilada peliculaAlquilada = new PeliculaAlquilada();
		peliculaAlquilada.setTituloVO("Blade Runner");
		peliculaAlquilada.setAnyo(1982);
		peliculaAlquilada.setIdSocio("12523657F");
		List<PeliculaAlquilada> peliculasPendientes = new ArrayList<>();
		peliculasPendientes.add(peliculaAlquilada);
		when(daoMock.listarPeliculasAlquiladas()).thenReturn(peliculasPendientes);

		Pelicula pelicula = new Pelicula();
		pelicula.setTituloVO("Blade Runner");
		pelicula.setAnyo(1982);
		pelicula.setCopiasDisponibles(0);
	}

	// E2.1
	@Test
	public void socioDevuelvePelicula() {

		PeliculaAlquilada alquilada = daoMock.listarPeliculasAlquiladas().get(0);
		videoclub.devolverPelicula(alquilada);

		// Verificamos que se ha producido la llamada correcta y recuperamos el
		// parametro
		ArgumentCaptor<PeliculaAlquilada> paramCaptor = ArgumentCaptor.forClass(PeliculaAlquilada.class);
		verify(daoMock, times(1)).removePeliculaAlquilada(paramCaptor.capture());
		PeliculaAlquilada alquiladaDevuelta = paramCaptor.getValue();

		// Se ha devuelto la pelicula alquilada "Blade Runner" de 1982 para el socio
		// correspondente
		Assert.assertEquals(alquilada, alquiladaDevuelta);

		// No comprobamos el número de copias disponibles porque lo generamos
		// dinámicamente a partir de la BD
	}

}
