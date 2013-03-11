/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdk.client.torneos.competidor;

import com.tdk.client.api.ServiceFactory;
import com.tdk.client.personas.institucion.AlumnoRootNode;
import com.tdk.client.utils.EstadoController;
import com.tdk.domain.Alumno;
import com.tdk.domain.Institucion;
import com.tdk.domain.PersonaFisica;
import com.tdk.domain.torneo.CategoriaForma;
import com.tdk.domain.torneo.CategoriaLucha;
import com.tdk.domain.torneo.Cinturon;
import com.tdk.domain.torneo.Competidor;
import com.tdk.domain.torneo.CompetidorCategoriaForma;
import com.tdk.domain.torneo.CompetidorCategoriaLucha;
import com.tdk.domain.torneo.EstadoCompetidor;
import com.tdk.domain.torneo.Peso;
import com.tdk.domain.torneo.TipoEstadoCompetidor;
import com.tdk.domain.torneo.Torneo;
import com.tdk.domain.torneo.TorneoInstitucion;
import com.tdk.services.PersonaServiceRemote;
import com.tdk.services.TorneoServiceRemote;
import com.tdk.services.UtilServiceRemote;
import com.tdk.utils.TDKServerException;
import com.thorplatform.swing.ChoiceField;
import com.thorplatform.swing.ChoiceRootNode;
import com.thorplatform.swing.DelegatingComboBoxModel;
import com.thorplatform.swing.ListProperty;
import com.thorplatform.swing.NodeBuilderFactory;
import com.thorplatform.swing.NodeFactory;
import com.thorplatform.swing.Property;
import com.thorplatform.swing.StrategySearchPattern;
import com.thorplatform.swing.SwingControllerChangeEvent;
import com.thorplatform.swing.SwingControllerFactory;
import com.thorplatform.swing.SwingModalController;
import com.thorplatform.swing.SwingNode;
import com.thorplatform.swing.ValidatorFilter;
import com.thorplatform.swing.validator.AbstractSwingValidator;
import com.thorplatform.swing.validator.RequiredPropertyValidator;
import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.openide.util.Lookup;

/**
 *
 * @author fernando
 */
public class CompetidorController extends SwingModalController implements SwingControllerChangeEvent {

    private CompetidorForm form = new CompetidorForm();
    private final Property<SwingNode<Alumno>> alumnoNode = new Property<SwingNode<Alumno>>("personaNode");
    private final Property<SwingNode<Institucion>> institucionNode = new Property<SwingNode<Institucion>>("institucionNode");
    private final Property<SwingNode<Torneo>> torneoNode = new Property<SwingNode<Torneo>>("torneoNode");
    private final Property<Cinturon> cinturonSelected = new Property<Cinturon>("cinturon");
    private final ListProperty<Cinturon> cinturones = new ListProperty<Cinturon>("cinturones");
    private final Property<CategoriaForma> categoriaFormaSelected = new Property<CategoriaForma>("categoriaFormaSelected");
    private final ListProperty<CategoriaForma> categoriasFormas = new ListProperty<CategoriaForma>("categoriasFormas");
    private final Property<CategoriaLucha> categoriaLuchaSelected = new Property<CategoriaLucha>("categoriaLuchaSelected");
    private final ListProperty<CategoriaLucha> categoriasLuchas = new ListProperty<CategoriaLucha>("categoriasLuchas");
    private final Property<Peso> pesoSelected = new Property<Peso>("pesoSelected");
    private final ListProperty<Peso> pesos = new ListProperty<Peso>("pesos");
    private final Property<Boolean> chkLucha = new Property<Boolean>("lucha");
    private final Property<Boolean> chkForma = new Property<Boolean>("forma");
    private EstadoController<EstadoCompetidor, TipoEstadoCompetidor> estadoLuchaController;
    private EstadoController<EstadoCompetidor, TipoEstadoCompetidor> estadoFormaController;
    private NodeBuilderFactory nodeBuilderFactory = Lookup.getDefault().lookup(NodeBuilderFactory.class);
    private SwingControllerFactory swingFactory = Lookup.getDefault().lookup(SwingControllerFactory.class);
    private NodeFactory<Alumno> alumnoNodeFactory = null;
    private NodeFactory<Institucion> institucionNodeFactory = null;
    private NodeFactory<Torneo> torneoNodeFactory = null;
    AlumnoRootNode crn = null;

    @Override
    protected JButton getAcceptButton() {
        return form.btnAceptar;
    }

    @Override
    protected JButton getCancelButton() {
        return form.btnCancelar;
    }

    @Override
    protected JPanel getForm() {
        return form;
    }

    @Override
    public void initController() {
        super.initController();
        installValidators();
        configureView();
        configureBingings();
        initPresentationModel();
    }

    @SuppressWarnings("unchecked")
    private void configureView() {
        categoriasFormas.assignData(getTorneoService().listarCategoriasForma("%"));
        categoriasLuchas.assignData(getTorneoService().listarCategoriasLucha("%"));
        cinturones.assignData(getTorneoService().listarCinturones("%"));

        form.cboCategoriaForma.setModel(new DelegatingComboBoxModel<CategoriaForma>(categoriasFormas.getList()));
        form.cboCategoriaLucha.setModel(new DelegatingComboBoxModel<CategoriaLucha>(categoriasLuchas.getList()));
        form.cboCinturon.setModel(new DelegatingComboBoxModel<Cinturon>(cinturones.getList()));
        form.cboPeso.setModel(new DelegatingComboBoxModel<Peso>(pesos.getList()));

        estadoFormaController = swingFactory.createController(EstadoController.class);
        estadoFormaController.setControllerChangeEvent(this);
        estadoFormaController.initEstadoController(Arrays.asList(TipoEstadoCompetidor.values()));
        form.pnlEstadoCategoriaForma.setLayout(new BorderLayout());
        form.pnlEstadoCategoriaForma.add(estadoFormaController.getPanel(), BorderLayout.CENTER);


        estadoLuchaController = swingFactory.createController(EstadoController.class);
        estadoLuchaController.setControllerChangeEvent(this);
        estadoLuchaController.initEstadoController(Arrays.asList(TipoEstadoCompetidor.values()));
        form.pnlEstadoCategoriaLucha.setLayout(new BorderLayout());
        form.pnlEstadoCategoriaLucha.add(estadoLuchaController.getPanel(), BorderLayout.CENTER);
    }

    @SuppressWarnings("unchecked")
    private void configureBingings() {
        getSwingBinder().bindComboBoxToObject(form.cboCategoriaForma, categoriaFormaSelected, categoriasFormas);
        getSwingBinder().bindComboBoxToObject(form.cboCategoriaLucha, categoriaLuchaSelected, categoriasLuchas);
        getSwingBinder().bindComboBoxToObject(form.cboCinturon, cinturonSelected, cinturones);
        getSwingBinder().bindComboBoxToObject(form.cboPeso, pesoSelected, pesos);

        getSwingBinder().bindCheckBoxToBoolean(form.chkForma, chkForma);
        getSwingBinder().bindCheckBoxToBoolean(form.chkLucha, chkLucha);

        alumnoNodeFactory = nodeBuilderFactory.createNodeFactory(Alumno.class);
        institucionNodeFactory = nodeBuilderFactory.createNodeFactory(Institucion.class);
        torneoNodeFactory = nodeBuilderFactory.createNodeFactory(Torneo.class);


        StrategySearchPattern<Alumno> strategyAlumno = new StrategySearchPattern<Alumno>() {

            public List<Alumno> strategySearch(String arg0) {
                try {
                    if (institucionNode.get() != null) {
                        return getPersonaService().listarAlumnosPorInstitucion(institucionNode.get().getValue().getId(), arg0);
                    } else {
                        getGuiUtils().warnnig("Seleccione una institución");
                    }
                } catch (Throwable ex) {
                    ex.printStackTrace();
                    getGuiUtils().warnnig(ex, TDKServerException.class);
                }

                return new ArrayList<Alumno>();
            }
        };

        //ChoiceRootNode crn = alumnoNodeFactory.createRootNode(strategyAlumno);
        crn = new AlumnoRootNode(strategyAlumno);
        crn.setValidatorFilter(new ValidatorFilter() {

            public boolean validate(String arg0) {
                return arg0 != null;
            }
        });

        ChoiceField<SwingNode<Alumno>> chAlumno = new ChoiceField<SwingNode<Alumno>>(form.txtPersona, "Selecciene un alumno", crn, alumnoNodeFactory.getNodeSelectedClass());
        chAlumno.addAnnounceSupportTag(Alumno.class);
        getSwingBinder().bindChoiceToObject(chAlumno, alumnoNode);
        
        StrategySearchPattern<Institucion> institucionSearch = new StrategySearchPattern<Institucion>() {

            public List<Institucion> strategySearch(String arg0) {
                if (torneoNode.get() != null && torneoNode.get().getValue() != null)  {
                    List<Institucion> instituciones = new ArrayList<Institucion>(); 
                    for(TorneoInstitucion ti : getPersonaService().listarInstitucionPorTorneo(torneoNode.get().getValue().getId(), arg0)) {
                        instituciones.add(ti.getInstitucion());
                    }
                    return instituciones;
                } else {
                    return new ArrayList<Institucion>();
                }
            }
        };

        ChoiceField<SwingNode<Institucion>> chInstitucion = new ChoiceField<SwingNode<Institucion>>(form.txtInstitucion, "Selecciene una institución", institucionNodeFactory.createRootNode(institucionSearch), institucionNodeFactory.getNodeSelectedClass());
        chInstitucion.addAnnounceSupportTag(Institucion.class);
        getSwingBinder().bindChoiceToObject(chInstitucion, institucionNode);

        ChoiceField<SwingNode<Torneo>> chTorneo = new ChoiceField<SwingNode<Torneo>>(form.txtTorneo, "Seleccione un torneo", torneoNodeFactory.createRootNode(), torneoNodeFactory.getNodeSelectedClass());
        chTorneo.addAnnounceSupportTag(Torneo.class);
        getSwingBinder().bindChoiceToObject(chTorneo, torneoNode);
    }

    private TorneoServiceRemote getTorneoService() {
        ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
        return sf.getService(TorneoServiceRemote.class);
    }

    private PersonaServiceRemote getPersonaService() {
        ServiceFactory sf = Lookup.getDefault().lookup(ServiceFactory.class);
        return sf.getService(PersonaServiceRemote.class);
    }

    private void initPresentationModel() {
        chkForma.set(false);
        chkLucha.set(false);
        institucionNode.set(null);
        torneoNode.set(null);
        alumnoNode.set(null);
        cinturonSelected.set(null);
    }

    private void cleanLucha() {
        categoriaLuchaSelected.set(null);
        pesoSelected.set(null);
        estadoLuchaController.setTipoEstado(null);
    }

    private void cleanForma() {
        categoriaFormaSelected.set(null);
        estadoFormaController.setTipoEstado(null);
    }

    private void installValidators() {
        getSwingValidator().setJLabel(form.lblMessage);
        getSwingValidator().addSwingValidator(new RequiredPropertyValidator(torneoNode, "Seleccione un torneo"));
        getSwingValidator().addSwingValidator(new RequiredPropertyValidator(institucionNode, "Seleccione una institución"));
        getSwingValidator().addSwingValidator(new RequiredPropertyValidator(alumnoNode, "Seleccione una persona"));
        getSwingValidator().addSwingValidator(new RequiredPropertyValidator(cinturonSelected, "Seleccione un cinturon"));
        getSwingValidator().addSwingValidator(new AbstractSwingValidator("Seleccione al menos una competencia a participar") {

            @Override
            public boolean validate() {
                return chkForma.get() != null && chkLucha.get() != null 
                        && (chkForma.get() == true
                        || chkLucha.get() == true);
            }
        });

        getSwingValidator().addSwingValidator(new AbstractSwingValidator("Seleccione una categoria de lucha") {

            @Override
            public boolean validate() {
                return chkLucha.get() != null && (chkLucha.get() == false) || (chkLucha.get() && categoriaLuchaSelected.get() != null);
            }
        });

        getSwingValidator().addSwingValidator(new AbstractSwingValidator("Seleccione un peso") {

            @Override
            public boolean validate() {
                return chkLucha.get() != null && (chkLucha.get() == false) || (
                        chkLucha.get() && categoriaLuchaSelected.get() != null && pesoSelected.get() != null);
            }
        });

        getSwingValidator().addSwingValidator(new AbstractSwingValidator("Seleccione una categoria forma") {

            @Override
            public boolean validate() {
                return chkForma.get() != null && (chkForma.get() == false) || (chkForma.get() && categoriaFormaSelected.get() != null);
            }
        });

    }

    //TODO:
    private void loadCategorias() {
        PersonaFisica p = alumnoNode.get().getValue().getPersonaFisica();
        
        Calendar birthdate = Calendar.getInstance();
        birthdate.setTime(p.getFechaNacimiento());
        
        Integer edad = getAge(birthdate);
        //Integer edad = getDateTimeUtils().countDayDiff(p.getFechaNacimiento(), getUtilService().getDiaHora());

        categoriasLuchas.assignData(getTorneoService().listarCategoriasLuchasPorSexoYEdad(p.getSexo(), edad));

        categoriasFormas.assignData(getTorneoService().listarCategoriasFormaPorEdad(edad));
    }

    private void setEnabledLucha(boolean enabled) {
        form.cboCategoriaLucha.setEnabled(enabled);
        form.cboPeso.setEnabled(enabled);
        if (!enabled) {
            cleanLucha();
        } else {
            if (estadoLuchaController.getTipoEstado() == null) {
                estadoLuchaController.setTipoEstado(TipoEstadoCompetidor.ACTIVO);  
            }
        }
    }

    private void setEnabledForma(boolean enabled) {
        form.cboCategoriaForma.setEnabled(enabled);
        if (!enabled) {
            cleanForma();
        } else {
            if (estadoFormaController.getTipoEstado() == null) {
                estadoFormaController.setTipoEstado(TipoEstadoCompetidor.ACTIVO);    
            }
        }
    }

    @Override
    protected void onPresentationModelChange(PropertyChangeEvent evt) {
        super.onPresentationModelChange(evt);
        if (evt.getSource() == chkForma && chkForma.get() != null) {
            setEnabledForma(chkForma.get());
        } else if (evt.getSource() == chkLucha && chkLucha.get() != null) {
            setEnabledLucha(chkLucha.get());
        } else if (evt.getSource() == categoriaLuchaSelected && categoriaLuchaSelected.get() != null) {
            pesos.assignData(categoriaLuchaSelected.get().getPesos());
        } else if (evt.getSource() == institucionNode) {
            form.txtPersona.setEnabled(institucionNode.get() != null);
        } else if (evt.getSource() == alumnoNode && alumnoNode.get() != null) {
            loadCategorias();
        }
    }

    public Competidor crearCompetidor() {
        return modificarCompetidor(new Competidor());
    }

    private CompetidorCategoriaForma mergeCategoriaForma(CompetidorCategoriaForma c) {
        if (chkForma.get()) {
            if (c == null) {
                c = new CompetidorCategoriaForma();
            }
            c.setCategoriaForma(categoriaFormaSelected.get());
            c.setEstadoCompetidor(crearEstadoCompetidor(c.getEstadoCompetidor(), estadoFormaController));
        } else {
            c = null;
        }

        return c;
    }

    private CompetidorCategoriaLucha mergeCategoriaLucha(CompetidorCategoriaLucha c) {
        if (chkLucha.get()) {
            if (c == null) {
                c = new CompetidorCategoriaLucha();
            }
            c.setPeso(pesoSelected.get());
            c.setEstadoCompetidor(crearEstadoCompetidor(c.getEstadoCompetidor(), estadoLuchaController));
        } else {
            c = null;
        }

        return c;
    }

    private EstadoCompetidor crearEstadoCompetidor(EstadoCompetidor estado, EstadoController<EstadoCompetidor, TipoEstadoCompetidor> estadoController) {
        if (estado == null) estado = new EstadoCompetidor();
        estado.setFechaDesde(estadoController.getFechaDesde());
        estado.setTipoEstadoCompetidor(estadoController.getTipoEstado());
        return estado;
    }

    public Competidor modificarCompetidor(Competidor competidor) {
        competidor.setAlumno(alumnoNode.get().getValue());
        competidor.setCinturon(cinturonSelected.get());
        competidor.setTorneo(torneoNode.get().getValue());
        competidor.setCompetidorCategoriaForma(mergeCategoriaForma(competidor.getCompetidorCategoriaForma()));
        competidor.setCompetidorCategoriaLucha(mergeCategoriaLucha(competidor.getCompetidorCategoriaLucha()));
        return competidor;
    }

    public void setTorneo(Torneo torneo) {
        torneoNode.set(torneoNodeFactory.createNode(torneo));
    }

    public void setInstitucion(Institucion institucion) {
        institucionNode.set(institucionNodeFactory.createNode(institucion));
        crn.setInstitucion(institucion);
        
    }

    public void notifyEvent(PropertyChangeEvent evt) {
        propertyChange(evt);
    }

    private UtilServiceRemote getUtilService() {
        return Lookup.getDefault().lookup(ServiceFactory.class).getService(UtilServiceRemote.class);
    }
    
    
    public void setAlumno(Alumno alumno) {
        alumnoNode.set(alumnoNodeFactory.createNode(alumno));
    }
    
    public void initCompetidorController(Competidor competidor) {
        setTorneo(competidor.getTorneo());
        setInstitucion(competidor.getAlumno().getInstitucion());
        setAlumno(competidor.getAlumno());
        cinturonSelected.set(competidor.getCinturon());
        
        setCompetenciaForma(competidor.getCompetidorCategoriaForma());
        setCompetenciaLucha(competidor.getCompetidorCategoriaLucha());
        
    }
    
    public void setCompetenciaLucha(CompetidorCategoriaLucha ccl) {
        chkLucha.set(ccl != null);
        
        if (ccl != null) {
            categoriaLuchaSelected.set(ccl.getPeso().getCategoriaLucha());
            pesoSelected.set(ccl.getPeso());
            estadoLuchaController.initEstadoController(ccl.getEstadoCompetidor(), ccl.getEstados());
        }
    }
    
    public void setCompetenciaForma(CompetidorCategoriaForma ccf) {
        chkForma.set(ccf != null);
        if (ccf != null) {
            categoriaFormaSelected.set(ccf.getCategoriaForma());
            estadoFormaController.initEstadoController(ccf.getEstadoCompetidor(), ccf.getEstados());
        }
    }
    
    private Integer getAge(Calendar birthdate) {
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - birthdate.get(Calendar.YEAR);
        if(today.get(Calendar.DAY_OF_YEAR)  < birthdate.get(Calendar.DAY_OF_YEAR)){
          age = age - 1; 
        }
        return new Integer(age);
    }

    @Override
    protected boolean acceptModalDialog() throws Exception {
        //return super.acceptModalDialog(); //To change body of generated methods, choose Tools | Templates.
         //Competidor competidor = crearCompetidor();
         competidor = getTorneoService().crearCompetidor(crearCompetidor());
         if (competidor == null) {
             throw new TDKServerException("No se pudo crear al competidor verifique los datos");
         }
         //agregarCompetidor(competidor);
         return true;
    }
    
    private Competidor competidor;
    
    public Competidor getCompetidorCreado() {
        return competidor;
    }
    
}
