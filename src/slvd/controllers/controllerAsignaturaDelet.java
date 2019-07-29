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

public class controllerAsignaturaDelet extends AbstractController {

	private IndicadorHome dao;
	
	@Override
	public void handleActionRequest(ActionRequest request,
			ActionResponse response) throws Exception {	
		
	}

	@Override
	public ModelAndView handleRenderRequest(RenderRequest request,
			RenderResponse response) throws Exception {
		Integer idAsign = Integer.parseInt(request.getParameter("id_asignatura"));
		dao.eliminarAsignatura(idAsign);
		List<Asignatura> asignatura = dao.listarAsign();
		int cantAsig = asignatura.size();
		Map<String, Object> aux = new HashMap<String, Object>();
		String path = request.getContextPath();
		aux.put("asignatura", asignatura);
		aux.put("cantAsig", cantAsig);
		aux.put("path", path);
		return new ModelAndView("asignaturaAdmin", "aux", aux);
	}

	public IndicadorHome getDao() {
		return dao;
	}

	public void setDao(IndicadorHome dao) {
		this.dao = dao;
	}
}
