/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import com.google.appengine.api.datastore.Key;
import dao.CidadeJpaController;
import dao.MotoristaJpaController;
import dao.SolicitacaoJpaController;
import dao.VeiculoJpaController;
import dao.ViagemJpaController;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.RollbackFailureException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import modelo.Cidade;
import modelo.Motorista;
import modelo.Solicitacao;
import modelo.Veiculo;
import modelo.Viagem;
import util.EMF;

/**
 *
 * @author Alisson
 */
public class ManterViagemNegocio {
    private ViagemJpaController vjc;
    private MotoristaJpaController mjc;
    private VeiculoJpaController vcjc;
    private CidadeJpaController cjc;
    
    public ManterViagemNegocio(){
        vjc = new ViagemJpaController(EMF.get());
        mjc = new MotoristaJpaController(EMF.get());
        vcjc = new VeiculoJpaController(EMF.get());
        cjc = new CidadeJpaController(EMF.get());
    }
    
    public void inserir(Viagem viagem){
        try {
            vjc.create(viagem);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(ManterViagemNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ManterViagemNegocio.class.getName()).log(Level.SEVERE, null, ex);
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
    public void editar(Viagem viagem){
        try {
            vjc.edit(viagem);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ManterVeiculoNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(ManterVeiculoNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ManterVeiculoNegocio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public List<Viagem> getViagens(){
        return vjc.findViagemEntities();
    }
    
    public List<Motorista> getMotoristas(){
        return mjc.findMotoristaEntities();
    }
    
    public List<Veiculo> getVeiculos(){
        return vcjc.findVeiculoEntities();
    }
    
    public int contador(){
        return vjc.getViagemCount();
    }

    public List<Cidade> getCidades() {
        return cjc.findCidadeEntities();
    }
}
