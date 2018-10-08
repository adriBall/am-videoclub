package modelo;

import java.util.List;

import dominio.Pelicula;
import excepciones.ExcepcionAutenticacionIncorrecta;
import excepciones.ExcepcionNoExistenPeliculas;

public interface IVideoclubComun {

	// HU04(socio) y HU09(admin)
	void iniciarSesion(String usuario, String contrasenya) throws ExcepcionAutenticacionIncorrecta;

	// HU08(socio) y HU18(admin)
	Pelicula verDatosPelicula(Pelicula peliculaID);

	// HU17 (admin) pero tambien se le permitira al socio
	List<Pelicula> listarPeliculas() throws ExcepcionNoExistenPeliculas;

	String getUsuarioSesion();

	boolean esSocio();

	boolean esAdmin();

	void cerrarSesion();

}