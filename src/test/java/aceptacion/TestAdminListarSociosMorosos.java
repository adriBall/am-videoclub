package aceptacion;

import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;
import org.mockito.Mockito;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static util.Fechas.*;

import java.util.Arrays;
import java.util.List;

import dao.IDAOVideoclub;
import dominio.Credenciales;
import dominio.Socio;
import modelo.IVideoclubAdmin;
import modelo.VideoclubAdmin;

//HU22
public class TestAdminListarSociosMorosos {
	private IVideoclubAdmin videoclub;
	private IDAOVideoclub daoMock;
	private Credenciales credencialesAdmin;

	// Inicializamos el test
	@Before
	public void initTest() throws Exception {
		daoMock = Mockito.mock(IDAOVideoclub.class);
		videoclub = new VideoclubAdmin(daoMock);

		credencialesAdmin = new Credenciales();
		credencialesAdmin.setUsuario("Kodos86");
		credencialesAdmin.setContrasenya("cualquiera");
		credencialesAdmin.setAdmin(true);
		when(daoMock.comprobarCredenciales(credencialesAdmin.getUsuario(), credencialesAdmin.getContrasenya()))
				.thenReturn(credencialesAdmin);
		videoclub.iniciarSesion("Kodos86", "cualquiera");

	}

	// 22.1
	@Test
	public void testListarSociosMororosCuandoNoHay() {
		Credenciales credencialesPepe = new Credenciales("Pepe12", "cualquiera", false);
		Socio pepe = new Socio("65476543D", "Pepe", "Una calle", 678564321, "pepe@pepe.es", credencialesPepe);
		when(daoMock.getSocioPorId("65476543D")).thenReturn(pepe);
		when(daoMock.getSocioPorUsuario("Pepe12")).thenReturn(pepe);

		Credenciales credencialesMaria = new Credenciales("Maria13", "cualquiera", false);
		Socio maria = new Socio("23671894D", "Maria", "Una calle", 673849208, "maria@maria.es", credencialesMaria);
		when(daoMock.getSocioPorId("23671894D")).thenReturn(maria);
		when(daoMock.getSocioPorUsuario("Maria13")).thenReturn(maria);

		List<Socio> listaSocios = Arrays.asList(new Socio[] { pepe, maria });
		when(daoMock.listarSociosVideoclub()).thenReturn(listaSocios);

		List<Socio> morosos = videoclub.listarUsuariosMorosos();
		assertTrue(morosos.isEmpty());
	}

	// 22.2
	@Test
	public void testListarSociosMororosHayUno() {
		Credenciales credencialesDavid = new Credenciales("David96", "cualquiera", false);
		Socio david = new Socio("65476543D", "David", "Una calle", 678564321, "david@david.es", credencialesDavid);
		when(daoMock.getSocioPorId("65476543D")).thenReturn(david);
		when(daoMock.getSocioPorUsuario("David96")).thenReturn(david);
		david.setUltimoPago(getDate(diasMes(mes(hoy()), anyo(hoy())), mes(hoy()), anyo(hoy()) - 1));

		Credenciales credencialesAlba = new Credenciales("Alba95", "cualquiera", false);
		Socio alba = new Socio("23671894D", "Alba", "Una calle", 673849208, "alba@alba.es", credencialesAlba);
		when(daoMock.getSocioPorId("23671894D")).thenReturn(alba);
		when(daoMock.getSocioPorUsuario("Alba95")).thenReturn(alba);

		List<Socio> listaSocios = Arrays.asList(new Socio[] { david, alba });
		when(daoMock.listarSociosVideoclub()).thenReturn(listaSocios);

		List<Socio> morosos = videoclub.listarUsuariosMorosos();
		assertTrue(morosos.size() == 1);
		Assert.assertEquals(david, morosos.get(0));

		// Tal como hemos puesto el ultimo pago de david, debera 11 o 12 meses
		// segun estemos a final de mes o no en el momento de pasar el test
		assertTrue(david.mensualidadesQueDebe() == 11 || david.mensualidadesQueDebe() == 12);
	}

	// 22.2 var.
	@Test
	public void testListarSociosMororosHayMasDeUno() {
		Credenciales credencialesDavid = new Credenciales("David96", "cualquiera", false);
		Socio david = new Socio("65476543D", "David", "Una calle", 678564321, "david@david.es", credencialesDavid);
		when(daoMock.getSocioPorId("65476543D")).thenReturn(david);
		when(daoMock.getSocioPorUsuario("David96")).thenReturn(david);
		david.setUltimoPago(getDate(diasMes(mes(hoy()), anyo(hoy())), mes(hoy()), anyo(hoy()) - 1));

		Credenciales credencialesAlba = new Credenciales("Alba95", "cualquiera", false);
		Socio alba = new Socio("23671894D", "Alba", "Una calle", 673849208, "alba@alba.es", credencialesAlba);
		when(daoMock.getSocioPorId("23671894D")).thenReturn(alba);
		when(daoMock.getSocioPorUsuario("Alba95")).thenReturn(alba);

		Credenciales credencialesPepe = new Credenciales("Pepe12", "cualquiera", false);
		Socio pepe = new Socio("65476543D", "Pepe", "Una calle", 678564321, "pepe@pepe.es", credencialesPepe);
		when(daoMock.getSocioPorId("65476543D")).thenReturn(pepe);
		when(daoMock.getSocioPorUsuario("Pepe12")).thenReturn(pepe);
		pepe.setUltimoPago(getDate(diasMes(mes(hoy()), anyo(hoy())), mes(hoy()), anyo(hoy()) - 2));

		Credenciales credencialesMaria = new Credenciales("Maria13", "cualquiera", false);
		Socio maria = new Socio("23671894D", "Maria", "Una calle", 673849208, "maria@maria.es", credencialesMaria);
		when(daoMock.getSocioPorId("23671894D")).thenReturn(maria);
		when(daoMock.getSocioPorUsuario("Maria13")).thenReturn(maria);
		maria.setUltimoPago(getDate(diasMes(mes(hoy()), anyo(hoy())), mes(hoy()), anyo(hoy()) - 1));

		List<Socio> listaSocios = Arrays.asList(new Socio[] { david, alba, pepe, maria });
		when(daoMock.listarSociosVideoclub()).thenReturn(listaSocios);

		List<Socio> morosos = videoclub.listarUsuariosMorosos();
		assertTrue(morosos.size() == 3);
	}

}