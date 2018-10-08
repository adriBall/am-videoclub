package aceptacion;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import dao.IDAOVideoclub;
import dominio.Credenciales;
import dominio.Genero;
import dominio.Licencia;
import dominio.Pelicula;
import dominio.PeliculaAlquilada;
import dominio.Socio;
import excepciones.ExcepcionAutenticacionIncorrecta;
import excepciones.ExcepcionNoHayCopiasDisponibles;
import excepciones.ExcepcionPeliculaYaAlquilada;
import excepciones.ExcepcionSocioNoExiste;
import excepciones.ExcepcionUsuarioMoroso;
import modelo.IVideoclubAdmin;
import modelo.VideoclubAdmin;
import modelo.VideoclubSocio;

//HU14 y 15, el admin da de baja socios
public class TestAdminBajaSocios {

	private IDAOVideoclub daoMock; // objeto mock que adopta la interfaz de IDAOVideoclub para simular su
									// comportamiento
	private Credenciales credencialesAdmin;
	private IVideoclubAdmin videoclubAdmin;

	@Before
	public void initTest() throws Exception {

		daoMock = Mockito.mock(IDAOVideoclub.class); // creacion del doble de prueba
		videoclubAdmin = new VideoclubAdmin(daoMock); // inyectamos el daoMock en la clase a probar

		// el administrador inicia sesión
		credencialesAdmin = new Credenciales("Kodos86", "ythjfr84", true);
		when(daoMock.comprobarCredenciales(credencialesAdmin.getUsuario(), credencialesAdmin.getContrasenya()))
				.thenReturn(credencialesAdmin);
		videoclubAdmin.iniciarSesion("Kodos86", "ythjfr84");
	}

	// escenario 14.1
	@Test
	public void adminBajaSocio() throws ExcepcionSocioNoExiste {

		// existe un socio no moroso sin pelis pendientes de ver
		Credenciales credencialesSocio = new Credenciales("Santi00", "ergrg48", false);
		Socio socio = new Socio("98765432H", "Santiago Manzano", "Calle Tarragona", 690787844, "santi00@gmail.com",
				credencialesSocio);
		when(daoMock.getSocioPorId("98765432H")).thenReturn(socio);

		videoclubAdmin.darBajaSocio(socio);

		ArgumentCaptor<Socio> paramCaptor = ArgumentCaptor.forClass(Socio.class);
		verify(daoMock, times(1)).deleteSocio(paramCaptor.capture()); // este metodo dara de baja tanto el socio como
																		// sus pelis alquiladas de la bbdd
		when(daoMock.getSocioPorId("98765432H")).thenReturn(null);
		Assert.assertNull(daoMock.getSocioPorId("98765432H"));
	}

	// escenario 14.2
	@SuppressWarnings("deprecation")
	@Test
	public void adminBajaSocioMoroso() throws ExcepcionSocioNoExiste {

		// existe un socio
		Credenciales credencialesSocio = new Credenciales("Jose95", "rvedrvr48", false);
		Socio socio = new Socio("26271856J", "Jose Herrero", "Calle Madrid 12, 2A", 684551259, "jose000@gmail.com",
				credencialesSocio);
		when(daoMock.getSocioPorId("26271856J")).thenReturn(socio);

		// hacemos que el socio sea moroso
		socio.setUltimoPago(new Date(2016, 9, 30));

		// damos de baja el socio
		videoclubAdmin.darBajaSocio(socio);

		ArgumentCaptor<Socio> paramCaptor = new ArgumentCaptor<>();
		verify(daoMock, times(1)).deleteSocio(paramCaptor.capture()); // este metodo dara de baja tanto el socio como
																		// sus pelis alquiladas de la bbdd
		when(daoMock.getSocioPorId("26271856J")).thenReturn(null);
		Assert.assertNull(daoMock.getSocioPorId("26271856J"));
	}

	// escenario 15.1
	@Test
	public void adminBajaSocioConPeliAlquilada() throws ExcepcionSocioNoExiste, ExcepcionAutenticacionIncorrecta,
			ExcepcionPeliculaYaAlquilada, ExcepcionNoHayCopiasDisponibles, ExcepcionUsuarioMoroso {

		// existe un socio
		Credenciales credencialesSocio = new Credenciales("Pepe95", "ytgkiuol770", false);
		Socio socio = new Socio("25654514J", "Francisco Sales", "Avenida Barcelona 12, 2A", 650225974,
				"pepe95@gmail.com", credencialesSocio);
		when(daoMock.getSocioPorId("25654514J")).thenReturn(socio);

		// el socio alquila una pelicula
		String sinopsis = "En 1960 un avión espía estadounidense es derribado por los soviéticos cuando sobrevolaba territorio enemigo. "
				+ "Un abogado es reclutado como encargado de negociar la liberación del piloto.";
		List<Genero> listaGeneros = new ArrayList<Genero>();
		listaGeneros.add(Genero.SUSPENSE);
		List<String> listaActores = new ArrayList<String>();
		listaActores.add("Tom Hanks");
		List<String> listaDirectores = new ArrayList<String>();
		listaDirectores.add("Steven Spielberg");
		Pelicula pelicula = new Pelicula("El puente de los espías", "Bridge of spies", 2015, sinopsis, Licencia.BASICA,
				listaGeneros, listaActores, listaDirectores);
		when(daoMock.getPelicula("Bridge of spies", 2015)).thenReturn(pelicula);

		// socio alquila peli
		videoclubAdmin.cerrarSesion();
		VideoclubSocio videoclubSocio = new VideoclubSocio(daoMock);
		when(daoMock.comprobarCredenciales(credencialesSocio.getUsuario(), credencialesSocio.getContrasenya()))
				.thenReturn(credencialesSocio);
		when(daoMock.getSocioPorUsuario(credencialesSocio.getUsuario())).thenReturn(socio);
		when(daoMock.getSocioPorId(socio.getIdSocio())).thenReturn(socio);
		videoclubSocio.iniciarSesion("Pepe95", "ytgkiuol770");
		videoclubSocio.alquilarPelicula(pelicula);
		pelicula.setCopiasDisponibles(0);
		videoclubSocio.cerrarSesion();
		when(daoMock.comprobarCredenciales(credencialesAdmin.getUsuario(), credencialesAdmin.getContrasenya()))
				.thenReturn(credencialesAdmin);
		videoclubAdmin.iniciarSesion("Kodos86", "ythjfr84");

		// damos de baja el socio
		videoclubAdmin.darBajaSocio(socio);

		ArgumentCaptor<Socio> paramCaptor = ArgumentCaptor.forClass(Socio.class);
		verify(daoMock, times(1)).deleteSocio(paramCaptor.capture()); // este metodo dara de baja tanto el socio como
																		// sus pelis alquiladas de la bbdd
		when(daoMock.getSocioPorId("25654514J")).thenReturn(null);
		when(daoMock.getPeliculasAlquiladas(credencialesSocio)).thenReturn(new ArrayList<PeliculaAlquilada>());
		when(daoMock.getNumeroCopiasDisponibles(pelicula)).thenReturn(1);
		Assert.assertNull(daoMock.getSocioPorId("25654514J"));
		Assert.assertEquals(daoMock.getPeliculasAlquiladas(credencialesSocio), new ArrayList<PeliculaAlquilada>());
		Assert.assertEquals(daoMock.getNumeroCopiasDisponibles(pelicula), (Integer) 1);

	}

}
