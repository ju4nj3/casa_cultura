package com.wearefive.casacultura;

import com.wearefive.casacultura.data.repos.RatingsRepository;
import com.wearefive.casacultura.data.repos.RentalsRepository;
import com.wearefive.casacultura.entities.Book;
import com.wearefive.casacultura.entities.Copy;
import com.wearefive.casacultura.entities.Genre;
import com.wearefive.casacultura.entities.Rental;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;

public class LibroController {

    @FXML private AnchorPane apLibro;
    @FXML private VBox ivImagenLibroContainer;
    @FXML private ImageView ivImagenLibro;
    @FXML private Label lblTitulo;
    @FXML private Label lblAutores;
    @FXML private Label lblOriginalTitle;
    @FXML private Label lblAñoPublicacion;
    @FXML private Label lblIdioma;
    @FXML private Label lblIsbn;
    @FXML private Label lblGeneros;
    @FXML private Label lblValoracion;
    @FXML private Label lblAlquiler;
    @FXML private Button btnAlquilar;
    @FXML private Button btnVolver;
    
    private static Book libroActual;
    private static String paginaAnterior;

    public static void setLibro(Book libro) {
        libroActual = libro;
    }

    public static void setPaginaAnterior(String paginaAnterior) {
        LibroController.paginaAnterior = paginaAnterior;
    }  
    
    @FXML
    public void initialize() {
        
        if(libroActual!=null) {
            
            lblTitulo.setText(!StringUtils.isEmpty(libroActual.getTitle()) ? libroActual.getTitle() : "(sin título)");
            lblOriginalTitle.setText(!StringUtils.isEmpty(libroActual.getOriginalTitle()) ? libroActual.getOriginalTitle() : "(sin título)");
            lblAutores.setText(!StringUtils.isEmpty(libroActual.getAuthors()) ? libroActual.getAuthors() : "(sin datos)");
            lblAñoPublicacion.setText(libroActual.getOriginalPublicationYear() != null ? libroActual.getOriginalPublicationYear().toString() : "(sin datos)");
            lblIdioma.setText(libroActual.getLanguageCode() != null ? libroActual.getLanguageCode() : "(sin datos)");
            lblIsbn.setText(libroActual.getIsbn() != null ? libroActual.getIsbn() : "(sin datos)");
            lblGeneros.setText(!libroActual.getGenreList().isEmpty() ? String.join(", ", libroActual.getGenreList().stream().map(Genre::getName).collect(Collectors.joining(", "))) : "(sin datos)");
            
            try(RatingsRepository rr = new RatingsRepository()) {

                var valores = rr.getValores(libroActual.getBookId());
                lblValoracion.setText(String.format("Hay %s valoraciones de este libro, con una puntuación de %.2f.", valores.getTotal(), valores.getPromedio()));
                
            } catch (Exception e) {
                lblValoracion.setText("No se han podido obtener valoraciones de este libro.");
                e.printStackTrace();                
            }            
                        
            try(RentalsRepository rr = new RentalsRepository()) {
                var copiasLibro = libroActual.getCopyList().size();
                var numAlquileresActivos = rr.getAlquileresActivos();
                var disponibles = copiasLibro-numAlquileresActivos;
                
                if(disponibles>0) {                    
                    lblAlquiler.setText(String.format("Hay %s copia%s de este libro disponible.", disponibles, disponibles!=1 ? "s" : ""));
                    lblAlquiler.setStyle("-fx-text-fill: green;");
                    btnAlquilar.setVisible(true);
                } else {
                    lblAlquiler.setText("No hay copias de este libro disponible.");
                    lblAlquiler.setStyle("-fx-text-fill: red;");
                    btnAlquilar.setVisible(false);
                }
            } catch (Exception e) {                
                btnAlquilar.setVisible(false);
                e.printStackTrace();
            }
            
            try {
                if (libroActual.getImageUrl() != null && !libroActual.getImageUrl().isEmpty()) {
                    Image img = new Image(libroActual.getImageUrl(), true);
                    ivImagenLibro.setImage(img);
                    ivImagenLibro.fitWidthProperty().bind(ivImagenLibroContainer.widthProperty());
                }
            } catch (Exception e) {
                ivImagenLibro.setImage(null);
            }
        }
        
        if(!App.getUsuarioActual().getAdmin()) {
            apLibro.getStylesheets().add(getClass().getResource("/styles/libro-main.css").toExternalForm());
            btnVolver.setText("Volver a Mi Perfil");            
        } else {
            apLibro.getStylesheets().add(getClass().getResource("/styles/libro-panel.css").toExternalForm());
            btnVolver.setText("Volver a la Lista de Libros");
            btnAlquilar.setVisible(false);
        }        
    }
    
    @FXML
    private void onActionVolver() throws IOException {
        
        switch(paginaAnterior)
        {
            case "main":
                App.setRoot("main");
                break;
                
            case "libros":
                App.setRoot("libros", "Casa de la Cultura - Catálogo de Libros"); 
                break;
        }        
    }
    
    @FXML
    private void onActionAlquilar() throws IOException {
         
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Alquilar libro");
        alerta.setHeaderText("¿Quieres alquilar este libro?\nEl plazo de devolución es de 15 días a partir de la fecha actual.");
        alerta.setContentText("Título: " + libroActual.getTitle());

        Optional<ButtonType> resultado = alerta.showAndWait();
        
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            
            try {
                
                    Optional<Copy> copiaDisponible = libroActual.getCopyList().stream().filter(c -> c.getRentalList().stream().noneMatch(r -> r.getReturnDate() == null)).findFirst();
                    
                    if(copiaDisponible==null)
                        throw new Exception("No se pudo obtener una copia disponible del libro.");
                
                    LocalDateTime ahora = LocalDateTime.now();
                    LocalDateTime en15Dias = ahora.plusDays(15);

                    Date hoyDate = Date.from(ahora.atZone(ZoneId.systemDefault()).toInstant());
                    Date fecha15Dias = Date.from(en15Dias.atZone(ZoneId.systemDefault()).toInstant());
        
                    Rental rental = new Rental();
                    rental.setUserId(App.getUsuarioActual());
                    rental.setCopyId(copiaDisponible.get());
                    rental.setDate(hoyDate);
                    rental.setDue(fecha15Dias);

                    try (RentalsRepository rr = new RentalsRepository()) {
                        rr.Guardar(rental);
                    }

                App.mostrarAlerta("Alquiler registrado", "El libro ha sido alquilado correctamente.", Alert.AlertType.INFORMATION);
                
                App.setRoot("main");
                
            } catch (Exception e) {
                App.mostrarAlerta("Error", "No se pudo registrar el alquiler. Por favor, inténtalo de nuevo más tarde.", Alert.AlertType.ERROR);
            }            
        }
    }
}
