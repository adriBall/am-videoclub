package modelo;

import java.util.List;

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
import excepciones.ExcepcionSocioYaExistente;
import excepciones.ExcepcionUsuarioMoroso;

public interface IVideoclubSocio extends IVideoclubComun {

	// HU01 (y HU26, son la misma)
	void alquilarPelicula(Pelicula pelicula)
			throws ExcepcionPeliculaYaAlquilada, ExcepcionNoHayCopiasDisponibles, ExcepcionUsuarioMoroso;

	// HU02 (y HU27, son la misma)
	void devolverPelicula(PeliculaAlquilada peliculaAlquilada);

	// HU03
	List<Pelicula> buscarPeliculas(String titulo, String actor, String director, Genero genero, Integer anyo);

	// HU05(socio)
	Socio verDatosPersonales();

	// HU06
	public void actualizarMisDatos(Socio socioNuevosDatos)
			throws ExcepcionCamposNoValidos, ExcepcionCamposNulos, ExcepcionNoPermitido, ExcepcionSocioYaExistente;

	// HU07
	List<PeliculaAlquilada> misPeliculasAlquiladas() throws ExcepcionNoHayPeliculasPendientes;

}
