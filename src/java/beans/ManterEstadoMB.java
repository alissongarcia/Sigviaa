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
import modelo.Estado;
import negocio.ManterEstadoNegocio;

/**
 *
 * @author Alisson
 */
@ManagedBean
@RequestScoped
public class ManterEstadoMB {
    
    private Estado estado = new Estado();
    private Estado selecionado = new Estado();            
    private ManterEstadoNegocio men;    
    private Converter converter;

    public ManterEstadoMB() {
        men = new ManterEstadoNegocio();
        //carregarValoresPadrao();
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
    public Estado getSelecionado() {
        return selecionado;
    }

    public void setSelecionado(Estado selecionado) {
        this.selecionado = selecionado;
    }
    public Estado getEstado() {
        return estado;
    }
    public void setEstado(Estado estado) {
        this.estado = estado;
    }
    public ManterEstadoNegocio getMen() {
        return men;
    }
    public void setMen(ManterEstadoNegocio men) {
        this.men = men;
    }    
    public void inserir(){
        
        if(estado.getKey()!=null){
            men.editar(estado);
            this.limpar();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Estado alterado com sucesso!",null);  
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }else{
            men.inserir(estado);
            this.limpar();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Estado cadastrado com sucesso!",null);  
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    public void remover(){
        if(selecionado.getKey()!=null){
            men.remover(selecionado.getKey());
            this.limpar();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Estado excluído com sucesso!",null);  
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }else{
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nenhum estado selecionado",null);  
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    public List<Estado> getEstados(){
        return men.getEstados();
    }
    public int contador(){
        return men.contador();
    }
    public void limpar(){
        estado = new Estado();
        selecionado = new Estado();
        //carregarValoresPadrao();
    }
    public void carregarParaEditar(){
        estado = selecionado;
    }
    /*private void carregarValoresPadrao(){
        estado.setAtivo(true);
    }*/
    
}
