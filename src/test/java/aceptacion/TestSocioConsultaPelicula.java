package aceptacion;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import dao.IDAOVideoclub;
import dominio.Credenciales;
import dominio.Genero;
import dominio.Licencia;
import dominio.Pelicula;
import modelo.IVideoclubSocio;
import modelo.VideoclubSocio;

// HU08
public class TestSocioConsultaPelicula {
	private IVideoclubSocio videoclub;
	private IDAOVideoclub daoMock;
	private Pelicula pelicula1;

	// Inicializamos el test
	@Before
	public void initTest() throws Exception {
		daoMock = Mockito.mock(IDAOVideoclub.class);
		videoclub = new VideoclubSocio(daoMock);

		Credenciales credencialesSocio = new Credenciales();
		credencialesSocio.setUsuario("Rodrigo98");
		credencialesSocio.setContrasenya("cualquiera");
		credencialesSocio.setAdmin(false);
		when(daoMock.comprobarCredenciales(credencialesSocio.getUsuario(), credencialesSocio.getContrasenya()))
				.thenReturn(credencialesSocio);
		videoclub.iniciarSesion("Rodrigo98", "cualquiera");

		pelicula1 = new Pelicula();
		pelicula1.setTituloEsp("El día más largo");
		pelicula1.setTituloVO("The longest day");
		pelicula1.setAnyo(1962);
		pelicula1.setSinopsis(
				"Segunda Guerra Mundial (1939-1945). Minucioso relato del desembarco de las tropas aliadas "
						+ "en las playas de Normandía el 6 de junio de 1944, día que señaló el comienzo del fin de la dominación "
						+ "nazi sobre Europa. En este ataque participaron 3.000.000 de hombres, 11.000 aviones y 4.000 barcos.");
		pelicula1.setNombreLicencia(Licencia.BASICA);
		pelicula1.setCopiasDisponibles(10);
		pelicula1.addActor("Sean Connery");
		pelicula1.addDirector("Ken Annakin");
		pelicula1.addDirector("Andrew Marton");
		pelicula1.addDirector("Bernhard Wicki");
		pelicula1.addGenero(Genero.BELICA);

		Pelicula pelicula2 = new Pelicula();
		pelicula2.setTituloVO("Dr. No");
		pelicula2.setAnyo(1962);

		when(daoMock.getPelicula("The longest day", 1962)).thenReturn(pelicula1);
		when(daoMock.getPelicula("Dr. No", 1962)).thenReturn(pelicula2);
	}

	// E8.1
	@Test
	public void socioConsultaPelicula() {

		Pelicula peliculaID = new Pelicula();
		peliculaID.setTituloVO("The longest day");
		peliculaID.setAnyo(1962);

		Pelicula peliculaObtenida = videoclub.verDatosPelicula(peliculaID);

		// Verificamos que la película contiene los datos correctos
		Assert.assertEquals(pelicula1.getTituloVO(), peliculaObtenida.getTituloVO());
		Assert.assertEquals(pelicula1.getTituloEsp(), peliculaObtenida.getTituloEsp());
		Assert.assertEquals(pelicula1.getAnyo(), peliculaObtenida.getAnyo());
		Assert.assertEquals(pelicula1.getSinopsis(), peliculaObtenida.getSinopsis());
		Assert.assertEquals(pelicula1.getNombreLicencia(), peliculaObtenida.getNombreLicencia());
		Assert.assertEquals(pelicula1.getCopiasDisponibles(), peliculaObtenida.getCopiasDisponibles());
		Assert.assertEquals(pelicula1.getListaGeneros(), peliculaObtenida.getListaGeneros());
		Assert.assertEquals(pelicula1.getListaActores(), peliculaObtenida.getListaActores());
		Assert.assertEquals(pelicula1.getListaDirectores(), peliculaObtenida.getListaDirectores());

	}

}
