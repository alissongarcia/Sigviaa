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
import modelo.Cidade;
import modelo.Estado;
import negocio.ManterCidadeNegocio;

/**
 *
 * @author Alisson
 */
@ManagedBean
@RequestScoped
public class ManterCidadeMB {
    
    private Cidade cidade = new Cidade();
    private Cidade selecionada = new Cidade();
    private Estado estado = new Estado();   
    private ManterCidadeNegocio mcn;
    private Converter converter;
    
    
    public ManterCidadeMB (){
        mcn = new ManterCidadeNegocio();        
        converter = new Converter() {
            
            @Override
            public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
                if(string==null||string.equals("")){
                   return null;
               }else{                    
                   return KeyFactory.stringToKey(string);
               }
            }

            @Override
            public String getAsString(FacesContext fc, UIComponent uic, Object o) {
                if(o==null){
                    return null;
                } 
                return KeyFactory.keyToString((Key)o);               
            }          
        };
        
    }
    
    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public Cidade getSelecionada() {
        return selecionada;
    }

    public void setSelecionada(Cidade selecionada) {
        this.selecionada = selecionada;
    }
    
    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public ManterCidadeNegocio getMcn() {
        return mcn;
    }

    public void setMcn(ManterCidadeNegocio mcn) {
        this.mcn = mcn;
    }

    public Converter getConverter() {
        return converter;
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }   
    public void inserir(){
        if(cidade.getKey()!=null){
            cidade.setEstado(mcn.getEstado(estado.getKey()));
            mcn.editar(cidade);
            this.limpar();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cidade alterada com sucesso!",null);  
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }else{           
            cidade.setEstado(mcn.getEstado(estado.getKey()));
            mcn.inserir(cidade);
            this.limpar();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cidade cadastrada com sucesso!",null);  
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    
    public void remover(){
        if(selecionada.getKey()!=null){
            mcn.remover(selecionada.getKey());
            this.limpar();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cidade excluída com sucesso!",null);  
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }else{
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nenhuma cidade selecionada!",null);  
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    public void limpar(){
        cidade = new Cidade();
        //cidade.setAtivo(true);
        selecionada = new Cidade();
        estado = new Estado();
    }
    public void carregarParaEditar(){
        cidade = selecionada;
        estado = selecionada.getEstado();
    }
    
    public List<Cidade> getCidades(){
        return mcn.getCidades();
    }
    public List<Estado> getEstados(){
        return mcn.getEstados();
    }
}
