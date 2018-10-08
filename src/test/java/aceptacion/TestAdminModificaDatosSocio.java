package aceptacion;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import static util.Fechas.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import dao.IDAOVideoclub;
import dominio.Credenciales;
import dominio.Socio;
import excepciones.ExcepcionAutenticacionIncorrecta;
import excepciones.ExcepcionCamposNoValidos;
import excepciones.ExcepcionCamposNulos;
import excepciones.ExcepcionSocioYaExistente;
import modelo.IVideoclubAdmin;
import modelo.VideoclubAdmin;

// HU13
public class TestAdminModificaDatosSocio {
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
		credencialesAdmin.setUsuario("Batmantio84");
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

		Credenciales credencialesSocio = new Credenciales();
		credencialesSocio.setUsuario("Dani90");
		credencialesSocio.setContrasenya("64dfe854");
		credencialesSocio.setAdmin(false);
		Socio socio = new Socio();
		socio.setIdSocio("20145896L");
		socio.setNombreSocio("Daniel García");
		socio.setDireccion("calle Enmedio 12, piso 1B");
		socio.setTelefono(685774496);
		socio.setCorreo("dani90@gmail.com");
		socio.setUltimoPago(getDate(31, 9, 2016));
		socio.setCredenciales(credencialesSocio);
		when(daoMock.getSocioPorId("20145896L")).thenReturn(socio);
		when(daoMock.getSocioPorUsuario("Dani90")).thenReturn(socio);

	}

	// E13.1
	@Test
	public void adminModificaDatosCalleCorrecta() throws ExcepcionCamposNoValidos, ExcepcionSocioYaExistente,
			ExcepcionCamposNulos, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Batmantio84", "cualquiera");
		Socio nuevosDatos = daoMock.getSocioPorUsuario("Dani90");
		nuevosDatos.setDireccion("calle Zaragoza 3, piso 2A");

		videoclub.actualizarDatosSocio(nuevosDatos.getCredenciales(), nuevosDatos);

		ArgumentCaptor<Credenciales> argumentCaptor1 = ArgumentCaptor.forClass(Credenciales.class);
		ArgumentCaptor<Socio> argumentCaptor2 = ArgumentCaptor.forClass(Socio.class);
		// Compruebo que se llama al metodo
		Mockito.verify(daoMock, times(1)).modificarSocio(argumentCaptor1.capture(), argumentCaptor2.capture());
		Assert.assertEquals("calle Zaragoza 3, piso 2A", argumentCaptor2.getValue().getDireccion());
	}

	// E13.1 var.
	@Test
	public void adminModificaDatosIdCorrecto() throws ExcepcionCamposNoValidos, ExcepcionCamposNulos,
			ExcepcionSocioYaExistente, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Batmantio84", "cualquiera");
		Socio nuevosDatos = daoMock.getSocioPorUsuario("Dani90");
		nuevosDatos.setIdSocio("34567823T");

		videoclub.actualizarDatosSocio(nuevosDatos.getCredenciales(), nuevosDatos);

		ArgumentCaptor<Credenciales> argumentCaptor1 = ArgumentCaptor.forClass(Credenciales.class);
		ArgumentCaptor<Socio> argumentCaptor2 = ArgumentCaptor.forClass(Socio.class);
		// Compruebo que se llama al metodo
		Mockito.verify(daoMock, times(1)).modificarSocio(argumentCaptor1.capture(), argumentCaptor2.capture());
		Assert.assertEquals("34567823T", argumentCaptor2.getValue().getIdSocio());
	}

	// E13.1 var.
	@Test
	public void adminModificaDatosNombreCorrecto() throws ExcepcionCamposNoValidos, ExcepcionCamposNulos,
			ExcepcionSocioYaExistente, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Batmantio84", "cualquiera");
		Socio nuevosDatos = daoMock.getSocioPorUsuario("Dani90");
		nuevosDatos.setNombreSocio("Nuevo nombre");

		videoclub.actualizarDatosSocio(nuevosDatos.getCredenciales(), nuevosDatos);

		ArgumentCaptor<Credenciales> argumentCaptor1 = ArgumentCaptor.forClass(Credenciales.class);
		ArgumentCaptor<Socio> argumentCaptor2 = ArgumentCaptor.forClass(Socio.class);
		// Compruebo que se llama al metodo
		Mockito.verify(daoMock, times(1)).modificarSocio(argumentCaptor1.capture(), argumentCaptor2.capture());
		Assert.assertEquals("Nuevo nombre", argumentCaptor2.getValue().getNombreSocio());
	}

	// E13.1 var.
	@Test
	public void adminModificaDatosTelefonoCorrecto() throws ExcepcionCamposNoValidos, ExcepcionCamposNulos,
			ExcepcionSocioYaExistente, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Batmantio84", "cualquiera");
		Socio nuevosDatos = daoMock.getSocioPorUsuario("Dani90");
		nuevosDatos.setTelefono(647839478);

		videoclub.actualizarDatosSocio(nuevosDatos.getCredenciales(), nuevosDatos);

		ArgumentCaptor<Credenciales> argumentCaptor1 = ArgumentCaptor.forClass(Credenciales.class);
		ArgumentCaptor<Socio> argumentCaptor2 = ArgumentCaptor.forClass(Socio.class);
		// Compruebo que se llama al metodo
		Mockito.verify(daoMock, times(1)).modificarSocio(argumentCaptor1.capture(), argumentCaptor2.capture());
		Assert.assertEquals((int) 647839478, (int) argumentCaptor2.getValue().getTelefono());
	}

	// E13.1 var.
	@Test
	public void adminModificaDatosCorreoCorrecto() throws ExcepcionCamposNoValidos, ExcepcionCamposNulos,
			ExcepcionSocioYaExistente, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Batmantio84", "cualquiera");
		Socio nuevosDatos = daoMock.getSocioPorUsuario("Dani90");
		nuevosDatos.setCorreo("nuevo@gmail.com");

		videoclub.actualizarDatosSocio(nuevosDatos.getCredenciales(), nuevosDatos);

		ArgumentCaptor<Credenciales> argumentCaptor1 = ArgumentCaptor.forClass(Credenciales.class);
		ArgumentCaptor<Socio> argumentCaptor2 = ArgumentCaptor.forClass(Socio.class);
		// Compruebo que se llama al metodo
		Mockito.verify(daoMock, times(1)).modificarSocio(argumentCaptor1.capture(), argumentCaptor2.capture());
		Assert.assertEquals("nuevo@gmail.com", argumentCaptor2.getValue().getCorreo());
	}

	// E13.1 var.
	@Test
	public void adminModificaDatosUsuarioCorrecto() throws ExcepcionCamposNoValidos, ExcepcionCamposNulos,
			ExcepcionSocioYaExistente, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Batmantio84", "cualquiera");
		Socio nuevosDatos = daoMock.getSocioPorUsuario("Dani90");
		nuevosDatos.getCredenciales().setUsuario("nuevo");

		videoclub.actualizarDatosSocio(nuevosDatos.getCredenciales(), nuevosDatos);

		ArgumentCaptor<Credenciales> argumentCaptor1 = ArgumentCaptor.forClass(Credenciales.class);
		ArgumentCaptor<Socio> argumentCaptor2 = ArgumentCaptor.forClass(Socio.class);
		// Compruebo que se llama al metodo
		Mockito.verify(daoMock, times(1)).modificarSocio(argumentCaptor1.capture(), argumentCaptor2.capture());
		Assert.assertEquals("nuevo", argumentCaptor2.getValue().getCredenciales().getUsuario());
	}

	// E13.1 var.
	@Test
	public void adminModificaDatosPwdCorrecto() throws ExcepcionCamposNoValidos, ExcepcionCamposNulos,
			ExcepcionSocioYaExistente, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Batmantio84", "cualquiera");
		Socio nuevosDatos = daoMock.getSocioPorUsuario("Dani90");
		nuevosDatos.getCredenciales().setContrasenya("nueva");

		videoclub.actualizarDatosSocio(nuevosDatos.getCredenciales(), nuevosDatos);

		ArgumentCaptor<Credenciales> argumentCaptor1 = ArgumentCaptor.forClass(Credenciales.class);
		ArgumentCaptor<Socio> argumentCaptor2 = ArgumentCaptor.forClass(Socio.class);
		// Compruebo que se llama al metodo
		Mockito.verify(daoMock, times(1)).modificarSocio(argumentCaptor1.capture(), argumentCaptor2.capture());
		Assert.assertEquals("nueva", argumentCaptor2.getValue().getCredenciales().getContrasenya());
	}

	// E13.1 var.
	@Test
	public void adminModificaFechaUltimoagoCorrecto() throws ExcepcionCamposNoValidos, ExcepcionCamposNulos,
			ExcepcionSocioYaExistente, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Batmantio84", "cualquiera");
		Socio nuevosDatos = daoMock.getSocioPorUsuario("Dani90");
		nuevosDatos.setUltimoPago(getDate(31, 9, 2016));

		videoclub.actualizarDatosSocio(nuevosDatos.getCredenciales(), nuevosDatos);

		ArgumentCaptor<Credenciales> argumentCaptor1 = ArgumentCaptor.forClass(Credenciales.class);
		ArgumentCaptor<Socio> argumentCaptor2 = ArgumentCaptor.forClass(Socio.class);
		// Compruebo que se llama al metodo
		Mockito.verify(daoMock, times(1)).modificarSocio(argumentCaptor1.capture(), argumentCaptor2.capture());
		Assert.assertEquals(getDate(31, 9, 2016), argumentCaptor2.getValue().getUltimoPago());
	}

	// E13.2
	@Test
	public void adminModificaSocioCorreoVacio() throws ExcepcionCamposNoValidos, ExcepcionCamposNulos,
			ExcepcionSocioYaExistente, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Kodos86", "cualquiera");
		Socio nuevosDatos = daoMock.getSocioPorUsuario("Dani90");
		nuevosDatos.setCorreo(null);

		// Excepcion esperada
		thrown.expect(ExcepcionCamposNulos.class);
		videoclub.actualizarDatosSocio(nuevosDatos.getCredenciales(), nuevosDatos);

	}

	// E13.2 var.
	@Test
	public void adminModificaSocioIdVacio() throws ExcepcionCamposNoValidos, ExcepcionCamposNulos,
			ExcepcionSocioYaExistente, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Kodos86", "cualquiera");
		Socio nuevosDatos = daoMock.getSocioPorUsuario("Dani90");
		nuevosDatos.setIdSocio(null);

		// Excepcion esperada
		thrown.expect(ExcepcionCamposNulos.class);
		videoclub.actualizarDatosSocio(nuevosDatos.getCredenciales(), nuevosDatos);

	}

	// E13.2 var.
	@Test
	public void adminModificaSocioNombreVacio() throws ExcepcionCamposNoValidos, ExcepcionCamposNulos,
			ExcepcionSocioYaExistente, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Kodos86", "cualquiera");
		Socio nuevosDatos = daoMock.getSocioPorUsuario("Dani90");
		nuevosDatos.setNombreSocio(null);

		// Excepcion esperada
		thrown.expect(ExcepcionCamposNulos.class);
		videoclub.actualizarDatosSocio(nuevosDatos.getCredenciales(), nuevosDatos);

	}

	// E13.2 var.
	@Test
	public void adminModificaSocioTelefonoVacio() throws ExcepcionCamposNoValidos, ExcepcionCamposNulos,
			ExcepcionSocioYaExistente, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Kodos86", "cualquiera");
		Socio nuevosDatos = daoMock.getSocioPorUsuario("Dani90");
		nuevosDatos.setTelefono(null);

		// Excepcion esperada
		thrown.expect(ExcepcionCamposNulos.class);
		videoclub.actualizarDatosSocio(nuevosDatos.getCredenciales(), nuevosDatos);

	}

	// E13.2 var.
	@Test
	public void adminModificaSocioDireccVacia() throws ExcepcionCamposNoValidos, ExcepcionCamposNulos,
			ExcepcionSocioYaExistente, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Kodos86", "cualquiera");
		Socio nuevosDatos = daoMock.getSocioPorUsuario("Dani90");
		nuevosDatos.setDireccion(null);

		// Excepcion esperada
		thrown.expect(ExcepcionCamposNulos.class);
		videoclub.actualizarDatosSocio(nuevosDatos.getCredenciales(), nuevosDatos);

	}

	// E13.2 var.
	@Test
	public void adminModificaSocioCredencialesNulos() throws ExcepcionCamposNoValidos, ExcepcionCamposNulos,
			ExcepcionSocioYaExistente, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Kodos86", "cualquiera");
		Socio nuevosDatos = daoMock.getSocioPorUsuario("Dani90");
		nuevosDatos.setCredenciales(null);

		// Excepcion esperada
		thrown.expect(ExcepcionCamposNulos.class);
		videoclub.actualizarDatosSocio(nuevosDatos.getCredenciales(), nuevosDatos);

	}

	// E13.2 var.
	@Test
	public void adminModificaSocioUsuarioVacio() throws ExcepcionCamposNoValidos, ExcepcionCamposNulos,
			ExcepcionSocioYaExistente, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Kodos86", "cualquiera");
		Socio nuevosDatos = daoMock.getSocioPorUsuario("Dani90");
		nuevosDatos.getCredenciales().setUsuario(null);

		// Excepcion esperada
		thrown.expect(ExcepcionCamposNulos.class);
		videoclub.actualizarDatosSocio(nuevosDatos.getCredenciales(), nuevosDatos);

	}

	// E13.2 var.
	@Test
	public void adminModificaSocioPwdVacio() throws ExcepcionCamposNoValidos, ExcepcionCamposNulos,
			ExcepcionSocioYaExistente, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Kodos86", "cualquiera");
		Socio nuevosDatos = daoMock.getSocioPorUsuario("Dani90");
		nuevosDatos.getCredenciales().setContrasenya(null);

		// Excepcion esperada
		thrown.expect(ExcepcionCamposNulos.class);
		videoclub.actualizarDatosSocio(nuevosDatos.getCredenciales(), nuevosDatos);

	}

	// E13.2 var.
	@Test
	public void adminModificaSocioUltimoPagoVacio() throws ExcepcionCamposNoValidos, ExcepcionSocioYaExistente,
			ExcepcionCamposNulos, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Kodos86", "cualquiera");
		Socio nuevosDatos = daoMock.getSocioPorUsuario("Dani90");
		nuevosDatos.setUltimoPago(null);

		// Excepcion esperada
		thrown.expect(ExcepcionCamposNulos.class);
		videoclub.actualizarDatosSocio(nuevosDatos.getCredenciales(), nuevosDatos);

	}

	// E13.3
	@Test
	public void adminModificaSocioNombreoNoValido() throws ExcepcionCamposNoValidos, ExcepcionCamposNulos,
			ExcepcionSocioYaExistente, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Kodos86", "cualquiera");
		Socio nuevosDatos = daoMock.getSocioPorUsuario("Dani90");
		nuevosDatos.setNombreSocio("896547");

		// Excepcion esperada
		thrown.expect(ExcepcionCamposNoValidos.class);
		videoclub.actualizarDatosSocio(nuevosDatos.getCredenciales(), nuevosDatos);
	}

	// E13.3 var.
	@Test
	public void adminModificaSocioIdNoValido() throws ExcepcionCamposNoValidos, ExcepcionCamposNulos,
			ExcepcionSocioYaExistente, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Kodos86", "cualquiera");
		Socio nuevosDatos = daoMock.getSocioPorUsuario("Dani90");
		nuevosDatos.setIdSocio("34");

		// Excepcion esperada
		thrown.expect(ExcepcionCamposNoValidos.class);
		videoclub.actualizarDatosSocio(nuevosDatos.getCredenciales(), nuevosDatos);
	}

	// E13.3 var.
	@Test
	public void adminModificaSocioTelefonoNoValido() throws ExcepcionCamposNoValidos, ExcepcionCamposNulos,
			ExcepcionSocioYaExistente, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Kodos86", "cualquiera");
		Socio nuevosDatos = daoMock.getSocioPorUsuario("Dani90");
		nuevosDatos.setTelefono(88);

		// Excepcion esperada
		thrown.expect(ExcepcionCamposNoValidos.class);
		videoclub.actualizarDatosSocio(nuevosDatos.getCredenciales(), nuevosDatos);
	}

	// E13.3 var.
	@Test
	public void adminModificaSocioDireccNoValido() throws ExcepcionCamposNoValidos, ExcepcionCamposNulos,
			ExcepcionSocioYaExistente, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Kodos86", "cualquiera");
		Socio nuevosDatos = daoMock.getSocioPorUsuario("Dani90");
		nuevosDatos.setDireccion("");

		// Excepcion esperada
		thrown.expect(ExcepcionCamposNoValidos.class);
		videoclub.actualizarDatosSocio(nuevosDatos.getCredenciales(), nuevosDatos);
	}

	// E13.3 var.
	@Test
	public void adminModificaSocioCorreoNoValido() throws ExcepcionCamposNoValidos, ExcepcionCamposNulos,
			ExcepcionSocioYaExistente, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Kodos86", "cualquiera");
		Socio nuevosDatos = daoMock.getSocioPorUsuario("Dani90");
		nuevosDatos.setCorreo("asd");

		// Excepcion esperada
		thrown.expect(ExcepcionCamposNoValidos.class);
		videoclub.actualizarDatosSocio(nuevosDatos.getCredenciales(), nuevosDatos);
	}

	// E13.3 var.
	@Test
	public void adminModificaSocioUsuarioNoValido() throws ExcepcionCamposNoValidos, ExcepcionCamposNulos,
			ExcepcionSocioYaExistente, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Kodos86", "cualquiera");
		Socio nuevosDatos = daoMock.getSocioPorUsuario("Dani90");
		nuevosDatos.getCredenciales().setUsuario("");

		// Excepcion esperada
		thrown.expect(ExcepcionCamposNoValidos.class);
		videoclub.actualizarDatosSocio(nuevosDatos.getCredenciales(), nuevosDatos);
	}

	// E13.3 var.
	@Test
	public void adminModificaSocioPwdNoValido() throws ExcepcionCamposNoValidos, ExcepcionCamposNulos,
			ExcepcionSocioYaExistente, ExcepcionAutenticacionIncorrecta {
		videoclub.iniciarSesion("Kodos86", "cualquiera");
		Socio nuevosDatos = daoMock.getSocioPorUsuario("Dani90");
		nuevosDatos.getCredenciales().setContrasenya("");

		// Excepcion esperada
		thrown.expect(ExcepcionCamposNoValidos.class);
		videoclub.actualizarDatosSocio(nuevosDatos.getCredenciales(), nuevosDatos);
	}

}
