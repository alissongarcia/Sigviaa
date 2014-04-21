/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import modelo.Cidade;
import modelo.Motorista;
import modelo.Solicitacao;
import modelo.Veiculo;
import modelo.Viagem;
import negocio.ManterViagemNegocio;

/**
 *
 * @author Alisson
 */
@ManagedBean
@RequestScoped
public class ManterViagemMB {
    
    private Viagem viagem = new Viagem();
    private Viagem selecionada = new Viagem();
    private Motorista motorista = new Motorista();
    private Veiculo veiculo = new Veiculo();
    private Cidade destino = new Cidade();
    private Cidade origem = new Cidade();
    private String status;
    private String justificativa;
    private Date dataSaida;
    private Date dataRetorno;
    
    
    private ManterViagemNegocio mvn;
    private Converter converter;
    
    public ManterViagemMB(){
        mvn = new ManterViagemNegocio();
        carregarValoresPadrao();
        converter = new Converter() {

            @Override
            public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
               if(string==null||string.equals("")){
                   return null;
               }else{
                   Key key = KeyFactory.stringToKey(string);
                   return key;
               }               
            }           
            @Override
            public String getAsString(FacesContext fc, UIComponent uic, Object o) {               
                
                    return KeyFactory.keyToString((Key)o);
                
            }
        };
    }

    public Viagem getViagem() {
        return viagem;
    }

    public void setViagem(Viagem viagem) {
        this.viagem = viagem;
    }

    public Viagem getSelecionado() {
        return getSelecionada();
    }

    public void setSelecionado(Viagem selecionada) {
        this.setSelecionada(selecionada);
    }

    public ManterViagemNegocio getMvn() {
        return mvn;
    }

    public void setMvn(ManterViagemNegocio mvn) {
        this.mvn = mvn;
    }

    public Converter getConverter() {
        return converter;
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }
    
    public void inserir(){        
        if(viagem.getKey()!=null){
            mvn.editar(viagem);
            this.limpar();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Viagem alterada com sucesso!",null);  
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }else{
            
            viagem.setDataRetorno(dataRetorno);
            viagem.setDataSaida(dataSaida);
            viagem.setDestino(destino);
            viagem.setJustificativa(justificativa);
            viagem.setMotorista(motorista);
            viagem.setOrigem(origem);
            viagem.setStatus(status);
            viagem.setVeiculo(veiculo);
            viagem.setCodigo("definir");
            viagem.setSolicitacoes(new ArrayList<Solicitacao>());
            
            mvn.inserir(viagem);
            this.limpar();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Viagem cadastrado com sucesso!",null);  
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    public void remover(){
        if(getSelecionada().getKey()!=null){
            mvn.remover(getSelecionada().getKey());
            this.limpar();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Viagem excluído com sucesso!",null);  
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }else{
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nenhuma viagem selecionada!",null);  
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    public List<Viagem> getViagens(){
        return mvn.getViagens();
    }
    
    public List<Motorista> getMotoristas(){
        return mvn.getMotoristas();
    }
    
    public List<Veiculo> getVeiculos(){
        return mvn.getVeiculos();
    }
    
    public List<Cidade> getCidades(){
        return mvn.getCidades();
        
    }
    
    public int contador(){
        return mvn.contador();
    }
    public void limpar(){
        viagem = new Viagem();        
        setSelecionada(new Viagem());
        carregarValoresPadrao();
    }
    public void carregarParaEditar(){
        viagem = getSelecionada();
    }
    private void carregarValoresPadrao(){
        
        motorista = new Motorista();
        veiculo = new Veiculo();
        destino = new Cidade();
        origem = new Cidade();
        status = "";
        justificativa = "";
        dataSaida = null;
        dataRetorno = null;
    }

    /**
     * @return the selecionada
     */
    public Viagem getSelecionada() {
        return selecionada;
    }

    /**
     * @param selecionada the selecionada to set
     */
    public void setSelecionada(Viagem selecionada) {
        this.selecionada = selecionada;
    }

    /**
     * @return the motorista
     */
    public Motorista getMotorista() {
        return motorista;
    }

    /**
     * @param motorista the motorista to set
     */
    public void setMotorista(Motorista motorista) {
        this.motorista = motorista;
    }

    /**
     * @return the veiculo
     */
    public Veiculo getVeiculo() {
        return veiculo;
    }

    /**
     * @param veiculo the veiculo to set
     */
    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    /**
     * @return the destino
     */
    public Cidade getDestino() {
        return destino;
    }

    /**
     * @param destino the destino to set
     */
    public void setDestino(Cidade destino) {
        this.destino = destino;
    }

    /**
     * @return the origem
     */
    public Cidade getOrigem() {
        return origem;
    }

    /**
     * @param origem the origem to set
     */
    public void setOrigem(Cidade origem) {
        this.origem = origem;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the justificativa
     */
    public String getJustificativa() {
        return justificativa;
    }

    /**
     * @param justificativa the justificativa to set
     */
    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    /**
     * @return the dataSaida
     */
    public Date getDataSaida() {
        return dataSaida;
    }

    /**
     * @param dataSaida the dataSaida to set
     */
    public void setDataSaida(Date dataSaida) {
        this.dataSaida = dataSaida;
    }

    /**
     * @return the dataRetorno
     */
    public Date getDataRetorno() {
        return dataRetorno;
    }

    /**
     * @param dataRetorno the dataRetorno to set
     */
    public void setDataRetorno(Date dataRetorno) {
        this.dataRetorno = dataRetorno;
    }
    
    
    
}
