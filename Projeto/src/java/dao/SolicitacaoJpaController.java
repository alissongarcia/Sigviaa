/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import com.google.appengine.api.datastore.Key;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import modelo.Solicitacao;
import modelo.Usuario;
import modelo.Viagem;

/**
 *
 * @author Fernando
 */
public class SolicitacaoJpaController implements Serializable {

    public SolicitacaoJpaController(EntityManagerFactory emf) {
        
        this.emf = emf;
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Solicitacao solicitacao) throws RollbackFailureException, Exception {
        EntityManager em = this.getEntityManager();
        try {
            em.getTransaction().begin();
            
            Usuario solicitante = solicitacao.getSolicitante();
            if (solicitante != null) {
                solicitante = em.getReference(solicitante.getClass(), solicitante.getKey());
                solicitacao.setSolicitante(solicitante);
            }
            Viagem viagem = solicitacao.getViagem();
            if (viagem != null) {
                viagem = em.getReference(viagem.getClass(), viagem.getKey());
                solicitacao.setViagem(viagem);
            }
            em.persist(solicitacao);
            if (solicitante != null) {
                solicitante.getSolicitacoes().add(solicitacao);
                solicitante = em.merge(solicitante);
            }
            if (viagem != null) {
                viagem.getSolicitacoes().add(solicitacao);
                viagem = em.merge(viagem);
            }
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

    public void edit(Solicitacao solicitacao) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = this.getEntityManager();
        try {
            em.getTransaction().begin();
            
            Solicitacao persistentSolicitacao = em.find(Solicitacao.class, solicitacao.getKey());
            Usuario solicitanteOld = persistentSolicitacao.getSolicitante();
            Usuario solicitanteNew = solicitacao.getSolicitante();
            Viagem viagemOld = persistentSolicitacao.getViagem();
            Viagem viagemNew = solicitacao.getViagem();
            if (solicitanteNew != null) {
                solicitanteNew = em.getReference(solicitanteNew.getClass(), solicitanteNew.getKey());
                solicitacao.setSolicitante(solicitanteNew);
            }
            if (viagemNew != null) {
                viagemNew = em.getReference(viagemNew.getClass(), viagemNew.getKey());
                solicitacao.setViagem(viagemNew);
            }
            solicitacao = em.merge(solicitacao);
            if (solicitanteOld != null && !solicitanteOld.equals(solicitanteNew)) {
                solicitanteOld.getSolicitacoes().remove(solicitacao);
                solicitanteOld = em.merge(solicitanteOld);
            }
            if (solicitanteNew != null && !solicitanteNew.equals(solicitanteOld)) {
                solicitanteNew.getSolicitacoes().add(solicitacao);
                solicitanteNew = em.merge(solicitanteNew);
            }
            if (viagemOld != null && !viagemOld.equals(viagemNew)) {
                viagemOld.getSolicitacoes().remove(solicitacao);
                viagemOld = em.merge(viagemOld);
            }
            if (viagemNew != null && !viagemNew.equals(viagemOld)) {
                viagemNew.getSolicitacoes().add(solicitacao);
                viagemNew = em.merge(viagemNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Key id = solicitacao.getKey();
                if (findSolicitacao(id) == null) {
                    throw new NonexistentEntityException("The solicitacao with id " + id + " no longer exists.");
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
            
            Solicitacao solicitacao;
            try {
                solicitacao = em.getReference(Solicitacao.class, id);
                solicitacao.getKey();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The solicitacao with id " + id + " no longer exists.", enfe);
            }
            Usuario solicitante = solicitacao.getSolicitante();
            if (solicitante != null) {
                solicitante.getSolicitacoes().remove(solicitacao);
                solicitante = em.merge(solicitante);
            }
            Viagem viagem = solicitacao.getViagem();
            if (viagem != null) {
                viagem.getSolicitacoes().remove(solicitacao);
                viagem = em.merge(viagem);
            }
            em.remove(solicitacao);
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

    public List<Solicitacao> findSolicitacaoEntities() {
        return findSolicitacaoEntities(true, -1, -1);
    }

    public List<Solicitacao> findSolicitacaoEntities(int maxResults, int firstResult) {
        return findSolicitacaoEntities(false, maxResults, firstResult);
    }

    private List<Solicitacao> findSolicitacaoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from Solicitacao as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Solicitacao findSolicitacao(Key id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Solicitacao.class, id);
        } finally {
            em.close();
        }
    }

    public int getSolicitacaoCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from Solicitacao as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
