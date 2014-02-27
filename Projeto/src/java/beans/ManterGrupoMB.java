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
import modelo.Grupo;
import negocio.ManterGrupoNegocio;

/**
 *
 * @author Fernando
 */
@ManagedBean
@RequestScoped
public class ManterGrupoMB {
      private Grupo grupo = new Grupo();
      private Grupo selecionado = new Grupo();          
      private ManterGrupoNegocio mgn;
      private Converter converter;

    /**
     * Creates a new instance of ManterGrupoMB
     */
    public ManterGrupoMB() {
        mgn= new ManterGrupoNegocio();
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

    public Grupo getSelecionado() {
        return selecionado;
    }

    public void setSelecionado(Grupo selecionado) {
        this.selecionado = selecionado;
    }
    
    public ManterGrupoNegocio getMgn() {
        return mgn;
    }

    public void setMgn(ManterGrupoNegocio mgn) {
        this.mgn = mgn;
    }

    public Converter getConverter() {
        return converter;
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }
    
    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }
    public void inserir(){        
        if(grupo.getKey()!=null){           
            mgn.editar(grupo);
            this.limpar();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Grupo alterado com sucesso!",null);  
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }else{            
            mgn.inserir(grupo);
            this.limpar();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Grupo cadastrado com sucesso!",null);  
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    public void remover(){
        if(selecionado.getKey()!=null){
            mgn.remover(selecionado.getKey());
            this.limpar();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Grupo excluído com sucesso!",null);  
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }else{
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nenhum grupo selecionado",null);  
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    public List<Grupo> getGrupos(){
        return mgn.getGrupos();
    }
    
    public void limpar(){
        grupo = new Grupo();
        selecionado = new Grupo();        
    }
    public void carregarParaEditar(){
        grupo = selecionado;
    }
}
