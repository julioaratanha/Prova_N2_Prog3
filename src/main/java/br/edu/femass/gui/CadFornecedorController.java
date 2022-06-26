package br.edu.femass.gui;

import br.edu.femass.LojaDiscos;
import br.edu.femass.model.Cliente;
import br.edu.femass.model.Fornecedor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;


public class CadFornecedorController {

    //List Views
    @FXML
    private ListView<Fornecedor> LstFornecedores;

    //Botões
    @FXML
    private Button BtnIncluir;

    @FXML
    private Button BtnAlterar;

    @FXML
    private Button BtnExcluir;

    @FXML
    private Button BtnGravar;

    @FXML
    private Button BtnCancelar;

    @FXML
    private Button BtnRetornar;

    //Text Fields
    @FXML
    private TextField TxtCnpj;

    @FXML
    private TextField TxtNome;

    @FXML
    private TextField TxtSobreNome;

    @FXML
    private TextField TxtLogradouro;

    @FXML
    private TextField TxtNumEnd;

    @FXML
    private TextField TxtComplemento;

    @FXML
    private TextField TxtBairro;

    @FXML
    private TextField TxtCidade;

    @FXML
    private TextField TxtUf;

    @FXML
    private TextField TxtDDD;

    @FXML
    private TextField TxtNumTel;

    //Actions
    @FXML
    private void BtnIncluir_Action(ActionEvent event) {

    }

    @FXML
    private void BtnAlterar_Action(ActionEvent event) {

    }

    @FXML
    private void BtnExcluir_Action(ActionEvent event) {

    }

    @FXML
    private void BtnGravar_Action(ActionEvent event) {

    }

    @FXML
    private void BtnCancelar_Action(ActionEvent event) {

    }

    @FXML
    private void BtnRetornar_Action(ActionEvent event) {
        LojaDiscos.irTelaPrincipal();
    }

}
