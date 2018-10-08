package aceptacion;

import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.List;
import dao.IDAOVideoclub;
import dominio.Credenciales;
import dominio.Genero;
import dominio.Licencia;
import dominio.Pelicula;
import dominio.PeliculaAlquilada;
import dominio.Socio;
import excepciones.ExcepcionAutenticacionIncorrecta;
import excepciones.ExcepcionNoHayCopiasDisponibles;
import excepciones.ExcepcionPeliculaNoExiste;
import excepciones.ExcepcionPeliculaYaAlquilada;
import excepciones.ExcepcionUsuarioMoroso;
import modelo.IVideoclubAdmin;
import modelo.IVideoclubSocio;
import modelo.VideoclubAdmin;
import modelo.VideoclubSocio;

//HU21, el admin da de baja peliculas
public class TestAdminBajaPeliculas {

	private IDAOVideoclub daoMock; // objeto mock que adopta la interfaz de IDAOVideoclub para simular su
									// comportamiento
	private Credenciales credencialesAdmin;
	private IVideoclubAdmin videoclubAdmin;
	private IVideoclubSocio videoclubSocio;
	private Pelicula pelicula; // la peli a borrar
	private Pelicula pelicula2;
	private Socio socio; // el socio que tiene la peli en sus pendientes

	@Before
	public void initTest() throws Exception {

		daoMock = Mockito.mock(IDAOVideoclub.class); // creacion del doble de prueba
		videoclubAdmin = new VideoclubAdmin(daoMock); // inyectamos el daoMock en la clase a probar
		videoclubSocio = new VideoclubSocio(daoMock);
		credencialesAdmin = new Credenciales("Heisenberg92", "eredf48", true);
		when(daoMock.comprobarCredenciales(credencialesAdmin.getUsuario(), credencialesAdmin.getContrasenya()))
				.thenReturn(credencialesAdmin);

		Credenciales credencialesSocio = new Credenciales("Adri95", "jun8745", false);
		socio = new Socio("20145896L", "Adrian Martinez", "plaza de la paz 1", 650441122, "adri95@gmail.com",
				credencialesSocio);

		String sinopsis = "Conjunto de varias historias que se desarrollan en el pasado, el presente y el futuro. "
				+ "Cada una de ellas está contenida en la anterior, y todas están enlazadas entre sí por pequeños "
				+ "detalles.";
		List<Genero> listaGeneros = new ArrayList<Genero>();
		listaGeneros.add(Genero.DRAMA);
		listaGeneros.add(Genero.CIENCIA_FICCION);
		List<String> listaActores = new ArrayList<String>();
		listaActores.add("Tom Hanks");
		List<String> listaDirectores = new ArrayList<String>();
		listaDirectores.add("Tom Tykwer");
		listaDirectores.add("Lana Wachowski");
		pelicula = new Pelicula("El atlas de las nubes", "Cloud Atlas", 2012, sinopsis, Licencia.BASICA, listaGeneros,
				listaActores, listaDirectores);

		when(daoMock.getPelicula("Cloud Atlas", 2012)).thenReturn(pelicula);

		String sinopsis2 = "En el verano de 1954, los agentes judiciales Teddy Daniels y Chuck Aule son destinados "
				+ "a una remota isla del puerto de Boston para investigar la desaparición de una peligrosa asesina.";
		List<Genero> listaGeneros2 = new ArrayList<Genero>();
		listaGeneros2.add(Genero.SUSPENSE);
		List<String> listaActores2 = new ArrayList<String>();
		listaActores2.add("Leonardo DiCaprio");
		List<String> listaDirectores2 = new ArrayList<String>();
		listaDirectores2.add("Martin Scorsese");
		pelicula2 = new Pelicula("Shutter Island", "Shutter Island", 2010, sinopsis2, Licencia.BASICA, listaGeneros2,
				listaActores2, listaDirectores2);

		when(daoMock.getPelicula("Shutter Island", 2010)).thenReturn(pelicula2);
	}

	// 21.1a
	@Test
	public void adminDabajaPeliA() throws ExcepcionPeliculaNoExiste, ExcepcionAutenticacionIncorrecta,
			ExcepcionPeliculaYaAlquilada, ExcepcionNoHayCopiasDisponibles, ExcepcionUsuarioMoroso {

		// socio alquila peli
		when(daoMock.comprobarCredenciales(socio.getCredenciales().getUsuario(),
				socio.getCredenciales().getContrasenya())).thenReturn(socio.getCredenciales());
		when(daoMock.getSocioPorUsuario(socio.getCredenciales().getUsuario())).thenReturn(socio);
		when(daoMock.getSocioPorId(socio.getIdSocio())).thenReturn(socio);
		videoclubSocio.iniciarSesion("Adri95", "jun8745");
		videoclubSocio.alquilarPelicula(pelicula);
		videoclubSocio.cerrarSesion();
		videoclubAdmin.iniciarSesion("Heisenberg92", "eredf48");

		videoclubAdmin.darBajaPelicula(pelicula);

		// Comprobamos que se producen las llamadas
		ArgumentCaptor<Pelicula> paramCaptor = ArgumentCaptor.forClass(Pelicula.class);
		verify(daoMock, times(1)).deletePelicula(paramCaptor.capture()); // este metodo dara de baja tanto la peli como
																			// las reservas de esta
		when(daoMock.getPelicula("Cloud Atlas", 2012)).thenReturn(null);
		when(daoMock.getPeliculasAlquiladas(socio.getCredenciales())).thenReturn(new ArrayList<PeliculaAlquilada>());
		Assert.assertNull(daoMock.getPelicula("Cloud Atlas", 2012));
		Assert.assertEquals(daoMock.getPeliculasAlquiladas(socio.getCredenciales()),
				new ArrayList<PeliculaAlquilada>());// este metodo buscara en la bbdd peliculas alquiladas donde el
													// idsocio coincida
	}

	// 21.1b
	@Test
	public void adminDabajaPeliB() throws ExcepcionPeliculaNoExiste, ExcepcionAutenticacionIncorrecta,
			ExcepcionPeliculaYaAlquilada, ExcepcionNoHayCopiasDisponibles, ExcepcionUsuarioMoroso {
		// creo otro socio que tambien tiene la peli
		Credenciales credencialesSocio2 = new Credenciales("Rober78", "sdfcf99", false);
		Socio socio2 = new Socio("23845899A", "Roberto Castillo", "calle mayor 2", 680457812, "rob78@gmail.com",
				credencialesSocio2);

		// los socios alquilan la peli
		when(daoMock.comprobarCredenciales(socio.getCredenciales().getUsuario(),
				socio.getCredenciales().getContrasenya())).thenReturn(socio.getCredenciales());
		when(daoMock.getSocioPorUsuario(socio.getCredenciales().getUsuario())).thenReturn(socio);
		when(daoMock.getSocioPorId(socio.getIdSocio())).thenReturn(socio);
		videoclubSocio.iniciarSesion("Adri95", "jun8745");
		videoclubSocio.alquilarPelicula(pelicula);
		videoclubSocio.cerrarSesion();
		when(daoMock.comprobarCredenciales(socio2.getCredenciales().getUsuario(),
				socio2.getCredenciales().getContrasenya())).thenReturn(socio2.getCredenciales());
		when(daoMock.getSocioPorUsuario(socio2.getCredenciales().getUsuario())).thenReturn(socio2);
		when(daoMock.getSocioPorId(socio2.getIdSocio())).thenReturn(socio2);
		videoclubSocio.iniciarSesion("Rober78", "sdfcf99");
		videoclubSocio.alquilarPelicula(pelicula);
		videoclubSocio.cerrarSesion();
		videoclubAdmin.iniciarSesion("Heisenberg92", "eredf48");

		videoclubAdmin.darBajaPelicula(pelicula);

		// Comprobamos que se producen las llamadas
		ArgumentCaptor<Pelicula> paramCaptor = ArgumentCaptor.forClass(Pelicula.class);
		verify(daoMock, times(1)).deletePelicula(paramCaptor.capture()); // este metodo dara de baja tanto la peli como
																			// las reservas de esta
		when(daoMock.getPelicula("Cloud Atlas", 2012)).thenReturn(null);
		when(daoMock.getPeliculasAlquiladas(socio.getCredenciales())).thenReturn(new ArrayList<PeliculaAlquilada>());
		when(daoMock.getPeliculasAlquiladas(socio2.getCredenciales())).thenReturn(new ArrayList<PeliculaAlquilada>());
		Assert.assertNull(daoMock.getPelicula("Cloud Atlas", 2012));
		Assert.assertEquals(daoMock.getPeliculasAlquiladas(socio.getCredenciales()),
				new ArrayList<PeliculaAlquilada>());
		Assert.assertEquals(daoMock.getPeliculasAlquiladas(socio2.getCredenciales()),
				new ArrayList<PeliculaAlquilada>());
	}

	// 21.2
	@Test
	public void adminDabajaPeliNoAlquilada() throws ExcepcionPeliculaNoExiste, ExcepcionAutenticacionIncorrecta {

		videoclubAdmin.iniciarSesion("Heisenberg92", "eredf48");
		// nadie alquila la peli
		videoclubAdmin.darBajaPelicula(pelicula);

		// Comprobamos que se producen las llamadas
		ArgumentCaptor<Pelicula> paramCaptor = ArgumentCaptor.forClass(Pelicula.class);
		verify(daoMock, times(1)).deletePelicula(paramCaptor.capture()); // este metodo dara de baja tanto la peli como
																			// las reservas de esta
		when(daoMock.getPelicula("Shutter Island", 2010)).thenReturn(null);

		Assert.assertNull(daoMock.getPelicula("Shutter Island", 2010));

	}

}