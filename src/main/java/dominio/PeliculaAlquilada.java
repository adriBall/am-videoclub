package dominio;

import java.sql.Timestamp;

public class PeliculaAlquilada {
	private Timestamp tiempoAlquiler; // momento en el que se alquilo la pelicula
	private String enlace;
	private String idSocio;
	private String tituloVO;
	private String tituloEsp;
	private Integer anyo;

	public static final long DURACION_ALQUILER_MILIS = 48 * 60 * 60 * 1000L;

	public PeliculaAlquilada() {
		super();
		tiempoAlquiler = new Timestamp(System.currentTimeMillis());
	}

	public PeliculaAlquilada(Pelicula pelicula) {
		this.tituloVO = pelicula.getTituloVO();
		this.tituloEsp = pelicula.getTituloEsp();
		this.anyo = pelicula.getAnyo();
		this.tiempoAlquiler = new Timestamp(System.currentTimeMillis());
		this.enlace = "http://amvideoclub.com" + "/" + "peliculas" + "/" + Math.abs(tiempoAlquiler.hashCode())
				+ tituloVO.replaceAll("\\s+", "");
	}

	public void setTiempoAlquiler(Timestamp tiempoAlquiler) {
		this.tiempoAlquiler = tiempoAlquiler;
	}

	public Timestamp getTiempoAlquiler() {
		return tiempoAlquiler;
	}

	public void setEnlace(String enlace) {
		this.enlace = enlace;
	}

	public String getEnlace() {
		return enlace;
	}

	public void setIdSocio(String idSocio) {
		this.idSocio = idSocio;
	}

	public String getIdSocio() {
		return idSocio;
	}

	public void setTituloVO(String tituloVO) {
		this.tituloVO = tituloVO;
	}

	public String getTituloVO() {
		return tituloVO;
	}

	public void setTituloEsp(String tituloEsp) {
		this.tituloEsp = tituloEsp;
	}

	public String getTituloEsp() {
		return tituloEsp;
	}

	public void setAnyo(Integer anyo) {
		this.anyo = anyo;
	}

	public Integer getAnyo() {
		return anyo;
	}

	public long getTiempoRestanteMilis() {
		return Math.max(DURACION_ALQUILER_MILIS - System.currentTimeMillis() + tiempoAlquiler.getTime(), 0);
	}

	public boolean esCaducada() {
		return getTiempoRestanteMilis() <= 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof PeliculaAlquilada))
			return false;
		PeliculaAlquilada otra = (PeliculaAlquilada) obj;
		return idSocio.equals(otra.idSocio) && tituloVO.equals(otra.tituloVO) && anyo.equals(otra.anyo);
	}

}
