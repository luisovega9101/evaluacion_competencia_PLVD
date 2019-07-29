package slvd.dao;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import slvd.database.AnalisisEstatico;
import slvd.database.Asignatura;
import slvd.database.Evaluacion;
import slvd.database.Pregunta;
import slvd.database.Indicador;
import slvd.database.IndicadorIndicador;
import slvd.database.MatrizAdyacencia;
import slvd.database.MatrizAdyacenciaAsociada;
import slvd.database.Respuesta;
import slvd.database.Resultado;
import slvd.database.Usuario;
import slvd.database.VariableLenguistica;
import slvd.hibernate.utiles.HibernateUtil;

public class IndicadorHome {

	private static final Log log = LogFactory.getLog(IndicadorHome.class);
	private Session sf = HibernateUtil.getSessionFactory().openSession();

	public List<Asignatura> listarAsign() {
		log.info("getting listarAsign ALL");
		System.out.println("getting listarAsign ALL");
		try {
			List<Asignatura> listarAsing = sf.createCriteria(Asignatura.class)
					.list();
			return listarAsing;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Asignatura listaAsignxId(int idAsign) {
		log.info("getting listarIndic ALL");
		System.out.println("getting listarIndic ALL");
		try {
			Asignatura asignat = (Asignatura) sf
					.createCriteria(Asignatura.class)
					.add(Restrictions.idEq(idAsign)).list().get(0);
			return asignat;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Asignatura> listaAsignxNomb(String asign) {
		log.info("getting listaAsignxNomb ALL");
		System.out.println("getting listaAsignxNomb ALL");
		try {
			List<Asignatura> asignat = sf
					.createCriteria(Asignatura.class)
					.add(Restrictions.eq("nombreAsignatura", asign)).list();
			return asignat;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public void insertarAsignatura(Asignatura asignatura) {
		try {
			Transaction transaction = sf.beginTransaction();
			sf.save(asignatura);
			transaction.commit();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public void modificarAsignatura(Asignatura asignatura) {
		log.info("getting ModifAsign ALL");
		System.out.println("getting ModifAsign ALL");
		try {
			Transaction transaction = sf.beginTransaction();
			sf.update(asignatura);
			transaction.commit();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public void eliminarAsignatura(int id) {
		log.info("getting eliminarAsignatura ALL");
		System.out.println("getting eliminarAsignatura ALL");
		try {
			sf.beginTransaction();
			Asignatura asign = (Asignatura) sf.load(Asignatura.class, id);
			List<Indicador> listaI = this.listarIndicxAsign(asign);
			for (Indicador indicador : listaI) {
				this.deleteAnalEst(indicador);

				List<IndicadorIndicador> listaE = this
						.obtenerIndIndE(indicador);
				for (IndicadorIndicador indicadorIndicadorE : listaE) {
					this.deleteMatrizAdAs(indicadorIndicadorE);
					this.deleteMatrizAd(indicadorIndicadorE);
					sf.delete(indicadorIndicadorE);
				}

				List<IndicadorIndicador> listaS = this
						.obtenerIndIndS(indicador);
				for (IndicadorIndicador indicadorIndicadorS : listaS) {
					this.deleteMatrizAdAs(indicadorIndicadorS);
					this.deleteMatrizAd(indicadorIndicadorS);
					sf.delete(indicadorIndicadorS);
				}
				sf.delete(indicador);
			}
			List<Pregunta> listarPreg = this.listarPregAsign(asign);
			for (Pregunta pregunta : listarPreg) {
				this.deleteById(pregunta.getIdPregunta());
			}
			sf.delete(asign);
			sf.getTransaction().commit();
			log.info("delete successful");
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public void deleteMatrizAd(IndicadorIndicador indInd) {
		log.debug("deleting deleteMatrizAdAs instance");
		try {
			List<MatrizAdyacencia> mad = sf
					.createCriteria(MatrizAdyacencia.class)
					.add(Restrictions.eq("indicadorIndicador", indInd)).list();
			if (mad != null) {
				for (MatrizAdyacencia matrizAdyacencia : mad) {
					sf.delete(matrizAdyacencia);
				}
			}
			log.info("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public void deleteMatrizAdAs(IndicadorIndicador indInd) {
		log.debug("deleting deleteMatrizAdAs instance");
		try {
			List<MatrizAdyacenciaAsociada> madas = sf
					.createCriteria(MatrizAdyacenciaAsociada.class)
					.add(Restrictions.eq("indicadorIndicador", indInd)).list();
			if (madas != null) {
				for (MatrizAdyacenciaAsociada matrizAdyacenciaAsociada : madas) {
					sf.delete(matrizAdyacenciaAsociada);
				}
			}
			log.info("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public List<IndicadorIndicador> obtenerIndIndE(Indicador idIndE) {
		log.info("getting obtenerIndIndE ALL");
		System.out.println("getting obtenerIndIndE ALL");
		try {
			return sf
					.createCriteria(IndicadorIndicador.class)
					.add(Restrictions.eq("indicadorByIdIndicadorEntrada",
							idIndE)).list();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<IndicadorIndicador> obtenerIndIndS(Indicador idIndS) {
		log.info("getting obtenerIndIndS ALL");
		System.out.println("getting obtenerIndIndS ALL");
		try {
			return sf
					.createCriteria(IndicadorIndicador.class)
					.add(Restrictions
							.eq("indicadorByIdIndicadorSalida", idIndS)).list();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public void deleteAnalEst(Indicador indic) {
		log.debug("deleting deleteAnalEst instance");
		try {
			List<AnalisisEstatico> ae = sf
					.createCriteria(AnalisisEstatico.class)
					.add(Restrictions.eq("indicador", indic)).list();
			if(ae.size()!=0)
				sf.delete(ae.get(0));
			log.info("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public List<Indicador> listarIndic() {
		log.info("getting listarIndic ALL");
		System.out.println("getting listarIndic ALL");
		try {
			List<Indicador> listarInd = sf.createCriteria(Indicador.class)
					.addOrder(Order.asc("idIndicador")).list();
			return listarInd;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Indicador> listarIndicxAsign(Asignatura asign) {
		log.info("getting listarIndic ALL");
		System.out.println("getting listarIndic ALL");
		try {
			List<Indicador> listarInd = sf.createCriteria(Indicador.class)
					.add(Restrictions.eq("asignatura", asign))
					.addOrder(Order.asc("idIndicador")).list();
			return listarInd;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public List<Indicador> listarIndicxAsignxDescrip(Asignatura asign, String descrip) {
		log.info("getting listarIndic ALL");
		System.out.println("getting listarIndic ALL");
		try {
			List<Indicador> listarInd = sf.createCriteria(Indicador.class)
					.add(Restrictions.eq("asignatura", asign))
					.add(Restrictions.eq("descripcionIndicador", descrip)).list();
			return listarInd;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Indicador listaIndicxId(int idInd) {
		log.info("getting listarIndic ALL");
		System.out.println("getting listarIndic ALL");
		try {
			Indicador indicad = (Indicador) sf.createCriteria(Indicador.class)
					.add(Restrictions.idEq(idInd)).list().get(0);
			return indicad;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public void insertarIndicador(Indicador indicador) {
		try {
			Transaction transaction = sf.beginTransaction();
			sf.save(indicador);
			transaction.commit();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public void modificarIndicador(Indicador indicador) {
		log.info("getting ModifInd ALL");
		System.out.println("getting ModifInd ALL");
		try {
			Transaction transaction = sf.beginTransaction();
			sf.update(indicador);
			transaction.commit();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public void eliminarIndicador(int id) {
		log.info("getting eliminarIndicador ALL");
		System.out.println("getting eliminarIndicador ALL");
		try {
			sf.beginTransaction();
			Indicador indicador = (Indicador) sf.load(Indicador.class, id);
			this.deleteAnalEst(indicador);

			List<IndicadorIndicador> listaE = this.obtenerIndIndE(indicador);
			for (IndicadorIndicador indicadorIndicadorE : listaE) {
				this.deleteMatrizAdAs(indicadorIndicadorE);
				this.deleteMatrizAd(indicadorIndicadorE);
				sf.delete(indicadorIndicadorE);
			}

			List<IndicadorIndicador> listaS = this.obtenerIndIndS(indicador);
			for (IndicadorIndicador indicadorIndicadorS : listaS) {
				this.deleteMatrizAdAs(indicadorIndicadorS);
				this.deleteMatrizAd(indicadorIndicadorS);
				sf.delete(indicadorIndicadorS);
			}
			sf.delete(indicador);
			sf.getTransaction().commit();
			log.info("delete successful");
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public void eliminarIndicadorCorrelac(int id) {
		log.info("getting eliminarIndicadorCorrelac ALL");
		System.out.println("getting eliminarIndicadorCorrelac ALL");
		try {
			sf.beginTransaction();
			Indicador indicador = (Indicador) sf.load(Indicador.class, id);
			this.deleteAnalEst(indicador);

			List<IndicadorIndicador> listaE = this.obtenerIndIndE(indicador);
			for (IndicadorIndicador indicadorIndicadorE : listaE) {
				this.deleteMatrizAdAs(indicadorIndicadorE);
				this.deleteMatrizAd(indicadorIndicadorE);
				sf.delete(indicadorIndicadorE);
			}

			List<IndicadorIndicador> listaS = this.obtenerIndIndS(indicador);
			for (IndicadorIndicador indicadorIndicadorS : listaS) {
				this.deleteMatrizAdAs(indicadorIndicadorS);
				this.deleteMatrizAd(indicadorIndicadorS);
				sf.delete(indicadorIndicadorS);
			}			
			sf.getTransaction().commit();
			log.info("delete successful");
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public BigInteger maxId() {
		log.info("getting MdCDC maxId");
		try {
			BigInteger id = (BigInteger) sf
					.createSQLQuery(
							"select nextval('indicador_id_indicador_seq')")
					.list().get(0);
			return id;
		} catch (HibernateException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<AnalisisEstatico> listaAnalisis(List<Indicador> listaIndic) {
		log.info("getting listaAnalisis ALL");
		System.out.println("getting listaAnalisis ALL");
		try {
			List<AnalisisEstatico> lista = new LinkedList<AnalisisEstatico>();
			for (Indicador indicador : listaIndic) {
				AnalisisEstatico ae = (AnalisisEstatico) sf
						.createCriteria(AnalisisEstatico.class)
						.add(Restrictions.eq("indicador", indicador)).list()
						.get(0);
				lista.add(ae);
			}
			return lista;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public void insertaAnalisisEstatico(Indicador indicador, float id,
			float od, float idN, float odN, float c, float cN) {
		log.info("getting insertaAnalisisEstatico ALL");
		System.out.println("getting insertaAnalisisEstatico ALL");
		try {
			Transaction transaction = (Transaction) sf.beginTransaction();
			AnalisisEstatico ae = new AnalisisEstatico();
			ae.setIndicador(indicador);
			ae.setGradoEntrada(id);
			ae.setGradoSalida(od);
			ae.setGradoEntradaNormalizado(idN);
			ae.setGradoSalidaNormalizado(odN);
			ae.setCentralidad(c);
			ae.setCentralidadNormalizada(cN);

			List<AnalisisEstatico> analisis = sf
					.createCriteria(AnalisisEstatico.class)
					.add(Restrictions.eq("indicador", indicador)).list();

			if (analisis.size() == 0) {
				sf.save(ae);
			} else {
				ae = (AnalisisEstatico) sf.load(AnalisisEstatico.class,
						analisis.get(0).getIdAnalisis());
				sf.update(ae);
			}
			transaction.commit();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public void insertIndicIndic(Indicador indicE, Indicador indicS) {
		try {
			Transaction transaction = sf.beginTransaction();
			List<IndicadorIndicador> i = sf
					.createCriteria(IndicadorIndicador.class)
					.add(Restrictions.eq("indicadorByIdIndicadorEntrada",
							indicE))
					.add(Restrictions
							.eq("indicadorByIdIndicadorSalida", indicS)).list();
			IndicadorIndicador indicindic;
			if (i.size() == 0) {
				indicindic = new IndicadorIndicador();
				indicindic.setIndicadorByIdIndicadorEntrada(indicE);
				indicindic.setIndicadorByIdIndicadorSalida(indicS);
				sf.save(indicindic);
				transaction.commit();
			} else {
				log.info("ya esta insertado IndicIndic ALL");
				System.out.println("ya esta insertado IndicIndic ALL");
			}
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<IndicadorIndicador> listarIndicIndic() {
		log.info("getting ListIndicIndic ALL");
		System.out.println("getting ListIndicIndic ALL");
		try {
			return (List<IndicadorIndicador>) sf.createCriteria(
					IndicadorIndicador.class).list();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public IndicadorIndicador listaIndicIndicXId(int idIndInd) {
		log.info("getting ListIndicIndic ALL");
		System.out.println("getting ListIndicIndic ALL");
		try {
			return (IndicadorIndicador) sf
					.createCriteria(IndicadorIndicador.class)
					.add(Restrictions.idEq(idIndInd)).list().get(0);
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Indicador obtenerIndic(int idInd) {
		log.info("getting ListIndicIndic ALL");
		System.out.println("getting ListIndicIndic ALL");
		try {
			return (Indicador) sf.createCriteria(Indicador.class)
					.add(Restrictions.idEq(idInd)).list().get(0);
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public IndicadorIndicador obtenerIndInd(Indicador idIndE, Indicador idIndS) {
		log.info("getting obtenerIndInd ALL");
		System.out.println("getting obtenerIndInd ALL");
		try {
			return (IndicadorIndicador) sf
					.createCriteria(IndicadorIndicador.class)
					.add(Restrictions.eq("indicadorByIdIndicadorEntrada",
							idIndE))
					.add(Restrictions
							.eq("indicadorByIdIndicadorSalida", idIndS)).list()
					.get(0);
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public void crearMatrizAsoc(String user, int idIndInd, int idVarLeng) {
		log.info("getting CreeMatrizAdyUser ALL");
		System.out.println("getting CreeMatrizAdyUser ALL");
		try {
			Transaction transaction = (Transaction) sf.beginTransaction();
			IndicadorIndicador IndInd = (IndicadorIndicador) sf
					.createCriteria(IndicadorIndicador.class)
					.add(Restrictions.idEq(idIndInd)).list().get(0);
			List<MatrizAdyacenciaAsociada> m = sf
					.createCriteria(MatrizAdyacenciaAsociada.class)
					.add(Restrictions.eq("idUsuario", user))
					.add(Restrictions.eq("indicadorIndicador", IndInd)).list();
			VariableLenguistica VarLeng = (VariableLenguistica) sf
					.createCriteria(VariableLenguistica.class)
					.add(Restrictions.idEq(idVarLeng)).list().get(0);
			MatrizAdyacenciaAsociada matriz;
			if (m.size() == 0) {
				matriz = new MatrizAdyacenciaAsociada();
				matriz.setIdUsuario(user);
				matriz.setIndicadorIndicador(IndInd);
				matriz.setVariableLenguistica(VarLeng);
				sf.save(matriz);
			} else {
				matriz = (MatrizAdyacenciaAsociada) sf.load(
						MatrizAdyacenciaAsociada.class, m.get(0)
								.getIdMatrizAdyAsoc());
				matriz.setVariableLenguistica(VarLeng);
				sf.update(matriz);
			}
			transaction.commit();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<MatrizAdyacenciaAsociada> matrizAdyacencAsoc() {
		log.info("getting TeDoyMatrizUsuario ALL");
		System.out.println("getting TeDoyMatrizUsuario ALL");
		try {
			List<MatrizAdyacenciaAsociada> matriz = sf.createCriteria(
					MatrizAdyacenciaAsociada.class).list();
			return matriz;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<MatrizAdyacenciaAsociada> obtenerMatrizAdyacUsuario(String user) {
		log.info("getting obtieneMatrizAdyAsocPor ALL");
		System.out.println("getting obtieneMatrizAdyAsocPor ALL");
		try {
			List<MatrizAdyacenciaAsociada> matriz = sf
					.createCriteria(MatrizAdyacenciaAsociada.class)
					.add(Restrictions.eq("idUsuario", user)).list();
			return matriz;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<MatrizAdyacenciaAsociada> obtenerMatrizAdyacUserIdd(String user,
			IndicadorIndicador idIndInd) {
		log.info("getting obtieneMatrizAdyAsocPor ALL");
		System.out.println("getting obtieneMatrizAdyAsocPor ALL");
		try {
			return sf
					.createCriteria(MatrizAdyacenciaAsociada.class)
					.add(Restrictions.eq("idUsuario", user))
					.add(Restrictions.eq("indicadorIndicador", idIndInd))
					.list();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<MatrizAdyacenciaAsociada> obtenerMatrizAdyacPor(
			IndicadorIndicador idIndInd) {
		log.info("getting obtieneMatrizAdyAsocPor ALL");
		System.out.println("getting obtieneMatrizAdyAsocPor ALL");
		try {
			List<MatrizAdyacenciaAsociada> matriz = sf
					.createCriteria(MatrizAdyacenciaAsociada.class)
					.add(Restrictions.eq("indicadorIndicador", idIndInd))
					.list();
			return matriz;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<MatrizAdyacencia> matrizAdyacenc() {
		log.info("getting matrizAdy ALL");
		System.out.println("getting matrizAdy ALL");
		try {
			List<MatrizAdyacencia> matriz = sf.createCriteria(
					MatrizAdyacencia.class).list();
			return matriz;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public void construirMatriz(IndicadorIndicador idIndicIndic,
			float valorAbsoluto) {
		log.info("getting ConstruirmatrizAdy ALL");
		System.out.println("getting ConstruirmatrizAdy ALL");
		try {
			Transaction transaction = (Transaction) sf.beginTransaction();
			List<MatrizAdyacencia> m = sf
					.createCriteria(MatrizAdyacencia.class)
					.add(Restrictions.eq("indicadorIndicador", idIndicIndic))
					.list();
			MatrizAdyacencia matriz;
			if (m.size() == 0) {
				matriz = new MatrizAdyacencia();
				matriz.setIndicadorIndicador(idIndicIndic);
				matriz.setValorAbsoluto(valorAbsoluto);
				sf.save(matriz);
			} else {
				matriz = (MatrizAdyacencia) sf.load(MatrizAdyacencia.class, m
						.get(0).getIdMatriz());
				matriz.setValorAbsoluto(valorAbsoluto);
				sf.update(matriz);
			}
			transaction.commit();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<VariableLenguistica> listarVarLeng() {
		log.info("getting listaVarLeng ALL");
		System.out.println("getting listaVarLeng ALL");
		try {
			List<VariableLenguistica> listVarLeng = sf.createCriteria(
					VariableLenguistica.class).list();
			return listVarLeng;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public VariableLenguistica obtenerVarLengPorImpacto(String impacto) {
		log.info("getting obtVarLengImp ALL");
		System.out.println("getting obtVarLengImp ALL");
		try {
			return (VariableLenguistica) sf
					.createCriteria(VariableLenguistica.class)
					.add(Restrictions.eq("impacto", impacto)).list().get(0);
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public VariableLenguistica obtenerVarLengPorId(int idLeng) {
		log.info("getting obtVarLengImp ALL");
		System.out.println("getting obtVarLengImp ALL");
		try {
			return (VariableLenguistica) sf
					.createCriteria(VariableLenguistica.class)
					.add(Restrictions.idEq(idLeng)).list().get(0);
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Pregunta> listarPregAsign(Asignatura asign) {
		log.info("getting listarCuest ALL");
		System.out.println("getting listarCuest ALL");
		try {
			List<Pregunta> listarPreg = sf.createCriteria(Pregunta.class)
					.add(Restrictions.eq("asignatura", asign)).list();
			return listarPreg;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Pregunta> cuestPreg(String pregunta, Asignatura asign) {
		log.info("getting listarCuest ALL");
		System.out.println("getting listarCuest ALL");
		try {
			List<Pregunta> listarCuest = sf.createCriteria(Pregunta.class)
					.add(Restrictions.eq("asignatura", asign))
					.add(Restrictions.eq("pregunta", pregunta)).list();
			return listarCuest;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Pregunta pregunta(int idP) {
		log.info("getting Pregunta ALL");
		System.out.println("getting Pregunta ALL");
		try {
			return (Pregunta) sf.createCriteria(Pregunta.class)
					.add(Restrictions.idEq(idP)).list().get(0);

		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Respuesta respuesta(int idR) {
		log.info("getting Pregunta ALL");
		System.out.println("getting Pregunta ALL");
		try {
			return (Respuesta) sf.createCriteria(Respuesta.class)
					.add(Restrictions.idEq(idR)).list().get(0);

		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Respuesta> listaRpta(Pregunta p) {
		log.info("getting listaRpta ALL");
		System.out.println("getting listaRpta ALL");
		try {
			List<Respuesta> lista = sf.createCriteria(Respuesta.class)
					.add(Restrictions.eq("pregunta", p)).list();
			return lista;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public void deleteById(int id) {
		log.info("deleting Operacion instance");
		try {
			sf.beginTransaction();
			Pregunta preg = (Pregunta) sf.load(Pregunta.class, id);
			if (preg != null) {
				List<Respuesta> listaR = this.findRespByIdPreg(preg);
				for (Respuesta respuesta : listaR) {
					this.deleteResultado(respuesta);
					this.deleteResp(respuesta);
				}
				sf.delete(preg);
				sf.getTransaction().commit();
			}
			log.info("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public List<Respuesta> findRespByIdPreg(Pregunta pregunta) {
		log.debug("getting Cuestionario instance with id: ");
		try {
			List<Respuesta> lista = sf.createCriteria(Respuesta.class)
					.add(Restrictions.eq("pregunta", pregunta)).list();
			return lista;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public void deleteResp(Respuesta persistentInstance) {
		log.debug("deleting Respuesta instance");
		try {
			if(persistentInstance!=null)
				sf.delete(persistentInstance);
			log.info("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public void deleteResultado(Respuesta persistentInstance) {
		log.debug("deleting Resultado instance");
		try {
			List<Resultado> r = sf.createCriteria(Resultado.class)
					.add(Restrictions.eq("respuesta", persistentInstance))
					.list();
			if (r != null) {
				for (Resultado resultado : r) {
					sf.delete(resultado);
				}
			}
			log.info("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public void insertarPregunta(Pregunta preg) {
		try {
			Transaction transaction = sf.beginTransaction();
			sf.save(preg);
			transaction.commit();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public void modificarPregunta(Pregunta preg) {
		try {
			Transaction transaction = sf.beginTransaction();
			sf.update(preg);
			transaction.commit();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Pregunta listaPregNomb(String preg, String tipo) {
		log.info("getting listarIndic ALL");
		System.out.println("getting listarIndic ALL");
		try {
			Pregunta pregunt = (Pregunta) sf.createCriteria(Pregunta.class)
					.add(Restrictions.eq("pregunta", preg))
					.add(Restrictions.eq("tipoPregunta", tipo)).list().get(0);
			return pregunt;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public void insertarRespuesta(Respuesta rpta) {
		try {
			Transaction transaction = sf.beginTransaction();
			sf.save(rpta);
			transaction.commit();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public void modificarRespuesta(Respuesta rpta) {
		try {
			Transaction transaction = sf.beginTransaction();
			sf.update(rpta);
			transaction.commit();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public void insertarUsuario(Usuario u) {
		try {
			Transaction transaction = sf.beginTransaction();
			sf.save(u);
			transaction.commit();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public void insertarResultado(Resultado result) {
		try {
			Transaction transaction = sf.beginTransaction();
			sf.save(result);
			transaction.commit();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public void insertarEvaluacion(Evaluacion eval) {
		try {
			Transaction transaction = sf.beginTransaction();
			sf.save(eval);
			transaction.commit();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Usuario> buscarUsuario(String usuario, String nombrusuario) {
		log.debug("deleting Resultado instance");
		try {
			List<Usuario> r = sf.createCriteria(Usuario.class)
					.add(Restrictions.eq("usuario", usuario))
					.add(Restrictions.eq("nombreUsuario", nombrusuario)).list();
			return r;
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
	
	public List<Evaluacion> buscarEvaluacion(Usuario u) {
		log.debug("deleting Resultado instance");
		try {
			List<Evaluacion> r = sf.createCriteria(Evaluacion.class)
					.add(Restrictions.eq("usuario", u)).list();
			return r;
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
		
	public void eliminaResultado(Usuario u) {
		log.debug("deleting Resultado instance");
		try {
			List<Resultado> r = sf.createCriteria(Resultado.class)
					.add(Restrictions.eq("usuario", u)).list();
			if (r != null) {
				for (Resultado resultado : r) {
					sf.delete(resultado);
				}
			}
			log.info("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
	
	public void eliminarEvaluacion(Usuario u) {
		log.debug("deleting eliminarEvaluacion instance");
		try {
			List<Evaluacion> r = sf.createCriteria(Evaluacion.class)
					.add(Restrictions.eq("usuario", u)).list();
			if (r != null) {
				for (Evaluacion evaluac : r) {
					sf.delete(evaluac);
				}
			}
			log.info("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
}