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
import modelo.Estado;
import modelo.Viagem;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Cidade;

/**
 *
 * @author Fernando
 */
public class CidadeJpaController implements Serializable {

    public CidadeJpaController(EntityManagerFactory emf) {
        
        this.emf = emf;
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cidade cidade) throws RollbackFailureException, Exception {
        if (cidade.getViagens() == null) {
            cidade.setViagens(new ArrayList<Viagem>());
        }
        EntityManager em = this.getEntityManager();
        try {
            em.getTransaction().begin();
            
            Estado estado = cidade.getEstado();
            if (estado != null) {
                estado = em.getReference(estado.getClass(), estado.getKey());
                cidade.setEstado(estado);
            }
            List<Viagem> attachedViagens = new ArrayList<Viagem>();
            for (Viagem viagensViagemToAttach : cidade.getViagens()) {
                viagensViagemToAttach = em.getReference(viagensViagemToAttach.getClass(), viagensViagemToAttach.getKey());
                attachedViagens.add(viagensViagemToAttach);
            }
            cidade.setViagens(attachedViagens);
            em.persist(cidade);
            if (estado != null) {
                estado.getCidades().add(cidade);
                estado = em.merge(estado);
            }
            for (Viagem viagensViagem : cidade.getViagens()) {
                Cidade oldDestinoOfViagensViagem = viagensViagem.getDestino();
                viagensViagem.setDestino(cidade);
                viagensViagem = em.merge(viagensViagem);
                if (oldDestinoOfViagensViagem != null) {
                    oldDestinoOfViagensViagem.getViagens().remove(viagensViagem);
                    oldDestinoOfViagensViagem = em.merge(oldDestinoOfViagensViagem);
                }
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

    public void edit(Cidade cidade) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = this.getEntityManager();
        try {
            em.getTransaction().begin();
            
            Cidade persistentCidade = em.find(Cidade.class, cidade.getKey());
            Estado estadoOld = persistentCidade.getEstado();
            Estado estadoNew = cidade.getEstado();
            List<Viagem> viagensOld = persistentCidade.getViagens();
            List<Viagem> viagensNew = cidade.getViagens();
            if (estadoNew != null) {
                estadoNew = em.getReference(estadoNew.getClass(), estadoNew.getKey());
                cidade.setEstado(estadoNew);
            }
            List<Viagem> attachedViagensNew = new ArrayList<Viagem>();
            for (Viagem viagensNewViagemToAttach : viagensNew) {
                viagensNewViagemToAttach = em.getReference(viagensNewViagemToAttach.getClass(), viagensNewViagemToAttach.getKey());
                attachedViagensNew.add(viagensNewViagemToAttach);
            }
            viagensNew = attachedViagensNew;
            cidade.setViagens(viagensNew);
            cidade = em.merge(cidade);
            if (estadoOld != null && !estadoOld.equals(estadoNew)) {
                estadoOld.getCidades().remove(cidade);
                estadoOld = em.merge(estadoOld);
            }
            if (estadoNew != null && !estadoNew.equals(estadoOld)) {
                estadoNew.getCidades().add(cidade);
                estadoNew = em.merge(estadoNew);
            }
            for (Viagem viagensOldViagem : viagensOld) {
                if (!viagensNew.contains(viagensOldViagem)) {
                    viagensOldViagem.setDestino(null);
                    viagensOldViagem = em.merge(viagensOldViagem);
                }
            }
            for (Viagem viagensNewViagem : viagensNew) {
                if (!viagensOld.contains(viagensNewViagem)) {
                    Cidade oldDestinoOfViagensNewViagem = viagensNewViagem.getDestino();
                    viagensNewViagem.setDestino(cidade);
                    viagensNewViagem = em.merge(viagensNewViagem);
                    if (oldDestinoOfViagensNewViagem != null && !oldDestinoOfViagensNewViagem.equals(cidade)) {
                        oldDestinoOfViagensNewViagem.getViagens().remove(viagensNewViagem);
                        oldDestinoOfViagensNewViagem = em.merge(oldDestinoOfViagensNewViagem);
                    }
                }
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
                Key id = cidade.getKey();
                if (findCidade(id) == null) {
                    throw new NonexistentEntityException("The cidade with id " + id + " no longer exists.");
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
            
            Cidade cidade;
            try {
                cidade = em.getReference(Cidade.class, id);
                cidade.getKey();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cidade with id " + id + " no longer exists.", enfe);
            }
            Estado estado = cidade.getEstado();
            if (estado != null) {
                estado.getCidades().remove(cidade);
                estado = em.merge(estado);
            }
            List<Viagem> viagens = cidade.getViagens();
            for (Viagem viagensViagem : viagens) {
                viagensViagem.setDestino(null);
                viagensViagem = em.merge(viagensViagem);
            }
            em.remove(cidade);
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

    public List<Cidade> findCidadeEntities() {
        return findCidadeEntities(true, -1, -1);
    }

    public List<Cidade> findCidadeEntities(int maxResults, int firstResult) {
        return findCidadeEntities(false, maxResults, firstResult);
    }

    private List<Cidade> findCidadeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from Cidade as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Cidade findCidade(Key id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cidade.class, id);
        } finally {
            em.close();
        }
    }

    public int getCidadeCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from Cidade as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
