package modelo;

import java.util.List;

import dao.IDAOVideoclub;
import dominio.Credenciales;
import dominio.Pelicula;
import excepciones.ExcepcionAutenticacionIncorrecta;
import excepciones.ExcepcionNoExistenPeliculas;
import excepciones.ExcepcionSesionIncorrecta;

//Clase que guarda los credenciales cuando se llama al metodo iniciarsesion con datos validos.
public class VideoclubComun implements IVideoclubComun {
	protected IDAOVideoclub dao;
	protected Credenciales credencialesSesion;

	public VideoclubComun(IDAOVideoclub dao) {
		this.dao = dao;
		credencialesSesion = null;
	}

	// Constructor que nos sera util en la GUI
	protected VideoclubComun(VideoclubComun otro) {
		this.dao = otro.dao;
		this.credencialesSesion = otro.credencialesSesion;
	}

	public String getUsuarioSesion() {
		if (credencialesSesion == null)
			return null;
		return credencialesSesion.getUsuario();
	}

	// HU04 y HU09
	@Override
	public void iniciarSesion(String usuario, String contrasenya) throws ExcepcionAutenticacionIncorrecta {
		Credenciales credenciales = new Credenciales();
		credenciales.setUsuario(usuario);
		credenciales.setContrasenya(contrasenya);
		credenciales = dao.comprobarCredenciales(credenciales.getUsuario(), credenciales.getContrasenya());
		if (credenciales == null)
			throw new ExcepcionAutenticacionIncorrecta();
		credencialesSesion = credenciales;
	}

	@Override
	public void cerrarSesion() {
		credencialesSesion = null;
	}

	// HU18 y HU08
	@Override
	public Pelicula verDatosPelicula(Pelicula peliculaID) {
		if (credencialesSesion == null)
			throw new ExcepcionSesionIncorrecta();
		return dao.getPelicula(peliculaID.getTituloVO(), peliculaID.getAnyo());
	}

	// HU17 y permitido para socio
	@Override
	public List<Pelicula> listarPeliculas() throws ExcepcionNoExistenPeliculas {
		if (credencialesSesion == null)
			throw new ExcepcionSesionIncorrecta();

		List<Pelicula> lista = dao.listarPeliculasVideoclub();
		if (lista.isEmpty()) {
			throw new ExcepcionNoExistenPeliculas();
		}
		return lista;
	}

	@Override
	public boolean esAdmin() {
		return credencialesSesion == null ? false : credencialesSesion.getAdmin();
	}

	@Override
	public boolean esSocio() {
		return credencialesSesion == null ? false : !credencialesSesion.getAdmin();
	}

}
