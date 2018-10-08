package ui.custom;

import com.jfoenix.controls.JFXButton;

import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class RemovableField extends HBox {
	JFXButton removeButton;
	TextField field;
	
	public RemovableField(HBox parent) {
		super();
		initElements();
		removeButton.setOnMouseClicked((e) -> parent.getChildren().remove(this));
	}
	
	public RemovableField(HBox parent, String text) {
		initElements();
		removeButton.setOnMouseClicked((e) -> parent.getChildren().remove(this));
		field.setText(text);
	}
	
	private void initElements() {
		this.setPrefHeight(USE_COMPUTED_SIZE);
		this.setPrefWidth(USE_COMPUTED_SIZE);
		this.setSpacing(5.0);
		field = new TextField();
		field.setStyle("-fx-font-size: 14");
		field.setPrefWidth(120.0);
		this.getChildren().add(field);
		removeButton = new JFXButton("X");
		removeButton.setStyle("-fx-font-size: 14; -fx-text-fill: white;-fx-background-color: #4DB6AC;-fx-font-weight: bold;");
		removeButton.setPrefWidth(USE_COMPUTED_SIZE);
		removeButton.setPrefHeight(USE_COMPUTED_SIZE);
		this.getChildren().add(removeButton);
	}
	
	public String getText() {
		return field.getText().trim();
	}
	
}
