/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import com.google.appengine.api.datastore.Key;
import dao.EstadoJpaController;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.RollbackFailureException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Estado;
import util.EMF;

/**
 *
 * @author Alisson
 */
public class ManterEstadoNegocio {
    
    private EstadoJpaController ejc;

    public ManterEstadoNegocio() {
        ejc= new EstadoJpaController(EMF.get());
    }
    
    public void inserir(Estado estado){
        try {
            ejc.create(estado);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(ManterMotoristaNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ManterMotoristaNegocio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void remover(Key key){
        try {
            ejc.destroy(key);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ManterMotoristaNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(ManterMotoristaNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ManterMotoristaNegocio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void editar(Estado estado){
        try {
            ejc.edit(estado);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ManterMotoristaNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(ManterMotoristaNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ManterMotoristaNegocio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public List<Estado> getEstados(){
        return ejc.findEstadoEntities();
    }
    public int contador(){
        return ejc.getEstadoCount();
    }
}
