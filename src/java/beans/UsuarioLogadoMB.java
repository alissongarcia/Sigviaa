/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import modelo.Usuario;

/**
 *
 * @author Fernando
 */
@ManagedBean
@RequestScoped
public class UsuarioLogadoMB{
    private Usuario usuarioLogado;
 
    public Usuario getUsuario() {
        
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);        
        usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
                
        return usuarioLogado;
    }
 
    public void setUsuario(Usuario usuario) {
        this.usuarioLogado = usuario;
    }
    
}
