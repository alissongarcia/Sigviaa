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
import modelo.Solicitacao;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import modelo.Usuario;

/**
 *
 * @author Fernando
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        
        this.emf = emf;
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws RollbackFailureException, Exception {
        /* EntityManager em = getEntityManager();
        try {
        em.getTransaction().begin();
        em.persist(usuario);
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
        }*/
        if (usuario.getSolicitacoes() == null) {
            usuario.setSolicitacoes(new ArrayList<Solicitacao>());
        }
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            
            List<Solicitacao> attachedSolicitacoes = new ArrayList<Solicitacao>();
            for (Solicitacao solicitacoesSolicitacaoToAttach : usuario.getSolicitacoes()) {
                solicitacoesSolicitacaoToAttach = em.getReference(solicitacoesSolicitacaoToAttach.getClass(), solicitacoesSolicitacaoToAttach.getKey());
                attachedSolicitacoes.add(solicitacoesSolicitacaoToAttach);
            }
            usuario.setSolicitacoes(attachedSolicitacoes);
            em.persist(usuario);
            for (Solicitacao solicitacoesSolicitacao : usuario.getSolicitacoes()) {
                Usuario oldSolicitanteOfSolicitacoesSolicitacao = solicitacoesSolicitacao.getSolicitante();
                solicitacoesSolicitacao.setSolicitante(usuario);
                solicitacoesSolicitacao = em.merge(solicitacoesSolicitacao);
                if (oldSolicitanteOfSolicitacoesSolicitacao != null) {
                    oldSolicitanteOfSolicitacoesSolicitacao.getSolicitacoes().remove(solicitacoesSolicitacao);
                    oldSolicitanteOfSolicitacoesSolicitacao = em.merge(oldSolicitanteOfSolicitacoesSolicitacao);
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

    public void edit(Usuario usuario) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getKey());
            List<Solicitacao> solicitacoesOld = persistentUsuario.getSolicitacoes();
            List<Solicitacao> solicitacoesNew = usuario.getSolicitacoes();
            List<Solicitacao> attachedSolicitacoesNew = new ArrayList<Solicitacao>();
            for (Solicitacao solicitacoesNewSolicitacaoToAttach : solicitacoesNew) {
                solicitacoesNewSolicitacaoToAttach = em.getReference(solicitacoesNewSolicitacaoToAttach.getClass(), solicitacoesNewSolicitacaoToAttach.getKey());
                attachedSolicitacoesNew.add(solicitacoesNewSolicitacaoToAttach);
            }
            solicitacoesNew = attachedSolicitacoesNew;
            usuario.setSolicitacoes(solicitacoesNew);
            usuario = em.merge(usuario);
            for (Solicitacao solicitacoesOldSolicitacao : solicitacoesOld) {
                if (!solicitacoesNew.contains(solicitacoesOldSolicitacao)) {
                    solicitacoesOldSolicitacao.setSolicitante(null);
                    solicitacoesOldSolicitacao = em.merge(solicitacoesOldSolicitacao);
                }
            }
            for (Solicitacao solicitacoesNewSolicitacao : solicitacoesNew) {
                if (!solicitacoesOld.contains(solicitacoesNewSolicitacao)) {
                    Usuario oldSolicitanteOfSolicitacoesNewSolicitacao = solicitacoesNewSolicitacao.getSolicitante();
                    solicitacoesNewSolicitacao.setSolicitante(usuario);
                    solicitacoesNewSolicitacao = em.merge(solicitacoesNewSolicitacao);
                    if (oldSolicitanteOfSolicitacoesNewSolicitacao != null && !oldSolicitanteOfSolicitacoesNewSolicitacao.equals(usuario)) {
                        oldSolicitanteOfSolicitacoesNewSolicitacao.getSolicitacoes().remove(solicitacoesNewSolicitacao);
                        oldSolicitanteOfSolicitacoesNewSolicitacao = em.merge(oldSolicitanteOfSolicitacoesNewSolicitacao);
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
                Key id = usuario.getKey();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getKey();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<Solicitacao> solicitacoes = usuario.getSolicitacoes();
            for (Solicitacao solicitacoesSolicitacao : solicitacoes) {
                solicitacoesSolicitacao.setSolicitante(null);
                solicitacoesSolicitacao = em.merge(solicitacoesSolicitacao);
            }
            em.remove(usuario);
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

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from Usuario as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Usuario findUsuario(Key id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from Usuario as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    public Usuario getUserByUsername(String username) {
        EntityManager em = getEntityManager();        
        Usuario gud = null;
                try {
                        Query q = em.createQuery("select u from Usuario u WHERE u.username=:username");
                        q.setParameter("username", username);
                        gud = (Usuario) q.getSingleResult();
                }catch (NoResultException e) {
                   
                }catch (Exception e) {
                    
                }finally {
                        em.close();
                }
                return gud;
    }

    public Usuario getUserByUsernamePassword(String username, String password) {
        EntityManager em = getEntityManager();        
        Usuario gud = null;
                try {
                        Query q = em.createQuery("select u from Usuario u WHERE u.username=:username AND u.password=:password");
                        q.setParameter("username", username);
                        q.setParameter("password", password);
                        gud = (Usuario) q.getSingleResult();
                }catch (NoResultException e) {
                    
                } catch (Exception e) {
                    
                }  finally {
                        em.close();
                }
                return gud;
    }
}
