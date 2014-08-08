/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import modelo.Cidade;
import modelo.Motorista;
import modelo.Solicitacao;
import modelo.Veiculo;
import modelo.Viagem;
import negocio.ManterCidadeNegocio;
import negocio.ManterMotoristaNegocio;
import negocio.ManterSolicitacaoNegocio;
import negocio.ManterVeiculoNegocio;

/**
 *
 * @author Alisson
 */
public class ManterSolicitacaoMB {
    private Solicitacao solicitacao;
    private Solicitacao selecionada;
    private Viagem viagem;
    private Motorista motorista;
    private Veiculo veiculo;
    private Cidade origem;
    private Cidade destino;
    private String justificativa;
    private String status;
    
    
    private ManterSolicitacaoNegocio msn;
    private ManterMotoristaNegocio mmn;
    private ManterVeiculoNegocio mvn;
    private ManterCidadeNegocio mcn;
    
    public ManterSolicitacaoMB(){
        
    }
    
    public void inserir(){        
        if(solicitacao.getKey()!=null){
            //veiculo.setPlaca(veiculo.getPlaca().toUpperCase());
            msn.editar(solicitacao);
            this.limpar();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Solicitação alterada com sucesso!",null);  
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }else{
            //veiculo.setPlaca(veiculo.getPlaca().toUpperCase());
            msn.inserir(solicitacao);
            this.limpar();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Solicitação cadastrada com sucesso!",null);  
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    public void remover(){
        if(selecionada.getKey()!=null){
            msn.remover(selecionada.getKey());
            this.limpar();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Solicitação excluída com sucesso!",null);  
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }else{
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nenhuma solicitação selecionada!",null);  
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    
    public List<Solicitacao> getSolicitacoes(){
        return msn.getSolicitacoes();
    }
    
    public List<Motorista> getMotoristas() {
        return mmn.getMotoristas();
    }
    
    public List<Veiculo> getVeiculos() {
        return mvn.getVeiculos();
    }
    
    public List<Cidade> getCidades() {
        return mcn.getCidades();
    }
    
    public int contador(){
        return msn.contador();
    }
    public void limpar(){
        solicitacao = new Solicitacao();        
        selecionada = new Solicitacao();
        //carregarValoresPadrao();
    }
    public void carregarParaEditar(){
        solicitacao = selecionada;
    }
    /*private void carregarValoresPadrao(){
        veiculo.setAtivo(true);
        veiculo.setCapacidade(4);
    }*/
    
    public Solicitacao getSelecionada(){
        return selecionada;
    }
    
    public Motorista getMotorista(){
        return motorista;
    }
    
    public Veiculo getVeiculo(){
        return veiculo;
    }
    
    public Cidade getOrigem(){
        return origem;
    }
    
    public Cidade getDestino(){
        return destino;
    }
    
    public String getJustificativa() {
        return justificativa;
    }
    
    public String getStatus() {
        return status;
    }
}
