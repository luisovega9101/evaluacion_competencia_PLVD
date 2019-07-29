package slvd.controllers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

import slvd.dao.IndicadorHome;
import slvd.database.AnalisisEstatico;
import slvd.database.Asignatura;
import slvd.database.Indicador;
import slvd.database.Pregunta;
import slvd.database.Respuesta;

public class controllerEvaluacion extends AbstractController {

	private IndicadorHome dao;

	@Override
	public void handleActionRequest(ActionRequest request,
			ActionResponse response) throws Exception {
	}

	@Override
	public ModelAndView handleRenderRequest(RenderRequest request,
			RenderResponse response) throws Exception {
		String idexperto = request.getParameter("idexperto");
		String nombrusuario = request.getParameter("nombusuario");
		int idasign = Integer.parseInt(request.getParameter("id_asignatura"));
		Asignatura asig = dao.listaAsignxId(idasign);
		Map<String, Object> aux = new HashMap<String, Object>();
		String path = request.getContextPath();
		aux.put("idexperto", idexperto);
		aux.put("nombusuario", nombrusuario);
		aux.put("path", path);
		List<Pregunta> listaPreg = dao.listarPregAsign(asig);
		if (listaPreg.size() == 0) {
			List<Asignatura> asignatura = dao.listarAsign();
			String txt = "No existen preguntas para la realización del cuestionario de esa asignatura";
			aux.put("txt", txt);
			aux.put("asignatura", asignatura);
			String sms = "Seleccione una asignatura para la evaluación de competencias";
			String accion = "evaluacion";
			aux.put("sms", sms);
			aux.put("accion", accion);
			return new ModelAndView("seleccionAsign", "aux", aux);
		}
		List<Indicador> indicadores = dao.listarIndicxAsign(asig);
		if (indicadores.size() != 0) {
			List<AnalisisEstatico> listaAE = dao.listaAnalisis(indicadores);
			if (listaAE.size() == 0) {
				List<Asignatura> asignatura = dao.listarAsign();
				String txt = "Aún no se encuentra habilitado el cuestionario";
				aux.put("txt", txt);
				aux.put("asignatura", asignatura);
				String sms = "Seleccione una asignatura para la evaluación de competencias";
				String accion = "evaluacion";
				aux.put("sms", sms);
				aux.put("accion", accion);
				return new ModelAndView("seleccionAsign", "aux", aux);
			}
		} else {
			List<Asignatura> asignatura = dao.listarAsign();
			String txt = "Aún no se encuentra habilitado el cuestionario";
			aux.put("txt", txt);
			aux.put("asignatura", asignatura);
			String sms = "Seleccione una asignatura para la evaluación de competencias";
			String accion = "evaluacion";
			aux.put("sms", sms);
			aux.put("accion", accion);
			return new ModelAndView("seleccionAsign", "aux", aux);
		}
		List<Respuesta> listaRpta = new LinkedList<Respuesta>();
		for (Pregunta pregunta : listaPreg) {
			List<Respuesta> r = dao.listaRpta(pregunta);
			for (Respuesta respuesta : r) {
				listaRpta.add(respuesta);
			}
		}
		aux.put("asignatura", asig);
		aux.put("pregunta", listaPreg);
		aux.put("listaRpta", listaRpta);
		return new ModelAndView("evaluacionPract", "aux", aux);
	}

	public IndicadorHome getDao() {
		return dao;
	}

	public void setDao(IndicadorHome dao) {
		this.dao = dao;
	}
}
