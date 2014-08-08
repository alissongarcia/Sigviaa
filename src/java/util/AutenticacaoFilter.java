/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import dao.UsuarioJpaController;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.Usuario;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 *
 * @author Fernando
 */
public class AutenticacaoFilter extends UsernamePasswordAuthenticationFilter {
 
    
    private UsuarioJpaController ujc;
    private String mensagem;
    private Usuario usuario;
    
    public AutenticacaoFilter(){
        ujc = new UsuarioJpaController(EMF.get());
    }
 
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, BadCredentialsException {
        String username = request.getParameter("j_username");
        String password = request.getParameter("j_password");       
        if(username.trim().equals("")||password.trim().equals("")){
            mensagem = "Preencha todos os campos";                               
            throw new BadCredentialsException(mensagem);
        }
        try {
            usuario = ujc.getUserByUsernamePassword(username,password);
            if(usuario==null){
                mensagem = "Dados incorretos!";                               
                throw new BadCredentialsException(mensagem);
            }else if (usuario.getAtivo()) {                   
                    request.getSession().setAttribute("usuarioLogado", usuario);
                    mensagem = "Bem vindo: " + usuario.getNome();                                       
                    return new UsernamePasswordAuthenticationToken(usuario.getUsername(), usuario.getPassword(), usuario.getAuthorities());
            } else {
                mensagem = "Sua conta não está ativa";                               
                throw new BadCredentialsException(mensagem);
            }
            
        } catch (Exception e) {
            throw new BadCredentialsException(e.getMessage());
        }
    }
 
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        request.getSession().setAttribute("msg", mensagem);
        if(usuario.getPermissao().getAuthority().equals("ROLE_USER")){
            response.sendRedirect("/faces/usuario/Principal.xhtml");
        }else{
            response.sendRedirect("/faces/administrador/Principal.xhtml");
        }
    }
 
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        request.getSession().setAttribute("msg", mensagem);
        
        response.sendRedirect("/faces/login.xhtml");
    }
 
    public void limparCampos(){
        
    }
    
}
