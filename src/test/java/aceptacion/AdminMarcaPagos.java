package aceptacion;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Date;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import static util.Fechas.*;
import dao.IDAOVideoclub;
import dominio.Credenciales;
import dominio.Socio;
import excepciones.ExcepcionSocioNoExiste;
import modelo.IVideoclubAdmin;
import modelo.VideoclubAdmin;

//HU25, el administrador marca que un socio ha pagado todas sus cuotas pendientes
public class AdminMarcaPagos {
	
	private IDAOVideoclub daoMock; //objeto mock que adopta la interfaz de IDAOVideoclub para simular su comportamiento
	private Credenciales credencialesAdmin;
	private IVideoclubAdmin videoclubAdmin;	
	private Socio socio;

	@Before
	public void initTest() throws Exception {
		
		daoMock = Mockito.mock(IDAOVideoclub.class); //creacion del doble de prueba
		videoclubAdmin = new VideoclubAdmin(daoMock); //inyectamos el daoMock en la clase a probar
		
		//el administrador inicia sesión
		credencialesAdmin = new Credenciales("Kodos86","ythjfr84",true);
		when(daoMock.comprobarCredenciales(credencialesAdmin.getUsuario(), credencialesAdmin.getContrasenya())).thenReturn(credencialesAdmin);
		videoclubAdmin.iniciarSesion("Kodos86","ythjfr84");	
		
		//existe un socio moroso
		Credenciales credencialesSocio = new Credenciales ("Santi00", "ergrg48", false);
		socio = new Socio ("98765432H","Santiago Manzano","Calle Tarragona",690787844,"santi00@gmail.com",credencialesSocio);	
		when(daoMock.getSocioPorId("98765432H")).thenReturn(socio);
	}
	
	//EHU 25.1
	@SuppressWarnings("deprecation")
	@Test
	public void AdminPagarMensualidad() throws ExcepcionSocioNoExiste {
		//el socio debe una mensualidad
		socio.setUltimoPago(new Date(2016, 11, 30));
		//el admin marca el pago
		videoclubAdmin.pagarMensualidadSocio(socio);
		ArgumentCaptor<Socio> paramCaptor = ArgumentCaptor.forClass(Socio.class);
		verify(daoMock, times(1)).pagarMensualidad(paramCaptor.capture());
		when(daoMock.getUltimoPago(socio)).thenReturn(fechaUltimoDiaMesAnterior(hoy()));
		Assert.assertEquals(daoMock.getUltimoPago(socio), fechaUltimoDiaMesAnterior(hoy()));		
	}
	
	//EHU 25.2
	@Test
	public void AdminPagarMensualidadYaPagada() throws ExcepcionSocioNoExiste {
		socio.setUltimoPago(fechaUltimoDiaMesAnterior(hoy()));
		videoclubAdmin.pagarMensualidadSocio(socio);
		ArgumentCaptor<Socio> paramCaptor = ArgumentCaptor.forClass(Socio.class);
		verify(daoMock, times(1)).pagarMensualidad(paramCaptor.capture()); 
		when(daoMock.getUltimoPago(socio)).thenReturn(fechaUltimoDiaMesAnterior(hoy()));
		Assert.assertEquals(daoMock.getUltimoPago(socio), fechaUltimoDiaMesAnterior(hoy()));	
	}
	
	//EHU 25.3
	@SuppressWarnings("deprecation")
	@Test
	public void AdminPagarMensualidadPendiente() throws ExcepcionSocioNoExiste {
		fechaUltimoDiaMesAnterior(new Date(2016, 9, 30));
		videoclubAdmin.pagarMensualidadSocio(socio);
		ArgumentCaptor<Socio> paramCaptor = new ArgumentCaptor<>();
		verify(daoMock, times(1)).pagarMensualidad(paramCaptor.capture()); 
		when(daoMock.getUltimoPago(socio)).thenReturn(fechaUltimoDiaMesAnterior(hoy()));
		Assert.assertEquals(daoMock.getUltimoPago(socio), fechaUltimoDiaMesAnterior(hoy()));
	}

}
