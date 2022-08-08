module com.example.algoritmogenetico {
    requires javafx.controls;
    requires javafx.fxml;


    opens algoritmog to javafx.fxml;
    exports algoritmog;
}