package com.wearefive.casacultura;

import com.wearefive.casacultura.data.repos.BooksRepository;
import com.wearefive.casacultura.data.repos.RentalsRepository;
import com.wearefive.casacultura.entities.Book;
import com.wearefive.casacultura.entities.Genre;
import com.wearefive.casacultura.entities.Rental;
import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class PrestamosController {
    
    @FXML private TextField tfBuscar;
    @FXML private TableView<Rental> tvLibrosPrestados;
    @FXML private TableColumn<Rental, Integer> colId;
    @FXML private TableColumn<Rental, String> colTitulo;
    @FXML private TableColumn<Rental, String> colUsuario;
    @FXML private TableColumn<Rental, String> colFecha;
    @FXML private TableColumn<Rental, String> colExpira;
    @FXML private TableColumn<Rental, String> colDevolucion;
    @FXML private TableColumn<Rental, Void> colAcciones;
    
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    
    private ObservableList<Rental> listaPrestamos;
    
    @FXML
    public void initialize() {
                
        listarPrestamos();
        
        tfBuscar.textProperty().addListener((obs, oldVal, newVal) -> {

         String filtro = newVal.toLowerCase();

        tvLibrosPrestados.setItems(listaPrestamos.filtered(rental ->
                (rental.getUserId()!= null && rental.getUserId().getNombreApellido().toLowerCase().contains(filtro)) ||
                (rental.getCopyId()!= null && rental.getCopyId().getBookId().getTitle().toLowerCase().contains(filtro)) ||
                (rental.getCopyId()!= null && rental.getCopyId().getBookId().getAuthors().toLowerCase().contains(filtro))
            ));
         });             
        
        Platform.runLater(() -> {
            tvLibrosPrestados.requestFocus();
            tvLibrosPrestados.getSelectionModel().selectFirst();
        }); 
    }
    
    private void listarPrestamos() {
        
        colId.setCellValueFactory(new PropertyValueFactory<>("rentalId"));
        
        colTitulo.setCellValueFactory(data -> new SimpleStringProperty(
            data.getValue().getCopyId().getBookId().getTitle())
        );
        
        colUsuario.setCellValueFactory(data -> new SimpleStringProperty(
            data.getValue().getUserId().getNombreApellido())
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
        
        colAcciones.setCellFactory(col -> new TableCell<>() {
            private final Button btnDevolver = new Button("Devolver");

            {
                btnDevolver.setOnAction(event -> {
                    Rental rental = getTableView().getItems().get(getIndex());

                    // Mostrar alerta de confirmación
                    Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
                    alerta.setTitle("Confirmar devolución");
                    alerta.setHeaderText("¿Desea marcar este libro como devuelto?");
                    alerta.setContentText("Título: " + rental.getCopyId().getBookId().getTitle());

                    Optional<ButtonType> resultado = alerta.showAndWait();
                    if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                        devolverLibro(rental);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Rental rental = getTableView().getItems().get(getIndex());
                    setGraphic(rental.getReturnDate() == null ? btnDevolver : null);
                }
            }
        });

        try (RentalsRepository rr = new RentalsRepository()) {
            var alquileres = FXCollections.observableArrayList(rr.getLista());
            tvLibrosPrestados.setItems(alquileres);      
        }
    }
    
    private void devolverLibro(Rental rental) {
        
        try {

            rental.setReturnDate(new Date());
            
             try (RentalsRepository rr = new RentalsRepository()) {
                rr.Guardar(rental);
            }

             // Refrescar tabla
            listarPrestamos();
            
            App.mostrarAlerta("Devolución registrada", "La devolución se ha registrado correctamente.", Alert.AlertType.INFORMATION);
            
        } catch (Exception e) {

            App.mostrarAlerta("Error", "No se pudo registrar la devolución. Por favor, inténtalo de nuevo más tarde.", Alert.AlertType.ERROR);
            
        } 
    }

    @FXML
    private void onBuscarClick() throws IOException {
              
    }
    
    @FXML
    private void onVolverClick() throws IOException {
        App.panel();
    }
}