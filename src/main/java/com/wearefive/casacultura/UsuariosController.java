package com.wearefive.casacultura;

import com.wearefive.casacultura.data.repos.UsersRepository;
import com.wearefive.casacultura.entities.User;
import java.io.IOException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class UsuariosController {
    
    @FXML private TextField tfBuscar;
    @FXML private TableView<User> tvUsuarios;
    @FXML private TableColumn<User, Integer> colId;
    @FXML private TableColumn<User, String> colDNINIE;
    @FXML private TableColumn<User, String> colNombre;
    @FXML private TableColumn<User, String> colApellido;
    @FXML private TableColumn<User, String> colSexo;
    @FXML private TableColumn<User, String> colEmail;
    @FXML private TableColumn<User, String> colTelefono;
    @FXML private TableColumn<User, String> colComentario;
    
    private ObservableList<User> listaUsuarios;
    
    @FXML
    public void initialize() {
        
        colId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colDNINIE.setCellValueFactory(new PropertyValueFactory<>("dniNie"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colSexo.setCellValueFactory(new PropertyValueFactory<>("sexo"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colComentario.setCellValueFactory(new PropertyValueFactory<>("comentario"));

        try(UsersRepository ur = new UsersRepository()) {
            listaUsuarios = FXCollections.observableArrayList(ur.getAll());
            tvUsuarios.setItems(listaUsuarios);
        }        

        // Buscar por nombre o email
        tfBuscar.textProperty().addListener((obs, oldVal, newVal) -> {
            tvUsuarios.setItems(listaUsuarios.filtered(user ->
                user.getNombre().toLowerCase().contains(newVal.toLowerCase()) ||
                user.getApellido().toLowerCase().contains(newVal.toLowerCase()) ||
                user.getEmail().toLowerCase().contains(newVal.toLowerCase()) ||
                user.getTelefono().toLowerCase().contains(newVal.toLowerCase()) ||
                user.getComentario().toLowerCase().contains(newVal.toLowerCase())
            ));
        });
        
        tvUsuarios.setRowFactory(tv -> {
            TableRow<User> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    User user = row.getItem();
                    try {
                        UsuarioController.setUsuarioActual(user);
                        App.setRoot("usuario", "Casa de la Cultura - Usuario: " + user.getNombre() + " " + user.getApellido());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });
        
        Platform.runLater(() -> {
            tvUsuarios.requestFocus();
            tvUsuarios.getSelectionModel().selectFirst();
        }); 
    }

    @FXML
    private void onBuscarClick() throws IOException {
        
      
    }
    
    @FXML
    private void onVolverClick() throws IOException {
        App.panel();
    }
}