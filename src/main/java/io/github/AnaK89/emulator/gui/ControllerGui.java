package io.github.AnaK89.emulator.gui;

import io.github.AnaK89.emulator.gui.model.Action;
import io.github.AnaK89.emulator.gui.model.ProcString;
import io.github.AnaK89.emulator.gui.model.RamString;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.github.AnaK89.emulator.equipment.Impl.MultiprocessorImpl;
import io.github.AnaK89.emulator.equipment.Multiprocessor;

import java.net.URL;
import java.util.*;

import static io.github.AnaK89.emulator.gui.model.Action.NEW_WRITE_TO_OWN_CACHE;

public class ControllerGui implements Initializable{
    private static final Logger logger = LogManager.getLogger(ControllerGui.class);

    @FXML
    private TableView<RamString> ramTable;

    @FXML
    private TableColumn<RamString, Integer> idRamColumn;

    @FXML
    private TableColumn<RamString, String> valueRamColumn;

    @FXML
    private TableView<ProcString> processorTable1;

    @FXML
    private TableColumn<ProcString, Integer> idProcColumn1;

    @FXML
    private TableColumn<ProcString, String> stateProcColumn1;

    @FXML
    private TableColumn<ProcString, String> valueProcColumn1;

    @FXML
    private TableView<ProcString> processorTable2;

    @FXML
    private TableColumn<ProcString, Integer> idProcColumn2;

    @FXML
    private TableColumn<ProcString, String> stateProcColumn2;

    @FXML
    private TableColumn<ProcString, String> valueProcColumn2;

    @FXML
    private TableView<ProcString> processorTable3;

    @FXML
    private TableColumn<ProcString, Integer> idProcColumn3;

    @FXML
    private TableColumn<ProcString, String> stateProcColumn3;

    @FXML
    private TableColumn<ProcString, String> valueProcColumn3;

    @FXML
    private TableView<ProcString> processorTable4;

    @FXML
    private TableColumn<ProcString, Integer> idProcColumn4;

    @FXML
    private TableColumn<ProcString, String> stateProcColumn4;

    @FXML
    private TableColumn<ProcString, String> valueProcColumn4;

    @FXML
    private ComboBox<String> action;

    @FXML
    private ComboBox<Integer> processorNum;

    @FXML
    private TextField id;

    @FXML
    private VBox idPane;

    @FXML
    private TextField data;

    @FXML
    private VBox dataPane;

    @FXML
    private Button cleanUpAll;

    @FXML
    private ListView systemMessage;

    private final Multiprocessor multiprocessor = new MultiprocessorImpl(4);
    private final UtilsGui utils = new UtilsGui();

    public ControllerGui(){
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initRAM();
        initProcessors();
        initControlPanel();
    }

    @FXML
    private void request() {
        final int id = validateIDField();
        if(id == -1 && !Objects.equals(Action.getValueOf(action.getValue()), NEW_WRITE_TO_OWN_CACHE)){
            final String log = "ID Number of Processor is invalid";
            multiprocessor.addLog(log);
            logger.info(log);
        }

        switch (Objects.requireNonNull(Action.getValueOf(action.getValue()))){
            case REQUEST_VALID_INFO:
                if(id != -1){
                    multiprocessor.addLog(String.format("Request: %s - Proc: %d - ID: %d", action.getValue(), processorNum.getValue(), id));
                    multiprocessor.getProcessors().get(processorNum.getValue() - 1).requestValidInfo(id);
                }
                break;
            case NEW_WRITE_TO_OWN_CACHE:
                multiprocessor.addLog(String.format("Request: %s - Proc: %d - Data: %s", action.getValue(), processorNum.getValue(), data.getText()));
                multiprocessor.getProcessors().get(processorNum.getValue() - 1).writeToOwnCache(data.getText());
                break;
            case REWRITE_TO_OWN_CACHE:
                if(id != -1){
                    multiprocessor.addLog(String.format("Request: %s - Proc: %d - ID: %d - Data: %s", action.getValue(), processorNum.getValue(), id, data.getText()));
                    multiprocessor.getProcessors().get(processorNum.getValue() - 1).writeToOwnCache(id, data.getText());
                }
                break;
        }

        updateProcTables();
        updateRamTable();
        updateSystemMessage();
        this.id.clear();
        this.data.clear();
    }

    private void initRAM() {
        idRamColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        valueRamColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
    }

    private void initProcessors() {
        setProcCellValueFactory(idProcColumn1, stateProcColumn1, valueProcColumn1);
        setProcCellValueFactory(idProcColumn2, stateProcColumn2, valueProcColumn2);
        setProcCellValueFactory(idProcColumn3, stateProcColumn3, valueProcColumn3);
        setProcCellValueFactory(idProcColumn4, stateProcColumn4, valueProcColumn4);
    }

    private void initControlPanel(){
        initAction();
        setVisibleAndManaged(idPane,false);
        processorNum.setItems(FXCollections.observableArrayList(1, 2, 3, 4));
        processorNum.setValue(1);
    }

    private void initAction(){
        action.setItems(FXCollections.observableArrayList(Action.getAll()));
        action.setValue(NEW_WRITE_TO_OWN_CACHE.toString());
        action.setOnAction(event -> {
            switch (Objects.requireNonNull(Action.getValueOf(action.getValue()))) {
                case REQUEST_VALID_INFO:
                    setVisibleAndManaged(dataPane, false);
                    setVisibleAndManaged(idPane, true);
                    break;
                case NEW_WRITE_TO_OWN_CACHE:
                    setVisibleAndManaged(dataPane, true);
                    setVisibleAndManaged(idPane, false);
                    break;
                case REWRITE_TO_OWN_CACHE:
                    setVisibleAndManaged(dataPane, true);
                    setVisibleAndManaged(idPane, true);
                    break;
            }
        });
    }

    private void updateProcTables(){
        processorTable1.setItems(utils.toProcStringList(multiprocessor.getProcessors().get(0).getController().getCache()));
        processorTable2.setItems(utils.toProcStringList(multiprocessor.getProcessors().get(1).getController().getCache()));
        processorTable3.setItems(utils.toProcStringList(multiprocessor.getProcessors().get(2).getController().getCache()));
        processorTable4.setItems(utils.toProcStringList(multiprocessor.getProcessors().get(3).getController().getCache()));
    }

    private void updateRamTable(){
        ramTable.setItems(utils.toRamStringList(multiprocessor.getMemory().getAllData()));
    }

    private void updateSystemMessage(){
        systemMessage.setItems(utils.toSystemMessage(multiprocessor.getLogs()));
        systemMessage.scrollTo(multiprocessor.getLogs().get().size());
    }

    private void setVisibleAndManaged(final Pane pane, final boolean value){
        pane.setVisible(value);
        pane.setManaged(value);
    }

    private Integer validateIDField(){
        try{
            return Integer.parseInt(id.getText());
        } catch (NumberFormatException e){
            return -1;
        }
    }

    private void setProcCellValueFactory(
            final TableColumn<ProcString, Integer> id,
            final TableColumn<ProcString, String> state,
            final TableColumn<ProcString, String> value){
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        state.setCellValueFactory(new PropertyValueFactory<>("state"));
        value.setCellValueFactory(new PropertyValueFactory<>("value"));
    }
}
