package modelo;

import java.util.List;
import java.util.stream.Collectors;
import dao.IDAOVideoclub;
import dominio.Credenciales;
import dominio.Pelicula;
import dominio.PeliculaAlquilada;
import dominio.Socio;
import excepciones.ExcepcionCamposNoValidos;
import excepciones.ExcepcionCamposNulos;
import excepciones.ExcepcionNoExistenSocios;
import excepciones.ExcepcionPeliculaNoExiste;
import excepciones.ExcepcionPeliculaYaExistente;
import excepciones.ExcepcionSesionIncorrecta;
import excepciones.ExcepcionSocioNoExiste;
import excepciones.ExcepcionSocioYaExistente;
import validadores.ValidadorPelicula;
import validadores.ValidadorSocio;

public class VideoclubAdmin extends VideoclubComun implements IVideoclubAdmin {

	public VideoclubAdmin(IDAOVideoclub dao) {
		super(dao);
	}

	// Constructor que nos sera util en la GUI
	public VideoclubAdmin(VideoclubComun otro) {
		super(otro);
	}

	@Override
	public void darAltaSocio(Socio socio)
			throws ExcepcionSocioYaExistente, ExcepcionCamposNoValidos, ExcepcionCamposNulos {
		comprobarSesion();

		String id = socio.getIdSocio();
		String usuario = socio.getCredenciales().getUsuario();

		// compruebo si el socio ya existe
		if (dao.getSocioPorId(id) != null || dao.getSocioPorUsuario(usuario) != null) {
			throw new ExcepcionSocioYaExistente();
		}
		// valido los campos
		ValidadorSocio.validar(socio);

		dao.addSocio(socio);
	}

	@Override
	public List<Socio> listarSocios() throws ExcepcionNoExistenSocios {
		comprobarSesion();

		List<Socio> lista = dao.listarSociosVideoclub();
		if (lista.isEmpty()) {
			throw new ExcepcionNoExistenSocios();
		}
		return lista;
	}

	@Override
	public Socio verDatosSocio(String socioId) {
		comprobarSesion();
		Socio socio = dao.getSocioPorId(socioId);
		return socio;
	}

	@Override
	public void actualizarDatosSocio(Credenciales socioAntiguosCredenciales, Socio socioNuevosDatos)
			throws ExcepcionSocioYaExistente, ExcepcionCamposNoValidos, ExcepcionCamposNulos {
		comprobarSesion();
		ValidadorSocio.validar(socioNuevosDatos);

		List<Credenciales> credenciales = dao.listarCredenciales();
		for (Credenciales credencial : credenciales)
			if (socioNuevosDatos.getCredenciales().getUsuario().equals(credencial.getUsuario())
					&& !socioAntiguosCredenciales.getUsuario().equals(socioNuevosDatos.getCredenciales().getUsuario()))
				throw new ExcepcionSocioYaExistente();

		dao.modificarSocio(socioAntiguosCredenciales, socioNuevosDatos);
	}

	@Override
	public void darBajaSocio(Socio socio) throws ExcepcionSocioNoExiste {
		comprobarSesion();
		// compruebo si el socio existe
		if (dao.getSocioPorId(socio.getIdSocio()) == null) {
			throw new ExcepcionSocioNoExiste();
		}
		dao.deleteSocio(socio);// este metodo borrara de la bbdd tanto el socio como la reserva de pelis
								// alquiladas por el

	}

	@Override
	public void darAltaPelicula(Pelicula pelicula)
			throws ExcepcionPeliculaYaExistente, ExcepcionCamposNoValidos, ExcepcionCamposNulos {
		comprobarSesion();

		String tituloVO = pelicula.getTituloVO();
		Integer anyo = pelicula.getAnyo();

		// compruebo si la pelicula ya existe
		if (dao.getPelicula(tituloVO, anyo) != null) {
			throw new ExcepcionPeliculaYaExistente();
		}
		// valido los campos
		ValidadorPelicula.validar(pelicula);

		dao.addPelicula(pelicula);
	}

	@Override
	public void actualizarDatosPelicula(String anteriorTituloVO, Integer anteriorAnyo, Pelicula peliculaNuevosDatos)
			throws ExcepcionCamposNoValidos, ExcepcionCamposNulos {
		comprobarSesion();

		ValidadorPelicula.validar(peliculaNuevosDatos);

		dao.modificarPelicula(anteriorTituloVO, anteriorAnyo, peliculaNuevosDatos);
	}

	@Override
	public void darBajaPelicula(Pelicula pelicula) throws ExcepcionPeliculaNoExiste {
		comprobarSesion();
		// compruebo si la peli existe
		if (dao.getPelicula(pelicula.getTituloVO(), pelicula.getAnyo()) == null) {
			throw new ExcepcionPeliculaNoExiste();
		}
		dao.deletePelicula(pelicula);// este metodo borrara de la bbdd tanto la peli como de la lista de pendientes
										// de socios
	}

	@Override
	public List<Socio> listarUsuariosMorosos() {
		comprobarSesion();
		return dao.listarSociosVideoclub().stream().filter((socio) -> (socio.esMoroso())).collect(Collectors.toList());
	}

	@Override
	public void chequearAntiguedadAlquileres() {
		comprobarSesion();
		dao.deleteReservasCaducadas(); // metodo que borrara las peliculas alquiladas cuyo tiempo haya excedido las 48
										// horas, ademas incrementara el numero de copias donde corresponda
	}

	@Override
	public void pagarMensualidadSocio(Socio socio) throws ExcepcionSocioNoExiste {
		comprobarSesion();
		// compruebo si el socio existe
		if (dao.getSocioPorId(socio.getIdSocio()) == null) {
			throw new ExcepcionSocioNoExiste();
		}
		dao.pagarMensualidad(socio);
	}

	private void comprobarSesion() {
		if (!super.esAdmin())
			throw new ExcepcionSesionIncorrecta();
	}

	@Override
	public List<PeliculaAlquilada> listarPeliculasAlquiladas() {
		return dao.listarPeliculasAlquiladas();
	}

}
