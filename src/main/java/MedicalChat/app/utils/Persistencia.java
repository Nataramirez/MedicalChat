package MedicalChat.app.utils;

import MedicalChat.app.modelo.HistoriaClinica;
import MedicalChat.app.modelo.Medico;
import MedicalChat.app.modelo.Paciente;
import MedicalChat.app.modelo.Persona;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Persistencia {
    public void serializarObjeto(String nombre, Object objeto) throws FileNotFoundException {
        XMLEncoder codificador;
        codificador = new XMLEncoder(new FileOutputStream(nombre));
        codificador.writeObject(objeto);
        codificador.close();
    }

    public Object deserializarObjetoXML(String nombre) throws  FileNotFoundException{
        Object obj;
        XMLDecoder decodificador;

        decodificador = new XMLDecoder(new FileInputStream(nombre));
        obj = decodificador.readObject();
        decodificador.close();

        return  obj;
    }
}