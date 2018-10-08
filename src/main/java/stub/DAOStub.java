package stub;

import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import dao.IDAOVideoclub;
import dominio.Credenciales;
import dominio.Genero;
import dominio.Licencia;
import dominio.Pelicula;
import dominio.PeliculaAlquilada;
import dominio.Socio;
import util.Fechas;

public class DAOStub implements IDAOVideoclub {
	private static List<Pelicula> listaPeliculas = new LinkedList<>();
	private static List<PeliculaAlquilada> listaAlquiladas = new LinkedList<>();
	private static List<Socio> listaSocios = new LinkedList<>();
	private static List<Credenciales> listaCredenciales = new LinkedList<>();

	static {
		Pelicula pelicula1 = new Pelicula();
		pelicula1.setTituloEsp("El día más largo");
		pelicula1.setTituloVO("The longest day");
		pelicula1.setAnyo(1962);
		pelicula1.setSinopsis(
				"Segunda Guerra Mundial (1939-1945). Minucioso relato del desembarco de las tropas aliadas "
						+ "en las playas de Normandía el 6 de junio de 1944, día que señaló el comienzo del fin de la dominación "
						+ "nazi sobre Europa. En este ataque participaron 3.000.000 de hombres, 11.000 aviones y 4.000 barcos.");
		pelicula1.setNombreLicencia(Licencia.BASICA);
		pelicula1.addActor("Sean Connery");
		pelicula1.addDirector("Ken Annakin");
		pelicula1.addDirector("Andrew Marton");
		pelicula1.addDirector("Bernhard Wicki");
		pelicula1.addGenero(Genero.BELICA);
		pelicula1.addGenero(Genero.COMEDIA);
		pelicula1.addGenero(Genero.FANTASIA);
		pelicula1.addGenero(Genero.DRAMA);
		listaPeliculas.add(pelicula1);

		Pelicula pelicula2 = new Pelicula();
		pelicula2.setTituloEsp("Blade Runner");
		pelicula2.setTituloVO("Blade Runner");
		pelicula2.setAnyo(1982);
		pelicula2.addActor("Harrison ford");
		pelicula2.addDirector("Ridley Scott");
		pelicula2.setSinopsis(
				"A principios del siglo XXI, la poderosa Tyrell Corporation creó, gracias a los avances de la "
						+ "ingeniería genética, un robot llamado Nexus 6, un ser virtualmente idéntico al hombre pero superior a él en "
						+ "fuerza y agilidad, al que se dio el nombre de Replicante.");
		pelicula2.setNombreLicencia(Licencia.BASICA);
		pelicula2.setCopiasDisponibles(1);
		pelicula2.addGenero(Genero.CIENCIA_FICCION);
		listaPeliculas.add(pelicula2);

		Credenciales credencialesSocio = new Credenciales();
		credencialesSocio.setUsuario("socio");
		credencialesSocio.setContrasenya("socio");
		credencialesSocio.setAdmin(false);
		Socio socio = new Socio();
		socio.setNombreSocio("Nombre Apellido");
		socio.setIdSocio("73402447V");
		socio.setDireccion("Calle Falsa 123");
		socio.setCorreo("socio@socio.com");
		socio.setTelefono(689562734);
		socio.setCredenciales(credencialesSocio);
		listaSocios.add(socio);
		listaCredenciales.add(socio.getCredenciales());

		Credenciales credencialesMoroso = new Credenciales();
		credencialesMoroso.setUsuario("moroso");
		credencialesMoroso.setContrasenya("moroso");
		credencialesMoroso.setAdmin(false);
		Socio socioMoroso = new Socio();
		socioMoroso.setNombreSocio("El Moroso");
		socioMoroso.setIdSocio("73402448H");
		socioMoroso.setDireccion("Calle Falsa 123");
		socioMoroso.setCorreo("moroso@moroso.com");
		socioMoroso.setTelefono(689542734);
		socioMoroso.setCredenciales(credencialesMoroso);
		socioMoroso.setUltimoPago(Fechas.fechaUltimoDiaMesAnterior(Fechas.fechaUltimoDiaMesAnterior(Fechas.hoy())));
		listaSocios.add(socioMoroso);
		listaCredenciales.add(socioMoroso.getCredenciales());

		PeliculaAlquilada alquilada1 = new PeliculaAlquilada();
		alquilada1.setTituloVO(pelicula1.getTituloVO());
		alquilada1.setTituloEsp(pelicula1.getTituloEsp());
		alquilada1.setAnyo(pelicula1.getAnyo());
		alquilada1.setIdSocio(socio.getIdSocio());
		alquilada1.setTiempoAlquiler(new Timestamp(System.currentTimeMillis()));
		alquilada1.setEnlace("https://github.com/adriBall");
		listaAlquiladas.add(alquilada1);

		Credenciales credencialesAdmin = new Credenciales();
		credencialesAdmin.setUsuario("admin");
		credencialesAdmin.setContrasenya("admin");
		credencialesAdmin.setAdmin(true);
		listaCredenciales.add(credencialesAdmin);
	}

	@Override
	public Credenciales comprobarCredenciales(String usuario, String contrasenya) {
		for (Credenciales credenciales : listaCredenciales)
			if (credenciales.getUsuario().equals(usuario) && credenciales.getContrasenya().equals(contrasenya))
				return new Credenciales(new String(credenciales.getUsuario()),
						new String(credenciales.getContrasenya()), credenciales.getAdmin());
		return null;
	}

	@Override
	public void addPelicula(Pelicula pelicula) {
		listaPeliculas.add(pelicula);
	}

	@Override
	public void modificarPelicula(String actualTituloVO, Integer actualAnyo, Pelicula peliculaModificada) {
		Pelicula aBuscar = new Pelicula();
		aBuscar.setTituloVO(actualTituloVO);
		aBuscar.setAnyo(actualAnyo);
		int i = listaPeliculas.indexOf(aBuscar);
		if (i == -1)
			throw new RuntimeException("No existe la peli a modificar");
		listaPeliculas.set(i, peliculaModificada);
	}

	@Override
	public Pelicula getPelicula(String tituloVO, Integer anyo) {
		Pelicula aBuscar = new Pelicula();
		aBuscar.setTituloVO(tituloVO);
		aBuscar.setAnyo(anyo);
		int i = listaPeliculas.indexOf(aBuscar);
		if (i == -1)
			return null;
		aBuscar = listaPeliculas.get(i);
		Pelicula copia = new Pelicula();
		copia.setTituloVO(new String(aBuscar.getTituloVO()));
		copia.setTituloEsp(new String(aBuscar.getTituloEsp()));
		copia.setAnyo(new Integer(aBuscar.getAnyo()));
		copia.setSinopsis(new String(aBuscar.getSinopsis()));
		copia.setNombreLicencia(aBuscar.getNombreLicencia());
		copia.setCopiasDisponibles(new Integer(aBuscar.getCopiasDisponibles()));
		copia.setListaActores(aBuscar.getListaActores());
		copia.setListaDirectores(aBuscar.getListaDirectores());
		copia.setListaGeneros(aBuscar.getListaGeneros());
		return copia;
	}

	@Override
	public List<Pelicula> listarPeliculasVideoclub() {
		return listaPeliculas;
	}

	@Override
	public void deletePelicula(Pelicula pelicula) {
		if (!listaPeliculas.remove(pelicula))
			throw new RuntimeException("No se encuentra la peli a eliminar");
		List<PeliculaAlquilada> aEliminar = new LinkedList<>();
		for (PeliculaAlquilada alquilada : listaAlquiladas)
			if (alquilada.getTituloVO().equals(pelicula.getTituloVO())
					&& alquilada.getAnyo().equals(pelicula.getAnyo()))
				aEliminar.add(alquilada);
		for (PeliculaAlquilada eliminar : aEliminar)
			listaAlquiladas.remove(eliminar);
	}

	@Override
	public Integer getNumeroCopiasDisponibles(Pelicula pelicula) {
		int i = listaPeliculas.indexOf(pelicula);
		if (i == -1)
			throw new RuntimeException("No se encuentra la peli a consultar sus copias");
		return new Integer(listaPeliculas.get(i).getCopiasDisponibles());
	}

	@Override
	public List<Pelicula> buscarPeliculas(String titulo, String actor, String director, Genero genero, Integer anyo) {
		// TODO Auto-generated method stub, las devuelve todas
		return listaPeliculas;
	}

	@Override
	public void addPeliculaAlquilada(PeliculaAlquilada peliculaAlquilada) {
		Pelicula aDecrementar = new Pelicula();
		aDecrementar.setTituloVO(peliculaAlquilada.getTituloVO());
		aDecrementar.setAnyo(peliculaAlquilada.getAnyo());
		int i = listaPeliculas.indexOf(aDecrementar);
		if (i == -1)
			throw new RuntimeException("La alquilada no tiene peli asociada");
		listaPeliculas.get(i).setCopiasDisponibles(listaPeliculas.get(i).getCopiasDisponibles() - 1);
		listaAlquiladas.add(peliculaAlquilada);
	}

	@Override
	public void removePeliculaAlquilada(PeliculaAlquilada peliculaAlquilada) {
		if (!listaAlquiladas.remove(peliculaAlquilada))
			throw new RuntimeException("No se ha encontrado la peli alquilada a eliminar");
		Pelicula aIncrementar = new Pelicula();
		aIncrementar.setTituloVO(peliculaAlquilada.getTituloVO());
		aIncrementar.setAnyo(peliculaAlquilada.getAnyo());
		int i = listaPeliculas.indexOf(aIncrementar);
		if (i == -1)
			throw new RuntimeException("La alquilada se ha eliminado pero no tiene peli asociada");
		listaPeliculas.get(i).setCopiasDisponibles(listaPeliculas.get(i).getCopiasDisponibles() + 1);
	}

	@Override
	public List<PeliculaAlquilada> listarPeliculasAlquiladas() {
		return listaAlquiladas;
	}

	@Override
	public void deleteReservasCaducadas() {
		List<PeliculaAlquilada> aEliminar = new LinkedList<>();
		for (PeliculaAlquilada alquilada : listaAlquiladas)
			if (alquilada.getTiempoRestanteMilis() <= 0)
				aEliminar.add(alquilada);
		for (PeliculaAlquilada eliminar : aEliminar)
			listaAlquiladas.remove(eliminar);
	}

	@Override
	public void addSocio(Socio nuevoSocio) {
		listaSocios.add(nuevoSocio);
		listaCredenciales.add(nuevoSocio.getCredenciales());
	}

	@Override
	public void modificarSocio(Credenciales actuales, Socio socioModificado) {
		int iSocio = -1;
		for (Socio s : listaSocios)
			if (s.getCredenciales().equals(actuales))
				iSocio = listaSocios.indexOf(s);
		if (iSocio == -1)
			throw new RuntimeException("No existe el socio a modificar");
		int iCredenciales = listaCredenciales.indexOf(actuales);
		if (iCredenciales == -1)
			throw new RuntimeException("No existen los credenciales del socio a modificar");
		listaSocios.set(iSocio, socioModificado);
		listaCredenciales.set(iCredenciales, socioModificado.getCredenciales());
	}

	@Override
	public Socio getSocioPorId(String idSocio) {
		for (Socio socio : listaSocios)
			if (socio.getIdSocio().equals(idSocio)) {
				Socio copia = new Socio();
				copia.setIdSocio(new String(socio.getIdSocio()));
				copia.setNombreSocio(new String(socio.getNombreSocio()));
				copia.setTelefono(new Integer(socio.getTelefono()));
				copia.setCorreo(new String(socio.getCorreo()));
				copia.setUltimoPago(new Date(socio.getUltimoPago().getTime()));
				copia.setCredenciales(new Credenciales(new String(socio.getCredenciales().getUsuario()),
						new String(socio.getCredenciales().getContrasenya()), false));
				return socio;
			}
		return null;
	}

	@Override
	public Socio getSocioPorUsuario(String usuario) {
		for (Socio socio : listaSocios)
			if (socio.getCredenciales().getUsuario().equals(usuario)) {
				Socio copia = new Socio();
				copia.setIdSocio(new String(socio.getIdSocio()));
				copia.setNombreSocio(new String(socio.getNombreSocio()));
				copia.setTelefono(new Integer(socio.getTelefono()));
				copia.setCorreo(new String(socio.getCorreo()));
				copia.setUltimoPago(new Date(socio.getUltimoPago().getTime()));
				copia.setCredenciales(new Credenciales(new String(socio.getCredenciales().getUsuario()),
						new String(socio.getCredenciales().getContrasenya()), false));
				return socio;
			}
		return null;
	}

	@Override
	public List<Socio> listarSociosVideoclub() {
		return listaSocios;
	}

	@Override
	public List<PeliculaAlquilada> getPeliculasAlquiladas(Credenciales credencialesSocio) {
		Socio socio = getSocioPorUsuario(credencialesSocio.getUsuario());
		if (socio == null)
			throw new RuntimeException("No existe el socio a consultar sus pelis");
		List<PeliculaAlquilada> alquiladasSocio = new LinkedList<>();
		for (PeliculaAlquilada alquilada : listaAlquiladas)
			if (alquilada.getIdSocio().equals(socio.getIdSocio()))
				alquiladasSocio.add(alquilada);
		return alquiladasSocio;
	}

	@Override
	public PeliculaAlquilada getPeliculaAlquilada(Credenciales credencialesSocio, String nombrePeli, int anyo) {
		PeliculaAlquilada aBuscar = new PeliculaAlquilada();
		aBuscar.setAnyo(anyo);
		aBuscar.setTituloVO(nombrePeli);
		Socio socio = getSocioPorUsuario(credencialesSocio.getUsuario());
		if (socio == null)
			throw new RuntimeException("No existe el socio de la peli alquilada a buscar");
		aBuscar.setIdSocio(socio.getIdSocio());
		for (PeliculaAlquilada alquilada : listaAlquiladas)
			if (alquilada.equals(aBuscar)) {
				PeliculaAlquilada copia = new PeliculaAlquilada();
				copia.setAnyo(new Integer(alquilada.getAnyo()));
				copia.setIdSocio(new String(alquilada.getIdSocio()));
				copia.setEnlace(new String(alquilada.getEnlace()));
				copia.setTituloEsp(new String(alquilada.getTituloEsp()));
				copia.setTituloEsp(new String(alquilada.getTituloVO()));
				copia.setTiempoAlquiler(new Timestamp(alquilada.getTiempoAlquiler().getTime()));
				return copia;
			}
		return null;

	}

	@Override
	public void deleteSocio(Socio socio) {
		int i = listaSocios.indexOf(socio);
		if (i == -1)
			throw new RuntimeException("No existe el socio a eliminar");
		Socio aux = listaSocios.remove(i);
		listaCredenciales.remove(aux.getCredenciales());
	}

	@Override
	public void pagarMensualidad(Socio socio) {
		int i = listaSocios.indexOf(socio);
		if (i == -1)
			throw new RuntimeException("No existe el socio a pagar");
		listaSocios.get(i).setUltimoPago(Fechas.fechaUltimoDiaMesSiguiente(listaSocios.get(i).getUltimoPago()));
	}

	@Override
	public Date getUltimoPago(Socio socio) {
		for (Socio s : listaSocios)
			if (s.equals(socio))
				return s.getUltimoPago();
		return null;
	}

	@Override
	public List<Credenciales> listarCredenciales() {
		// TODO Auto-generated method stub
		return null;
	}

}
