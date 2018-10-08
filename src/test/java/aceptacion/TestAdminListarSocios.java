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
import dominio.Socio;
import excepciones.ExcepcionNoExistenSocios;
import modelo.IVideoclubAdmin;
import modelo.VideoclubAdmin;

//HU11, el admin lista todos los socios del sistema
public class TestAdminListarSocios {

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

	// 11.1 a: Existe un socio en el sistema
	@Test
	public void testListarSociosCuandoHayUno() throws ExcepcionNoExistenSocios {

		Credenciales credencialesSocio = new Credenciales("Dani90", "loquwse", false);
		Socio socio = new Socio("20145896L", "Daniel Rodiguez", "plaza de la paz 1", 650449785, "dani90@gmail.com",
				credencialesSocio);
		List<Socio> listaSocios = new ArrayList<Socio>();
		listaSocios.add(socio);
		when(daoMock.listarSociosVideoclub()).thenReturn(listaSocios);

		List<Socio> lista = videoclubAdmin.listarSocios();
		assertTrue(lista.contains(socio));
	}

	// 11.1 b: Existen varios socios en el sistema
	@Test
	public void testListarSociosCuandoHayVarios() throws ExcepcionNoExistenSocios {

		Credenciales credencialesSocio = new Credenciales("Dani90", "loquwse", false);
		Socio socio = new Socio("20145896L", "Daniel Rodiguez", "plaza de la paz 1", 650449785, "dani90@gmail.com",
				credencialesSocio);
		Credenciales credencialesSocio2 = new Credenciales("Laura23", "drvrd", false);
		Socio socio2 = new Socio("18569748L", "Laura Mesero", "calle madrid 4", 670525698, "laura23@gmail.com",
				credencialesSocio2);
		Credenciales credencialesSocio3 = new Credenciales("Pedro12", "fdvrv23", false);
		Socio socio3 = new Socio("19502444K", "Pedro Martin", "calle tarragona 1, 1B", 670412365, "pedro90@gmail.com",
				credencialesSocio3);
		List<Socio> listaSocios = new ArrayList<Socio>();
		listaSocios.add(socio);
		listaSocios.add(socio2);
		listaSocios.add(socio3);
		when(daoMock.listarSociosVideoclub()).thenReturn(listaSocios);

		List<Socio> lista = videoclubAdmin.listarSocios();
		assertTrue(lista.contains(socio));
		assertTrue(lista.contains(socio2));
		assertTrue(lista.contains(socio3));
	}

	// 11.2: No hay socios en el sistema
	@Test(expected = ExcepcionNoExistenSocios.class)
	public void testListarSociosNoHaySocios() throws ExcepcionNoExistenSocios {
		List<Socio> listaSocios = new ArrayList<Socio>();
		when(daoMock.listarSociosVideoclub()).thenReturn(listaSocios);

		videoclubAdmin.listarSocios();

	}

}