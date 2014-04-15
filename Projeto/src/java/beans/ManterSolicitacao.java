/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import modelo.Solicitacao;
import modelo.Viagem;
import negocio.ManterSolicitacaoNegocio;

/**
 *
 * @author Alisson
 */
public class ManterSolicitacao {
    private Solicitacao solicitacao;
    private Solicitacao selecionada;
    private Viagem viagem;
    private ManterSolicitacaoNegocio msn;
    
    public ManterSolicitacao(){
        
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
        return msn.getVeiculos();
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
}
