package aceptacion;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import static util.Fechas.*;

import dao.IDAOVideoclub;
import dominio.Credenciales;
import dominio.Socio;
import modelo.IVideoclubAdmin;
import modelo.VideoclubAdmin;

//HU12 el administrador consulta datos de un socio
public class TestAdminConsultarDatosSocio {

	private IDAOVideoclub daoMock; // objeto mock que adopta la interfaz de IDAOVideoclub para simular su
									// comportamiento
	private Credenciales credencialesAdmin;
	private IVideoclubAdmin videoclubAdmin;

	@Before
	public void initTest() throws Exception {

		daoMock = Mockito.mock(IDAOVideoclub.class); // creacion del doble de prueba
		videoclubAdmin = new VideoclubAdmin(daoMock); // inyectamos el daoMock en la clase a probar

		credencialesAdmin = new Credenciales("Batmantio84", "fhftb54", true);
		when(daoMock.comprobarCredenciales(credencialesAdmin.getUsuario(), credencialesAdmin.getContrasenya()))
				.thenReturn(credencialesAdmin);
		videoclubAdmin.iniciarSesion("Batmantio84", "fhftb54");

		// En el sistema existe este socio:
		Credenciales credencialesSocio = new Credenciales("Dani90", "64dfe854", false);
		Socio socio = new Socio("20145896L", "Daniel Garcia", "calle Enmedio 12, piso 1B", 685774496,
				"dani90@gmail.com", credencialesSocio);
		socio.setUltimoPago(getDate(31, 10, 2016));
		when(daoMock.getSocioPorId("20145896L")).thenReturn(socio);
	}

	// 12.1
	@Test
	public void testConsultarDatosSocio() {

		Credenciales credencialesSocio = new Credenciales("Dani90", "64dfe854", false);
		Socio socioAComprobar = new Socio("20145896L", "Daniel Garcia", "calle Enmedio 12, piso 1B", 685774496,
				"dani90@gmail.com", credencialesSocio);
		socioAComprobar.setUltimoPago(getDate(31, 10, 2016));

		Socio socio = videoclubAdmin.verDatosSocio("20145896L");
		ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
		// Compruebo que se llama al metodo
		Mockito.verify(daoMock, times(1)).getSocioPorId(argumentCaptor.capture());
		assertEquals(socio, socioAComprobar);
	}
}