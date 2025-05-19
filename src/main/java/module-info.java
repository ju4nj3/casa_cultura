module com.wearefive.casacultura {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.persistence;
    requires org.eclipse.persistence.jpa;
    requires org.apache.commons.lang3;

    opens com.wearefive.casacultura.entities to javafx.base, java.persistence;
    opens com.wearefive.casacultura to javafx.fxml;
    exports com.wearefive.casacultura;
}
