/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import dbconnection.Dbconnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import models.Activite;
import models.Reclamation;

/**
 * FXML Controller class
 *
 * @author karbo
 */
public class AddreclamationController implements Initializable {

    @FXML
    private TextField typeFid;
    @FXML
    private TextField contenuTid;
    
    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    Reclamation reclamation = null ;
    private boolean update ;
    int reclamationId;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void save(MouseEvent event) {
        
        connection = Dbconnection.getInstance().getCnx();
        String type = typeFid.getText();
        String contenu = contenuTid.getText();

        if (type.isEmpty() || contenu.isEmpty() ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Remplir tous les champs !");
            alert.showAndWait();

        } else {
            getQuery();
            insert();
            clean();

        }
        
    }

    @FXML
    private void clean() {
        
        typeFid.setText(null);
        contenuTid.setText(null);
        
    }
    
    void setUpdate(boolean b) {
        this.update = b;
        
    }

    private void getQuery() {
        if (update == false) {
            
            query = "INSERT INTO `reclamation`(`type`, `contenu`) VALUES (?,?)";

        }else{
            query = "UPDATE `reclamation` SET "
                    + "`type`=?,"
                    + "`contenu`= ? WHERE id = '"+reclamationId+"'";
        }
        
    }

    private void insert() {
        try {

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, typeFid.getText());
            preparedStatement.setString(2, contenuTid.getText());
            preparedStatement.execute();

        } catch (SQLException ex) {
            Logger.getLogger(AddreclamationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void setTextField(int id, String type, String contenu) {
        reclamationId = id;
        typeFid.setText(type);
        contenuTid.setText(contenu);
    }
    
}
