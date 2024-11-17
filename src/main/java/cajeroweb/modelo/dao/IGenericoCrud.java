package cajeroweb.modelo.dao;

import java.util.List;

//No van a ser implementados todos estos metodos en todas las clases DAO, pero los pongo y lo desarrollo en cada clase para practicar. Quiza en el futuro loa app pueda crecer y ya estaan.
public interface IGenericoCrud<E, ID> {
    E buscarUno(ID clavePk);
    List<E> buscarTodos();
    E insertUno(E entidad);
    int updateUno(E entidad);
    int deleteUno(ID clavePk);
}
