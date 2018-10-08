package aceptacion;

import org.junit.Test;
import org.junit.Before;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.List;
import dao.IDAOVideoclub;
import dominio.Credenciales;
import dominio.Genero;
import dominio.Licencia;
import dominio.Pelicula;
import excepciones.ExcepcionCamposNoValidos;
import excepciones.ExcepcionCamposNulos;
import excepciones.ExcepcionPeliculaYaExistente;
import modelo.IVideoclubAdmin;
import modelo.VideoclubAdmin;

//HU16, el admin da de alta peliculas en el sistema
public class TestAdminAltaPeliculas {

	private IDAOVideoclub daoMock; // objeto mock que adopta la interfaz de IDAOVideoclub para simular su
									// comportamiento
	private Credenciales credencialesAdmin;
	private IVideoclubAdmin videoclubAdmin;

	@Before
	public void initTest() throws Exception {

		daoMock = Mockito.mock(IDAOVideoclub.class); // creacion del doble de prueba
		videoclubAdmin = new VideoclubAdmin(daoMock); // inyectamos el daoMock en la clase a probar

		credencialesAdmin = new Credenciales("Heisenberg92", "1258opi", true);
		when(daoMock.comprobarCredenciales(credencialesAdmin.getUsuario(), credencialesAdmin.getContrasenya()))
				.thenReturn(credencialesAdmin);
		videoclubAdmin.iniciarSesion("Heisenberg92", "1258opi");
	}

	// 16.1: Se intenta dar de alta una pelicula que ya existe
	@Test(expected = ExcepcionPeliculaYaExistente.class)
	public void testDarAltaPeliculaYaExistente()
			throws ExcepcionPeliculaYaExistente, ExcepcionCamposNoValidos, ExcepcionCamposNulos {

		String sinopsis = "A principios del siglo XXI, la poderosa Tyrell Corporation creó, gracias a los avances de la ingeniería genética, "
				+ "un robot llamado Nexus 6, un ser virtualmente idéntico al hombre pero superior a él en fuerza y agilidad, al que se dio el "
				+ "nombre de Replicante.";
		List<Genero> listaGeneros = new ArrayList<Genero>();
		listaGeneros.add(Genero.ACCION);
		listaGeneros.add(Genero.CIENCIA_FICCION);
		List<String> listaActores = new ArrayList<String>();
		listaActores.add("Harrison Ford");
		List<String> listaDirectores = new ArrayList<String>();
		listaDirectores.add("Ridley Scott");
		Pelicula pelicula = new Pelicula("Blade Runner", "Blade Runner", 1982, sinopsis, Licencia.BASICA, listaGeneros,
				listaActores, listaDirectores);

		when(daoMock.getPelicula("Blade Runner", 1982)).thenReturn(pelicula);
		videoclubAdmin.darAltaPelicula(pelicula);

		// Compruebo que no se llama al metodo de anyadir socio
		Mockito.verify(daoMock, never()).addPelicula(null);
	}

	// 16.2: Se da de alta una pelicula que no estaba en el sistema con datos
	// validos
	@Test
	public void testDarAltaPeliculaValida()
			throws ExcepcionPeliculaYaExistente, ExcepcionCamposNoValidos, ExcepcionCamposNulos {

		String sinopsis = "A principios del siglo XXI, la poderosa Tyrell Corporation creó, gracias a los avances de la ingeniería genética, "
				+ "un robot llamado Nexus 6, un ser virtualmente idéntico al hombre pero superior a él en fuerza y agilidad, al que se dio el "
				+ "nombre de Replicante.";
		List<Genero> listaGeneros = new ArrayList<Genero>();
		listaGeneros.add(Genero.CIENCIA_FICCION);
		List<String> listaActores = new ArrayList<String>();
		listaActores.add("Harrison Ford");
		listaActores.add("Rutger Hauer");
		List<String> listaDirectores = new ArrayList<String>();
		listaDirectores.add("Ridley Scott");
		Pelicula pelicula = new Pelicula("Blade Runner", "Blade Runner", 1982, sinopsis, Licencia.BASICA, listaGeneros,
				listaActores, listaDirectores);

		videoclubAdmin.darAltaPelicula(pelicula);
		ArgumentCaptor<Pelicula> argumentCaptor = ArgumentCaptor.forClass(Pelicula.class);
		// Compruebo que se llama al metodo de anyadir pelicula
		Mockito.verify(daoMock, times(1)).addPelicula(argumentCaptor.capture());
		// Compruebo que las copias disponibles est�n bien.
		assertEquals(argumentCaptor.getValue(), pelicula);
		assertEquals(argumentCaptor.getValue().getCopiasDisponibles(), Licencia.BASICA.getNumeroPrestamos());
	}

	// 16.3 a: Se da de alta una pelicula con campos nulos
	@Test(expected = ExcepcionCamposNulos.class)
	public void testDarAltaPeliculaCamposNulosA()
			throws ExcepcionPeliculaYaExistente, ExcepcionCamposNoValidos, ExcepcionCamposNulos {

		String sinopsis = "A principios del siglo XXI, la poderosa Tyrell Corporation creó, gracias a los avances de la ingeniería genética, "
				+ "un robot llamado Nexus 6, un ser virtualmente idéntico al hombre pero superior a él en fuerza y agilidad, al que se dio el "
				+ "nombre de Replicante.";
		List<Genero> listaGeneros = new ArrayList<Genero>();
		listaGeneros.add(Genero.CIENCIA_FICCION);
		List<String> listaActores = new ArrayList<String>();
		listaActores.add("Harrison Ford");
		listaActores.add("Rutger Hauer");
		List<String> listaDirectores = new ArrayList<String>();
		listaDirectores.add("Ridley Scott");
		Pelicula pelicula = new Pelicula(null, "Blade Runner", 1982, sinopsis, Licencia.BASICA, listaGeneros,
				listaActores, listaDirectores);

		videoclubAdmin.darAltaPelicula(pelicula);
		ArgumentCaptor<Pelicula> argumentCaptor = ArgumentCaptor.forClass(Pelicula.class);
		// Compruebo que se llama al metodo de anyadir pelicula
		Mockito.verify(daoMock, times(0)).addPelicula(argumentCaptor.capture());
	}

	// 16.3 b: Se da de alta una pelicula con campos nulos
	@Test(expected = ExcepcionCamposNulos.class)
	public void testDarAltaPeliculaCamposNulosB()
			throws ExcepcionPeliculaYaExistente, ExcepcionCamposNoValidos, ExcepcionCamposNulos {

		String sinopsis = "A principios del siglo XXI, la poderosa Tyrell Corporation creó, gracias a los avances de la ingeniería genética, "
				+ "un robot llamado Nexus 6, un ser virtualmente idéntico al hombre pero superior a él en fuerza y agilidad, al que se dio el "
				+ "nombre de Replicante.";
		List<Genero> listaGeneros = new ArrayList<Genero>();
		listaGeneros.add(Genero.CIENCIA_FICCION);
		List<String> listaActores = new ArrayList<String>();
		listaActores.add("Harrison Ford");
		listaActores.add("Rutger Hauer");
		List<String> listaDirectores = new ArrayList<String>();
		listaDirectores.add("Ridley Scott");
		Pelicula pelicula = new Pelicula("Blade Runner", null, 1982, sinopsis, Licencia.BASICA, listaGeneros,
				listaActores, listaDirectores);

		videoclubAdmin.darAltaPelicula(pelicula);
		ArgumentCaptor<Pelicula> argumentCaptor = ArgumentCaptor.forClass(Pelicula.class);
		// Compruebo que se llama al metodo de anyadir pelicula
		Mockito.verify(daoMock, times(0)).addPelicula(argumentCaptor.capture());
	}

	// 16.3 c: Se da de alta una pelicula con campos nulos
	@Test(expected = ExcepcionCamposNulos.class)
	public void testDarAltaPeliculaCamposNulosC()
			throws ExcepcionPeliculaYaExistente, ExcepcionCamposNoValidos, ExcepcionCamposNulos {

		String sinopsis = "A principios del siglo XXI, la poderosa Tyrell Corporation creó, gracias a los avances de la ingeniería genética, "
				+ "un robot llamado Nexus 6, un ser virtualmente idéntico al hombre pero superior a él en fuerza y agilidad, al que se dio el "
				+ "nombre de Replicante.";
		List<Genero> listaGeneros = new ArrayList<Genero>();
		listaGeneros.add(Genero.CIENCIA_FICCION);
		List<String> listaActores = new ArrayList<String>();
		listaActores.add("Harrison Ford");
		listaActores.add("Rutger Hauer");
		List<String> listaDirectores = new ArrayList<String>();
		listaDirectores.add("Ridley Scott");
		Pelicula pelicula = new Pelicula("Blade Runner", "Blade Runner", null, sinopsis, Licencia.BASICA, listaGeneros,
				listaActores, listaDirectores);

		videoclubAdmin.darAltaPelicula(pelicula);
		ArgumentCaptor<Pelicula> argumentCaptor = ArgumentCaptor.forClass(Pelicula.class);
		// Compruebo que se llama al metodo de anyadir pelicula
		Mockito.verify(daoMock, times(0)).addPelicula(argumentCaptor.capture());
	}

	// 16.3 d: Se da de alta una pelicula con campos nulos
	@SuppressWarnings("unused")
	@Test(expected = ExcepcionCamposNulos.class)
	public void testDarAltaPeliculaCamposNulosD()
			throws ExcepcionPeliculaYaExistente, ExcepcionCamposNoValidos, ExcepcionCamposNulos {

		String sinopsis = "A principios del siglo XXI, la poderosa Tyrell Corporation creó, gracias a los avances de la ingeniería genética, "
				+ "un robot llamado Nexus 6, un ser virtualmente idéntico al hombre pero superior a él en fuerza y agilidad, al que se dio el "
				+ "nombre de Replicante.";
		List<Genero> listaGeneros = new ArrayList<Genero>();
		listaGeneros.add(Genero.CIENCIA_FICCION);
		List<String> listaActores = new ArrayList<String>();
		listaActores.add("Harrison Ford");
		listaActores.add("Rutger Hauer");
		List<String> listaDirectores = new ArrayList<String>();
		listaDirectores.add("Ridley Scott");
		Pelicula pelicula = new Pelicula("Blade Runner", "Blade Runner", 1982, null, Licencia.BASICA, listaGeneros,
				listaActores, listaDirectores);

		videoclubAdmin.darAltaPelicula(pelicula);
		ArgumentCaptor<Pelicula> argumentCaptor = ArgumentCaptor.forClass(Pelicula.class);
		// Compruebo que se llama al metodo de anyadir pelicula
		Mockito.verify(daoMock, times(0)).addPelicula(argumentCaptor.capture());
	}

	// 16.3 e: Se da de alta una pelicula con campos nulos
	@Test(expected = ExcepcionCamposNulos.class)
	public void testDarAltaPeliculaCamposNulosE()
			throws ExcepcionPeliculaYaExistente, ExcepcionCamposNoValidos, ExcepcionCamposNulos {

		String sinopsis = "A principios del siglo XXI, la poderosa Tyrell Corporation creó, gracias a los avances de la ingeniería genética, "
				+ "un robot llamado Nexus 6, un ser virtualmente idéntico al hombre pero superior a él en fuerza y agilidad, al que se dio el "
				+ "nombre de Replicante.";
		List<Genero> listaGeneros = new ArrayList<Genero>();
		List<String> listaActores = new ArrayList<String>();
		listaActores.add("Harrison Ford");
		listaActores.add("Rutger Hauer");
		List<String> listaDirectores = new ArrayList<String>();
		listaDirectores.add("Ridley Scott");
		Pelicula pelicula = new Pelicula("Blade Runner", "Blade Runner", 1982, sinopsis, Licencia.BASICA, listaGeneros,
				listaActores, listaDirectores);

		videoclubAdmin.darAltaPelicula(pelicula);
		ArgumentCaptor<Pelicula> argumentCaptor = ArgumentCaptor.forClass(Pelicula.class);
		// Compruebo que se llama al metodo de anyadir pelicula
		Mockito.verify(daoMock, times(0)).addPelicula(argumentCaptor.capture());
	}

	// 16.3 f: Se da de alta una pelicula con campos nulos
	@Test(expected = ExcepcionCamposNulos.class)
	public void testDarAltaPeliculaCamposNulosF()
			throws ExcepcionPeliculaYaExistente, ExcepcionCamposNoValidos, ExcepcionCamposNulos {

		String sinopsis = "A principios del siglo XXI, la poderosa Tyrell Corporation creó, gracias a los avances de la ingeniería genética, "
				+ "un robot llamado Nexus 6, un ser virtualmente idéntico al hombre pero superior a él en fuerza y agilidad, al que se dio el "
				+ "nombre de Replicante.";
		List<Genero> listaGeneros = new ArrayList<Genero>();
		listaGeneros.add(Genero.CIENCIA_FICCION);
		List<String> listaActores = new ArrayList<String>();
		List<String> listaDirectores = new ArrayList<String>();
		listaDirectores.add("Ridley Scott");
		Pelicula pelicula = new Pelicula("Blade Runner", "Blade Runner", 1982, sinopsis, Licencia.BASICA, listaGeneros,
				listaActores, listaDirectores);

		videoclubAdmin.darAltaPelicula(pelicula);
		ArgumentCaptor<Pelicula> argumentCaptor = ArgumentCaptor.forClass(Pelicula.class);
		// Compruebo que se llama al metodo de anyadir pelicula
		Mockito.verify(daoMock, times(0)).addPelicula(argumentCaptor.capture());
	}

	// 16.3 g: Se da de alta una pelicula con campos nulos
	@Test(expected = ExcepcionCamposNulos.class)
	public void testDarAltaPeliculaCamposNulosG()
			throws ExcepcionPeliculaYaExistente, ExcepcionCamposNoValidos, ExcepcionCamposNulos {

		String sinopsis = "A principios del siglo XXI, la poderosa Tyrell Corporation creó, gracias a los avances de la ingeniería genética, "
				+ "un robot llamado Nexus 6, un ser virtualmente idéntico al hombre pero superior a él en fuerza y agilidad, al que se dio el "
				+ "nombre de Replicante.";
		List<Genero> listaGeneros = new ArrayList<Genero>();
		listaGeneros.add(Genero.CIENCIA_FICCION);
		List<String> listaActores = new ArrayList<String>();
		listaActores.add("Harrison Ford");
		listaActores.add("Rutger Hauer");
		List<String> listaDirectores = new ArrayList<String>();
		Pelicula pelicula = new Pelicula("Blade Runner", "Blade Runner", 1982, sinopsis, Licencia.BASICA, listaGeneros,
				listaActores, listaDirectores);

		videoclubAdmin.darAltaPelicula(pelicula);
		ArgumentCaptor<Pelicula> argumentCaptor = ArgumentCaptor.forClass(Pelicula.class);
		// Compruebo que se llama al metodo de anyadir pelicula
		Mockito.verify(daoMock, times(0)).addPelicula(argumentCaptor.capture());
	}

	// 16.4 a: Se da de alta una pelicula con campos no v�lidos
	@Test(expected = ExcepcionCamposNoValidos.class)
	public void testDarAltaPeliculaCamposNoValidosA()
			throws ExcepcionPeliculaYaExistente, ExcepcionCamposNoValidos, ExcepcionCamposNulos {

		String sinopsis = "A principios del siglo XXI, la poderosa Tyrell Corporation creó, gracias a los avances de la ingeniería genética, "
				+ "un robot llamado Nexus 6, un ser virtualmente idéntico al hombre pero superior a él en fuerza y agilidad, al que se dio el "
				+ "nombre de Replicante.";
		List<Genero> listaGeneros = new ArrayList<Genero>();
		listaGeneros.add(Genero.CIENCIA_FICCION);
		List<String> listaActores = new ArrayList<String>();
		listaActores.add("Harrison Ford");
		listaActores.add("Rutger Hauer");
		List<String> listaDirectores = new ArrayList<String>();
		listaDirectores.add("Ridley Scott");
		Pelicula pelicula = new Pelicula("Blade Runner", "Blade Runner", 1982454, sinopsis, Licencia.BASICA,
				listaGeneros, listaActores, listaDirectores);

		videoclubAdmin.darAltaPelicula(pelicula);
		ArgumentCaptor<Pelicula> argumentCaptor = ArgumentCaptor.forClass(Pelicula.class);
		// Compruebo que no se llama al metodo de anyadir pelicula
		Mockito.verify(daoMock, times(0)).addPelicula(argumentCaptor.capture());
	}

	// 16.4 b: Se da de alta una pelicula con campos no v�lidos
	@Test(expected = ExcepcionCamposNoValidos.class)
	public void testDarAltaPeliculaCamposNoValidosB()
			throws ExcepcionPeliculaYaExistente, ExcepcionCamposNoValidos, ExcepcionCamposNulos {

		String sinopsis = "A principios del siglo XXI, la poderosa Tyrell Corporation creó, gracias a los avances de la ingeniería genética, "
				+ "un robot llamado Nexus 6, un ser virtualmente idéntico al hombre pero superior a él en fuerza y agilidad, al que se dio el "
				+ "nombre de Replicante.";
		List<Genero> listaGeneros = new ArrayList<Genero>();
		listaGeneros.add(Genero.CIENCIA_FICCION);
		List<String> listaActores = new ArrayList<String>();
		listaActores.add("Harrison Ford");
		listaActores.add("Rutger Hauer");
		List<String> listaDirectores = new ArrayList<String>();
		listaDirectores.add("Ridley Scott");
		Pelicula pelicula = new Pelicula("._fdgj", "Blade Runner", 1982, sinopsis, Licencia.BASICA, listaGeneros,
				listaActores, listaDirectores);

		videoclubAdmin.darAltaPelicula(pelicula);
		ArgumentCaptor<Pelicula> argumentCaptor = ArgumentCaptor.forClass(Pelicula.class);
		// Compruebo que no se llama al metodo de anyadir pelicula
		Mockito.verify(daoMock, times(0)).addPelicula(argumentCaptor.capture());
	}

	// 16.4 c: Se da de alta una pelicula con campos no v�lidos
	@Test(expected = ExcepcionCamposNoValidos.class)
	public void testDarAltaPeliculaCamposNoValidosC()
			throws ExcepcionPeliculaYaExistente, ExcepcionCamposNoValidos, ExcepcionCamposNulos {

		String sinopsis = "A principios del siglo XXI, la poderosa Tyrell Corporation creó, gracias a los avances de la ingeniería genética, "
				+ "un robot llamado Nexus 6, un ser virtualmente idéntico al hombre pero superior a él en fuerza y agilidad, al que se dio el "
				+ "nombre de Replicante.";
		List<Genero> listaGeneros = new ArrayList<Genero>();
		listaGeneros.add(Genero.CIENCIA_FICCION);
		List<String> listaActores = new ArrayList<String>();
		listaActores.add("Harrison Ford");
		listaActores.add("Rutger Hauer");
		List<String> listaDirectores = new ArrayList<String>();
		listaDirectores.add("Ridley Scott");
		Pelicula pelicula = new Pelicula("Blade Runner", "fd_-5", 1982, sinopsis, Licencia.BASICA, listaGeneros,
				listaActores, listaDirectores);

		videoclubAdmin.darAltaPelicula(pelicula);
		ArgumentCaptor<Pelicula> argumentCaptor = ArgumentCaptor.forClass(Pelicula.class);
		// Compruebo que no se llama al metodo de anyadir pelicula
		Mockito.verify(daoMock, times(0)).addPelicula(argumentCaptor.capture());
	}

	// 16.4 d: Se da de alta una pelicula con campos no v�lidos
	@Test(expected = ExcepcionCamposNoValidos.class)
	public void testDarAltaPeliculaCamposNoValidosD()
			throws ExcepcionPeliculaYaExistente, ExcepcionCamposNoValidos, ExcepcionCamposNulos {

		String sinopsis = "A principios del siglo XXI, la poderosa Tyrell Corporation creó, gracias a los avances de la ingeniería genética, "
				+ "un robot llamado Nexus 6, un ser virtualmente idéntico al hombre pero superior a él en fuerza y agilidad, al que se dio el "
				+ "nombre de Replicante. Estos robots trabajaban como esclavos en las colonias exteriores de la Tierra. Después de la sangrienta "
				+ "rebelión de un equipo de Nexus-6, los Replicantes fueron desterrados de la Tierra. Brigadas especiales de policía, los Blade Runners, "
				+ "tenían órdenes de matar a todos los que no hubieran acatado la condena. Pero a esto no se le llamaba ejecución, se le llamaba retiro. "
				+ "Tras un grave incidente, el ex Blade Runner Rick Deckard es llamado de nuevo al servicio para encontrar y retirar a unos "
				+ "replicantes rebeldes.";
		List<Genero> listaGeneros = new ArrayList<Genero>();
		listaGeneros.add(Genero.CIENCIA_FICCION);
		List<String> listaActores = new ArrayList<String>();
		listaActores.add("Harrison Ford");
		listaActores.add("Rutger Hauer");
		List<String> listaDirectores = new ArrayList<String>();
		listaDirectores.add("Ridley Scott");
		Pelicula pelicula = new Pelicula("Blade Runner", "Blade Runner", 1982, sinopsis, Licencia.BASICA, listaGeneros,
				listaActores, listaDirectores);

		videoclubAdmin.darAltaPelicula(pelicula);
		ArgumentCaptor<Pelicula> argumentCaptor = ArgumentCaptor.forClass(Pelicula.class);
		// Compruebo que no se llama al metodo de anyadir pelicula
		Mockito.verify(daoMock, times(0)).addPelicula(argumentCaptor.capture());
	}

}
