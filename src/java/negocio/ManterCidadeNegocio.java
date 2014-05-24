/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import com.google.appengine.api.datastore.Key;
import dao.CidadeJpaController;
import dao.EstadoJpaController;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.RollbackFailureException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Cidade;
import modelo.Estado;
import modelo.Viagem;
import util.EMF;

/**
 *
 * @author Alisson
 */
public class ManterCidadeNegocio {
    private CidadeJpaController cjc;
    private EstadoJpaController ejc;
    
    public ManterCidadeNegocio(){
        cjc = new CidadeJpaController(EMF.get());
        ejc = new EstadoJpaController(EMF.get());
    }
    public void inserir(Cidade cidade){       
        try {
            //usuario.setSolicitacoes(new ArrayList<Solicitacao>());
            cidade.setViagens(new ArrayList<Viagem>());
            cjc.create(cidade);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(ManterUsuarioNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ManterUsuarioNegocio.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    public void editar(Cidade cidade){
        try {
            //usuario.setSolicitacoes(new ArrayList<Solicitacao>());
            cjc.edit(cidade);
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
            cjc.destroy(key);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ManterUsuarioNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(ManterUsuarioNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ManterUsuarioNegocio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    public Cidade getCidade(Key key){
        return cjc.findCidade(key);
    }
    public List<Cidade> getCidades(){
        return cjc.findCidadeEntities();
    }
    
    public List<Estado> getEstados(){
        return ejc.findEstadoEntities();
    }
    public Estado getEstado(Key key){
        return ejc.findEstado(key);
    }
}
