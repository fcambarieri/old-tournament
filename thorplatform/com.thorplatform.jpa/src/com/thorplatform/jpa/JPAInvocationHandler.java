package com.thorplatform.jpa;

import com.thorplatform.notifier.Notifier;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

public class JPAInvocationHandler
        implements InvocationHandler {

    private static final Logger logger = Logger.getLogger(JPAInvocationHandler.class.getName());
    private JPAService localService;
    private JPAInterceptorService interceptorService;
    private EntityManagerFactory entityManagerFactory;
    private static TransactionMonitor monitor = new TransactionMonitor();

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        synchronized (monitor) {
            logger.info("trx: " + getLocalServiceMethodName(method));

            EntityManager em = getEntityManagerFactory().createEntityManager();
            this.localService.setEntityManager(em);
            try {
                Object localObject1 = null;
                if (isTransactional(method)) {
                    localObject1 = transactionalInvokation(proxy, method, args, em);

                    em.close();
                    em = null;
                    return localObject1;
                }
                localObject1 = nonTranasctionalInvokaion(proxy, method, args);

                em.close();
                em = null;
                return localObject1;
            } finally {
                em.close();
                em = null;
            }
        }
    }

    private Object transactionalInvokation(Object proxy, Method method, Object[] args, EntityManager em) throws Throwable {
        EntityTransaction trx = em.getTransaction();
        trx.begin();
        try {
            if (this.interceptorService != null) {
                this.interceptorService.preIntercept(this.localService, method, args);
            }

            Object result = method.invoke(this.localService, args);

            trx.commit();
            logger.info("Transactional committed: " + getLocalServiceMethodName(method));

            if (this.localService.getNotifier() != null) {
                this.localService.getNotifier().notifyAllListener();
            }
            return result;
        } catch (InvocationTargetException ite) {
            if (trx.isActive()) {
                trx.rollback();
            }
            ite.getTargetException().printStackTrace();

            throw ite.getTargetException();
        } catch (Throwable t) {
            if (trx.isActive()) {
                trx.rollback();
            }
            throw t;
        }
        
    }

    private Object nonTranasctionalInvokaion(Object proxy, Method method, Object[] args)
            throws Throwable {
        Object result = method.invoke(this.localService, args);

        logger.info("NonTransactional committed: " + getLocalServiceMethodName(method));

        return result;
    }

    private String getLocalServiceMethodName(Method method) {
        return this.localService.getClass().getName() + "." + method.getName();
    }

    public void setEntityManagerFactory(EntityManagerFactory value) {
        this.entityManagerFactory = value;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return this.entityManagerFactory;
    }

    public void setLocalService(JPAService value) {
        this.localService = value;
    }

    public JPAService getLocalService() {
        return this.localService;
    }

    public void setInterceptorService(JPAInterceptorService interceptorService) {
        this.interceptorService = interceptorService;
    }

    public JPAInterceptorService getInterceptorService() {
        return this.interceptorService;
    }

    private boolean isTransactional(Method method) throws NoSuchMethodException {
        Method m = this.localService.getClass().getMethod(method.getName(), method.getParameterTypes());
        JPATransactional trx = (JPATransactional) m.getAnnotation(JPATransactional.class);
        if (trx == null) {
            return true;
        }
        return trx.value().equals(IsTransactional.TRANSACTIONAL);
    }
}
