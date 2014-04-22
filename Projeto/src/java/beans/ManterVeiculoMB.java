/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.event.ActionEvent;
import modelo.Veiculo;
import negocio.ManterVeiculoNegocio;

/**
 *
 * @author Fernando
 */
@ManagedBean
@RequestScoped
public class ManterVeiculoMB {
    private Veiculo veiculo = new Veiculo();
    private Veiculo selecionado = new Veiculo();
    private ManterVeiculoNegocio mvn;
    private Converter converter;
    
    public ManterVeiculoMB(){
        mvn = new ManterVeiculoNegocio();
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

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public Veiculo getSelecionado() {
        return selecionado;
    }

    public void setSelecionado(Veiculo selecionado) {
        this.selecionado = selecionado;
    }

    public ManterVeiculoNegocio getMvn() {
        return mvn;
    }

    public void setMvn(ManterVeiculoNegocio mvn) {
        this.mvn = mvn;
    }

    public Converter getConverter() {
        return converter;
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }
    
    public void inserir(){        
        if(veiculo.getKey()!=null){
            veiculo.setPlaca(veiculo.getPlaca().toUpperCase());
            mvn.editar(veiculo);
            this.limpar();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Veículo alterado com sucesso!",null);  
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }else{
            veiculo.setPlaca(veiculo.getPlaca().toUpperCase());
            mvn.inserir(veiculo);
            this.limpar();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Veículo cadastrado com sucesso!",null);  
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    public void remover(){
        if(selecionado.getKey()!=null){
            mvn.remover(selecionado.getKey());
            this.limpar();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Veículo excluído com sucesso!",null);  
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }else{
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nenhum veículo selecionado!",null);  
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    public List<Veiculo> getVeiculos(){
        return mvn.getVeiculos();
    }
    public int contador(){
        return mvn.contador();
    }
    public void limpar(){
        veiculo = new Veiculo();        
        selecionado = new Veiculo();
        carregarValoresPadrao();
    }
    public void carregarParaEditar(){
        veiculo = selecionado;
    }
    private void carregarValoresPadrao(){
        veiculo.setAtivo(true);
        veiculo.setCapacidade(4);
    }
}
