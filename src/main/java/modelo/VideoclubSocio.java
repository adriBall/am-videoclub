package modelo;

import java.util.List;
import java.util.stream.Collectors;

import dao.IDAOVideoclub;
import dominio.Credenciales;
import dominio.Genero;
import dominio.Pelicula;
import dominio.PeliculaAlquilada;
import dominio.Socio;
import excepciones.ExcepcionCamposNoValidos;
import excepciones.ExcepcionCamposNulos;
import excepciones.ExcepcionNoHayCopiasDisponibles;
import excepciones.ExcepcionNoHayPeliculasPendientes;
import excepciones.ExcepcionNoPermitido;
import excepciones.ExcepcionPeliculaYaAlquilada;
import excepciones.ExcepcionSesionIncorrecta;
import excepciones.ExcepcionSocioYaExistente;
import excepciones.ExcepcionUsuarioMoroso;
import validadores.ValidadorSocio;

public class VideoclubSocio extends VideoclubComun implements IVideoclubSocio {

	public VideoclubSocio(IDAOVideoclub dao) {
		super(dao);
	}

	// Constructor que nos sera util en la GUI
	public VideoclubSocio(VideoclubComun otro) {
		super(otro);
	}

	@Override
	public void alquilarPelicula(Pelicula pelicula)
			throws ExcepcionPeliculaYaAlquilada, ExcepcionNoHayCopiasDisponibles, ExcepcionUsuarioMoroso {

		comprobarSesion();

		if (pelicula.getCopiasDisponibles() <= 0)
			throw new ExcepcionNoHayCopiasDisponibles();

		Socio socio = dao.getSocioPorUsuario(credencialesSesion.getUsuario());

		if (socio.esMoroso())
			throw new ExcepcionUsuarioMoroso();

		List<PeliculaAlquilada> alquiladas = dao.listarPeliculasAlquiladas().stream()
				.filter((alquilada) -> socio.getIdSocio().equals(alquilada.getIdSocio())).collect(Collectors.toList());

		for (PeliculaAlquilada peliculaAlquilada : alquiladas)
			if (peliculaAlquilada.getTituloVO().equals(pelicula.getTituloVO())
					&& peliculaAlquilada.getAnyo().equals(pelicula.getAnyo()))
				throw new ExcepcionPeliculaYaAlquilada();

		PeliculaAlquilada alquilada = new PeliculaAlquilada(pelicula);
		alquilada.setIdSocio(dao.getSocioPorUsuario(credencialesSesion.getUsuario()).getIdSocio());
		dao.addPeliculaAlquilada(alquilada);

	}

	@Override
	public void devolverPelicula(PeliculaAlquilada peliculaAlquilada) {
		comprobarSesion();
		dao.removePeliculaAlquilada(peliculaAlquilada);
	}

	@Override
	public List<Pelicula> buscarPeliculas(String titulo, String actor, String director, Genero genero, Integer anyo) {
		return dao.buscarPeliculas(titulo, actor, director, genero, anyo);
	}

	@Override
	public Socio verDatosPersonales() {
		comprobarSesion();
		return dao.getSocioPorUsuario(credencialesSesion.getUsuario());
	}

	@Override
	public void actualizarMisDatos(Socio socioNuevosDatos)
			throws ExcepcionCamposNoValidos, ExcepcionCamposNulos, ExcepcionNoPermitido, ExcepcionSocioYaExistente {

		ValidadorSocio.validar(socioNuevosDatos);

		List<Credenciales> credenciales = dao.listarCredenciales();
		for (Credenciales credencial : credenciales)
			if (socioNuevosDatos.getCredenciales().getUsuario().equals(credencial.getUsuario())
					&& !socioNuevosDatos.getCredenciales().getUsuario().equals(credencialesSesion.getUsuario()))
				throw new ExcepcionSocioYaExistente();

		if (!socioNuevosDatos.getUltimoPago()
				.equals(dao.getSocioPorUsuario(credencialesSesion.getUsuario()).getUltimoPago())
				|| socioNuevosDatos.getCredenciales().getAdmin())
			throw new ExcepcionNoPermitido();
		dao.modificarSocio(credencialesSesion, socioNuevosDatos);
		credencialesSesion = socioNuevosDatos.getCredenciales();
	}

	@Override
	public List<PeliculaAlquilada> misPeliculasAlquiladas() throws ExcepcionNoHayPeliculasPendientes {
		comprobarSesion();

		Socio socio = dao.getSocioPorUsuario(credencialesSesion.getUsuario());
		List<PeliculaAlquilada> alquiladas = dao.listarPeliculasAlquiladas().stream()
				.filter((alquilada) -> socio.getIdSocio().equals(alquilada.getIdSocio())).collect(Collectors.toList());
		if (alquiladas.size() == 0)
			throw new ExcepcionNoHayPeliculasPendientes();

		return alquiladas;
	}

	private void comprobarSesion() {
		if (!super.esSocio())
			throw new ExcepcionSesionIncorrecta();
	}

}
