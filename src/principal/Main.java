package principal;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("FormularioPrincipal.fxml"));
			Scene scene = new Scene(root,1350,720);
			scene.getStylesheets().add(getClass().getResource("dark.css").toExternalForm());
			primaryStage.setTitle(Constantes.APP_NAME + " " + Constantes.VERSION + " by " + Constantes.AUTHOR);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
