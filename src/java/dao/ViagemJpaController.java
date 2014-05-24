/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import com.google.appengine.api.datastore.Key;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import modelo.Motorista;
import modelo.Veiculo;
import modelo.Cidade;
import modelo.Solicitacao;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Viagem;

/**
 *
 * @author Fernando
 */
public class ViagemJpaController implements Serializable {

    public ViagemJpaController(EntityManagerFactory emf) {
        
        this.emf = emf;
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Viagem viagem) throws RollbackFailureException, Exception {
        /*EntityManager em = getEntityManager();
        try {
        em.getTransaction().begin();
        em.persist(viagem);
        em.getTransaction().commit();
        } catch (Exception ex) {
        try {
        em.getTransaction().rollback();
        } catch (Exception re) {
        throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
        }
        System.out.println("Imprimindo...");
        ex.printStackTrace();
        throw ex;
        } finally {
        if (em != null) {
        em.close();
        }
        }*/
        if (viagem.getSolicitacoes() == null) {
            viagem.setSolicitacoes(new ArrayList<Solicitacao>());
        }
        EntityManager em = this.getEntityManager();
        try {
            em.getTransaction().begin();
            Motorista motorista = viagem.getMotorista();
            if (motorista != null) {
                motorista = em.getReference(motorista.getClass(), motorista.getKey());
                viagem.setMotorista(motorista);
            }
            Veiculo veiculo = viagem.getVeiculo();
            if (veiculo != null) {
                veiculo = em.getReference(veiculo.getClass(), veiculo.getKey());
                viagem.setVeiculo(veiculo);
            }
            Cidade origem = viagem.getOrigem();
            if (origem != null) {
                origem = em.getReference(origem.getClass(), origem.getKey());
                viagem.setOrigem(origem);
            }
            Cidade destino = viagem.getDestino();
            if (destino != null) {
                destino = em.getReference(destino.getClass(), destino.getKey());
                viagem.setDestino(destino);
            }
            List<Solicitacao> attachedSolicitacoes = new ArrayList<Solicitacao>();
            for (Solicitacao solicitacoesSolicitacaoToAttach : viagem.getSolicitacoes()) {
                solicitacoesSolicitacaoToAttach = em.getReference(solicitacoesSolicitacaoToAttach.getClass(), solicitacoesSolicitacaoToAttach.getKey());
                attachedSolicitacoes.add(solicitacoesSolicitacaoToAttach);
            }
            
            viagem.setSolicitacoes(attachedSolicitacoes);
            em.persist(viagem);
             
            /*if (veiculo != null) {
                veiculo.getViagens().add(viagem);
                veiculo = em.merge(veiculo);
            }
            
            if (motorista != null) {
                motorista.getViagens().add(viagem);
                motorista = em.merge(motorista);
            }
            
            if (veiculo != null) {
                veiculo.getViagens().add(viagem);
                veiculo = em.merge(veiculo);
            }
            
            
            if (origem != null) {
                origem.getViagens().add(viagem);
                origem = em.merge(origem);
            }
            
            if (destino != null) {
                destino.getViagens().add(viagem);
                destino = em.merge(destino);
            }
            for (Solicitacao solicitacoesSolicitacao : viagem.getSolicitacoes()) {
                Viagem oldViagemOfSolicitacoesSolicitacao = solicitacoesSolicitacao.getViagem();
                solicitacoesSolicitacao.setViagem(viagem);
                solicitacoesSolicitacao = em.merge(solicitacoesSolicitacao);
                if (oldViagemOfSolicitacoesSolicitacao != null) {
                    oldViagemOfSolicitacoesSolicitacao.getSolicitacoes().remove(solicitacoesSolicitacao);
                    oldViagemOfSolicitacoesSolicitacao = em.merge(oldViagemOfSolicitacoesSolicitacao);
                }
            }*/
            
            
            em.getTransaction().commit();
            
        } catch (Exception ex) {
            
            try {
                em.getTransaction().rollback();
             
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            System.out.println("Imprimindo...");
            ex.printStackTrace();
        } finally {
            
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Viagem viagem) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = this.getEntityManager();
        try {
            em.getTransaction().begin();
            Viagem persistentViagem = em.find(Viagem.class, viagem.getKey());
            Motorista motoristaOld = persistentViagem.getMotorista();
            Motorista motoristaNew = viagem.getMotorista();
            Veiculo veiculoOld = persistentViagem.getVeiculo();
            Veiculo veiculoNew = viagem.getVeiculo();
            Cidade origemOld = persistentViagem.getOrigem();
            Cidade origemNew = viagem.getOrigem();
            Cidade destinoOld = persistentViagem.getDestino();
            Cidade destinoNew = viagem.getDestino();
            List<Solicitacao> solicitacoesOld = persistentViagem.getSolicitacoes();
            List<Solicitacao> solicitacoesNew = viagem.getSolicitacoes();
            if (motoristaNew != null) {
                motoristaNew = em.getReference(motoristaNew.getClass(), motoristaNew.getKey());
                viagem.setMotorista(motoristaNew);
            }
            if (veiculoNew != null) {
                veiculoNew = em.getReference(veiculoNew.getClass(), veiculoNew.getKey());
                viagem.setVeiculo(veiculoNew);
            }
            if (origemNew != null) {
                origemNew = em.getReference(origemNew.getClass(), origemNew.getKey());
                viagem.setOrigem(origemNew);
            }
            if (destinoNew != null) {
                destinoNew = em.getReference(destinoNew.getClass(), destinoNew.getKey());
                viagem.setDestino(destinoNew);
            }
            
            viagem = em.merge(viagem);
            
            /*List<Solicitacao> attachedSolicitacoesNew = new ArrayList<Solicitacao>();
            for (Solicitacao solicitacoesNewSolicitacaoToAttach : solicitacoesNew) {
            solicitacoesNewSolicitacaoToAttach = em.getReference(solicitacoesNewSolicitacaoToAttach.getClass(), solicitacoesNewSolicitacaoToAttach.getKey());
            attachedSolicitacoesNew.add(solicitacoesNewSolicitacaoToAttach);
            }
            solicitacoesNew = attachedSolicitacoesNew;
            viagem.setSolicitacoes(solicitacoesNew);*/
            
            /*if (motoristaOld != null && !motoristaOld.equals(motoristaNew)) {
            motoristaOld.getViagens().remove(viagem);
            motoristaOld = em.merge(motoristaOld);
            }
            if (motoristaNew != null && !motoristaNew.equals(motoristaOld)) {
            motoristaNew.getViagens().add(viagem);
            motoristaNew = em.merge(motoristaNew);
            }
            if (veiculoOld != null && !veiculoOld.equals(veiculoNew)) {
            veiculoOld.getViagens().remove(viagem);
            veiculoOld = em.merge(veiculoOld);
            }
            if (veiculoNew != null && !veiculoNew.equals(veiculoOld)) {
            veiculoNew.getViagens().add(viagem);
            veiculoNew = em.merge(veiculoNew);
            }
            if (origemOld != null && !origemOld.equals(origemNew)) {
            origemOld.getViagens().remove(viagem);
            origemOld = em.merge(origemOld);
            }
            if (origemNew != null && !origemNew.equals(origemOld)) {
            origemNew.getViagens().add(viagem);
            origemNew = em.merge(origemNew);
            }
            if (destinoOld != null && !destinoOld.equals(destinoNew)) {
            destinoOld.getViagens().remove(viagem);
            destinoOld = em.merge(destinoOld);
            }
            if (destinoNew != null && !destinoNew.equals(destinoOld)) {
            destinoNew.getViagens().add(viagem);
            destinoNew = em.merge(destinoNew);
            }
            for (Solicitacao solicitacoesOldSolicitacao : solicitacoesOld) {
            if (!solicitacoesNew.contains(solicitacoesOldSolicitacao)) {
            solicitacoesOldSolicitacao.setViagem(null);
            solicitacoesOldSolicitacao = em.merge(solicitacoesOldSolicitacao);
            }
            }
            for (Solicitacao solicitacoesNewSolicitacao : solicitacoesNew) {
            if (!solicitacoesOld.contains(solicitacoesNewSolicitacao)) {
            Viagem oldViagemOfSolicitacoesNewSolicitacao = solicitacoesNewSolicitacao.getViagem();
            solicitacoesNewSolicitacao.setViagem(viagem);
            solicitacoesNewSolicitacao = em.merge(solicitacoesNewSolicitacao);
            if (oldViagemOfSolicitacoesNewSolicitacao != null && !oldViagemOfSolicitacoesNewSolicitacao.equals(viagem)) {
            oldViagemOfSolicitacoesNewSolicitacao.getSolicitacoes().remove(solicitacoesNewSolicitacao);
            oldViagemOfSolicitacoesNewSolicitacao = em.merge(oldViagemOfSolicitacoesNewSolicitacao);
            }
            }
            }*/
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Key id = viagem.getKey();
                if (findViagem(id) == null) {
                    throw new NonexistentEntityException("The viagem with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Key id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = this.getEntityManager();
        try {
            em.getTransaction().begin();
            Viagem viagem;
            try {
                viagem = em.getReference(Viagem.class, id);
                viagem.getKey();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The viagem with id " + id + " no longer exists.", enfe);
            }
            /*Motorista motorista = viagem.getMotorista();
            if (motorista != null) {
            motorista.getViagens().remove(viagem);
            motorista = em.merge(motorista);
            }
            Veiculo veiculo = viagem.getVeiculo();
            if (veiculo != null) {
            veiculo.getViagens().remove(viagem);
            veiculo = em.merge(veiculo);
            }
            Cidade origem = viagem.getOrigem();
            if (origem != null) {
            origem.getViagens().remove(viagem);
            origem = em.merge(origem);
            }
            Cidade destino = viagem.getDestino();
            if (destino != null) {
            destino.getViagens().remove(viagem);
            destino = em.merge(destino);
            }
            List<Solicitacao> solicitacoes = viagem.getSolicitacoes();
            for (Solicitacao solicitacoesSolicitacao : solicitacoes) {
            solicitacoesSolicitacao.setViagem(null);
            solicitacoesSolicitacao = em.merge(solicitacoesSolicitacao);
            }*/
            em.remove(viagem);
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Viagem> findViagemEntities() {
        return findViagemEntities(true, -1, -1);
    }

    public List<Viagem> findViagemEntities(int maxResults, int firstResult) {
        return findViagemEntities(false, maxResults, firstResult);
    }

    private List<Viagem> findViagemEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from Viagem as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Viagem findViagem(Key id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Viagem.class, id);
        } finally {
            em.close();
        }
    }

    public int getViagemCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from Viagem as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
