module com.genSci.tools.MoodleClozeHelper {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens com.genSci.tools.MoodleClozeHelper to javafx.fxml;
    exports com.genSci.tools.MoodleClozeHelper;
}