/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.server.services;


import com.tdk.domain.security.Acceso;
import com.tdk.domain.security.FuncionalidadRol;
import com.tdk.domain.security.Funcionalidad;
import com.tdk.domain.security.KeywordUsuario;
import com.tdk.domain.security.Rol;
import com.tdk.domain.security.FuncionalidadUsuario;
import com.tdk.domain.security.Usuario;
import com.tdk.services.SecurityServiceRemote;
import com.tdk.utils.TDKServerException;
import com.thorplatform.jpa.IsTransactional;
import com.thorplatform.jpa.JPAService;
import com.thorplatform.jpa.JPATransactional;
import com.thorplatform.notifier.NotifierEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

/**
 *
 * @author fernando
 */
public class SecurityServiceBean extends JPAService implements SecurityServiceRemote {

    private static final long MAX_RESULT = 100;

    private void registerKeywordUsuario(Usuario u) {
        List<String> keywords = u.getKeywords();
        for (String key : keywords) {
            KeywordUsuario keyword = new KeywordUsuario();
            keyword.setKeyword(key);
            keyword.setUsuario(u);
            getEntityManager().persist(keyword);
        }
    }

    public void anRegisterKeywordUsuario(Long idUsuario) {
        Query query = getEntityManager().createQuery("delete from KeywordUsuario ku " +
                "where ku.usuario.id = :id");
        query.setParameter("id", idUsuario);
        query.executeUpdate();
    }

    public Usuario crearUsuario(Usuario usuario) {
        if (recuperarUsuario(usuario.getUserName()) != null) {
            throw new TDKServerException("<HTML>Existe un usuario con este nombre</TML>");
        }
        getEntityManager().persist(usuario);

        registerKeywordUsuario(usuario);

        getNotifier().mark(Usuario.class, NotifierEvent.ADD, usuario);
        return usuario;
    }

    public Usuario recuperarUsuario(String name) {
        Query query = getEntityManager().createQuery("select u from Usuario u where " +
                "u.userName = :user");
        query.setParameter("user", name);
        Usuario usuario = null;
        try {
            usuario = (Usuario) query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
        return usuario;
    }

    public void modificarUsuario(Usuario usuario) {
        Usuario other = recuperarUsuario(usuario.getUserName());
        if (!other.equals(usuario)) {
            throw new TDKServerException("<HTML> Ya existe un usuario " + usuario.getUserName() + "</HTML>");
        }
        getEntityManager().merge(usuario);
        anRegisterKeywordUsuario(usuario.getId());
        registerKeywordUsuario(usuario);

        getNotifier().mark(Usuario.class, NotifierEvent.UDPDATE, usuario);

    }

    public List<Usuario> listarUsuarios(String name) {
        if (name != null && name.trim().length() == 0) {
            throw new TDKServerException("Ingrese un filtro");
        }
        Query query = getEntityManager().createQuery("select count(distinct ku.usuario) from KeywordUsuario ku where " +
                "lower(ku.keyword) like :patron");
        query.setParameter("patron", STARTING_WITH_WILDCARD + name.toLowerCase() + STARTING_WITH_WILDCARD);
        Long count = (Long) query.getSingleResult();
        if (count > MAX_RESULT) {
            throw new TDKServerException("<HTML>Demacioados resultados, filtre con más datos</HTML>");
        } else if (count == 0) {
            throw new TDKServerException("<HTML>No hay resultados</HTML>");
        }

        query = getEntityManager().createQuery("select distinct ku.usuario from KeywordUsuario ku where " +
                "lower(ku.keyword) like :patron");
        query.setParameter("patron", STARTING_WITH_WILDCARD + name.toLowerCase() + STARTING_WITH_WILDCARD);
        return query.getResultList();
    }

    public Funcionalidad crearFuncionalidad(Funcionalidad funcionaliddad) {
        Funcionalidad other = recuperarFuncionalidadPorDescripcion(funcionaliddad.getDescripcion());
        if (other != null) {
            throw new TDKServerException("<HTML>Ya existe un permiso llamado " + funcionaliddad.getDescripcion() + "</HTML>");
        }
        getEntityManager().persist(funcionaliddad);

        return funcionaliddad;
    }

    public void modificarFuncionalidad(Funcionalidad funcionalidad) {
        Funcionalidad other = recuperarFuncionalidadPorDescripcion(funcionalidad.getDescripcion());
        if (other != null && other.equals(funcionalidad)) {
            throw new TDKServerException("<HTML>Ya existe un permiso llamado " + funcionalidad.getDescripcion() + "</HTML>");
        }
        getEntityManager().merge(funcionalidad);
    }

    public Funcionalidad recuperarFuncionalidad(Long idPermiso) {
        return getEntityManager().find(Funcionalidad.class, idPermiso);
    }

    public List<Funcionalidad> listarFuncionalidad(String descripcion) {
        if (descripcion != null && descripcion.trim().length() == 0) {
            throw new TDKServerException("Ingrese un filtro");
        }
            Query query = getEntityManager().createQuery("select count(p) from Funcionalidad p " +
                    "where lower(p.descripcion) like :patron");
            query.setParameter("patron", STARTING_WITH_WILDCARD + descripcion.toLowerCase() + STARTING_WITH_WILDCARD);
            Long count = (Long) query.getSingleResult();
            if (count > MAX_RESULT) {
                throw new TDKServerException("<HTML>Hay demaciados resultados</HTML>");
            } else if (count == 0) {
                throw new TDKServerException("<HTML>Nay resultados para la búsqueda efectuada</HTML>");
            }
            query = getEntityManager().createQuery("select p from Funcionalidad p " +
                    "where lower(p.descripcion) like :patron");
            query.setParameter("patron", STARTING_WITH_WILDCARD + descripcion.toLowerCase() + STARTING_WITH_WILDCARD);
            return query.getResultList();
    }

    public Funcionalidad recuperarFuncionalidadPorDescripcion(String descripcion) {
        Query query = getEntityManager().createQuery("select p from Funcionalidad p " +
                "where lower(p.descripcion) = :patron");
        query.setParameter("patron", descripcion.toLowerCase().trim());
        Funcionalidad permiso = null;
        try {
            permiso = (Funcionalidad) query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (NonUniqueResultException ex) {
            throw new TDKServerException("<HTML> Existen más de una funcionalidad con la misma descripción</HTML>");
        }

        return permiso;
    }

    public Rol crearRol(Rol rol) {
        Rol other = recuperarRolPorDescripcion(rol.getDescripcion());
        if (other != null) {
            throw new TDKServerException("<HTML>Ya existe un rol con la misma descripción</HTML>");
        }
        getEntityManager().persist(rol);
        return rol;
    }

    public void modificarRol(Rol rol) {
        Rol other = recuperarRolPorDescripcion(rol.getDescripcion());
        if (other != null && !other.equals(rol)) {
            throw new TDKServerException("<HTML>Ya existe un rol con la misma descripción</HTML>");
        }
        getEntityManager().merge(rol);
    }

    @JPATransactional(IsTransactional.NOT_TRANSACTIONAL)
    public Rol recuperarRolPorDescripcion(String descripcion) {
        Query query = getEntityManager().createQuery("select p from Rol p " +
                "where lower(p.descripcion) = :patron");
        query.setParameter("patron", descripcion.toLowerCase().trim());
        Rol rol = null;
        try {
            rol = (Rol) query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (NonUniqueResultException ex) {
            throw new TDKServerException("<HTML> Existen más de un rol con la misma descripción</HTML>");
        }

        return rol;
    }

    @JPATransactional(IsTransactional.NOT_TRANSACTIONAL)
    public List<Rol> listarRoles(String descripcion) {
        if (descripcion != null && descripcion.trim().length() == 0) {
            throw new TDKServerException("Ingrese un filtro");
        }
        Query query = getEntityManager().createQuery("select count(p) from Rol p " +
                "where lower(p.descripcion) like :patron");
        query.setParameter("patron", STARTING_WITH_WILDCARD + descripcion.toLowerCase() + STARTING_WITH_WILDCARD);
        Long count = (Long) query.getSingleResult();
        if (count > MAX_RESULT) {
            throw new TDKServerException("<HTML>Hay demaciados resultados</HTML>");
        } else if (count == 0) {
            throw new TDKServerException("<HTML>Nay resultados para la búsqueda efectuada</HTML>");
        }
        query = getEntityManager().createQuery("select r from Rol r " +
                "where lower(r.descripcion) like :patron order by r.descripcion");
        query.setParameter("patron", STARTING_WITH_WILDCARD + descripcion.toLowerCase() + STARTING_WITH_WILDCARD);
        return query.getResultList();
    }

    @JPATransactional(IsTransactional.NOT_TRANSACTIONAL)
    public List<FuncionalidadRol> listarFuncionesPorRol(Long idRol) {
        if (idRol == null) {
            throw new TDKServerException("No hay rol seleccionado para esta busqueda");
        }
        Query query = getEntityManager().createQuery("select p from FuncionalidadRol p " +
                "where p.rol.id = :idRol order by p.funcionalidad.descripcion");
        query.setParameter("idRol", idRol);
        return query.getResultList();
    }

    @JPATransactional(IsTransactional.NOT_TRANSACTIONAL)
    public List<FuncionalidadUsuario> listarFuncionalidadesPorUsuario(Long idUsuario) {
        if (idUsuario == null) {
            throw new TDKServerException("No hay usuario seleccionado para esta busqueda");
        }
        Query query = getEntityManager().createQuery("select p from FuncionalidadUsuario p " +
                "where p.usuario.id = :idUsuario");
        query.setParameter("idUsuario", idUsuario);
        return query.getResultList();
    }

    @JPATransactional(IsTransactional.NOT_TRANSACTIONAL)
    public Usuario login(String usuario, String password) {
        Query query = getEntityManager().createQuery("Select u from Usuario u " +
                "where u.userName = :user AND u.password = :psw");
        query.setParameter("user", usuario);
        query.setParameter("psw", password);
        Usuario user = null;
        try {
            user = (Usuario) query.getSingleResult();
        } catch (Throwable ex) {
            ex.printStackTrace();
            throw new TDKServerException("No existe un usuario con es nombre o clave");
        }
        return user;
       
    }

    @JPATransactional(IsTransactional.NOT_TRANSACTIONAL)
    public List<Acceso> recuperarPermiso(Usuario usuario, String funcionalidad) {
        FuncionalidadUsuario rolUsuario = recuperarFuncionalidadPorUsuario(usuario.getId(), funcionalidad);
        FuncionalidadRol funcionalidadRol = recuperarFuncionalidadPorRol(usuario.getRol().getId(), funcionalidad);
        List<Acceso> accesos = new ArrayList<Acceso>();

        if (rolUsuario == null && funcionalidadRol == null) {
            return accesos;
        } else if (rolUsuario != null && funcionalidadRol != null) {
            if (rolUsuario.getAlta() || funcionalidadRol.getAlta()) {
                accesos.add(Acceso.ALTA);
            }
            if (rolUsuario.getBaja() || funcionalidadRol.getBaja()) {
                accesos.add(Acceso.BAJA);
            }
            if (rolUsuario.getModificacion() || funcionalidadRol.getModificacion()) {
                accesos.add(Acceso.MODIFICACION);
            }
            if (rolUsuario.getConsulta() || funcionalidadRol.getConsulta()) {
                accesos.add(Acceso.CONSULTA);
            }
        } else if (rolUsuario != null) {
            accesos = mapAccesoRolPorUsuario(rolUsuario);
        } else if (funcionalidadRol != null) {
            accesos = mapAccesoFuncionalidadPorRol(funcionalidadRol);
        }


        return accesos;
    }

    @JPATransactional(IsTransactional.NOT_TRANSACTIONAL)
    private List<Acceso> mapAccesoRolPorUsuario(FuncionalidadUsuario rolUsuario) {
        List<Acceso> accesos = new ArrayList<Acceso>();

        if (rolUsuario.getAlta()) {
            accesos.add(Acceso.ALTA);
        }
        if (rolUsuario.getBaja()) {
            accesos.add(Acceso.BAJA);
        }
        if (rolUsuario.getModificacion()) {
            accesos.add(Acceso.MODIFICACION);
        }
        if (rolUsuario.getConsulta()) {
            accesos.add(Acceso.CONSULTA);
        }
        return accesos;
    }

    private List<Acceso> mapAccesoFuncionalidadPorRol(FuncionalidadRol funcionRol) {
        List<Acceso> accesos = new ArrayList<Acceso>();

        if (funcionRol.getAlta()) {
            accesos.add(Acceso.ALTA);
        }
        if (funcionRol.getBaja()) {
            accesos.add(Acceso.BAJA);
        }
        if (funcionRol.getModificacion()) {
            accesos.add(Acceso.MODIFICACION);
        }
        if (funcionRol.getConsulta()) {
            accesos.add(Acceso.CONSULTA);
        }
        return accesos;
    }

    public FuncionalidadUsuario recuperarFuncionalidadPorUsuario(Long idUsuario, String rol) {
        Query query = getEntityManager().createQuery("select pu from FuncionalidadUsuario pu " +
                "where pu.usuario.id = :id and lower(pu.funcionalidad.descripcion) like :funcionalidad");
        query.setParameter("id", idUsuario);
        query.setParameter("funcionalidad", rol.toLowerCase());
        FuncionalidadUsuario pu = null;
        try {
            pu = (FuncionalidadUsuario) query.getSingleResult();
        } catch (NonUniqueResultException ex) {
            throw new TDKServerException("Existe más de un funcionalidad para el mismo usurio");
        } catch (NoResultException ex) {
            return null;
        }

        return pu;
    }

    public FuncionalidadRol recuperarFuncionalidadPorRol(Long idRol, String funcionalidad) {
        Query query = getEntityManager().createQuery("select pf from FuncionalidadRol pf " +
                "where pf.rol.id = :id and lower(pf.funcionalidad.descripcion) like :funcionalidad");
        query.setParameter("id", idRol);
        query.setParameter("funcionalidad", funcionalidad.toLowerCase());
        FuncionalidadRol funcionRol = null;
        try {
            funcionRol = (FuncionalidadRol) query.getSingleResult();
        } catch (NonUniqueResultException ex) {
            throw new TDKServerException("Existe más de una funcionalidad para el mismo Rol");
        } catch (NoResultException ex) {
            return null;
        }
        return funcionRol;
    }

    public FuncionalidadRol crearFuncionalidadRol(FuncionalidadRol funcionalidadRol) {
        FuncionalidadRol other = recuperarFuncionalidadRol(funcionalidadRol.getFuncionalidad().getId(), funcionalidadRol.getRol().getId());
        if (other != null) {
            throw new TDKServerException("Ya existe el permiso " + funcionalidadRol.getFuncionalidad().getDescripcion() + " para el perfil " + funcionalidadRol.getRol().getDescripcion());
        }
        getEntityManager().persist(funcionalidadRol);
        return funcionalidadRol;
    }

    public void modificarFuncionalidadRol(FuncionalidadRol funcionalidadRol) {
        FuncionalidadRol other = recuperarFuncionalidadRol(funcionalidadRol.getFuncionalidad().getId(), funcionalidadRol.getRol().getId());
        if (other != null && !other.equals(funcionalidadRol)) {
            throw new TDKServerException("Ya existe la funcionalidad " + funcionalidadRol.getFuncionalidad().getDescripcion() + " para el Rol " + funcionalidadRol.getRol().getDescripcion());
        }
        getEntityManager().merge(funcionalidadRol);
    }

    public void crearFuncionalidadesRol(List<FuncionalidadRol> funcionalidadesRol) {
        if (funcionalidadesRol.isEmpty()) {
            throw new TDKServerException("La lista se encuentra vacia");
        }
        List<FuncionalidadRol> attachedList = listarFuncionesPorRol(funcionalidadesRol.get(0).getRol().getId());

        updateList(attachedList, funcionalidadesRol);

        for (FuncionalidadRol funcionalidadRol : funcionalidadesRol) {
            if (funcionalidadRol.getId() == null) {
                crearFuncionalidadRol(funcionalidadRol);
            } else {
                modificarFuncionalidadRol(funcionalidadRol);
            }
        }
    }

    public FuncionalidadUsuario crearRolUsuario(FuncionalidadUsuario rolUsuario) {
        FuncionalidadUsuario other = recuperarFuncionalidadUsuario(rolUsuario.getFuncionalidad().getId(), rolUsuario.getUsuario().getId());
        if (other != null) {
            throw new TDKServerException("Ya existe el permiso " + rolUsuario.getFuncionalidad().getDescripcion() + " para el usuario " + rolUsuario.getUsuario().getUserName());
        }
        getEntityManager().persist(rolUsuario);
        return rolUsuario;
    }

    public void modificarRolUsuario(FuncionalidadUsuario rolUsuario) {
        FuncionalidadUsuario other = recuperarFuncionalidadUsuario(rolUsuario.getFuncionalidad().getId(), rolUsuario.getUsuario().getId());
        if (other != null && !other.equals(rolUsuario)) {
            throw new TDKServerException("Ya existe el permiso " + rolUsuario.getFuncionalidad().getDescripcion() + " para el usuario " + rolUsuario.getUsuario().getUserName());
        }
        getEntityManager().merge(rolUsuario);
    }

    public void crearRolUsuario(List<FuncionalidadUsuario> permisos) {
        if (permisos.isEmpty()) {
            throw new TDKServerException("La lista se encuentra vacia");
        }
        List<FuncionalidadUsuario> attachedList = listarFuncionalidadesPorUsuario(permisos.get(0).getUsuario().getId());

        updateList(attachedList, permisos);

        for (FuncionalidadUsuario rolUsuario : permisos) {
            if (rolUsuario.getId() == null) {
                crearRolUsuario(rolUsuario);
            } else {
                modificarRolUsuario(rolUsuario);
            }
        }
    }

    public FuncionalidadRol recuperarFuncionalidadRol(Long idFuncionalidad, Long idRol) {
        Query query = getEntityManager().createQuery("select pp from FuncionalidadRol pp " +
                "where pp.funcionalidad.id = :idFuncionalidad and pp.rol.id = :idRol");
        query.setParameter("idFuncionalidad", idFuncionalidad);
        query.setParameter("idRol", idRol);
        FuncionalidadRol pp = null;
        try {
            pp = (FuncionalidadRol) query.getSingleResult();
        } catch (NonUniqueResultException ex) {
            throw new TDKServerException("Existe más de un resultado para la funcion y rol dados");
        } catch (NoResultException ex) {
            return null;
        }

        return pp;
    }

    public FuncionalidadUsuario recuperarFuncionalidadUsuario(Long idFuncionalidad, Long idUsuario) {
        Query query = getEntityManager().createQuery("select pp from FuncionalidadUsuario pp " +
                "where pp.funcionalidad.id = :idFuncionalidad and pp.usuario.id = :idUsuario");
        query.setParameter("idFuncionalidad", idFuncionalidad);
        query.setParameter("idUsuario", idUsuario);
        FuncionalidadUsuario funcionalidadUsuario = null;
        try {
            funcionalidadUsuario = (FuncionalidadUsuario) query.getSingleResult();
        } catch (NonUniqueResultException ex) {
            throw new TDKServerException("Existe más de un resultado para el usuario y funcionalidad dados");
        } catch (NoResultException ex) {
            return null;
        }

        return funcionalidadUsuario;
    }

   

    }
