package aceptacion;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import dao.IDAOVideoclub;
import dominio.Credenciales;
import dominio.Genero;
import dominio.Licencia;
import dominio.Pelicula;
import excepciones.ExcepcionAutenticacionIncorrecta;
import excepciones.ExcepcionCamposNoValidos;
import excepciones.ExcepcionCamposNulos;
import modelo.IVideoclubAdmin;
import modelo.VideoclubAdmin;

// HU19 (junto con la 20)
public class TestAdminModificaDatosPelicula {
	private IVideoclubAdmin videoclub;
	private IDAOVideoclub daoMock;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	// Inicializamos el test
	@Before
	public void initTest() {
		daoMock = Mockito.mock(IDAOVideoclub.class);
		videoclub = new VideoclubAdmin(daoMock);

		Credenciales credencialesAdmin = new Credenciales();
		credencialesAdmin.setUsuario("Heisenberg92");
		credencialesAdmin.setContrasenya("cualquiera");
		credencialesAdmin.setAdmin(true);

		Credenciales credencialesAdmin2 = new Credenciales();
		credencialesAdmin2.setUsuario("Kodos86");
		credencialesAdmin2.setContrasenya("cualquiera");
		credencialesAdmin2.setAdmin(true);
		when(daoMock.comprobarCredenciales(credencialesAdmin.getUsuario(), credencialesAdmin.getContrasenya()))
				.thenReturn(credencialesAdmin);
		when(daoMock.comprobarCredenciales(credencialesAdmin2.getUsuario(), credencialesAdmin2.getContrasenya()))
				.thenReturn(credencialesAdmin2);

		Pelicula wallStreet = new Pelicula();
		wallStreet.setAnyo(1987);
		wallStreet.setTituloVO("Wall Street");
		wallStreet.setTituloEsp("Wall Street");
		wallStreet.addActor("Michael Douglas");
		wallStreet.addActor("Charlie Sheen");
		wallStreet.addDirector("Oliver Stone");
		wallStreet.addGenero(Genero.DRAMA);
		wallStreet.setSinopsis("Bud Fox es un joven corredor de bolsa que consiguió terminar "
				+ "sus estudios universitarios gracias a su esfuerzo y al de su padre. "
				+ "Su mayor deseo es trabajar con un hombre al que admira, Gordon Gekko. "
				+ "Bud se introduce en el círculo privado de Gekko, y comienza a colaborar con "
				+ "él en sus negocios.");
		wallStreet.setNombreLicencia(Licencia.SILVER);
		wallStreet.setCopiasDisponibles(32);
		when(daoMock.getPelicula("Wall Street", 1987)).thenReturn(wallStreet);

		Pelicula yoRobot = new Pelicula();
		yoRobot.setAnyo(2004);
		yoRobot.setTituloVO("I robot");
		yoRobot.setTituloEsp("Yo robot");
		yoRobot.addActor("Will Smith");
		yoRobot.addDirector("Alex Proyas");

		yoRobot.addGenero(Genero.CIENCIA_FICCION);
		yoRobot.setSinopsis("Chicago, año 2035. Vivimos en completa armonía con robots inteligentes. "
				+ "Cocinan para nosotros, conducen nuestros aviones, cuidan de nuestros hijos y confiamos "
				+ "plenamente en ellos debido a que se rigen por las Tres Leyes de la Robótica que nos protegen de "
				+ "cualquier daño.");
		yoRobot.setNombreLicencia(Licencia.BASICA);
		yoRobot.setCopiasDisponibles(8);
		when(daoMock.getPelicula("I robot", 2004)).thenReturn(yoRobot);

	}

	// E19.1
	@Test
	public void adminModificaDatosSinopsisYActoresCorrecta()
			throws ExcepcionCamposNoValidos, ExcepcionCamposNulos, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Heisenberg92", "cualquiera");
		Pelicula pelicula = daoMock.getPelicula("Wall Street", 1987);
		pelicula.setSinopsis(
				"Bud Fox es un joven corredor de bolsa que tras terminar sus estudios cumple el sueño de trabajar con el hombre al que admira");
		pelicula.addActor("Martin Sheen");

		videoclub.actualizarDatosPelicula("Wall Street", 1987, pelicula);

		ArgumentCaptor<String> argumentCaptor1 = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Integer> argumentCaptor2 = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<Pelicula> argumentCaptor3 = ArgumentCaptor.forClass(Pelicula.class);
		// Compruebo que se llama al metodo
		Mockito.verify(daoMock, times(1)).modificarPelicula(argumentCaptor1.capture(), argumentCaptor2.capture(),
				argumentCaptor3.capture());
		Assert.assertEquals(
				"Bud Fox es un joven corredor de bolsa que tras terminar sus estudios cumple el sueño de trabajar con el hombre al que admira",
				argumentCaptor3.getValue().getSinopsis());
		Assert.assertTrue(argumentCaptor3.getValue().getListaActores().contains("Martin Sheen"));
	}

	// E19.1 var.
	@Test
	public void adminModificaDirectoresCorrecto()
			throws ExcepcionCamposNoValidos, ExcepcionCamposNulos, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Heisenberg92", "cualquiera");
		Pelicula pelicula = daoMock.getPelicula("Wall Street", 1987);
		pelicula.addDirector("Pepe Bao");

		videoclub.actualizarDatosPelicula("Wall Street", 1987, pelicula);

		ArgumentCaptor<String> argumentCaptor1 = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Integer> argumentCaptor2 = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<Pelicula> argumentCaptor3 = ArgumentCaptor.forClass(Pelicula.class);
		// Compruebo que se llama al metodo
		Mockito.verify(daoMock, times(1)).modificarPelicula(argumentCaptor1.capture(), argumentCaptor2.capture(),
				argumentCaptor3.capture());
		Assert.assertTrue(argumentCaptor3.getValue().getListaDirectores().contains("Pepe Bao"));
	}

	// E19.1 var.
	@Test
	public void adminModificaTituloEspCorrecto()
			throws ExcepcionCamposNoValidos, ExcepcionCamposNulos, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Heisenberg92", "cualquiera");
		Pelicula pelicula = daoMock.getPelicula("Wall Street", 1987);
		pelicula.setTituloEsp("Wallapop Street");

		videoclub.actualizarDatosPelicula("Wall Street", 1987, pelicula);

		ArgumentCaptor<String> argumentCaptor1 = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Integer> argumentCaptor2 = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<Pelicula> argumentCaptor3 = ArgumentCaptor.forClass(Pelicula.class);
		// Compruebo que se llama al metodo
		Mockito.verify(daoMock, times(1)).modificarPelicula(argumentCaptor1.capture(), argumentCaptor2.capture(),
				argumentCaptor3.capture());
		Assert.assertTrue(argumentCaptor3.getValue().getTituloEsp().equals("Wallapop Street"));
	}

	// E19.1 var.
	@Test
	public void adminModificaGenerosCorrecto()
			throws ExcepcionCamposNoValidos, ExcepcionCamposNulos, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Heisenberg92", "cualquiera");
		Pelicula pelicula = daoMock.getPelicula("Wall Street", 1987);
		pelicula.addGenero(Genero.COMEDIA);

		videoclub.actualizarDatosPelicula("Wall Street", 1987, pelicula);

		ArgumentCaptor<String> argumentCaptor1 = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Integer> argumentCaptor2 = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<Pelicula> argumentCaptor3 = ArgumentCaptor.forClass(Pelicula.class);
		// Compruebo que se llama al metodo
		Mockito.verify(daoMock, times(1)).modificarPelicula(argumentCaptor1.capture(), argumentCaptor2.capture(),
				argumentCaptor3.capture());
		Assert.assertTrue(argumentCaptor3.getValue().getListaGeneros().contains(Genero.COMEDIA));
	}

	// E19.1 var.
	@Test
	public void adminModificaTituloVOCorrecto()
			throws ExcepcionCamposNoValidos, ExcepcionCamposNulos, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Heisenberg92", "cualquiera");
		Pelicula pelicula = daoMock.getPelicula("Wall Street", 1987);
		pelicula.setTituloVO("Subelo");

		videoclub.actualizarDatosPelicula("Wall Street", 1987, pelicula);

		ArgumentCaptor<String> argumentCaptor1 = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Integer> argumentCaptor2 = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<Pelicula> argumentCaptor3 = ArgumentCaptor.forClass(Pelicula.class);
		// Compruebo que se llama al metodo
		Mockito.verify(daoMock, times(1)).modificarPelicula(argumentCaptor1.capture(), argumentCaptor2.capture(),
				argumentCaptor3.capture());
		Assert.assertTrue(argumentCaptor3.getValue().getTituloVO().equals("Subelo"));
	}

	// E19.1 var.
	@Test
	public void adminModificaAnyoCorrecto()
			throws ExcepcionCamposNoValidos, ExcepcionCamposNulos, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Heisenberg92", "cualquiera");
		Pelicula pelicula = daoMock.getPelicula("Wall Street", 1987);
		pelicula.setAnyo(2000);

		videoclub.actualizarDatosPelicula("Wall Street", 1987, pelicula);

		ArgumentCaptor<String> argumentCaptor1 = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Integer> argumentCaptor2 = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<Pelicula> argumentCaptor3 = ArgumentCaptor.forClass(Pelicula.class);
		// Compruebo que se llama al metodo
		Mockito.verify(daoMock, times(1)).modificarPelicula(argumentCaptor1.capture(), argumentCaptor2.capture(),
				argumentCaptor3.capture());
		Assert.assertTrue(argumentCaptor3.getValue().getAnyo() == 2000);
	}

	// E19.2
	@Test
	public void adminModificatTituloVOVacio()
			throws ExcepcionCamposNoValidos, ExcepcionCamposNulos, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Kodos86", "cualquiera");
		Pelicula pelicula = daoMock.getPelicula("I robot", 2004);
		pelicula.setTituloVO(null);

		// Excepcion esperada
		thrown.expect(ExcepcionCamposNulos.class);

		videoclub.actualizarDatosPelicula("I robot", 2004, pelicula);

	}

	// E19.2 var.
	@Test
	public void adminModificaTituloEspVacio()
			throws ExcepcionCamposNoValidos, ExcepcionCamposNulos, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Kodos86", "cualquiera");
		Pelicula pelicula = daoMock.getPelicula("I robot", 2004);
		pelicula.setTituloEsp(null);

		// Excepcion esperada
		thrown.expect(ExcepcionCamposNulos.class);

		videoclub.actualizarDatosPelicula("I robot", 2004, pelicula);

	}

	// E19.2 var.
	@Test
	public void adminModificaAnyoVacio()
			throws ExcepcionCamposNoValidos, ExcepcionCamposNulos, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Kodos86", "cualquiera");
		Pelicula pelicula = daoMock.getPelicula("I robot", 2004);
		pelicula.setAnyo(null);

		// Excepcion esperada
		thrown.expect(ExcepcionCamposNulos.class);

		videoclub.actualizarDatosPelicula("I robot", 2004, pelicula);

	}

	// E19.2 var.
	@Test
	public void adminModificaSinopsisVacio()
			throws ExcepcionCamposNoValidos, ExcepcionCamposNulos, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Kodos86", "cualquiera");
		Pelicula pelicula = daoMock.getPelicula("I robot", 2004);
		pelicula.setSinopsis(null);

		// Excepcion esperada
		thrown.expect(ExcepcionCamposNulos.class);

		videoclub.actualizarDatosPelicula("I robot", 2004, pelicula);

	}

	// E19.2 var.
	@Test
	public void adminModificaActoresVacio()
			throws ExcepcionCamposNoValidos, ExcepcionCamposNulos, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Kodos86", "cualquiera");
		Pelicula pelicula = daoMock.getPelicula("I robot", 2004);
		pelicula.setListaActores(new LinkedList<>());

		// Excepcion esperada
		thrown.expect(ExcepcionCamposNulos.class);

		videoclub.actualizarDatosPelicula("I, robot", 2004, pelicula);

	}

	// E19.2 var.
	@Test
	public void adminModificaDirectoresVacio()
			throws ExcepcionCamposNoValidos, ExcepcionCamposNulos, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Kodos86", "cualquiera");
		Pelicula pelicula = daoMock.getPelicula("I robot", 2004);
		pelicula.setListaDirectores(new LinkedList<>());

		// Excepcion esperada
		thrown.expect(ExcepcionCamposNulos.class);

		videoclub.actualizarDatosPelicula("I robot", 2004, pelicula);

	}

	// E19.2 var.
	@Test
	public void adminModificaGenerosVacio()
			throws ExcepcionCamposNoValidos, ExcepcionCamposNulos, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Kodos86", "cualquiera");
		Pelicula pelicula = daoMock.getPelicula("I robot", 2004);
		pelicula.setListaGeneros(new LinkedList<>());

		// Excepcion esperada
		thrown.expect(ExcepcionCamposNulos.class);

		videoclub.actualizarDatosPelicula("I robot", 2004, pelicula);

	}

	// E19.3
	@Test
	public void adminModificaSinopsisMasDe300Caracteres()
			throws ExcepcionCamposNoValidos, ExcepcionCamposNulos, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Kodos86", "cualquiera");
		Pelicula pelicula = daoMock.getPelicula("I robot", 2004);
		pelicula.setSinopsis("Chicago, año 2035. Vivimos en completa armonía con robots inteligentes. "
				+ "Cocinan para nosotros, conducen nuestros aviones, cuidan de nuestros hijos y confiamos "
				+ "plenamente en ellos debido a que se rigen por las Tres Leyes de la Robótica que nos protegen de "
				+ "cualquier daño. Inesperadamente un robot se ve implicado en el crimen de un brillante científico y "
				+ "el detective Del Spooner (Will Smith) queda a cargo de la investigación.");

		// Excepcion esperada
		thrown.expect(ExcepcionCamposNoValidos.class);

		videoclub.actualizarDatosPelicula("I robot", 2004, pelicula);

	}

	// E19.3 var.
	@Test
	public void adminModificaAnyoNoValido()
			throws ExcepcionCamposNoValidos, ExcepcionCamposNulos, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Kodos86", "cualquiera");
		Pelicula pelicula = daoMock.getPelicula("I robot", 2004);
		pelicula.setAnyo(50000);

		// Excepcion esperada
		thrown.expect(ExcepcionCamposNoValidos.class);

		videoclub.actualizarDatosPelicula("I robot", 2004, pelicula);

	}

	// E19.3 var.
	@Test
	public void adminModificaTituloVONoValido()
			throws ExcepcionCamposNoValidos, ExcepcionCamposNulos, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Kodos86", "cualquiera");
		Pelicula pelicula = daoMock.getPelicula("I robot", 2004);
		pelicula.setTituloVO("I,,");

		// Excepcion esperada
		thrown.expect(ExcepcionCamposNoValidos.class);

		videoclub.actualizarDatosPelicula("I robot", 2004, pelicula);

	}

	// E19.3 var.
	@Test
	public void adminModificaTituloEspNoValido()
			throws ExcepcionCamposNoValidos, ExcepcionCamposNulos, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Kodos86", "cualquiera");
		Pelicula pelicula = daoMock.getPelicula("I robot", 2004);
		pelicula.setTituloEsp("I ,,");

		// Excepcion esperada
		thrown.expect(ExcepcionCamposNoValidos.class);

		videoclub.actualizarDatosPelicula("I, robot", 2004, pelicula);

	}

	// E19.3 var.
	@Test
	public void adminModificaActoresNoValido()
			throws ExcepcionCamposNoValidos, ExcepcionCamposNulos, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Kodos86", "cualquiera");
		Pelicula pelicula = daoMock.getPelicula("I robot", 2004);
		pelicula.addActor("8");

		// Excepcion esperada
		thrown.expect(ExcepcionCamposNoValidos.class);

		videoclub.actualizarDatosPelicula("I robot", 2004, pelicula);

	}

	// E19.3 var.
	@Test
	public void adminModificaDirectoresNoValido()
			throws ExcepcionCamposNoValidos, ExcepcionCamposNulos, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Kodos86", "cualquiera");
		Pelicula pelicula = daoMock.getPelicula("I robot", 2004);
		pelicula.addDirector("8");

		// Excepcion esperada
		thrown.expect(ExcepcionCamposNoValidos.class);

		videoclub.actualizarDatosPelicula("I, robot", 2004, pelicula);

	}

	// E20.1 (modificado con datos de 19)
	@Test
	public void adminModificaLicenciaMasCopias()
			throws ExcepcionCamposNoValidos, ExcepcionCamposNulos, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Kodos86", "cualquiera");
		Pelicula pelicula = daoMock.getPelicula("I robot", 2004);
		pelicula.setNombreLicencia(Licencia.SILVER);

		videoclub.actualizarDatosPelicula("I robot", 2004, pelicula);

		Assert.assertTrue(daoMock.getPelicula("I robot", 2004).getCopiasDisponibles() == 48);
	}

	// E20.2 (modificado con datos de 19)
	@Test
	public void adminModificaLicenciaMenosCopiasSeVuelveNegativo()
			throws ExcepcionCamposNoValidos, ExcepcionCamposNulos, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Kodos86", "cualquiera");
		Pelicula pelicula = daoMock.getPelicula("Wall Street", 1987);
		pelicula.setNombreLicencia(Licencia.BASICA);

		videoclub.actualizarDatosPelicula("Wall Street", 1987, pelicula);

		Assert.assertTrue(daoMock.getPelicula("Wall Street", 1987).getCopiasDisponibles() == -8);
	}

	// E20.3 (modificado con datos de 19)
	@Test
	public void adminModificaLicenciaMenosCopiasSiguePositivo()
			throws ExcepcionCamposNoValidos, ExcepcionCamposNulos, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Kodos86", "cualquiera");
		Pelicula pelicula = daoMock.getPelicula("Wall Street", 1987);
		pelicula.setNombreLicencia(Licencia.PREMIUM);

		videoclub.actualizarDatosPelicula("Wall Street", 1987, pelicula);

		Assert.assertTrue(daoMock.getPelicula("Wall Street", 1987).getCopiasDisponibles() == 2);
	}

	// E20 var.
	@Test
	public void adminModificaLicenciaSeVuelve0()
			throws ExcepcionCamposNoValidos, ExcepcionCamposNulos, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Kodos86", "cualquiera");
		Pelicula pelicula = daoMock.getPelicula("Wall Street", 1987);
		pelicula.setCopiasDisponibles(30);
		pelicula.setNombreLicencia(Licencia.PREMIUM);

		videoclub.actualizarDatosPelicula("Wall Street", 1987, pelicula);

		Assert.assertTrue(daoMock.getPelicula("Wall Street", 1987).getCopiasDisponibles() == 0);
	}

}
