package aceptacion;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import dao.IDAOVideoclub;
import dominio.Credenciales;
import dominio.Genero;
import dominio.Licencia;
import dominio.Pelicula;
import dominio.PeliculaAlquilada;
import dominio.Socio;
import modelo.IVideoclubAdmin;
import modelo.IVideoclubSocio;
import modelo.VideoclubAdmin;
import modelo.VideoclubSocio;

//HU 24 el administrador elimina las reservas de peliculas caducadas
public class TestAdminBorrarReservasCaducadas {

	private IDAOVideoclub daoMock; // objeto mock que adopta la interfaz de IDAOVideoclub para simular su
									// comportamiento
	private Credenciales credencialesAdmin;
	private IVideoclubAdmin videoclubAdmin;
	private IVideoclubSocio videoclubSocio;
	private Pelicula pelicula;
	private Pelicula pelicula2;
	private Pelicula pelicula3;
	private PeliculaAlquilada peliAlquilada;
	private PeliculaAlquilada peliAlquilada2;
	private PeliculaAlquilada peliAlquilada3;
	private Socio socio;
	private Socio socio2;
	private Socio socio3;

	@Before
	public void initTest() throws Exception {

		daoMock = Mockito.mock(IDAOVideoclub.class); // creacion del doble de prueba
		videoclubAdmin = new VideoclubAdmin(daoMock); // inyectamos el daoMock en la clase a probar
		videoclubSocio = new VideoclubSocio(daoMock);

		// tenemos 3 peliculas
		String sinopsis = "La cinta, que muestra el humor corrosivo de los Python, ya un sello de marca, "
				+ "es una serie de sketches que pretenden, de una manera satírica, mostrar las "
				+ "diferentes etapas de la vida y llegar a la conclusión sobre el sentido de la misma.";
		List<Genero> listaGeneros = new ArrayList<Genero>();
		listaGeneros.add(Genero.COMEDIA);
		List<String> listaActores = new ArrayList<String>();
		listaActores.add("Michael Palin");
		listaActores.add("Eric Idle");
		listaActores.add("Graham Chapman");
		List<String> listaDirectores = new ArrayList<String>();
		listaDirectores.add("Terry Jones");
		listaDirectores.add("Lana Wachowski");
		pelicula = new Pelicula("El sentido de la vida", "Monty Python's The Meaning of Life", 1983, sinopsis,
				Licencia.BASICA, listaGeneros, listaActores, listaDirectores);
		pelicula.setCopiasDisponibles(9);
		when(daoMock.getPelicula("Monty Python's The Meaning of Life", 1983)).thenReturn(pelicula);

		String sinopsis2 = "Kevin Flynn es un programador de videojuegos que trabajaba para la empresa Encom hasta que fue engañado por un ejecutivo. Dos empleados que también han "
				+ "sido manipulados por él, piden ayuda a Flynn para acceder a los datos del sistema informático de la empresa.";
		List<Genero> listaGeneros2 = new ArrayList<Genero>();
		listaGeneros2.add(Genero.CIENCIA_FICCION);
		listaGeneros2.add(Genero.ACCION);
		listaGeneros2.add(Genero.AVENTURA);
		List<String> listaActores2 = new ArrayList<String>();
		listaActores2.add("Jeff Bridges");
		listaActores2.add("David Warner");
		listaActores2.add("Bruce Boxleitner");
		List<String> listaDirectores2 = new ArrayList<String>();
		listaDirectores2.add("Steven Lisberger");
		pelicula2 = new Pelicula("Tron", "Tron", 1982, sinopsis2, Licencia.BASICA, listaGeneros2, listaActores2,
				listaDirectores2);
		when(daoMock.getPelicula("Tron", 1982)).thenReturn(pelicula2);

		String sinopsis3 = "Jack Torrance es un hombre casado y con un hijo, Danny. Un día, Jack acepta un puesto como vigilante en un hotel. "
				+ "El trabajo consiste en pasar todo el invierno allí con su familia, cuidando del recinto, pero lo que no sospechan es que "
				+ "sus vidas van a cambiar en cuanto crucen la puerta del hotel.";
		List<Genero> listaGeneros3 = new ArrayList<Genero>();
		listaGeneros3.add(Genero.TERROR);
		List<String> listaActores3 = new ArrayList<String>();
		listaActores3.add("Jack Nicholson");
		List<String> listaDirectores3 = new ArrayList<String>();
		listaDirectores3.add("Stanley Kubrick");
		pelicula3 = new Pelicula("El resplandor", "The Shining", 1980, sinopsis3, Licencia.BASICA, listaGeneros3,
				listaActores3, listaDirectores3);
		when(daoMock.getPelicula("The Shining", 1980)).thenReturn(pelicula3);

		// creamos socios, que inician sesion y alquilan las pelis, luego cambiamos el
		// timepo restante
		//// 1
		Credenciales credencialesSocio = new Credenciales("Santi00", "yfgjgfygj78", false);
		socio = new Socio("98765432H", "Santiago Valle", "calle mayor 3", 650441289, "santi00@gmail.com",
				credencialesSocio);
		when(daoMock.comprobarCredenciales(socio.getCredenciales().getUsuario(),
				socio.getCredenciales().getContrasenya())).thenReturn(socio.getCredenciales());
		when(daoMock.getSocioPorUsuario(socio.getCredenciales().getUsuario())).thenReturn(socio);
		when(daoMock.getSocioPorId(socio.getIdSocio())).thenReturn(socio);
		videoclubSocio.iniciarSesion("Santi00", "yfgjgfygj78");
		videoclubSocio.alquilarPelicula(pelicula);

		peliAlquilada = new PeliculaAlquilada();
		peliAlquilada.setAnyo(1983);
		peliAlquilada.setIdSocio(socio.getIdSocio());
		peliAlquilada.setTiempoAlquiler(new Timestamp(System.currentTimeMillis() - (24 * 60 * 60 * 1000L)));

		when(daoMock.getPeliculaAlquilada(credencialesSocio, "Monty Python's The Meaning of Life", 1983))
				.thenReturn(peliAlquilada);
		videoclubSocio.cerrarSesion();

		//// 2
		Credenciales credencialesSocio2 = new Credenciales("Adri95", "jun8745", false);
		socio2 = new Socio("20145896L", "Adrian Martinez", "plaza de la paz 1", 650441122, "adri95@gmail.com",
				credencialesSocio2);
		when(daoMock.comprobarCredenciales("Adri95", "jun8745")).thenReturn(socio2.getCredenciales());
		when(daoMock.getSocioPorUsuario(socio2.getCredenciales().getUsuario())).thenReturn(socio2);
		when(daoMock.getSocioPorId(socio2.getIdSocio())).thenReturn(socio2);
		videoclubSocio.iniciarSesion("Adri95", "jun8745");
		videoclubSocio.alquilarPelicula(pelicula2);

		peliAlquilada2 = new PeliculaAlquilada();
		peliAlquilada2.setAnyo(1982);
		peliAlquilada2.setIdSocio(socio2.getIdSocio());
		peliAlquilada2.setTiempoAlquiler(new Timestamp(System.currentTimeMillis() - (38 * 60 * 60 * 1000L)));

		when(daoMock.getPeliculaAlquilada(socio2.getCredenciales(), "Tron", 1982)).thenReturn(peliAlquilada2);
		videoclubSocio.cerrarSesion();

		//// 3
		Credenciales credencialesSocio3 = new Credenciales("Fanny95", "yjjgjty48", false);
		socio3 = new Socio("25656544", "Fanny Hernandez", "calle rafalafena 2", 680778459, "fanny95@gmail.com",
				credencialesSocio3);
		when(daoMock.comprobarCredenciales("Fanny95", "yjjgjty48")).thenReturn(socio3.getCredenciales());
		when(daoMock.getSocioPorUsuario(socio3.getCredenciales().getUsuario())).thenReturn(socio3);
		when(daoMock.getSocioPorId(socio3.getIdSocio())).thenReturn(socio3);
		videoclubSocio.iniciarSesion("Fanny95", "yjjgjty48");
		videoclubSocio.alquilarPelicula(pelicula3);

		peliAlquilada3 = new PeliculaAlquilada();
		peliAlquilada3.setAnyo(1980);
		peliAlquilada3.setIdSocio(socio3.getIdSocio());
		peliAlquilada3.setTiempoAlquiler(new Timestamp(System.currentTimeMillis() - (47 * 60 * 60 * 1000L)));

		when(daoMock.getPeliculaAlquilada(socio3.getCredenciales(), "The shining", 1980)).thenReturn(peliAlquilada3);
		videoclubSocio.cerrarSesion();

		// el administrador inicia sesión
		credencialesAdmin = new Credenciales("Kodos86", "ythjfr84", true);
		when(daoMock.comprobarCredenciales(credencialesAdmin.getUsuario(), credencialesAdmin.getContrasenya()))
				.thenReturn(credencialesAdmin);
		videoclubAdmin.iniciarSesion("Kodos86", "ythjfr84");

	}

	// 24.1
	@Test
	public void AdminChequeaReservas() {
		videoclubAdmin.chequearAntiguedadAlquileres();

		// Comprobamos que se producen las llamadas
		verify(daoMock, times(1)).deleteReservasCaducadas(); // este metodo dara de baja tanto la peli como las reservas
																// de esta

		// compruebo que las pelis alquiladas aun existen
		Assert.assertEquals(
				daoMock.getPeliculaAlquilada(socio.getCredenciales(), "Monty Python's The Meaning of Life", 1983),
				peliAlquilada);
		Assert.assertEquals(daoMock.getPeliculaAlquilada(socio2.getCredenciales(), "Tron", 1982), peliAlquilada2);
		Assert.assertEquals(daoMock.getPeliculaAlquilada(socio3.getCredenciales(), "The shining", 1980),
				peliAlquilada3);
	}

	// 24.2a
	@Test
	public void AdminChequeaReservasCaducadasA() {

		peliAlquilada.setTiempoAlquiler(new Timestamp(System.currentTimeMillis() - (48 * 60 * 60 * 1000L)));

		videoclubAdmin.chequearAntiguedadAlquileres();

		// Comprobamos que se producen las llamadas
		verify(daoMock, times(1)).deleteReservasCaducadas(); // este metodo dara de baja tanto la peli como las reservas
																// de esta

		pelicula.setCopiasDisponibles(pelicula.getCopiasDisponibles() + 1);
		when(daoMock.getNumeroCopiasDisponibles(pelicula)).thenReturn(pelicula.getCopiasDisponibles());

		when(daoMock.getPeliculaAlquilada(socio.getCredenciales(), "Monty Python's The Meaning of Life", 1983))
				.thenReturn(null);

		// compruebo los cambios
		Assert.assertNull(
				daoMock.getPeliculaAlquilada(socio.getCredenciales(), "Monty Python's The Meaning of Life", 1983));
		Assert.assertEquals(daoMock.getPeliculaAlquilada(socio2.getCredenciales(), "Tron", 1982), peliAlquilada2);
		Assert.assertEquals(daoMock.getPeliculaAlquilada(socio3.getCredenciales(), "The shining", 1980),
				peliAlquilada3);
	}

	// 24.2b
	@Test
	public void AdminChequeaReservasCaducadasB() {

		peliAlquilada.setTiempoAlquiler(new Timestamp(System.currentTimeMillis() - (48 * 60 * 60 * 1000L)));
		peliAlquilada2.setTiempoAlquiler(new Timestamp(System.currentTimeMillis() - (50 * 60 * 60 * 1000L)));
		peliAlquilada3.setTiempoAlquiler(new Timestamp(System.currentTimeMillis() - (60 * 60 * 60 * 1000L)));
		when(daoMock.getPeliculaAlquilada(socio.getCredenciales(), "Monty Python's The Meaning of Life", 1983))
				.thenReturn(null);
		when(daoMock.getPeliculaAlquilada(socio2.getCredenciales(), "Tron", 1982)).thenReturn(null);
		when(daoMock.getPeliculaAlquilada(socio3.getCredenciales(), "The shining", 1980)).thenReturn(null);

		videoclubAdmin.chequearAntiguedadAlquileres();

		// Comprobamos que se producen las llamadas
		verify(daoMock, times(1)).deleteReservasCaducadas(); // este metodo dara de baja tanto la peli como las reservas
																// de esta

		// compruebo que las pelis alquiladas no existen
		Assert.assertNull(
				daoMock.getPeliculaAlquilada(socio.getCredenciales(), "Monty Python's The Meaning of Life", 1983));
		Assert.assertNull(daoMock.getPeliculaAlquilada(socio2.getCredenciales(), "Tron", 1982));
		Assert.assertNull(daoMock.getPeliculaAlquilada(socio3.getCredenciales(), "The shining", 1980));
	}

}
