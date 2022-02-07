package es.android.coches.servicio.implementacion;

import android.content.Context;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import es.android.coches.entidad.Pregunta;
import es.android.coches.repositorio.Repositorio;
import es.android.coches.repositorio.implementacion.RepositorioSQLiteImpl;

public class ServicioPreguntasXMLImpl implements es.android.coches.servicio.interfaz.ServicioPreguntas {
    Context context;

    Repositorio<Pregunta> repositorio;
    List<Pregunta> todasLasPreguntas;
    List<String> todasLasRespuestas;

    public ServicioPreguntasXMLImpl(Context context){

        repositorio= new RepositorioSQLiteImpl(context);
    }

    @Override
    public List<String> generarRespuestasPosibles(String respuestaCorrecta, int numRespuestas) {
        List<String> respuestasPosibles = new ArrayList<>();
        respuestasPosibles.add(respuestaCorrecta);
        List<String> respuestasIncorrectas = new ArrayList<>(
                this.todasLasPreguntas.stream().map(p ->
                        p.getRespuestaCorrecta()).collect(Collectors.toList()));
        respuestasIncorrectas.remove(respuestaCorrecta);
        for(int i=0; i<numRespuestas-1; i++) {
            int indiceRespuesta = new
                    Random().nextInt(respuestasIncorrectas.size());
            respuestasPosibles.add(respuestasIncorrectas.remove(indiceRespuesta));
        }
        Collections.shuffle(respuestasPosibles);
        return respuestasPosibles;


    }


    private Document leerXML(String recurso) throws Exception {
        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
        DocumentBuilder constructor = factory.newDocumentBuilder();
        Document doc = constructor.parse(context.getAssets().open(recurso));
        doc.getDocumentElement().normalize();
        return doc;
    }

    @Override
    public List<Pregunta> generarPreguntas(String recurso) throws Exception {
        todasLasPreguntas = repositorio.getAll();
        return todasLasPreguntas;
    }
}
