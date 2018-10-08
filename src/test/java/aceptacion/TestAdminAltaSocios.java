package aceptacion;

import org.junit.Test;
import org.junit.Before;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import dao.IDAOVideoclub;
import dominio.Credenciales;
import dominio.Socio;
import excepciones.ExcepcionCamposNoValidos;
import excepciones.ExcepcionCamposNulos;
import excepciones.ExcepcionSocioYaExistente;
import modelo.IVideoclubAdmin;
import modelo.VideoclubAdmin;

import static util.Fechas.*;

//HU10, el admin da de alta a socios en el sistema
public class TestAdminAltaSocios {

	private IDAOVideoclub daoMock; // objeto mock que adopta la interfaz de IDAOVideoclub para simular su
									// comportamiento
	private Credenciales credencialesAdmin;
	private IVideoclubAdmin videoclubAdmin;

	@Before
	public void initTest() throws Exception {

		daoMock = Mockito.mock(IDAOVideoclub.class); // creacion del doble de prueba
		videoclubAdmin = new VideoclubAdmin(daoMock); // inyectamos el daoMock en la clase a probar

		credencialesAdmin = new Credenciales("Batmantio84", "cualquiera", true);
		when(daoMock.comprobarCredenciales(credencialesAdmin.getUsuario(), credencialesAdmin.getContrasenya()))
				.thenReturn(credencialesAdmin);
		videoclubAdmin.iniciarSesion("Batmantio84", "cualquiera");

	}

	// 10.1 a
	@Test(expected = ExcepcionCamposNulos.class)
	public void testDarAltaSocioCamposNulosA()
			throws ExcepcionSocioYaExistente, ExcepcionCamposNoValidos, ExcepcionCamposNulos {

		Credenciales credencialesSocio = new Credenciales();
		credencialesSocio.setContrasenya("dsfgf45");
		credencialesSocio.setAdmin(false);

		Socio nuevoSocio = new Socio();
		nuevoSocio.setIdSocio("18594125L");
		nuevoSocio.setNombreSocio("alberto");
		nuevoSocio.setDireccion("calle Mayor 12");
		nuevoSocio.setTelefono(670885548);
		nuevoSocio.setCorreo("alberto89@gmail.com");
		nuevoSocio.setCredenciales(credencialesSocio);

		videoclubAdmin.darAltaSocio(nuevoSocio);
		// Compruebo que no se llama al metodo de anyadir socio
		Mockito.verify(daoMock, never()).addSocio(null);
	}

	// 10.1 b
	@Test(expected = ExcepcionCamposNulos.class)
	public void testDarAltaSocioCamposNulosB()
			throws ExcepcionSocioYaExistente, ExcepcionCamposNoValidos, ExcepcionCamposNulos {

		Credenciales credencialesSocio = new Credenciales();
		credencialesSocio.setUsuario("albert89");
		credencialesSocio.setAdmin(false);

		Socio nuevoSocio = new Socio();
		nuevoSocio.setIdSocio("18594125L");
		nuevoSocio.setNombreSocio("alberto");
		nuevoSocio.setDireccion("calle Mayor 12");
		nuevoSocio.setTelefono(670885548);
		nuevoSocio.setCorreo("alberto89@gmail.com");
		nuevoSocio.setCredenciales(credencialesSocio);

		videoclubAdmin.darAltaSocio(nuevoSocio);
		// Compruebo que no se llama al metodo de anyadir socio
		Mockito.verify(daoMock, never()).addSocio(null);
	}

	// 10.1 c
	@Test(expected = ExcepcionCamposNulos.class)
	public void testDarAltaSocioCamposNulosC()
			throws ExcepcionSocioYaExistente, ExcepcionCamposNoValidos, ExcepcionCamposNulos {

		Credenciales credencialesSocio = new Credenciales();
		credencialesSocio.setUsuario("albert89");
		credencialesSocio.setContrasenya("dsfgf45");

		Socio nuevoSocio = new Socio();
		nuevoSocio.setIdSocio("18594125L");
		nuevoSocio.setNombreSocio("alberto");
		nuevoSocio.setDireccion("calle Mayor 12");
		nuevoSocio.setTelefono(670885548);
		nuevoSocio.setCorreo("alberto89@gmail.com");
		nuevoSocio.setCredenciales(credencialesSocio);

		videoclubAdmin.darAltaSocio(nuevoSocio);
		// Compruebo que no se llama al metodo de anyadir socio
		Mockito.verify(daoMock, never()).addSocio(null);
	}

	// 10.1 d
	@Test(expected = ExcepcionCamposNulos.class)
	public void testDarAltaSocioCamposNulosD()
			throws ExcepcionSocioYaExistente, ExcepcionCamposNoValidos, ExcepcionCamposNulos {

		Credenciales credencialesSocio = new Credenciales();
		credencialesSocio.setUsuario("albert89");
		credencialesSocio.setContrasenya("dsfgf45");
		credencialesSocio.setAdmin(false);

		Socio nuevoSocio = new Socio();
		nuevoSocio.setNombreSocio("alberto");
		nuevoSocio.setDireccion("calle Mayor 12");
		nuevoSocio.setTelefono(670885548);
		nuevoSocio.setCorreo("alberto89@gmail.com");
		nuevoSocio.setCredenciales(credencialesSocio);

		videoclubAdmin.darAltaSocio(nuevoSocio);
		// Compruebo que no se llama al metodo de anyadir socio
		Mockito.verify(daoMock, never()).addSocio(null);
	}

	// 10.1 e
	@Test(expected = ExcepcionCamposNulos.class)
	public void testDarAltaSocioCamposNulosE()
			throws ExcepcionSocioYaExistente, ExcepcionCamposNoValidos, ExcepcionCamposNulos {

		Credenciales credencialesSocio = new Credenciales();
		credencialesSocio.setUsuario("albert89");
		credencialesSocio.setContrasenya("dsfgf45");
		credencialesSocio.setAdmin(false);

		Socio nuevoSocio = new Socio();
		nuevoSocio.setIdSocio("18594125L");
		nuevoSocio.setDireccion("calle Mayor 12");
		nuevoSocio.setTelefono(670885548);
		nuevoSocio.setCorreo("alberto89@gmail.com");
		nuevoSocio.setCredenciales(credencialesSocio);

		videoclubAdmin.darAltaSocio(nuevoSocio);
		// Compruebo que no se llama al metodo de anyadir socio
		Mockito.verify(daoMock, never()).addSocio(null);
	}

	// 10.1 f
	@Test(expected = ExcepcionCamposNulos.class)
	public void testDarAltaSocioCamposNulosF()
			throws ExcepcionSocioYaExistente, ExcepcionCamposNoValidos, ExcepcionCamposNulos {

		Credenciales credencialesSocio = new Credenciales();
		credencialesSocio.setUsuario("albert89");
		credencialesSocio.setContrasenya("dsfgf45");
		credencialesSocio.setAdmin(false);

		Socio nuevoSocio = new Socio();
		nuevoSocio.setIdSocio("18594125L");
		nuevoSocio.setNombreSocio("alberto");
		nuevoSocio.setTelefono(670885548);
		nuevoSocio.setCorreo("alberto89@gmail.com");
		nuevoSocio.setCredenciales(credencialesSocio);

		videoclubAdmin.darAltaSocio(nuevoSocio);
		// Compruebo que no se llama al metodo de anyadir socio
		Mockito.verify(daoMock, never()).addSocio(null);
	}

	// 10.1 g
	@Test(expected = ExcepcionCamposNulos.class)
	public void testDarAltaSocioCamposNulosG()
			throws ExcepcionSocioYaExistente, ExcepcionCamposNoValidos, ExcepcionCamposNulos {

		Credenciales credencialesSocio = new Credenciales();
		credencialesSocio.setUsuario("albert89");
		credencialesSocio.setContrasenya("dsfgf45");
		credencialesSocio.setAdmin(false);

		Socio nuevoSocio = new Socio();
		nuevoSocio.setIdSocio("18594125L");
		nuevoSocio.setNombreSocio("alberto");
		nuevoSocio.setDireccion("calle Mayor 12");
		nuevoSocio.setCorreo("alberto89@gmail.com");
		nuevoSocio.setCredenciales(credencialesSocio);

		videoclubAdmin.darAltaSocio(nuevoSocio);
		// Compruebo que no se llama al metodo de anyadir socio
		Mockito.verify(daoMock, never()).addSocio(null);
	}

	// 10.1 h
	@Test(expected = ExcepcionCamposNulos.class)
	public void testDarAltaSocioCamposNulosH()
			throws ExcepcionSocioYaExistente, ExcepcionCamposNoValidos, ExcepcionCamposNulos {

		Credenciales credencialesSocio = new Credenciales();
		credencialesSocio.setUsuario("albert89");
		credencialesSocio.setContrasenya("dsfgf45");
		credencialesSocio.setAdmin(false);

		Socio nuevoSocio = new Socio();
		nuevoSocio.setIdSocio("18594125L");
		nuevoSocio.setNombreSocio("alberto");
		nuevoSocio.setDireccion("calle Mayor 12");
		nuevoSocio.setTelefono(670885548);
		nuevoSocio.setCredenciales(credencialesSocio);

		videoclubAdmin.darAltaSocio(nuevoSocio);
		// Compruebo que no se llama al metodo de anyadir socio
		Mockito.verify(daoMock, never()).addSocio(null);
	}

	// 10.2 a
	@Test(expected = ExcepcionSocioYaExistente.class)
	public void testDarAltaSocioYaExistenteA()
			throws ExcepcionSocioYaExistente, ExcepcionCamposNoValidos, ExcepcionCamposNulos {
		// Socio antiguo
		Credenciales credencialesSocioAnterior = new Credenciales("albert89", "cualquiera", false);
		Socio socioAnterior = new Socio("18594125L", "alberto torres", "calle enmedio 12", 680559985, "al89@gmail.com",
				credencialesSocioAnterior);
		// Socio nuevo
		Credenciales credencialesSocio = new Credenciales();
		credencialesSocio.setUsuario("albert89");
		credencialesSocio.setContrasenya("dsfgf45");
		credencialesSocio.setAdmin(false);
		Socio nuevoSocio = new Socio();
		nuevoSocio.setIdSocio("18594125L");
		nuevoSocio.setNombreSocio("alberto");
		nuevoSocio.setDireccion("calle Mayor 12");
		nuevoSocio.setTelefono(670885548);
		nuevoSocio.setCorreo("alberto89@gmail.com");
		nuevoSocio.setCredenciales(credencialesSocio);

		when(daoMock.getSocioPorId("18594125L")).thenReturn(socioAnterior);
		when(daoMock.getSocioPorUsuario("albert89")).thenReturn(socioAnterior);
		videoclubAdmin.darAltaSocio(nuevoSocio);
		// Compruebo que no se llama al metodo de anyadir socio
		Mockito.verify(daoMock, never()).addSocio(null);
	}

	// 10.2 b
	@Test(expected = ExcepcionSocioYaExistente.class)
	public void testDarAltaSocioYaExistenteB()
			throws ExcepcionSocioYaExistente, ExcepcionCamposNoValidos, ExcepcionCamposNulos {
		// Socio antiguo
		Credenciales credencialesSocioAnterior = new Credenciales("albertoTorres", "cualquiera", false);
		Socio socioAnterior = new Socio("18594125L", "alberto torres", "calle enmedio 12", 680559985, "al89@gmail.com",
				credencialesSocioAnterior);
		// Socio nuevo
		Credenciales credencialesSocio = new Credenciales();
		credencialesSocio.setUsuario("albert89");
		credencialesSocio.setContrasenya("dsfgf45");
		credencialesSocio.setAdmin(false);
		Socio nuevoSocio = new Socio();
		nuevoSocio.setIdSocio("18594125L");
		nuevoSocio.setNombreSocio("alberto");
		nuevoSocio.setDireccion("calle Mayor 12");
		nuevoSocio.setTelefono(670885548);
		nuevoSocio.setCorreo("alberto89@gmail.com");
		nuevoSocio.setCredenciales(credencialesSocio);

		when(daoMock.getSocioPorId("18594125L")).thenReturn(socioAnterior);
		when(daoMock.getSocioPorUsuario("albertoTorres")).thenReturn(null);
		videoclubAdmin.darAltaSocio(nuevoSocio);
		// Compruebo que no se llama al metodo de anyadir socio
		Mockito.verify(daoMock, never()).addSocio(null);
	}

	// 10.2 c
	@Test(expected = ExcepcionSocioYaExistente.class)
	public void testDarAltaSocioYaExistenteC()
			throws ExcepcionSocioYaExistente, ExcepcionCamposNoValidos, ExcepcionCamposNulos {
		// Socio antiguo
		Credenciales credencialesSocioAnterior = new Credenciales("albert89", "cualquiera", false);
		Socio socioAnterior = new Socio("20415897J", "alberto torres", "calle enmedio 12", 680559985, "al89@gmail.com",
				credencialesSocioAnterior);
		// Socio nuevo
		Credenciales credencialesSocio = new Credenciales();
		credencialesSocio.setUsuario("albert89");
		credencialesSocio.setContrasenya("dsfgf45");
		credencialesSocio.setAdmin(false);
		Socio nuevoSocio = new Socio();
		nuevoSocio.setIdSocio("18594125L");
		nuevoSocio.setNombreSocio("alberto");
		nuevoSocio.setDireccion("calle Mayor 12");
		nuevoSocio.setTelefono(670885548);
		nuevoSocio.setCorreo("alberto89@gmail.com");
		nuevoSocio.setCredenciales(credencialesSocio);

		when(daoMock.getSocioPorId("20415897J")).thenReturn(null);
		when(daoMock.getSocioPorUsuario("albert89")).thenReturn(socioAnterior);
		videoclubAdmin.darAltaSocio(nuevoSocio);
		// Compruebo que no se llama al metodo de anyadir socio
		Mockito.verify(daoMock, never()).addSocio(null);
	}

	// 10.3
	@Test
	public void testDarAltaSocioDatosValidos()
			throws ExcepcionSocioYaExistente, ExcepcionCamposNoValidos, ExcepcionCamposNulos {

		Credenciales credencialesSocio = new Credenciales("albert89", "dsfgf45", false);
		Socio nuevoSocio = new Socio("18594125L", "alberto", "calle Mayor 12", 670885548, "alberto89@gmail.com",
				credencialesSocio);
		videoclubAdmin.darAltaSocio(nuevoSocio);
		ArgumentCaptor<Socio> argumentCaptor = ArgumentCaptor.forClass(Socio.class);
		// Compruebo que se llama al metodo de anyadir socio
		Mockito.verify(daoMock, times(1)).addSocio(argumentCaptor.capture());
		// Compruebo que la fecha ultimo pago es correcta
		assertEquals(fechaUltimoDiaMesAnterior(hoy()), argumentCaptor.getValue().getUltimoPago());
	}

	// 10.4 a
	@Test(expected = ExcepcionCamposNoValidos.class)
	public void testDarAltaSocioCamposInvalidosA()
			throws ExcepcionSocioYaExistente, ExcepcionCamposNoValidos, ExcepcionCamposNulos {
		// Socio nuevo
		Credenciales credencialesSocio = new Credenciales("albert89", "dsfgf45", false);
		Socio nuevoSocio = new Socio("hola", "alberto", "calle Mayor 12", 670885548, "alberto89@gmail.com",
				credencialesSocio);

		when(daoMock.getSocioPorId("hola")).thenReturn(null);
		when(daoMock.getSocioPorUsuario("albert89")).thenReturn(null);
		videoclubAdmin.darAltaSocio(nuevoSocio);
		// Compruebo que no se llama al metodo de anyadir socio
		Mockito.verify(daoMock, never()).addSocio(null);
	}

	// 10.4 b
	@Test(expected = ExcepcionCamposNoValidos.class)
	public void testDarAltaSocioCamposInvalidosB()
			throws ExcepcionSocioYaExistente, ExcepcionCamposNoValidos, ExcepcionCamposNulos {
		// Socio nuevo
		Credenciales credencialesSocio = new Credenciales("albert89", "dsfgf45", false);
		Socio nuevoSocio = new Socio("18594125L", "4585", "calle Mayor 12", 670885548, "alberto89@gmail.com",
				credencialesSocio);

		when(daoMock.getSocioPorId("18594125L")).thenReturn(null);
		when(daoMock.getSocioPorUsuario("albert89")).thenReturn(null);
		videoclubAdmin.darAltaSocio(nuevoSocio);
		// Compruebo que no se llama al metodo de anyadir socio
		Mockito.verify(daoMock, never()).addSocio(null);
	}

	// 10.4 c
	@Test(expected = ExcepcionCamposNoValidos.class)
	public void testDarAltaSocioCamposInvalidosC()
			throws ExcepcionSocioYaExistente, ExcepcionCamposNoValidos, ExcepcionCamposNulos {
		// Socio nuevo
		Credenciales credencialesSocio = new Credenciales("albert89", "dsfgf45", false);
		Socio nuevoSocio = new Socio("18594125L", "alberto", "      ", 670885548, "alberto89@gmail.com",
				credencialesSocio);

		when(daoMock.getSocioPorId("18594125L")).thenReturn(null);
		when(daoMock.getSocioPorUsuario("albert89")).thenReturn(null);
		videoclubAdmin.darAltaSocio(nuevoSocio);
		// Compruebo que no se llama al metodo de anyadir socio
		Mockito.verify(daoMock, never()).addSocio(null);
	}

	// 10.4 d
	@Test(expected = ExcepcionCamposNoValidos.class)
	public void testDarAltaSocioCamposInvalidosD()
			throws ExcepcionSocioYaExistente, ExcepcionCamposNoValidos, ExcepcionCamposNulos {
		// Socio nuevo
		Credenciales credencialesSocio = new Credenciales("albert89", "dsfgf45", false);
		Socio nuevoSocio = new Socio("18594125L", "4585", "calle Mayor 12", 6708, "alberto89@gmail.com",
				credencialesSocio);

		when(daoMock.getSocioPorId("18594125L")).thenReturn(null);
		when(daoMock.getSocioPorUsuario("albert89")).thenReturn(null);
		videoclubAdmin.darAltaSocio(nuevoSocio);
		// Compruebo que no se llama al metodo de anyadir socio
		Mockito.verify(daoMock, never()).addSocio(null);
	}

	// 10.4 e
	@Test(expected = ExcepcionCamposNoValidos.class)
	public void testDarAltaSocioCamposInvalidosE()
			throws ExcepcionSocioYaExistente, ExcepcionCamposNoValidos, ExcepcionCamposNulos {
		// Socio nuevo
		Credenciales credencialesSocio = new Credenciales("albert89", "dsfgf45", false);
		Socio nuevoSocio = new Socio("18594125L", "4585", "calle Mayor 12", 670885548, "alberto89gmail.com",
				credencialesSocio);

		when(daoMock.getSocioPorId("18594125L")).thenReturn(null);
		when(daoMock.getSocioPorUsuario("albert89")).thenReturn(null);
		videoclubAdmin.darAltaSocio(nuevoSocio);
		// Compruebo que no se llama al metodo de anyadir socio
		Mockito.verify(daoMock, never()).addSocio(null);
	}

	// 10.4 f
	@Test(expected = ExcepcionCamposNoValidos.class)
	public void testDarAltaSocioCamposInvalidosF()
			throws ExcepcionSocioYaExistente, ExcepcionCamposNoValidos, ExcepcionCamposNulos {
		// Socio nuevo
		Credenciales credencialesSocio = new Credenciales("albert89__*", "dsfgf45", false);
		Socio nuevoSocio = new Socio("18594125L", "4585", "calle Mayor 12", 670885548, "alberto89@gmail.com",
				credencialesSocio);

		when(daoMock.getSocioPorId("18594125L")).thenReturn(null);
		when(daoMock.getSocioPorUsuario("albert89__*")).thenReturn(null);
		videoclubAdmin.darAltaSocio(nuevoSocio);
		// Compruebo que no se llama al metodo de anyadir socio
		Mockito.verify(daoMock, never()).addSocio(null);
	}

	// 10.4 g
	@Test(expected = ExcepcionCamposNoValidos.class)
	public void testDarAltaSocioCamposInvalidosG()
			throws ExcepcionSocioYaExistente, ExcepcionCamposNoValidos, ExcepcionCamposNulos {
		// Socio nuevo
		Credenciales credencialesSocio = new Credenciales("albert89", "     ", false);
		Socio nuevoSocio = new Socio("18594125L", "4585", "calle Mayor 12", 670885548, "alberto89@gmail.com",
				credencialesSocio);

		when(daoMock.getSocioPorId("18594125L")).thenReturn(null);
		when(daoMock.getSocioPorUsuario("albert89")).thenReturn(null);
		videoclubAdmin.darAltaSocio(nuevoSocio);
		// Compruebo que no se llama al metodo de anyadir socio
		Mockito.verify(daoMock, never()).addSocio(null);
	}

}
