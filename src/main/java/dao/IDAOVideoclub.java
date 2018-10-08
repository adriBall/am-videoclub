package dao;

import java.util.Date;
import java.util.List;
import dominio.Credenciales;
import dominio.Genero;
import dominio.Pelicula;
import dominio.PeliculaAlquilada;
import dominio.Socio;

public interface IDAOVideoclub {
	
	// Login, devuelve null si no son correctos
	public Credenciales comprobarCredenciales(String usuario, String contrasenya);
	public List<Credenciales> listarCredenciales();	
	
	// Gestion de Peliculas 
	public void addPelicula(Pelicula pelicula);
	public void modificarPelicula(String actualTituloVO, Integer actualAnyo, Pelicula peliculaModificada);
	public Pelicula getPelicula(String tituloVO, Integer anyo); // Ha de calcular las copias disponibles restando la capacidad de la licencia y las alquiladas correspondientes
	public List<Pelicula> listarPeliculasVideoclub();
	public void deletePelicula(Pelicula pelicula); // Ha de eliminar las alquiladas correspondientes
	public Integer getNumeroCopiasDisponibles(Pelicula pelicula);
	public List<Pelicula> buscarPeliculas(String titulo, String actor, String director, Genero genero, Integer anyo);
	
	// Gestion de Peliculas alquiladas
	public void addPeliculaAlquilada(PeliculaAlquilada peliculaAlquilada);
	public void removePeliculaAlquilada(PeliculaAlquilada peliculaAlquilada);
	public List<PeliculaAlquilada> listarPeliculasAlquiladas();
	public void deleteReservasCaducadas();

	// Gestion de Socios
	public void addSocio(Socio nuevoSocio);
	public void modificarSocio(Credenciales actuales, Socio socioModificado);
	public Socio getSocioPorId(String idSocio);
	public Socio getSocioPorUsuario(String usuario);
	public List<Socio> listarSociosVideoclub();
	public List<PeliculaAlquilada> getPeliculasAlquiladas(Credenciales credencialesSocio);
	public PeliculaAlquilada getPeliculaAlquilada(Credenciales credencialesSocio, String nombrePeli, int anyo);
	public void deleteSocio(Socio socio);
	public void pagarMensualidad(Socio socio);
	public Date getUltimoPago(Socio socio);

	
}