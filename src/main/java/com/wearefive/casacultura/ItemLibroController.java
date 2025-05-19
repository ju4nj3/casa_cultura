/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wearefive.casacultura;

import com.wearefive.casacultura.entities.Book;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 *
 * @author Juanje
 */
public class ItemLibroController {

    @FXML private ImageView imagenLibro;
    @FXML private Label tituloLabel;
    @FXML private Label autorLabel;
    @FXML private Label anioLabel;

    private Book libro;
    private String paginaAnterior;
    
    public void setLibro(Book libro) {
        this.libro = libro;
        tituloLabel.setText(libro.getOriginalTitle());
        autorLabel.setText(libro.getAuthors());
        anioLabel.setText(String.valueOf(libro.getOriginalPublicationYear()));

        try {
            Image imagen = new Image(libro.getImageUrl(), true);
            imagenLibro.setImage(imagen);
        } catch (Exception e) {
            imagenLibro.setImage(null); // Si falla
        }
    }

    public void setPaginaAnterior(String paginaAnterior) {
        this.paginaAnterior = paginaAnterior;
    }        
    
    @FXML
    private void onLibroClick() throws IOException {
        LibroController.setLibro(libro);
        LibroController.setPaginaAnterior(paginaAnterior);
        App.setRoot("libro", "Casa de la Cultura - Libro: " + libro.getTitle());        
    }
    
    @FXML
    private void onLibroClickNuevaVentana() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/wearefive/casacultura/libro.fxml"));
            Parent root = loader.load();

            LibroController lc = loader.getController();
            lc.setLibro(libro);

            Stage stage = new Stage();
            stage.setTitle(libro.getTitle());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
