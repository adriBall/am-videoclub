package dominio;

public class Credenciales {

	private String usuario;
	private String contrasenya;
	private Boolean admin;

	public Credenciales() {
		super();
	}

	public Credenciales(String usuario, String contrasenya, Boolean admin) {
		this.usuario = usuario;
		this.contrasenya = contrasenya;
		this.admin = admin;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setContrasenya(String contrasenya) {
		this.contrasenya = contrasenya;
	}

	public String getContrasenya() {
		return contrasenya;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	public Boolean getAdmin() {
		return admin;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Credenciales))
			return false;
		Credenciales otro = (Credenciales) obj;
		return usuario.equals(otro.usuario) && contrasenya.equals(otro.contrasenya);
	}

}