package br.edu.femass.gui;

import br.edu.femass.LojaDiscos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;


public class InicialController {

    //Botões
    @FXML
    private Button BtnCadPessoa;

    @FXML
    private Button BtnCadDisco;

    @FXML
    private Button BtnCadArt;

    @FXML
    private Button BtnCadGen;

    @FXML
    private Button BtnCadCompra;

    @FXML
    private Button BtnCadVenda;

    @FXML
    private Button BtnTelaEstoque;

    @FXML
    private Button BtnFechCaixa;


    //Actions
    @FXML
    private void BtnCadPessoa_Action(ActionEvent event) {
        LojaDiscos.irTelaCadastroPessoas();
    }

    @FXML
    private void BtnCadDisco_Action(ActionEvent event) {
        LojaDiscos.irTelaCadastroDisco();
    }

    @FXML
    private void BtnCadArt_Action(ActionEvent event) {
        LojaDiscos.irTelaCadastroArtista();
    }

    @FXML
    private void BtnCadGen_Action(ActionEvent event) {
        LojaDiscos.irTelaCadastroGenero();
    }

    @FXML
    private void BtnCadCompra_Action(ActionEvent event) {
        LojaDiscos.irTelaCadastroCompra();
    }

    @FXML
    private void BtnCadVenda_Action(ActionEvent event) {
        LojaDiscos.irTelaCadastroVenda();
    }

    @FXML
    private void BtnTelaEstoque_Action(ActionEvent event) {
        LojaDiscos.irTelaEstoque();
    }

    @FXML
    private void BtnFechCaixa_Action(ActionEvent event) {
        LojaDiscos.irTelaFechamentoDiario();
    }
}
