CREATE TABLE asignatura (
  id_asignatura      SERIAL NOT NULL, 
  nombre_asignatura varchar(255), 
  PRIMARY KEY (id_asignatura));
CREATE TABLE indicador (
  id_indicador            SERIAL NOT NULL, 
  nomenclatura_indicador varchar(10), 
  descripcion_indicador  varchar(255), 
  id_asignatura          int4, 
  PRIMARY KEY (id_indicador));
  CREATE TABLE indicador_indicador (
  id_indicador_indicador SERIAL NOT NULL,
  id_indicador_entrada int4 NOT NULL, 
  id_indicador_salida  int4 NOT NULL, 
  PRIMARY KEY (id_indicador_indicador));
CREATE TABLE matriz_adyacencia (
  id_matriz             SERIAL NOT NULL, 
  id_indicador_indicador int4 NOT NULL, 
  valor_absoluto       float4, 
  PRIMARY KEY (id_matriz));
CREATE TABLE variable_lenguistica (
  id_variable_lenguistica  SERIAL NOT NULL, 
  valor_numerico          float4, 
  impacto                 varchar(255), 
  PRIMARY KEY (id_variable_lenguistica));
CREATE TABLE matriz_adyacencia_asociada (
  id_matriz_ady_asoc       SERIAL NOT NULL, 
  id_usuario              varchar(10), 
  id_indicador_indicador    int4 NOT NULL,
  id_variable_lenguistica int4 NOT NULL, 
  PRIMARY KEY (id_matriz_ady_asoc));
CREATE TABLE analisis_estatico (
  id_analisis                SERIAL NOT NULL, 
  id_indicador              int4 NOT NULL, 
  grado_salida              float4, 
  grado_entrada             float4, 
  centralidad               float4, 
  centralidad_normalizada   float4, 
  grado_entrada_normalizado float4, 
  grado_salida_normalizado  float4, 
  PRIMARY KEY (id_analisis));
  CREATE TABLE pregunta (
  id_pregunta    SERIAL NOT NULL, 
  id_asignatura int4, 
  pregunta      varchar(255), 
  tipo_pregunta varchar(50),
  imagen_file varchar(255),  
  PRIMARY KEY (id_pregunta));
  CREATE TABLE respuesta (
  id_respuesta  SERIAL NOT NULL, 
  id_pregunta  int4 NOT NULL, 
  respuesta    varchar(255), 
  peso         float4, 
  PRIMARY KEY (id_respuesta));
CREATE TABLE resultado (
  id_resultado  SERIAL NOT NULL, 
  id_usuario   varchar(10), 
  id_respuesta int4, 
  PRIMARY KEY (id_resultado));

ALTER TABLE matriz_adyacencia_asociada ADD CONSTRAINT FKmatriz_ady875600 FOREIGN KEY (id_variable_lenguistica) REFERENCES variable_lenguistica (id_variable_lenguistica);
ALTER TABLE analisis_estatico ADD CONSTRAINT FKanalisis_e523630 FOREIGN KEY (id_indicador) REFERENCES indicador (id_indicador);
ALTER TABLE indicador_indicador ADD CONSTRAINT FKindicador_182029 FOREIGN KEY (id_indicador_entrada) REFERENCES indicador (id_indicador);
ALTER TABLE indicador_indicador ADD CONSTRAINT FKindicador_532190 FOREIGN KEY (id_indicador_salida) REFERENCES indicador (id_indicador);
ALTER TABLE matriz_adyacencia ADD CONSTRAINT FKmatriz_ady834324 FOREIGN KEY (id_indicador_indicador) REFERENCES indicador_indicador (id_indicador_indicador);
ALTER TABLE matriz_adyacencia_asociada ADD CONSTRAINT FKmatriz_ady335850 FOREIGN KEY (id_indicador_indicador) REFERENCES indicador_indicador (id_indicador_indicador);
ALTER TABLE indicador ADD CONSTRAINT FKindicador344213 FOREIGN KEY (id_asignatura) REFERENCES asignatura (id_asignatura);
ALTER TABLE indicador_indicador ADD CONSTRAINT unico UNIQUE (id_indicador_entrada, id_indicador_salida);
ALTER TABLE pregunta ADD CONSTRAINT FKpregunta903603 FOREIGN KEY (id_asignatura) REFERENCES asignatura (id_asignatura);
ALTER TABLE respuesta ADD CONSTRAINT FKrespuesta285161 FOREIGN KEY (id_pregunta) REFERENCES pregunta (id_pregunta);
ALTER TABLE resultado ADD CONSTRAINT FKresultado667123 FOREIGN KEY (id_respuesta) REFERENCES respuesta (id_respuesta);

INSERT INTO variable_lenguistica(valor_numerico, impacto)  VALUES (-1, 'Negativamente extremadamente importante');
INSERT INTO variable_lenguistica(valor_numerico, impacto)  VALUES (-0.75, 'Negativamente muy fuertemente importante');
INSERT INTO variable_lenguistica(valor_numerico, impacto) VALUES (-0.50, 'Negativamente muy importante');
INSERT INTO variable_lenguistica(valor_numerico, impacto) VALUES (-0.25, 'Negativamente importante');
INSERT INTO variable_lenguistica(valor_numerico, impacto) VALUES (0, 'Sin importancia');
INSERT INTO variable_lenguistica(valor_numerico, impacto) VALUES (0.25, 'Importante');
INSERT INTO variable_lenguistica(valor_numerico, impacto) VALUES (0.50, 'Muy importante');
INSERT INTO variable_lenguistica(valor_numerico, impacto) VALUES (0.75, 'Fuertemente importante');
INSERT INTO variable_lenguistica(valor_numerico, impacto) VALUES (1, 'Extremadamente importante');

INSERT INTO asignatura(nombre_asignatura) VALUES ('Control Automático');

INSERT INTO indicador(nomenclatura_indicador, descripcion_indicador, id_asignatura) VALUES ('I1', 'Fundamentos de los automatismos y métodos de control', 1);
INSERT INTO indicador(nomenclatura_indicador, descripcion_indicador, id_asignatura) VALUES ('I2', 'Capacidad para modelar y simular sistemas', 1);
INSERT INTO indicador(nomenclatura_indicador, descripcion_indicador, id_asignatura) VALUES ('I3', 'Regulación automática y técnicas de control', 1);
INSERT INTO indicador(nomenclatura_indicador, descripcion_indicador, id_asignatura) VALUES ('I4', 'Principio y aplicación de los sistemas robotizados', 1);
INSERT INTO indicador(nomenclatura_indicador, descripcion_indicador, id_asignatura) VALUES ('I5', 'Conocimiento aplicado de informática industrial y comunicaciones', 1);
INSERT INTO indicador(nomenclatura_indicador, descripcion_indicador, id_asignatura) VALUES ('I6', 'Diseño de sistemas de control Automatizado', 1);
INSERT INTO indicador(nomenclatura_indicador, descripcion_indicador, id_asignatura) VALUES ('I7', 'Principio de la regulación automática y sus aplicaciones a la automática industrial', 1);