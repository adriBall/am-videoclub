package aceptacion;

import org.junit.Test;
import org.junit.Before;
import org.mockito.Mockito;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.List;
import dao.IDAOVideoclub;
import dominio.Credenciales;
import dominio.Genero;
import dominio.Licencia;
import dominio.Pelicula;
import excepciones.ExcepcionNoExistenPeliculas;
import modelo.IVideoclubAdmin;
import modelo.VideoclubAdmin;

//HU17, el admin lista todas las peliculas del sistema
public class TestAdminListarPeliculas {

	private IDAOVideoclub daoMock; // objeto mock que adopta la interfaz de IDAOVideoclub para simular su
									// comportamiento
	private Credenciales credencialesAdmin;
	private IVideoclubAdmin videoclubAdmin;

	@Before
	public void initTest() throws Exception {

		daoMock = Mockito.mock(IDAOVideoclub.class); // creacion del doble de prueba
		videoclubAdmin = new VideoclubAdmin(daoMock); // inyectamos el daoMock en la clase a probar

		credencialesAdmin = new Credenciales("CloudVII ", "fh548", true);
		when(daoMock.comprobarCredenciales(credencialesAdmin.getUsuario(), credencialesAdmin.getContrasenya()))
				.thenReturn(credencialesAdmin);
		videoclubAdmin.iniciarSesion("CloudVII ", "fh548");
	}

	// 17.1: No hay peliculas en el sistema
	@Test(expected = ExcepcionNoExistenPeliculas.class)
	public void testListarPeliculasNoHay() throws ExcepcionNoExistenPeliculas {
		List<Pelicula> listaPeliculas = new ArrayList<Pelicula>();
		when(daoMock.listarPeliculasVideoclub()).thenReturn(listaPeliculas);

		videoclubAdmin.listarPeliculas();
		Mockito.verify(daoMock, never()).addPelicula(null);
	}

	// 17.2 a: Existe una pelicula en el sistema
	@Test
	public void testListarPeliculasHayUna() throws ExcepcionNoExistenPeliculas {

		String sinopsis = "Por orden imperial, la familia Atreides debe hacerse cargo de la explotacion del desértico "
				+ "planeta Arrakis, también llamado Dune. Es el único planeta donde se encuentra la especia, una potente "
				+ "droga que es indispensable para los vuelos espaciales.";
		List<Genero> listaGeneros = new ArrayList<Genero>();
		listaGeneros.add(Genero.CIENCIA_FICCION);
		List<String> listaActores = new ArrayList<String>();
		listaActores.add("Kyle MacLachlan");
		listaActores.add(" Francesca Annis");
		List<String> listaDirectores = new ArrayList<String>();
		listaDirectores.add("David Lynch");
		Pelicula pelicula = new Pelicula("Dune", "Dune", 1984, sinopsis, Licencia.PREMIUM, listaGeneros, listaActores,
				listaDirectores);

		List<Pelicula> listaPeliculas = new ArrayList<Pelicula>();
		listaPeliculas.add(pelicula);
		when(daoMock.listarPeliculasVideoclub()).thenReturn(listaPeliculas);

		List<Pelicula> lista = videoclubAdmin.listarPeliculas();
		assertTrue(lista.contains(pelicula));
	}

	// 17.2 b: Existen varias peliculas en el sistema
	@Test
	public void testListarPeliculasHayVarias() throws ExcepcionNoExistenPeliculas {

		String sinopsis = "Por orden imperial, la familia Atreides debe hacerse cargo de la explotacion del desértico "
				+ "planeta Arrakis, también llamado Dune. Es el único planeta donde se encuentra la especia, una potente "
				+ "droga que es indispensable para los vuelos espaciales.";
		List<Genero> listaGeneros = new ArrayList<Genero>();
		listaGeneros.add(Genero.CIENCIA_FICCION);
		List<String> listaActores = new ArrayList<String>();
		listaActores.add("Kyle MacLachlan");
		listaActores.add(" Francesca Annis");
		List<String> listaDirectores = new ArrayList<String>();
		listaDirectores.add("David Lynch");
		Pelicula pelicula = new Pelicula("Dune", "Dune", 1984, sinopsis, Licencia.PREMIUM, listaGeneros, listaActores,
				listaDirectores);

		String sinopsis2 = "Año 2073. Hace más de 60 años la Tierra fue atacada; se ganó la guerra, pero la mitad del planeta quedó destruido, "
				+ "y todos los seres humanos fueron evacuados. Jack Harper (Tom Cruise), un antiguo marine, es uno de los últimos hombres que la habitan. ";
		List<Genero> listaGeneros2 = new ArrayList<Genero>();
		listaGeneros2.add(Genero.CIENCIA_FICCION);
		listaGeneros2.add(Genero.AVENTURA);
		List<String> listaActores2 = new ArrayList<String>();
		listaActores2.add("Tom Cruise");
		listaActores2.add("Andrea Riseborough");
		List<String> listaDirectores2 = new ArrayList<String>();
		listaDirectores2.add("Joseph Kosinski");
		Pelicula pelicula2 = new Pelicula("Oblivion", "Oblivion", 2013, sinopsis2, Licencia.BASICA, listaGeneros2,
				listaActores2, listaDirectores2);

		List<Pelicula> listaPeliculas = new ArrayList<Pelicula>();
		listaPeliculas.add(pelicula);
		listaPeliculas.add(pelicula2);
		when(daoMock.listarPeliculasVideoclub()).thenReturn(listaPeliculas);

		List<Pelicula> lista = videoclubAdmin.listarPeliculas();
		assertTrue(lista.contains(pelicula));
		assertTrue(lista.contains(pelicula2));

	}

}