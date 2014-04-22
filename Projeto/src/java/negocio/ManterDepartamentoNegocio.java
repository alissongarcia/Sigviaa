/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import com.google.appengine.api.datastore.Key;
import dao.DepartamentoJpaController;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.RollbackFailureException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Departamento;
import util.EMF;

/**
 *
 * @author Fernando
 */
public class ManterDepartamentoNegocio {
    private DepartamentoJpaController djc;
    
    public ManterDepartamentoNegocio(){
        djc = new DepartamentoJpaController(EMF.get());
    }

    public DepartamentoJpaController getDjc() {
        return djc;
    }

    public void setDjc(DepartamentoJpaController djc) {
        this.djc = djc;
    }
    public void inserir(Departamento departamento){        
        try {
            djc.create(departamento);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(ManterDepartamentoNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ManterDepartamentoNegocio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void editar(Departamento departamento){        
        try {
            djc.edit(departamento);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ManterDepartamentoNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(ManterDepartamentoNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ManterDepartamentoNegocio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void remover(Key key){
        try {
            djc.destroy(key);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ManterDepartamentoNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(ManterDepartamentoNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ManterDepartamentoNegocio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public List<Departamento> getDepartamentos(){
        return djc.findDepartamentoEntities();
    }
}
