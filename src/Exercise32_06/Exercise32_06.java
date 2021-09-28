package Exercise32_06;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;


public class Exercise32_06 extends Application {

    private TextField tfTableName = new TextField();
    private TextArea taResult = new TextArea();
    private Button btnShowContent = new Button("Show Contents");
    private Label lblStatus = new Label();

    //Statement for query execution
    private Statement stmt;

    @Override
    public void start(Stage primaryStage) throws Exception{

        HBox hBox = new HBox(5);
        hBox.getChildren().addAll(new Label("Table Name"), tfTableName, btnShowContent);
        hBox.setAlignment(Pos.CENTER);

        BorderPane pane = new BorderPane();
        pane.setCenter(new ScrollPane(taResult));
        pane.setTop(hBox);
        pane.setBottom(lblStatus);

        //Create a scene and place it in the stage
        Scene scene = new Scene(pane, 500, 200);
        primaryStage.setTitle("Exercise32-06");
        primaryStage.setScene(scene);
        primaryStage.show();

        initializeDB();

        btnShowContent.setOnAction(actionEvent -> showContents());
    }

    private void initializeDB() {
        //Load the JDBC driver
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            System.out.println("Driver loaded");

            //Establish a connection
            Connection connection = DriverManager.getConnection("jdbc:ucanaccess://C:/Data/exampleMDB.accdb");

            /*("jdbc:oracle:thin:@liang.armstrong.edu:1521:ora9i",
          "scott", "tiger"); */

            //Create a Statement
            stmt = connection.createStatement();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showContents(){
        String tableName = tfTableName.getText();

        try {
            String queryString = "select * from " + tableName;
            ResultSet resultSet = stmt.executeQuery(queryString);
            ResultSetMetaData rsMetaData = resultSet.getMetaData();
            for (int i = 1; i <= rsMetaData.getColumnCount(); i++){
                taResult.appendText(rsMetaData.getColumnName(i) + " ");
            }
            taResult.appendText("\n");

            //Iterate through the result and print the student names
            while (resultSet.next()){
                for (int i = 1; i <= rsMetaData.getColumnCount(); i++){
                    taResult.appendText(resultSet.getObject(i) + " ");
                }
                taResult.appendText("\n");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * The main method is only needed for the IDE with limited JavaFX support.
     * Not needed for running from the command line.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
