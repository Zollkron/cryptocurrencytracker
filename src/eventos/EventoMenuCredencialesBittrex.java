package eventos;

import java.util.Optional;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import principal.ControladorFormularioPrincipal;

public class EventoMenuCredencialesBittrex implements EventHandler<ActionEvent> {

	private ControladorFormularioPrincipal controlador;
	
	public EventoMenuCredencialesBittrex(ControladorFormularioPrincipal controlador) {
		super();
		this.controlador = controlador;
	}

	@Override
	public void handle(ActionEvent event) {
		// Create the custom dialog.
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("Settings");
		dialog.setHeaderText(
				"Set your Bittrex API code and key secret to operate. Cancel if only monitoring.\n\n"
						+ "DISCLAIMER: This data is needed to operate with Bittrex Exchange but it's given by you under YOUR OWN RISK.\n\n"
						+ "WARNING: The data IS ONLY STORED in your local machine for major security, "
						+ "and ONLY the public API Code is\n sent to Bittrex for operate with its API. PLEASE, NEVER SHARE YOUR SECRET KEY WITH ANYONE!!!");

		// Set the button types.
		ButtonType saveButtonType = new ButtonType("Save", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

		// Create the username and password labels and fields.
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField apiCode = new TextField();
		apiCode.setPromptText("API Code");
		TextField secretKey = new TextField();
		secretKey.setPromptText("Secret Key");

		grid.add(new Label("API Code:"), 0, 0);
		grid.add(apiCode, 1, 0);
		grid.add(new Label("Secret Key:"), 0, 1);
		grid.add(secretKey, 1, 1);

		// Enable/Disable login button depending on whether a username
		// was entered.
		Node saveButton = dialog.getDialogPane().lookupButton(saveButtonType);
		saveButton.setDisable(true);

		// Do some validation (using the Java 8 lambda syntax).
		apiCode.textProperty().addListener((observable, oldValue, newValue) -> {
			saveButton.setDisable(newValue.trim().isEmpty());
		});

		dialog.getDialogPane().setContent(grid);

		// Request focus on the api code field by default.
		Platform.runLater(() -> apiCode.requestFocus());

		// Convert the result to a apicode-secretkey-pair when the save
		// button is clicked.
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == saveButtonType) {
				return new Pair<>(apiCode.getText(), secretKey.getText());
			}
			return null;
		});

		Optional<Pair<String, String>> result = dialog.showAndWait();

		result.ifPresent(apiCodeSecretKey -> {
			System.out.println(
					"API Code=" + apiCodeSecretKey.getKey() + ", Secret Key=" + apiCodeSecretKey.getValue());
			controlador.getServicioPropiedades().setPropiedadesAPI(apiCodeSecretKey.getKey(), apiCodeSecretKey.getValue());
			controlador.setCodigoApi(apiCodeSecretKey.getKey());
			controlador.setKeySecret(apiCodeSecretKey.getValue());
			System.out.println(controlador.getCodigoApi() + " - " + controlador.getKeySecret());
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information");
			alert.setHeaderText("Changes saved");
			alert.setContentText("You must restart the application. The app will close now.");
			alert.showAndWait();
			System.exit(0);
		});
	}
	
}
