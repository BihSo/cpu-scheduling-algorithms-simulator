module eelu.osproject {
    requires javafx.controls;
    requires javafx.fxml;

    opens eelu.osproject to javafx.fxml;
    exports eelu.osproject;

    opens eelu.osproject.algorithms to javafx.fxml;
    exports eelu.osproject.algorithms;
}