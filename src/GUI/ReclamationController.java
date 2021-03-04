/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import dbconnection.Dbconnection;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import models.Activite;
import models.Reclamation;

/**
 * FXML Controller class
 *
 * @author karbo
 */
public class ReclamationController implements Initializable {

    @FXML
    private TableView<Reclamation> reclamationTable;
    @FXML
    private TableColumn<Reclamation, String> idCol;
    @FXML
    private TableColumn<Reclamation, String> typeCol;
    @FXML
    private TableColumn<Reclamation, String> contenuCol;
    @FXML
    private TableColumn<Reclamation, String> editCol;
    @FXML
    private TextField searchFid;
    
    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    Reclamation reclamation = null ;
    
    ObservableList<Reclamation>  reclamationList = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        loaddate();
    }    

    @FXML
    private void getActivites(MouseEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/GUI/tableView.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @FXML
    private void close(MouseEvent event) {
        
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void getAddreclamation(MouseEvent event) {
         try {
            Parent parent = FXMLLoader.load(getClass().getResource("/GUI/Addreclamation.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(ReclamationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void sort(MouseEvent event) {
        
        try {
            reclamationList.clear();
            
            query = "SELECT * FROM `reclamation` ORDER BY nom  ";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()){
                reclamationList.add(new  Reclamation(
                        resultSet.getInt("id"),
                        resultSet.getString("type"),
                        resultSet.getString("contenu")));
                reclamationTable.setItems(reclamationList);
                
            }
            System.out.println("sort execute");
            
            
        } catch (SQLException ex) {
            Logger.getLogger(ReclamationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void chercher(MouseEvent event) {
        
        try {
            reclamationList.clear();
            
            query = "SELECT * FROM `reclamation` WHERE (nom LIKE ?)";
            preparedStatement = connection.prepareStatement(query);
            String x = "%";
            String r = x+searchFid.getText()+x;
            preparedStatement.setString(1, r);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()){
                reclamationList.add(new  Reclamation(
                        resultSet.getInt("id"),
                        resultSet.getString("type"),
                        resultSet.getString("contenu")));
                reclamationTable.setItems(reclamationList);
                
            }
            System.out.println("search execute");
            
            
        } catch (SQLException ex) {
            Logger.getLogger(ReclamationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void refresh() {
        
        try {
            reclamationList.clear();
            
            query = "SELECT * FROM `reclamation`";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()){
                reclamationList.add(new  Reclamation(
                        resultSet.getInt("id"),
                        resultSet.getString("type"),
                        resultSet.getString("contenu")));
                reclamationTable.setItems(reclamationList);
                
                System.out.println("refresh");
                
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(ReclamationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void getreclamations(MouseEvent event) {
    }
    
    
    private void loaddate() {
        
        connection = Dbconnection.getInstance().getCnx();
        refresh();
        
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        contenuCol.setCellValueFactory(new PropertyValueFactory<>("contenu"));
        
        
        
        //add cell of button edit 
         Callback<TableColumn<Reclamation, String>, TableCell<Reclamation, String>> cellFoctory = (TableColumn<Reclamation, String> param) -> {
            // make cell containing buttons
            final TableCell<Reclamation, String> cell = new TableCell<Reclamation, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows
                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {

                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);

                        deleteIcon.setStyle(
                                " -fx-cursor: hand ;"
                                + "-glyph-size:28px;"
                                + "-fx-fill:#ff1744;"
                        );
                        editIcon.setStyle(
                                " -fx-cursor: hand ;"
                                + "-glyph-size:28px;"
                                + "-fx-fill:#00E676;"
                        );
                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            
                            try {
                                reclamation = reclamationTable.getSelectionModel().getSelectedItem();
                                query = "DELETE FROM `reclamation` WHERE id  ="+reclamation.getId();
   
                                preparedStatement = connection.prepareStatement(query);
                                preparedStatement.execute();
                                refresh();
                                
                            } catch (SQLException ex) {
                                Logger.getLogger(ReclamationController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                           

                          

                        });
                        editIcon.setOnMouseClicked((MouseEvent event) -> {
                            
                            reclamation = reclamationTable.getSelectionModel().getSelectedItem();
                            FXMLLoader loader = new FXMLLoader ();
                            loader.setLocation(getClass().getResource("/GUI/Addreclamation.fxml"));
                            try {
                                loader.load();
                            } catch (IOException ex) {
                                Logger.getLogger(ReclamationController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                            AddreclamationController addreclamationController = loader.getController();
                            addreclamationController.setUpdate(true);
                            addreclamationController.setTextField(reclamation.getId(),
                                    reclamation.getType(), reclamation.getContenu());
                            Parent parent = loader.getRoot();
                            Stage stage = new Stage();
                            stage.setScene(new Scene(parent));
                            stage.initStyle(StageStyle.UTILITY);
                            stage.show();
                            

                           

                        });

                        HBox managebtn = new HBox(editIcon, deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
                        HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));

                        setGraphic(managebtn);

                        setText(null);

                    }
                }

            };

            return cell;
        };
         editCol.setCellFactory(cellFoctory);
         reclamationTable.setItems(reclamationList);
         
         
 }

    
}
