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
import java.util.Set;

import static br.edu.femass.LojaDiscos.*;


public class CadVendaController implements Initializable {

    Disco discoSelecionado;
    Cliente clienteSelecionado;
    Venda venda;
    DetalheVenda detalheVendaSelecionado;

    //List Views
    @FXML
    private ListView<Disco> LstDiscos;

    @FXML
    private ListView<Cliente> LstClientes;

    @FXML
    private ListView<Venda> LstVendas;

    @FXML
    private ListView<DetalheVenda> LstDetalheVenda;

    //Botões
    @FXML
    private Button BtnCadastrarVenda;

    @FXML
    private Button BtnGravar;

    @FXML
    private Button BtnCancelar;

    @FXML
    private Button BtnRetornar;

    @FXML
    private Button BtnSelecionarCliente;

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
    private TextField TxtDataVenda;

    @FXML
    private TextField TxtCPF;

    @FXML
    private TextField TxtNomeCliente;

    @FXML
    private TextField TxtSobrenomeCliente;

    //Métodos Auxiliares
    private void atualizarLista(){
        Set<Disco> discos = null;
        Set<Pessoa> pessoas=null;
        Set<Venda> vendas = null;

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
        Set<Cliente> clientes = new HashSet<>();
        for (Pessoa pessoa : pessoas){
            if (pessoa instanceof Cliente) clientes.add((Cliente) pessoa);
        }

        ObservableList<Cliente> clientesOb = FXCollections.observableArrayList(clientes);
        LstClientes.setItems(clientesOb);

        try {
            vendas = vendaDao.listar();
        } catch (Exception e) {
            e.printStackTrace();
            vendas = new HashSet<>();
        }

        ObservableList<Venda> vendasOb = FXCollections.observableArrayList(vendas);
        LstVendas.setItems(vendasOb);

    }

    private void atualizaDetalhesVendas(){
        if(venda!=null){
            ObservableList<DetalheVenda> detalhevendasOb = FXCollections.observableArrayList(venda.getItens());
            LstDetalheVenda.setItems(detalhevendasOb);
        }else{
            ObservableList<DetalheVenda> detalhevendasOb = FXCollections.observableArrayList();
            LstDetalheVenda.setItems(detalhevendasOb);
        }
    }

    private void habilitarInterface(Boolean incluir){
        BtnAdicionarDisco.setDisable(!incluir);
        BtnSelecionarCliente.setDisable(!incluir);
        BtnGravar.setDisable(!incluir);
        BtnCancelar.setDisable(!incluir);
        BtnRemoverDisco.setDisable(!incluir);
        BtnAumentarQuantidade.setDisable(!incluir);
        BtnDiminuirQuantidade.setDisable(!incluir);
        BtnCadastrarVenda.setDisable(incluir);
        BtnRetornar.setDisable(incluir);
        LstDiscos.setDisable(!incluir);
        LstClientes.setDisable(!incluir);
        LstDetalheVenda.setDisable(!incluir);
        LstVendas.setDisable(incluir);
    }

    private void limparTela(){
        TxtNotaFiscal.setText("");
        TxtDataVenda.setText("");
        TxtCPF.setText("");
        TxtNomeCliente.setText("");
        TxtSobrenomeCliente.setText("");
    }

    private void selecionaDisco(){
        discoSelecionado = LstDiscos.getSelectionModel().getSelectedItem();
    }

    private void selecionaCliente(){
        clienteSelecionado = LstClientes.getSelectionModel().getSelectedItem();
    }

    private void selecionaDetalhe(){
        detalheVendaSelecionado = LstDetalheVenda.getSelectionModel().getSelectedItem();
    }

    private void selecionaVenda(){
        venda=LstVendas.getSelectionModel().getSelectedItem();
        if (venda==null) return;
        atualizaDetalhesVendas();
        SimpleDateFormat formatadorDeData = new SimpleDateFormat("dd/MM/yyyy");
        TxtNotaFiscal.setText(venda.getNf().toString());
        TxtDataVenda.setText(formatadorDeData.format(venda.getData()));
        TxtCPF.setText(venda.getCliente().getCpf());
        TxtNomeCliente.setText(venda.getCliente().getNome());
        TxtSobrenomeCliente.setText(venda.getCliente().getSobreNome());
    }

    //Actions
    @FXML
    private void BtnCadastrarVenda_Action(ActionEvent event) {
        venda = new Venda();
        atualizarLista();
        atualizaDetalhesVendas();
        habilitarInterface(true);
        limparTela();
    }

    @FXML
    private void BtnGravar_Action(ActionEvent event) {
        if (venda==null) return;
        try {
            for (DetalheVenda detalhe : venda.getItens()){
                Integer estoque = detalhe.getDisco().getEstoque();
                Integer quantidade = detalhe.getQuantidade();
                if (quantidade>estoque){
                    Alert alerta = new Alert(Alert.AlertType.ERROR);
                    DialogPane dp = alerta.getDialogPane();
                    dp.setStyle("-fx-font-family: 'serif'");
                    alerta.setHeaderText("Não foi possível efetivar a venda, a quantidade do disco "+detalhe.getDisco().getArtista()+" -> "+detalhe.getDisco().getTitulo()+
                                         " é maior que o disponível no estoque("+detalhe.getDisco().getEstoque()+").\n Corrija e tente novamente!");
                    alerta.show();
                    return;
                }
            }
            venda.setData(new Date(System.currentTimeMillis()));
            vendaDao.gravar(venda);
            atualizarLista();
            habilitarInterface(false);
            for (DetalheVenda detalhe : venda.getItens()){
                detalhe.getDisco().setEstoque(detalhe.getDisco().getEstoque()-detalhe.getQuantidade());
                discoDao.alterar(detalhe.getDisco());
            }
            limparTela();
            atualizaDetalhesVendas();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void BtnCancelar_Action(ActionEvent event) {
        venda=null;
        habilitarInterface(false);
        atualizarLista();
        limparTela();
        atualizaDetalhesVendas();
    }

    @FXML
    private void BtnRetornar_Action(ActionEvent event) {
        LojaDiscos.irTelaPrincipal();
    }

    @FXML
    private void BtnSelecionarCliente_Action(ActionEvent event){
        if (clienteSelecionado==null) return;
        TxtCPF.setText(clienteSelecionado.getCpf());
        TxtNomeCliente.setText(clienteSelecionado.getNome());
        TxtSobrenomeCliente.setText(clienteSelecionado.getSobreNome());
        venda.setCliente(clienteSelecionado);
    }

    @FXML
    private void BtnAdicionarDisco_Action(ActionEvent event){
        if (discoSelecionado==null) return;
        ObservableList<Disco> discos = LstDiscos.getItems();
        discos.remove(discoSelecionado);
        LstDiscos.setItems(discos);
        DetalheVenda detalheVenda = new DetalheVenda();
        detalheVenda.setQuantidade(1);
        detalheVenda.setVenda(venda);
        detalheVenda.setDisco(discoSelecionado);
        detalheVenda.setPrecoUnitario(discoSelecionado.getPrecoVenda());
        venda.getItens().add(detalheVenda);
        atualizaDetalhesVendas();
    }

    @FXML
    private void BtnRemoverDisco_Action(ActionEvent event) {
        if (detalheVendaSelecionado==null) return;
        ObservableList<Disco> discos = LstDiscos.getItems();
        discos.add(detalheVendaSelecionado.getDisco());
        LstDiscos.setItems(discos);
        venda.getItens().remove(detalheVendaSelecionado);
        atualizaDetalhesVendas();
    }

    @FXML
    private void BtnAumentarQuantidade_Action(ActionEvent event) {
        if (detalheVendaSelecionado==null) return;
        detalheVendaSelecionado.setQuantidade(detalheVendaSelecionado.getQuantidade()+1);
        atualizaDetalhesVendas();
    }

    @FXML
    private void BtnDiminuirQuantidade_Action(ActionEvent event) {
        if (detalheVendaSelecionado==null) return;
        detalheVendaSelecionado.setQuantidade(detalheVendaSelecionado.getQuantidade()-1);
        atualizaDetalhesVendas();
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
    private void LstClientes_KeyPressed(KeyEvent event){
        selecionaCliente();
    }

    @FXML
    private void LstClientes_MouseClicked(MouseEvent event){
        selecionaCliente();
    }

    @FXML
    private void LstDetalheVenda_KeyPressed(KeyEvent event){
        selecionaDetalhe();
    }

    @FXML
    private void LstDetalheVenda_MouseClicked(MouseEvent event){
        selecionaDetalhe();
    }

    @FXML
    private void LstVendas_KeyPressed(KeyEvent event){
        selecionaVenda();
    }

    @FXML
    private void LstVendas_MouseClicked(MouseEvent event){
        selecionaVenda();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        atualizarLista();
    }
}
