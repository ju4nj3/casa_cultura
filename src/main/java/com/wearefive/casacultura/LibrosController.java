package com.wearefive.casacultura;

import com.wearefive.casacultura.data.repos.BooksRepository;
import com.wearefive.casacultura.data.repos.UsersRepository;
import com.wearefive.casacultura.entities.Book;
import com.wearefive.casacultura.entities.Genre;
import java.io.IOException;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class LibrosController {
    
    @FXML private AnchorPane apLibros;
    @FXML private TextField tfBuscar;
    @FXML private TableView<Book> tvLibros;
    @FXML private TableColumn<Book, Integer> colId;
    @FXML private TableColumn<Book, String> colTitulo;
    @FXML private TableColumn<Book, String> colTituloOriginal;
    @FXML private TableColumn<Book, String> colAutores;
    @FXML private TableColumn<Book, String> colIsbn;
    @FXML private TableColumn<Book, String> colIdioma;
    @FXML private TableColumn<Book, String> colAño;
    @FXML private TableColumn<Book, String> colGeneros;
    
    @FXML private Button btnVolver;
    
    private ObservableList<Book> listaLibros;
    
    @FXML
    public void initialize() {
        
        if(!App.getUsuarioActual().getAdmin()) {
            apLibros.getStylesheets().add(getClass().getResource("/styles/libros-main.css").toExternalForm());
        }
        else {
            apLibros.getStylesheets().add(getClass().getResource("/styles/libros-panel.css").toExternalForm());
        }    
               
        colId.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("title"));
        colTituloOriginal.setCellValueFactory(new PropertyValueFactory<>("originalTitle"));
        colAutores.setCellValueFactory(new PropertyValueFactory<>("authors"));
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colIdioma.setCellValueFactory(new PropertyValueFactory<>("languageCode"));
        colAño.setCellValueFactory(new PropertyValueFactory<>("originalPublicationYear"));
        
        colGeneros.setCellValueFactory(data -> new SimpleStringProperty(
            data.getValue().getGenreList().stream().map(Genre::getName).collect(Collectors.joining(", ")))
        );

        try(BooksRepository br = new BooksRepository()) {
            listaLibros = FXCollections.observableArrayList(br.getAll());
            tvLibros.setItems(listaLibros);
        }        

        // Buscar por nombre o email
            tfBuscar.textProperty().addListener((obs, oldVal, newVal) -> {
                
             String filtro = newVal.toLowerCase();

             tvLibros.setItems(listaLibros.filtered(book ->
                 (book.getTitle() != null && book.getTitle().toLowerCase().contains(filtro)) ||
                 (book.getOriginalTitle() != null && book.getOriginalTitle().toLowerCase().contains(filtro)) ||
                 (book.getAuthors() != null && book.getAuthors().toLowerCase().contains(filtro)) ||
                 (book.getIsbn() != null && book.getIsbn().toLowerCase().contains(filtro)) ||
                 (book.getOriginalPublicationYear() != null && book.getOriginalPublicationYear().toString().contains(filtro)) ||
                 (book.getLanguageCode() != null && book.getLanguageCode().toLowerCase().contains(filtro)) ||
                 (book.getGenreList() != null && book.getGenreList().stream().anyMatch(
                     g -> g.getName() != null && g.getName().toLowerCase().contains(filtro)
                 ))
             ));
         });
        
        tvLibros.setRowFactory(tv -> {
            TableRow<Book> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    Book book = row.getItem();
                    try {
                        LibroController.setLibro(book);
                        LibroController.setPaginaAnterior("libros");
                        App.setRoot("libro", "Casa de la Cultura - Libro: " + book.getTitle());  
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });
        
        Platform.runLater(() -> {
            tvLibros.requestFocus();
            tvLibros.getSelectionModel().selectFirst();
        }); 
    }

    @FXML
    private void onBuscarClick() throws IOException {
              
    }
    
    @FXML
    private void onVolverClick() throws IOException {
        
        if(!App.getUsuarioActual().getAdmin()) {
            App.setRoot("main");   
        } else {
            App.panel();
        }        
    }
}