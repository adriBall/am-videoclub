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
import excepciones.ExcepcionCamposNoValidos;
import excepciones.ExcepcionCamposNulos;
import excepciones.ExcepcionNoPermitido;
import excepciones.ExcepcionSocioYaExistente;
import modelo.IVideoclubSocio;
import modelo.VideoclubSocio;

// HU06
public class TestSocioModificaSusDatos {
	private IVideoclubSocio videoclub;
	private IDAOVideoclub daoMock;
	private Credenciales credencialesSocio;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	// Inicializamos el test
	@Before
	public void initTest() throws Exception {
		daoMock = Mockito.mock(IDAOVideoclub.class);
		videoclub = new VideoclubSocio(daoMock);

		credencialesSocio = new Credenciales();
		credencialesSocio.setUsuario("Manel33");
		credencialesSocio.setContrasenya("ef58sefe");
		credencialesSocio.setAdmin(false);
		when(daoMock.comprobarCredenciales(credencialesSocio.getUsuario(), credencialesSocio.getContrasenya()))
				.thenReturn(credencialesSocio);
		videoclub.iniciarSesion("Manel33", "ef58sefe");

		Socio socio = new Socio();
		socio.setIdSocio("21569888K");
		socio.setNombreSocio("Manuel Gual");
		socio.setDireccion("calle Colmenar num. 2");
		socio.setTelefono(690887887);
		socio.setCorreo("manel03@yahoo.com");
		socio.setCredenciales(credencialesSocio);
		when(daoMock.getSocioPorId("21569888K")).thenReturn(socio);
		when(daoMock.getSocioPorUsuario("Manel33")).thenReturn(socio);

	}

	// E6.1
	@Test
	public void socioModificaSusDatosPwdCorrecto()
			throws ExcepcionSocioYaExistente, ExcepcionCamposNoValidos, ExcepcionCamposNulos, ExcepcionNoPermitido {
		Socio nuevosDatos = daoMock.getSocioPorUsuario(credencialesSocio.getUsuario());
		Credenciales nuevosCredenciales = new Credenciales();
		nuevosCredenciales.setUsuario(credencialesSocio.getUsuario());
		nuevosCredenciales.setContrasenya("errefs485");
		nuevosCredenciales.setAdmin(false);
		nuevosDatos.setCredenciales(nuevosCredenciales);

		videoclub.actualizarMisDatos(nuevosDatos);

		ArgumentCaptor<Credenciales> argumentCaptor1 = ArgumentCaptor.forClass(Credenciales.class);
		ArgumentCaptor<Socio> argumentCaptor2 = ArgumentCaptor.forClass(Socio.class);
		// Compruebo que se llama al metodo
		Mockito.verify(daoMock, times(1)).modificarSocio(argumentCaptor1.capture(), argumentCaptor2.capture());
		Assert.assertEquals("errefs485", argumentCaptor2.getValue().getCredenciales().getContrasenya());
	}

	// E6.1 var.
	@Test
	public void socioModificaSusDatosUsuarioCorrecto()
			throws ExcepcionSocioYaExistente, ExcepcionCamposNoValidos, ExcepcionCamposNulos, ExcepcionNoPermitido {
		Socio nuevosDatos = daoMock.getSocioPorUsuario(credencialesSocio.getUsuario());
		Credenciales nuevosCredenciales = new Credenciales();
		nuevosCredenciales.setUsuario("nuevo");
		nuevosCredenciales.setContrasenya(credencialesSocio.getContrasenya());
		nuevosCredenciales.setAdmin(false);
		nuevosDatos.setCredenciales(nuevosCredenciales);

		videoclub.actualizarMisDatos(nuevosDatos);

		ArgumentCaptor<Credenciales> argumentCaptor1 = ArgumentCaptor.forClass(Credenciales.class);
		ArgumentCaptor<Socio> argumentCaptor2 = ArgumentCaptor.forClass(Socio.class);
		// Compruebo que se llama al metodo
		Mockito.verify(daoMock, times(1)).modificarSocio(argumentCaptor1.capture(), argumentCaptor2.capture());
		Assert.assertEquals("nuevo", argumentCaptor2.getValue().getCredenciales().getUsuario());
	}

	// E6.1 var.
	@Test
	public void socioModificaSusDatosIdCorrecto()
			throws ExcepcionSocioYaExistente, ExcepcionCamposNoValidos, ExcepcionCamposNulos, ExcepcionNoPermitido {
		Socio nuevosDatos = daoMock.getSocioPorUsuario(credencialesSocio.getUsuario());
		nuevosDatos.setIdSocio("64182745Y");

		videoclub.actualizarMisDatos(nuevosDatos);

		ArgumentCaptor<Credenciales> argumentCaptor1 = ArgumentCaptor.forClass(Credenciales.class);
		ArgumentCaptor<Socio> argumentCaptor2 = ArgumentCaptor.forClass(Socio.class);
		// Compruebo que se llama al metodo
		Mockito.verify(daoMock, times(1)).modificarSocio(argumentCaptor1.capture(), argumentCaptor2.capture());
		Assert.assertEquals("64182745Y", argumentCaptor2.getValue().getIdSocio());
	}

	// E6.1 var.
	@Test
	public void socioModificaSusDatosNombreCorrecto()
			throws ExcepcionSocioYaExistente, ExcepcionCamposNoValidos, ExcepcionCamposNulos, ExcepcionNoPermitido {
		Socio nuevosDatos = daoMock.getSocioPorUsuario(credencialesSocio.getUsuario());
		nuevosDatos.setNombreSocio("Nuevo Nombre");

		videoclub.actualizarMisDatos(nuevosDatos);

		ArgumentCaptor<Credenciales> argumentCaptor1 = ArgumentCaptor.forClass(Credenciales.class);
		ArgumentCaptor<Socio> argumentCaptor2 = ArgumentCaptor.forClass(Socio.class);
		// Compruebo que se llama al metodo
		Mockito.verify(daoMock, times(1)).modificarSocio(argumentCaptor1.capture(), argumentCaptor2.capture());
		Assert.assertEquals("Nuevo Nombre", argumentCaptor2.getValue().getNombreSocio());
	}

	// E6.1 var.
	@Test
	public void socioModificaSusDatosDireccCorrecta()
			throws ExcepcionCamposNoValidos, ExcepcionSocioYaExistente, ExcepcionCamposNulos, ExcepcionNoPermitido {
		Socio nuevosDatos = daoMock.getSocioPorUsuario(credencialesSocio.getUsuario());
		nuevosDatos.setDireccion("Calle nueva n2");

		videoclub.actualizarMisDatos(nuevosDatos);

		ArgumentCaptor<Credenciales> argumentCaptor1 = ArgumentCaptor.forClass(Credenciales.class);
		ArgumentCaptor<Socio> argumentCaptor2 = ArgumentCaptor.forClass(Socio.class);
		// Compruebo que se llama al metodo
		Mockito.verify(daoMock, times(1)).modificarSocio(argumentCaptor1.capture(), argumentCaptor2.capture());
		Assert.assertEquals("Calle nueva n2", argumentCaptor2.getValue().getDireccion());
	}

	// E6.1 var.
	@Test
	public void socioModificaSusDatosTelefonoCorrecto()
			throws ExcepcionCamposNoValidos, ExcepcionSocioYaExistente, ExcepcionCamposNulos, ExcepcionNoPermitido {
		Socio nuevosDatos = daoMock.getSocioPorUsuario(credencialesSocio.getUsuario());
		nuevosDatos.setTelefono(645781546);

		videoclub.actualizarMisDatos(nuevosDatos);

		ArgumentCaptor<Credenciales> argumentCaptor1 = ArgumentCaptor.forClass(Credenciales.class);
		ArgumentCaptor<Socio> argumentCaptor2 = ArgumentCaptor.forClass(Socio.class);
		// Compruebo que se llama al metodo
		Mockito.verify(daoMock, times(1)).modificarSocio(argumentCaptor1.capture(), argumentCaptor2.capture());
		Assert.assertEquals((int) 645781546, (int) argumentCaptor2.getValue().getTelefono());
	}

	// E6.1 var.
	@Test
	public void socioModificaSusDatosCorreoCorrecto()
			throws ExcepcionCamposNoValidos, ExcepcionSocioYaExistente, ExcepcionCamposNulos, ExcepcionNoPermitido {
		Socio nuevosDatos = daoMock.getSocioPorUsuario(credencialesSocio.getUsuario());
		nuevosDatos.setCorreo("nuevo@gmail.com");

		videoclub.actualizarMisDatos(nuevosDatos);

		ArgumentCaptor<Credenciales> argumentCaptor1 = ArgumentCaptor.forClass(Credenciales.class);
		ArgumentCaptor<Socio> argumentCaptor2 = ArgumentCaptor.forClass(Socio.class);
		// Compruebo que se llama al metodo
		Mockito.verify(daoMock, times(1)).modificarSocio(argumentCaptor1.capture(), argumentCaptor2.capture());
		Assert.assertEquals("nuevo@gmail.com", argumentCaptor2.getValue().getCorreo());
	}

	// E6.2
	@Test
	public void socioModificaSusDatosTelefonoNoValido()
			throws ExcepcionCamposNoValidos, ExcepcionSocioYaExistente, ExcepcionCamposNulos, ExcepcionNoPermitido {
		Socio nuevosDatos = daoMock.getSocioPorUsuario(credencialesSocio.getUsuario());
		// Excepcion esperada
		thrown.expect(NumberFormatException.class);
		nuevosDatos.setTelefono(Integer.parseInt("no deseo poner mi numero"));

		videoclub.actualizarMisDatos(nuevosDatos);
	}

	// E6.2 var
	@Test
	public void socioModificaSusDatosIdNoValido()
			throws ExcepcionCamposNoValidos, ExcepcionSocioYaExistente, ExcepcionCamposNulos, ExcepcionNoPermitido {
		Socio nuevosDatos = daoMock.getSocioPorUsuario(credencialesSocio.getUsuario());
		// Excepcion esperada
		thrown.expect(ExcepcionCamposNoValidos.class);
		nuevosDatos.setIdSocio("");

		videoclub.actualizarMisDatos(nuevosDatos);
	}

	// E6.2 var
	@Test
	public void socioModificaSusDatosNombreNoValido()
			throws ExcepcionCamposNoValidos, ExcepcionSocioYaExistente, ExcepcionCamposNulos, ExcepcionNoPermitido {
		Socio nuevosDatos = daoMock.getSocioPorUsuario(credencialesSocio.getUsuario());
		// Excepcion esperada
		thrown.expect(ExcepcionCamposNoValidos.class);
		nuevosDatos.setNombreSocio("");

		videoclub.actualizarMisDatos(nuevosDatos);
	}

	// E6.2 var
	@Test
	public void socioModificaSusDatosDirNoValida()
			throws ExcepcionCamposNoValidos, ExcepcionCamposNulos, ExcepcionSocioYaExistente, ExcepcionNoPermitido {
		Socio nuevosDatos = daoMock.getSocioPorUsuario(credencialesSocio.getUsuario());
		// Excepcion esperada
		thrown.expect(ExcepcionCamposNoValidos.class);
		nuevosDatos.setDireccion("");

		videoclub.actualizarMisDatos(nuevosDatos);
	}

	// E6.2 var
	@Test
	public void socioModificaSusDatosCorreoNoValido()
			throws ExcepcionCamposNoValidos, ExcepcionCamposNulos, ExcepcionSocioYaExistente, ExcepcionNoPermitido {
		Socio nuevosDatos = daoMock.getSocioPorUsuario(credencialesSocio.getUsuario());
		// Excepcion esperada
		thrown.expect(ExcepcionCamposNoValidos.class);
		nuevosDatos.setCorreo("");

		videoclub.actualizarMisDatos(nuevosDatos);
	}

	// E6.2 var
	@Test
	public void socioModificaSusDatosUsuarioNoValido()
			throws ExcepcionCamposNoValidos, ExcepcionSocioYaExistente, ExcepcionCamposNulos, ExcepcionNoPermitido {
		Socio nuevosDatos = daoMock.getSocioPorUsuario(credencialesSocio.getUsuario());
		// Excepcion esperada
		thrown.expect(ExcepcionCamposNoValidos.class);
		nuevosDatos.getCredenciales().setUsuario("");

		videoclub.actualizarMisDatos(nuevosDatos);
	}

	// E6.2 var
	@Test
	public void socioModificaSusDatosPwdNoValido()
			throws ExcepcionCamposNoValidos, ExcepcionSocioYaExistente, ExcepcionCamposNulos, ExcepcionNoPermitido {
		Socio nuevosDatos = daoMock.getSocioPorUsuario(credencialesSocio.getUsuario());
		// Excepcion esperada
		thrown.expect(ExcepcionCamposNoValidos.class);
		nuevosDatos.getCredenciales().setContrasenya("");

		videoclub.actualizarMisDatos(nuevosDatos);
	}

	// E6.3
	@Test
	public void socioModificaSusDatosDireccVacia()
			throws ExcepcionCamposNoValidos, ExcepcionSocioYaExistente, ExcepcionCamposNulos, ExcepcionNoPermitido {
		Socio nuevosDatos = daoMock.getSocioPorUsuario(credencialesSocio.getUsuario());
		nuevosDatos.setDireccion(null);

		// Excepcion esperada
		thrown.expect(ExcepcionCamposNulos.class);
		videoclub.actualizarMisDatos(nuevosDatos);

	}

	// E6.3 var.
	@Test
	public void socioModificaSusDatosIdVacio()
			throws ExcepcionCamposNoValidos, ExcepcionSocioYaExistente, ExcepcionCamposNulos, ExcepcionNoPermitido {
		Socio nuevosDatos = daoMock.getSocioPorUsuario(credencialesSocio.getUsuario());
		nuevosDatos.setIdSocio(null);

		// Excepcion esperada
		thrown.expect(ExcepcionCamposNulos.class);
		videoclub.actualizarMisDatos(nuevosDatos);

	}

	// E6.3 var.
	@Test
	public void socioModificaSusDatosNombreVacio()
			throws ExcepcionCamposNoValidos, ExcepcionSocioYaExistente, ExcepcionCamposNulos, ExcepcionNoPermitido {
		Socio nuevosDatos = daoMock.getSocioPorUsuario(credencialesSocio.getUsuario());
		nuevosDatos.setNombreSocio(null);

		// Excepcion esperada
		thrown.expect(ExcepcionCamposNulos.class);
		videoclub.actualizarMisDatos(nuevosDatos);

	}

	// E6.3 var.
	@Test
	public void socioModificaSusDatosTelefonoVacio()
			throws ExcepcionCamposNoValidos, ExcepcionSocioYaExistente, ExcepcionCamposNulos, ExcepcionNoPermitido {
		Socio nuevosDatos = daoMock.getSocioPorUsuario(credencialesSocio.getUsuario());
		nuevosDatos.setTelefono(null);

		// Excepcion esperada
		thrown.expect(ExcepcionCamposNulos.class);
		videoclub.actualizarMisDatos(nuevosDatos);

	}

	// E6.3 var.
	@Test
	public void socioModificaSusDatosCorreoVacio()
			throws ExcepcionCamposNoValidos, ExcepcionSocioYaExistente, ExcepcionCamposNulos, ExcepcionNoPermitido {
		Socio nuevosDatos = daoMock.getSocioPorUsuario(credencialesSocio.getUsuario());
		nuevosDatos.setCorreo(null);

		// Excepcion esperada
		thrown.expect(ExcepcionCamposNulos.class);
		videoclub.actualizarMisDatos(nuevosDatos);

	}

	// E6.3 var.
	@Test
	public void socioModificaSusDatosCredencialesNulos()
			throws ExcepcionCamposNoValidos, ExcepcionSocioYaExistente, ExcepcionCamposNulos, ExcepcionNoPermitido {
		Socio nuevosDatos = daoMock.getSocioPorUsuario(credencialesSocio.getUsuario());
		nuevosDatos.setCredenciales(null);

		// Excepcion esperada
		thrown.expect(ExcepcionCamposNulos.class);
		videoclub.actualizarMisDatos(nuevosDatos);

	}

	// E6.3 var.
	@Test
	public void socioModificaSusDatosUsuarioVacio()
			throws ExcepcionCamposNoValidos, ExcepcionSocioYaExistente, ExcepcionCamposNulos, ExcepcionNoPermitido {
		Socio nuevosDatos = daoMock.getSocioPorUsuario(credencialesSocio.getUsuario());
		nuevosDatos.getCredenciales().setUsuario(null);

		// Excepcion esperada
		thrown.expect(ExcepcionCamposNulos.class);
		videoclub.actualizarMisDatos(nuevosDatos);

	}

	// E6.3 var.
	@Test
	public void socioModificaSusDatosPwdVacio()
			throws ExcepcionCamposNoValidos, ExcepcionCamposNulos, ExcepcionSocioYaExistente, ExcepcionNoPermitido {
		Socio nuevosDatos = daoMock.getSocioPorUsuario(credencialesSocio.getUsuario());
		nuevosDatos.getCredenciales().setContrasenya(null);

		// Excepcion esperada
		thrown.expect(ExcepcionCamposNulos.class);
		videoclub.actualizarMisDatos(nuevosDatos);

	}

	// E6.4
	@Test
	public void socioModificaSuFechaUltimoPago()
			throws ExcepcionCamposNoValidos, ExcepcionCamposNulos, ExcepcionSocioYaExistente, ExcepcionNoPermitido {
		Socio actualesDatos = daoMock.getSocioPorUsuario(credencialesSocio.getUsuario());
		Socio nuevosDatos = new Socio();
		nuevosDatos.setIdSocio(actualesDatos.getIdSocio());
		nuevosDatos.setCorreo(actualesDatos.getCorreo());
		nuevosDatos.setCredenciales(actualesDatos.getCredenciales());
		nuevosDatos.setDireccion(actualesDatos.getDireccion());
		nuevosDatos.setNombreSocio(actualesDatos.getNombreSocio());
		nuevosDatos.setTelefono(actualesDatos.getTelefono());
		// 9 quiere decir 10 (octubre). La numeracion de meses empieza desde 0
		nuevosDatos.setUltimoPago(getDate(31, 9, 2016));

		// Excepcion esperada
		thrown.expect(ExcepcionNoPermitido.class);
		videoclub.actualizarMisDatos(nuevosDatos);

	}

}
