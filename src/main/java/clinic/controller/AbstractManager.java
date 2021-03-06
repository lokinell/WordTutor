package clinic.controller;

import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.UserTransaction;

/**
 */
public abstract class AbstractManager {

    @PersistenceUnit(unitName="recipeUnit")
    private EntityManagerFactory emf;
    @Resource
    private UserTransaction userTransaction;

    protected final <T> T doInTransaction(PersistenceAction<T> action) throws ManagerException {
        EntityManager em = emf.createEntityManager();
        try {
            userTransaction.begin();
            T result = action.execute(em);
            userTransaction.commit();
            return result;
        } catch (Exception e) {
            try {
                userTransaction.rollback();
            } catch (Exception ex) {
                Logger.getLogger(AbstractManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            throw new ManagerException(e);
        } finally {
            em.close();
        }

    }

    protected final void doInTransaction(PersistenceActionWithoutResult action) throws ManagerException {
        EntityManager em = emf.createEntityManager();
        try {
            userTransaction.begin();
            action.execute(em);
            userTransaction.commit();
        } catch (Exception e) {
            try {
                userTransaction.rollback();
            } catch (Exception ex) {
                Logger.getLogger(AbstractManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            throw new ManagerException(e);
        } finally {
            em.close();
        }
    }

    protected static interface PersistenceAction<T> {

        T execute(EntityManager em);
    }

    protected static interface PersistenceActionWithoutResult {

        void execute(EntityManager em);
    }

    protected void addMessage(String message) {
        addMessage(null, message, FacesMessage.SEVERITY_INFO);
    }

    protected void addMessage(String componentId, String message) {
        addMessage(componentId, message, FacesMessage.SEVERITY_INFO);
    }

    protected void addMessage(String message, Severity severity) {
        addMessage(null, message, severity);
    }

    protected void addMessage(String componentId, String message, Severity severity) {
        FacesContext.getCurrentInstance().addMessage(componentId, new FacesMessage(severity, message, message));
    }

    protected String getMessageForKey(String key) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ResourceBundle rb = ctx.getApplication().getResourceBundle(ctx, "i18n");
        return rb.getString(key);
    }

    protected FacesMessage getFacesMessageForKey(String key) {
        return new FacesMessage(getMessageForKey(key));
    }

    protected Logger getLogger(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class for logger is required.");
        }
        return Logger.getLogger(clazz.getName());
    }

    protected void publishEvent(Class<? extends SystemEvent> eventClass, Object source) {
        if (source != null) {
            FacesContext ctx = FacesContext.getCurrentInstance();
            ctx.getApplication().publishEvent(ctx, eventClass, source);
        }
    }

    protected void subscribeToEvent(Class<? extends SystemEvent> eventClass, SystemEventListener listener) {
        FacesContext.getCurrentInstance().getApplication().subscribeToEvent(eventClass, listener);
    }

    protected void unsubscribeFromEvent(Class<? extends SystemEvent> eventClass, SystemEventListener listener) {
        FacesContext.getCurrentInstance().getApplication().unsubscribeFromEvent(eventClass, listener);
    }
}
