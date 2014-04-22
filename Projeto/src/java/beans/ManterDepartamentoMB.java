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
import modelo.Departamento;
import modelo.Usuario;
import negocio.ManterDepartamentoNegocio;

/**
 *
 * @author Fernando
 */
@ManagedBean
@RequestScoped
public class ManterDepartamentoMB {
    private Departamento departamento = new Departamento();
    private Departamento selecionado = new Departamento();
    private List<Usuario> usuarios;
    private ManterDepartamentoNegocio mdn;
    private Converter converter;
    public ManterDepartamentoMB(){
        mdn = new ManterDepartamentoNegocio();
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

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
    
    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Departamento getSelecionado() {
        return selecionado;
    }

    public void setSelecionado(Departamento selecionado) {
        this.selecionado = selecionado;
    }

    public ManterDepartamentoNegocio getMdn() {
        return mdn;
    }

    public void setMdn(ManterDepartamentoNegocio mdn) {
        this.mdn = mdn;
    }

    public Converter getConverter() {
        return converter;
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }
    public void inserir(){        
        if(departamento.getKey()!=null){           
            mdn.editar(departamento);
            this.limpar();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Departamento alterado com sucesso!",null);  
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }else{            
            mdn.inserir(departamento);
            this.limpar();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Departamento cadastrado com sucesso!",null);  
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    public void remover(){
        if(selecionado.getKey()!=null){
            mdn.remover(selecionado.getKey());
            this.limpar();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Departamento excluído com sucesso!",null);  
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }else{
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nenhum departamento selecionado",null);  
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    public List<Departamento> getDepartamentos(){
        return mdn.getDepartamentos();
    }
    
    public void limpar(){
        departamento = new Departamento();
        selecionado = new Departamento();        
    }
    public void carregarParaEditar(){
        departamento = selecionado;
    }
    
}
