/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.server.services;

import com.tdk.domain.Numerador;
import com.tdk.services.UtilServiceRemote;
import com.thorplatform.jpa.JPAService;
import com.thorplatform.utils.DateTimeUtils;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

/**
 *
 * @author fernando
 */
public class UtilServiceBean extends JPAService implements UtilServiceRemote {

    private static Monitor monitor = new Monitor();

    public Date getDiaHora() {
        synchronized (monitor) {
            DateTimeUtils util = getDateTimeUtils();
            Date date = Calendar.getInstance().getTime();
            String fmtDate = util.formatDateTime(date);
            Date parsed = util.parseDateTime(fmtDate);
            return parsed;
        }
    }

    public Long getNuevoNumero(Class clase) {
        synchronized (monitor) {
            Numerador numerador = null;
            numerador = recuperarNumeradorPorKey(clase.getName());
            if (numerador == null) {
                numerador = crearNumerador(clase.getName(), null);
            }
            Long nuevoNumero = numerador.incrementarNumeroActual();
            getEntityManager().merge(numerador);
            return nuevoNumero;
        }
    }

    public Numerador crearNumerador(String keyNumerador, Long inicializarConNumero) {
        Numerador numerador = new Numerador();
        numerador.setKeyNumerador(keyNumerador);

        if (inicializarConNumero == null) {
            numerador.setNumeroActual(0L);
        } else {
            numerador.setNumeroActual(inicializarConNumero);
        }
        getEntityManager().persist(numerador);

        return numerador;
    }

    private Numerador recuperarNumeradorPorKey(String keyNumerador) {
        Query query = getEntityManager().createQuery("select n from Numerador n where n.keyNumerador = :keyNumerador");
        query.setParameter("keyNumerador", keyNumerador);

        Numerador numerador = null;
        try {
            numerador = (Numerador) query.getSingleResult();

        } catch (NoResultException e) {
            numerador = null;
        } catch (NonUniqueResultException e) {
            throw new RuntimeException("Error: Existe mas de un Numerador con key " + keyNumerador);
        }

        return numerador;

    }
}

class Monitor {
}