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

import com.liferay.portal.kernel.util.ParamUtil;

import slvd.dao.IndicadorHome;
import slvd.database.AnalisisEstatico;
import slvd.database.Asignatura;
import slvd.database.Evaluacion;
import slvd.database.Indicador;
import slvd.database.Respuesta;
import slvd.database.Resultado;
import slvd.database.Usuario;

public class controllerEvaluacionInsert extends AbstractController {

	IndicadorHome dao;

	@Override
	public void handleActionRequest(ActionRequest request,
			ActionResponse response) throws Exception {
		String btnEnviar = ParamUtil.get(request, "button_enviar", "default");
		String idusuario = request.getParameter("idexperto");
		String nombusuario = request.getParameter("nombusuario");
		
		String idA = request.getParameter("id_asignatura");
		request.setAttribute("iduser", idusuario);
		request.setAttribute("nombusuario", nombusuario);
		request.setAttribute("idAsig", idA);
		
		List<Usuario> listuser = dao.buscarUsuario(idusuario, nombusuario);
		if(listuser.size()!=0){
			dao.eliminaResultado(listuser.get(0));
			dao.eliminarEvaluacion(listuser.get(0));
		}

		String cantidad = request.getParameter("cantidad");
		if (!btnEnviar.equalsIgnoreCase("default")) {
			Asignatura a = dao.listaAsignxId(Integer.parseInt(idA));
			List<Usuario> listauser = dao.buscarUsuario(idusuario, nombusuario);
			Usuario user;
			if(listauser.size()==0){
				Usuario u = new Usuario();
				u.setUsuario(idusuario);
				u.setNombreUsuario(nombusuario);
				dao.insertarUsuario(u);
				user = dao.buscarUsuario(idusuario, nombusuario).get(0);
			}
			else{
				user= listauser.get(0);
			}			
			String p;
			String[] r;
			List<Float> pesos = new LinkedList<Float>();
			int cant = Integer.parseInt(cantidad);
			for (int i = 1; i <= cant; i++) {
				p = request.getParameter("pregunta"+i);
				r = request.getParameterValues("rpta"+i);
				System.out.println("esta es la asign: "+a.getNombreAsignatura());
				System.out.println("este es el usuario: "+idusuario);
				System.out.println("Esta es la pregunta: "+p);
				float peso = 0;
				for (int j = 0; j < r.length; j++) {
					System.out.println("esta es la rpta asociada: "+r[j]);
					Respuesta rpta = dao.respuesta(Integer.parseInt(r[j]));
					Resultado result = new Resultado();
					result.setUsuario(user);
					result.setRespuesta(rpta);
					dao.insertarResultado(result);
					System.out.println("resultado insertado");
					peso+= rpta.getPeso();
				}
				System.out.println();				
				pesos.add(peso);
			}
			List<Float> gradoSN = new LinkedList<Float>();
			
			List<Indicador> indicadores = dao.listarIndicxAsign(a);
			List<AnalisisEstatico> listaAE = dao.listaAnalisis(indicadores);	
			for (AnalisisEstatico analisisEstatico : listaAE) {
				gradoSN.add(analisisEstatico.getGradoSalidaNormalizado());
			}
			
			List<Float> valores = new LinkedList<Float>();
			for (int i = 0; i < pesos.size(); i++) {
				float valor=0;
				for (int j = 0; j < gradoSN.size(); j++) {
					valor+= gradoSN.get(j)*pesos.get(i);
				}
				valores.add(valor);
			}
			
			float sumaValores=0;
			int cantValores = valores.size();
			for (Float float1 : valores) {
				sumaValores+=float1;
			}
			
			float promedioValores = sumaValores/cantValores;
			String inferencia = "";
			
			Evaluacion e = new Evaluacion();
			e.setPromedioValor(promedioValores);
			e.setUsuario(user);
			if(promedioValores<=0.33){
				System.out.println("Deficiente");
				inferencia = "Deficiente";
				e.setEvaluacion(inferencia);
				dao.insertarEvaluacion(e);				
			}
			else if(promedioValores>=0.34 && promedioValores<=0.66){
				System.out.println("Adecuado");
				inferencia = "Adecuado";
				e.setEvaluacion(inferencia);
				dao.insertarEvaluacion(e);
			}
			else if (promedioValores>=0.67){
				System.out.println("Superior");
				inferencia = "Superior";
				e.setEvaluacion(inferencia);
				dao.insertarEvaluacion(e);
			}
			request.setAttribute("inferencia", inferencia);
			
		}
	}

	@Override
	public ModelAndView handleRenderRequest(RenderRequest request,
			RenderResponse response) throws Exception {
		Map<String, Object> aux = new HashMap<String, Object>();	
		String idAsig = (String) request.getAttribute("idAsig");
		int idasign = Integer.parseInt(idAsig);
		String idusr = (String) request.getAttribute("iduser");
		String nombusuario = (String) request.getAttribute("nombusuario");
		String path = request.getContextPath();
		
		Asignatura asig = dao.listaAsignxId(idasign);	
		Usuario user = dao.buscarUsuario(idusr, nombusuario).get(0);
		Evaluacion eval = dao.buscarEvaluacion(user).get(0);
		aux.put("path", path);		
		aux.put("user", user);		
		aux.put("eval", eval);	
		aux.put("asig", asig);	
		return new ModelAndView("resultEvaluac", "aux", aux);
	}

	public IndicadorHome getDao() {
		return dao;
	}

	public void setDao(IndicadorHome dao) {
		this.dao = dao;
	}

}
