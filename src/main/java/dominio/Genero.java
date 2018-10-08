package dominio;

public enum Genero {

	DRAMA("drama"), COMEDIA("comedia"), ACCION("accion"), AVENTURA("aventura"), TERROR("terror"), CIENCIA_FICCION(
			"ciencia ficcion"), ROMANTICO(
					"romantico"), MUSICAL("musical"), SUSPENSE("suspense"), BELICA("belica"), FANTASIA("fantasia");

	private String nombreGenero;

	private Genero(String nombreGenero) {
		this.nombreGenero = nombreGenero;
	}

	public String getNombreGenero() {
		return nombreGenero;
	}

	public static Genero getGeneroPorNombre(String nombre) {
		for (Genero genero : values())
			if (genero.getNombreGenero().equals(nombre))
				return genero;
		return null;
	}

	@Override
	public String toString() {
		return this.nombreGenero;
	}

}