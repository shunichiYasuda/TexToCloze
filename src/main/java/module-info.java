module com.genSci.tools.TexToCloze{
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens com.genSci.tools.TexToCloze to javafx.fxml;
    exports com.genSci.tools.TexToCloze;
}