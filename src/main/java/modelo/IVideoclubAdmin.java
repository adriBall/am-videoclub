package modelo;

import java.util.List;

import dominio.Credenciales;
import dominio.Pelicula;
import dominio.PeliculaAlquilada;
import dominio.Socio;
import excepciones.ExcepcionCamposNoValidos;
import excepciones.ExcepcionCamposNulos;
import excepciones.ExcepcionNoExistenSocios;
import excepciones.ExcepcionPeliculaNoExiste;
import excepciones.ExcepcionPeliculaYaExistente;
import excepciones.ExcepcionSocioNoExiste;
import excepciones.ExcepcionSocioYaExistente;

public interface IVideoclubAdmin extends IVideoclubComun {

	// HU10
	void darAltaSocio(Socio socio) throws ExcepcionSocioYaExistente, ExcepcionCamposNoValidos, ExcepcionCamposNulos;

	// HU11
	List<Socio> listarSocios() throws ExcepcionNoExistenSocios;

	// HU12(admin)
	Socio verDatosSocio(String socioID);

	// HU13
	void actualizarDatosSocio(Credenciales socioAntiguosCredenciales, Socio socioNuevosDatos)
			throws ExcepcionSocioYaExistente, ExcepcionCamposNoValidos, ExcepcionCamposNulos;

	// HU14 y HU15
	void darBajaSocio(Socio socio) throws ExcepcionSocioNoExiste;

	// HU16
	void darAltaPelicula(Pelicula pelicula)
			throws ExcepcionPeliculaYaExistente, ExcepcionCamposNoValidos, ExcepcionCamposNulos;

	// HU19 y HU20
	void actualizarDatosPelicula(String anteriorTituloVO, Integer anteriorAnyo, Pelicula peliculaNuevosDatos)
			throws ExcepcionCamposNoValidos, ExcepcionCamposNulos;

	// HU21
	void darBajaPelicula(Pelicula pelicula) throws ExcepcionPeliculaNoExiste;

	// HU22
	List<Socio> listarUsuariosMorosos();

	// HU23
	List<PeliculaAlquilada> listarPeliculasAlquiladas();

	// HU24
	void chequearAntiguedadAlquileres();

	// HU25
	void pagarMensualidadSocio(Socio socio) throws ExcepcionSocioNoExiste;

}