package com.wearefive.casacultura;

import com.wearefive.casacultura.entities.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;


/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Stage stage;
    private static User usuarioActual;

    public static User getUsuarioActual() {
        return usuarioActual;
    }

    public static void setUsuarioActual(User usuarioActual) {
        App.usuarioActual = usuarioActual;
    }       
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        
        stage = primaryStage;
        scene = new Scene(loadFXML("login"), 1366, 768);
        stage.setTitle("Casa de la Cultura - Iniciar sesión");
        stage.setScene(scene);
        stage.show();
    }      
    
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    static void setRoot(String fxml, String tituloVentana) throws IOException {
        scene.setRoot(loadFXML(fxml));
        stage.setTitle(tituloVentana);
    }
    
    static void setTituloVentana(String tituloVentana) {
        stage.setTitle(tituloVentana);
    }
    
    static void setCursor(Cursor cursor) {
        scene.setCursor(cursor);
    }
    
    static void panel() throws IOException {
        setRoot("panel", "Casa de la Cultura - Panel de Administración");
    }
    
    static void logout() throws IOException {
        setUsuarioActual(null);
        setRoot("login", "Casa de la Cultura - Iniciar sesión");
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    
     public static void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        
        // Asegura que JavaFX se lanza correctamente en macOS sin advertencias
        //System.setProperty("apple.laf.useScreenMenuBar", "true"); // opcional: para que el menú vaya al menú global
        //Application.launch(App.class, args);
        
        launch();                  

    }
}