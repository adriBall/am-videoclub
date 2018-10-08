package ui.custom;

import dominio.Pelicula;
import javafx.scene.control.Label;

public class LabelPelicula extends Label {
	private Pelicula pelicula;
	
	public LabelPelicula(Pelicula pelicula) {
		super(pelicula.getTituloEsp()+" ("+pelicula.getTituloVO()+" - "+pelicula.getAnyo()+")"+(pelicula.getCopiasDisponibles() <= 0? " AGOTADA":""));
		this.pelicula = pelicula;
		setStyle("-fx-font-size: 14;");
		if(pelicula.getCopiasDisponibles() <= 0)
			setStyle(getStyle()+"-fx-text-fill: rgb(230, 81, 0);");
	}

	public String getTituloVO() {
		return pelicula.getTituloVO();
	}
	
	public Integer getAnyo() {
		return pelicula.getAnyo();
	}
	
	public Pelicula getPelicula() {
		return pelicula;
	}
	
}
