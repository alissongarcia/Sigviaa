/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import com.google.appengine.api.datastore.Key;
import dao.VeiculoJpaController;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.RollbackFailureException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Veiculo;
import modelo.Viagem;
import util.EMF;

/**
 *
 * @author Fernando
 */
public class ManterVeiculoNegocio {
    private VeiculoJpaController vjc;
    
    public ManterVeiculoNegocio(){
        vjc = new VeiculoJpaController(EMF.get());
    }
    
    public void inserir(Veiculo veiculo){
        try {
            veiculo.setViagens(new ArrayList<Viagem>());
            vjc.create(veiculo);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(ManterVeiculoNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ManterVeiculoNegocio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void remover(Key key){
        try {
            vjc.destroy(key);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ManterVeiculoNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(ManterVeiculoNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ManterVeiculoNegocio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void editar(Veiculo veiculo){
        try {
            vjc.edit(veiculo);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ManterVeiculoNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(ManterVeiculoNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ManterVeiculoNegocio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public List<Veiculo> getVeiculos(){
        return vjc.findVeiculoEntities();
    }
    public int contador(){
        return vjc.getVeiculoCount();
    }

    public Veiculo getVeiculo(Key key) {
        return vjc.findVeiculo(key);
    }
}
