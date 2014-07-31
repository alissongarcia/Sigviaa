/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;


import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import dao.DepartamentoJpaController;
import dao.GrupoJpaController;
import dao.UsuarioJpaController;
import dao.exceptions.RollbackFailureException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.Departamento;
import modelo.Grupo;
import modelo.Solicitacao;
import modelo.Usuario;
import negocio.CadastrarUsuarioNegocio;
import util.CriarUsuario;
import util.EMF;
import util.SendMail;

/**
 *
 * @author Fernando
 */
@ManagedBean
@RequestScoped
public class CadastrarUsuarioMB {

    /**
     * Creates a new instance of CadastrarUsuarioMB
     */
    private Usuario usuario = new Usuario();
    private Departamento departamento = new Departamento();    
    private CadastrarUsuarioNegocio cun;
    private Converter converter;
    private boolean loginDisponivel;
    
    List<Usuario> usuarios;
    
    public CadastrarUsuarioMB() throws RollbackFailureException, Exception {
        
        CriarUsuario newUsuario = new CriarUsuario();
                
        cun = new CadastrarUsuarioNegocio();
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
    
    public void metodoTeste(){
        //Para
    }

    public boolean isLoginDisponivel() {
        return loginDisponivel;
    }

    public void setLoginDisponivel(boolean loginDisponivel) {
        this.loginDisponivel = loginDisponivel;
    }
    
    public Converter getConverter() {
        return converter;
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }   
    
    public CadastrarUsuarioNegocio getCun() {
        return cun;
    }

    public void setCun(CadastrarUsuarioNegocio cun) {
        this.cun = cun;
    }   
    
    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    public void inserir() {        
            usuario.setDepartamento(cun.getDepartamento(departamento.getKey()));
            cun.inserir(this.usuario);
            this.sendMail(usuario.getEmail());
            this.sendMailAdministrador();
            this.limpar();              
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            request.getSession().setAttribute("msg", "Cadastro efetuado com sucesso! Você receberá um email quando sua conta for ativada pelo administrador.");
            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            response.sendRedirect("/faces/login.xhtml"); 
        } catch (IOException ex) {
            Logger.getLogger(CadastrarUsuarioMB.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
    }
    
    public List<Departamento> getDepartamentos(){
        return cun.getDepartamentos();
    }
    public void limpar(){
        usuario = new Usuario();
    }
    public void verificarDisponibilidade(){
        if(!usuario.getUsername().trim().equals("")||usuario.getUsername()!=null){
            boolean disponivel = cun.verificarDisponibilidade(usuario.getUsername());
            if(disponivel){
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Login disponível",null);  
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }else{
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login indisponível",null);  
                FacesContext.getCurrentInstance().addMessage(null, msg);
                usuario.setUsername(null);
            }
        }
    }

    public void sendMail(String email) {
        SendMail sm = new SendMail();
        String msg = "Sr(a) "+ usuario.getNome()+",\n\n seu cadastro foi realizado com sucesso."+
                " Seus dados serão analisados e sua conta será ativada em breve. Aguarde."+
                "\n\n Atenciosamente,\n\n A Direção";
        sm.sendMail("sistemas@ceresufrn.org", email, "SIGVIAA - Cadastro realizado com sucesso", msg);        
    }
    
    public void sendMailAdministrador() {
        SendMail sm = new SendMail();
        String msg = "Sr(a) administrador,\n\n um cadastro foi realizado."+
                " O cadastro do usuário "+usuario.getNome()+" foi realizado com sucesso, e necessita da sua aprovação."+
                "\n\n Atenciosamente,\n\n SIGVIAA";        
        
        for(Usuario administrador : cun.getAdministrador()){
            if(administrador.getPermissao().getAuthority().equals("ROLE_ADMIN")){
                sm.sendMail("sistemas@ceresufrn.org", administrador.getEmail(), "SIGVIAA - Novo cadastro realizado", msg);

            }
        }
        
    }
}
