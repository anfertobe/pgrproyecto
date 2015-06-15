# T-Service-Proyecto
commit proyecto conn plantilla propuesta

Descripción del proyecto
T-Service
La creación de la plataforma T-Service se creó con el objetivo de brindar a los usuarios un espacio en donde puedan publicar labores de cualquier índole con el fin de buscar personas capacitadas para que las realicen, también es una oportunidad para aquellos usuarios interesados en realizar labores publicadas en cualquier área. T-Service no sólo se asegura de brindar este canal de comunicación entre usuarios para realizar cualquier labor, también asegura que los usuarios que interacción que los usuarios tienen entre si no desembocará en situaciones negativas y que comprometan la seguridad de los usuarios. 

Integrantes
-	Luis Andrés Gómez Coronell
-	Abdamir Saab Garzón
-	Andrés Fernando Torres Beltrán

URL pública donde se despliega el proyecto (dominio de Heroku) a través de CircleCI
-	http://proyecto-tservise-cosw.herokuapp.com/

Recursos REST disponibles

Obtener todas las entidades por tipo

•	Obtener todos los postulantes
	http://proyecto-tservise-cosw.herokuapp.com/rest/tservice/Postulantes

•	Obtener todos los postulantes
	http://proyecto-tservise-cosw.herokuapp.com/rest/tservice/Publicantes

•	Obtener todas las ofertas
	http://proyecto-tservise-cosw.herokuapp.com/rest/tservice/Ofertas

Obtener todas las entidades por tipo y Id

•	Obtener postulante por id

http://proyecto-tservise-cosw.herokuapp.com/rest/tservice/Postulantes/{idPostulante}


•	Obtener publicante por id

http://proyecto-tservise-cosw.herokuapp.com/rest/tservice/Publicantes/{idPublicante}

•	Obtener oferta por id.

http://proyecto-tservise-cosw.herokuapp.com/rest/tservice/Ofertas/{idOferta}



Agregar todas las entidades por tipo
•	Agregar postulante
http://proyecto-tservise-cosw.herokuapp.com/rest/tservice/Postulantes/
 
Ejemplo
Headers
Content-Type : application/json‏
  	Accept: application/json‏

JSONObject Postulante

{"identificacion":22,"hojaDeVida":{"id":65,"hojaDeVida":"HojaDeVidaPrueba","fechaActualizacion":"FechaActualizacionPrueba","foto":"FotoPrueba"},"aspiracionSalarial":2000000,"nombre":"Abdamir Saab","fechaNacimiento":"2015-03-26","correo":"aa","direccion":"dir","telefono":"1234567","pais":"Colombia","region":"aaa","ciudad":"Bogota","ofertas":[],"ofertas_1":[],"intereses":[],"experienciaLaborals":[]}

•	Agregar publicante
http://proyecto-tservise-cosw.herokuapp.com/rest/tservice/Publicantes/

Ejemplo
Headers

Content-Type : application/json‏

Accept: application/json‏

Json Publicante
{"identificacion":23,"experiencia":"experiencia en mecanica","fechaUltimaLicecia":"2015-03-26","nombre":"Andres","fechaNacimiento":"2015-03-26","direccion":"dir","telefono":"2345678","pais":"Colombia","ragion":"bbbb","ciudad":"Bogota","correo":"aa","facturas":[],"ofertas":[]}

•	Agregar oferta
http://proyecto-tservise-cosw.herokuapp.com/rest/tservice/Ofertas/{id} 

(El id que se solicita es el del publicante)

Ejemplo
Headers

Content-Type : application/json‏

Accept: application/json‏
Json Oferta
{"id":3,"calificacion":null,"postulante":null,"publicante":null,"fechaCreacion":"2015-03-26","fechaFinalizacion":"2015-03-26","valor":2000.0,"descripcion":"cuidar perro","estado":"Disponible","categorias":null,"postulantes":[]}


•	Agregar Empleado oferta

http://proyecto-tservise-cosw.herokuapp.com/rest/tservice/Ofertas/agregarEmpleadoOferta/{idpostulante}/{idoferta}

•	Aplicar oferta posultante
http://proyecto-tservise-cosw.herokuapp.com/rest/tservice/Ofertas/aplicarOferta/{idpostulante}/{idoferta}





