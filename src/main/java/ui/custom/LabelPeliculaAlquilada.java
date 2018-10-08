package ui.custom;

import dominio.PeliculaAlquilada;
import javafx.scene.control.Label;

public class LabelPeliculaAlquilada extends Label {
	private PeliculaAlquilada alquilada;
	
	public LabelPeliculaAlquilada(PeliculaAlquilada alquilada) {
		super(alquilada.getTituloEsp()+" ("+alquilada.getTituloVO()+" - "+alquilada.getAnyo()+") "+calculaHorasRestantes(alquilada)+" h. restantes"+(alquilada.esCaducada()? " CADUCADA":""));
		this.alquilada = alquilada;
		setStyle("-fx-font-size: 14");
		if(alquilada.esCaducada())
			setStyle(getStyle()+"-fx-text-fill: rgb(230, 81, 0);");
	}

	private static int calculaHorasRestantes(PeliculaAlquilada alquilada) {
		return Math.round(alquilada.getTiempoRestanteMilis()/(1000*60*60));
	}
	
	public String getEnlace(){
		return alquilada.getEnlace();
	}
	
	public PeliculaAlquilada getAlquilada() {
		return alquilada;
	}
	
}
