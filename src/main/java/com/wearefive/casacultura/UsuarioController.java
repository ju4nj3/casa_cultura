package com.wearefive.casacultura;

import com.wearefive.casacultura.data.repos.RentalsRepository;
import com.wearefive.casacultura.entities.Rental;
import com.wearefive.casacultura.entities.User;
import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class UsuarioController {
    
    @FXML private Label lblNombre;
    @FXML private Label lblEmail;
    @FXML private Label lblDniNie;
    @FXML private Label lblTelefono;
    @FXML private Label lblSexo;
    @FXML private Label lblFechaNacimiento;
    @FXML private Label lblComentario;
    @FXML private ImageView ivUsuario;
    
    @FXML private TableView<Rental> tvLibrosPrestados;
    @FXML private TableColumn<Rental, Integer> colId;
    @FXML private TableColumn<Rental, String> colTitulo;
    @FXML private TableColumn<Rental, String> colAutor;
    @FXML private TableColumn<Rental, String> colFecha;
    @FXML private TableColumn<Rental, String> colExpira;
    @FXML private TableColumn<Rental, String> colDevolucion;
    
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private static User usuarioActual;

    public static void setUsuarioActual(User usuario) {
        usuarioActual = usuario;
    }

    @FXML
    public void initialize() {
        
        if (usuarioActual != null) {            
            
            String img;

            switch(usuarioActual.getSexo()) {
                case "Hombre":
                    img = "avatar-chico.png";
                    break;

                case "Mujer":
                    img = "avatar-chica.png";
                    break;

                default:
                    img = "avatar-neutro.png";
                    break;
            }        

            Image imagen = new Image(getClass().getResourceAsStream("/images/" + img));
            ivUsuario.setImage(imagen);

            lblNombre.setText(usuarioActual.getNombreApellido());
            lblEmail.setText(usuarioActual.getEmail());
            lblDniNie.setText(usuarioActual.getDniNie());
            lblTelefono.setText(usuarioActual.getTelefono());
            lblSexo.setText(usuarioActual.getSexo());
            lblFechaNacimiento.setText(usuarioActual.getFechaNacimiento());
            lblComentario.setText(usuarioActual.getComentario());    
            
            listarPrestamos();
        }
    }
    
    private void listarPrestamos() {
        
        colId.setCellValueFactory(new PropertyValueFactory<>("rentalId"));
        
        colTitulo.setCellValueFactory(data -> new SimpleStringProperty(
            data.getValue().getCopyId().getBookId().getTitle())
        );
        
        colAutor.setCellValueFactory(data -> new SimpleStringProperty(
            data.getValue().getCopyId().getBookId().getAuthors())
        );

        colFecha.setCellValueFactory(data -> {
            Date date = data.getValue().getDate();
            String texto = date != null ? formatter.format(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()) : "";
            return new SimpleStringProperty(texto);
        });

        colExpira.setCellValueFactory(data -> {
            Date date = data.getValue().getDue();
            String texto = date != null ? formatter.format(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()) : "";
            return new SimpleStringProperty(texto);
        });

        colDevolucion.setCellValueFactory(data -> {
            Date date = data.getValue().getReturnDate();
            String texto = date != null ? formatter.format(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()) : "";
            return new SimpleStringProperty(texto);
        });
     
        try (RentalsRepository rr = new RentalsRepository()) {
            var alquileres = FXCollections.observableArrayList(rr.getListaPorIdUsuario(usuarioActual));
            tvLibrosPrestados.setItems(alquileres);      
        }
    }
    
    @FXML
    private void onVolverClick() throws IOException {
        App.setRoot("usuarios", "Casa de la Cultura - Usuarios"); 
    }
}
