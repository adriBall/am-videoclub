package dominio;

import java.util.ArrayList;
import java.util.List;

public class Pelicula {
	private String tituloEsp;
	private String tituloVO;
	private Integer anyo;
	private String sinopsis;
	private Licencia nombreLicencia;
	private List<Genero> listaGeneros;
	private List<String> listaActores;
	private List<String> listaDirectores;
	private Integer copiasDisponibles;

	public Pelicula() {
		super();
		listaGeneros = new ArrayList<Genero>();
		listaActores = new ArrayList<String>();
		listaDirectores = new ArrayList<String>();
	}

	public Pelicula(String tituloEsp, String tituloVO, Integer anyo, String sinopsis, Licencia nombreLicencia,
			List<Genero> listaGeneros, List<String> listaActores, List<String> listaDirectores) {
		this.tituloEsp = tituloEsp;
		this.tituloVO = tituloVO;
		this.anyo = anyo;
		this.sinopsis = sinopsis;
		this.nombreLicencia = nombreLicencia;
		this.listaGeneros = listaGeneros;
		this.listaActores = listaActores;
		this.listaDirectores = listaDirectores;
		this.copiasDisponibles = nombreLicencia.getNumeroPrestamos();
	}

	public void setTituloEsp(String tituloEsp) {
		this.tituloEsp = tituloEsp;
	}

	public String getTituloEsp() {
		return tituloEsp;
	}

	public void setTituloVO(String tituloVO) {
		this.tituloVO = tituloVO;
	}

	public String getTituloVO() {
		return tituloVO;
	}

	public void setAnyo(Integer anyo) {
		this.anyo = anyo;
	}

	public Integer getAnyo() {
		return anyo;
	}

	public void setSinopsis(String sinopsis) {
		this.sinopsis = sinopsis;
	}

	public String getSinopsis() {
		return sinopsis;
	}

	public void setCopiasDisponibles(Integer copiasDisponibles) {
		this.copiasDisponibles = copiasDisponibles;
	}

	public Integer getCopiasDisponibles() {
		return copiasDisponibles;
	}

	public void setNombreLicencia(Licencia nombreLicencia) {
		if (this.nombreLicencia == null)
			this.nombreLicencia = nombreLicencia;
		if (copiasDisponibles == null)
			copiasDisponibles = nombreLicencia.getNumeroPrestamos();
		if (!this.nombreLicencia.equals(nombreLicencia)) {
			int prestadas = this.nombreLicencia.getNumeroPrestamos() - copiasDisponibles;
			this.nombreLicencia = nombreLicencia;
			this.copiasDisponibles = this.nombreLicencia.getNumeroPrestamos() - prestadas;
		}

	}

	public Licencia getNombreLicencia() {
		return nombreLicencia;
	}

	public void addGenero(Genero genero) {
		listaGeneros.add(genero);
	}

	public List<Genero> getListaGeneros() {
		return listaGeneros;
	}

	public void addDirector(String director) {
		listaDirectores.add(director);
	}

	public List<String> getListaDirectores() {
		return listaDirectores;
	}

	public void addActor(String actor) {
		listaActores.add(actor);
	}

	public List<String> getListaActores() {
		return listaActores;
	}

	public void setListaActores(List<String> listaActores) {
		this.listaActores = listaActores;
	}

	public void setListaDirectores(List<String> listaDirectores) {
		this.listaDirectores = listaDirectores;
	}

	public void setListaGeneros(List<Genero> listaGeneros) {
		this.listaGeneros = listaGeneros;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Pelicula))
			return false;
		Pelicula otra = (Pelicula) obj;
		return tituloVO.equals(otra.tituloVO) && anyo.equals(otra.anyo);
	}

}
