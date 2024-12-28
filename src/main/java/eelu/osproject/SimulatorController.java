package eelu.osproject;

import eelu.osproject.algorithms.AlgorithmSelection;
import eelu.osproject.algorithms.ProcessUtils;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import eelu.osproject.algorithms.Process;
import javafx.util.Duration;
import java.util.Optional;

public class SimulatorController {

    @FXML
    private TableColumn<Process, Integer> arrivalTimeCol;

    @FXML
    private TextField arrivalTimeField;

    @FXML
    private TextField averageTurnaroundTime;

    @FXML
    private TextField averageWaitingTime;

    @FXML
    private TableColumn<Process, Integer> burstTimeCol;

    @FXML
    private TextField burstTimeField;

    @FXML
    private CheckBox checkBox;

    @FXML
    private ComboBox<String> comboBox;

    @FXML
    private TableColumn<Process, Integer> completionTimeCol;

    @FXML
    private TableColumn<Process, String> inputProcessIDCol;

    @FXML
    private TableView<Process> inputTable;

    @FXML
    private Text message;

    @FXML
    private TableColumn<Process, String> outputProcessIDCol;

    @FXML
    private TableView<Process> outputTable;

    @FXML
    private TableColumn<Process, Integer> priorityCol;

    @FXML
    private TextField priorityField;

    @FXML
    private TextField processIDField;

    @FXML
    private TableColumn<Process, Integer> responseTimeCol;

    @FXML
    private TextField throughput;

    @FXML
    private TextField timeQuantumField;

    @FXML
    private TableColumn<Process, Integer> turnaroundTimeCol;

    @FXML
    private TableColumn<Process, Integer> waitingTimeCol;

    @FXML
    public void initialize() {
        prepareComboBox();
        prepareTextFormatters();
        prepareInputTable();
        prepareOutputTable();
    }

    @FXML
    void addNewProcess(MouseEvent event) {
        Process p = new Process();
        if (burstTimeField.getText().isEmpty()) {
            unSuccessfulMSG("Burst Time is required!");
            return;
        } else {
            p.setBurstTime(Integer.parseInt(burstTimeField.getText()));
        }
        if (!arrivalTimeField.getText().isEmpty()) {
            p.setArrivalTime(Integer.parseInt(arrivalTimeField.getText()));
        }
        if (!priorityField.getText().isEmpty()) {
            p.setPriority(Integer.parseInt(priorityField.getText()));
        }
        if (processIDField.isDisable()) {
            p.setProcessID("");
        } else {
            if (processIDField.getText().isEmpty()) {
                unSuccessfulMSG("Process ID is required!");
                return;
            } else if (processIDField.getText().equalsIgnoreCase("p")) {
                unSuccessfulMSG("The Process ID must follow the format 'P{number}'.");
                return;
            }
            p.setProcessID(processIDField.getText());
        }
        inputTable.getItems().add(p);
        successfulMSG("The process has been added successfully.");
        clearInputFields();
    }

    @FXML
    void calculate(MouseEvent event) {
        outputTable.getItems().clear();
        if (inputTable.getItems().isEmpty()) {
            unSuccessfulMSG("At least one process must be added to the input table!");
            return;
        }
        if (!timeQuantumField.isDisable() && timeQuantumField.getText().isEmpty()) {
            unSuccessfulMSG("Time Quantum is required for the Round Robin algorithm!");
            return;
        }
        Process[] inputTableProcesses = inputTable.getItems().toArray(new Process[0]);
        Process[] processedData;
        String algorithm = comboBox.getValue();
        if (algorithm.contains("FCFS")) {
            processedData = AlgorithmSelection.FCFS(inputTableProcesses);
        } else if (algorithm.contains("Round")) {
            processedData = AlgorithmSelection.RoundRobin(Integer.parseInt(timeQuantumField.getText()), inputTableProcesses);
        } else if (algorithm.contains("SJF")) {
            processedData = AlgorithmSelection.SJFNonPreemptive(inputTableProcesses);
        } else {
            processedData = AlgorithmSelection.PriorityNonPreemptive(inputTableProcesses);
        }
        averageTurnaroundTime.setText(String.format("%.4f", Process.avgTurnaroundTime));
        averageWaitingTime.setText(String.format("%.4f", Process.avgWaitingTime));
        ProcessUtils.calcThroughput(processedData);
        throughput.setText(String.format("%.4f processes/time unit", Process.throughput));
        outputTable.getItems().addAll(processedData);
    }

    @FXML
    void clearAllProcesses(MouseEvent event) {
        if (inputTable.getItems().isEmpty()) {
            return;
        }
        boolean confirmed = showConfirmationDialog("Delete All Processes",
                "Are you sure you want to delete all processes? This action cannot be undone.");
        if (!confirmed) {
            return;
        }
        inputTable.getItems().clear();
        outputTable.getItems().clear();
        Process.count = 0;
        successfulMSG("All processes have been cleared successfully.");
    }

    @FXML
    void removeSelectedProcess(MouseEvent event) {
        Process p = inputTable.getSelectionModel().getSelectedItem();
        if (p != null) {
            inputTable.getItems().remove(p);
            outputTable.getItems().remove(p);
            successfulMSG("The selected process has been removed.");
        } else {
            unSuccessfulMSG("Please select a process to remove.");
        }
    }

    @FXML
    void checkBoxForAutoGenerateID(MouseEvent event) {
        processIDField.setDisable(checkBox.isSelected());
        if (checkBox.isSelected()) {
            processIDField.setText("");
        }
    }

    private boolean showConfirmationDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private FadeTransition fade(Node node, double fromOpacity, double toOpacity, Duration duration) {
        FadeTransition fade = new FadeTransition(duration, node);
        fade.setFromValue(fromOpacity);
        fade.setToValue(toOpacity);
        fade.setAutoReverse(true);
        return fade;
    }

    private void successfulMSG(String msg) {
        message.setText(msg);
        message.setStyle("-fx-fill: #00ff00");
        FadeTransition fadeIn = fade(message, 0, 1, Duration.millis(1000));
        FadeTransition fadeOut = fade(message, 1, 0, Duration.millis(1000));
        fadeOut.setDelay(Duration.millis(2000));
        new SequentialTransition(fadeIn, fadeOut).play();
    }

    private void unSuccessfulMSG(String msg) {
        message.setText(msg);
        message.setStyle("-fx-fill: #ff0000");
        FadeTransition fadeIn = fade(message, 0, 1, Duration.millis(1000));
        FadeTransition fadeOut = fade(message, 1, 0, Duration.millis(1000));
        fadeOut.setDelay(Duration.millis(2000));
        new SequentialTransition(fadeIn, fadeOut).play();
    }

    private void prepareComboBox() {
        comboBox.getItems().addAll(
                "FCFS (First Come First Serve)",
                "SJF (Shortest Job First)",
                "RR (Round Robin Algorithm)",
                "Priority CPU Scheduling"
        );
        comboBox.setValue(comboBox.getItems().get(0));
        comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.contains("Round")) {
                timeQuantumField.setDisable(false);
            } else {
                timeQuantumField.setDisable(true);
                timeQuantumField.setText("");
            }
        });
    }

    private void prepareTextFormatters() {
        processIDField.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().matches("[pP]?\\d{0,3}") ? change : null));
        arrivalTimeField.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().matches("\\d{0,5}") ? change : null));
        burstTimeField.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().matches("\\d{0,5}") ? change : null));
        priorityField.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().matches("\\d{0,5}") ? change : null));
        timeQuantumField.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().matches("\\d{0,5}") ? change : null));
    }

    private void prepareInputTable() {
        inputProcessIDCol.setCellValueFactory(new PropertyValueFactory<>("processID"));
        arrivalTimeCol.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        burstTimeCol.setCellValueFactory(new PropertyValueFactory<>("burstTime"));
        priorityCol.setCellValueFactory(new PropertyValueFactory<>("priority"));
    }

    private void prepareOutputTable() {
        outputProcessIDCol.setCellValueFactory(new PropertyValueFactory<>("processID"));
        responseTimeCol.setCellValueFactory(new PropertyValueFactory<>("responseTime"));
        turnaroundTimeCol.setCellValueFactory(new PropertyValueFactory<>("turnaroundTime"));
        waitingTimeCol.setCellValueFactory(new PropertyValueFactory<>("waitingTime"));
        completionTimeCol.setCellValueFactory(new PropertyValueFactory<>("completionTime"));
    }

    private void clearInputFields() {
        processIDField.setText("");
        burstTimeField.setText("");
        arrivalTimeField.setText("");
        priorityField.setText("");
    }
}
