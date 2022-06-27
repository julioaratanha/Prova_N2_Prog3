package br.edu.femass.gui;

import br.edu.femass.LojaDiscos;
import br.edu.femass.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Set;

import javax.swing.*;

import static br.edu.femass.LojaDiscos.*;


public class CadCompraController implements Initializable {

    Disco discoSelecionado;
    Fornecedor fornecedorSelecionado;
    Compra compra;
    DetalheCompra detalheCompraSelecionado;

    //List Views
    @FXML
    private ListView<Disco> LstDiscos;

    @FXML
    private ListView<Fornecedor> LstFornecedores;

    @FXML
    private ListView<Compra> LstCompras;

    @FXML
    private ListView<DetalheCompra> LstDetalheCompra;

    //Botões
    @FXML
    private Button BtnCadastrarCompra;

    @FXML
    private Button BtnGravar;

    @FXML
    private Button BtnCancelar;

    @FXML
    private Button BtnRetornar;

    @FXML
    private Button BtnSelecionarFornecedor;

    @FXML
    private Button BtnAdicionarDisco;

    @FXML
    private Button BtnRemoverDisco;

    @FXML
    private Button BtnAumentarQuantidade;

    @FXML
    private Button BtnDiminuirQuantidade;


    //Text Fields
    @FXML
    private TextField TxtNotaFiscal;

    @FXML
    private TextField TxtDataCompra;

    @FXML
    private TextField TxtCNPJ;

    @FXML
    private TextField TxtNomeFornecedor;

    @FXML
    private TextField TxtSobrenomeFornecedor;

    //Métodos Auxiliares
    private void atualizarLista(){
        Set<Disco> discos = null;
        Set<Pessoa> pessoas=null;
        Set<Compra> compras = null;

        try {
            discos = discoDao.listar();
        } catch (Exception e) {
            e.printStackTrace();
            discos = new HashSet<>();
        }

        ObservableList<Disco> discosOb = FXCollections.observableArrayList(discos);
        LstDiscos.setItems(discosOb);

        try {
            pessoas = pessoaDao.listar();
        } catch (Exception e){
            e.printStackTrace();
            pessoas = new HashSet<>();
        }
        Set<Fornecedor> fornecedores = new HashSet<>();
        for (Pessoa pessoa : pessoas){
            if (pessoa instanceof Fornecedor) fornecedores.add((Fornecedor) pessoa);
        }

        ObservableList<Fornecedor> fornecedoresOb = FXCollections.observableArrayList(fornecedores);
        LstFornecedores.setItems(fornecedoresOb);

        try {
            compras = compraDao.listar();
        } catch (Exception e) {
            e.printStackTrace();
            compras = new HashSet<>();
        }

        ObservableList<Compra> comprasOb = FXCollections.observableArrayList(compras);
        LstCompras.setItems(comprasOb);

    }

    private void atualizaDetalhesCompras(){
        if(compra!=null){
            ObservableList<DetalheCompra> detalhecomprasOb = FXCollections.observableArrayList(compra.getItens());
            LstDetalheCompra.setItems(detalhecomprasOb);
        }else{
            ObservableList<DetalheCompra> detalhecomprasOb = FXCollections.observableArrayList();
            LstDetalheCompra.setItems(detalhecomprasOb);
        }
    }

    private void habilitarInterface(Boolean incluir){
        BtnAdicionarDisco.setDisable(!incluir);
        BtnSelecionarFornecedor.setDisable(!incluir);
        BtnGravar.setDisable(!incluir);
        BtnCancelar.setDisable(!incluir);
        BtnRemoverDisco.setDisable(!incluir);
        BtnAumentarQuantidade.setDisable(!incluir);
        BtnDiminuirQuantidade.setDisable(!incluir);
        BtnCadastrarCompra.setDisable(incluir);
        BtnRetornar.setDisable(incluir);
        LstDiscos.setDisable(!incluir);
        LstFornecedores.setDisable(!incluir);
        LstDetalheCompra.setDisable(!incluir);
        LstCompras.setDisable(incluir);
    }

    private void limparTela(){
        TxtNotaFiscal.setText("");
        TxtDataCompra.setText("");
        TxtCNPJ.setText("");
        TxtNomeFornecedor.setText("");
        TxtSobrenomeFornecedor.setText("");
    }

    private void selecionaDisco(){
        discoSelecionado = LstDiscos.getSelectionModel().getSelectedItem();
    }

    private void selecionaFornecedor(){
        fornecedorSelecionado = LstFornecedores.getSelectionModel().getSelectedItem();
    }

    private void selecionaDetalhe(){
        detalheCompraSelecionado = LstDetalheCompra.getSelectionModel().getSelectedItem();
    }

    private void selecionaCompra(){
        compra=LstCompras.getSelectionModel().getSelectedItem();
        if (compra==null) return;
        atualizaDetalhesCompras();
        SimpleDateFormat formatadorDeData = new SimpleDateFormat("dd/MM/yyyy");
        TxtNotaFiscal.setText(compra.getNf().toString());
        TxtDataCompra.setText(formatadorDeData.format(compra.getData()));
        TxtCNPJ.setText(compra.getFornecedor().getCnpj());
        TxtNomeFornecedor.setText(compra.getFornecedor().getNome());
        TxtSobrenomeFornecedor.setText(compra.getFornecedor().getSobreNome());
    }

    //Actions
    @FXML
    private void BtnCadastrarCompra_Action(ActionEvent event) {
        compra = new Compra();
        atualizarLista();
        atualizaDetalhesCompras();
        habilitarInterface(true);
        limparTela();
    }

    @FXML
    private void BtnGravar_Action(ActionEvent event) {
        if (compra==null) return;
        if (compra.getFornecedor()==null){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            DialogPane dp = errorAlert.getDialogPane();
            dp.setStyle("-fx-font-family: 'serif'");
            errorAlert.setContentText("Selecione um Fornecedor!!");
            errorAlert.show();
            return;
        }
        if (compra.getItens().isEmpty()){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            DialogPane dp = errorAlert.getDialogPane();
            dp.setStyle("-fx-font-family: 'serif'");
            errorAlert.setContentText("Selecione um Disco!!");
            errorAlert.show();
            return;
        }
        try {
            compra.setData(new Date(System.currentTimeMillis()));
            compraDao.gravar(compra);
            atualizarLista();
            habilitarInterface(false);
            for (DetalheCompra detalhe : compra.getItens()){
                detalhe.getDisco().setEstoque(detalhe.getDisco().getEstoque()+detalhe.getQuantidade());
                discoDao.alterar(detalhe.getDisco());
            }
            limparTela();
            atualizaDetalhesCompras();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void BtnCancelar_Action(ActionEvent event) {
        compra=null;
        habilitarInterface(false);
        atualizarLista();
        limparTela();
        atualizaDetalhesCompras();
    }

    @FXML
    private void BtnRetornar_Action(ActionEvent event) {
        LojaDiscos.irTelaPrincipal();
    }

    @FXML
    private void BtnSelecionarFornecedor_Action(ActionEvent event){
        if (fornecedorSelecionado==null) return;
        TxtCNPJ.setText(fornecedorSelecionado.getCnpj());
        TxtNomeFornecedor.setText(fornecedorSelecionado.getNome());
        TxtSobrenomeFornecedor.setText(fornecedorSelecionado.getSobreNome());
        compra.setFornecedor(fornecedorSelecionado);
    }

    @FXML
    private void BtnAdicionarDisco_Action(ActionEvent event){
        if (discoSelecionado==null) return;
        ObservableList<Disco> discos = LstDiscos.getItems();
        discos.remove(discoSelecionado);
        LstDiscos.setItems(discos);
        DetalheCompra detalheCompra = new DetalheCompra();
        detalheCompra.setQuantidade(1);
        detalheCompra.setCompra(compra);
        detalheCompra.setDisco(discoSelecionado);
        TextInputDialog t = new TextInputDialog();
        DialogPane dp = t.getDialogPane();
        dp.setStyle("-fx-font-family: 'serif'");
        t.setHeaderText("Digite o valor do preço unitário desse disco:");
        t.showAndWait();
        String resultado = t.getEditor().getText();
        Double preco = Double.parseDouble(resultado);
        detalheCompra.setPrecoUnitario(preco);
        compra.getItens().add(detalheCompra);
        atualizaDetalhesCompras();
    }

    @FXML
    private void BtnRemoverDisco_Action(ActionEvent event) {
        if (detalheCompraSelecionado==null) return;
        ObservableList<Disco> discos = LstDiscos.getItems();
        discos.add(detalheCompraSelecionado.getDisco());
        LstDiscos.setItems(discos);
        compra.getItens().remove(detalheCompraSelecionado);
        atualizaDetalhesCompras();
    }

    @FXML
    private void BtnAumentarQuantidade_Action(ActionEvent event) {
        if (detalheCompraSelecionado==null) return;
        detalheCompraSelecionado.setQuantidade(detalheCompraSelecionado.getQuantidade()+1);
        atualizaDetalhesCompras();
    }

    @FXML
    private void BtnDiminuirQuantidade_Action(ActionEvent event) {
        if (detalheCompraSelecionado==null) return;
        detalheCompraSelecionado.setQuantidade(detalheCompraSelecionado.getQuantidade()-1);
        atualizaDetalhesCompras();
    }

    @FXML
    private void LstDiscos_KeyPressed(KeyEvent event){
        selecionaDisco();
    }

    @FXML
    private void LstDiscos_MouseClicked(MouseEvent event){
        selecionaDisco();
    }

    @FXML
    private void LstFornecedores_KeyPressed(KeyEvent event){
        selecionaFornecedor();
    }

    @FXML
    private void LstFornecedores_MouseClicked(MouseEvent event){
        selecionaFornecedor();
    }

    @FXML
    private void LstDetalheCompra_KeyPressed(KeyEvent event){
        selecionaDetalhe();
    }

    @FXML
    private void LstDetalheCompra_MouseClicked(MouseEvent event){
        selecionaDetalhe();
    }

    @FXML
    private void LstCompras_KeyPressed(KeyEvent event){
        selecionaCompra();
    }

    @FXML
    private void LstCompras_MouseClicked(MouseEvent event){
        selecionaCompra();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        atualizarLista();
    }
}
