/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import com.google.appengine.api.datastore.Key;
import dao.MotoristaJpaController;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.RollbackFailureException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Motorista;
import util.EMF;

/**
 *
 * @author Fernando
 */
public class ManterMotoristaNegocio {
    
    private MotoristaJpaController mjc;

    public ManterMotoristaNegocio() {
        mjc= new MotoristaJpaController(EMF.get());
    }
    
    public void inserir(Motorista motorista){
        try {
            mjc.create(motorista);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(ManterMotoristaNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ManterMotoristaNegocio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void remover(Key key){
        try {
            mjc.destroy(key);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ManterMotoristaNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(ManterMotoristaNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ManterMotoristaNegocio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void editar(Motorista motorista){
        try {
            mjc.edit(motorista);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ManterMotoristaNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(ManterMotoristaNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ManterMotoristaNegocio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public List<Motorista> getMotoristas(){
        return mjc.findMotoristaEntities();
    }
    public Motorista getMotorista(Key key){
        return mjc.findMotorista(key);
    }
    public int contador(){
        return mjc.getMotoristaCount();
    }
}
