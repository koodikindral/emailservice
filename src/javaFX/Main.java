import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    Group group = new Group();
    Scene scene = new Scene(group, 300, 400);
    stage.setScene(scene);
    stage.setTitle("Vaheetapp 2 - Külalisteraamat");

    GridPane grid = new GridPane();
    grid.setPadding(new Insets(5, 5, 5, 5));

    grid.setVgap(5);
    grid.setHgap(5);

    scene.setRoot(grid);

    TextField name = new TextField();
    name.setPromptText("Sisesta oma nimi");
    name.setPrefColumnCount(10);
    GridPane.setConstraints(name, 0, 0);
    grid.getChildren().add(name);


    TextField comments = new TextField();
    comments.setPrefColumnCount(15);
    comments.setPromptText("Lisa kommentaar");
    GridPane.setConstraints(comments, 0, 1);
    grid.getChildren().add(comments);

    Button submit = new Button("Sisesta");
    GridPane.setConstraints(submit, 1, 0);
    grid.getChildren().add(submit);

    Button clear = new Button("Tühjenda");
    GridPane.setConstraints(clear, 1, 1);
    grid.getChildren().add(clear);

    Label label = new Label();
    GridPane.setConstraints(label, 0, 2);
    GridPane.setColumnSpan(label, 2);
    grid.getChildren().add(label);


    submit.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent e) {
        if ((comments.getText() != null && !comments.getText().isEmpty())) {
          label.setText(label.getText() + "\n" + name.getText() + ": " + comments.getText());
        }
      }
    });

    clear.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent e) {
        name.clear();
        comments.clear();
      }
    });

    stage.show();
  }
}
