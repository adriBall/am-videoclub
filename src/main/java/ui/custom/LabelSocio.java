package ui.custom;

import dominio.Socio;
import javafx.scene.control.Label;

public class LabelSocio extends Label {
	private Socio socio;
	
	public LabelSocio(Socio socio) {
		super(socio.getCredenciales().getUsuario()+" ("+socio.getNombreSocio()+")"+(socio.esMoroso()? " MOROSO":""));
		this.socio = socio;
		setStyle("-fx-font-size: 14");
	}

	public String getUsuario() {
		return socio.getCredenciales().getUsuario();
	}
	
	public String getNombreSocio() {
		return socio.getNombreSocio();
	}
	
	public Socio getSocio() {
		return socio;
	}
	
}
