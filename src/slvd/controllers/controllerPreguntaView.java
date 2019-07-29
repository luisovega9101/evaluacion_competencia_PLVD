package slvd.controllers;

import java.util.HashMap;
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

public class controllerPreguntaView extends AbstractController {

	IndicadorHome dao;

	@Override
	public void handleActionRequest(ActionRequest request,
			ActionResponse response) throws Exception {
				
	}

	@Override
	public ModelAndView handleRenderRequest(RenderRequest request,
			RenderResponse response) throws Exception {
		Map<String, Object> aux = new HashMap<String, Object>();
		String path = request.getContextPath();
		aux.put("path", path);
		String idP = request.getParameter("idpregunta");
		int idasign = Integer.parseInt(request.getParameter("id_asignatura"));
		Asignatura asig = dao.listaAsignxId(idasign);
		Pregunta p = dao.pregunta(Integer.parseInt(idP));
		List<Respuesta> lista = dao.listaRpta(p);
		aux.put("asignatura", asig);
		aux.put("pregunta", p);
		aux.put("rpta", lista);
		return new ModelAndView("preguntaView", "aux", aux);
	}

	public IndicadorHome getDao() {
		return dao;
	}

	public void setDao(IndicadorHome dao) {
		this.dao = dao;
	}
}