package validadores;

import java.util.List;

import dominio.Genero;
import dominio.Licencia;
import dominio.Pelicula;
import excepciones.ExcepcionCamposNoValidos;
import excepciones.ExcepcionCamposNulos;

import static util.Validadores.*;

public class ValidadorPelicula {

	public static void validar(Pelicula pelicula) throws ExcepcionCamposNulos, ExcepcionCamposNoValidos {
		String tituloEsp = pelicula.getTituloEsp();
		String tituloVO = pelicula.getTituloVO();
		Integer anyo = pelicula.getAnyo();
		String sinopsis = pelicula.getSinopsis();
		Licencia nombreLicencia = pelicula.getNombreLicencia();
		List<Genero> listaGeneros = pelicula.getListaGeneros();
		List<String> listaActores = pelicula.getListaActores();
		List<String> listaDirectores = pelicula.getListaDirectores();

		if (tituloEsp == null || tituloVO == null || anyo == null || sinopsis == null || nombreLicencia == null
				|| listaGeneros.isEmpty() || listaActores.isEmpty() || listaDirectores.isEmpty()) {
			throw new ExcepcionCamposNulos();
		}

		for (Genero genero : listaGeneros)
			if (genero == null)
				throw new ExcepcionCamposNulos();

		// Compruebo si hay campos invalidos
		if (!(esAlfanumericoMasEspacios(tituloEsp)) || !(esAlfanumericoMasEspacios(tituloVO))
				|| anyo.toString().length() != 4 || sinopsis.length() > 300) {
			throw new ExcepcionCamposNoValidos();
		}

		if (pelicula.getCopiasDisponibles() > pelicula.getNombreLicencia().getNumeroPrestamos())
			throw new ExcepcionCamposNoValidos();

		for (String actor : listaActores) {
			if (actor == null)
				throw new ExcepcionCamposNulos();
			if (!(esNombre(actor)))
				throw new ExcepcionCamposNoValidos();
		}
		for (String director : listaDirectores) {
			if (director == null)
				throw new ExcepcionCamposNulos();
			if (!(esNombre(director)))
				throw new ExcepcionCamposNoValidos();
		}
	}

}
