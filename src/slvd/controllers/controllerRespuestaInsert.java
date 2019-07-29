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

import com.liferay.portal.kernel.util.ParamUtil;

import slvd.dao.IndicadorHome;
import slvd.database.Asignatura;
import slvd.database.Pregunta;
import slvd.database.Respuesta;

public class controllerRespuestaInsert extends AbstractController {

	IndicadorHome dao;

	@Override
	public void handleActionRequest(ActionRequest request,
			ActionResponse response) throws Exception {

		String btnContinuar = ParamUtil.get(request, "button_continuar",
				"default");
		String btnCancelar = ParamUtil.get(request, "button_cancelar",
				"default");
		String idA = request.getParameter("asignatura");
		request.setAttribute("idAsig", idA);
		if (!btnContinuar.equalsIgnoreCase("default")) {
			Asignatura a = dao.listaAsignxId(Integer.parseInt(idA));

			String pregunta = request.getParameter("pregunta");
			String tipopregunta = request.getParameter("tipopregunta");
			String nameFile = request.getParameter("imagenFile");
			
			String idpregunta = request.getParameter("idP");
			if(!idpregunta.equalsIgnoreCase("0")){
				Pregunta p = dao.pregunta(Integer.parseInt(idpregunta));
				p.setAsignatura(a);
				p.setPregunta(pregunta);
				p.setTipoPregunta(tipopregunta);
				p.setImagenFile(nameFile);
				dao.modificarPregunta(p);
				
				String[] opcs = request.getParameterValues("txt_opcion[]");
				String[] peso = request.getParameterValues("txt_peso[]");
				
				List<Respuesta> listaRpta = dao.listaRpta(p);
				int longL = listaRpta.size();
				for (int i = 0; i < opcs.length; i++) {
					if(i<longL){
						listaRpta.get(i).setPregunta(p);
						listaRpta.get(i).setRespuesta(opcs[i]);
						listaRpta.get(i).setPeso(Float.valueOf(peso[i]));
						dao.modificarRespuesta(listaRpta.get(i));
					}
					else{
						Respuesta rpta = new Respuesta();
						rpta.setPregunta(p);
						rpta.setRespuesta(opcs[i]);
						rpta.setPeso(Float.valueOf(peso[i]));
						dao.insertarRespuesta(rpta);
					}
				}
			}
			else{
				Pregunta p = new Pregunta();
				p.setAsignatura(a);
				p.setPregunta(pregunta);
				p.setTipoPregunta(tipopregunta);
				p.setImagenFile(nameFile);
				dao.insertarPregunta(p);

				Pregunta preg = dao.listaPregNomb(pregunta, tipopregunta);
				String[] opcs = request.getParameterValues("txt_opcion[]");
				String[] peso = request.getParameterValues("txt_peso[]");

				for (int i = 0; i < opcs.length; i++) {
					Respuesta rpta = new Respuesta();
					rpta.setPregunta(preg);
					rpta.setRespuesta(opcs[i]);
					rpta.setPeso(Float.valueOf(peso[i]));
					dao.insertarRespuesta(rpta);
				}
			}			
		} else if (!btnCancelar.equalsIgnoreCase("default")) {
			System.out.println("bueno cancelo y no se annade nada");
		}
	}

	@Override
	public ModelAndView handleRenderRequest(RenderRequest request,
			RenderResponse response) throws Exception {
		Map<String, Object> aux = new HashMap<String, Object>();
		String path = request.getContextPath();
		aux.put("path", path);
		String idAs = (String) request.getAttribute("idAsig");
		int idasign = Integer.parseInt(idAs);
		Asignatura asig = dao.listaAsignxId(idasign);
		List<Pregunta> lista = dao.listarPregAsign(asig);
		int cantPreg = lista.size();
		aux.put("asignatura", asig);
		aux.put("pregunta", lista);
		aux.put("cantPreg", cantPreg);
		return new ModelAndView("cuestionarioGestion", "aux", aux);
	}

	public IndicadorHome getDao() {
		return dao;
	}

	public void setDao(IndicadorHome dao) {
		this.dao = dao;
	}

}
