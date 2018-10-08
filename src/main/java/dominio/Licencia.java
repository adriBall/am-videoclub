package dominio;

public enum Licencia {

	BASICA("basica", 10), PREMIUM("premium", 20), SILVER("silver", 50), GOLD("gold", 100);

	private String nombreLicencia;
	private Integer numeroPrestamos;

	private Licencia(String nombreLicencia, Integer numeroPrestamos) {
		this.nombreLicencia = nombreLicencia;
		this.numeroPrestamos = numeroPrestamos;
	}

	public String getNombreLicencia() {
		return nombreLicencia;
	}

	public static Licencia getLicenciaPorNombre(String nombre) {
		for (Licencia licencia : values())
			if (licencia.getNombreLicencia().equals(nombre))
				return licencia;
		return null;
	}

	public Integer getNumeroPrestamos() {
		return numeroPrestamos;
	}

	@Override
	public String toString() {
		return this.nombreLicencia;
	}
}