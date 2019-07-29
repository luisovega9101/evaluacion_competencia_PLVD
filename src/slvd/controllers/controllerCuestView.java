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
import slvd.database.Asignatura;
import slvd.database.Pregunta;
import slvd.database.Respuesta;

public class controllerCuestView extends AbstractController {

	private IndicadorHome dao;
	
	@Override
	public void handleActionRequest(ActionRequest request,
			ActionResponse response) throws Exception {	
	}

	@Override
	public ModelAndView handleRenderRequest(RenderRequest request,
			RenderResponse response) throws Exception {			
		int idasign = Integer.parseInt(request.getParameter("id_asignatura"));	
		Asignatura asig = dao.listaAsignxId(idasign);
		Map<String, Object> aux = new HashMap<String, Object>();
		String path = request.getContextPath();
		List<Respuesta> listaRpta = new LinkedList<Respuesta>();
		List<Pregunta> listaPreg = dao.listarPregAsign(asig);
		for (Pregunta pregunta : listaPreg) {
			List<Respuesta> r = dao.listaRpta(pregunta);
			for (Respuesta respuesta : r) {
				listaRpta.add(respuesta);
			}
		}			
		aux.put("asignatura", asig);
		aux.put("pregunta", listaPreg);
		aux.put("path", path);	
		aux.put("listaRpta", listaRpta);
		
		return new ModelAndView("cuestView", "aux", aux);
	}

	public IndicadorHome getDao() {
		return dao;
	}

	public void setDao(IndicadorHome dao) {
		this.dao = dao;
	}
}
