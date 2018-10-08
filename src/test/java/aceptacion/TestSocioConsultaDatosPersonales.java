package aceptacion;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import dao.IDAOVideoclub;
import dominio.Credenciales;
import dominio.Socio;
import modelo.IVideoclubSocio;
import modelo.VideoclubSocio;

//HU05 un socio consulta sus datos personales
public class TestSocioConsultaDatosPersonales {

	private IDAOVideoclub daoMock; // objeto mock que adopta la interfaz de IDAOVideoclub para simular su
									// comportamiento
	private Credenciales credencialesSocio;
	private IVideoclubSocio videoclubSocio;
	Socio socio;

	@Before
	public void initTest() throws Exception {

		daoMock = Mockito.mock(IDAOVideoclub.class); // creacion del doble de prueba
		videoclubSocio = new VideoclubSocio(daoMock); // inyectamos el daoMock en la clase a probar

		credencialesSocio = new Credenciales("Lourdes88", "cf87uiA", false);
		socio = new Socio("20458451J", "Lourdes Palacios", "calle Mayor 110 piso 5", 690335889,
				"louPalacios78@yahoo.com", credencialesSocio);

		when(daoMock.comprobarCredenciales(credencialesSocio.getUsuario(), credencialesSocio.getContrasenya()))
				.thenReturn(credencialesSocio);
		videoclubSocio.iniciarSesion("Lourdes88", "cf87uiA");

		when(daoMock.getSocioPorId("20458451J")).thenReturn(socio);
		when(daoMock.getSocioPorUsuario(credencialesSocio.getUsuario())).thenReturn(socio);

	}

	// 5.1
	@Test
	public void testConsultarDatosPersonales() {

		Socio socioRecogido = videoclubSocio.verDatosPersonales();
		ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
		// Compruebo que se llama al metodo
		Mockito.verify(daoMock, times(1)).getSocioPorUsuario(argumentCaptor.capture());
		assertEquals(socio, socioRecogido);
	}
}