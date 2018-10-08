package aceptacion;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
import dominio.Socio;
import modelo.IVideoclubSocio;
import modelo.VideoclubSocio;

////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////
////////EL ESCENARIO 3.1 DE LA MEMORIA ES AQUI EL 3.4a//////////////
////////EL ESCENARIO 3.2 DE LA MEMORIA ES AQUI EL 3.1d//////////////
////////EL ESCENARIO 3.3 DE LA MEMORIA ES AQUI EL 3.1c//////////////
////////EL ESCENARIO 3.4 DE LA MEMORIA ES AQUI EL 3.2b//////////////
////////EL ESCENARIO 3.5 DE LA MEMORIA ES AQUI EL 3.6e//////////////
////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////

public class TestSocioBusquedaPeliculas {

	private IVideoclubSocio videoclub;
	private IDAOVideoclub daoMock;
	private Credenciales credencialesSocio;
	private Pelicula pelicula1;
	private Pelicula pelicula2;
	private Pelicula pelicula3;
	private Pelicula pelicula4;
	private Pelicula pelicula5;
	private Pelicula pelicula6;
	private Pelicula pelicula7;
	private Pelicula pelicula9;

	@Before
	public void initTest() throws Exception {
		daoMock = Mockito.mock(IDAOVideoclub.class);
		videoclub = new VideoclubSocio(daoMock);

		// el socio
		credencialesSocio = new Credenciales("Laura23", "dererg78", false);
		when(daoMock.comprobarCredenciales(credencialesSocio.getUsuario(), credencialesSocio.getContrasenya()))
				.thenReturn(credencialesSocio);

		Socio socio = new Socio("22147895K", "Laura Moreno", "Calle Tarragona 2", 650223874, "laura23@gmail.com",
				credencialesSocio);
		when(daoMock.getSocioPorId("22147895K")).thenReturn(socio);
		when(daoMock.getSocioPorUsuario(credencialesSocio.getUsuario())).thenReturn(socio);
		videoclub.iniciarSesion("Laura23", "dererg78");

		// las peliculas de la bbdd
		//// 1
		String sinopsis1 = "Los miembros de un comando de la CIA son enviados a Praga con la misión de capturar, durante una recepción en la embajada americana, "
				+ "a un espía enemigo que se dispone a robar un disco que contiene una lista secreta de agentes de Europa central.";
		List<Genero> listaGeneros1 = new ArrayList<Genero>();
		listaGeneros1.add(Genero.ACCION);
		List<String> listaActores1 = new ArrayList<String>();
		listaActores1.add("Tom Cruise");
		List<String> listaDirectores1 = new ArrayList<String>();
		listaDirectores1.add("Brian De Palma");
		pelicula1 = new Pelicula("Misión imposible", "Mission: Impossible", 1996, sinopsis1, Licencia.BASICA,
				listaGeneros1, listaActores1, listaDirectores1);
		pelicula1.setCopiasDisponibles(3);
		when(daoMock.getPelicula("Mission: Impossible", 1996)).thenReturn(pelicula1);

		//// 2
		String sinopsis2 = "Ethan Hunt (Tom Cruise) tiene una nueva misión: debe evitar que un antiguo ex-agente y ahora terrorista "
				+ "internacional llamado Sean Ambrose (Dougray Scott) se haga con un virus mortal y lo lance sobre Australia, lo que causaría millones de víctimas. ";
		List<Genero> listaGeneros2 = new ArrayList<Genero>();
		listaGeneros2.add(Genero.ACCION);
		List<String> listaActores2 = new ArrayList<String>();
		listaActores2.add("Tom Cruise");
		List<String> listaDirectores2 = new ArrayList<String>();
		listaDirectores2.add("John Woo");
		pelicula2 = new Pelicula("Misión imposible II", "Mission: Impossible II", 2000, sinopsis2, Licencia.BASICA,
				listaGeneros2, listaActores2, listaDirectores2);
		pelicula2.setCopiasDisponibles(5);
		when(daoMock.getPelicula("Mission: Impossible II", 2000)).thenReturn(pelicula2);

		//// 3
		String sinopsis3 = "Los Ángeles, año 2029. Las máquinas dominan el mundo. Los rebeldes que luchan contra ellas tienen como líder a John Connor. "
				+ "Para acabar con la rebelión, las máquinas deciden enviar al pasado a un robot cuya misión será eliminar a Sarah Connor, la madre de John, "
				+ "e impedir así su nacimiento.";
		List<Genero> listaGeneros3 = new ArrayList<Genero>();
		listaGeneros3.add(Genero.ACCION);
		List<String> listaActores3 = new ArrayList<String>();
		listaActores3.add("Arnold Schwarzenegger");
		listaActores3.add("Linda Hamilton");
		List<String> listaDirectores3 = new ArrayList<String>();
		listaDirectores3.add("James Cameron");
		pelicula3 = new Pelicula("Terminator", "Terminator", 1984, sinopsis3, Licencia.BASICA, listaGeneros3,
				listaActores3, listaDirectores3);
		pelicula3.setCopiasDisponibles(2);
		when(daoMock.getPelicula("Terminator", 1984)).thenReturn(pelicula3);

		//// 4
		String sinopsis4 = "En 1960 un avión espía estadounidense es derribado por los soviéticos cuando sobrevolaba territorio enemigo. "
				+ "Un abogado es reclutado como encargado de negociar la liberación del piloto.";
		List<Genero> listaGeneros4 = new ArrayList<Genero>();
		listaGeneros4.add(Genero.SUSPENSE);
		List<String> listaActores4 = new ArrayList<String>();
		listaActores4.add("Tom Hanks");
		List<String> listaDirectores4 = new ArrayList<String>();
		listaDirectores4.add("Steven Spielberg");
		pelicula4 = new Pelicula("El puente de los espías", "Bridge of spies", 2015, sinopsis4, Licencia.BASICA,
				listaGeneros4, listaActores4, listaDirectores4);
		pelicula4.setCopiasDisponibles(1);
		when(daoMock.getPelicula("Bridge of spies", 2015)).thenReturn(pelicula4);

		//// 5
		String sinopsis5 = "El día de su boda, una asesina profesional sufre el ataque de algunos miembros de su propia banda, "
				+ "que obedecen las órdenes de Bill, el jefe de la organización criminal. Logra sobrevivir al ataque, aunque queda en coma. "
				+ "Cuatro años después despierta dominada por un gran deseo de venganza.";
		List<Genero> listaGeneros5 = new ArrayList<Genero>();
		listaGeneros5.add(Genero.ACCION);
		List<String> listaActores5 = new ArrayList<String>();
		listaActores5.add("Uma Thurman");
		listaActores5.add("Lucy Liu");
		List<String> listaDirectores5 = new ArrayList<String>();
		listaDirectores5.add("Quentin Tarantino");
		pelicula5 = new Pelicula("Kill Bill", "Kill Bill", 2003, sinopsis5, Licencia.BASICA, listaGeneros5,
				listaActores5, listaDirectores5);
		pelicula5.setCopiasDisponibles(4);
		when(daoMock.getPelicula("Kill Bill", 2003)).thenReturn(pelicula5);

		//// 6
		String sinopsis6 = "Pocos años después de la Guerra de Secesión, una diligencia avanza por el invernal paisaje de Wyoming. Los pasajeros, el cazarrecompensas "
				+ "John Ruth y su fugitiva Daisy Domergue, intentan llegar al pueblo de Red Rock, donde Ruth entregará a Domergue a la justicia.";
		List<Genero> listaGeneros6 = new ArrayList<Genero>();
		listaGeneros6.add(Genero.SUSPENSE);
		List<String> listaActores6 = new ArrayList<String>();
		listaActores6.add("Samuel L. Jackson");
		listaActores6.add("Kurt Russell");
		List<String> listaDirectores6 = new ArrayList<String>();
		listaDirectores6.add("Quentin Tarantino");
		pelicula6 = new Pelicula("Los odiosos ocho", "The Hateful Eight", 2015, sinopsis6, Licencia.BASICA,
				listaGeneros6, listaActores6, listaDirectores6);
		pelicula6.setCopiasDisponibles(7);
		when(daoMock.getPelicula("The Hateful Eight", 2015)).thenReturn(pelicula6);

		//// 7
		String sinopsis7 = "Jules y Vincent, dos asesinos a sueldo con no demasiadas luces, trabajan para el gángster Marsellus Wallace. "
				+ "Vincent le confiesa a Jules que Marsellus le ha pedido que cuide de Mia, su atractiva mujer. "
				+ "Jules le recomienda prudencia porque es muy peligroso sobrepasarse con la novia del jefe. ";
		List<Genero> listaGeneros7 = new ArrayList<Genero>();
		listaGeneros7.add(Genero.ACCION);
		List<String> listaActores7 = new ArrayList<String>();
		listaActores7.add("John Travolta");
		listaActores7.add("Samuel L. Jackson");
		listaActores7.add("Uma Thurman");
		List<String> listaDirectores7 = new ArrayList<String>();
		listaDirectores7.add("Quentin Tarantino");
		pelicula7 = new Pelicula("Pulp Fiction", "Pulp Fiction", 1994, sinopsis7, Licencia.BASICA, listaGeneros7,
				listaActores7, listaDirectores7);
		pelicula1.setCopiasDisponibles(4);
		when(daoMock.getPelicula("Pulp Fiction", 2012)).thenReturn(pelicula7);

		//// 9
		String sinopsis9 = "Cada 5.000 años se abre una puerta entre dos dimensiones. En una dimensión existe el Universo y la vida. "
				+ "En la otra dimensión existe un elemento que no está hecho ni de tierra, ni de fuego, ni de aire, ni de agua, "
				+ "sino que es una anti-energía, la anti-vida: es el quinto elemento.";
		List<Genero> listaGeneros9 = new ArrayList<Genero>();
		listaGeneros9.add(Genero.ACCION);
		listaGeneros9.add(Genero.CIENCIA_FICCION);
		List<String> listaActores9 = new ArrayList<String>();
		listaActores9.add("Bruce Willis");
		listaActores9.add("Milla Jovovich");
		List<String> listaDirectores9 = new ArrayList<String>();
		listaDirectores9.add("Luc Besson");
		pelicula9 = new Pelicula("El quinto elemento", "The Fifth Element", 1997, sinopsis9, Licencia.BASICA,
				listaGeneros9, listaActores9, listaDirectores9);
		pelicula9.setCopiasDisponibles(3);
		when(daoMock.getPelicula("The Fifth Element", 1997)).thenReturn(pelicula9);
	}

	/////////////////////////////////////////////////////////
	///////// 3.1 - ESCENARIOS DE BUSQUEDA POR TITULO////////
	///////////////////////////////////////////////////////
	// 3.1a Busqueda por titulo con resultados
	@Test
	public void adminBuscaPelisPorTituloConResultados() {
		videoclub.buscarPeliculas("El quinto elemento", null, null, null, null);

		List<Pelicula> lista = new ArrayList<Pelicula>();
		lista.add(pelicula9);

		when(daoMock.buscarPeliculas("El quinto elemento", null, null, null, null)).thenReturn(lista);

		verify(daoMock, times(1)).buscarPeliculas("El quinto elemento", null, null, null, null);
		Assert.assertEquals(daoMock.buscarPeliculas("El quinto elemento", null, null, null, null), lista);

	}

	// 3.1b Busqueda por titulo sin resultados
	@Test
	public void adminBuscaPelisPorTituloSinResultados() {
		videoclub.buscarPeliculas("Cantando bajo la lluvia", null, null, null, null);

		List<Pelicula> lista = new ArrayList<Pelicula>();

		when(daoMock.buscarPeliculas("Cantando bajo la lluvia", null, null, null, null)).thenReturn(lista);

		verify(daoMock, times(1)).buscarPeliculas("Cantando bajo la lluvia", null, null, null, null);
		Assert.assertEquals(daoMock.buscarPeliculas("Cantando bajo la lluvia", null, null, null, null), lista);
	}

	// 3.1c Busqueda por palabras del título contenidas en el título de una película
	// existente en el sistema.
	@Test
	public void adminBuscaPelisPorPalabrasContenidasEnTitulo() {
		String sinopsis8 = "Tras eliminar a algunos miembros de la banda que intentaron asesinarla el día de su boda, Mamba Negra intenta acabar con los demás, "
				+ "especialmente con Bill, su antiguo jefe, que la había dado por muerta.";
		List<Genero> listaGeneros8 = new ArrayList<Genero>();
		listaGeneros8.add(Genero.ACCION);
		List<String> listaActores8 = new ArrayList<String>();
		listaActores8.add("Uma Thurman");
		listaActores8.add("David Carradine");
		List<String> listaDirectores8 = new ArrayList<String>();
		listaDirectores8.add("Quentin Tarantino");
		Pelicula pelicula8 = new Pelicula("Kill Bill 2", "Kill Bill 2", 2004, sinopsis8, Licencia.BASICA, listaGeneros8,
				listaActores8, listaDirectores8);
		pelicula8.setCopiasDisponibles(7);
		when(daoMock.getPelicula("Kill Bill 2", 2004)).thenReturn(pelicula8);

		videoclub.buscarPeliculas("Kill Bill", null, null, null, null);
		List<Pelicula> lista = new ArrayList<Pelicula>();
		lista.add(pelicula5);
		lista.add(pelicula8);
		when(daoMock.buscarPeliculas("Kill Bill", null, null, null, null)).thenReturn(lista);

		verify(daoMock, times(1)).buscarPeliculas("Kill Bill", null, null, null, null);
		Assert.assertEquals(daoMock.buscarPeliculas("Kill Bill", null, null, null, null), lista);
	}

	// 3.1d Busqueda por palabras del título que coinciden solo parcialmente con el
	// título de una película existente en el sistema.
	@Test
	public void adminBuscaPelisPorPalabrasParcialmenteContenidasEnTitulo() {
		videoclub.buscarPeliculas("Kill Bill 2", null, "Quentin Tarantino", null, null);

		List<Pelicula> lista = new ArrayList<Pelicula>();

		when(daoMock.buscarPeliculas("Kill Bill 2", null, "Quentin Tarantino", null, null)).thenReturn(lista);

		verify(daoMock, times(1)).buscarPeliculas("Kill Bill 2", null, "Quentin Tarantino", null, null);
		Assert.assertEquals(daoMock.buscarPeliculas("Kill Bill 2", null, "Quentin Tarantino", null, null), lista);
	}

	////////////////////////////////////////////////////////
	///////// 3.2 - ESCENARIOS DE BUSQUEDA POR ACTOR////////
	//////////////////////////////////////////////////////
	// 3.2a Busqueda por actor con resultados
	@Test
	public void adminBuscaPelisPorActorConResultados() {
		videoclub.buscarPeliculas(null, "Uma Thurman", null, null, null);
		List<Pelicula> lista = new ArrayList<Pelicula>();
		lista.add(pelicula5);
		when(daoMock.buscarPeliculas(null, "Uma Thurman", null, null, null)).thenReturn(lista);

		verify(daoMock, times(1)).buscarPeliculas(null, "Uma Thurman", null, null, null);
		Assert.assertEquals(daoMock.buscarPeliculas(null, "Uma Thurman", null, null, null), lista);
	}

	// 3.2b Busqueda por actor sin resultados
	@Test
	public void adminBuscaPelisPorActorSinResultados() {
		videoclub.buscarPeliculas(null, "Sean Connery", null, null, null);
		List<Pelicula> lista = new ArrayList<Pelicula>();
		when(daoMock.buscarPeliculas(null, "Sean Connery", null, null, null)).thenReturn(lista);

		verify(daoMock, times(1)).buscarPeliculas(null, "Sean Connery", null, null, null);
		Assert.assertEquals(daoMock.buscarPeliculas(null, "Sean Connery", null, null, null), lista);

	}

	/////////////////////////////////////////////////////////
	///////// 3.3 - ESCENARIOS DE BUSQUEDA POR DIRECTOR//////
	///////////////////////////////////////////////////////
	// 3.3a Busqueda por director con resultados
	@Test
	public void adminBuscaPelisPorDirectorConResultados() {
		videoclub.buscarPeliculas(null, null, "Quentin Tarantino", null, null);

		List<Pelicula> lista = new ArrayList<Pelicula>();
		lista.add(pelicula5);
		lista.add(pelicula6);
		lista.add(pelicula7);
		when(daoMock.buscarPeliculas(null, null, "Quentin Tarantino", null, null)).thenReturn(lista);

		verify(daoMock, times(1)).buscarPeliculas(null, null, "Quentin Tarantino", null, null);
		Assert.assertEquals(daoMock.buscarPeliculas(null, null, "Quentin Tarantino", null, null), lista);
	}

	// 3.3b Busqueda por director sin resultados
	@Test
	public void adminBuscaPelisPorDirectorSinResultados() {
		videoclub.buscarPeliculas(null, null, "Alfred Hitchcock", null, null);

		List<Pelicula> lista = new ArrayList<Pelicula>();

		when(daoMock.buscarPeliculas(null, null, "Alfred Hitchcock", null, null)).thenReturn(lista);

		verify(daoMock, times(1)).buscarPeliculas(null, null, "Alfred Hitchcock", null, null);
		Assert.assertEquals(daoMock.buscarPeliculas(null, null, "Alfred Hitchcock", null, null), lista);
	}

	/////////////////////////////////////////////////////////
	///////// 3.4 - ESCENARIOS DE BUSQUEDA POR GENERO////////
	///////////////////////////////////////////////////////
	// 3.4a Busqueda por genero con resultados
	@Test
	public void adminBuscaPelisPorGeneroConResultados() {

		videoclub.buscarPeliculas(null, null, null, Genero.ACCION, null);
		List<Pelicula> lista = new ArrayList<Pelicula>();
		lista.add(pelicula1);
		lista.add(pelicula3);
		lista.add(pelicula5);
		lista.add(pelicula7);
		lista.add(pelicula9);
		when(daoMock.buscarPeliculas(null, null, null, Genero.ACCION, null)).thenReturn(lista);

		verify(daoMock, times(1)).buscarPeliculas(null, null, null, Genero.ACCION, null);
		Assert.assertEquals(daoMock.buscarPeliculas(null, null, null, Genero.ACCION, null), lista);
	}

	/////////////////////////////////////////////////////////
	///////// 3.5 - ESCENARIOS DE BUSQUEDA POR ANYO DE ESTRENO/////
	///////////////////////////////////////////////////////
	// 3.5a Busqueda por anyo con resultados
	@Test
	public void adminBuscaPelisPorAnyoConResultados() {
		videoclub.buscarPeliculas(null, null, null, null, 1996);

		List<Pelicula> lista = new ArrayList<Pelicula>();
		lista.add(pelicula1);
		when(daoMock.buscarPeliculas(null, null, null, null, 1996)).thenReturn(lista);

		verify(daoMock, times(1)).buscarPeliculas(null, null, null, null, 1996);
		Assert.assertEquals(daoMock.buscarPeliculas(null, null, null, null, 1996), lista);
	}

	// 3.5b Busqueda por anyo de estreno sin resultados
	@Test
	public void adminBuscaPelisPorAnyoSinResultados() {
		videoclub.buscarPeliculas(null, null, null, null, 2013);

		List<Pelicula> lista = new ArrayList<Pelicula>();

		when(daoMock.buscarPeliculas(null, null, null, null, 2013)).thenReturn(lista);

		verify(daoMock, times(1)).buscarPeliculas(null, null, null, null, 2013);
		Assert.assertEquals(daoMock.buscarPeliculas(null, null, null, null, 2013), lista);

	}

	///////////////////////////////////////////////////////////////////////
	///////// 3.6 - ESCENARIOS DE BUSQUEDA POR COMBINACION DE CRITERIOS//////////
	/////////////////////////////////////////////////////////////////////
	// 3.6a Busqueda por combinacion de dos criterios con resultados
	@Test
	public void adminBuscaPelisPorCombinacionDosConResultados() {
		videoclub.buscarPeliculas("El quinto elemento", "Bruce Willis", null, null, null);
		List<Pelicula> lista = new ArrayList<Pelicula>();
		lista.add(pelicula5);
		when(daoMock.buscarPeliculas("El quinto elemento", "Bruce Willis", null, null, null)).thenReturn(lista);

		verify(daoMock, times(1)).buscarPeliculas("El quinto elemento", "Bruce Willis", null, null, null);
		Assert.assertEquals(daoMock.buscarPeliculas("El quinto elemento", "Bruce Willis", null, null, null), lista);
	}

	// 3.6b Busqueda por combinacion de tres criterios con resultados
	@Test
	public void adminBuscaPelisPorCombinacionTresConResultados() {
		videoclub.buscarPeliculas(null, "Lucy Liu", "Quentin Tarantino", null, 2003);
		List<Pelicula> lista = new ArrayList<Pelicula>();
		lista.add(pelicula5);
		when(daoMock.buscarPeliculas(null, "Lucy Liu", "Quentin Tarantino", null, 2003)).thenReturn(lista);

		verify(daoMock, times(1)).buscarPeliculas(null, "Lucy Liu", "Quentin Tarantino", null, 2003);
		Assert.assertEquals(daoMock.buscarPeliculas(null, "Lucy Liu", "Quentin Tarantino", null, 2003), lista);
	}

	// 3.6c Busqueda por combinacion de cuatro criterios con resultados
	@Test
	public void adminBuscaPelisPorCombinacionCuatroConResultados() {
		videoclub.buscarPeliculas("Mission: Impossible", "Tom Cruise", "Brian de Palma", null, 1996);
		List<Pelicula> lista = new ArrayList<Pelicula>();
		lista.add(pelicula5);
		when(daoMock.buscarPeliculas("Mission: Impossible", "Tom Cruise", "Brian de Palma", null, 1996))
				.thenReturn(lista);

		verify(daoMock, times(1)).buscarPeliculas("Mission: Impossible", "Tom Cruise", "Brian de Palma", null, 1996);
		Assert.assertEquals(daoMock.buscarPeliculas("Mission: Impossible", "Tom Cruise", "Brian de Palma", null, 1996),
				lista);
	}

	// 3.6d Busqueda por combinacion de cinco criterios con resultados
	@Test
	public void adminBuscaPelisPorCombinacionCincoConResultados() {
		videoclub.buscarPeliculas("Los odiosos", "Kurt Russell", "Quentin Tarantino", Genero.SUSPENSE, 2015);
		List<Pelicula> lista = new ArrayList<Pelicula>();
		lista.add(pelicula5);
		when(daoMock.buscarPeliculas("Los odiosos", "Kurt Russell", "Quentin Tarantino", Genero.SUSPENSE, 2015))
				.thenReturn(lista);

		verify(daoMock, times(1)).buscarPeliculas("Los odiosos", "Kurt Russell", "Quentin Tarantino", Genero.SUSPENSE,
				2015);
		Assert.assertEquals(
				daoMock.buscarPeliculas("Los odiosos", "Kurt Russell", "Quentin Tarantino", Genero.SUSPENSE, 2015),
				lista);
	}

	// 3.6e Busqueda por combinacion de criterios sin resultados
	@Test
	public void adminBuscaPelisPorCombinacionDosSinResultados() {

		videoclub.buscarPeliculas("Armageddon", "Bruce Willis", null, null, null);
		List<Pelicula> lista = new ArrayList<Pelicula>();
		lista.add(pelicula5);
		when(daoMock.buscarPeliculas("Armageddon", "Bruce Willis", null, null, null)).thenReturn(lista);

		verify(daoMock, times(1)).buscarPeliculas("Armageddon", "Bruce Willis", null, null, null);
		Assert.assertEquals(daoMock.buscarPeliculas("Armageddon", "Bruce Willis", null, null, null), lista);
	}

	// 3.6f Busqueda por combinacion de criterios sin resultados
	@Test
	public void adminBuscaPelisPorCombinacionCuatroSinResultados() {

		videoclub.buscarPeliculas("Armageddon", "Bruce Willis", null, Genero.ACCION, 2015);
		List<Pelicula> lista = new ArrayList<Pelicula>();
		lista.add(pelicula5);
		when(daoMock.buscarPeliculas("Armageddon", "Bruce Willis", null, Genero.ACCION, 2015)).thenReturn(lista);

		verify(daoMock, times(1)).buscarPeliculas("Armageddon", "Bruce Willis", null, Genero.ACCION, 2015);
		Assert.assertEquals(daoMock.buscarPeliculas("Armageddon", "Bruce Willis", null, Genero.ACCION, 2015), lista);
	}

}
