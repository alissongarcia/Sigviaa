/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.event.ActionEvent;
import modelo.Departamento;
import modelo.Grupo;
import modelo.Usuario;
import negocio.ManterUsuarioNegocio;

/**
 *
 * @author Fernando
 */
@ManagedBean
@RequestScoped
public class ManterUsuarioMB {
    private Usuario usuario = new Usuario();
    private Usuario selecionado = new Usuario();
    private Departamento departamento = new Departamento();   
    private Grupo grupo = new Grupo();
    private ManterUsuarioNegocio mun;
    private Converter converter;
    
    private List<String> selectedMovies;  
    private Map<String,String> movies; 
    
    
    public ManterUsuarioMB (){
        mun = new ManterUsuarioNegocio();        
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
        
        movies = new HashMap<String, String>();  
        movies.put("Usuário", "Usuário");  
        movies.put("Administrador", "Administrador");  
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getSelecionado() {
        return selecionado;
    }

    public void setSelecionado(Usuario selecionado) {
        this.selecionado = selecionado;
    }
    
    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public ManterUsuarioNegocio getMun() {
        return mun;
    }

    public void setMun(ManterUsuarioNegocio mun) {
        this.mun = mun;
    }

    public Converter getConverter() {
        return converter;
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }   
    public void inserir(){
        if(usuario.getKey()!=null){
            usuario.setDepartamento(mun.getDepartamento(departamento.getKey()));
            usuario.setPermissao(mun.getGrupo(grupo.getKey()));
            mun.editar(usuario);
            this.limpar();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuário alterado com sucesso!",null);  
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }else{           
            usuario.setDepartamento(mun.getDepartamento(departamento.getKey()));
            usuario.setPermissao(mun.getGrupo(grupo.getKey()));
            mun.inserir(usuario);
            this.limpar();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuário cadastrado com sucesso!",null);  
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    public void remover(){
        if(selecionado.getKey()!=null){
            mun.remover(selecionado.getKey());
            this.limpar();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuário excluído com sucesso!",null);  
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }else{
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nenhum usuário selecionado!",null);  
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    public void limpar(){
        usuario = new Usuario();
        usuario.setAtivo(true);
        selecionado = new Usuario();
        departamento = new Departamento();
    }
    public void carregarParaEditar(){
        usuario = selecionado;
    }
    public void carregarValoresPadrao(){        
        usuario.setAtivo(true);
        
    }
    public List<Usuario> getUsuarios(){
        return mun.getUsuarios();
    }
    public List<Departamento> getDepartamentos(){
        return mun.getDepartamentos();
    }
    public List<Grupo> getGrupos(){
        return mun.getGrupos();
    }
  
    /* Testes para tonar funcional o campo perfil*/
    
    public List<String> getSelectedMovies() {  
        return selectedMovies;  
    }  
    public void setSelectedMovies(List<String> selectedMovies) {  
        this.selectedMovies = selectedMovies;  
    }  
  
    public Map<String, String> getMovies() {  
        return movies;  
    }  
}
