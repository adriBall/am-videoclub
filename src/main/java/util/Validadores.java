package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validadores {

	public static boolean esAlfanumericoSinEspacios(String cadena) {
		for (int i = 0; i < cadena.length(); i++) {
			char caracter = cadena.charAt(i);
			if (!Character.isLetterOrDigit(caracter)) {
				return false;
			}
		}
		return true;
	}

	public static boolean esAlfanumericoMasEspacios(String cadena) {
		for (int i = 0; i < cadena.length(); i++) {
			char caracter = cadena.charAt(i);
			if (!(Character.isLetterOrDigit(caracter) || caracter == ' ')) {
				return false;
			}
		}
		return true;
	}

	public static boolean esCadenaDeEspacios(String cadena) {
		for (int i = 0; i < cadena.length(); i++) {
			char caracter = cadena.charAt(i);
			if (caracter != ' ') {
				return false;
			}
		}
		return true;
	}

	public static boolean esMailValido(String cadena) {
		Pattern patron = Pattern.compile(
				"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		Matcher matcher = patron.matcher(cadena);
		return matcher.matches();
	}

	public static boolean esNombre(String cadena) {
		if (cadena.isEmpty())
			return false;
		for (int i = 0; i < cadena.length(); i++) {
			char caracter = cadena.charAt(i);
			if (!(Character.isLetter(caracter) || caracter == ' ')) {
				return false;
			}
		}
		return true;
	}

}
