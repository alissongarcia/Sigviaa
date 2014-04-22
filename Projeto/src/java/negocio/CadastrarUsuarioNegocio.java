/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import com.google.appengine.api.datastore.Key;
import dao.DepartamentoJpaController;
import dao.GrupoJpaController;
import dao.UsuarioJpaController;
import dao.exceptions.RollbackFailureException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Departamento;
import modelo.Solicitacao;
import modelo.Usuario;
import util.EMF;

/**
 *
 * @author Fernando
 */
public class CadastrarUsuarioNegocio {
    
    public DepartamentoJpaController djc;
    public UsuarioJpaController ujc;
    public GrupoJpaController gjc;
    
    public CadastrarUsuarioNegocio(){
        djc = new DepartamentoJpaController(EMF.get());
        ujc = new UsuarioJpaController(EMF.get());
        gjc = new GrupoJpaController(EMF.get());
    }

    public List<Departamento> getDepartamentos() {
        return djc.findDepartamentoEntities();
    }
    
    public Departamento getDepartamento(Key key){
        return djc.findDepartamento(key);
    }
    
    public void inserir(Usuario usuario) {
        try {           
            usuario.setPermissao(gjc.findGrupoByRole("ROLE_USER"));
            usuario.setAtivo(false);
            usuario.setSolicitacoes(new ArrayList<Solicitacao>());
            ujc.create(usuario);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(CadastrarUsuarioNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CadastrarUsuarioNegocio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean verificarDisponibilidade(String username) {
        Usuario aux = ujc.getUserByUsername(username);
        if(aux==null){
            return true;
        }
        return false;
    }
    
}
