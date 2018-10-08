package validadores;

import java.util.Date;

import dominio.Credenciales;
import dominio.Socio;
import excepciones.ExcepcionCamposNoValidos;
import excepciones.ExcepcionCamposNulos;

import static util.Validadores.*;

public class ValidadorSocio {

	public static void validar(Socio socio) throws ExcepcionCamposNulos, ExcepcionCamposNoValidos {
		String id = socio.getIdSocio();
		String nombre = socio.getNombreSocio();
		String direccion = socio.getDireccion();
		String correo = socio.getCorreo();
		Integer telefono = socio.getTelefono();
		Date ultimoPago = socio.getUltimoPago();
		Credenciales credenciales = socio.getCredenciales();

		if (id == null || nombre == null || direccion == null || correo == null || telefono == null
				|| ultimoPago == null || credenciales == null || credenciales.getUsuario() == null
				|| credenciales.getAdmin() == null || credenciales.getContrasenya() == null)
			throw new ExcepcionCamposNulos();

		if (!(esAlfanumericoSinEspacios(id)) || id.length() != 9 || !(esNombre(nombre)) || direccion.isEmpty()
				|| esCadenaDeEspacios(direccion) || telefono.toString().length() != 9 || !(esMailValido(correo))
				|| !(esAlfanumericoSinEspacios(credenciales.getUsuario())) || credenciales.getContrasenya().isEmpty()
				|| esCadenaDeEspacios(credenciales.getContrasenya()) || credenciales.getUsuario().isEmpty())
			throw new ExcepcionCamposNoValidos();
	}

}
