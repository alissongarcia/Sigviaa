/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import com.google.appengine.api.datastore.Key;
import dao.GrupoJpaController;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.RollbackFailureException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Grupo;
import util.EMF;

/**
 *
 * @author Fernando
 */
public class ManterGrupoNegocio {
    private GrupoJpaController gjc;
    
    public ManterGrupoNegocio(){
        gjc = new GrupoJpaController(EMF.get());
    }

    public GrupoJpaController getGjc() {
        return gjc;
    }

    public void setGjc(GrupoJpaController gjc) {
        this.gjc = gjc;
    }
    public void inserir(Grupo grupo){
        try{
            gjc.create(grupo);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(ManterGrupoNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ManterGrupoNegocio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void editar(Grupo grupo){        
        try {
            gjc.edit(grupo);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ManterGrupoNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(ManterGrupoNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ManterGrupoNegocio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void remover(Key key){
        try {
            gjc.destroy(key);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ManterGrupoNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(ManterGrupoNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ManterGrupoNegocio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public List<Grupo> getGrupos(){
        return gjc.findGrupoEntities();
    }
}
