package com.wearefive.casacultura;

import com.wearefive.casacultura.data.repos.UsersRepository;
import com.wearefive.casacultura.entities.User;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField tfUsuario;
     
    @FXML
    private void login() throws IOException {
        
        try {
            
            if (tfUsuario.getText() == null || tfUsuario.getText().trim().isEmpty()) {
                App.mostrarAlerta("Error de login", "Por favor, introduce tu ID de usuario", Alert.AlertType.ERROR);
                return;
            }
            else if (!tfUsuario.getText().matches("\\d+")) {
                App.mostrarAlerta("Error de login", "El ID de usuario debe contener solo números.", Alert.AlertType.ERROR);
                return;
            }
                    
            Integer id = Integer.valueOf(tfUsuario.getText());

            var ur = new UsersRepository();
            var usuario = ur.getId(id);

            if(usuario==null) {
                App.mostrarAlerta("Error de login", "ID Usuario no encontrado", Alert.AlertType.ERROR);
                return;
            }

            App.setUsuarioActual(usuario);            
           
            String bienvenida = String.format("Bienvenid%s, %s", 
                    usuario.getSexo().equals("Hombre") ? "o" : usuario.getSexo().equals("Mujer") ? "a" : "o/a", usuario.getNombreApellido());
            
            App.mostrarAlerta("Login correcto", bienvenida, Alert.AlertType.INFORMATION);
            
            if (usuario.getAdmin()) {
                App.setRoot("panel");
            } else {
                App.setRoot("main");
            }
        }
        catch(Exception e)  {
            e.printStackTrace();
            App.mostrarAlerta("Error grave", e.getMessage(), Alert.AlertType.INFORMATION);            
        }
    }      
}