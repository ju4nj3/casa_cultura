package com.wearefive.casacultura;

import com.wearefive.casacultura.data.repos.BooksRepository;
import com.wearefive.casacultura.data.repos.RatingsRepository;
import com.wearefive.casacultura.data.repos.RentalsRepository;
import com.wearefive.casacultura.data.repos.UsersRepository;
import com.wearefive.casacultura.entities.Book;
import com.wearefive.casacultura.entities.Rental;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javafx.util.Duration;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;


public class PanelController {

    @FXML private Label lblTitulo;    
    @FXML private Label lblFechaHora;
    
    @FXML private Label lblUsuarios;
    @FXML private Label lblLibros;
    @FXML private Label lblPrestamos;
    @FXML private Label lblValoraciones;
    
    @FXML private TableView<Rental> tvLibrosAlquilados;
    @FXML private TableColumn<Rental, Integer> colIdLA;
    @FXML private TableColumn<Rental, String> colTituloLA;
    @FXML private TableColumn<Rental, String> colUsuarioLA;
    @FXML private TableColumn<Rental, String> colFechaLA;
    @FXML private TableColumn<Rental, String> colExpiraLA;
    @FXML private TableColumn<Rental, String> colDevolucionLA;
    @FXML private TableColumn<Rental, Void> colAccionesLA;      
    
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public void initialize() {
        
        // Fecha/Hora
        
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.ZERO, e -> {
                lblFechaHora.setText(LocalDateTime.now().format(formatter));
            }),
            new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();   
        
        // Asignar datos de usuario
        
        var usuarioActual = App.getUsuarioActual();
        
        String bienvenida = String.format("Bienvenid%s, %s", 
                    usuarioActual.getSexo().equals("Hombre") ? "o" : usuarioActual.getSexo().equals("Mujer") ? "a" : "o/a", usuarioActual.getNombreApellido());
        
        App.setTituloVentana("Casa de la Cultura - " + bienvenida + " - Panel de Control");
        
        lblTitulo.setText("Hola, " + usuarioActual.getNombreApellido());        
                
        // Mensaje por defecto en el TableView
        tvLibrosAlquilados.setPlaceholder(new Label("No hay libros alquilados."));
        
        generarEstadisticas();    
        
        listarPrestamos();
    }    
        
    private void generarEstadisticas() {
        
        try {
            
           UsersRepository ur = new UsersRepository();
           BooksRepository br = new BooksRepository();
           RentalsRepository rr = new RentalsRepository();
           RatingsRepository ra = new RatingsRepository();
           
           lblUsuarios.setText(ur.getTotal().toString());
           lblLibros.setText(br.getTotal().toString());
           lblPrestamos.setText(rr.getTotal().toString());
           lblValoraciones.setText(ra.getTotal().toString());
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
       
    }
    
    private void listarPrestamos() {
        
        colIdLA.setCellValueFactory(new PropertyValueFactory<>("rentalId"));
        
        colTituloLA.setCellValueFactory(data -> new SimpleStringProperty(
            data.getValue().getCopyId().getBookId().getTitle())
        );
        
        colUsuarioLA.setCellValueFactory(data -> new SimpleStringProperty(
            data.getValue().getUserId().getNombreApellido())
        );

        colFechaLA.setCellValueFactory(data -> {
            Date date = data.getValue().getDate();
            String texto = date != null ? formatter.format(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()) : "";
            return new SimpleStringProperty(texto);
        });

        colExpiraLA.setCellValueFactory(data -> {
            Date date = data.getValue().getDue();
            String texto = date != null ? formatter.format(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()) : "";
            return new SimpleStringProperty(texto);
        });

        colDevolucionLA.setCellValueFactory(data -> {
            Date date = data.getValue().getReturnDate();
            String texto = date != null ? formatter.format(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()) : "";
            return new SimpleStringProperty(texto);
        });
        
        colAccionesLA.setCellFactory(col -> new TableCell<>() {
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
            tvLibrosAlquilados.setItems(alquileres);      
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
    private void onPanelUsuariosClick() throws IOException {
        App.setRoot("usuarios", "Casa de la Cultura - Usuarios"); 
    }

    @FXML
    private void onPanelLibrosClick() throws IOException {
        App.setRoot("libros", "Casa de la Cultura - Catálogo de Libros"); 
    }

    @FXML
    private void onPanelPrestamosClick() throws IOException {
        App.setRoot("prestamos", "Casa de la Cultura - Préstamo de Libros"); 
    }

    @FXML
    private void onPanelValoracionesClick() throws IOException {
        
    }

    @FXML
    private void onPanelEstadisticasClick() throws IOException {
        App.setRoot("estadisticas", "Casa de la Cultura - Estadísticas");
    }
       
    @FXML
    private void onSalirClick() throws IOException {        
        App.logout();
    }
}
