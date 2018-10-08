package aceptacion;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import dao.IDAOVideoclub;
import dominio.Credenciales;
import excepciones.ExcepcionAutenticacionIncorrecta;
import modelo.IVideoclubSocio;
import modelo.VideoclubSocio;

// HU04
public class TestSocioIniciaSesion {
	private IVideoclubSocio videoclub;
	private IDAOVideoclub daoMock;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	// Inicializamos antes de cada test
	@Before
	public void initTest() throws Exception {
		daoMock = Mockito.mock(IDAOVideoclub.class);
		videoclub = new VideoclubSocio(daoMock);

		Credenciales credencialesSocio = new Credenciales();
		credencialesSocio.setUsuario("Maria19");
		credencialesSocio.setContrasenya("hy78");
		credencialesSocio.setAdmin(false);
		when(daoMock.comprobarCredenciales(credencialesSocio.getUsuario(), credencialesSocio.getContrasenya()))
				.thenReturn(credencialesSocio);

	}

	// E4.1
	@Test
	public void socioIniciaSesionCorrecto() throws Exception {

		videoclub.iniciarSesion("Maria19", "hy78");

	}

	// E4.2
	@Test
	public void socioIniciaSesionContrasenyaMal() throws Exception {

		// Excepcion esperada
		thrown.expect(ExcepcionAutenticacionIncorrecta.class);

		videoclub.iniciarSesion("Maria19", "9999");

	}

	// E4.2 var.
	@Test
	public void socioIniciaSesionNombreMal() throws Exception {

		// Excepcion esperada
		thrown.expect(ExcepcionAutenticacionIncorrecta.class);

		videoclub.iniciarSesion("Mariiia19", "hy78");

	}

	// E4.3
	@Test
	public void socioIniciaSesionContrasenyaVacia() throws Exception {

		// Excepcion esperada
		thrown.expect(ExcepcionAutenticacionIncorrecta.class);

		videoclub.iniciarSesion("Maria19", "");

	}

	// E4.3 var.
	@Test
	public void socioIniciaSesionNombreVacio() throws Exception {

		// Excepcion esperada
		thrown.expect(ExcepcionAutenticacionIncorrecta.class);

		videoclub.iniciarSesion("", "hy78");

	}

}
