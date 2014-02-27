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
import modelo.Motorista;
import negocio.ManterMotoristaNegocio;

/**
 *
 * @author Fernando
 */
@ManagedBean
@RequestScoped
public class ManterMotoristaMB {
    
    private Motorista motorista = new Motorista();
    private Motorista selecionado = new Motorista();            
    private ManterMotoristaNegocio mmn;    
    private Converter converter;

    public ManterMotoristaMB() {
        mmn = new ManterMotoristaNegocio();
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
    
    public Converter getConverter() {
        return converter;
    }
    public void setConverter(Converter converter) {
        this.converter = converter;
    }
    public Motorista getSelecionado() {
        return selecionado;
    }

    public void setSelecionado(Motorista selecionado) {
        this.selecionado = selecionado;
    }
    public Motorista getMotorista() {
        return motorista;
    }
    public void setMotorista(Motorista motorista) {
        this.motorista = motorista;
    }
    public ManterMotoristaNegocio getMmn() {
        return mmn;
    }
    public void setMmn(ManterMotoristaNegocio mmn) {
        this.mmn = mmn;
    }    
    public void inserir(){        
        if(motorista.getKey()!=null){                        
            mmn.editar(motorista);
            this.limpar();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Motorista alterado com sucesso!",null);  
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }else{
            mmn.inserir(motorista);
            this.limpar();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Motorista cadastrado com sucesso!",null);  
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    public void remover(){
        if(selecionado.getKey()!=null){
            mmn.remover(selecionado.getKey());
            this.limpar();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Motorista excluído com sucesso!",null);  
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }else{
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nenhum motorista selecionado",null);  
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    public List<Motorista> getMotoristas(){
        return mmn.getMotoristas();
    }
    public int contador(){
        return mmn.contador();
    }
    public void limpar(){
        motorista = new Motorista();
        selecionado = new Motorista();
        carregarValoresPadrao();
    }
    public void carregarParaEditar(){
        motorista = selecionado;
    }
    private void carregarValoresPadrao(){
        motorista.setAtivo(true);
    }
    
}
