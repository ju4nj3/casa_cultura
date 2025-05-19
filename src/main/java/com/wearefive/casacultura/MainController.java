package com.wearefive.casacultura;

import com.wearefive.casacultura.core.Apriori;
import com.wearefive.casacultura.data.repos.BooksRepository;
import com.wearefive.casacultura.data.repos.RentalsRepository;
import com.wearefive.casacultura.entities.Book;
import com.wearefive.casacultura.entities.Rental;
import com.wearefive.casacultura.entities.User;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.util.Duration;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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


public class MainController {

    @FXML private Label lblTitulo;    
    @FXML private Label lblFechaHora;    
    @FXML private Label lblNombre;
    @FXML private Label lblEmail;
    @FXML private Label lblDniNie;
    @FXML private Label lblTelefono;
    @FXML private Label lblSexo;
    @FXML private Label lblFechaNacimiento;
    @FXML private Label lblComentario;
    @FXML private ImageView ivUsuario;

    @FXML private GridPane gridLibros;
    
    @FXML private TableView<Rental> tvLibrosPrestados;
    @FXML private TableColumn<Rental, Integer> colId;
    @FXML private TableColumn<Rental, String> colTitulo;
    @FXML private TableColumn<Rental, String> colAutor;
    @FXML private TableColumn<Rental, String> colFecha;
    @FXML private TableColumn<Rental, String> colExpira;
    @FXML private TableColumn<Rental, String> colDevolucion;
    @FXML private TableColumn<Rental, Void> colAcciones;
    
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
        
        App.setTituloVentana("Casa de la Cultura - " + bienvenida);
        
        lblTitulo.setText("Hola, " + usuarioActual.getNombreApellido());
        
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
        
        // Mensaje por defecto en el TableView
        tvLibrosPrestados.setPlaceholder(new Label("No hay libros prestados."));
        
        listarPrestamos();
        obtenerLibrosRecomendados();
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
            var alquileres = FXCollections.observableArrayList(rr.getListaPorIdUsuario(App.getUsuarioActual()));
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
    
    private void obtenerLibrosRecomendados() {
        
        try {
            
            var rr = new RentalsRepository();  
            var br = new BooksRepository();
            var usuario = App.getUsuarioActual();
            
            // Obtenemos los libros por Género, Comentarios del Usuario y por Títulos relacionados
            
            List<Book> librosPorGenero = getLibrosPorGeneros(usuario);
            List<Book> librosPorComentarios = br.getLibrosPorComentarios(usuario);
            List<Book> librosPorTitulos = getLibrosPorTitulos(usuario);
            
            // Apriori
            var transacciones = rr.getTransacciones();
            // Establecemos en 2 el mínimo
            List<Set<Integer>> frecuentes = Apriori.getFrequentItemsets(transacciones, 2);
            
            Set<Integer> librosDelUsuario = App.getUsuarioActual().getRentalList().stream()
                .map(r -> r.getCopyId().getBookId().getBookId())
                .collect(Collectors.toSet());
            
            Set<Integer> idsRecomendados = Apriori.recomendar(librosDelUsuario, frecuentes);

            List<Book> librosAPriori = !idsRecomendados.isEmpty() ? br.getLibrosRecomendados(idsRecomendados) :  new ArrayList<>();
                        
            // Unificar listas priorizadas y quitar duplicados
            LinkedHashMap<Integer, Book> mapaRecomendaciones = new LinkedHashMap<>();

            librosAPriori.forEach(b -> mapaRecomendaciones.putIfAbsent(b.getBookId(), b));
            librosPorGenero.forEach(b -> mapaRecomendaciones.putIfAbsent(b.getBookId(), b));
            librosPorComentarios.forEach(b -> mapaRecomendaciones.putIfAbsent(b.getBookId(), b));
            librosPorTitulos.forEach(b -> mapaRecomendaciones.putIfAbsent(b.getBookId(), b));

            // Quitar libros ya alquilados
            librosDelUsuario.forEach(mapaRecomendaciones::remove);

            // Limitamos los resultados a 100 para no hacer lento el interfaz con demasiadas recomendaciones
            
            List<Book> librosRecomendados = new ArrayList<>(mapaRecomendaciones.values());

            if (librosRecomendados.size() > 100) {
                librosRecomendados = librosRecomendados.subList(0, 100);
            }
            
            // Mostramos los libros
            mostrarLibros(librosRecomendados);
            
        } catch(Exception e) {
            e.printStackTrace();
        }        
    }
    
    public List<Book> getLibrosPorGeneros(User usuario) {
        
        var br = new BooksRepository();
        
        Map<String, Long> frecuencia = usuario.getRentalList().stream()
            .flatMap(r -> r.getCopyId().getBookId().getGenreList().stream())
            .collect(Collectors.groupingBy(g -> g.getName().toLowerCase(), Collectors.counting()));

        List<String> generos = frecuencia.entrySet().stream()
            .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

        return !generos.isEmpty() ? br.getLibrosPorGeneros(generos) : new ArrayList<>();
    }
    
    public List<Book> getLibrosPorTitulos(User usuario) {
        
        var br = new BooksRepository();
        
        List<String> palabras = usuario.getRentalList().stream()
            .map(r -> r.getCopyId().getBookId().getTitle())
            .filter(Objects::nonNull)
            .flatMap(titulo -> Arrays.stream(titulo.toLowerCase().split("\\W+")))
            .filter(p -> p.length() > 3)
            .distinct()
            .collect(Collectors.toList());

        return !palabras.isEmpty() ? br.getLibrosPorTitulos(palabras) : new ArrayList<>();
    }
    
    private void mostrarLibros(List<Book> lista) {
         
        gridLibros.getChildren().clear();
        
        if(lista==null)
            return;

        int columna = 0;
        int fila = 0;

        for (Book libro : lista) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/wearefive/casacultura/item_libro.fxml"));
                Parent tarjeta = loader.load();

                ItemLibroController controller = loader.getController();
                controller.setLibro(libro);
                controller.setPaginaAnterior("main");

                gridLibros.add(tarjeta, columna, fila);

                columna++;
                
                if (columna == 5) {
                    columna = 0;
                    fila++;
                }
                
            } catch (IOException e) {
            }
        }
    }
    
     @FXML
    private void onActionVerLibros() throws IOException {
        App.setRoot("libros", "Casa de la Cultura - Catálogo de Libros");
    }
    
    @FXML
    private void onActionSalir() throws IOException {        
        App.logout();
    }
}
