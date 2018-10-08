package dominio;

import java.util.Date;
import static util.Fechas.*;

public class Socio {
	private String idSocio;
	private String nombreSocio;
	private String direccion;
	private Integer telefono;
	private String correo;
	private Date ultimoPago;
	private Credenciales credenciales;

	public Socio(String idSocio, String nombreSocio, String direccion, Integer telefono, String correo,
			Credenciales credenciales) {
		this.idSocio = idSocio;
		this.nombreSocio = nombreSocio;
		this.direccion = direccion;
		this.telefono = telefono;
		this.correo = correo;
		this.credenciales = credenciales;
		this.ultimoPago = fechaUltimoDiaMesAnterior(hoy());
	}

	public Socio() {
		super();
		this.ultimoPago = fechaUltimoDiaMesAnterior(hoy());
	}

	public void setIdSocio(String idSocio) {
		this.idSocio = idSocio;
	}

	public String getIdSocio() {
		return idSocio;
	}

	public void setNombreSocio(String nombreSocio) {
		this.nombreSocio = nombreSocio;
	}

	public String getNombreSocio() {
		return nombreSocio;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setTelefono(Integer telefono) {
		this.telefono = telefono;
	}

	public Integer getTelefono() {
		return telefono;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getCorreo() {
		return correo;
	}

	public void setUltimoPago(Date ultimoPago) {
		this.ultimoPago = ultimoPago;
	}

	public Date getUltimoPago() {
		return ultimoPago;
	}

	public void setCredenciales(Credenciales credenciales) {
		this.credenciales = credenciales;
	}

	public Credenciales getCredenciales() {
		return credenciales;
	}

	public boolean esMoroso() {
		return hoy().getTime() - ultimoPago.getTime() >= diasMes(mes(hoy()), anyo(hoy())) * MILISEGUNDOS_POR_DIA;
	}

	public int mensualidadesQueDebe() {
		int mesesDebe = 0;
		Socio aux = new Socio();
		aux.setUltimoPago(ultimoPago);
		while (aux.esMoroso()) {
			mesesDebe++;
			aux.setUltimoPago(fechaUltimoDiaMesSiguiente(aux.ultimoPago));
		}
		return mesesDebe;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Socio))
			return false;
		Socio otro = (Socio) obj;
		return idSocio.equals(otro.idSocio);
	}

}
