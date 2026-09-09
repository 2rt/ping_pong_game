module com.example.project_1_part_2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.project_1_part_3 to javafx.fxml;
    exports com.example.project_1_part_3;
}