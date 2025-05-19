package com.wearefive.casacultura;

import com.wearefive.casacultura.data.repos.BooksRepository;
import com.wearefive.casacultura.data.repos.RatingsRepository;
import com.wearefive.casacultura.data.repos.RentalsRepository;
import com.wearefive.casacultura.data.repos.UsersRepository;
import com.wearefive.casacultura.entities.Book;
import com.wearefive.casacultura.entities.Rental;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

public class EstadisticasController {
  
    @FXML private PieChart pieChartSexos;
    @FXML private PieChart pieChartEdades;
    @FXML private BarChart barChartValoraciones;
    @FXML private BarChart barCharPrestados;
    @FXML private PieChart pieChartDevoluciones;

    public void initialize() {
        
        try {
            
            generarEstadisticas();    
            
        } catch(Exception e) {
            e.printStackTrace();
        }

    }    
        
    private void generarEstadisticas() {
                
         obtenerEstadisticasPorGenero();
         obtenerEstadisticasPorEdad();
         obtenerLibrosMasValorados();
         obtenerLibrosMasPrestados();
         obtenerLibrosPrestadosVSDevueltos();
           
    }  
    
    private void obtenerEstadisticasPorGenero() {
        
        UsersRepository ur = new UsersRepository();        
            
        Map<String, Long> datos = ur.getEstadisticasGenero();

        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();

        for (Map.Entry<String, Long> entry : datos.entrySet()) {
            data.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        pieChartSexos.setData(data);
        pieChartSexos.setTitle("Usuarios por género/sexo");
    }
    
    private void obtenerEstadisticasPorEdad() {
        
        UsersRepository ur = new UsersRepository();   
        
        Map<String, Long> datos = ur.getEstadisticasPorRangoEdad();

        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();

        for (var entry : datos.entrySet()) {
            data.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        pieChartEdades.setData(data);
        pieChartEdades.setTitle("Rangos de Edad");
    }
    
    private void obtenerLibrosMasValorados() {
        
        RatingsRepository rr = new RatingsRepository();
        
        List<Object[]> datos = rr.getLibrosMasValorados(15);

        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Promedio de valoración");

        for (Object[] fila : datos) {
            String titulo = (String) fila[0];
            Double promedio = (Double) fila[1];
            serie.getData().add(new XYChart.Data<>(titulo, promedio));
        }

        barChartValoraciones.getData().clear();
        barChartValoraciones.getData().add(serie);     
        barChartValoraciones.getXAxis().setTickLabelRotation(45);
    }
    
    private void obtenerLibrosMasPrestados() {
        
        RentalsRepository rr = new RentalsRepository();
        
        List<Object[]> datos = rr.getLibrosMasPrestados(15);

        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Número de alquileres");

        for (Object[] fila : datos) {
            String titulo = (String) fila[0];
            Long totalAlquileres = (Long) fila[1];
            serie.getData().add(new XYChart.Data<>(titulo, totalAlquileres));
        }

        barCharPrestados.getData().clear();
        barCharPrestados.getData().add(serie);

        barChartValoraciones.getXAxis().setTickLabelRotation(45);
    }
    
    private void obtenerLibrosPrestadosVSDevueltos() {
        
        RentalsRepository rr = new RentalsRepository();
        Map<String, Long> datos = rr.getEstadisticasDevoluciones();

        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        
        for (Map.Entry<String, Long> entry : datos.entrySet()) {
            data.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        pieChartDevoluciones.setData(data);
        pieChartDevoluciones.setTitle("Estado de devoluciones");
    }
       
    @FXML
    private void onActionVolver() throws IOException {        
        App.panel();
    }
}
