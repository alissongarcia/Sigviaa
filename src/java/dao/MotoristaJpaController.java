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
import modelo.Motorista;

/**
 *
 * @author Fernando
 */
public class MotoristaJpaController implements Serializable {

    public MotoristaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Motorista motorista) throws RollbackFailureException, Exception {
        EntityManager em = this.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(motorista);
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

    public void edit(Motorista motorista) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = this.getEntityManager();        
        try {
            em.getTransaction().begin();
            motorista = em.merge(motorista);
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Key id = motorista.getKey();
                if (findMotorista(id) == null) {
                    throw new NonexistentEntityException("The motorista with id " + id + " no longer exists.");
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
            Motorista motorista;
            try {
                motorista = em.getReference(Motorista.class, id);
                motorista.getKey();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The motorista with id " + id + " no longer exists.", enfe);
            }
            em.remove(motorista);
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

    public List<Motorista> findMotoristaEntities() {
        return findMotoristaEntities(true, -1, -1);
    }

    public List<Motorista> findMotoristaEntities(int maxResults, int firstResult) {
        return findMotoristaEntities(false, maxResults, firstResult);
    }

    private List<Motorista> findMotoristaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from Motorista as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Motorista findMotorista(Key id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Motorista.class, id);
        } finally {
            em.close();
        }
    }

    public int getMotoristaCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from Motorista as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
