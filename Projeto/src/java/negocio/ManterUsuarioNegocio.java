/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import com.google.appengine.api.datastore.Key;
import dao.DepartamentoJpaController;
import dao.GrupoJpaController;
import dao.UsuarioJpaController;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.RollbackFailureException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Departamento;
import modelo.Grupo;
import modelo.Solicitacao;
import modelo.Usuario;
import util.EMF;

/**
 *
 * @author Fernando
 */
public class ManterUsuarioNegocio {
    private UsuarioJpaController ujc;
    private GrupoJpaController gjc;
    private DepartamentoJpaController djc;
    
    public ManterUsuarioNegocio(){
        ujc = new UsuarioJpaController(EMF.get());
        gjc = new GrupoJpaController(EMF.get());
        djc = new DepartamentoJpaController(EMF.get());
    }
    public void inserir(Usuario usuario){       
        try {
            usuario.setSolicitacoes(new ArrayList<Solicitacao>());
            ujc.create(usuario);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(ManterUsuarioNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ManterUsuarioNegocio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void editar(Usuario usuario){
        try {
            usuario.setSolicitacoes(new ArrayList<Solicitacao>());
            ujc.edit(usuario);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ManterUsuarioNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(ManterUsuarioNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ManterUsuarioNegocio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void remover(Key key){
        try {
            ujc.destroy(key);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ManterUsuarioNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(ManterUsuarioNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ManterUsuarioNegocio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public boolean verificarDisponibilidade(String username) {
        Usuario aux = ujc.getUserByUsername(username);
        if(aux==null){
            return true;
        }
        return false;
    }
    
    public Usuario getUsuario(Key key){
        return ujc.findUsuario(key);
    }
    public List<Usuario> getUsuarios(){
        return ujc.findUsuarioEntities();
    }
    public List<Grupo> getGrupos(){
        return gjc.findGrupoEntities();
    }
    public List<Departamento> getDepartamentos(){
        return djc.findDepartamentoEntities();
    }
    public Departamento getDepartamento(Key key){
        return djc.findDepartamento(key);
    }
    public Grupo getGrupo(Key key){
        return gjc.findGrupo(key);
    }
}
