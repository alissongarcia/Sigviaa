/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import com.google.appengine.api.datastore.Key;
import dao.SolicitacaoJpaController;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.RollbackFailureException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Solicitacao;
import util.EMF;

/**
 *
 * @author Alisson
 */
public class ManterSolicitacaoNegocio {
    private SolicitacaoJpaController sjc;
    
    public ManterSolicitacaoNegocio(){
        sjc = new SolicitacaoJpaController(EMF.get());
    }
    
    public void inserir(Solicitacao solicitacao){
        try {
            sjc.create(solicitacao);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(ManterVeiculoNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ManterVeiculoNegocio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void remover(Key key){
        try {
            sjc.destroy(key);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ManterVeiculoNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(ManterVeiculoNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ManterVeiculoNegocio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void editar(Solicitacao solicitacao){
        try {
            sjc.edit(solicitacao);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ManterVeiculoNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(ManterVeiculoNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ManterVeiculoNegocio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public List<Solicitacao> getVeiculos(){
        return sjc.findSolicitacaoEntities();
    }
    public int contador(){
        return sjc.getSolicitacaoCount();
    }
}
