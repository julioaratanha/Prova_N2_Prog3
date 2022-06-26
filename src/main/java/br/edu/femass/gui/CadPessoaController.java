package br.edu.femass.gui;

import br.edu.femass.LojaDiscos;
import br.edu.femass.model.Cliente;
import br.edu.femass.model.Fornecedor;
import br.edu.femass.model.Pessoa;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import static br.edu.femass.LojaDiscos.pessoaDao;


public class CadPessoaController implements Initializable {

    private Pessoa pessoaSelecionada;

    //List Views
    @FXML
    private ListView<Pessoa> LstClientes;

    @FXML
    private ListView<Pessoa> LstFornecedores;


    //Labels
    @FXML
    private Label LblSubClasse;

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

    @FXML
    private Button BtnConfirmarAlteracao;

    //Text Fields
    @FXML
    private TextField TxtSubClasse;

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

    //Radio Buttons
    @FXML
    private RadioButton Radio_Cliente;

    @FXML
    private RadioButton Radio_Fornecedor;

    //Métodos Auxiliares
    private void limparTela(){
        TxtSubClasse.setText("");
        TxtNome.setText("");
        TxtSobreNome.setText("");
        TxtLogradouro.setText("");
        TxtNumEnd.setText("");
        TxtComplemento.setText("");
        TxtBairro.setText("");
        TxtCidade.setText("");
        TxtUf.setText("");
        TxtDDD.setText("");
        TxtNumTel.setText("");
    }

    private void habilitarInterface(Boolean incluir){
        TxtSubClasse.setDisable(!incluir);
        TxtSubClasse.setEditable(incluir);

        TxtNome.setDisable(!incluir);
        TxtNome.setEditable(incluir);

        TxtSobreNome.setDisable(!incluir);
        TxtSobreNome.setEditable(incluir);

        TxtLogradouro.setDisable(!incluir);
        TxtLogradouro.setEditable(incluir);

        TxtNumEnd.setDisable(!incluir);
        TxtNumEnd.setEditable(incluir);

        TxtComplemento.setDisable(!incluir);
        TxtComplemento.setEditable(incluir);

        TxtBairro.setDisable(!incluir);
        TxtBairro.setEditable(incluir);

        TxtCidade.setDisable(!incluir);
        TxtCidade.setEditable(incluir);

        TxtUf.setDisable(!incluir);
        TxtUf.setEditable(incluir);

        TxtDDD.setDisable(!incluir);
        TxtDDD.setEditable(incluir);

        TxtNumTel.setDisable(!incluir);
        TxtNumTel.setEditable(incluir);

        BtnGravar.setDisable(!incluir);
        BtnCancelar.setDisable(!incluir);
        BtnIncluir.setDisable(incluir);
        BtnExcluir.setDisable(incluir);
        BtnRetornar.setDisable(incluir);
        LstClientes.setDisable(incluir);
        LstFornecedores.setDisable(incluir);

        Radio_Cliente.setDisable(!incluir);
        Radio_Fornecedor.setDisable(!incluir);
    }

    private void atualizarLista() {
        Set<Pessoa> pessoas = null;
        try {
            pessoas = pessoaDao.listar();
        } catch (Exception e){
            e.printStackTrace();
            pessoas = new HashSet<>();
        }
        Set<Pessoa> clientes = new HashSet<>();
        Set<Pessoa> fornecedores = new HashSet<>();
        for (Pessoa pessoa : pessoas){
           if (pessoa instanceof Cliente) clientes.add(pessoa);
           else fornecedores.add(pessoa);
        }
        ObservableList<Pessoa> clientesOb = FXCollections.observableArrayList(clientes);
        LstClientes.setItems(clientesOb);

        ObservableList<Pessoa> fornecedoresOb = FXCollections.observableArrayList(fornecedores);
        LstFornecedores.setItems(fornecedoresOb);
    }

    private void atualizarSubClasse(){
        if (Radio_Cliente.isSelected()) {
            LblSubClasse.setText("CPF");
        } else {
            LblSubClasse.setText("CNPJ");
        }
    }

    private void incluir_ou_alterar_pessoa(Pessoa pessoa, String tipo){
        if (pessoa instanceof Cliente) ((Cliente) pessoa).setCpf(TxtSubClasse.getText());
        else ((Fornecedor) pessoa).setCnpj(TxtSubClasse.getText());
        pessoa.setNome(TxtNome.getText());
        pessoa.setSobreNome(TxtSobreNome.getText());
        pessoa.setLogradouroEnd(TxtLogradouro.getText());
        pessoa.setNumeroEnd(Integer.parseInt(TxtNumEnd.getText()));
        pessoa.setComplementoEnd(TxtComplemento.getText());
        pessoa.setBairro(TxtBairro.getText());
        pessoa.setCidade(TxtCidade.getText());
        pessoa.setUf(TxtUf.getText());
        pessoa.setDdd(Integer.parseInt(TxtDDD.getText()));
        pessoa.setNumeroTel(Long.parseLong(TxtNumTel.getText()));
        try {
            if (tipo=="inserir") pessoaDao.gravar(pessoa);
            else if (tipo=="alterar") pessoaDao.alterar(pessoa);
        } catch (Exception e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText(e.getMessage());
            errorAlert.show();
        }
    }

    private void exibirPessoa(String tipo){
        if (tipo=="Cliente"){
            pessoaSelecionada = LstClientes.getSelectionModel().getSelectedItem();
            if (pessoaSelecionada==null) return;
            TxtSubClasse.setText(((Cliente) pessoaSelecionada).getCpf());
            Radio_Cliente.setSelected(true);
        }else if (tipo=="Fornecedor"){
            pessoaSelecionada = LstFornecedores.getSelectionModel().getSelectedItem();
            if (pessoaSelecionada==null) return;
            TxtSubClasse.setText(((Fornecedor) pessoaSelecionada).getCnpj());
            Radio_Fornecedor.setSelected(true);
        }
        atualizarSubClasse();
        TxtNome.setText(pessoaSelecionada.getNome());
        TxtSobreNome.setText(pessoaSelecionada.getSobreNome());
        TxtLogradouro.setText(pessoaSelecionada.getLogradouroEnd());
        TxtNumEnd.setText(pessoaSelecionada.getNumeroEnd().toString());
        TxtComplemento.setText(pessoaSelecionada.getComplementoEnd());
        TxtBairro.setText(pessoaSelecionada.getBairro());
        TxtCidade.setText(pessoaSelecionada.getCidade());
        TxtUf.setText(pessoaSelecionada.getUf());
        TxtDDD.setText(pessoaSelecionada.getDdd().toString());
        TxtNumTel.setText(pessoaSelecionada.getNumeroTel().toString());
        ativaBtnAlterar(true);
    }

    private void ativaBtnAlterar(boolean ativa){
            BtnAlterar.setVisible(ativa);
            BtnAlterar.setDisable(!ativa);
    }

    private void ativaBtnConfirmarAlteracao(boolean ativa){
        BtnConfirmarAlteracao.setVisible(ativa);
        BtnConfirmarAlteracao.setDisable(!ativa);
        BtnGravar.setVisible(!ativa);
        BtnGravar.setDisable(ativa);
    }

    //Actions
    @FXML
    private void BtnIncluir_Action(ActionEvent event) {
        atualizarLista();
        habilitarInterface(true);
        limparTela();
        TxtSubClasse.requestFocus();
        ativaBtnAlterar(false);
    }

    @FXML
    private void BtnAlterar_Action(ActionEvent event) {
        atualizarLista();
        habilitarInterface(true);
        TxtSubClasse.requestFocus();
        ativaBtnAlterar(false);
        ativaBtnConfirmarAlteracao(true);
    }

    @FXML
    private void BtnConfirmarAlteracao_Action(ActionEvent event) {
        incluir_ou_alterar_pessoa(pessoaSelecionada, "alterar");
        atualizarLista();
        ativaBtnConfirmarAlteracao(false);
        habilitarInterface(false);

    }

    @FXML
    private void BtnExcluir_Action(ActionEvent event) {
        pessoaSelecionada = LstClientes.getSelectionModel().getSelectedItem();
        if (pessoaSelecionada==null) pessoaSelecionada = LstFornecedores.getSelectionModel().getSelectedItem();
        if (pessoaSelecionada==null) return;
        try {
            pessoaDao.excluir(pessoaSelecionada);
        } catch (Exception e) {
            e.printStackTrace();
        }
        atualizarLista();
        limparTela();
        ativaBtnAlterar(false);
    }

    @FXML
    private void BtnGravar_Action(ActionEvent event) {
        if (Radio_Cliente.isSelected()) {
            Cliente cliente = new Cliente();
            incluir_ou_alterar_pessoa(cliente, "inserir");
        } else {
            Fornecedor fornecedor = new Fornecedor();
            incluir_ou_alterar_pessoa(fornecedor, "inserir");
        }
        atualizarLista();
        habilitarInterface(false);
    }

    @FXML
    private void BtnCancelar_Action(ActionEvent event) {
        ativaBtnConfirmarAlteracao(false);
        habilitarInterface(false);
        limparTela();
    }

    @FXML
    private void BtnRetornar_Action(ActionEvent event) {
        LojaDiscos.irTelaPrincipal();
    }


    @FXML
    public void Radio_Cliente_KeyPressed(KeyEvent event) {
        atualizarSubClasse();
    }

    @FXML
    private void Radio_Cliente_MouseClicked(MouseEvent event) {
        atualizarSubClasse();
    }

    @FXML
    public void Radio_Fornecedor_KeyPressed(KeyEvent event) {
        atualizarSubClasse();
    }

    @FXML
    private void Radio_Fornecedor_MouseClicked(MouseEvent event) {
        atualizarSubClasse();
    }

    @FXML
    private void LstClientes_KeyPressed(KeyEvent event){
        exibirPessoa("Cliente");
    }

    @FXML
    private void LstClientes_MouseClicked(MouseEvent event){
        exibirPessoa("Cliente");
    }

    @FXML
    private void LstFornecedores_KeyPressed(KeyEvent event){
        exibirPessoa("Fornecedor");
    }

    @FXML
    private void LstFornecedores_MouseClicked(MouseEvent event){
        exibirPessoa("Fornecedor");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        atualizarLista();
    }


}
