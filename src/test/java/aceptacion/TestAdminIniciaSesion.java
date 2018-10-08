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
import modelo.IVideoclubAdmin;
import modelo.VideoclubAdmin;

// HU09
public class TestAdminIniciaSesion {
	private IVideoclubAdmin videoclub;
	private IDAOVideoclub daoMock;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	// Inicializamos antes de cada test
	@Before
	public void initTest() throws Exception {
		daoMock = Mockito.mock(IDAOVideoclub.class);
		videoclub = new VideoclubAdmin(daoMock);

		Credenciales credencialesAdmin = new Credenciales();
		credencialesAdmin.setUsuario("admin12");
		credencialesAdmin.setContrasenya("h45y78");
		credencialesAdmin.setAdmin(true);
		when(daoMock.comprobarCredenciales(credencialesAdmin.getUsuario(), credencialesAdmin.getContrasenya()))
				.thenReturn(credencialesAdmin);

	}

	// E9.1
	@Test
	public void adminIniciaSesionCorrecto() throws Exception {

		videoclub.iniciarSesion("admin12", "h45y78");

	}

	// E9.2
	@Test
	public void adminIniciaSesionContrasenyaMal() throws Exception {

		// Excepcion esperada
		thrown.expect(ExcepcionAutenticacionIncorrecta.class);

		videoclub.iniciarSesion("admin12", "9999");

	}

	// E9.2 var.
	@Test
	public void adminIniciaSesionNombreMal() throws Exception {

		// Excepcion esperada
		thrown.expect(ExcepcionAutenticacionIncorrecta.class);

		videoclub.iniciarSesion("admon12", "h45y78");

	}

	// E9.3
	@Test
	public void adminIniciaSesionContrasenyaVacia() throws Exception {

		// Excepcion esperada
		thrown.expect(ExcepcionAutenticacionIncorrecta.class);

		videoclub.iniciarSesion("admin12", "");

	}

	// E9.3 var.
	@Test
	public void adminIniciaSesionNombreVacio() throws Exception {

		// Excepcion esperada
		thrown.expect(ExcepcionAutenticacionIncorrecta.class);

		videoclub.iniciarSesion("", "h45y78");

	}

}
