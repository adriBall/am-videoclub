package util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class Fechas {
	public static final long MILISEGUNDOS_POR_DIA = 24 * 60 * 60 * 1000;

	public static Date fechaUltimoDiaMesAnterior(Date fecha) {
		int mes = mes(fecha);
		int anyo = anyo(fecha);

		if (mes != 1)
			mes -= 1;
		else {
			mes = 12;
			anyo -= 1;
		}

		return getDate(diasMes(mes, anyo), mes, anyo);
	}

	public static Date fechaUltimoDiaMesSiguiente(Date fecha) {
		int mes = mes(fecha);
		int anyo = anyo(fecha);

		if (mes != 12)
			mes += 1;
		else {
			mes = 1;
			anyo += 1;
		}

		return getDate(diasMes(mes, anyo), mes, anyo);
	}

	public static boolean esBisiesto(int anyo) {
		return anyo % 4 == 0 && ((anyo % 100 != 0) || (anyo % 400 == 0));
	}

	public static int diasMes(int mes, int anyo) {
		if (mes == 1 || mes == 3 || mes == 5 || mes == 7 || mes == 8 || mes == 10 || mes == 12)
			return 31;
		if (mes != 2)
			return 30;
		if (esBisiesto(anyo))
			return 29;
		else
			return 28;
	}

	// Este mes se considera numerando los meses desde 1
	public static int mes(Date fecha) {
		SimpleDateFormat dateFormatMes = new SimpleDateFormat("MM");
		return Integer.parseInt(dateFormatMes.format(fecha));
	}

	public static int anyo(Date fecha) {
		SimpleDateFormat dateFormatAnyo = new SimpleDateFormat("yyyy");
		return Integer.parseInt(dateFormatAnyo.format(fecha));
	}

	public static Date hoy() {
		return new Date();
	}

	public static Date getDate(int dia, int mes, int anyo) {
		// En el constructor de Gregoriancalendar los meses se numeran desde 0
		return new GregorianCalendar(anyo, mes - 1, dia).getTime();
	}

}
